package org.chous.bets.service.impl;

import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.chous.bets.exception.EmailSendingException;
import org.chous.bets.exception.TokenReceivingException;
import org.chous.bets.service.MailService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class MailServiceImpl implements MailService {

    private static final String SENDER = "BETS - ставки - Чемпионат мира 2026";

    @Value("${mail.host-name}")
    private String hostName;

    @Value("${mail.app-name}")
    private String appName;

    @Value("${mail.password}")
    private String appPassword;

    @Value("${mail.media-type:application/json; charset=utf-8}")
    private String mediaTypeUtf8;

    @Value("${mail.call-timeout}")
    private int callTimeout;

    @Value("${mail.max-retries}")
    private int maxRetries;

    private final OkHttpClient client = new OkHttpClient.Builder()
            .callTimeout(Duration.ofSeconds(callTimeout))
            .build();

    private final ExecutorService executor = Executors.newCachedThreadPool();

    /**
     * Основной метод: получить токен и отправить письмо
     */
    public void send(String to, String subject, String text) {
        getTokenWithRetry(maxRetries)
                .thenCompose(token -> sendMessage(token, to, subject, text))
                .thenRun(() -> log.info("Письмо успешно отправлено на {}", to))
                .exceptionally(ex -> {
                    log.error("Ошибка при отправке письма: {}", ex.getMessage(), ex);
                    return null;
                    //todo нужно, чтобы при ошибке не появлялась страничка с надписью "письмо успешно отправлено"
                    // а появлялась страничка, сообщающая об ошибке и предлогом попробовать ещё раз
                });
    }

    /**
     * Попытка получить токен с retry
     */
    private CompletableFuture<String> getTokenWithRetry(int maxRetries) {
        return CompletableFuture.supplyAsync(() -> {
            for (int attempt = 1; attempt <= maxRetries; attempt++) {
                try {
                    String token = getTokenSync();
                    log.debug("Токен успешно получен (попытка {})", attempt);
                    return token;
                } catch (Exception e) {
                    log.warn("Не удалось получить токен (попытка {}): {}", attempt, e.getMessage());
                    if (attempt == maxRetries) {
                        throw new TokenReceivingException("Ошибка при получении токена после " + maxRetries + " попыток");
                    }
                    try {
                        Thread.sleep(1000L * attempt); // постепенная задержка
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        throw new TokenReceivingException("Поток был прерван во время ожидания получения токена. " + e.getMessage());
                    }
                }
            }
            throw new TokenReceivingException("Не удалось получить токен");
        }, executor);
    }

    /**
     * Получение токена (синхронно, внутри фонового потока)
     */
    private String getTokenSync() throws IOException {
        JSONObject json = new JSONObject()
                .put("login", appName)
                .put("password", appPassword);

        RequestBody requestBody = RequestBody.create(json.toString(), MediaType.get(mediaTypeUtf8));

        Request request = new Request.Builder()
                .url(hostName + "/token")
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Ошибка получения токена: " + response.code());
            }

            ResponseBody body = response.body();
            if (body == null) {
                throw new TokenReceivingException("Пустое тело ответа при получении токена. HTTP: " + response.code());
            }

            String responseBody = body.string();
            JSONObject tokenJson = new JSONObject(responseBody);
            return tokenJson.getString("accessToken");
        }
    }

    /**
     * Отправка письма
     */
    private CompletableFuture<Void> sendMessage(String token, String to, String subject, String text) {
        return CompletableFuture.runAsync(() -> {
            JSONObject email = new JSONObject()
                    .put("clientName", appName)
                    .put("clientMessageId", "none")
                    .put("sender", SENDER)
                    .put("recipients", to)
                    .put("subject", subject)
                    .put("body", text);

            JSONArray jsonArray = new JSONArray().put(email);

            RequestBody body = RequestBody.create(jsonArray.toString(), MediaType.get(mediaTypeUtf8));

            Request request = new Request.Builder()
                    .url(hostName + "/add")
                    .header("Authorization", "Bearer " + token)
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Ошибка при отправке письма: " + response.code());
                }
            } catch (IOException e) {
                throw new EmailSendingException("Не удалось отправить письмо: " + e.getMessage());
            }
        }, executor);
    }
}
