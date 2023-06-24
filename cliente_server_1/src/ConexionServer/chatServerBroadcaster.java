
package ConexionServer;

import java.io.*;
import java.util.*;

import ConexionConfig.chatEnvelope;

public class chatServerBroadcaster {
    
    
    private Map<Integer,ObjectOutputStream> broadcastList;
    
    
    public chatServerBroadcaster(){
        this.broadcastList = new HashMap<>();
    }

    /**
     * @param broadcastList 
     */
    public void setBroadcastList(Map<Integer, ObjectOutputStream> broadcastList) {
        this.broadcastList = broadcastList;
    }

   
    public Map<Integer, ObjectOutputStream> getBroadcastList() {
        return this.broadcastList;
    }
    
    
    public synchronized int getBroadcastListSize() {
        return this.broadcastList.size();
    }    
    
    /**
     * @param id 
     * @param oos 
     * 
     */
    public synchronized void addObjectOutputStreamToBroadcastList(int id, ObjectOutputStream oos){
        this.broadcastList.put(id, oos);
    }

    
    public synchronized void removeObjectOutputStreamFromBroadcastList(int id) throws IOException {
        ObjectOutputStream oos;                
        oos = getObjectOutputStreamFromBroadcastList(id);
        if ( oos != null ) oos.close();        
        this.broadcastList.remove(id);
    }
    
    /**
     * @param id 
     * @return 
     */
    public synchronized ObjectOutputStream getObjectOutputStreamFromBroadcastList(int id){
        return this.broadcastList.get(id);
    }    
    
    /**
     * @param ce A {@link chatEnvelope} 
     * @throws IOException 
     * 
     */
    public synchronized void broadcastMessage(chatEnvelope ce) throws IOException{
        
        ObjectOutputStream oos;        
        for ( Map.Entry me : this.broadcastList.entrySet() ) {            
            oos = this.getObjectOutputStreamFromBroadcastList( new Integer( me.getKey().toString() ) );           
            oos.writeObject(ce);
            oos.flush();
            oos.reset();            
        }        
        
    }
    
    /**
     * @throws IOException If a problem occurs when closing the ObjectOutputStream.
     */
    public synchronized void closeOutputStreamsFromPooler() throws IOException{
        
        ObjectOutputStream oos;        
        for ( Map.Entry me : this.broadcastList.entrySet() ) {            
            oos = this.getObjectOutputStreamFromBroadcastList( new Integer( me.getKey().toString() ) );                       
            oos.close();
        }        
        
    }    
    public synchronized void clearBroadcastPooler(){
        this.broadcastList.clear();
    }
    
}
