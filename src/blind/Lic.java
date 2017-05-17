/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blind;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;

/**
 *
 * @author oadm
 */
public class Lic {
    static boolean checkLic(){
        /*
        Проверяем тру от лик в скуль, сверяем мд5 и мд5 ид в файле, если нет, то
        берем файл, запрашиваем его значение в мэйне в форм мд5
        забираем ид и номер записываем ид и лик в мд5
        
        
        */
        boolean ok=false;
        SQL lic_act = new SQL();
        String[] values = new String[3];
        values[0]="key";
        values[1]="prod";
        values[2]="actual";
        
        
        
        ArrayList<String[]> data = lic_act.select("select * from `lic`", values);
        String activation_key=data.get(0)[0];
        String prod_key=data.get(0)[1];
        int act=Integer.parseInt(data.get(0)[2]);
        //System.out.println(activation_key);
        if(act==1){
            //ok=true;
            //+ дополнительная проверка.
            try{
                ArrayList<String> list = new ArrayList<>();
                File license = new File(Configuration.getServiceDirectory()+"license/key.lic");
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(license), "UTF-8"));
                String line=null;
                while( ( line = reader.readLine() ) != null){
                    list.add(line);
                }
                String  act_key = md5(list.get(1));
                String entered_prod_key = list.get(0);
                //System.out.println(act_key+"/(file)id:/"+entered_prod_key);
                //System.out.println(activation_key+"/id:/"+prod_key);
                if(act_key.equals(activation_key) && prod_key.equals(entered_prod_key)){
                    ok=true;
                    System.out.println("Key is true.");
                }
                }catch(Exception e){
                
                        }
            
            
        }else if(act==0){
            try{
                ArrayList<String> list = new ArrayList<>();
                File license = new File(Configuration.getServiceDirectory()+"license/key.lic");
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(license), "UTF-8"));
                String line=null;
                while( ( line = reader.readLine() ) != null){
                    list.add(line);
                }
                reader.close();
                String prodkey=list.get(0);
                
                SQLHead req = new SQLHead(3);
                String[] valuesm = new String[2];
                valuesm[1]="activation_key";
                valuesm[0]="prod_key";
                String product_key=req.select("select * from `lic` where `prod_key`=MD5('"+prodkey+"')",valuesm).get(0)[0];
                String act_key=req.select("select * from `lic` where `prod_key`=MD5('"+prodkey+"')",valuesm).get(0)[1];
                //записываем в файл  и в базу
                FileOutputStream os = new FileOutputStream(license, true);
                os.write(act_key.getBytes(), 0, act_key.length());
                SQL insert = new SQL();
                insert.insertUpdate("insert into `lic` (`key`,`prod`,`actual`) values (MD5('"+act_key+"'),'"+product_key+"','1')");
                ok=true;
            }catch (Exception e){
                
            }
            
        }
        return ok;
    }

    public static String md5(String raw) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            //md.update(raw.getBytes(), 0, raw.length());
            //return new BigInteger(1, md.digest()).toString(16);
            return ByteToHexString(md.digest(raw.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private static String ByteToHexString(byte hash[]) {
    StringBuffer buf = new StringBuffer(hash.length * 2);
    int i;
    for (i = 0; i < hash.length; i++) {
      if ((hash[i] & 0xff) < 0x10)
      buf.append("0");
      buf.append(Long.toString(hash[i] & 0xff, 16));
    }
    return buf.toString();
  }

}
