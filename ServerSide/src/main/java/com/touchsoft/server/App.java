package com.touchsoft.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;


/**
 * Класс сервера. Мониторит порт, принимает сообщение.
 */
public class App {
    private ServerSocket serverSocket; // сам сервер-сокет
    private Thread serverThread; // главный поток обработки сервер-сокета
    //очередь, где хранятся все SocketProcessor-ы для рассылки
    private BlockingQueue<SocketProcessor> usersQueue = new LinkedBlockingQueue<SocketProcessor>();
    ExecutorService ex;

    /**
     * Конструктор объекта сервера
     */
    public App(int port) throws IOException {
        serverSocket = new ServerSocket(port); // создаем сервер-сокет
        System.out.println("Чат-сервер запущен на порту " + port);
        ServerLogger.writeLog("Чат-сервер запущен на порту " + port);
        ex = Executors.newFixedThreadPool(100);
    }

    /**
     * главный цикл прослушивания/ожидания коннекта.
     */
    void run() {
        serverThread = Thread.currentThread(); //сохраняем поток
        while (true) {
            Socket socket = getNewConnection();
            if (serverThread.isInterrupted()) { // если это фейк-соединение, то наш поток был interrupted(),
                break;
            } else if (socket != null) {
                try {
                    SocketProcessor processor = new SocketProcessor(socket, usersQueue);
                    /*Thread thread = new Thread(processor);
                    thread.setDaemon(true); //ставим поток в демона (чтобы не ожидать его закрытия)
                    thread.start();*/
                    ex.submit(processor);
                    usersQueue.offer(processor); //добавляем в список
                } catch (IOException ignored) {
                    ServerLogger.writeLog(ignored.getStackTrace().toString());
                }
            }
        }
    }

    /**
     * Ожидает новое подключение.
     */
    private Socket getNewConnection() {
        Socket s = null;
        try {
            s = serverSocket.accept();
        } catch (IOException e) {
            ServerLogger.writeLog(e.getStackTrace().toString());
        }
        return s;
    }

    /**
     * выключение сервера
     */
    private synchronized void shutdownServer() {
        // обрабатываем список рабочих коннектов, закрываем каждый
        for (SocketProcessor s : usersQueue) {
            s.close(usersQueue, s, s.getS());
        }
        if (!serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException ignored) {
                ServerLogger.writeLog(ignored.getStackTrace().toString());
            }
        }
    }

    /**
     * входная точка программы
     */
    public static void main(String[] args) throws IOException {
        new App(45000).run();

    }

}
