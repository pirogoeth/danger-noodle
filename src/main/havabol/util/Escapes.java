package havabol.util;

import havabol.Token;

import java.util.regex.Matcher;

public class Escapes {

    public static String generateEscapeSequences(String line) {
        char[] lChars = line.toCharArray();
        StringBuilder sb = new StringBuilder();

        for ( int i = 0; i < lChars.length; i++ ) {
            if ( lChars[i] == '\\' ) {
                switch ( lChars[i + 1] ) {
                    case 'a':
                        sb.append((char) 0x07);
                        break;
                    case 't':
                        sb.append((char) 0x09);
                        break;
                    case 'n':
                        sb.append((char) 0x0a);
                        break;
                    default:
                        sb.append(lChars[i + 1]);
                        break;
                }

                i++;
            } else {
                sb.append(lChars[i]);
            }
        }

        return sb.toString();
    }

}
