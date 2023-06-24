
package chatserver;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Servidorframe.frameservidor;
import ConexionServer.chatServerNetworkInterfaces;


public class chatServerControlador {
    
  
    private chatServerModelo model;

 
    private frameservidor view;
    
   
    private ActionListener actionListenerStart;

   
    private ActionListener actionListenerStop;

    
    private ActionListener actionListenerSend;
    
   
    String msgBroadcast;

    
    private chatServerNetworkInterfaces nic;

    /**
     * 
     * @param model A {@link chatServerModelo} object reference.
     * @param view A {@link frameservidor} object reference.
     */
    public chatServerControlador(chatServerModelo model, frameservidor view) {
        this.model = model;
        this.view = view;
        this.nic = new chatServerNetworkInterfaces();
    }

    
    public void triggerButtonStart(){
                
        actionListenerStart = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                
                int indexFromList;
                String serverIP;
                String serverPort;                
                
             
                view.getTxtNotificationAreaReference().append("Iniciando el servidor...\n");
                
               
                indexFromList = view.getListNetworkInterfacesReference().getSelectedIndex();
                
                if ( indexFromList > -1 ){
                    
                    nic = view.getChatServerNetworkInterfacesReference();
                    serverIP = nic.getNetworkInterfaceAddressElement( indexFromList );
                    serverPort = view.getTxtPortReference().getValue().toString();
                    
                    if ( model.serverNetworkParametersChecked(serverIP, serverPort) ){
                        
                      
                        view.getButtonStartServerReference().setEnabled(false);
                        view.getButtonStopServerReference().setEnabled(true);
                        
              
                        view.getTxtBroadcastMessageReference().setEditable(true);
                        view.getButtonSendMessageReference().setEnabled(true);
                        
                
                        model.startChatServer();                       
                        
                    }
                }
                
            }
        };
        view.getButtonStartServerReference().addActionListener(actionListenerStart);
        
    }
    

    public void triggerButtonStop(){        
        
        actionListenerStop = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                
                //UPDATE MESSAGE TO CHAT SERVER NOTIFICATION AREA
                view.getTxtNotificationAreaReference().append("Deteniendo Servidor\n");
                
                //IF THERE ARE CONNECTED USERS THEN SEND DISCONNECT MESSAGE
                if ( model.getNumberOfConnectedClients() > 0 ){
                    msgBroadcast = "El servidor esta apagado. Tu conexion sera desconectada.\n";
                    model.broadcastMessage(8, msgBroadcast);
                }
                model.stopChatServer();
                
                view.getTxtNotificationAreaReference().append("Servidor detenido...\n");
                                                    
                //DISABLE COMPONENTS FROM GUI
                view.getButtonStartServerReference().setEnabled(true);
                view.getButtonStopServerReference().setEnabled(false);
                        
                //DISABLE BROADCAST & NOTIFICATION MESSAGE SECTION
                view.getTxtBroadcastMessageReference().setText("");
                view.getTxtBroadcastMessageReference().setEditable(false);
                view.getTxtNotificationAreaReference().setText("");
                view.getButtonSendMessageReference().setEnabled(false); 
                
              
                view.getLblConnectedUsersReference().setText("0");
                
            }
        };
        view.getButtonStopServerReference().addActionListener(actionListenerStop);        
    }
    
  
    public void triggerButtonSend(){       
        
        actionListenerStop = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                
          
                msgBroadcast = view.getTxtBroadcastMessageReference().getText();
                
                if ( !msgBroadcast.equals("") ){
                    
                    model.broadcastMessage(4, msgBroadcast);
                    view.getTxtNotificationAreaReference().append("[Estatus]: El servidor conecto con los Clientes/Mensaje enviado.\n");
                    
                }else{
                    view.getTxtNotificationAreaReference().append("[ERROR]: Tipo invalido de mensaje.\n");
                }
            }
        };
        view.getButtonSendMessageReference().addActionListener(actionListenerStop);                
    }    
}
