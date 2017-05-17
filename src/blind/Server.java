/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blind;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author oadm
 */
public class Server extends Thread{
    private static String login;
    private static String password;
    private static int port;
    public void run(){
        Blind.threads_list.add(this);
        this.isDaemon();
    while(Blind.running){    System.out.println("Server...");
        System.out.println("Welcome to Server side");
    BufferedReader in = null;
    PrintWriter    out= null;

    ServerSocket servers = null;
    Socket       fromclient = null;

    // create server socket
    try {
        this.port=Configuration.getPort();
      servers = new ServerSocket(this.port);
      Logger.log("Server listening "+this.port+" port.");
    } catch (IOException e) {
      System.out.println("Couldn't listen to port "+this.port);
      Logger.log("Couldn't listen to port "+this.port);
      System.exit(-1);
    }

    try {
      System.out.println("Waiting for a client...");
      Logger.log("Waiting for a client...");
      fromclient= servers.accept();
      System.out.println("Client connected");
      Logger.log("Client connected");
      
    } catch (IOException e) {
      System.out.println("Can't accept");
      System.exit(-1);
    }
    try{
    in  = new BufferedReader(new 
     InputStreamReader(fromclient.getInputStream()));
    out = new PrintWriter(fromclient.getOutputStream(),true);
    String         input,output;

    
    login=in.readLine();
    password=in.readLine();
    
    //System.out.println(login+"@"+password);
    if(Authentication.check(login,password)){
         out.println("Authenticated");
         Logger.log(login+" authenticated.");
         //Передаем построчно ArrayList камер
         String[] values=new String[3];
         values[0]="name";
         values[1]="path";
         values[2]="func";
         SQL cameraset = new SQL();
         ArrayList<String[]> list = cameraset.select("select * from `monitors`", values);
         int count = list.size();//длительность приема листа
         int size = values.length;//длянна массивов в листе
         //System.out.println(count);
         out.println(count);
         out.println(size);//Передаем размер
         for(String[] camera : list){//Построчно перекидываем массивы
             for(String val : camera){
                 //System.out.println(val);
                 out.println(val);
             }
         }
         
    while ((input = in.readLine()) != null) {
        
     //if (input.equalsIgnoreCase("exit")) break;
     out.println("S ::: "+input);
     System.out.println(input);
     Configure conf = new Configure(input);
     conf.set();
    }

    }else{
        
        out.println("Authentication failed.");
        Logger.log(login+" authentication failed. Client disconnected.");
        out.close();
        in.close();
        fromclient.close();
        
        
       }
    out.close();
    in.close();
    fromclient.close();
    servers.close();
    }catch(IOException e){
        
    }
    }
    
    }
    
    
}
