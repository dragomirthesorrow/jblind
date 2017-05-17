/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blind;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author oadm
 */
public class Logger {
    public static void log(String string){
        check();
        try{
            
            SimpleDateFormat formatd = new SimpleDateFormat("YYYY MM DD HH:mm:ss");
            String root = Configuration.getServiceDirectory();
        File log_txt = new File(root+"log/blind.log");
        String date= formatd.format(new Date());
        String new_text = date+" "+string+"\n";
        if(log_txt.isFile()){
            
            //appendUsingFileWriter
            //PrintWriter out = new PrintWriter(log_txt.getAbsoluteFile());
 
        try {
            FileOutputStream os;
            os = new FileOutputStream(log_txt, true);
            os.write(new_text.getBytes(), 0, new_text.length());
            //Записываем текст у файл
            //out.print(date+log);
        } finally {
            //После чего мы должны закрыть файл
            //Иначе файл не запишется
            //log_txt.close();
        }
        }else{
            log_txt.createNewFile();
            //PrintWriter out = new PrintWriter(log_txt.getAbsoluteFile());
 
        try {
                //Записываем текст у файл
                //File log_txt = new File("/var/www/html/blind.log");
                FileOutputStream os = new FileOutputStream(log_txt, true);
            os.write(new_text.getBytes(), 0, new_text.length());
            //out.print(date+log);
        } finally {
            //После чего мы должны закрыть файл
            //Иначе файл не запишется
            //out.close();
        }
     
    
        }}catch(IOException e){
            System.out.println("Ошибка логирования: "+e);
        }
    }
    public static void check(){
        File log = new File(Configuration.getServiceDirectory()+"log/blind.log");
        BasicFileAttributes attr;
        Path path = log.toPath();
        try{
            int today = new Date().getDay();
        attr = Files.readAttributes(path, BasicFileAttributes.class);
            FileTime created = attr.creationTime();
            int lc = new Date(created.toMillis()).getDay();
            if(today!=lc){
                String logg = Integer.toString(today-1);
                log.renameTo(new File(Configuration.getServiceDirectory()+"log/lc_"+logg+".log"));
                //File new_log= new File("Configuration.getServiceDirectory()+\"log/blind.log");
                //new_log.
            }
        }
        catch(IOException e){
            
        }
    }
}
