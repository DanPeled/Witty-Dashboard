package com.frcforftc.wittydashboard.sendables.opModeControl;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareDevice;

import org.frcforftc.networktables.sendable.Sendable;
import org.frcforftc.networktables.sendable.SendableBuilder;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * The RobotSendable class represents a sendable robot configuration.
 * It implements the Sendable interface to allow for data sending.
 */
public class OpModeSendable implements Sendable {
    private final OpMode m_opMode;
    private final Map<String, List<HardwareDevice>> hardwareMap;

    /**
     * Constructs a RobotSendable instance.
     *
     * @param opMode the OpMode instance associated with this RobotSendable
     * @throws RuntimeException if the hardware map field cannot be accessed
     */
    public OpModeSendable(@NonNull OpMode opMode) {
        this.m_opMode = opMode;

        // Access hardwareMap directly through reflection
        Field mapField;
        try {
            mapField = m_opMode.hardwareMap.getClass().getDeclaredField("allDevicesMap");
            mapField.setAccessible(true);

            hardwareMap = (Map<String, List<HardwareDevice>>) mapField.get(m_opMode.hardwareMap);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Initializes the SendableBuilder for the SmartDashboard.
     *
     * @param builder the SendableBuilder to initialize
     * @throws RuntimeException if the isStarted field cannot be accessed
     */
    @Override
    public void initSendable(SendableBuilder builder) {
        try {
            Field isStartedField = m_opMode.getClass().getField("isStarted");
            isStartedField.setAccessible(true);
            boolean isStarted = isStartedField.getBoolean(m_opMode);

            // Add properties to SendableBuilder
            builder.addStringArrayProperty("Registered Hardware",
                    () -> {
                        if (hardwareMap == null) {
                            return new String[0];
                        }
                        return hardwareMap.keySet().toArray(new String[0]);
                    }, null);

            builder.addBooleanProperty("Is Started", () -> isStarted, null);
            builder.addIntProperty("Runtime", () -> (int) m_opMode.time, null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to initialize RobotSendable", e);
        }
    }
}
