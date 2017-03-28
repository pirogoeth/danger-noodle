package test.util;

import java.net.URISyntaxException;

import java.io.*;
import java.util.*;

/**
 * Resources for testing.
 *
 * @author Sean Johnson <isr830@my.utsa.edu>
 */
public class Resources {

    private static final Resources instance = new Resources();

    /**
     * Shortcut for calling this class instance's classloader to get a resource from
     * the classloader path.
     *
     * @param String fPath File path to open.
     * @return File
     */
    private static File openResource(String fPath) {
        try {
            return new File(instance.getClass().getClassLoader().getResource(fPath).toURI());
        } catch (URISyntaxException ex) {
            return null;
        }
    }

    /**
     * Stream for `/resources/p1Input.txt`
     *
     * @return File
     */
    public static final File P1_IN = openResource("resources/p1Input.txt");

    /**
     * Stream for `/resources/p1InputX1.txt`
     *
     * @return File
     */
    public static final File P1_IN_X1 = openResource("resources/p1InputX1.txt");

    /**
     * Stream for `/resources/p1InputX2.txt`
     *
     * @return File
     */
    public static final File P1_IN_X2 = openResource("resources/p1InputX2.txt");

    /**
     * Stream for `/resources/p1InputX3.txt`
     *
     * @return File
     */
    public static final File P1_IN_X3 = openResource("resources/p1InputX3.txt");

    /**
     * Stream for `/resources/p2Input.txt`
     *
     * @return File
     */
    public static final File P2_IN = openResource("resources/p2Input.txt");

    /**
     * Stream for `/resources/p3Input.txt`
     *
     * @return File
     */
    public static final File P3_IN = openResource("resources/p3Input.txt");

    /**
     * Stream for `/resources/p3InputSE.txt`
     *
     * @return File
     */
    public static final File P3_IN_SE = openResource("resources/p3InputSE.txt");

    /**
     * Stream for `/resources/p3Parser.txt`
     *
     * @return File
     */
    public static final File P3_IN_PARSER = openResource("resource/p3Parser.txt");

}
