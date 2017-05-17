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
public class Blind {

    /**
     * @param args the command line arguments
     */
    public static boolean running=true;
    public static ArrayList<String[]> camera_list=new ArrayList<>();
    public static ArrayList<Camera> cams = new ArrayList<>();
    public static ArrayList<Thread> threads_list = new ArrayList<>();
    public static ArrayList<Event> events = new ArrayList<>();
    static boolean lic;
    
    public static void main(String[] args) {
        Lic lc = new Lic();
        Blind.lic=lc.checkLic();
        if(lic==true){
            //Проверка обновления версии сервера
            UpdateVer ver = new UpdateVer();
            ver.start();
            
            // TODO code application logic here
            Logger.log("Blind "+Configuration.getVersion()+" server was started...");
            System.out.println("Blind "+Configuration.getVersion()+" server was started...");
            //server
            Server serv = new Server();
            serv.start();
            //user interface thread
            UserCLI cli = new UserCLI();
            cli.start();
            //server ui thread
            //app thread
            Starter.startApp();

            /*Test tst = new Test();
            if(tst.ping()==true){
                System.out.println("accs");
            }else{
                System.out.println("unreach");
            }*/
        }else{
            System.out.println("Your lic is not supported.");
        }
    }
    public static void close(){
        Logger.log("App stopped.");
        System.out.println("App stopped.");
        running=false;
    }
    
}
