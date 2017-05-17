/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blind;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author oadm
 */
public class Dect extends Thread{
    private String name;
    private String path;
    private String func;
    private int interval;
    private int cam_index;
    private long started_sql;
    private int diff;
    private boolean auto_sence;
    Modect modect;
    //boolean bad_src1=false;
    //boolean bad_src2;
    Dect(String name,String path,String func, int index, boolean auto_sence){
        this.name=name;
        this.path=path;
        this.func=func;
        this.interval=Configuration.getInterval();
        this.cam_index=index;
        this.auto_sence=auto_sence;
    }
    public void run(){
        Blind.threads_list.add(this);
        Logger.log(this.func+" was started on "+this.name+".");
        while(Blind.running){
            if(this.func.equals("modect")){
                //Добавить стоп в случае недоступности камеры и вэйт до аксеса
                modect();
            }else if(this.func.equals("hudect")){
                hudect();
            }else if(this.func.equals("behdect")){
                behdect();
            }
        }
        
    }
    public void modect(){
        File fl = new File(Configuration.getServiceDirectory()+"service/record/"+this.name+"/record.avi");
        if(fl.isFile()){
                
            }else{
                while(!fl.isFile()){
                   // System.out.println("no file for: "+this.name);
                }
            }
        
            long dateUp=new Date().getTime();
                SimpleDateFormat sqlFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
                String sqlDate = sqlFormat.format(dateUp);
                SQL upd_time = new SQL();
                upd_time.insertUpdate("update `log_record` set `start_time`='"+sqlDate+"', `finished`='0' where `monitor`='"+this.name+"'");
                Logger.log("Record started for "+this.name+".");
        String[] requested_value= new String [1];
            requested_value[0]="start_time";
            SQL get_start = new SQL();
            String geted_start=get_start.select("select * from `log_record` where `monitor`='"+this.name+"'", requested_value).get(0)[0];
            SimpleDateFormat sd = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
            Date date = new Date();
        try{
            Date started = sd.parse(geted_start);
            started.setDate(date.getDate());
            started.setMonth(date.getMonth());
            
            this.started_sql = started.getTime();
        }catch(ParseException e){
            System.out.println(e);
        }
            this.diff = (int)(date.getTime()-started_sql)/1000;

            Modect md = new Modect(this.name);
            this.modect=md;
            
            md.getImage1();
            
            try{
                Thread.sleep(1000);
                } catch (InterruptedException e){
                    
                }
            while(diff<=this.interval*1000){
                //System.out.println(diff);
                
                //if(this.bad_src1==true){
                 //   md.getImage1();
                //}else if(this.bad_src1==false){
                try{
                Thread.sleep(1000);
                } catch (InterruptedException e){
                    
                }
               md.getImage2(this.diff);
               md.presetImages();
                
               md.compareImages();
               //System.out.println(this.diff);
                
                long now = new Date().getTime();
                this.diff= (int)(now - started_sql)/1000;
            //}
            
            }
            Blind.cams.get(cam_index).rec.restartRec(cam_index);
    }
    public void hudect(){
        
    }
    public void behdect(){
        
    }
}
