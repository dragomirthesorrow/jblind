/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blind;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author oadm
 */
public class Event extends Thread{//extends Thread{
    //Принимает от дектов
    //Принимает команду на старт события с сччетчиком и с длинной по умолчанию
    //Принимает триггеры от класса Trigger например сигнал от датчика или любого другого устройства или приложения. В дальнейшем
    //для этого будет использоваться отдельный соккет сервер
    //После все хрени передает значения CompileEvent
    private String cam_name;
    private long dur;
    private long start_time_rec;
    private String cam_path;
    boolean proc;
    private int event_num;

    Event(String cam,  long duration){
        Blind.events.add(this);
        this.cam_name=cam;
        this.dur=duration;
        SQL req = new SQL();
        String[] values = new String[1];
        values[0]="start_time";
        this.start_time_rec=Long.parseLong(req.select("select * from `log_record` where `monitor`='"+this.cam_name+"'", values).get(0)[0]);
        SQL event_last=new SQL();
        String[] values1;
        values1 = new String[1];
        values1[0]="id";
        this.event_num=Integer.parseInt(event_last.select("select * from `events` order by `id` desc limit 1", values1).get(0)[0])+1;
        String[] values3 = new String[1];
        values[0]="start_time";
        SQL get_st = new SQL();
        long start_time=Long.parseLong(get_st.select("select * from `log_record` where `monitor`='"+this.cam_name+"'", values3).get(0)[0]);
        
        int wait=0;
        while(wait<duration){
            try{
                Thread.sleep(1000);
                wait++;
            }catch(InterruptedException e){
                
            }
        }
        long start = new Date().getTime()-start_time;
        long date=new Date().getTime();
                 SimpleDateFormat sqlFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
                 String sqlDate = sqlFormat.format(date);
        CompileEvent cp = new CompileEvent((int)start,(int)(start+this.dur),this.cam_name,sqlDate,this.event_num);
        cp.start();
    }
    Event(String cam){
        Blind.events.add(this);
        //если без дьюрации, то начинаем запись как ивента, но добавляем к названию файла user_
        //останавливаем модект
        //потом снова запускаем
        
        this.cam_name=cam;
        Camera to_ev = null;
        for(Camera mon : Blind.cams){
            if(mon.name.equals(this.cam_name)){
                to_ev=mon;
                this.cam_path=mon.path;
            }
        }
        String pervious_func=to_ev.func;
        to_ev.func="monitor";
        to_ev.rec.stopRec();
        //Получаем последний евент и сразу создаем новый
        SQL event_last=new SQL();
        String[] values = new String[1];
        values[0]="id";
        this.event_num=Integer.parseInt(event_last.select("select * from `events` order by `id` desc limit 1", values).get(0)[0])+1;
        long date=new Date().getTime();
                 SimpleDateFormat sqlFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
                 String sqlDate = sqlFormat.format(date);
        this.proc=true;
        SQL new_ev = new SQL();
        new_ev.insertUpdate("insert into `events` (`id`,`start_time`,`end_time`,``monitor`,`unact`) values ('"+this.event_num+"','"+sqlDate+"','null',"+this.cam_name+"','null')");
        //Запускаем прямую запись с камеры
        Logger.log("Event "+this.event_num+" initialized by administrator.");
        
        ProcessBuilder proc = new ProcessBuilder("start-stop-daemon","-Sbmp",Configuration.getRecDirectory()+"service/record/"+this.cam_name+"/pid","-x","/usr/bin/ffmpeg","--", "-i",this.cam_path,Configuration.getRecDirectory()+this.event_num+".avi");
        //this.proc=proc;
        //Logger.log("Record started for "+this.cam_name);



  proc.redirectErrorStream(true);
  Logger.log("Record started for "+this.cam_name+".");
  try{
          Process process = proc.start();
          //this.process=process;
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
        
        to_ev.func=pervious_func;
        
    }
    public void run(){
        
        //Logger.log("Event is ready.");
    }
    void make(){
        
    }
    void deleteEvents(){
        
    }
    void stopEvent(){
        long date=new Date().getTime();
                 SimpleDateFormat sqlFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
                 String sqlDate = sqlFormat.format(date);
        ProcessBuilder procfin = new ProcessBuilder("start-stop-daemon","-Kp",Configuration.getRecDirectory()+"service/record/"+this.cam_name+"/pid");
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
   SQL upd_ev = new SQL();
   upd_ev.insertUpdate("update `events` set `end_time`='"+sqlDate+"' where `id`='"+this.event_num+"'");
   Logger.log("Event "+this.event_num+" stoped by administrator.");
   }catch(IOException | InterruptedException e){//}catch(IOException | InterruptedException e){
      
  }
    }
}
