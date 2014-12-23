package es.amanzag.restrocket;

import java.util.List;

import javax.usb.UsbControlIrp;
import javax.usb.UsbDevice;
import javax.usb.UsbDisconnectedException;
import javax.usb.UsbException;
import javax.usb.UsbHostManager;
import javax.usb.UsbHub;
import javax.usb.UsbInterface;
import javax.usb.UsbInterfacePolicy;

/**
 * @author amanzaneque
 *
 */
public class RocketDevice {
    private UsbDevice device = null;

    private final short VENDOR_ID = (short) 0x2123;
    private final short PRODUCT_ID = (short) 0x1010;
    
    public enum Command {
        STOP(  new byte[]{ 0x02, 0x20, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }, false), 
        LEDON( new byte[]{ 0x03, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }, false), 
        LEDOFF(new byte[]{ 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }, false), 
        UP(    new byte[]{ 0x02, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }, true), 
        DOWN(  new byte[]{ 0x02, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }, true), 
        LEFT(  new byte[]{ 0x02, 0x04, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }, true), 
        RIGHT( new byte[]{ 0x02, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }, true), 
        FIRE(  new byte[]{ 0x02, 0x10, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }, false)
        ;
        
        private byte[] data;
        boolean needsStop;
        private Command(byte[] data, boolean needsStop) {
            this.data = data;
            this.needsStop = needsStop;
        }
    }

    public RocketDevice() {
    }

    /**
     * Resets the turret to a guess at middle position
     * @throws UsbException 
     */
    public void resetToInitialPosition() throws UsbException { 
        sendCommand(Command.DOWN, 3000);
        sendCommand(Command.LEFT, 6000);
        sendCommand(Command.RIGHT, 3000);
        sendCommand(Command.UP, 200);
    }

    /**
     * Open the USB Thunder Launcher
     * @throws UsbException 
     * @throws UsbDisconnectedException 
     * @throws IllegalArgumentException 
     */
    public void init() throws UsbException {
        UsbHub rootDevice = UsbHostManager.getUsbServices().getRootUsbHub();

        device = findDevice(rootDevice, VENDOR_ID, PRODUCT_ID);
        if (device == null) {
            throw new UsbException("Couldn't find USB device");
        }
        device.getActiveUsbConfiguration().getUsbInterface((byte)0).claim((iface) -> true);
    }

    @SuppressWarnings("unchecked")
    private UsbDevice findDevice(UsbHub hub, short vendorID, short productID) {
        for (UsbDevice device : (List<UsbDevice>) hub.getAttachedUsbDevices()) {
            if (device.getUsbDeviceDescriptor().idVendor() == vendorID
                    && device.getUsbDeviceDescriptor().idProduct() == productID) {
                return device;
            } else if (device.isUsbHub()) {
                UsbDevice found = findDevice((UsbHub) device, vendorID, productID);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }
    
    public void sendCommand(Command c, long duration) throws UsbException {
        UsbControlIrp ctrlTransfer = device.createUsbControlIrp((byte)0x21, (byte)0x9, (short)0, (short)0);
        ctrlTransfer.setData(c.data);
        device.syncSubmit(ctrlTransfer);
        if(c.needsStop) {
            try {
                Thread.sleep(duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ctrlTransfer = device.createUsbControlIrp((byte)0x21, (byte)0x9, (short)0, (short)0);
            ctrlTransfer.setData(Command.STOP.data);
            device.syncSubmit(ctrlTransfer);
        }
    }
    
}
