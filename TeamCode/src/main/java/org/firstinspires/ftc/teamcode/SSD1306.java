package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchDevice;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.I2cDeviceType;

import java.util.Arrays;

@I2cDeviceType
@DeviceProperties(name = "SSD1306 OLED", description = "OLED Display", xmlTag = "SSD1306")
public class SSD1306 extends I2cDeviceSynchDevice<I2cDeviceSynch> {

    private final byte[] buffer = new byte[128 * 64 / 8]; // 1024 bytes for 128x64
    public static final I2cAddr ADDRESS_I2C_DEFAULT = I2cAddr.create7bit(0x3C);

    public SSD1306(I2cDeviceSynch deviceClient) {
        super(deviceClient, true);
        this.deviceClient.setI2cAddress(ADDRESS_I2C_DEFAULT);
        super.registerArmingStateCallback(false);
        this.deviceClient.engage();
    }

    @Override
    public Manufacturer getManufacturer() {
        return Manufacturer.Other;
    }

    @Override
    public String getDeviceName() {
        return "SSD1306 OLED Display";
    }

    @Override
    protected boolean doInitialize() {
        // Initialization Sequence
        sendCommand(0xAE); // Display Off
        sendCommand(0xD5);
        sendCommand(0x80); // Set Display Clock Divide Ratio
        sendCommand(0xA8);
        sendCommand(0x3F); // Set Multiplex Ratio
        sendCommand(0xD3);
        sendCommand(0x00); // Set Display Offset
        sendCommand(0x40); // Set Start Line
        sendCommand(0x8D);
        sendCommand(0x14); // Enable Charge Pump
        sendCommand(0x20);
        sendCommand(0x00); // Horizontal Addressing Mode
        sendCommand(0xA1); // Set Segment Re-map
        sendCommand(0xC8); // Set COM Output Scan Direction
        sendCommand(0xDA);
        sendCommand(0x12); // Set COM Pins Hardware Config
        sendCommand(0x81);
        sendCommand(0xCF); // Set Contrast Control
        sendCommand(0xA4); // Entire Display On (Resume)
        sendCommand(0xA6); // Normal Display
        sendCommand(0xAF); // Display On
        return true;
    }

    public void sendCommand(int command) {
        deviceClient.write8(0x00, command);
    }

    public void drawPixel(int x, int y, boolean on) {
        if (x < 0 || x >= 128 || y < 0 || y >= 64) return;
        if (on) buffer[x + (y / 8) * 128] |= (1 << (y & 7));
        else buffer[x + (y / 8) * 128] &= ~(1 << (y & 7));
    }

    public void display() {
        // Set column and page address to start
        sendCommand(0x21);
        sendCommand(0);
        sendCommand(127);
        sendCommand(0x22);
        sendCommand(0);
        sendCommand(7);

        // Write buffer (Data mode 0x40)
        for (int i = 0; i < buffer.length; i++) {
            deviceClient.write8(0x40, buffer[i]);
        }
    }

    public void clear() {
        Arrays.fill(buffer, (byte) 0);
    }

    // A simplified 5x7 ASCII font (Space through 'Z')
    // Each hex value represents a column of 8 pixels
    private final byte[][] FONT = {
            {0x00, 0x00, 0x00, 0x00, 0x00}, // Space
            {0x00, 0x00, 0x5F, 0x00, 0x00}, // !
            {0x00, 0x07, 0x00, 0x07, 0x00}, // "
            {0x14, 0x7F, 0x14, 0x7F, 0x14}, // #
            {0x24, 0x2A, 0x7F, 0x2A, 0x12}, // $
            {0x23, 0x13, 0x08, 0x64, 0x62}, // %
            {0x36, 0x49, 0x55, 0x22, 0x50}, // &
            {0x00, 0x05, 0x03, 0x00, 0x00}, // '
            {0x00, 0x1C, 0x22, 0x41, 0x00}, // (
            {0x00, 0x41, 0x22, 0x1C, 0x00}, // )
            {0x14, 0x08, 0x3E, 0x08, 0x14}, // *
            {0x08, 0x08, 0x3E, 0x08, 0x08}, // +
            {0x00, 0x50, 0x30, 0x00, 0x00}, // ,
            {0x08, 0x08, 0x08, 0x08, 0x08}, // -
            {0x00, 0x60, 0x60, 0x00, 0x00}, // .
            {0x20, 0x10, 0x08, 0x04, 0x02}, // /
            {0x3E, 0x51, 0x49, 0x45, 0x3E}, // 0
            {0x00, 0x42, 0x7F, 0x40, 0x00}, // 1
            {0x42, 0x61, 0x51, 0x49, 0x46}, // 2
            {0x21, 0x41, 0x45, 0x4B, 0x31}, // 3
            {0x18, 0x14, 0x12, 0x7F, 0x10}, // 4
            {0x27, 0x45, 0x45, 0x45, 0x39}, // 5
            {0x3C, 0x4A, 0x49, 0x49, 0x30}, // 6
            {0x01, 0x71, 0x09, 0x05, 0x03}, // 7
            {0x36, 0x49, 0x49, 0x49, 0x36}, // 8
            {0x06, 0x49, 0x49, 0x29, 0x1E}, // 9
            {0x00, 0x36, 0x36, 0x00, 0x00}, // :
            {0x00, 0x56, 0x36, 0x00, 0x00}, // ;
            {0x08, 0x14, 0x22, 0x41, 0x00}, // <
            {0x14, 0x14, 0x14, 0x14, 0x14}, // =
            {0x00, 0x41, 0x22, 0x14, 0x08}, // >
            {0x02, 0x01, 0x51, 0x09, 0x06}, // ?
            {0x32, 0x49, 0x79, 0x41, 0x3E}, // @
            {0x7E, 0x11, 0x11, 0x11, 0x7E}, // A
            {0x7F, 0x49, 0x49, 0x49, 0x36}, // B
            {0x3E, 0x41, 0x41, 0x41, 0x22}, // C
            {0x7F, 0x41, 0x41, 0x22, 0x1C}, // D
            {0x7F, 0x49, 0x49, 0x49, 0x41}, // E
            {0x7F, 0x09, 0x09, 0x09, 0x01}, // F
            {0x3E, 0x41, 0x49, 0x49, 0x7A}, // G
            {0x7F, 0x08, 0x08, 0x08, 0x7F}, // H
            {0x00, 0x41, 0x7F, 0x41, 0x00}, // I
            {0x20, 0x40, 0x41, 0x3F, 0x01}, // J
            {0x7F, 0x08, 0x14, 0x22, 0x41}, // K
            {0x7F, 0x40, 0x40, 0x40, 0x40}, // L
            {0x7F, 0x02, 0x0C, 0x02, 0x7F}, // M
            {0x7F, 0x04, 0x08, 0x10, 0x7F}, // N
            {0x3E, 0x41, 0x41, 0x41, 0x3E}, // O
            {0x7F, 0x09, 0x09, 0x09, 0x06}, // P
            {0x3E, 0x41, 0x51, 0x21, 0x5E}, // Q
            {0x7F, 0x09, 0x19, 0x29, 0x46}, // R
            {0x46, 0x49, 0x49, 0x49, 0x31}, // S
            {0x01, 0x01, 0x7F, 0x01, 0x01}, // T
            {0x3F, 0x40, 0x40, 0x40, 0x3F}, // U
            {0x1F, 0x20, 0x40, 0x20, 0x1F}, // V
            {0x3F, 0x40, 0x38, 0x40, 0x3F}, // W
            {0x63, 0x14, 0x08, 0x14, 0x63}, // X
            {0x07, 0x08, 0x70, 0x08, 0x07}, // Y
            {0x61, 0x51, 0x49, 0x45, 0x43}  // Z
    };

    /**
     * Draws a single character at coordinates x, y
     */
    public void drawChar(int x, int y, char c) {
        int index = c - ' '; // Map ASCII to our array index
        if (index < 0 || index >= FONT.length) return;

        for (int col = 0; col < 5; col++) { // 5 pixels wide
            byte line = FONT[index][col];
            for (int row = 0; row < 8; row++) { // 8 pixels high
                boolean pixelOn = ((line >> row) & 0x01) == 1;
                drawPixel(x + col, y + row, pixelOn);
            }
        }
    }

    /**
     * Draws a string starting at x, y
     */
    public void drawString(int x, int y, String text) {
        int currentX = x;
        for (char c : text.toUpperCase().toCharArray()) {
            drawChar(currentX, y, c);
            currentX += 6; // 5 pixels for char + 1 pixel for space
            if (currentX > 122) break; // Prevent overflow
        }
    }

    /**
     * Draws a large digit (approx 40x56 pixels)
     * @param x start position
     * @param y start position
     * @param digit the char '0'-'9'
     * @param scale the multiplier (use 8 for full-screen height)
     */
    public void drawLargeDigit(int x, int y, char digit, int scale) {
        int index = digit - ' '; // Use the same FONT map
        if (digit < '0' || digit > '9') return;

        for (int col = 0; col < 5; col++) {
            byte line = FONT[index][col];
            for (int row = 0; row < 8; row++) {
                boolean pixelOn = ((line >> row) & 0x01) == 1;

                // Draw a scaled block for every 1 font pixel
                if (pixelOn) {
                    for (int sx = 0; sx < scale; sx++) {
                        for (int sy = 0; sy < scale; sy++) {
                            drawPixel(x + (col * scale) + sx, y + (row * scale) + sy, true);
                        }
                    }
                }
            }
        }
    }
}