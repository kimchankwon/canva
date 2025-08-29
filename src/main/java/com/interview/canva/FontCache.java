package com.interview.canva;

import java.awt.Font;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class FontCache {
    
    private final Map<String, Font> fontCache = new ConcurrentHashMap<>();
    
    public Font getFont(String fontUrl) {
        return fontCache.get(fontUrl);
    }
    
    public void putFont(String fontUrl, Font font) {
        fontCache.put(fontUrl, font);
    }
    
    public boolean containsFont(String fontUrl) {
        return fontCache.containsKey(fontUrl);
    }
    
    public void clear() {
        fontCache.clear();
    }
    
    public int size() {
        return fontCache.size();
    }
}
