package tuni.fi.weatherDashboard.controller;

/**
 *
 * @author janik
 */
public class ControllerHolder {

    private static HistoryController historyController;
    private static DashboardController dashboardController;
    private static SavePreferencesController preferencesController;
    private static AboutController aboutController;

    public static void setHistoryController(HistoryController controller) {
        historyController = controller;
        System.out.println(
                "ControllerHolder setter: historyController is null? " +
                (historyController == null));
    }

    public static void setDashboardController(DashboardController controller) {
        dashboardController = controller;
        System.out.println(
                "ControllerHolder setter: dashboardController is null? " +
                (dashboardController == null));
    }

    public static void setPreferencesController(
            SavePreferencesController controller) {
        preferencesController = controller;
    }


    public static void setAboutController(AboutController controller) {
        aboutController = controller;
    }

    public static DashboardController getDashboardController() {
        return dashboardController;
    }

    public static HistoryController getHistoryController() {
        System.out.println("HistoryController getter: null? " +
                (historyController == null));
        return historyController;
    }

    public static SavePreferencesController getPreferencesController() {
        return preferencesController;
    }

    public static AboutController getAboutController() {
        return aboutController;
    }
}
