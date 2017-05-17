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
 */
public class Starter {
    static void startApp(){
        //sql - получаем корректно все данные камеры
        String[] values=new String[4];
         values[0]="name";
         values[1]="path";
         values[2]="func";
         values[3]="auto_sence";
         SQL cameraset = new SQL();
         ArrayList<String[]> list = cameraset.select("select * from `monitors`", values);
         Blind.camera_list=list;
         //Конструируем объекты камер, каждую
         for(int c=0;c<Blind.camera_list.size();c++){
             Camera cam = new Camera(c);
             Blind.cams.add(cam);
             cam.start();
         }
         SysTest st = new SysTest();
         st.start();
         

    }
}
