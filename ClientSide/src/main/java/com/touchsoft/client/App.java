package com.touchsoft.client;

import org.json.JSONObject;
import java.net.Socket;
import java.io.*;

public class App {
    private final Socket socket;
    private final BufferedReader socketReader;
    private final BufferedWriter socketWriter;
    private final BufferedReader userInput;
    private JSONObject jsonObject;
    private ChatUtils chatUtils;
    private String userString;
    /**
     * Конструктор объекта клиента
     */
    public App() throws IOException {
        final String host = "localhost"; //default
        final int port = 45000; //default
        chatUtils = new ChatUtils();
        userInput = new BufferedReader(new InputStreamReader(System.in));

        jsonObject = chatUtils.registerClient(userInput);

        socket = new Socket(host, port); // создаем сокет
        socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        socketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
        try {
            socketWriter.write(jsonObject.toString());
            socketWriter.write("\n");
            socketWriter.flush();
        } catch (IOException e) {
            chatUtils.close(socket); // в любой ошибке - закрываем.
        }
        new Thread(new Receiver(socket, socketReader,chatUtils)).start(); // создаем и запускаем поток асинхронного чтения из сокета
    }

    /**
     * метод, где происходит главный цикл чтения сообщений с консоли и отправки на сервер
     */
    public void run() {
        System.out.println("Вводите свои сообщения:");
        while (true) {
            userString = null;
            try {
                userString = userInput.readLine();
            } catch (IOException ignored) {
            }
            if (userString == null || socket.isClosed()) {
                chatUtils.close(socket);
                break;
            } else {
               chatUtils.sendMessage(userString,socketWriter);
            }
        }
    }

    /**
     * метод закрывает коннект и выходит из программы
     */


    public static void main(String[] args) {

        try {
            new App().run();
        } catch (IOException e) { // если объект не создан
            System.out.println("Невозможно подключиться");
        }
    }


}