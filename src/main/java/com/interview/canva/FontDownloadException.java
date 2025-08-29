package com.interview.canva;

public class FontDownloadException extends RuntimeException {
    
    public FontDownloadException(String message) {
        super(message);
    }
    
    public FontDownloadException(String message, Throwable cause) {
        super(message, cause);
    }
}
