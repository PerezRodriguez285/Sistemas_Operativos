
package ConexionConfig;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;


public class chatUsersList implements java.io.Serializable{
    
    ////MEMBER ATTRIBUTES

    /**
     * This member attribute represents a HashMap where the list of users will
     * be stored.
     */      
    private Map<Integer,String> listOfConnectedUsers;
    
    /**
     * Default constructor method for class chatUsersList.  It takes no parameters.
     */
    public chatUsersList(){
        this.listOfConnectedUsers = new HashMap<>();
    }

    /**
     * The addUserToListOfUsers method adds a new key-value pair to the list of
     * users.
     * @param id The (int) id referencing the key of the key-value pair .
     * @param name The (String) name referencing the value of the key-value pair.
     */
    public synchronized void addUserToListOfUsers(int id, String name){
        this.listOfConnectedUsers.put(id, name);
    }

    /**
     * The removeUserFromListOfUsers method removes a key-value pair from the list
     * of users referenced by the pair's key.
     * @param id The (int) id referencing the key of the key-value pair to remove 
     * from the HashMap object.
     */
    public synchronized void removeUserFromListOfUsers(int id){
        this.listOfConnectedUsers.remove(id);
    }
    
    /**
     * The getListOfUsersSize method returns the size of the list of users.
     * @return Returns the size of the HashMap object.
     */
    public synchronized int getListOfUsersSize(){
        return this.listOfConnectedUsers.size();
    }
    
    /**
     * The getUsernameFromListOfUsers method returns the username of the
     * key-value pair referenced by the pair's key.
     * @param key The (int) key referencing the value to obtain.
     * @return Returns a String object representing a username.
     */
    public synchronized String getUsernameFromListOfUsers(int key){
        return this.listOfConnectedUsers.get(key);
    }
    
    /**
     * The getUsersListObject method returns a reference to the list of users
     * object.
     * @return Returns a HashMap<Ingeter,String> object representing a list of 
     * connected users.
     */    
    public synchronized Map<Integer,String> getUsersListObject(){
        return this.listOfConnectedUsers;
    }
    
    /**
     * The usernameInUsersList method ensures that a username is not already stored
     * in the list of users, so usernames are not repeated.
     * @param searchUsername The (String) referencing the username to search for in 
     * the list of users.
     * @return Returns true is the username is already present in the list otherwise 
     * returns false.
     */
    public synchronized boolean usernameInUserslist(String searchUsername){
        
        boolean wasFound = false;
        String nameToCompare;        

        if ( this.listOfConnectedUsers.size() > 0 ){           
            Iterator entries = listOfConnectedUsers.entrySet().iterator();            
            while ( entries.hasNext() ) {
                Map.Entry entry = (Map.Entry) entries.next();
                nameToCompare = entry.getValue().toString();
                if ( nameToCompare.equals(searchUsername) ){
                    wasFound = true;
                    break;
                }
            }
        }                       
        return wasFound;
    }
    
    /**
     * The clearUsersList method clears out the content of the list of users by
     * calling the clear method of the HashMap type of Map.
     */
    public void clearUsersList(){
        this.listOfConnectedUsers.clear();
    }
    
}