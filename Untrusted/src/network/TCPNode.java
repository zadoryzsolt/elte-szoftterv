package network;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import static network.NetworkConstants.*;

public abstract class TCPNode {
    protected boolean closed; 
    protected ArrayList<TCPConnection> ActiveConnections;
    
    protected void setupNode(){
        closed=false;
        ActiveConnections=new ArrayList<>();
    }
    
    public synchronized TCPConnection openConnection(InetAddress ip, int p, boolean listening) throws IOException {
        if (closed) {return null;}
        TCPConnection cn=new TCPConnection(ip, p);
        ActiveConnections.add(cn);
        if (listening) {cn.listen();}
        return cn;
    }
    
    public synchronized TCPConnection openConnection(Socket s, boolean listening){
        if (closed) {return null;}
        TCPConnection cn=new TCPConnection(s);
        ActiveConnections.add(cn);
        if (listening) {cn.listen();}
        return cn;
    }
    
    public synchronized void listenonConnection(TCPConnection cn){
        if (!ActiveConnections.contains(cn)){return;}
        if (cn.listening) return;
        cn.listen();
    }
    
    public synchronized void closeConnection(TCPConnection cn){
        if (!ActiveConnections.contains(cn)){return;}
        cn.close();
        ActiveConnections.remove(cn);
    }  
    
    public synchronized void closeNode(){
        closed=true;
        for (TCPConnection cn:ActiveConnections){
            cn.close();
        }
        ActiveConnections.clear();
    }  

    public String fixString(String str){        
        if (str==null){return "";}
        String s=str;
        for(int i=0; i<STR_NOT_ALLOWED.length;++i){
            s=s.replaceAll(STR_NOT_ALLOWED[i], "");
        }
        return s;
    }
    
    public String fixString(String str, int l){
        if (str==null){return "";}
        String s=str;
        for(int i=0; i<STR_NOT_ALLOWED.length;++i){
            s=s.replaceAll(STR_NOT_ALLOWED[i], "");
        }
        s=s.substring(0, Math.min(s.length(),l+1));
        return s;
    }
        
    public abstract void gotMsgAction(String msg, TCPConnection cn); 
    public abstract void gotMsgAction(int code, String msg, TCPConnection cn); 
    public abstract void partnerQuitAction(TCPConnection cn);  
    
    
    public class TCPConnection {
        private final Socket so;
        private Scanner sc;
        private PrintWriter pw;
        private Thread ListenerThread;
        boolean ok, listening, coded;
        
        TCPConnection(InetAddress ip,int p) throws IOException {
            so = new Socket(ip,p);
            ok=true;
            setup();
        }
        TCPConnection(Socket s){
            so=s;
            ok=true;
            setup();
        }
        
        private void setup(){
            if (!ok) {
                return;
            }
            coded=true;
            listening=false;
            try {
                sc=new Scanner(new InputStreamReader(so.getInputStream(),"UTF-8"));
                pw=new PrintWriter(new OutputStreamWriter(so.getOutputStream(),"UTF-8"));
            } catch (IOException ex) {
                ok=false;
            }
        }
        
        private void listen(){
            listening=true;
            ListenerThread=new Thread(new Listener());
            ListenerThread.start();    
        }
                
        private void close(){
            listening=false;
            try {
                so.close();
            } catch (IOException ex) {
                int a=1;
            }
            sc.close();
            pw.close();
        }
                
        public void setEnableCoding(boolean b){
            coded=b;
        }
        
        //simple message
        public void sendMsg(String s){
            pw.println(s);
            pw.flush();
        }
        
        //this is a coded message
        public void sendMsg(int code, String s){
            pw.println(Integer.toString(1000+code)+s);
            pw.flush();
        }
                
        //gets the message as it is
        public String getMsg(){
            return sc.nextLine();
        }
        
        public InetAddress getPartnerIp(){
            return so.getInetAddress();
        }
        
        @Override
        public boolean equals(Object o){
            return so.equals(((TCPConnection) o).so);        
        }
                
        private class Listener implements Runnable {
            Listener(){
            }

            @Override
            public void run() {
                while(listening && sc.hasNext()){
                    String str=null;
                    try{
                        str=sc.nextLine();
                    } catch (IllegalStateException ex){
                        //nothing to do here, scanner is closed
                        break;
                    }
                    if (coded){
                        int code=Integer.parseInt(str.substring(0, 4))-1000;
                        gotMsgAction(Integer.parseInt(str.substring(0, 4))-1000,str.substring(4, str.length()), TCPConnection.this);
                    }
                    else {
                        gotMsgAction(str, TCPConnection.this);
                    }
                }
                if (listening) {
                    partnerQuitAction(TCPConnection.this);
                }
                listening=false;
            }
        }        
    }
    
}

