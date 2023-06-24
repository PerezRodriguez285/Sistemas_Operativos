
package chatserver;

import ConexionConfig.chatUsersList;
import ConexionConfig.chatEnvelope;
import ConexionServer.chatServerClientHilos;
import ConexionServer.chatServerBroadcaster;
import ConexionServer.chatServerClientsockets;
import java.io.*;
import java.net.*;

import javax.swing.JLabel;
import javax.swing.JTextArea;



public class chatServerModelo{
    
    
    private String cServerAddress;

    
    private String cServerPort;

    
    private ServerSocket serverSocket;
    
    
    private JLabel lblNumberOfClients;
    
      
    private JTextArea txtNotificationArea;

       
    private int numberOfClientConnections;
    
       
    private boolean handlerSwitch;    

    
    private chatEnvelope msgEnvelope;    

    private chatUsersList chatUsersListServer;

    
    private chatServerBroadcaster csBroadcaster;

    
    private chatServerClientsockets csClientsocketKeeper;

    
    private chatServerOrchestator csOrchestator;
    
    /**
     * 
     * @param lblNumberOfClients
     * 
     * @param txtNotificationArea 
     * 
     */
    public chatServerModelo( javax.swing.JLabel lblNumberOfClients, 
                            javax.swing.JTextArea txtNotificationArea){
        
        this.serverSocket = null;
        this.lblNumberOfClients = lblNumberOfClients;
        this.txtNotificationArea = txtNotificationArea;        
        this.chatUsersListServer = new chatUsersList();
        this.csClientsocketKeeper = new chatServerClientsockets();
        this.csBroadcaster = new chatServerBroadcaster();
        this.msgEnvelope = new chatEnvelope();
        this.numberOfClientConnections = 0;
        this.handlerSwitch = false;
        this.csOrchestator = null;        
    }    
    
    
    public void startChatServer(){
        
        try{            
            //OPEN SERVER CONNECTION
            InetAddress serverIP = InetAddress.getByName( cServerAddress );
            serverSocket = new ServerSocket( Integer.parseInt(cServerPort), 50, serverIP );                           

            //UPDATE MESSAGE ON CHAT SERVER NOTIFICATION AREA
            writeToNotificationArea("Servidor iniciado de forma local " + cServerAddress + ".\n");
            writeToNotificationArea("Servidor en puerto " + cServerPort + ".\n");
            
            //SET SWITCH FOR ORCHESTATOR
            handlerSwitch = true;

            //CREATE A NEW INSTANCE OF THE ORCHESTATOR            
            csOrchestator = new chatServerOrchestator( serverSocket );
            csOrchestator.start();
            
        }catch(UnknownHostException uhe){
            writeToNotificationArea("[ERROR]: .\n");
            writeToNotificationArea( uhe.toString() + "\n" );
        }catch(IOException ioe){
            writeToNotificationArea("[ERROR]: .\n");
            writeToNotificationArea( ioe.toString() + "\n" );
        }
    }
    
    /**
     * 
     * 
     * @param IPaddress I
     * @param port 
     * @return 
     */
    public boolean serverNetworkParametersChecked(String IPaddress, String port){        
        
        cServerAddress = IPaddress;
        cServerPort = port;        
        boolean result = true;

        ServerSocket localSS;        
        try{            
            InetAddress serverIP = InetAddress.getByName( cServerAddress );
            localSS = new ServerSocket( Integer.parseInt(cServerPort), 50, serverIP );
            localSS.close();
        }catch( IOException e ){
            result = false;
            writeToNotificationArea("[ERROR]: Servidor invalido .\n");
            writeToNotificationArea( e.toString() + "\n");
        }        
        return result;
    }
    
    /**
     * The broadcastMessage method sends out a chat message from the server to all 
     * connected clients.
     * @param type A {@link chatEnvelope} header type of message.
     * @param msgToBroadcast A {@link chatEnvelope} body type of message.
     */
    public void broadcastMessage(int type, String msgToBroadcast){
                
        try{            
            msgEnvelope.setEnvHeader(type);
            msgEnvelope.setEnvFrom("Servidor");
            msgEnvelope.setEnvBody(msgToBroadcast);
            csBroadcaster.broadcastMessage(msgEnvelope);                     
        }catch(IOException ioe){
            writeToNotificationArea("[ERROR]: ERROR de envio de mensaje.\n");
            writeToNotificationArea( ioe.toString() + "\n");
        }
    }

    /**
     * The writeToNotificationArea appends a message to the chat server notification
     * area.
     * @param message A String having the message to append.
     */    
    public void writeToNotificationArea(String message){
        this.txtNotificationArea.append(message);
    }
    
    /**
     * The getNumberOfConnectedClients method returns the number of connected clients.
     * @return An integer with the number of connected clients.
     */
    public int getNumberOfConnectedClients(){
        return this.chatUsersListServer.getListOfUsersSize();
    }
    
    /**
     * The stopChatServer method stops the chat server execution.
     */
    public void stopChatServer(){        

        Socket fakeSocket;
        numberOfClientConnections = 0;
        handlerSwitch = false;
        
        try{            

            //CONNECT TO MYSELF TO FORCE server.accept() TO RESUME
            fakeSocket = new Socket( cServerAddress, Integer.valueOf( cServerPort) );
            fakeSocket.close();                        

            //UPDATE LABEL OF CONNECTED CLIENTS
            lblNumberOfClients.setText( Integer.toString( numberOfClientConnections ) );

            //RESET USERS LIST
            chatUsersListServer.clearUsersList();

            //RESET POOLER
            csBroadcaster.closeOutputStreamsFromPooler();
            csBroadcaster.clearBroadcastPooler();

            //RESET CLIENT SOCKET KEEPER
            csClientsocketKeeper.closeClientsocketsFromVault();
            csClientsocketKeeper.clearClientSocketVault();            

            //CLOSE SERVER SOCKET
            serverSocket.close();
            
            //WAIT 2 SECS TO OS TO PROPERLY CLOSE THE SOCKET            
            Thread.sleep(2000);            

        }catch(UnknownHostException uhe){
            writeToNotificationArea("[ERROR]: .\n");
            writeToNotificationArea( uhe.toString() + "\n");                            
        }catch(IOException ioe){
            writeToNotificationArea("[ERROR]:.\n");
            writeToNotificationArea( ioe.toString() + "\n");            
        }catch(InterruptedException ie){
            writeToNotificationArea("[ERROR]: .\n");
            writeToNotificationArea( ie.toString() + "\n");                        
        }
        
    }

   
    class chatServerOrchestator extends Thread{

      

        /** 
         * A ServerSocket object reference to the parent ServerSocket.
         */
        private ServerSocket ss;

        /** 
         * A Socket object reference to the incoming client connection.
         */
        private Socket incomingClientSocket;

        /** 
         *
         * @param ss 
         * 
         */
        public chatServerOrchestator( ServerSocket ss ){            
            this.ss = ss;
            this.incomingClientSocket = null;
        }
        
        /** 
         * The overloaded run() method implements the logic that the server chat
         * orchestator will be doing durint its execution on an infinite loop.
         */
        public void run(){
            
            while( handlerSwitch ){                
                
                try{
                    
                    //UPDATE MESSAGE TO CHAT SERVER NOTIFICATION AREA
                    writeToNotificationArea("Clientes en espera de conexion....\n\n");
                                    
                    //WAITING FOR INCOMING CLIENTS TO CONNECT
                    incomingClientSocket = ss.accept();
                    
                    if ( !handlerSwitch ) break;

                    //UPDATE NUMBER OF CONNECTIONS on SERVER GUI
                    lblNumberOfClients.setText( Integer.toString( chatUsersListServer.getListOfUsersSize() ) );                    
                    
                    //UPDATE MESSAGE TO CHAT SERVER NOTIFICATION AREA
                    writeToNotificationArea("INTENTO DE CONEXIÓN DESDE.. " 
                                            + incomingClientSocket.getRemoteSocketAddress().toString()
                                            + ".\n");                   
                    
                    //CREATE A NEW INSTANCE OF THE CHAT SERVER CLIENT THREAD HANDLER 
                    new chatServerClientHilos( incomingClientSocket,
                                                chatUsersListServer,
                                                csBroadcaster,
                                                csClientsocketKeeper,
                                                txtNotificationArea,
                                                lblNumberOfClients).start();
                    
                    //UPDATE NUMBER OF CONNECTIONS
                    numberOfClientConnections = chatUsersListServer.getListOfUsersSize();
                    
                }catch(SocketException se){
                    writeToNotificationArea("[ERROR]: .\n");
                    writeToNotificationArea( se.toString() + "\n");                    
                }catch(IOException ioe){
                    writeToNotificationArea("[ERROR]: .\n");
                    writeToNotificationArea( ioe.toString() + "\n");
                }                                
            }
            
            //CLOSE TEMPORARY ORCHESTATOR SOCKET
            try{
                if ( incomingClientSocket != null && !incomingClientSocket.isClosed() ) 
                    incomingClientSocket.close();
                if ( ss != null && !ss.isClosed() ) 
                    ss.close();
            }catch(IOException ioe){
                writeToNotificationArea("[ERROR]:.\n");
            }
        }        
    
    }
    
}
