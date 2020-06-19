package com.edenrump.graphic.viewport;

import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11C.*;

public class Screenshot {


    public static void saveScreenDataToFile(ByteBuffer screenData, int width, int height, int bytesPerPixel, String fileName) {
        File file = new File(fileName + ".png");
        String format = "PNG";

        BufferedImage image = convertToBufferedImage(screenData, width, height, bytesPerPixel);

        try {
            ImageIO.write(image, format, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ByteBuffer getWindowPixelData(int width, int height, int bytesPerPixel) {
        glReadBuffer(GL_FRONT);
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bytesPerPixel);
        glReadPixels(0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        return buffer;
    }

    public static BufferedImage convertToBufferedImage(ByteBuffer screenData, int width, int height, int bytesPerPixel){
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int i = (x + (width * y)) * bytesPerPixel;
                int r = screenData.get(i) & 0xFF;
                int g = screenData.get(i + 1) & 0xFF;
                int b = screenData.get(i + 2) & 0xFF;
                image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
            }
        }
        return image;
    }
}
