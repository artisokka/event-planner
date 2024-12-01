package tuni.fi.weatherDashboard.utils;

/**
 *
 * @author afeera
 */

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

class DateTimeUtilsTest {

    @Test
    void formatDateTime_ValidDate_ReturnsCorrectFormat() {
        // Arrange
        LocalDateTime dateTime = LocalDateTime.of(2023, 12, 1, 10, 30);
        
        // Act
        String result = DateTimeUtils.formatDateTime(dateTime);
        
        // Assert
        assertEquals("01.12.", result);
    }

    @Test
    void formatDateTimeWithYear_ValidDate_ReturnsCorrectFormat() {
        // Arrange
        LocalDateTime dateTime = LocalDateTime.of(2023, 12, 1, 10, 30);
        
        // Act
        String result = DateTimeUtils.formatDateTimeWithYear(dateTime);
        
        // Assert
        assertEquals("01.12.2023", result);
    }

    @Test
    void formatDateTime_SingleDigitDate_PadsWithZero() {
        // Arrange
        LocalDateTime dateTime = LocalDateTime.of(2023, 9, 5, 10, 30);
        
        // Act
        String result = DateTimeUtils.formatDateTime(dateTime);
        
        // Assert
        assertEquals("05.09.", result);
    }
}