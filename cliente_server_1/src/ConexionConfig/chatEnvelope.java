
package ConexionConfig;


public class chatEnvelope implements java.io.Serializable{
    
   
    public static final int INVALID_MSG = 0;
    
    
    public static final int CONNECT_MSG = 1;    
    
        
    public static final int CONNECT_MSG_ACK = 10;

       
    public static final int CONNECT_MSG_NACK = 100;

     
    public static final int DISCONNECT_MSG = 2;

         
    public static final int DISCONNECT_MSG_ACK = 20;
    
        
    public static final int CHAT_MSG = 3;

          
    public static final int SERVERBROADCAST_MSG = 4;

    
    public static final int HOMEALONE_MSG = 5;

    
    public static final int UPDTLISTOFUSERS_MSG = 6;

       
    public static final int JOINING_MSG = 7;

     
    public static final int SERVERGOINGDOWN_MSG = 8;

    
    public static final int SERVERGOINGDOWN_MSG_ACK = 80;

   
    public static final int ABANDON_MSG = 9;
    
    
    private int envHeader;

    
    private String envFrom;

    
    private String envBody;

    /**
     * A {@link chatUsersList} 
     */
    private chatUsersList envList;
    
    /**
     * Default constructor method for class chatEnvelope.
     * @param eHeader A control header for the envelope.
     * @param eFrom An envelope sender.
     * @param eBody The body of the envelope message.
     * @param eList A list of users.
     */
    public chatEnvelope(int eHeader, String eFrom, String eBody, chatUsersList eList){
        this.envHeader = eHeader;
        this.envFrom = eFrom;
        this.envBody = eBody;
        this.envList = eList;
    }
    
    /**
     * Another constructor for the chatEnvelope class.  It takes no parameters and
     * sets all its member attributes to its default values.
     */
    public chatEnvelope(){
        this.envHeader = 0;
        this.envFrom = "";
        this.envBody = "";
        this.envList = null;
    }    

    /**
     * The setEnvHeader method sets the value of the control header member attribute
     * for the chat envelope.
     * @param envHeader An integer representing a control message header for the
     * chat envelope.
     */
    public void setEnvHeader(int envHeader) {
        this.envHeader = envHeader;
    }

    /**
     * The setEnvFrom method sets the value of the sender member attribute of 
     * the chat envelope.
     * @param envFrom A string representing the sender for the chat envelope.
     */
    public void setEnvFrom(String envFrom) {
        this.envFrom = envFrom;
    }    

    /**
     * The setEnvBody method sets the value of the body member attribute of the 
     * chat envelope.
     * @param envBody A string representing the body for the chat envelope.
     */
    public void setEnvBody(String envBody) {
        this.envBody = envBody;
    }
    
    /**
     * The setEnvList method sets the value of the list of users member attribute 
     * of the chat envelope.
     * @param envList A {@link chatUsersList} object representing a list of users
     * for the chat envelope.
     */
    public synchronized void setEnvList(chatUsersList envList){
        this.envList = envList;
    }

    /**
     * The getEnvHeader method returns the value of the control message header
     * of the chat envelope.
     * @return An integer representing a control header message of the chat
     * envelope.
     */
    public int getEnvHeader() {
        return envHeader;
    }

    /**
     * The getEnvFrom method returns the value of the sender of the chat envelope.
     * @return A string representing the sender of the chat envelope.
     */
    public String getEnvFrom() {
        return envFrom;
    }

    /**
     * The getEnvBody method returns the value of the body of the chat envelope.
     * @return A string representing the body of the chat envelope.
     */
    public String getEnvBody() {
        return envBody;
    }
    
    /**
     * The getEnvUsersList return the list of users set in the chat envelope.
     * @return A {@link chatUsersList} object representing a list of users of the
     * chat envelope.
     */
    public synchronized chatUsersList getEnvUsersList(){
        return envList;
    }
    
    /**
     * The resetEnvelope method resets the value of the member attributes of the
     * chat envelope to its default values.
     */
    public void resetEnvelope(){
        this.setEnvHeader(0);
        this.setEnvFrom("");
        this.setEnvBody("");
        this.envList = null;
    }
    
}
