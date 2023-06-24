
package chatcliente;

import framecliente.framecliente;


public class chatCliente{
     
  
    private framecliente view;

    
    private chatClientModelo model;

  
    private chatClientControlador controller;

    
    public chatCliente() {
        this.view = new framecliente();
        this.model = new chatClientModelo(view.getTxtChatAreaReference(), 
                                    view.getTxtNotificationAreaReference(),
                                    view.getTxtUsersListReference(),
                                    view.getButtonConnectReference(),
                                    view.getButtonDisconnectReference(),
                                    view.getButtonSendReference(),
                                    view.getLblUserStatus(),
                                    view.getTxtChatMessageReference(),
                                    view.getTxtUserNameReference() );
        this.controller = new chatClientControlador(model, view);
    }
    
   
    public void chatClientStart(){
        
        view.setupNetworkFields();
        view.setVisible(true);
        controller.triggerButtonConnect();
        controller.triggerButtonDisconnect();
        controller.triggerButtonSend();
    }    
}