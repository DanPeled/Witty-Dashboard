package com.frcforftc.wittydashboard.sendables.opModeControl;

import android.content.Context;

import com.qualcomm.ftccommon.FtcEventLoop;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl;

import org.firstinspires.ftc.ftccommon.external.OnCreateEventLoop;

public class OpModeController {
    private static FtcEventLoop m_eventLoop;
    private static OpModeManagerImpl m_opModeManager;

    @OnCreateEventLoop
    public static void attachEventLoop(Context context, FtcEventLoop loop) {
        if (loop == null) return;
        OpModeController.m_eventLoop = loop;
        m_opModeManager = loop.getOpModeManager();
    }

    public static FtcEventLoop getEventLoop() {
        return m_eventLoop;
    }

    public static OpModeManagerImpl getOpModeManager() {
        return m_opModeManager;
    }



}