package com.frcforftc.wittydashboard.sendables;

import androidx.annotation.NonNull;

import org.frcforftc.networktables.sendable.Sendable;
import org.frcforftc.networktables.sendable.SendableBuilder;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorAlertsSendable implements Thread.UncaughtExceptionHandler, Sendable {
    private Throwable m_throwable;

    @Override
    public void uncaughtException(@NonNull Thread thread, @NonNull Throwable throwable) {
        this.m_throwable = throwable;
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Alerts"); // From https://github.com/Mechanical-Advantage/RobotCode2023/blob/main/src/main/java/org/littletonrobotics/frc2023/util/Alert.java


        builder.addStringProperty("errors", () -> getStackTrace(m_throwable), null);
        builder.addStringProperty("infos", () -> getStackTrace(m_throwable), null);
        builder.addStringProperty("warnings", () -> getStackTrace(m_throwable), null);
    }

    public String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }
}
