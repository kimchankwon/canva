package com.interview.canva;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TextRenderServiceTest {

    @InjectMocks
    private TextRenderServiceImpl textRenderService;

    @Test
    public void testRenderTextWithValidFontAndText() {
        String fontUrl = "https://font-public.canva.com/YAFdJkVWBPo/0/MoreSugar-Regular.62992e429acdec5e01c3db.6f7a950ef2bb9f1314d37ac4a660925e.otf";
        String text = "Hello World";
        
        byte[] result = textRenderService.renderText(fontUrl, text);
        
        assertNotNull(result);
        assertTrue(result.length > 0);
        
        // Verify it's a valid PNG (PNG files start with specific bytes)
        assertEquals((byte) 0x89, result[0]);
        assertEquals((byte) 0x50, result[1]);
        assertEquals((byte) 0x4E, result[2]);
        assertEquals((byte) 0x47, result[3]);
    }

    @Test
    public void testRenderTextWithEmptyText() {
        String fontUrl = "https://font-public.canva.com/YAFdJkVWBPo/0/MoreSugar-Regular.62992e429acdec5e01c3db.6f7a950ef2bb9f1314d37ac4a660925e.otf";
        String text = "";
        
        byte[] result = textRenderService.renderText(fontUrl, text);
        
        assertNotNull(result);
        assertTrue(result.length > 0);
    }

    @Test
    public void testRenderTextWithLongText() {
        String fontUrl = "https://font-public.canva.com/YAFdJkVWBPo/0/MoreSugar-Regular.62992e429acdec5e01c3db.6f7a950ef2bb9f1314d37ac4a660925e.otf";
        String text = "This is a very long text that should test the automatic dimension calculation and ensure the image is properly sized for longer content.";
        
        byte[] result = textRenderService.renderText(fontUrl, text);
        
        assertNotNull(result);
        assertTrue(result.length > 0);
    }

    @Test
    public void testRenderTextWithInvalidFontUrl() {
        String fontUrl = "https://invalid-url-that-does-not-exist.com/font.otf";
        String text = "Hello World";
        
        assertThrows(TextRenderException.class, () -> {
            textRenderService.renderText(fontUrl, text);
        });
    }

    @Test
    public void testRenderTextWithNullFontUrl() {
        String text = "Hello World";
        
        assertThrows(TextRenderException.class, () -> {
            textRenderService.renderText(null, text);
        });
    }

    @Test
    public void testRenderTextWithNullText() {
        String fontUrl = "https://font-public.canva.com/YAFdJkVWBPo/0/MoreSugar-Regular.62992e429acdec5e01c3db.6f7a950ef2bb9f1314d37ac4a660925e.otf";
        
        assertThrows(TextRenderException.class, () -> {
            textRenderService.renderText(fontUrl, null);
        });
    }
}
