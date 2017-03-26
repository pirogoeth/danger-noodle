package havabol.parse;

import havabol.Token;

import java.util.*;

public interface ParseElement {

    boolean isValid();
    String debug();

}
