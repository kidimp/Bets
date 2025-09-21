package org.chous.bets.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chous.bets.exception.EmailSendException;
import org.chous.bets.service.MailService;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
//todo сделать мэйлер

//    private final String HOST_NAME = "https://mailer.pras.by";
//    private final String APP_NAME = "pras.by";
//    private final String SENDER = "BETS - ставки";
//    private final String APP_PASSWORD = "PasswordPrasBy";
//    private final String MEDIA_TYPE_UTF8 = "application/json; charset=utf-8";
//    private volatile String token = "";

//    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//
//
//    public void getToken() {
//        OkHttpClient client = new OkHttpClient();
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("login", APP_NAME);
//        jsonObject.put("password", APP_PASSWORD);
//
//        RequestBody body = RequestBody.create(
//                jsonObject.toString(), MediaType.get(MEDIA_TYPE_UTF8));
//
//        Request request = new Request.Builder()
//                .url(HOST_NAME +"/token")
//                .post(body)
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                logger.error("Failed to get Token: {}", e.getMessage());
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (!response.isSuccessful()) {
//                    logger.error("Failed to get Token. Unexpected code: {}", response);
//                    throw new IOException("Unexpected code " + response);
//                }
//
//                String tokenObject = response.body().string();
//
//                //System.out.println("Response Code: " + response.code());
//                //System.out.println("Response Body: " + tokenObject);
//
//                parseToken(tokenObject);
//                sendMessageToServer();
//            }
//        });
//    }
//
//    public void parseToken(String tokenObject) {
//        token = "";
//
//        JSONObject object = new JSONObject(tokenObject);
//        token = object.get("accessToken").toString();
//    }

//    public void sendMessageToServer() {
//
//    }
//
//    public void send(String to, String subject, String text) {
//        CompletableFuture.runAsync(() -> {
//
//            token = "";
//            getToken();
//            //CompletableFuture.supplyAsync(() -> getToken()).thenAccept(result -> {
//
//            while (token.isEmpty()) {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//
//            OkHttpClient client = new OkHttpClient();
//
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("clientName", APP_NAME);
//            jsonObject.put("clientMessageId", "none");
//            jsonObject.put("sender", SENDER);
//            jsonObject.put("recipients", to);
//            jsonObject.put("subject", subject);
//            jsonObject.put("body", text);
//
//            JSONArray jsonArray = new JSONArray();
//            jsonArray.put(jsonObject);
//
//            RequestBody body = RequestBody.create(
//                    jsonArray.toString(), MediaType.get(MEDIA_TYPE_UTF8));
//
//            Request request = new Request.Builder()
//                    .url(HOST_NAME +"/add")
//                    .header("Authorization", "Bearer " + token)
//                    .post(body)
//                    .build();
//
//            client.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    logger.error("Failed to send email to {}: {}", to, e.getMessage(), e);
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    if (!response.isSuccessful()) {
//                        logger.error("Failed to send email to {}: Unexpected code {}", to, response);
//                        throw new IOException("Unexpected code " + response);
//                    }
//
//                    //System.out.println("Response Code: " + response.code());
//                    //System.out.println("Response Body: " + response.body().string());
//                }
//            });
//
//
//        }).thenRun(() -> System.out.println("Готово!"));
//        /* обработка результата */// });
//    }

    @Override
    public void send(String to, String subject, String text) {

        to = "kidminsk@yandex.ru";
        String from = "kidimpminsk@gmail.com";
        final String username = "kidimpminsk";
        final String password = "jgpdnhecvehsuwdm";

        String host = "smtp.gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));

            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));

            message.setSubject(subject, "UTF-8");

            message.setText(text, "UTF-8");

            Transport.send(message);

            log.info("Message was successfully sent.");

        } catch (MessagingException e) {
            throw new EmailSendException("Не удалось отправить письмо: " + e.getMessage());
        }
    }
}
