package havabol.common;

/**
 * Exposes a common interface for debuggable objects.
 */
public interface Debuggable {

    default String debug() {
        return this.debug(0);
    }

    String debug(int indent);

}
