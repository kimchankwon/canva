package com.interview.canva;

public class TextRenderException extends RuntimeException {
    
    public TextRenderException(String message) {
        super(message);
    }
    
    public TextRenderException(String message, Throwable cause) {
        super(message, cause);
    }
}
