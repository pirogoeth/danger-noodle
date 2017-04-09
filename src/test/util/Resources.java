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
     * Gets a resource from the ClassLoader and writes it to a temporary file, then
     * returns a File handle.
     *
     * Thanks StackOverflow!
     *  http://stackoverflow.com/questions/14089146/file-loading-by-getclass-getresource
     *
     * @param String resourcePath
     *
     * @return File
     */
    private static File getResourceAsFile(String resourcePath) {
        try {
            InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(resourcePath);
            if (in == null) {
                return null;
            }

            File tempFile = File.createTempFile(String.valueOf(in.hashCode()), ".tmp");
            tempFile.deleteOnExit();

            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                //copy stream
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
            return tempFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Shortcut for calling this class instance's classloader to get a resource from
     * the classloader path.
     *
     * @param String fPath File path to open.
     *
     * @return File
     */
    private static File openResource(String fPath) {
        File f = getResourceAsFile(fPath);
        if ( f == null ) {
            throw new IllegalArgumentException("File handle returned by getResourceAsFile is null");
        }

        return f;
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
    public static final File P3_IN_PARSER = openResource("resources/p3Parser.txt");

    /**
     * Stream for `/resourcce/p4Arrays.txt`
     *
     * @return File
     */
    public static final File P4_IN_ARRAYS = openResource("resources/p4Arrays.txt");

    /**
     * Stream for `/resources/p4Eval.txt`
     *
     * @return File
     */
    public static final File P4_IN_EVAL = openResource("resources/p4Eval.txt");

}
