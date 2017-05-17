/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blind;

import java.util.ArrayList;

/**
 *
 * @author oadm
 * Позволяет автоматически менять чувствительноть, забирая значения компаратора в течение 30 секунд, если значения отличаются
 * меньше чем на 2 пункта в течение 15-ти секунд, то меняет чувствительность
 */
public class AutoSence extends Thread{
    private String cam_name;
    ArrayList<Integer> sence_arr;
    private int current_sence;
    private int depth_arr=30;//Это 30 секунд
    
    AutoSence(String name_device){
        this.cam_name=name_device;
        this.sence_arr=new ArrayList();
        String[] values=new String[1];
        values[0]="sence";
        SQL sn = new SQL();
        this.current_sence=Integer.parseInt(sn.select("select * from `monitors` where `name`='"+this.cam_name+"'", values).get(0)[0]);
        Blind.threads_list.add(this);
    }
    public void run(){
        int new_sence=0;
        while(Blind.running){
            while(this.sence_arr.size()<this.depth_arr){
                continue;
            }
            int count=0;
            for(int z=0;z<this.sence_arr.size();z++){
                int dif = this.sence_arr.get(z)-this.sence_arr.get(z+1);
                if(dif<2 || dif>2){
                    count++;
                }else{
                    count=0;
                }
                if(count>15){
                    //Запрашиваем нынешнюю сенсе и если отличие менее 2-х, то ничего не меняем, если же более, то передаем новое значение
                    if(this.current_sence-this.sence_arr.get(z)<2){
                    int cam_ind=0;
                    for(int c=0;c<Blind.cams.size();c++){
                        if(Blind.cams.get(c).name.equals(this.cam_name)){
                            cam_ind=c;
                        }
                        
                        Blind.cams.get(cam_ind).rec.dectThr.modect.max_diff=this.sence_arr.get(z);
                        this.current_sence=this.sence_arr.get(z);
                        SQL newd = new SQL();
                        newd.insertUpdate("update `monitors` set `sence`='"+this.sence_arr.get(z)+"' where `name`='"+this.cam_name+"'");
                        Logger.log("Autosence change sence value to "+this.sence_arr.get(z)+" for camera "+this.cam_name);
                    }
                    
                    }
                }
            }
            this.sence_arr.remove(0);
        }
        
    }
    void changeSence(){
        int middle=midSencr();
        SQL ch = new SQL();
        ch.insertUpdate("update `monitors` set `sence`='"+middle+"' where `name`='"+this.cam_name+"'");
        Logger.log("Sence was auto changed for "+this.cam_name+", now sence is: "+middle+". You can off this feature by seting auto_sence flag to false.");
        this.current_sence=middle;
        //middle=null;
    }
    int midSencr(){
        int mid=0;
        for(Integer s_o : this.sence_arr){
            
        }
        return mid;
    }
    
    
}
