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
public class SQL {
    private static final String url = "jdbc:mysql://localhost:3306/blind?useSSL=false";
    private static final String user = "root";
    private static final String password = "Gthdsqghblehjr1";
    
    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;
    public void insertUpdate(String request){
        try{
        con = DriverManager.getConnection(url, user, password);
                    stmt = con.createStatement();
                    stmt.executeUpdate(request);
        }catch(SQLException e){
            System.out.print(e);
            Logger.log(e.toString());
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
            sqlEx.printStackTrace();
        } finally {
            //close connection ,stmt and resultset here
            try { con.close(); } catch(SQLException se) { /*can't do anything */ }
            try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
            try { rs.close(); } catch(SQLException se) { /*can't do anything */ }
        }
        return list;
    }
    
}
