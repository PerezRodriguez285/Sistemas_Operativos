/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatcliente;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.*;
import java.net.*;

import framecliente.framecliente;

/**
 * The class chatClientController implements a controller application listening
 * for user actions to be triggered. It receives a reference of the view and model
 * clases.
 * 
 * @author aorozco bigfito@gmail.com
 * @version 1.0
 */
public class chatClientControlador {
    
    //MEMBER ATTRIBUTES SECTION

    /** 
     * A {@link chatClientModelo} object reference.
     */    
    private chatClientModelo model;

    /** 
     * A {@link framecliente} object reference.
     */
    private framecliente view;
    
    /** 
     * An action listener for the CONNECT button.
     */
    private ActionListener actionListenerConnect;

    /** 
     * An action listener for the DISCONNECT button.
     */
    private ActionListener actionListenerDisconnect;

    /** 
     * An action listener for the SEND button.
     */
    private ActionListener actionListenerSend;

    /** 
     * A String for storing temporary messages.
     */    
    private String strAnnounce;

    /**
     * Default constructor for the chatClientController class.
     * @param model A {@link chatClientModelo} reference of the model.
     * @param view A {@link framecliente} reference of the view.
     */
    public chatClientControlador(chatClientModelo model, framecliente view) {
        this.model = model;
        this.view = view;
        this.view.addTextToNotificationArea("Por favor Conecte su usuario al Servidor...");
        this.strAnnounce = "";
    }
    
    /**
     * The cleanStrAnnounce method clears the content of the strAnnounce String variable.
     */
    public void cleanStrAnnounce(){
        strAnnounce = "";
    }    
   
    /**
     * The triggerButtonConnect method is called when the user hits the CONNECT button
     * in order to connect to the chat server.
     */
    public void triggerButtonConnect(){
        actionListenerConnect = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                
                /* WRITE CONNECTION ATTEMPT TO NOTIFICATION AREA */
                view.addTextToNotificationArea("[INFO]: INICIA INTENTO DE CONEXION...");
                
                /* VALIDATE IP ADDRESS */
                if ( model.serverIPAddressIsValid( view.getTxtServerAddress().getText() ) ){
                    
                    /* RETRIEVE USERNAME */
                    model.setStrUserName( view.getTxtUserNameReference().getText() );
                    view.getTxtUserNameReference().setText( model.getStrUserName() );

                    if ( model.isUsernameValid() ){

                        view.getTxtUserNameReference().setEditable(false);

                        strAnnounce =  "[INFO]: USUARIO (" + model.getStrUserName() + ") ";
                        strAnnounce += "CONECTANDO DESDE LA MAQUINA (" + view.getClientHostname() + ") ";
                        strAnnounce += "CON IP (" + view.getClientIPAddress() + ").";
                        view.addTextToNotificationArea(strAnnounce);
                        cleanStrAnnounce();

                        /* DISABLE CONNECT BUTTON AND ENABLE DISCONNECT */
                        view.getButtonConnectReference().setEnabled(false);
                        view.getButtonDisconnectReference().setEnabled(true); 

                        /* CREATE CONNECTING MESSAGE ENVELOPE */
                        model.prepareConnectionEnvelope();

                        try{
                            //SET CHAT SERVER IPv4 & PORT
                            model.setChatServerIPAddress( view.getTxtServerAddress().getText() );
                            model.setChatServerPort( Integer.parseInt( view.getTxtServerPort().getValue().toString() ) );
                            
                            /* CONNECT TO SERVER */
                            model.connectToServer();

                            /* SUBMIT LOGIN & REGISTER INTO SERVER */
                            model.sendEnvelopeMessageToServer();                        

                            /* HANDLE CONTROL TO MASTER THREAD */
                            model.monitoringIncomingMessaging();

                            /* IF LOGIN IS NOT ALREADY IN USE */
                            view.getLblUserStatus().setText("CONECTADO");
                            view.getTxtChatAreaReference().append("BIENVENIDO " + model.getStrUserName() + "\n");                        

                            /* ENABLE CHAT TEXT MESSAGE AND SEND BUTTON */
                            view.getTxtChatMessageReference().setEnabled(true);
                            view.getTxtChatMessageReference().setEditable(true);
                            view.getButtonSendReference().setEnabled(true);

                        }catch(SocketException e1){
                            view.addTextToNotificationArea("[ERROR] [CONNECT]: Problema al conectar con el servidor.\n");
                            view.addTextToNotificationArea(e1.toString());                                                
                        }catch(IOException e2){
                            view.addTextToNotificationArea("[ERROR] [CONNECT]: Problema al crear los flujos de entrada salida.\n");
                            view.addTextToNotificationArea(e2.toString());                                                
                        }

                    }else{
                        view.addTextToNotificationArea("[ERROR] [CONNECT]: USERNAME invalido.  Vuelva a intentar.\n");
                    }
                    
                }else{
                    view.addTextToNotificationArea("[ERROR] [CONNECT]: La direccion IP del servidor no es valida.  Vuelva a intentar.\n");
                }                                
            }
        };
        view.getButtonConnectReference().addActionListener(actionListenerConnect);
    }
    
    /**
     * The triggerButtonDisconnect method is called when the user hits the DISCONNECT
     * butto in order to disconnect from the chat server.
     */
    public void triggerButtonDisconnect(){        
        actionListenerDisconnect = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                
                view.addTextToNotificationArea("[INFO]: USTED HA PULSADO DISCONNECT...");
                
                /* STOP MONITORING INCOMING MESSAGES BY MASTER THREAD */
                try{
                    
                    /* SEND DISCONNECT MESSAGE TO SERVER */
                    model.prepareDisconnectionEnvelope();
                    model.sendEnvelopeMessageToServer();                    
                    
                    /* CLEAN CHAT AREA & USERS LIST */
                    view.getLblUserStatus().setText("DESCONECTADO");
                    view.getTxtChatAreaReference().setText("");                    
                    
                    /* DISABLE CHAT TEXT MESSAGE AND SEND BUTTON */
                    view.getTxtChatMessageReference().setEnabled(false);
                    view.getTxtChatMessageReference().setEditable(false);
                    view.getButtonSendReference().setEnabled(false);                    

                    /* DISABLE DISCONNECT BUTTON AND ENABLE CONNECT */
                    view.getTxtUserNameReference().setEditable(true);
                    view.getButtonConnectReference().setEnabled(true);
                    view.getButtonDisconnectReference().setEnabled(false);
                
                }catch(IOException e){
                    view.addTextToNotificationArea("[ERROR] [DISCONNECT]: Problema al desconectar del servidor.");
                    view.addTextToNotificationArea(e.toString());                                                
                }                                                
            }
        };
        view.getButtonDisconnectReference().addActionListener(actionListenerDisconnect);
    }
    
    /**
     * The triggerButtonSend method is called when the user hits the SEND button.
     */
    public void triggerButtonSend(){        
        
        actionListenerSend = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {                           
                                               
                try{
                    
                    /* PREPARE CHAT MESSAGE */
                    model.prepareChatMessageEnvelope( view.getTextFromChatMessage() );
                    model.sendEnvelopeMessageToServer();                                                           
                    
                    /* DISABLE CHAT TEXT MESSAGE AND SEND BUTTON */
                    view.getTxtChatMessageReference().setText("");
                
                }catch(IOException e){
                    view.addTextToNotificationArea("[ERROR] [GENERAL]: Problema al enviar mensaje " +
                                                   "de chat al servidor.");
                    view.addTextToNotificationArea(e.toString());                                                
                }                 
            }
        };
        view.getButtonSendReference().addActionListener(actionListenerSend);
    }
    
}
