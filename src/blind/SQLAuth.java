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
public class SQLAuth {
    static boolean check(String login, String password){
        String query = "select * from `users` where `login`='"+login+"' and `password`='"+password+"'";
        SQL res = new SQL();
        String[] fields = new String[1];
        fields[0]="name";
        if(res.select(query,fields).size()>0){return true;}
        
        return false;
    }
    
}
