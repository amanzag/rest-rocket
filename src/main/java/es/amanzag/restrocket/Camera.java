package es.amanzag.restrocket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_highgui.VideoCapture;

/**
 * @author amanzaneque
 *
 */
public class Camera {
    
    
    public static Camera getDevice(int deviceNumber) {
        return new Camera(deviceNumber);
    }
    
    private VideoCapture device;
    private int deviceNumber;
    private Mat frame;
    
    protected Camera(int deviceNumber) {
        this.deviceNumber = deviceNumber;
        device = new VideoCapture(deviceNumber);
        frame = new Mat();
    }
    
    public void open() {
        device.open(deviceNumber);
        if (!device.isOpened()) {
            throw new RuntimeException("Camera device could not be opened");
        }
    }
    
    public void close() {
        device.release();
    }
    
    public byte[] grabFrame() {
        device.read(frame);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(frame.getBufferedImage(), "jpg", baos);
        } catch (IOException e) {
            // shouldn't happen as it's written to memory
            e.printStackTrace();
        }
        frame.release();
        return baos.toByteArray();
    }

}
