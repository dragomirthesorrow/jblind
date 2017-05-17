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
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author oadm
 */
public class Record extends Thread{//Record start in new thread
    private String cam_name;
    private String cam_path;
    String cam_func;
    private String recDir;
    private ProcessBuilder proc;
    Dect dectThr;
    private Process process;
    private int cam_index;
    private boolean auto_sence;

    Record(String name,String path,String func, int index, boolean auto_sence){
        this.cam_name=name;
        this.cam_path=path;
        this.cam_func=func;
        this.recDir=Configuration.getServiceDirectory();
        this.cam_index=index;
        this.auto_sence=auto_sence;
    }
    public void run(){
        Blind.threads_list.add(this);
        startRec();
        
    }
        void restartRec(int cam_index){
            stopRec();
            startRec();
        }
        void startRec(){
        ProcessBuilder proc = new ProcessBuilder("start-stop-daemon","-Sbmp",this.recDir+"service/record/"+this.cam_name+"/pid_record","-x","/usr/bin/ffmpeg","--", "-i",this.cam_path,"-r","24",this.recDir+"service/record/"+this.cam_name+"/record.avi");
        this.proc=proc;
        //Logger.log("Record started for "+this.cam_name);
  long date=new Date().getTime();
                 SimpleDateFormat sqlFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
                 String sqlDate = sqlFormat.format(date);

  
  proc.redirectErrorStream(true);
  
  if(!this.cam_func.equals("record")){
      Dect dect = new Dect(this.cam_name,this.cam_path,this.cam_func,this.cam_index,this.auto_sence);
      this.dectThr=dect;
      dect.start();
  }else{
      SQL upd_time = new SQL();
  upd_time.insertUpdate("update `log_record` set `start_time`='"+sqlDate+"', `finished`='0' where `monitor`='"+this.cam_name+"'");
  Logger.log("Record started for "+this.cam_name+".");
  }
  // запуск программы
try{
          Process process = proc.start();
          this.process=process;
          //Log.log("Record restarting for "+name);
   // читаем стандартный поток вывода
  // и выводим на экран
  InputStream stdout = process.getInputStream();
  InputStreamReader isrStdout = new InputStreamReader(stdout);
  BufferedReader brStdout = new BufferedReader(isrStdout);
  String line = null;
  while((line = brStdout.readLine()) != null) {
   System.out.println(line);
  }
   int exitVal = process.waitFor();
/*if(!this.cam_func.equals("record")){
    process
int exitVal = process.waitFor(interval);}else{
    int exitVal = process.waitFor();
}*/
}catch(IOException | InterruptedException e){//}catch(IOException | InterruptedException e){
      
  }
        
        }
        void stopRec(){
            ProcessBuilder procfin = new ProcessBuilder("start-stop-daemon","-Kp",this.recDir+"service/record/"+this.cam_name+"/pid_record");
            try{
          Process process = procfin.start();
          InputStream stdout = process.getInputStream();
  InputStreamReader isrStdout = new InputStreamReader(stdout);
  BufferedReader brStdout = new BufferedReader(isrStdout);
  String line = null;
  while((line = brStdout.readLine()) != null) {
   System.out.println(line);
  }
   int exitVal = process.waitFor();
   }catch(IOException | InterruptedException e){//}catch(IOException | InterruptedException e){
      
  }
            this.process.destroy();
            this.process=null;
            Logger.log("Stopped record for "+this.cam_name+".");
            SQL upd = new SQL();
            String query = "update `log_record` set `finished`='1' where `monitor`='"+this.cam_name+"'";
            upd.insertUpdate(query);
            //Удаление файла, переименование и тп.
            File old_rec = new File(Configuration.getServiceDirectory()+"/service/record/"+this.cam_name+"/old_record.avi");
            old_rec.delete();
            File new_rec = new File(Configuration.getServiceDirectory()+"/service/record/"+this.cam_name+"/record.avi");
            new_rec.renameTo(new File(Configuration.getServiceDirectory()+"/service/record/"+this.cam_name+"/old_record.avi"));
            File pid = new File(Configuration.getServiceDirectory()+"/service/record/"+this.cam_name+"/pid_record");
            pid.delete();
        }
    }
