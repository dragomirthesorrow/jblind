/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blind;

/**
 *
 * @author oadm
 */
public class Authentication {
    static boolean check(String login, String password){
        boolean result=false;
        if(Configuration.getAuthType()==1){
            if(SQLAuth.check(login, password)==true){result=true;}else{result=false;}
        }else if(Configuration.getAuthType()==2){
            if(LDAPAuth.authenticate(login, password)==true){result=true;}else{result=false;}
        }
        return result;
    }
}
