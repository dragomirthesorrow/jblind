/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blind;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author oadm
 */
public class Camera extends Thread{
    //there will methods to start subclass method record, to restart it or to stop, but at the 
    //beginning start to construct all variables for Cam-object
    String name;
    String path;
    String func;
    private int index;
    String command;
    Record rec;
    boolean reachable=true;
    boolean auto_sence;
    
    public Camera(int cam_index){
    this.index=cam_index;
    this.name=Blind.camera_list.get(cam_index)[0];
    this.path=Blind.camera_list.get(cam_index)[1];
    this.func=Blind.camera_list.get(cam_index)[2];
    
    
            if(Integer.parseInt(Blind.camera_list.get(cam_index)[3])==1){
                this.auto_sence=true;
            }else if(Integer.parseInt(Blind.camera_list.get(cam_index)[3])==0){
                this.auto_sence=false;
            }
    this.command="";
    }
    public void run(){
        //while(Blind.running){
        Blind.threads_list.add(this);
        //set("blblblb");
        if(!this.func.equals("monitor")){
            Record rec = new Record(this.name,this.path,this.func,this.index,this.auto_sence);
            this.rec=rec;
            rec.start();
            
            while(Blind.running){
                //ping
                if(this.reachable==false){
                    Logger.log("Device "+this.name+" is unreachable.");
                    this.rec.stopRec();
                    //this.rec=null;
                    while(this.reachable==false){
                        System.out.println("Device "+this.name+" is unreachable.");
                        try{
                        Thread.sleep(30000);
                        }
                        catch(InterruptedException e){
                            Logger.log(e.toString());
                        }
                    }
                    //Record rec1 = new Record(this.name,this.path,this.func,this.index);
                    //this.rec=rec1;
                    this.rec.startRec();
                    
                }
                if(this.command.length()!=0){
                    set(this.command);
                    System.out.println(this.command);
                    this.command="";
                }else{
                    continue;
                }
            }
        }else if(this.func.equals("monitor")){
            while(Blind.running){
                //System.out.println(this.command);
                try{
                    Thread.sleep(10000);
                }catch(InterruptedException e){
                    
                }
                if(this.command.length()!=0){
                   // System.out.print(this.command);
                    set(this.command);
                   // System.out.print(this.command);
                    this.command="";
                }else{
                    continue;
                }
            }
        }
        //}
        
    }
    void set(String com){
        //System.out.println(com);
        String full_com=com.substring(14);
        System.out.println(full_com);
        String action = full_com.substring(0,3);
        ArrayList<Integer> pos = new ArrayList<>();
        int sum=0;
        char[] arr = full_com.toCharArray();
        for(int i=0; i<arr.length; i++){
            if(arr[i]=='='){sum=i;}
            if(arr[i]==' '){pos.add(i);}
        }
        String param = full_com.substring(pos.get(0)+1,sum);
        String param_value = full_com.substring(sum+1);
        
        //System.out.println(action);
        //System.out.println(param);
        //System.out.println(param_value);
        
        
       if(action.equals("set")){
           if(param.equals("func")){
               if(this.func.equals("monitor")){
                   this.func=param_value;
                   SQL upd=new SQL();
                   upd.insertUpdate("update `monitors` set `func`='"+this.func+"' where `name`='"+this.name+"'");
                   Blind.camera_list.get(index)[2]=this.func;
                this.rec=new Record(this.name,this.path,this.func,this.index,this.auto_sence);
                this.rec.start();
                //this.rec.startRec();
                Logger.log("For "+this.name+" was func "+this.func+" setted.");
                
            }else{
                   this.func=param_value;
                   if(this.func.equals("monitor")){
                       Blind.camera_list.get(index)[2]=this.func;
                       SQL upd=new SQL();
                   upd.insertUpdate("update `monitors` set `func`='"+this.func+"' where `name`='"+this.name+"'");
                       this.rec.dectThr.stop();
                       this.rec.stopRec();
                       //this.rec.stop();
                       
                       this.rec=null;
                       Logger.log("For "+this.name+" was func "+this.func+" setted.");
                   }else{
                       Blind.camera_list.get(index)[2]=this.func;
                       this.rec.cam_func=this.func;
                       SQL upd=new SQL();
                   upd.insertUpdate("update `monitors` set `func`='"+this.func+"' where `name`='"+this.name+"'");
                      this.rec.restartRec(this.index); 
                      Logger.log("For "+this.name+" was func "+this.func+" setted.");
                   }
                   
               }
           }else{
               if(this.func.equals("monitor")){
                   System.out.print("update `monitors` set `"+param+"`='"+param_value+"' where `name`='"+this.name+"'");
                    SQL sql = new SQL();
                    sql.insertUpdate("update `monitors` set `"+param+"`='"+param_value+"' where `name`='"+this.name+"'");
           
                    Logger.log("For "+this.name+" was changed parameter: "+param+"="+param_value+" setted.");
               }else{
                    System.out.print("update `monitors` set `"+param+"`='"+param_value+"' where `name`='"+this.name+"'");
                    SQL sql = new SQL();
                    sql.insertUpdate("update `monitors` set `"+param+"`='"+param_value+"' where `name`='"+this.name+"'");
                    //и обязательно рестарт записи для объекта!!!!!!!!!!!!!!!!!!!!!!!!!
                    Logger.log("For "+this.name+" was changed parameter: "+param+"="+param_value+" setted.");
                    this.rec.restartRec(this.index);
               }
           }
        }
        
        }
    
    
}
