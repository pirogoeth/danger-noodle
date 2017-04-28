package havabol.classify;

/**
 * Introduces special flow result types for modifying control flow.
 */
public enum FlowResult {

    /**
     * Allows early breaking from a loop.
     */
    BREAK,

    /**
     * Skips to the next iteration of the loop.
     */
    CONTINUE,

    /**
     * No flow control modifying result.
     */
    NONE;
}
