/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blind;

import com.mysql.jdbc.CommunicationsException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author oadm
 */
public class UpdateVer extends Thread{
    public void run(){
        if(Configuration.getUpdate()==1){
        try{
            if(checkVer()==false){
            
            updateServ();
            
            }
        else{System.out.println("Version is up to date.");}
        }catch( NullPointerException e){
                
            }
        }else{
            Logger.log("Autoupgrade is disabled. To enable change directive in conf-file.");
        }
    }
    
    static boolean checkVer(){
        /*Скуль на сервер ПО
        проверка текущей версии, при несоответствии запрос админу на обновление
        
        */
        boolean actual=true;
        SQLHead sql = new SQLHead(2);
        String[] values=new String[1];
        values[0]="version";
        double off_ver=Double.parseDouble(sql.select("select * from `programms` where `name`='blind'", values).get(0)[0]);
        double cur_ver=Configuration.getVersion();
        if(off_ver>cur_ver){
            actual=false;
        }
        
        return actual;
        
    }
    static void updateServ(){
        //Уведомляем о необходимости обновиться, спрашиваем через сколько, затем все тормозим, загружаем, обновляем.
        System.out.println("You`ll have to upgrade your server. To disable - change directive in conf-file.");
        for(Camera cam : Blind.cams){
            cam.rec.dectThr.stop();
            cam.rec.stopRec();
            cam.rec.dectThr=null;
            cam.rec=null;
            cam.stop();
        }
        Blind.running=false;
        ProcessBuilder sys_update = new ProcessBuilder(Configuration.getServiceDirectory()+"update.sh"+Configuration.getServUpdate());
        //все делаем в sh
        sys_update.redirectErrorStream(true);
        try{
          Process process = sys_update.start();
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

        }catch(IOException | InterruptedException e){//}catch(IOException | InterruptedException e){
      
  }
        
    }
    
}
