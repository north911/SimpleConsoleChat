package com.touchsoft.server;

import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.BlockingQueue;

public class SocketProcessor implements Runnable {
    private Socket s; // наш сокет
    private BufferedReader br;
    private BufferedWriter bw;
    private ChatUser chatUser;
    private BlockingQueue<SocketProcessor> usersQueue;
    private ClientAgentConnector clientAgentConnector;
    private JSONObject jsonObject;
    private ChatUserRegister chatUserRegister;

    public BufferedWriter getBw() {
        return bw;
    }

    /**
     * Сохраняем сокет, пробуем создать читателя и писателя. Если не получается - вылетаем без создания объекта
     */
    SocketProcessor(Socket socketParam, BlockingQueue<SocketProcessor> usersQueue) throws IOException {
        s = socketParam;
        br = new BufferedReader(new InputStreamReader(s.getInputStream(), "UTF-8"));
        bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), "UTF-8"));
        this.usersQueue = usersQueue;
        clientAgentConnector = new ClientAgentConnector();
        chatUserRegister = new ChatUserRegister();

    }

    SocketProcessor() {

    }

    /**
     * создает "префикс" к каждому сообщению из времени и ника
     */
    private String getPrefix() {
        return "[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "] " + chatUser.getName() + " ";
    }

    public Socket getS() {
        return s;
    }

    /**
     * Главный цикл чтения сообщений/рассылки
     */
    public void run() {
        while (!s.isClosed()) {
            String line = null;
            try {
                line = br.readLine();
                // пробуем прочесть.
            } catch (IOException e) {
                close(usersQueue, this, s); // если не получилось - закрываем сокет.
            }
            //если объект чатюзера еще null то создаем его и тем самым заполняем поле
            //для идентификации потока
            if (chatUser == null) {
                JSONObject jsonObject = new JSONObject(line);
                chatUserRegister.registerChatUser(jsonObject, this);
            } else {
                jsonObject = new JSONObject(line);//преобразуем в json
                line = jsonObject.getString("message");
                if (line.equals("/exit")) {
                    close(usersQueue, this, s);
                } else if ("/leave".equals(line)) {
                    clientAgentConnector.removeAgent(this);
                } else {
                    this.send(getPrefix() + ": " + line, getBw());
                    if (getChatUser().getUserTo() != null) {
                        this.getChatUser().getUserTo().send(getPrefix() + ": " + line, getChatUser().getUserTo().getBw());
                    } else if (this.chatUser.getRole().equals("client")) {
                        clientAgentConnector.tryAssignAgent(usersQueue, this);
                    } else
                        this.send("нет собеседника", getBw());

                }
            }

        }

    }

    /**
     * Метод посылает в сокет полученную строку
     */
    public synchronized void send(String line, BufferedWriter bufferedWriter) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", line);
        try {
            bufferedWriter.write(jsonObject.toString()); // пишем строку
            bufferedWriter.write("\n"); // пишем перевод строки
            bufferedWriter.flush(); // отправляем
        } catch (IOException e) {
            close(usersQueue, this, s); //если глюк в момент отправки - закрываем данный сокет.
        }
    }

    /**
     * метод аккуратно закрывает сокет и убирает его со списка активных сокетов
     */
    synchronized void close(BlockingQueue<SocketProcessor> usersQueue, SocketProcessor socketProcessor, Socket soc) {
        usersQueue.remove(socketProcessor);//убираем из списка
        ServerLogger.writeLog(socketProcessor.getChatUser().getName() + " disconected");
        if (socketProcessor.getChatUser().getUserTo() != null) {
            socketProcessor.getChatUser().getUserTo().send(this.chatUser.getName() + " disconnectet from the chat", socketProcessor.getBw());
            socketProcessor.getChatUser().getUserTo().getChatUser().setUserTo(null);
            socketProcessor.getChatUser().getUserTo().getChatUser().setAvailable(true);
        }
        if (!soc.isClosed()) {
            try {
                soc.close(); // закрываем
            } catch (IOException ignored) {
            }
        }
    }

    public ChatUser getChatUser() {
        return chatUser;
    }

    public void setChatUser(ChatUser chatUser) {
        this.chatUser = chatUser;
    }
}
