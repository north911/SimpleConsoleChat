package com.touchsoft.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {

   public static Properties getProperties(){
       Properties prop = new Properties();
       InputStream input = null;
       try {

           String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
           String appConfigPath = rootPath + "Constants.properties";
           prop = new Properties();
           prop.load(new FileInputStream(appConfigPath));

       } catch (IOException ex) {
           ex.printStackTrace();
       } finally {
           if (input != null) {
               try {
                   input.close();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
       }

       return prop;
   }

}

