/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blind;
import java.io.IOException;
import javax.naming.NamingException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.directory.*;
import java.util.Hashtable;
import java.util.TreeMap;
import java.util.Map;
/**
 *
 * @author oadm
 */
public class LDAPAuth {
    
    static boolean authenticate(String login, String password){
       Hashtable<String, String> env = new Hashtable<String, String>();
 
        String userName = login+"@corp.digital-grass.ru";
        String userPassword = password;
        String ldapURL = "ldap://dc1.corp.digital-grass.ru:389";
 
        //Access the keystore, this is where the Root CA public key cert was installed
        //Could also do this via the command line option java -Djavax.net.ssl.trustStore....
        //No need to specifiy the keystore password for read operations
        String keystore = "C:/jdk1.6.0/jre/lib/security/cacerts";
        System.setProperty("javax.net.ssl.trustStore", keystore);
 
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
 
        //set security credentials
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, userName);
        env.put(Context.SECURITY_CREDENTIALS, userPassword);
 
        //specify use of ssl
        //        env.put(Context.SECURITY_PROTOCOL, "ssl");
 
        //connect to my domain controller
        env.put(Context.PROVIDER_URL, ldapURL);
        try {
 
            // Create the initial directory context
            DirContext ctx = new InitialLdapContext(env, null);
 
            //Create the search controls
            SearchControls searchCtls = new SearchControls();
 
            //Specify the attributes to return
            //String returnedAtts[] = {"sn", "givenName", "mail"};
            //searchCtls.setReturningAttributes(returnedAtts);
            searchCtls.setReturningAttributes(null);
 
            //Specify the search scope
            searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
 
            //specify the LDAP search filter
            String searchFilter = "(&(memberOf=CN=VideoAdmins,OU=User,DC=corp,DC=digital-grass,DC=ru)(sAMAccountName="+login+"))";
 
            //Specify the Base for the search
            String searchBase = "OU=User,DC=corp,DC=digital-grass,DC=ru";
 
            //initialize counter to total the results
            int totalResults = 0;
 
 
            // Search for objects using the filter
            NamingEnumeration answer = ctx.search(searchBase, searchFilter, searchCtls);
 
            //Loop through the search results
            while (answer.hasMoreElements()) {
                SearchResult sr = (SearchResult) answer.next();
 
                totalResults++;
 
                //System.out.println(">>>" + sr.getName());
 
                // Print out some of the attributes, catch the exception if the attributes have no values
                Attributes attrs = sr.getAttributes();
                if (attrs != null) {
                    NamingEnumeration myattrs = attrs.getAll();
                    TreeMap<String, Object> sorted = new TreeMap<String, Object>();
                    while (myattrs.hasMoreElements()){
                        Attribute attr = (Attribute)myattrs.next();
                        sorted.put(attr.getID(), attr.get());
                    }
                    for(Map.Entry entry : sorted.entrySet()){
                        if (entry.getValue() instanceof String)
                        {}//System.out.println(entry.getKey()+": "+entry.getValue());
                        else
                        {}  //System.out.println(entry.getKey()+": binary");
                    }
                }
 
            }
            //System.out.println("Total results: " + totalResults);
            ctx.close();
            if(totalResults!=0){return true;}else{return false;}
        }
        catch (NamingException e) {
            
            System.err.println("Problem searching directory: " + e);
            return false;
        }
        //return false;
    }
    
}
