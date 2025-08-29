package com.interview.canva;

public class FontLoadException extends RuntimeException {
    
    public FontLoadException(String message) {
        super(message);
    }
    
    public FontLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
