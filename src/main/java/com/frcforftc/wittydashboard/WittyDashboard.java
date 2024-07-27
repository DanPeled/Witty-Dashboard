package com.frcforftc.wittydashboard;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.command.Command;
import com.frcforftc.wittydashboard.sendables.RobotSendable;
import com.frcforftc.wittydashboard.sendables.ftclib.CommandSendable;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotserver.internal.webserver.websockets.tootallnate.TooTallWebSocket;
import org.frcforftc.networktables.NetworkTablesEntry;
import org.frcforftc.networktables.NetworkTablesInstance;
import org.frcforftc.networktables.NetworkTablesValueType;
import org.frcforftc.networktables.sendable.Sendable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * The WittyDashboard class manages the integration with NetworkTables * and handles sending data from the robot to the dashboard.
 */
public class WittyDashboard {
    private static NetworkTablesInstance m_ntInstance;
    //    private static NetworkTable m_ntTable;
    private static final ConcurrentMap<String, SendableBuilderImpl> m_sendableBuilders = new ConcurrentHashMap<>();
    private static final Set<String> addedValues = ConcurrentHashMap.newKeySet();
    private static RobotSendable m_robotSendable;
    private static Thread runThread;
    private static boolean isRunning = false;

    /**
     * Starts the WittyDashboard with the given OpMode. * * @param opMode the OpMode to associate with the dashboard * @see OpMode
     */
    public static synchronized void start(OpMode opMode) {
        m_ntInstance = NetworkTablesInstance.getDefaultInstance();
        if (opMode != null) m_robotSendable = new RobotSendable(opMode);
        isRunning = true;
        m_ntInstance.startNT4Server("192.168.49.1", 5810);

        runThread = new Thread(() -> {
            while (isRunning) {
                update();
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        runThread.start();
        if (m_ntInstance.getServer() == null) {
            throw new RuntimeException("AHHHHHHH");
        } else {
            RobotLog.vv("NetworkTables", "Server started...");
        }
    }

    public void putCommand(String key, Command command) {
        addSendable(key, new CommandSendable(command));
    }

//    /**
//     * Adds a value to the NetworkTable and sets up a listener for remote changes. * * @param key    the key for the value * @param value  the initial value * @param setter the consumer to handle remote updates
//     */
//    public static <T> void put(@NonNull String key, T value, Consumer<T> setter) {
//        put(key, value);
//        if (!addedValues.add(key)) return;
////        m_ntTable.addListener(key, EnumSet.of(NetworkTableEvent.Kind.kValueRemote), (NetworkTable table, String key_, NetworkTableEvent event) -> {
////            NetworkTableEntry entry = table.getEntry(key);
////            if (value instanceof Integer) {
////                ((Consumer<Integer>) setter).accept((int) entry.getInteger(0));
////            } else if (value instanceof Integer[]) {
////                ((Consumer<Integer[]>) setter).accept(Arrays.stream(entry.getIntegerArray(new long[0])).mapToInt((long s) -> ((Long) s).intValue()).boxed().toArray(Integer[]::new));
////            } else if (value instanceof Double) {
////                ((Consumer<Double>) setter).accept(entry.getDouble(0.0));
////            } else if (value instanceof Double[]) {
////                ((Consumer<Double[]>) setter).accept(Arrays.stream(entry.getDoubleArray(new double[0])).boxed().toArray(Double[]::new));
////            } else if (value instanceof Boolean) {
////                ((Consumer<Boolean>) setter).accept(entry.getBoolean(false));
////            } else if (value instanceof Boolean[]) {
////                boolean[] boolArray = entry.getBooleanArray(new boolean[0]);
////                Boolean[] boxedBoolArray = new Boolean[boolArray.length];
////                for (int i = 0; i < boolArray.length; i++) {
////                    boxedBoolArray[i] = boolArray[i];
////                }
////                ((Consumer<Boolean[]>) setter).accept(boxedBoolArray);
////            } else if (value instanceof String) {
////                ((Consumer<String>) setter).accept(entry.getString(""));
////            } else if (value instanceof String[]) {
////                ((Consumer<String[]>) setter).accept(entry.getStringArray(new String[0]));
////            } else {
////                throw new IllegalArgumentException("Unsupported type: " + value.getClass().getSimpleName());
////            }
////        });
//    }

    /**
     * Updates the dashboard with the current state of the robot.
     */
    private static void update() {
        sendRobotData();
    }

    /**
     * Sends the robot data sendable to the network tables * * @see RobotSendable * @see NetworkTable * @see #addSendable(String, Sendable)
     */
    private static void sendRobotData() {
        if (m_robotSendable != null) WittyDashboard.addSendable("OpMode", m_robotSendable);
    }


    /**
     * Stops the WittyDashboard.
     */
    public static synchronized void stop() {
        isRunning = false;
        runThread.interrupt();
        m_ntInstance.closeServer();
    }

    /**
     * Adds a value to the NetworkTable. * If provided with a sendable will automatically call the addSendable method instead * * @param key   the key for the value * @param value the value to add * @see #addSendable(String, Sendable) * @see NetworkTable
     */
    public synchronized static <T> void put(@NonNull String key, T value) {
        if (value instanceof Sendable) {
            addSendable(key, (Sendable) value);
            return;
        }
        RobotLog.vv("Value", value.toString());
        switch (NetworkTablesValueType.determineType(value)) {
            case Boolean -> m_ntInstance.putBoolean(key, (boolean) value);
            case Double -> m_ntInstance.putNumber(key, (double) value);
            case Float -> m_ntInstance.putNumber(key, (float) value);
            case Int -> m_ntInstance.putNumber(key, (int) value);
            case String -> m_ntInstance.putString(key, (String) value);
            case BooleanArray -> m_ntInstance.putBooleanArray(key, (boolean[]) value);
            case DoubleArray -> m_ntInstance.putNumberArray(key, (double[]) value);
            case FloatArray -> m_ntInstance.putNumberArray(key, (float[]) value);
            case IntArray -> m_ntInstance.putNumberArray(key, (int[]) value);
            case StringArray -> m_ntInstance.putStringArray(key, (String[]) value);
        }
    }

    /**
     * Retrieves a value from the NetworkTable. * * @param key the key for the value * @return the value associated with the key * @see NetworkTableType
     */
    public static synchronized Object get(@NonNull String key) {
        NetworkTablesEntry value = m_ntInstance.get(key);
        if (value == null) {
            return null;
        }

        return value.getValue().get();
    }


    /**
     * Adds a Sendable to the NetworkTable. * * @param key      the key for the Sendable * @param sendable the Sendable to add * @see Sendable
     */
    public static void addSendable(@NonNull String key, Sendable sendable) {
        SendableBuilderImpl impl = new SendableBuilderImpl(sendable);
        impl.post(key, WittyDashboard::put);
    }

}