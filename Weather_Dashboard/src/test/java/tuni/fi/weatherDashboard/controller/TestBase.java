package tuni.fi.weatherDashboard.controller;

/**
 *
 * @author afeera
 */

import org.junit.jupiter.api.BeforeAll;
import javafx.application.Platform;

public class TestBase {
    private static boolean jfxIsSetup;

    @BeforeAll
    public static void setupJavaFX() {
        if (!jfxIsSetup) {
            try {
                // Initialize the JavaFX platform
                Platform.startup(() -> {});
                Platform.setImplicitExit(false);
                jfxIsSetup = true;
            } catch (IllegalStateException e) {
                // Platform already started
                jfxIsSetup = true;
            }
        }
    }
}