/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConexionCliente;

import ConexionConfig.chatUsersList;
import ConexionConfig.chatEnvelope;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;


public class chatClientconexion extends Thread {
    

    private boolean looping;

       
    private Socket threadSocket;

   
    private ObjectInputStream sInput;

    
    private ObjectOutputStream sOutput;
    
    
    private JTextArea txtChatAreaRef;

    
    private JTextArea txtNotificationAreaRef;    

   
    private JList txtUserListRef;

   
    private JButton btnConn;

       
    private JButton btnDis;

   
    private JButton btnSnd;

    
    private JLabel lblStat;

   
    private JTextField txtChatInput;

  
    private JTextField txtUsernameInput;

    
    private chatEnvelope chatMessage;

    
    private chatUsersList listOfUsers;
    
  
    public chatClientconexion(Socket s, ObjectInputStream i, ObjectOutputStream o, 
                            JTextArea txtTextArea1, JTextArea txtTextArea2, JList txtUsersList,
                            JButton btnConn1, JButton btnDis1, JButton btnSnd1, JLabel lblStat1,
                            JTextField txtChatInput1, JTextField txtUsernameInput1){
        
        this.looping = true;        
        this.threadSocket = s;
        this.sInput = i;
        this.sOutput = o;        
        this.txtChatAreaRef = txtTextArea1;
        this.txtNotificationAreaRef = txtTextArea2;
        this.txtUserListRef = txtUsersList;
        this.btnConn = btnConn1;
        this.btnDis = btnDis1;
        this.btnSnd = btnSnd1;
        this.lblStat = lblStat1;
        this.txtChatInput = txtChatInput1;
        this.txtUsernameInput = txtUsernameInput1;
    }
    
  
    public void initiateInputStream() throws IOException {
        sInput = new ObjectInputStream( threadSocket.getInputStream() );        
    }

    
    public void run(){
                    
        try{
            
           
            while( looping ){
                
                chatMessage = (chatEnvelope)sInput.readObject();                
                switch( chatMessage.getEnvHeader() ){
                    
                    case chatEnvelope.CONNECT_MSG_ACK:
                        
                       
                        txtNotificationAreaRef.append("[Estatus]: Cliente Aceptado.\n");
                        break;
                        
                    case chatEnvelope.CONNECT_MSG_NACK:
                        
                        
                        looping = false;
                        txtChatAreaRef.append("[ERROR]: Usuario en Uso .\n");                        
                        txtNotificationAreaRef.append("[ERROR]:Usuario en Uso  .\n");                        
                        lblStat.setText("DESCONECTADO");
                        btnConn.setEnabled(true);
                        btnDis.setEnabled(false);
                        btnSnd.setEnabled(false);
                        txtChatInput.setText("");
                        txtUsernameInput.setEnabled(true);
                        txtUsernameInput.setEditable(true);
                        txtChatInput.setEnabled(false);
                        txtChatInput.setEditable(false);                        
                        close();
                        break;
                        
                    case chatEnvelope.DISCONNECT_MSG_ACK:
                        
                        //DISCONNECT ACCEPTED
                        looping = false;
                        clearListOfUsers();
                        txtNotificationAreaRef.append("[Estatus]: Servidor acepta tu salida.\n");
                        close();
                        break;
                        
                    case chatEnvelope.CHAT_MSG:
                        
                        //CHAT MESSAGE
                        txtChatAreaRef.append("[" + chatMessage.getEnvFrom() + "]: " 
                                                  + chatMessage.getEnvBody() + ".\n");
                        break;
                        
                    case chatEnvelope.SERVERBROADCAST_MSG:
                        
                     
                        txtChatAreaRef.append("[Servidor]: " + chatMessage.getEnvBody() + ".\n");
                        break;
                        
                    case chatEnvelope.HOMEALONE_MSG:
                        
                        
                        txtChatAreaRef.append("\n");
                        break;
                        
                    case chatEnvelope.UPDTLISTOFUSERS_MSG:
                        
                      
                        populateListOfUsers( chatMessage.getEnvUsersList() );
                        break;
                        
                    case chatEnvelope.JOINING_MSG:
                        
                     
                        txtChatAreaRef.append("Usuario " + chatMessage.getEnvBody() + 
                                              " TE uniste al servidor.\n");
                        break;
                        
                    case chatEnvelope.SERVERGOINGDOWN_MSG:
                        
                        //SERVER IS GOING DOWN
                        looping = false;
                        
                        //SEND GOING DOWN ACK BACK TO SERVER
                        chatMessage.setEnvHeader(chatEnvelope.SERVERGOINGDOWN_MSG_ACK);
                        chatMessage.setEnvBody("ACK");
                        sOutput.writeObject(chatMessage);
                        sOutput.flush();
                        sOutput.reset();
                        
                        txtNotificationAreaRef.append("[Estatus]: EL SERVIDOR FALLÓ///// ENVIADO DE NUEVO AL SERVIDOR.\n");
                        
                        //CLEAR LIST OF USERS
                        clearListOfUsers();
                        
                        txtChatAreaRef.append("[Estatus]: .\n");                        
                        txtNotificationAreaRef.append("[estatus]: .\n");
                        
                        //UPDATE GUI
                        lblStat.setText("DESCONECTADO");
                        btnConn.setEnabled(true);
                        btnDis.setEnabled(false);
                        btnSnd.setEnabled(false);
                        txtChatInput.setText("");
                        txtUsernameInput.setEnabled(true);
                        txtUsernameInput.setEditable(true);
                        txtChatInput.setEnabled(false);
                        txtChatInput.setEditable(false);                                                
                        
                    
                        close();                       
                        break;
                    
                    case chatEnvelope.ABANDON_MSG:
                        
                        txtChatAreaRef.append("Usuario [" + chatMessage.getEnvBody() + "] Abandono el Server.\n");                        
                        break;
                        
                    default:
                        break;
                }
                
                //RESETS CHAT MESSAGE
                chatMessage.resetEnvelope();
                
            }            
            
        }catch(IOException e1){
                
                txtNotificationAreaRef.append("ERROR: Problema al leer mensaje del servidor.\n");
                txtNotificationAreaRef.append( e1.toString() + "\n");
                
        }catch(ClassNotFoundException e2){
            
                txtNotificationAreaRef.append("ERROR: Problema al cerrar recursos de red.\n");
                txtNotificationAreaRef.append( e2.toString() + "\n");
            
        }
        
    }
    
  
    public void populateListOfUsers(chatUsersList listOfUsers){
        
        Vector v = new Vector();        
        this.listOfUsers = listOfUsers;
        
        if ( listOfUsers != null ){
            for ( Map.Entry me : listOfUsers.getUsersListObject().entrySet() ) {
                v.add( listOfUsers.getUsernameFromListOfUsers( new Integer( me.getKey().toString() ) ) );
            }
            this.txtUserListRef.setListData(v);
        }else{
            v.add("");
            this.txtUserListRef.setListData(v);
        }                
        
    }
    
    public void clearListOfUsers(){
        Vector v;
        v = new Vector();
        v.add("");
        this.txtUserListRef.setListData(v);
    }

    public void close() throws IOException{
        
        if ( sOutput != null ) sOutput.close();
        if ( sInput != null ) sInput.close();
        if ( threadSocket != null ) threadSocket.close();
    }
    
}
