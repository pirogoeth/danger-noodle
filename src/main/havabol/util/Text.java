package havabol.util;

import java.util.*;

public class Text {

    /**
     * lpads - Left Pad with Spaces
     *
     * @param int pad_length
     * @param String pad_string
     *
     * @return padded string
     */
    public static String lpads(int pad_length, String pad_string) {
        StringBuilder sb = new StringBuilder();

        for ( int i = 0; i < pad_length; i++ ) {
            sb.append(" ");
        }

        sb.append(pad_string);

        return sb.toString();
    }

}
