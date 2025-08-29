package com.interview.canva;

import org.springframework.stereotype.Service;
import jakarta.annotation.PreDestroy;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class TextRenderServiceImpl implements TextRenderService {
    
    private static final int DEFAULT_FONT_SIZE = 16;
    private static final Color DEFAULT_TEXT_COLOR = Color.BLACK;
    
    private final FontCache fontCache = new FontCache();
    
    @Override
    public byte[] renderText(String fontUrl, String text) {
        if (fontUrl == null) {
            throw new TextRenderException("Font URL cannot be null");
        }
        if (text == null) {
            throw new TextRenderException("Text cannot be null");
        }
        if (text.trim().isEmpty()) {
            throw new TextRenderException("Text cannot be empty");
        }
        
        Font font = getOrLoadFont(fontUrl);
        return generateTextImage(font, text);
    }
    
    private Font getOrLoadFont(String fontUrl) {
        if (fontCache.containsFont(fontUrl)) {
            return fontCache.getFont(fontUrl);
        }
        
        Font font = downloadAndLoadFont(fontUrl);
        fontCache.putFont(fontUrl, font);
        return font;
    }
    
    private Font downloadAndLoadFont(String fontUrl) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(java.net.URI.create(fontUrl))
                    .build();
            
            HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
            
            if (response.statusCode() != 200) {
                throw new FontDownloadException("Failed to download font. HTTP status: " + response.statusCode());
            }
            
            byte[] fontBytes = response.body();
            return Font.createFont(Font.TRUETYPE_FONT, new java.io.ByteArrayInputStream(fontBytes))
                     .deriveFont(Font.PLAIN, DEFAULT_FONT_SIZE);
        } catch (IOException | InterruptedException e) {
            throw new FontDownloadException("Failed to download font from URL: " + fontUrl, e);
        } catch (FontFormatException e) {
            throw new FontLoadException("Failed to load font from downloaded bytes: " + fontUrl, e);
        }
    }
    
    private byte[] generateTextImage(Font font, String text) {
        // Create a temporary graphics context to measure text
        BufferedImage tempImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D tempGraphics = tempImage.createGraphics();
        tempGraphics.setFont(font);
        FontMetrics fontMetrics = tempGraphics.getFontMetrics();
        
        // Calculate text dimensions
        int textWidth = fontMetrics.stringWidth(text);
        int textHeight = fontMetrics.getHeight();
        int ascent = fontMetrics.getAscent();
        
        tempGraphics.dispose();
        
        // Create the actual image with calculated dimensions
        BufferedImage image = new BufferedImage(textWidth, textHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        
        // Enable anti-aliasing for better text quality
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Set font and color
        graphics.setFont(font);
        graphics.setColor(DEFAULT_TEXT_COLOR);
        
        // Draw text (positioned to account for ascent)
        graphics.drawString(text, 0, ascent);
        
        graphics.dispose();
        
        // Convert to PNG byte array
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            javax.imageio.ImageIO.write(image, "PNG", outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new TextRenderException("Failed to convert image to PNG format", e);
        }
    }
    
    @PreDestroy
    public void cleanup() {
        fontCache.clear();
    }
}
