
package ConexionServer;

import java.io.*;
import java.util.*;
import java.net.*;


public class chatServerClientsockets {
    
    //MEMBER ATTRIBUTES DEFINITION
    
    
    private Map<Integer,Socket> clientSocketVault;
    
    
    public chatServerClientsockets(){
        this.clientSocketVault = new HashMap<>();
    }
    
  
    public synchronized void addClientsocketToVault(int key, Socket socket){
        this.clientSocketVault.put(key, socket);
    }
 
    public synchronized void removeClientsocketFromVault(int key) throws IOException{
        Socket s;
        s = getClientsocketFromVault(key);
        if ( s != null && s.isConnected() ) s.close();
        this.clientSocketVault.remove(key);
    }
    
    /**
     * @param key 
     * @return 
     */
    public synchronized Socket getClientsocketFromVault(int key){
        return this.clientSocketVault.get(key);
    }
    
    
    public synchronized void closeClientsocketsFromVault() throws IOException{
        Socket s;        
        
        for ( Map.Entry me : this.clientSocketVault.entrySet() ){
            s = this.getClientsocketFromVault( new Integer( me.getKey().toString() ) );
            if ( s != null && s.isConnected() ) s.close();
        }
    }
    
   
    public synchronized void clearClientSocketVault(){
        this.clientSocketVault.clear();
    }
    
}