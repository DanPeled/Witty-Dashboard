package com.frcforftc.wittydashboard.sendables.hardware;

import com.qualcomm.robotcore.hardware.Servo;

import org.frcforftc.networktables.sendable.Sendable;
import org.frcforftc.networktables.sendable.SendableBuilder;

public class ServoSendable implements Sendable {
    private Servo m_servo;

    public ServoSendable(Servo servo) {
        if (servo == null) {
            throw new NullPointerException("Servo cannot be null");
        }

        this.m_servo = servo;
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Servo");
        builder.addDoubleProperty("Value", this.m_servo::getPosition, this.m_servo::setPosition);
        builder.addBooleanProperty("Direction", this::getDirection, this::setDirection);
        builder.addStringProperty("Device name", this.m_servo::getDeviceName, null);
    }

    public boolean getDirection() {
        return this.m_servo.getDirection() == Servo.Direction.FORWARD;
    }

    public void setDirection(boolean direction) {
        this.m_servo.setDirection(direction ? Servo.Direction.FORWARD : Servo.Direction.REVERSE);
    }
}
