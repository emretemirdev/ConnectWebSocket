package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.websocket.*;
import org.glassfish.tyrus.client.*;
import java.net.URI;
import java.util.Scanner;
import java.io.File;
import com.fasterxml.jackson.core.JsonProcessingException;

@ClientEndpoint
public class WebSocketClient{

    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("WebSocket connection opened");
        this.session = session;
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("Gelen Mesaj: " + message);
    }

    @OnClose
    public void onClose() {
        System.out.println("WebSocket connection closed");
    }

    public void sendMessage(String message) throws Exception {
        this.session.getBasicRemote().sendText(message);
        File file = new File("src/main/resources/data.json");
        if (file.exists()) {
            System.out.println("data.json dosyası başarıyla gönderildi!");
        } else {
            System.out.println("data.json dosyası gönderilemedi!");
        }
    }

    public static void main(String[] args) throws Exception {
        ClientManager client = ClientManager.createClient();
        WebSocketClient endpoint = new WebSocketClient();
        client.connectToServer(endpoint, URI.create("wss://cekirdektenyetisenler.kartaca.com/ws"));

        // JSON dosyasını String olarak oku
        ObjectMapper mapper = new ObjectMapper();
        String jsonString;
        try {
            jsonString = mapper.readTree(new File("src/main/resources/data.json")).toString();
        } catch (JsonProcessingException e) {
            System.err.println("JSON dosyası okunamadı: " + e.getMessage());
            return;
        }
        // WebSocket üzerinden JSON String veriyi gönder
        endpoint.sendMessage(jsonString);
    }
}
