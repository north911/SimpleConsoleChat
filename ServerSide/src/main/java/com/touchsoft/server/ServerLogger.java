package com.touchsoft.server;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class ServerLogger {

    public static void writeLog(String info) {

        try (OutputStream out = new BufferedOutputStream(
                Files.newOutputStream(Paths.get("log.txt"), CREATE, APPEND))) {

            out.write(("[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "] " + info + "\n").getBytes("UTF-8"));
            // out.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
