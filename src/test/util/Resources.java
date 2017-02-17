package test.util;

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
     * @return InputStream
     */
    private static InputStream streamResource(String fPath) {
        return instance.getClass().getClassLoader().getResourceAsStream(fPath);
    }

    /**
     * Stream for `/resources/p1Input.txt`
     *
     * @return InputStream
     */
    public static final InputStream P1_IN = streamResource("resources/p1Input.txt");

    /**
     * Stream for `/resources/p1InputX1.txt`
     *
     * @return InputStream
     */
    public static final InputStream P1_IN_X1 = streamResource("resources/p1InputX1.txt");

    /**
     * Stream for `/resources/p1InputX2.txt`
     *
     * @return InputStream
     */
    public static final InputStream P1_IN_X2 = streamResource("resources/p1InputX2.txt");

    /**
     * Stream for `/resources/p1InputX3.txt`
     *
     * @return InputStream
     */
    public static final InputStream P1_IN_X3 = streamResource("resources/p1InputX3.txt");

    /**
     * Stream for `/resources/p2Input.txt`
     *
     * @return InputStream
     */
    public static final InputStream P2_IN = streamResource("resources/p2Input.txt");

    /**
     * Stream for `/resources/p2InputE1.txt`
     *
     * @return InputStream
     */
    public static final InputStream P2_IN_E1 = streamResource("resources/p2InputE1.txt");

}
