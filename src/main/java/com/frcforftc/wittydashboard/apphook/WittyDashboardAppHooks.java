package com.frcforftc.wittydashboard.apphook;

import android.content.Context;

import androidx.annotation.NonNull;

import com.frcforftc.wittydashboard.WittyDashboard;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.ThreadPool;

import dev.frozenmilk.sinister.apphooks.OnCreate;
import dev.frozenmilk.sinister.apphooks.OnDestroy;

public class WittyDashboardAppHooks {
//    public static class OnCreateAppHook implements OnCreate {
//
//        @Override
//        public void onCreate(@NonNull Context context) {
//            ThreadPool.getDefault().execute(() -> {
//                WittyDashboard.start(null);
//                WittyDashboard.put("Test", 3);
//                RobotLog.vv("NetworkTables", WittyDashboard.get("Test").toString());
//            });
//        }
//    }
//
//    public static class OnDestroyAppHook implements OnDestroy {
//
//        @Override
//        public void onDestroy(@NonNull Context context) {
//            RobotLog.vv("NetworkTables", "Server stopped...");
//            WittyDashboard.stop();
//        }
//    }
}
