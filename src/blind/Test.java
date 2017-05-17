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
public class Test {
    public boolean ping(){
        boolean r=false;
        try{
        InetAddress adr = InetAddress.getByName("172.16.0.23");
        r = adr.isReachable(10000);
         }catch(IOException e){
             
         }
        return r;
    }
}
