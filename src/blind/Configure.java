/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blind;

import java.io.File;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author oadm
 */
public class Configure {
    private boolean complex_command=false;
    private String string;
    private String object_to_act;
    private String object_action;
    private String object_action_param;
    private boolean correct=true;
    private String name_object;
    private String add_comand;
    
    

    Configure(String s){
        //Здесь разбираем строку на составные части и запускаем соответствующую команду. Принимает из сервера и юзеркли
        this.string=s;
        if(s.equals("help")||s.equals("exit")||s.equals("stop")||s.equals("s")||s.substring(0,6).equals("camera")||s.substring(0,5).equals("event")){
            
            
            this.correct=true;
            ArrayList<Integer> spaces_pos = new ArrayList<>();

            char[] a=s.toCharArray();
            for(int n=0;n<s.length();n++){
                if(a[n]==' '){
                    spaces_pos.add(n);
                }
            }
        
        //System.out.println(spaces_pos.size());
            if(spaces_pos.size()!=0){
                
                this.object_to_act=s.substring(0,spaces_pos.get(0));
                    
                if(this.object_to_act.equals("camera")){
                    if(s.substring(spaces_pos.get(0)+1,spaces_pos.get(0)+1+3).equals("add")){
                        //this.object_to_act="camera";
                        this.complex_command=true;
                        this.object_action="add";
                        this.add_comand=s.substring(spaces_pos.get(0)+1+4);

                    }else if(s.substring(spaces_pos.get(0)+1,spaces_pos.get(0)+1+3).equals("del")){
                        this.complex_command=true;
                        this.name_object=s.substring(spaces_pos.get(1)+1,spaces_pos.get(1)+1+6);
                        this.object_action="del";
                    }else{        
                        this.name_object=s.substring(spaces_pos.get(0)+1,spaces_pos.get(1)-1);
                        this.object_action=s.substring(spaces_pos.get(1)+1,spaces_pos.get(2)-1);
                        int set_val=0;
                        for(int z=0;z<a.length;z++){
                            if(a[z]=='='){
                                set_val=z;
                            }
                        }
                        this.object_action_param=s.substring(spaces_pos.get(2)+1,set_val-1);
                        this.object_action_param=s.substring(set_val+1);
                        this.complex_command=true;}
                }else if(this.object_to_act.equals("event")){
                    
                        int set_val=0;
                        for(int z=0;z<a.length;z++){
                            if(a[z]=='='){
                                set_val=z;
                            }
                        }
                        this.name_object=s.substring(spaces_pos.get(0)+1,spaces_pos.get(1)-1);
                        this.object_action=s.substring(spaces_pos.get(1)+1,set_val);
                            //this.object_action_param=s.substring(spaces_pos.get(2)+1,set_val-1);
                        this.object_action_param=s.substring(set_val+1);
                        this.complex_command=true;
                }
                

                    
            }else{
                    // System.out.println("Not complex");
                    this.complex_command=false;
                }
        }else{
            this.correct=false;
        }
    }
    public String set(){
        String x=this.string;
        //System.out.println("Configure: "+x);
        if(!this.correct){
            System.out.println("Command you`ve entered is incorrect! "+x.substring(0,5));
            printHelp();
            return x;
        }else{
        
        if(complex_command){
            //Разбираем по объектам
            //System.out.println(this.object_to_act);
            if(this.object_to_act.equals("camera")){
                //Разбираем по индексу девайсены
                //System.out.println("camera...");
                int ind_obj=0;
                for(int z=0;z<Blind.cams.size();z++){
                    if(Blind.cams.get(z).name==this.name_object){ind_obj=z;}
                }
                //System.out.println(ind_obj);
               // System.out.println(Blind.cams.get(ind_obj).command);
               //System.out.println(this.object_action);
               if(this.object_action.equals("del")){
                   if(Blind.cams.get(ind_obj).func.equals("monitor")){
                       Blind.cams.get(ind_obj).stop();
                       Blind.cams.remove(ind_obj);
                       Blind.camera_list.remove(ind_obj);
                       SQL del = new SQL();
                       del.insertUpdate("delete from `monitors` where `name`='"+this.name_object+"'");
                       del.insertUpdate("delete from `log_record` where `monitor`='"+this.name_object+"'");
                       del.insertUpdate("update `events` set `monitor`='deleted' where `monitor`='"+this.name_object+"'");
                       File path = new File(Configuration.getServiceDirectory()+"service/record/"+this.name_object);
                       path.delete();
                       Logger.log("Camera "+this.name_object+" was deleted by user.");
                   }else{
                       Blind.cams.get(ind_obj).rec.stopRec();
                       Blind.cams.get(ind_obj).rec=null;
                       Blind.cams.get(ind_obj).stop();
                       Blind.cams.remove(ind_obj);
                       Blind.camera_list.remove(ind_obj);
                       SQL del = new SQL();
                       del.insertUpdate("delete from `monitors` where `name`='"+this.name_object+"'");
                       del.insertUpdate("delete from `log_record` where `monitor`='"+this.name_object+"'");
                       del.insertUpdate("update `events` set `monitor`='deleted' where `monitor`='"+this.name_object+"'");
                       File path = new File(Configuration.getServiceDirectory()+"service/record/"+this.name_object);
                       path.delete();
                       Logger.log("Camera "+this.name_object+" was deleted by user.");
                   }
               }else if(this.object_action.equals("add")){
                   //System.out.println("Add... ");
                   //System.out.println(this.add_comand);
                   char[] add = this.add_comand.toCharArray();
                   ArrayList<Integer> prob=new ArrayList<>();
                   for(int p=0;p<add.length;p++){
                       if(add[p]==' '){
                           prob.add(p);
                       }
                   }
                   String name=this.add_comand.substring(0,prob.get(0));
                   String path=this.add_comand.substring(prob.get(0)+1,prob.get(1));
                   String func=this.add_comand.substring(prob.get(1)+1,prob.get(2));
                   String sence=this.add_comand.substring(prob.get(2)+1,prob.get(3));
                   String auto_sence=this.add_comand.substring(prob.get(3)+1);
                   System.out.println(name);
                   System.out.println(path);
                   System.out.println(func);
                   System.out.println(sence);
                   System.out.println(auto_sence);
                   int auto=0;
                   if(auto_sence.equals("true")){
                       auto=1;
                   }else{
                       auto=0;
                   }
                   long date=new Date().getTime();
                 SimpleDateFormat sqlFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
                 String sqlDate = sqlFormat.format(date);
                   SQL new_cam =new SQL();
                   new_cam.insertUpdate("insert into `monitors` (`name`,`path`,`func`,`sence`,`auto_sence`) values ('"+name+"','"+path+"','"+func+"','"+sence+"','"+auto+"')");
                   new_cam.insertUpdate("insert into `log_record` (`id_monitor`,`pid`,`start_time`,`finished`,`monitor`) values ('0','0','"+sqlDate+"','1','"+name+"')");
                   Camera cam = new Camera(Blind.cams.size()-1);
                   Blind.cams.add(cam);
                   cam.start();
                   
               }else{
                   //System.out.println("For cam");
                Blind.cams.get(ind_obj).command=x;}
               // System.out.println(Blind.cams.get(ind_obj).name+Blind.cams.get(ind_obj).func+Blind.cams.get(ind_obj).command);
            }else if(this.object_to_act.equals("event")){
                //System.out.println("event...");
                //System.out.println(this.object_action);
                //System.out.println(this.object_action_param);
                //Разбираем параметры и передаем в Event
                if(this.object_action.equals("dur")){
                    Event ev = new Event(this.name_object,Long.parseLong(this.object_action_param));
                    ev.start();
                    //System.out.println("Event s dur");
                }else if(this.object_action.equals("event")){
                    if(this.object_action_param.equals("start")){
                    Event ev = new Event(this.name_object);
                    Blind.events.add(ev);
                    ev.start();}
                    else if(this.object_action_param.equals("stop"))
                    {
                        Blind.events.get(0).stopEvent();
                    }
                    //System.out.println("STart/stop event");
                }
                
            }
            
            
        }else{
            if(x.equals("help")){
            //Вывести помощь ввода команд
            printHelp();
            }
            else if(x.equals("exit") || x.equals("stop") || x.equals("s")){
                
                Blind.running=false;
                
                for(Camera cam:Blind.cams){
                    try{
                    cam.rec.stopRec();
                    }catch(NullPointerException e){
                        
                    }
                }
                for(Thread thr : Blind.threads_list){
                    //System.out.println(thr);
                    thr.interrupt();
                    thr.stop();
                }
                
                try{
                String host = "localhost";
                Socket fromserver = new Socket(host,4444);
                PrintWriter out = new
                        PrintWriter(fromserver.getOutputStream(), true);
                out.println("");
                out.println("");
                }catch(Exception e){

                }
                Blind.running=false;
                Blind.close();
            }else{printHelp();}
        //Logger.log(x);
        return x;
    }
        
    }return x;}
    public void printHelp(){
        System.out.println("Your syntax should be like:");
        System.out.println("action(stop(s,exit),help(h)");
        System.out.println("or");
        System.out.println("object_to_change name_object action(set)=param ");
        System.out.println("Like: camera Cam_01 set func=modect");
        System.out.println("Like: \"camera add Cam_0X rtsp://video:123456@192.168.1.1:7070 monitor 10 true\" monitor/modect/hudect/behdect int is sence");
        System.out.println("Like: camera del Cam_0X");
        System.out.println("Like: event Cam_01 dur=25 or event Cam_01 event=start/stop");
        System.out.println("all parameters: func,path,on_secure");
    }
}
