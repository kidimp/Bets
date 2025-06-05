package org.chous.bets.service;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Service
public class MailService {
    private final String HOST_NAME = "https://mailer.pras.by";
    private final String APP_NAME = "pras.by";
    private final String SENDER = "BETS - ставки";
    private final String APP_PASSWORD = "PasswordPrasBy";
    private final String MEDIA_TYPE_UTF8 = "application/json; charset=utf-8";
    private volatile String token = "";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    public void getToken() {
        OkHttpClient client = new OkHttpClient();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("login", APP_NAME);
        jsonObject.put("password", APP_PASSWORD);

        RequestBody body = RequestBody.create(
                jsonObject.toString(), MediaType.get(MEDIA_TYPE_UTF8));

        Request request = new Request.Builder()
                .url(HOST_NAME +"/token")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                logger.error("Failed to get Token: {}", e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    logger.error("Failed to get Token. Unexpected code: {}", response);
                    throw new IOException("Unexpected code " + response);
                }

                String tokenObject = response.body().string();

                //System.out.println("Response Code: " + response.code());
                //System.out.println("Response Body: " + tokenObject);

                parseToken(tokenObject);
                sendMessageToServer();
            }
        });
    }

    public void parseToken(String tokenObject) {
        token = "";

        JSONObject object = new JSONObject(tokenObject);
        token = object.get("accessToken").toString();
    }

    public void sendMessageToServer() {

    }

    public void send(String to, String subject, String text) {
        CompletableFuture.runAsync(() -> {

        token = "";
        getToken();
        //CompletableFuture.supplyAsync(() -> getToken()).thenAccept(result -> {

        while (token.isEmpty()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

            OkHttpClient client = new OkHttpClient();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("clientName", APP_NAME);
            jsonObject.put("clientMessageId", "none");
            jsonObject.put("sender", SENDER);
            jsonObject.put("recipients", to);
            jsonObject.put("subject", subject);
            jsonObject.put("body", text);

            JSONArray jsonArray = new JSONArray();
            jsonArray.put(jsonObject);

            RequestBody body = RequestBody.create(
                    jsonArray.toString(), MediaType.get(MEDIA_TYPE_UTF8));

            Request request = new Request.Builder()
                    .url(HOST_NAME +"/add")
                    .header("Authorization", "Bearer " + token)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    logger.error("Failed to send email to {}: {}", to, e.getMessage(), e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        logger.error("Failed to send email to {}: Unexpected code {}", to, response);
                        throw new IOException("Unexpected code " + response);
                    }

                    //System.out.println("Response Code: " + response.code());
                    //System.out.println("Response Body: " + response.body().string());
                }
            });


        }).thenRun(() -> System.out.println("Готово!"));
            /* обработка результата */// });
    }

/*    public void send(String to, String subject, String text) {

//        to = "kidminsk@yandex.ru";
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

//            setAttachment(message, "/Users/pras/Desktop/text.txt");

            Transport.send(message);

            System.out.println("Sent message successfully....");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }*/


//    public static void setAttachment(Message message, String filename) throws MessagingException {
//        message.setText(filename);
//
//        // Создание и заполнение первой части
//        MimeBodyPart p1 = new MimeBodyPart();
//        p1.setText("This is part one of a test multipart e-mail." +
//                "The second part is file as an attachment");
//
//        // Добавление файла во вторую часть
//        FileDataSource fds = new FileDataSource(filename);
//        p1.setDataHandler(new DataHandler(fds));
//        p1.setFileName(fds.getName());
//
//        // Создание экземпляра класса Multipart. Добавление частей сообщения в него.
//        Multipart mp = new MimeMultipart();
//        mp.addBodyPart(p1);
//
//        // Установка экземпляра класса Multipart в качестве контента документа
//        message.setContent(mp);
//    }

}