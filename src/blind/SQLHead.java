/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blind;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author oadm
 */
public class SQLHead {
    private static String url;
    private static final String user = "blind";
    private static final String password = "blind";
    
    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;
    SQLHead(){
        this.url="jdbc:mysql://localhost:3306/versions?useSSL=false";
    }
    SQLHead(int kind){
        if(kind==1){
            this.url="jdbc:mysql://localhost:3306/versions?useSSL=false";
        }else if(kind==2){
            this.url="jdbc:mysql://"+Configuration.getServUpdate()+":3306/vers?useSSL=false";
        }
        else if(kind==3){
            this.url="jdbc:mysql://"+Configuration.getServUpdate()+":3306/lic?useSSL=false";
        }
        
    }
    public ArrayList<String[]> select(String request, String[] values){
        ArrayList<String[]> list=new ArrayList<>();
        try {
            // opening database connection to MySQL server
            con = DriverManager.getConnection(url, user, password);
 
            // getting Statement object to execute query
            stmt = con.createStatement();
 
            // executing SELECT query
            rs = stmt.executeQuery(request);
            
            //int len=values.length;
            
 
            while (rs.next()) {
                //rs.getArray(url);
                String[] sqlstring=new String[values.length];
                
                for(int i=0;i<values.length;i++){
                String value = rs.getString(values[i]);
                //String value1 = rs.getString("path");
                sqlstring[i]=value;
                //System.out.println("value"+value);
                }
                //System.out.println("sqlstring"+sqlstring[0]+sqlstring[1]);
                list.add(sqlstring);
                //System.out.println("Monitors : " + count);
            }
 
        } catch (SQLException sqlEx) {
            System.out.println("Unable to connect to update server.");
           // sqlEx.printStackTrace();
        } finally {
            //close connection ,stmt and resultset here
            try { con.close(); } catch(SQLException se) { /*can't do anything */ }
            try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
            try { rs.close(); } catch(SQLException se) { /*can't do anything */ }
        }
        return list;
    }
}
