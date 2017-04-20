package havabol.common;

/**
 * Exposes a common interface for debuggable objects.
 */
public interface Debuggable {

    String debug();
    String debug(int indent);

}
