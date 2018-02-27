package com.touchsoft.client;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

public class Receiver implements Runnable {
    /**
     * run() вызовется после запуска потока из конструктора клиента чата.
     */
    private Socket socket;
    private BufferedReader socketReader;
    private JSONObject jsonObject;
    private ChatUtils chatUtils;

    /**
     * конструктор класса
     */
    public Receiver(Socket socket, BufferedReader socketReader, ChatUtils chatUtils) {
        this.socket = socket;
        this.socketReader = socketReader;
        this.chatUtils = chatUtils;
    }

    public void run() {
        while (!socket.isClosed()) { //сходу проверяем коннект.
            String line = null;
            try {
                line = socketReader.readLine();// пробуем прочесть
                if (line != null) {
                    jsonObject = new JSONObject(line);//преобразуем в json
                    line = jsonObject.getString("message");
                }

            } catch (IOException e) { // если в момент чтения ошибка, то

                if ("Socket closed".equals(e.getMessage())) {
                    break;
                }
                System.out.println("Подключение потеряно"); // а сюда мы попадем в случае ошибок сети.
                 chatUtils.close(socket);
            }
            if (line == null) {  // строка будет null если сервер прикрыл коннект по своей инициативе, сеть работает
                System.out.println("Сервер закрыл подключение");
                chatUtils.close(socket);
            } else { // иначе печатаем то, что прислал сервер.
                System.out.println(line);
            }
        }
    }


}
