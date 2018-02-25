package com.touchsoft.client;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;

public class ChatUtils {

    public JSONObject registerClient(BufferedReader userInput) {

        String input;
        String[] massiveInput;
        JSONObject jsonObject = new JSONObject();
        try {
            System.out.println("для регистрации введите /register затем свою роль и имя");
            input = userInput.readLine();
            massiveInput = input.split(" ");

            while (massiveInput.length != 3 && !massiveInput[0].equals("/register")
                    || (!massiveInput[1].equals("agent") && !massiveInput[1].equals("client"))) {
                System.out.println("ошибка ввода");
                input = userInput.readLine();
                massiveInput = input.split(" ");
            }
            // nickname = massiveInput[2];
            jsonObject.put("name", massiveInput[2]);
            jsonObject.put("role", massiveInput[1]);

        } catch (IOException ignored) {
            ignored.printStackTrace();
        }

        return jsonObject;
    }


    public synchronized void sendMessage(String line, BufferedWriter socketWriter) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", line);
        try {
            socketWriter.write(jsonObject.toString()); // пишем строку
            socketWriter.write("\n"); // пишем перевод строки
            socketWriter.flush(); // отправляем
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void close(Socket curSocket) {//метод синхронизирован, чтобы исключить двойное закрытие.
        if (!curSocket.isClosed()) { // проверяем, что сокет не закрыт...
            try {
                curSocket.close(); // закрываем...
                System.exit(0); // выходим!
            } catch (IOException ignored) {
                ignored.printStackTrace();
            }
        }
    }
}
