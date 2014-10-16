/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import java.util.ArrayList;
import server.GameMap;

/**
 *
 * @author angelid
 */
public class ClientNetworkInterface extends TCPNode {
    /**
    * 
     * @param ServerName
     * @param ServerPort
    */    
    public ClientNetworkInterface(String ServerName, int ServerPort){
    
    }
    
    /**
    * 
     * @param PlayerEmail
     * @return
     * @throws java.lang.Exception
    */
    public boolean checkPlayerNameAvailable(String PlayerEmail) throws Exception{
        
        return true;
    }
    
    /**
     * 
     * @param PlayerEmail
     * @param Password
     * @return visszaadja a játékos azonosítóját
     * @throws java.lang.Exception
    */
    public int registerPlayer(String PlayerEmail, String Password) throws Exception{
        return 0;
    }
    
    /**
     * 
     * @param PlayerEmail
     * @param Password
     * @return visszaadja a játékos azonosítóját
     * @throws java.lang.Exception
    */
    public int loginPlayer(String PlayerEmail, String Password) throws Exception{
        return 0;
    }
    
    /**
     * 
     * @param PlayerEmail
     * @param Password
     * @return 
     * @throws java.lang.Exception
    */
    public boolean logoutPlayer(String PlayerEmail, String Password) throws Exception{
        return true;    
    }
    
    /**
     * 
     * @return 
     * @throws java.lang.Exception
    */
    public ArrayList<GameMap> getGameMaps() throws Exception{
        ArrayList<GameMap> gmal = new ArrayList();
        return gmal;
    }

    @Override
    public void gotMsgAction(String msg, TCPConnection cn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void gotMsgAction(int code, String msg, TCPConnection cn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void partnerQuitAction(TCPConnection cn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
