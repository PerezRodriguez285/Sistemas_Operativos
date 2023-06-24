
package chatserver;

import java.net.SocketException;
import javax.swing.DefaultComboBoxModel;

import Servidorframe.frameservidor;


public class chatServer{
    
   
    private frameservidor view;
    
    
    private chatServerModelo model;
    
    
    private chatServerControlador controller;    

   
    public chatServer() {
        this.view = new frameservidor();
        this.model = new chatServerModelo(view.getLblConnectedUsersReference(),
                                         view.getTxtNotificationAreaReference() );
        this.controller = new chatServerControlador(model, view);  
    }    

   
    public void chatServerStart(){
        
        view.setVisible(true);
        controller.triggerButtonStart();
        controller.triggerButtonStop();
        controller.triggerButtonSend();
    }
}
