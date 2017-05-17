/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blind;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author oadm
 */
public class CompileEvent {
    /*Разбираемся в длинне записи, далее если все в текущей записи выдергиваем кусок,
    если нет - выдергиваем и из старой и склеиваем. 
    Затем читаем куда сохранять файл из конфига и сохраняем, добавляя событие в базу данных.
    
    */
    private int start;
    private int end;
    private int current_durency;
    private long old_durency;
    private String event;
    private String current_record;
    private String old_record;
    private String device;
    private int num_event;
    private boolean combine;
    private int buffer;
    CompileEvent(int start, int end, String device,String end_time,int nuevent){
        
        //В констуркторе выставляем все необходимые поля, затем стартуем саму работу с ffmpeg.
        this.num_event=nuevent;
        SQL update = new SQL();
        update.insertUpdate("update `events` set `end_time`='"+end_time+"' where `id`='"+this.num_event+"'");
        this.device=device;
        this.buffer=Configuration.getBuffer();
        this.start=start-5;
        this.end=end;
        this.event=Configuration.getRecDirectory()+num_event+".avi";
        if(this.start>this.end){
            this.combine=true;
            this.current_record=Configuration.getServiceDirectory()+"service/record/"+device+"/record.avi";
            this.old_record=Configuration.getServiceDirectory()+"service/record/"+device+"/old_record.avi";
            this.old_durency=Configuration.getInterval()-start;
            this.current_durency=this.end+5;
        }else{
            this.combine=false;
            this.current_record=Configuration.getServiceDirectory()+"service/record/"+device+"/record.avi";
            this.current_durency=this.end-this.start+5;
        }
    }
    void start(){
        //теперь собираем событие из исходных данных.
        if(this.combine==true){//если событие в двух файлах
            
                    
            ProcessBuilder pb1 = new ProcessBuilder("ffmpeg","-ss",Integer.toString(this.start),"-t",Integer.toString((int)this.old_durency),"-i",this.old_record,Configuration.getRecDirectory()+"1_"+this.num_event+".avi").redirectOutput(ProcessBuilder.Redirect.INHERIT);
            pb1.redirectErrorStream(true);
            try{
                Process process = pb1.start();
  
                // читаем стандартный поток вывода
                // и выводим на экран
                InputStream stdout = process.getInputStream();
                InputStreamReader isrStdout = new InputStreamReader(stdout);
                BufferedReader brStdout = new BufferedReader(isrStdout);
                String line = null;
                    while((line = brStdout.readLine()) != null) {
                        //System.out.println(line);
                    }
                int exitVal = process.waitFor();
                }catch(IOException | InterruptedException e){
                    System.out.println(e);
                    Logger.log(e.toString());
                }
            ProcessBuilder pb2 = new ProcessBuilder("ffmpeg","-ss","0","-t",Integer.toString((int)this.end),"-i",this.current_record,Configuration.getRecDirectory()+"2_"+this.num_event+".avi").redirectOutput(ProcessBuilder.Redirect.INHERIT);
            pb2.redirectErrorStream(true);
            try{
                Process process = pb2.start();
  
                // читаем стандартный поток вывода
                // и выводим на экран
                InputStream stdout = process.getInputStream();
                InputStreamReader isrStdout = new InputStreamReader(stdout);
                BufferedReader brStdout = new BufferedReader(isrStdout);
                String line = null;
                    while((line = brStdout.readLine()) != null) {
                        //System.out.println(line);
                    }
                int exitVal = process.waitFor();
                }catch(IOException | InterruptedException e){
                    System.out.println(e);
                    Logger.log(e.toString());
                }
            //склеиваем события
            ProcessBuilder pb3 = new ProcessBuilder("ffmpeg","-i","concat:","\""+Configuration.getRecDirectory()+"1_"+this.num_event+".avi|"+Configuration.getRecDirectory()+"2_"+this.num_event+"\"","-c","copy",Configuration.getRecDirectory()+this.num_event+"");
            pb3.redirectErrorStream(true);
            try{
                Process process = pb3.start();
                InputStream stdout = process.getInputStream();
                InputStreamReader isrStdout = new InputStreamReader(stdout);
                BufferedReader brStdout = new BufferedReader(isrStdout);
                String line = null;
                    while((line = brStdout.readLine()) != null) {
                        //System.out.println(line);
                    }
                int exitVal = process.waitFor();
            } catch(IOException | InterruptedException e){
                System.out.println(e);
                Logger.log(e.toString());
            }
        }else if(this.combine==false){//если событие в одной записи
            ProcessBuilder pb = new ProcessBuilder("ffmpeg","-ss",Integer.toString((int)this.start),"-t",Integer.toString((int)this.current_durency),"-i",this.current_record,this.event).redirectOutput(ProcessBuilder.Redirect.INHERIT);
            pb.redirectErrorStream(true);
            try{
                Process process = pb.start();
  
                // читаем стандартный поток вывода
                // и выводим на экран
                InputStream stdout = process.getInputStream();
                InputStreamReader isrStdout = new InputStreamReader(stdout);
                BufferedReader brStdout = new BufferedReader(isrStdout);
                String line = null;
                    while((line = brStdout.readLine()) != null) {
                        //System.out.println(line);
                    }
                int exitVal = process.waitFor();
                }catch(IOException | InterruptedException e){
                    System.out.println(e);
                    Logger.log(e.toString());
                }
        }
        
        
    }
    protected void finalize(){
        //после сборки все зачищаем.
        
        
    }
}
