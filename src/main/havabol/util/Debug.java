package havabol.util;

public class Debug {

    public static void DEBUG(String msg) {

        if ( System.getenv().containsKey("DEBUG") ) {
            System.out.println("[debug] " + msg);
        }

    }
}
