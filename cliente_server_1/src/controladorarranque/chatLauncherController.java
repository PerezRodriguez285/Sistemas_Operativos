
package controladorarranque;

import menuframe.menuframe;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import chatserver.chatServer;
import chatcliente.chatCliente;

/**
 *
 * @author aorozco
 */
public class chatLauncherController{
    
    private menuframe gui;
    private ActionListener actionListenerLaunchServer;
    private ActionListener actionListenerLaunchClient;

    public chatLauncherController(menuframe gui) {
        this.gui = gui;
    }

    public void triggerBtnLaunchServer(){
        actionListenerLaunchServer = new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent) {
                gui.getReferenceBtnLaunchServer().setEnabled(false);
                new chatServer().chatServerStart();
            }
        };
        gui.getReferenceBtnLaunchServer().addActionListener(actionListenerLaunchServer);
    }
    
    public void triggerBtnLaunchClient(){
        actionListenerLaunchClient = new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent) {
                new chatCliente().chatClientStart();
            }
        };
        gui.getReferenceBtnLaunchClient().addActionListener(actionListenerLaunchClient);
    }
    
    
}
