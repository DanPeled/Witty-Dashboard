package com.frcforftc.wittydashboard.sendables.vision;

import org.frcforftc.networktables.sendable.Sendable;
import org.frcforftc.networktables.sendable.SendableBuilder;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.function.Supplier;


/**
 * Represents a camera preview image as a Sendable for sending over NetworkTables.
 */
public class CameraPreview implements Sendable {
    private final Supplier<Mat> frameSupplier;

    /**
     * Constructs a CameraPreview object with a supplier for retrieving the current frame.
     *
     * @param frameSupplier A supplier function that provides the current camera frame as a Mat object.
     */
    public CameraPreview(Supplier<Mat> frameSupplier) {
        this.frameSupplier = frameSupplier;
    }

    /**
     * Initializes the Sendable representation of the camera preview image.
     *
     * @param builder The builder used to define properties of the Sendable object.
     */
    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Image");
        Mat frame = frameSupplier.get();
//        builder.addRawProperty("image", "Image", () -> frameToArray(frame), null); // TODO
        builder.addIntProperty("width", frame::cols, null);
        builder.addIntProperty("height", frame::rows, null);
    }

    /**
     * Converts a Mat object representing an image frame to a JPEG byte array.
     *
     * @param frame The Mat object containing the image frame.
     * @return A byte array containing the JPEG encoded image data.
     */
    private byte[] frameToArray(Mat frame) {
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", frame, matOfByte);
        return matOfByte.toArray();
    }
}
