
package ConexionServer;

import ConexionConfig.chatUsersList;
import ConexionConfig.chatEnvelope;
import java.io.*;
import java.net.*;
import javax.swing.*;


public class chatServerClientHilos extends Thread {
    
    //MEMBER ATTRIBUTES DEFINITION
    
    
    private boolean looping;

    
    private Socket clientThreadSocket;

    
    private ObjectInputStream serverInput;
    
    
    private ObjectOutputStream serverOutput;
    
    
    private JTextArea txtNotifArea;
    
    
    private JLabel lblnumberOfConnections;
    
    
    private chatEnvelope chatMessageS;
    
    
    private chatUsersList chatUsersListServerThread;
    
    
    private chatServerBroadcaster cserverBroadcasterThread;
    
    
    private chatServerClientsockets csClientsocketKeeperThread;        
    
   
    public chatServerClientHilos(Socket clientThreadSocket,
                                  chatUsersList chatUsersListServerThread,
                                  chatServerBroadcaster cserverBroadcaster,
                                  chatServerClientsockets csClientsocketKeeperThread,
                                  JTextArea txtNotifArea,
                                  JLabel lblnumberOfConnections){
        
        this.looping = true;
        
        this.clientThreadSocket = clientThreadSocket;
        
        this.chatUsersListServerThread = chatUsersListServerThread;
        this.cserverBroadcasterThread = cserverBroadcaster;
        this.csClientsocketKeeperThread = csClientsocketKeeperThread;
        
        this.lblnumberOfConnections = lblnumberOfConnections;
        this.txtNotifArea = txtNotifArea;        
        
    }
    
    /**
     * The initiateStreams() method initializes the input/output 
     * connected client's socket.   
     * @throws IOException If a
     */
    public void initiateStreams() throws IOException{
        
        this.serverOutput = new ObjectOutputStream( clientThreadSocket.getOutputStream() );
        this.serverInput = new ObjectInputStream( clientThreadSocket.getInputStream() );
        
        //ADD A REFERENCE OF THE OUTPUT STREAM IN THE BROADCAST POOLER
        cserverBroadcasterThread.addObjectOutputStreamToBroadcastList( (int)this.getId(),
                                                                        serverOutput );
                    
        //ADD A REFERENCE OF THE INCOMING CLIENT SOCKET IN THE VAULT
        csClientsocketKeeperThread.addClientsocketToVault( (int)this.getId() , 
                                                                    clientThreadSocket);        
    }
   
    public void run(){
        
        String envelopeSender = "";
        
        try{
           
            initiateStreams();
            
            while ( looping ){

                //START READING MESSAGES FROM INCOMING CLIENT
                chatMessageS = new chatEnvelope();
                chatMessageS = (chatEnvelope)serverInput.readObject();
                envelopeSender = chatMessageS.getEnvFrom();
                
                switch ( chatMessageS.getEnvHeader() ){                    
                    
                    case chatEnvelope.CONNECT_MSG:                        
                        
                      
                        if ( !chatUsersListServerThread.usernameInUserslist(envelopeSender) ){

                            writeToNotificationArea("Usuario " + chatMessageS.getEnvFrom() + " Quiere conectarse.\n");
                           
                            chatMessageS.setEnvHeader( chatEnvelope.CONNECT_MSG_ACK );
                            chatMessageS.setEnvFrom("Servidor");
                            chatMessageS.setEnvBody("ACK");
                            serverOutput.writeObject(chatMessageS);
                            serverOutput.flush();
                            serverOutput.reset();

                            chatUsersListServerThread.addUserToListOfUsers((int)this.getId(), envelopeSender);
                            
                            updateLabelConnections();

                            //BROADCAST UPDATED LIST OF USERS TO ALL CONNECTED CLIENTS
                            chatMessageS.setEnvHeader( chatEnvelope.UPDTLISTOFUSERS_MSG );
                            chatMessageS.setEnvFrom("Servidor");
                            chatMessageS.setEnvBody("USERSLIST");                        
                            chatMessageS.setEnvList(chatUsersListServerThread);
                            cserverBroadcasterThread.broadcastMessage(chatMessageS);

                            //IF ONLY ONE USER CONNECTED SEND WAIT MESSAGE
                            if ( chatUsersListServerThread.getListOfUsersSize() == 1 ){                                

                                chatMessageS.setEnvHeader( chatEnvelope.HOMEALONE_MSG );
                                chatMessageS.setEnvFrom("Servidor");
                                chatMessageS.setEnvBody("Espere..");
                                serverOutput.writeObject(chatMessageS);
                                serverOutput.flush();
                                serverOutput.reset();

                            }
                            
                            writeToNotificationArea("Usuario " + envelopeSender + " Esta listo en el servidor.\n");

                            //SEND BROADCAST ANNOUNCING CLIENT HAS JOINED THE CHAT
                            chatMessageS.setEnvHeader( chatEnvelope.JOINING_MSG );
                            chatMessageS.setEnvFrom("Servidor");
                            chatMessageS.setEnvBody(envelopeSender);
                            cserverBroadcasterThread.broadcastMessage(chatMessageS);
                        
                        }else{                            
                                
                            //STOP LOOPING
                            looping = false;
                            
                            //DUPLICATED MESSAGE
                            writeToNotificationArea("Usuario " + chatMessageS.getEnvFrom() + " .\n");
                            
                            //NACK TO CLIENT USERNAME IN USE
                            chatMessageS.setEnvHeader( chatEnvelope.CONNECT_MSG_NACK );
                            chatMessageS.setEnvFrom("Servidor");
                            chatMessageS.setEnvBody("NACK");
                            serverOutput.writeObject(chatMessageS);
                            serverOutput.flush();
                            serverOutput.reset();

                            //UPDATE NUMBER OF CONNECTED CLIENTS IN SERVER GUI
                            updateLabelConnections();
                            
                            //REMOVE REFERENCE TO THREAD OUTPUTSTREAM FROM BROADCAST LIST
                            cserverBroadcasterThread.removeObjectOutputStreamFromBroadcastList( (int)this.getId() );

                            //CLOSE STREAMS
                            finalizeThreadCommunication();
                            
                            //REMOVE THE REFERENCE OF THE INCOMING CLIENT SOCKET FROM THE VAULT
                            csClientsocketKeeperThread.removeClientsocketFromVault( (int)this.getId() );                       

                        }
                        
                        break;
                        
                    case chatEnvelope.DISCONNECT_MSG:
                        
                        //DISCONNECTING MESSAGE
                        writeToNotificationArea("Usuario " + envelopeSender + " Se desconecto.\n");                        
                        
                        //SEND DISCONNECT ACK BACK TO CLIENT
                        chatMessageS.setEnvHeader( chatEnvelope.DISCONNECT_MSG_ACK );
                        chatMessageS.setEnvFrom("Servidor");
                        chatMessageS.setEnvBody("ACK");
                        serverOutput.writeObject(chatMessageS);
                        serverOutput.flush();
                        serverOutput.reset();                        
                        
                        //REMOVE USER FROM LIST OF USERS
                        chatUsersListServerThread.removeUserFromListOfUsers( (int)this.getId() );

                        //REMOVE REFERENCE TO THREAD OUTPUTSTREAM FROM BROADCAST LIST
                        cserverBroadcasterThread.removeObjectOutputStreamFromBroadcastList( (int)this.getId() );
                        
                        //BROADCAST UPDATED LIST OF USERS TO ALL CONNECTED CLIENTS (IF ANY)
                        chatMessageS.setEnvHeader( chatEnvelope.UPDTLISTOFUSERS_MSG );
                        chatMessageS.setEnvFrom("Servidor");
                        chatMessageS.setEnvBody("USERSLIST");                        
                        chatMessageS.setEnvList(chatUsersListServerThread);
                        cserverBroadcasterThread.broadcastMessage(chatMessageS);                                              

                        //SEND BROADCAST ANNOUNCING CLIENT HAS ABANDONED THE CHAT
                        //BROADCAST UPDATED LIST OF USERS TO REMAINING CLIENTS (IF ANY)
                        chatMessageS.setEnvHeader( chatEnvelope.ABANDON_MSG );
                        chatMessageS.setEnvFrom("Servidor");
                        chatMessageS.setEnvBody(envelopeSender);
                        cserverBroadcasterThread.broadcastMessage(chatMessageS); 
                        
                        //UPDATE NUMBER OF CONNECTED CLIENTS IN SERVER GUI
                        updateLabelConnections();                        
                        
                        //CLOSE CLIENT THREAD COMMUNICATION CHANNELS
                        finalizeThreadCommunication();
                        
                        //REMOVE THE REFERENCE OF THE INCOMING CLIENT SOCKET FROM THE VAULT
                        csClientsocketKeeperThread.removeClientsocketFromVault( (int)this.getId() );
                        
                        //UPDATAE MESSAGE ON SERVER NOTIFICATION AREA
                        writeToNotificationArea("Usuario " + envelopeSender + " Se desconecto con exito.\n");

                        //STOP LOOPING
                        looping = false;                        
                        break;
                        
                    case chatEnvelope.CHAT_MSG:
                        
                        //CHAT MESSAGE FROM CLIENT USER (SEND TO ALL CONNECTED USERS )                        
                        chatMessageS.setEnvHeader( chatEnvelope.CHAT_MSG );
                        chatMessageS.setEnvFrom(envelopeSender);
                        cserverBroadcasterThread.broadcastMessage(chatMessageS);                        
                        break;
                    
                    case chatEnvelope.SERVERGOINGDOWN_MSG_ACK:
                        
                        //CLOSE CLIENT THREAD COMMUNICATION CHANNELS
                        finalizeThreadCommunication();                        

                        //STOP LOOPING
                        looping = false;                        
                        break;
                        
                    default :
                        break;
                }
                
                //RESET CHAT MESSAGE ENVELOPE
                chatMessageS.resetEnvelope();
                
            }
            
        }catch( ClassNotFoundException cnf ){
            writeToNotificationArea ( cnf.toString() + "\n");
        }catch( IOException ioe ){
            writeToNotificationArea ( ioe.toString() + "\n");
        }
    }
    
    
    public synchronized void updateLabelConnections(){
        int listSize = chatUsersListServerThread.getListOfUsersSize();
        lblnumberOfConnections.setText( Integer.toString( listSize ) );
    }

   
    public void writeToNotificationArea(String message){
        this.txtNotifArea.append(message);
    }          
    
    
    public void finalizeThreadCommunication() throws IOException{
        
        if ( this.serverOutput != null ) this.serverOutput.close();
        if ( this.serverInput  != null )  this.serverInput.close();
        
    }
    
}