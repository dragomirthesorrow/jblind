/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blind;

import java.io.IOException;
import java.net.InetAddress;


/**
 *
 * @author oadm
 */
public class SysTest extends Thread{
    //Здесь проверяется доступность камер и места хранения
    /*private int timeout;
    SysTest(){
        this.timeout=5;
    }*/
    public void run(){
        while(Blind.running){
        for(Camera cam : Blind.cams){
            String pt = cam.path;
            int dog=0;
            for(int i=0;i<pt.length();i++){
                if(pt.charAt(i)=='@'){
                    dog=i;
                }
            }
            String pt1 = pt.substring(dog);
            int doble_point=0;
            for(int i=0;i<pt1.length();i++){
                if(pt1.charAt(i)==':'){
                    doble_point=i;
                }
            }
            if(isReachable(pt1.substring(0,doble_point))){
                cam.reachable=true;
            }else{
                cam.reachable=false;
            }
            
            
        }
        try{
        Thread.sleep(30000);
        }catch(InterruptedException e){
            Logger.log(e.toString());
        }}
    }
     public boolean isReachable(String ip){
         try{
        InetAddress adr = InetAddress.getByName(ip);
        boolean r = adr.isReachable(10000);
         }catch(IOException e){
             
         }
         return true;
     }
}
