
package ConexionServer;

import java.net.*;
import java.util.*;


public class chatServerNetworkInterfaces {
    
    
    private ArrayList<String> networkInterfaces;

    
    private ArrayList<String> networkAddresses;

    
    private ArrayList<String> nInterfacesAndAddresses;

  
    public chatServerNetworkInterfaces() {
        this.networkInterfaces = new ArrayList<>();
        this.networkAddresses = new ArrayList<>();
        this.nInterfacesAndAddresses = new ArrayList<>();
    }
    
    public int getNumberOfNetworkIntefaces(){
        return this.networkInterfaces.size();
    }
    
    
    public ArrayList<String> getNetworkIntefacesArray(){
        return this.networkInterfaces;
    }

   
    public ArrayList<String> getNetworkIntefacesAddressesArray(){
        return this.networkAddresses;
    }
    
   
    public ArrayList<String> getNetworkInterfacesAndAddressesArray(){
        return this.nInterfacesAndAddresses;
    }    

    /**
     * @param index 
     * @return 
     */
    public String getNetworkInterfaceNameElement(int index){
        return this.networkInterfaces.get(index);
    }
    
    /**
     * @param index 
     * @return 
     */
    public String getNetworkInterfaceAddressElement(int index){
        return this.networkAddresses.get(index);
    }
    
    /**
     * @param index 
     * @return 
     */
    public String getNetworkInterfacesAndAddressesArrayElement(int index){
        return this.nInterfacesAndAddresses.get(index);
    }
    
    /**
     * The retrieveNetworkInterfacesInfo 
     * @throws SocketException 
     */
    public void retrieveNetworkInterfacesInfo() throws SocketException {
        
        int i = 0;
        Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
        while ( e.hasMoreElements() ) {

            NetworkInterface ni = e.nextElement();
            if ( ni.isVirtual() ){
                // discard virtual interfaces
                continue;
            }

            Enumeration<InetAddress> e2 = ni.getInetAddresses();
            while ( e2.hasMoreElements() ) {

                InetAddress ip = e2.nextElement();
                if ( ip instanceof Inet4Address ){
                        networkInterfaces.add( i, ni.getName() );
                        networkAddresses.add( i, ip.getHostAddress() );
                        nInterfacesAndAddresses.add(i, "INTERFACE: " + ni.getName() 
                                                     + " - " + ip.getHostAddress() );
                    i++;
                }
            }
        }
    }
    
}
