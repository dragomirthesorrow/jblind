/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blind;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author oadm
 */
public class UserCLI extends Thread{
    public void run(){
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter your command...");
        while(Blind.running){
            try{
            String command = reader.readLine();
            Configure conf = new Configure(command);
            conf.set();
            }catch(IOException e){
                System.out.print(e);
            }
        }
    }
    
    
}
