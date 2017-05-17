/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blind;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author oadm
 */
public class Configuration {
    public static int getInterval(){
        int dir=0;
         try{
                ArrayList<String> list = new ArrayList<>();
                File file = new File(getServiceDirectory()+"config/blind.config");
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
                String line = null;
                //StringBuilder stringBuilder = new StringBuilder();
                //String ls = System.getProperty("line.separator");
                while( ( line = reader.readLine() ) != null ) {
                    list.add(line);
                }
                reader.close();
                String directive = list.get(0).substring(7);
                dir=Integer.parseInt(directive);
            //System.out.println("Directive: "+dir);
            }catch(IOException e){
                System.out.println(e);
            }
        return dir;
    }
    public static int getPort(){
        int port=0;
        try{
                ArrayList<String> list = new ArrayList<>();
                File file = new File(getServiceDirectory()+"config/blind.config");
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
                String line = null;
                //StringBuilder stringBuilder = new StringBuilder();
                //String ls = System.getProperty("line.separator");
                while( ( line = reader.readLine() ) != null ) {
                    list.add(line);
                }
                reader.close();
                String directive = list.get(3).substring(12);
                port=Integer.parseInt(directive);
            //System.out.println("Directive: "+dir);
            }catch(IOException e){
                System.out.println(e);
            }
        return port;
    }
    public static int getAuthType(){
        int type=0;
        try{
                ArrayList<String> list = new ArrayList<>();
                File file = new File(getServiceDirectory()+"config/blind.config");
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
                String line = null;
                //StringBuilder stringBuilder = new StringBuilder();
                //String ls = System.getProperty("line.separator");
                while( ( line = reader.readLine() ) != null ) {
                    list.add(line);
                }
                reader.close();
                String directive = list.get(5).substring(0);
                type=Integer.parseInt(directive);
            //System.out.println("Directive: "+dir);
            }catch(IOException e){
                System.out.println(e);
            }
        //System.out.print(type);
        return type;
    }
     public static String getServUpdate(){
        String server="";
        try{
            ArrayList<String> list = new ArrayList<>();
            File file = new File(getServiceDirectory()+"config/blind.config");
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String line = null;
            //StringBuilder stringBuilder = new StringBuilder();
            //String ls = System.getProperty("line.separator");
            while( ( line = reader.readLine() ) != null ) {
                list.add(line);
            }
            reader.close();
            server=list.get(9).substring(0);
        }catch(IOException e){
            Logger.log(e.toString());
        }
        return server;
    }
    public static String getRecDirectory(){
        String directory="";
        try{
            ArrayList<String> list = new ArrayList<>();
            File file = new File(getServiceDirectory()+"config/blind.config");
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String line = null;
            //StringBuilder stringBuilder = new StringBuilder();
            //String ls = System.getProperty("line.separator");
            while( ( line = reader.readLine() ) != null ) {
                list.add(line);
            }
            reader.close();
            directory=list.get(2).substring(20);
        }catch(IOException e){
            Logger.log(e.toString());
        }
        return directory;
    }
    public static double getVersion(){
        String version="";
        try{
            ArrayList<String> list = new ArrayList<>();
            File file = new File(getServiceDirectory()+"/version");
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String line = null;
            //StringBuilder stringBuilder = new StringBuilder();
            //String ls = System.getProperty("line.separator");
            while( ( line = reader.readLine() ) != null ) {
                list.add(line);
            }
            reader.close();
            version=list.get(1).substring(0);
        }catch(IOException e){
            Logger.log(e.toString());
        }
        return Double.parseDouble(version);
    }
    public static int getBuffer(){
        String buffer="";
        try{
            ArrayList<String> list = new ArrayList<>();
            File file = new File("/etc/blind/config/blind.config");
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String line = null;
            //StringBuilder stringBuilder = new StringBuilder();
            //String ls = System.getProperty("line.separator");
            while( ( line = reader.readLine() ) != null ) {
                list.add(line);
            }
            reader.close();
            buffer=list.get(7).substring(0);
        }catch(IOException e){
            Logger.log(e.toString());
        }
        return Integer.parseInt(buffer);
    }
    public static int getUpdate(){
        String update="";
        try{
            ArrayList<String> list = new ArrayList<>();
            File file = new File("/etc/blind/config/blind.config");
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String line = null;
            //StringBuilder stringBuilder = new StringBuilder();
            //String ls = System.getProperty("line.separator");
            while( ( line = reader.readLine() ) != null ) {
                list.add(line);
            }
            reader.close();
            update=list.get(11);
        }catch(IOException e){
            Logger.log(e.toString());
        }
        return Integer.parseInt(update);
    }
    public static String getServiceDirectory(){
        String service_path="";
        try{ArrayList<String> list = new ArrayList<>();
                File file = new File("/etc/blind/config/blind.config");
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
                String line = null;
                //StringBuilder stringBuilder = new StringBuilder();
                //String ls = System.getProperty("line.separator");
                while( ( line = reader.readLine() ) != null ) {
                    list.add(line);
                }
                reader.close();
                service_path=list.get(1).substring(13);
        }catch(IOException e){
            System.out.println(e);
        }

        return service_path;
    }
    public static int getSence(String cam){
        int sence=0;
        String req = "select * from `monitors` where `name`='"+cam+"'";
        String[] values = new String[1];
        values[0]="sence";
        SQL request = new SQL();
        sence=Integer.parseInt(request.select(req, values).get(0)[0]);
        
        return sence;
    }
    /*public String getCamFunc(String name){
        String func="";
        SqlAction sql = new SqlAction();
        func=sql.select("select * from `monitors` where `name`='"+name+"'","func").get(0);
        
        
        return func;
    }*/
}
