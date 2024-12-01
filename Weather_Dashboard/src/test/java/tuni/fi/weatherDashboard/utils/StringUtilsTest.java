package tuni.fi.weatherDashboard.utils;

/**
 *
 * @author afeera
 */

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {

    @Test
    void capitalizeFirstLetter_NullInput_ReturnsNull() {
        // Act
        String result = StringUtils.capitalizeFirstLetter(null);
        
        // Assert
        assertNull(result);
    }

    @Test
    void capitalizeFirstLetter_EmptyString_ReturnsEmptyString() {
        // Act
        String result = StringUtils.capitalizeFirstLetter("");
        
        // Assert
        assertEquals("", result);
    }

    @Test
    void capitalizeFirstLetter_SingleWord_CapitalizesCorrectly() {
        // Act
        String result = StringUtils.capitalizeFirstLetter("hello");
        
        // Assert
        assertEquals("Hello", result);
    }

    @Test
    void capitalizeFirstLetter_MultipleWords_CapitalizesAllWords() {
        // Act
        String result = StringUtils.capitalizeFirstLetter("hello world");
        
        // Assert
        assertEquals("Hello World", result);
    }

    @Test
    void capitalizeFirstLetter_MixedCase_NormalizesCase() {
        // Act
        String result = StringUtils.capitalizeFirstLetter("hElLo WoRlD");
        
        // Assert
        assertEquals("Hello World", result);
    }
}
