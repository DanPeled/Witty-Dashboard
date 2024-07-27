package com.frcforftc.wittydashboard.sendables.opModeControl;

import android.content.Context;

import com.qualcomm.ftccommon.FtcEventLoop;

import org.firstinspires.ftc.ftccommon.external.OnCreateEventLoop;
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta;
import org.firstinspires.ftc.robotcore.internal.opmode.RegisteredOpModes;
import org.frcforftc.networktables.sendable.Sendable;
import org.frcforftc.networktables.sendable.SendableBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OpModeRegistrarSendable implements Sendable {
    private static FtcEventLoop m_eventLoop = null;
    private List<String> m_opModeNames;

    @OnCreateEventLoop
    public static void attachEventLoop(Context context, FtcEventLoop loop) {
        m_eventLoop = loop;
    }

    public OpModeRegistrarSendable() {
        if (m_eventLoop == null) {
            // Handle initialization error
        }

        m_opModeNames = collectOpModeNames();
    }

    private String getCurrentOpModeName() {
        if (m_eventLoop == null) {
            return "";
        }

        return m_eventLoop.getOpModeManager().getActiveOpModeName();
    }

    private List<OpModeMeta> collectOpModesMeta() {
        return RegisteredOpModes.getInstance().getOpModes();
    }

    private List<String> collectOpModeNames() {
        return collectOpModeNames(collectOpModesMeta().stream());
    }

    private List<String> collectOpModeNames(Stream<OpModeMeta> stream) {
        return stream.map(OpModeMeta::getDisplayName).collect(Collectors.toList());
    }

    private List<String> collectTeleopNames(Stream<OpModeMeta> stream) {
        return collectOpModeNames(stream.filter(meta -> meta.flavor.equals(OpModeMeta.Flavor.TELEOP)));
    }

    private List<String> collectAutonomousNames(Stream<OpModeMeta> stream) {
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
