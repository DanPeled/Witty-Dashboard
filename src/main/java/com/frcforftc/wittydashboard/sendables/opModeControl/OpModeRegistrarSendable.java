package com.frcforftc.wittydashboard.sendables.opModeControl;

import android.annotation.SuppressLint;
import android.content.Context;

import com.qualcomm.ftccommon.FtcEventLoop;

import org.firstinspires.ftc.ftccommon.external.OnCreateEventLoop;
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta;
import org.firstinspires.ftc.robotcore.internal.opmode.RegisteredOpModes;
import org.frcforftc.networktables.sendable.Sendable;
import org.frcforftc.networktables.sendable.SendableBuilder;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class OpModeRegistrarSendable implements Sendable {
    @SuppressLint("StaticFieldLeak")
    private List<String> m_opModeNames;


    public OpModeRegistrarSendable() {
        if (OpModeController.getEventLoop() == null) {
            // Handle initialization error
        }

        m_opModeNames = collectOpModeNames();
    }

    private String getCurrentOpModeName() {
        if (OpModeController.getEventLoop() == null) {
            return "";
        }

        return OpModeController.getOpModeManager().getActiveOpModeName();
    }

    public static List<OpModeMeta> collectOpModesMeta() {
        return RegisteredOpModes.getInstance().getOpModes().stream().filter((meta) -> meta.flavor != OpModeMeta.Flavor.SYSTEM).collect(Collectors.toList());
    }

    public static List<String> collectOpModeNames() {
        return collectOpModeNames(collectOpModesMeta().stream());
    }

    public static List<String> collectOpModeNames(Stream<OpModeMeta> stream) {
        return stream.map(OpModeMeta::getDisplayName).collect(Collectors.toList());
    }

    public static List<String> collectTeleopNames(Stream<OpModeMeta> stream) {
        return collectOpModeNames(stream.filter(meta -> meta.flavor.equals(OpModeMeta.Flavor.TELEOP)));
    }

    public static List<String> collectAutonomousNames(Stream<OpModeMeta> stream) {
        return collectOpModeNames(stream.filter(meta -> meta.flavor.equals(OpModeMeta.Flavor.AUTONOMOUS)));
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        List<OpModeMeta> opModeMetaList = collectOpModesMeta();
        builder.addStringProperty("Current OpMode", this::getCurrentOpModeName, null);
        builder.addStringArrayProperty("Registered OpModes", () -> collectOpModeNames(opModeMetaList.stream()).toArray(new String[0]), null);
        builder.addStringArrayProperty("Registered TeleOp", () -> collectTeleopNames(opModeMetaList.stream()).toArray(new String[0]), null);
        builder.addStringArrayProperty("Registered Autonomous", () -> collectAutonomousNames(opModeMetaList.stream()).toArray(new String[0]), null);
    }
}
