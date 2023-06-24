
package controladorarranque;

import menuframe.menuframe;


public class chatLauncher implements Runnable{
    
    
    private menuframe view;
    private chatLauncherController controller;
    
    public chatLauncher(){
        view = new menuframe();
        controller = new chatLauncherController(view);
    }
    
    @Override
    public void run(){
        view.setVisible(true);
        controller.triggerBtnLaunchServer();
        controller.triggerBtnLaunchClient();
    }
    
    public static void main(String[] args){
        new chatLauncher().run();
    }
}
