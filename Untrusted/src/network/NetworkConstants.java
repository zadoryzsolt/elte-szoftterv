package network;

public final class NetworkConstants {
    
    public static final int TCPC_RESP_FAILURE=0;
    public static final int TCPC_RESP_SUCCESS=1;
    public static final int TCPC_HELLOSERVER=4;    
    
    public static final String STR_ESCAPE="<<<";
    public static final String STR_SEPARATOR1="&&&";
    public static final String STR_SEPARATOR2=":::";
    public final static String[] STR_NOT_ALLOWED={STR_ESCAPE, STR_SEPARATOR1,STR_SEPARATOR1,};
    
}
