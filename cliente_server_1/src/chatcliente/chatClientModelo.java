/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatcliente;

import ConexionConfig.ipAddressValidator;
import ConexionConfig.chatEnvelope;
import java.io.*;
import java.net.*;
import javax.swing.*;

import ConexionCliente.chatClientconexion;

/**
 * The class chatClientModel implements the model (logic) of the chat client 
 * application.  Once the connection to the server is accepted and the username
 * has been validated, this class instantiates a child thread process to handle
 * communication with the chat server.
 * 
 * @author aorozco bigfito@gmail.com
 * @version 1.0
 */
public class chatClientModelo{
    
    //MEMBER ATTRIBUTES DEFINITION
    
    /**
     * A String for storing the IPv4 address of the chat server.
     */
    private String serverIPAddress;

    /**
     * An integer for storing the TCP port where the chat server will be listening to.
     */
    private int serverPort;
    
    /**
     * A String for storing the username typed by the user.
     */
    private String strUserName;
    
    /**
     * An integer for storing the size of the username.
     */
    private int sizeUserName;    
    
    /**
     * A Socket object to keep the connection to the chat server.
     */
    private Socket clientSocket;

    /**
     * An ObjectInputStream object to read from the chat server.
     */
    private ObjectInputStream clientStreamInput;

    /**
     * An ObjectOutputStream object to write to the chat server.
     */
    private ObjectOutputStream clientStreamOutput;    
    
    /**
     * A JTextArea object to reference the original chat area reference object from
     * the GUI.
     */
    private JTextArea txtChatAreaRef;

    /**
     * A JTextArea object to reference the original chat notification area reference
     * object from the GUI.
     */
    private JTextArea txtNotificationAreaRef;

    /**
     * A JList object to reference the original list of users reference object from
     * the GUI.
     */
    private JList txtUserListRef;

    /**
     * A JButton object to reference the original connect button object from the
     * GUI.
     */
    private JButton btnConnRef;

    /**
     * A JButton object to reference the original disconnect button object from the
     * GUI.
     */
    private JButton btnDisRef;

    /**
     * A JButton object to reference the original send button object from the GUI.
     */
    private JButton btnSndRef;

    /**
     * A JLabel object to reference the original status label object from the GUI.
     */
    private JLabel lblStatRef;

    /**
     * A JTextField object to reference the original chat message field object from
     * the GUI.
     */
    private JTextField txtChatMessageRef;

    /**
     * A JTextField object to reference the original username field object from the
     * GUI.
     */
    private JTextField txtUsernameRef;    

    /**
     * A chatEnvelope object to exchange messages with the server.
     */
    private chatEnvelope msgEnvelope;
    
    /**
     * A chatClientThread object to handle communication with the chat server.
     */
    private chatClientconexion masterThread;    

    /**
     * Default constructor for the chatClientModel class.
     * @param txtChatAreaReference A reference to the chat area from the GUI.
     * @param txtNotificationAreaReference A reference to the chat notification area
     * from the GUI.
     * @param txtUsersListReference A reference to the list of users from the GUI.
     * @param btnConnectReference A reference to the CONNECT button from the GUI.
     * @param btnDisconnectReference A reference to the DISCONNECT button from the GUI.
     * @param btnSendReference A reference to the SEND button from the GUI.
     * @param lblStatusReference A reference to the STATUS label from the GUI.
     * @param txtChatMessageReference A reference to the chat message field from the GUI.
     * @param txtUsernameReference A reference to the username field from the GUI.
     */
    public chatClientModelo( JTextArea txtChatAreaReference, 
                       JTextArea txtNotificationAreaReference, 
                       JList txtUsersListReference,
                       JButton btnConnectReference,
                       JButton btnDisconnectReference,
                       JButton btnSendReference,
                       JLabel lblStatusReference,
                       JTextField txtChatMessageReference,
                       JTextField txtUsernameReference){
        
        this.strUserName = "";
        this.sizeUserName = 0;
               
        this.txtChatAreaRef = txtChatAreaReference;
        this.txtNotificationAreaRef = txtNotificationAreaReference;
        this.txtUserListRef = txtUsersListReference;
        this.btnConnRef = btnConnectReference;
        this.btnDisRef = btnDisconnectReference;
        this.btnSndRef = btnSendReference;
        this.lblStatRef = lblStatusReference;
        this.txtChatMessageRef = txtChatMessageReference;
        this.txtUsernameRef = txtUsernameReference;
        
        this.clientStreamInput = null;
        this.clientStreamOutput = null;        
        
        //SET SERVER PARAMETERS TO DEFAULTS
        this.serverIPAddress = "127.0.0.1";
        this.serverPort = 32565;
        
        this.msgEnvelope = new chatEnvelope();
    }

    /**
     * The setChatServerIPAddress sets the IPv4 address of the chat server.
     * @param ip A String representing the IPv4 address of the chat server.
     */    
    public void setChatServerIPAddress(String ip){
        this.serverIPAddress = ip;
    }

    /**
     * The setChatServerPort sets the TCP listening port of the chat server.
     * @param port An integer representing the TCP port of the chat server.
     */    
    public void setChatServerPort(int port){
        this.serverPort = port;
    }    

    /**
     * The getStrUserName method returns the username typed by the user.
     * @return A String representing the username typed by the user.
     */
    public String getStrUserName() {
        return strUserName;
    }

    /**
     * The setStrUserName method sets the username typed by the user and its size.
     * @param strUserName A String representing the username typed by the user.
     */
    public void setStrUserName(String strUserName) {
        this.strUserName = strUserName.toUpperCase();
        this.sizeUserName = strUserName.length();
    }
    
    /**
     * The isUsernameValid method validates the username typed by the user against
     * a java regular expression (pattern).
     * @return TRUE if the username matches the pattern. Otherwise returns FALSE.
     */
    public boolean isUsernameValid(){
        
        String pattern;
        pattern = "(\\w{4,15})";        
        return ( strUserName.matches(pattern) ) ? true : false ;
        
    }
    
    /**
     * The prepareConnectionEnvelope method prepares a {@link chatEnvelope} type
     * of message as a connecting message.
     */
    public void prepareConnectionEnvelope(){
        
        msgEnvelope.setEnvHeader( chatEnvelope.CONNECT_MSG );
        msgEnvelope.setEnvFrom(strUserName);
        msgEnvelope.setEnvBody("CONNECT");
        
    }
    
    /**
     * The prepareDisconnectionEnvelope method prepares a {@link chatEnvelope} type
     * of message as a disconnecting message.
     */
    public void prepareDisconnectionEnvelope(){
        
        msgEnvelope.setEnvHeader( chatEnvelope.DISCONNECT_MSG );
        msgEnvelope.setEnvFrom(strUserName);
        msgEnvelope.setEnvBody("DISCONNECT");
        
    }
    
    /**
     * The prepareChatMessageEnvelope method prepares a {@link chatEnvelope} type
     * of message as a regular chat message from the user.
     * @param msgBody A String representing the chat message from the user.
     */
    public void prepareChatMessageEnvelope(String msgBody){
        
        msgEnvelope.setEnvHeader( chatEnvelope.CHAT_MSG );
        msgEnvelope.setEnvFrom(strUserName);
        msgEnvelope.setEnvBody( msgBody );
    }
    
    /**
     * The serverIPAddressIsValid method validates the IPv4 address typed by the
     * user in the chat client's GUI interface.
     * @param ipAddress A String representing the IPv4 address to validate.
     * @return TRUE if the IPv4 address is valid.  Otherwise returns FALSE.
     */
    public boolean serverIPAddressIsValid(String ipAddress){
        
        ipAddressValidator ipav;
        
        ipav = new ipAddressValidator();    
        return ipav.validateIP( ipAddress );
    }
    
    /**
     * The connectToServer method connects to the chat server's listening port.
     * @throws SocketException If aproblem occurs when opening the socket connection.
     * @throws UnknownHostException If the chat server does not respond to the connection
     * attempt.
     * @throws IOException If a problem occurs when initializing the output stream.
     */
    public void connectToServer() throws SocketException, UnknownHostException, IOException{

        this.clientSocket = new Socket( serverIPAddress, serverPort );
        this.clientStreamOutput = new ObjectOutputStream( clientSocket.getOutputStream() );
        
    }
    
    /**
     * The sendEnvelopeMessageToServer method sends out a {@link chatEnvelope} type
     * of message to the chat server.
     * @throws IOException If a problem occurs when writing to the output stream.
     */
    public void sendEnvelopeMessageToServer() throws IOException {
        
        this.clientStreamOutput.writeObject( msgEnvelope );
        this.clientStreamOutput.flush();
        this.clientStreamOutput.reset();
        
    }
    
    /**
     * The monitoringIncomingMessaging method creates an instance of a monitoring
     * thread that will handle all communication between the chat client and the
     * chat server.
     * @throws IOException If a problem occurs when initializing the input stream.
     */
    public void monitoringIncomingMessaging() throws IOException {
        
        //CREATES AN INSTANCE OF THE MONITORING THREAD
        masterThread = new chatClientconexion(clientSocket, clientStreamInput,
                                                 clientStreamOutput, txtChatAreaRef, 
                                                 txtNotificationAreaRef, txtUserListRef,
                                                 btnConnRef, btnDisRef, btnSndRef, lblStatRef,
                                                 txtChatMessageRef, txtUsernameRef);
        masterThread.initiateInputStream();        
        masterThread.start();
        
    }
    
}