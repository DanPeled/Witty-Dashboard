package com.frcforftc.wittydashboard;

import java.util.ArrayList;
import java.util.List;

public class Alert {
    private static List<Alert> m_alerts = new ArrayList<>();
    private final AlertType m_type;
    private final String m_message;

    private enum AlertType {
        INFO, WARNING, ERROR
    }


    public static void info(String message) {
        m_alerts.add(new Alert(message, AlertType.INFO));
    }

    public static void warn(String message) {
        m_alerts.add(new Alert(message, AlertType.WARNING));
    }

    private Alert(String message, AlertType type) {
        this.m_message = message;
        this.m_type = type;
        m_alerts.add(this);
    }
}
