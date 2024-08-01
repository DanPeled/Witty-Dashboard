package com.frcforftc.wittydashboard.apphook;
// Thanks to https://github.com/Dairy-Foundation/Dairy

import android.content.Context;
import android.view.Menu;

import androidx.annotation.NonNull;

import com.frcforftc.wittydashboard.WittyDashboard;
import com.qualcomm.ftccommon.FtcEventLoop;
import com.qualcomm.robotcore.eventloop.opmode.AnnotatedOpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerNotifier;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.WebHandlerManager;

import dev.frozenmilk.sinister.apphooks.OnCreate;
import dev.frozenmilk.sinister.apphooks.OnCreateEventLoop;
import dev.frozenmilk.sinister.apphooks.OnCreateMenu;
import dev.frozenmilk.sinister.apphooks.OnDestroy;
import dev.frozenmilk.sinister.apphooks.OpModeRegistrar;
import dev.frozenmilk.sinister.apphooks.WebHandlerRegistrar;

public class WittyDashboardHooks implements OpModeManagerNotifier.Notifications {
    public final static WittyDashboardHooks INSTANCE = new WittyDashboardHooks();
    private OpModeManagerImpl opModeManager;

    private WittyDashboardHooks() {
    }

    @Override
    public void onOpModePreInit(OpMode opMode) {
    }

    @Override
    public void onOpModePreStart(OpMode opMode) {
    }

    @Override
    public void onOpModePostStop(OpMode opMode) {
    }

    /**
     * this is a private static inner class, to prevent AppHook code from leaking to the public api
     */
    private static class AppHook implements OnCreate, OnCreateEventLoop, OnCreateMenu, OnDestroy, OpModeRegistrar, WebHandlerRegistrar {
        /**
         * Sinister's scanning looks for instances to work with. In kotlin, this is an object class, in Java we need to re-create that by hand.
         */
        private static final AppHook APP_HOOK = new AppHook();
        private Thread m_runThread;

        private AppHook() {
        }

        @Override
        public void onCreate(@NonNull Context context) {
//            m_runThread = new Thread(() -> {
//                WittyDashboard.start(null);
//                RobotLog.vv("WittyDashboard", "Started server...");
//            });


//            m_runThread.start();
        }

        @Override
        public void onCreateEventLoop(@NonNull Context context, @NonNull FtcEventLoop ftcEventLoop) {
            OpModeManagerImpl opModeManager = ftcEventLoop.getOpModeManager();

            if (WittyDashboardHooks.INSTANCE.opModeManager != null) {
                WittyDashboardHooks.INSTANCE.opModeManager.unregisterListener(WittyDashboardHooks.INSTANCE);
            }
            WittyDashboardHooks.INSTANCE.opModeManager = opModeManager;
            opModeManager.registerListener(WittyDashboardHooks.INSTANCE);

            WittyDashboard.setOpMode(opModeManager.getActiveOpMode());
        }

        @Override
        public void onCreateMenu(@NonNull Context context, @NonNull Menu menu) {
        }

        @Override
        public void onDestroy(@NonNull Context context) {
            m_runThread.interrupt();

            if (WittyDashboard.isRunning())
                WittyDashboard.stop();

            RobotLog.vv("WittyDashboard", "Stopped server...");
        }

        @Override
        public void registerOpModes(@NonNull AnnotatedOpModeManager opModeManager) {
        }

        @Override
        public void webHandlerRegistrar(@NonNull Context context, @NonNull WebHandlerManager webHandlerManager) {
        }
    }
}