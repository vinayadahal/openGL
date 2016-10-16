package com.mediaplayer.codectest;

import android.graphics.Bitmap;

import org.bytedeco.javacv.Frame;

import java.nio.ByteBuffer;

public class FrameConverter {

    Bitmap bitmap;
    ByteBuffer buffer;

    public Bitmap getImage(Frame frame) {
        if (frame != null && frame.image != null) {
            Bitmap.Config config = null;
            switch (frame.imageChannels) {
                case 1:
                case 3:
                case 4:
                    config = Bitmap.Config.ARGB_8888;
                    break;
                case 2:
                    config = Bitmap.Config.RGB_565;
                    break;
                default:
                    assert false;
            }
            if (this.bitmap == null || this.bitmap.getWidth() != frame.imageWidth || this.bitmap.getHeight() != frame.imageHeight || this.bitmap.getConfig() != config) {
                this.bitmap = Bitmap.createBitmap(frame.imageWidth, frame.imageHeight, config); //bitmap init
            }
            ByteBuffer in = (ByteBuffer) frame.image[0];
            int width = frame.imageWidth;
            int height = frame.imageHeight;
            int stride = frame.imageStride;
            int rowBytes = this.bitmap.getRowBytes();
            int y;
            int x;
            byte B;
            if (frame.imageChannels == 1) {
                if (this.buffer == null || this.buffer.capacity() < height * rowBytes) {
                    this.buffer = ByteBuffer.allocate(height * rowBytes);
                }
                for (y = 0; y < height; ++y) {
                    for (x = 0; x < width; ++x) {
                        B = in.get(y * stride + x);
                        this.buffer.put(y * rowBytes + 4 * x, B);
                        this.buffer.put(y * rowBytes + 4 * x + 1, B);
                        this.buffer.put(y * rowBytes + 4 * x + 2, B);
                        this.buffer.put(y * rowBytes + 4 * x + 3, (byte) -1);
                    }
                }
                this.bitmap.copyPixelsFromBuffer(this.buffer.position(0));
            } else if (frame.imageChannels == 3) {
                if (this.buffer == null || this.buffer.capacity() < height * rowBytes) {
                    this.buffer = ByteBuffer.allocate(height * rowBytes);
                }
                for (y = 0; y < height; ++y) {
                    for (x = 0; x < width; ++x) {
                        B = in.get(y * stride + 3 * x);
                        byte G = in.get(y * stride + 3 * x + 1);
                        byte R = in.get(y * stride + 3 * x + 2);
                        this.buffer.put(y * rowBytes + 4 * x, R);
                        this.buffer.put(y * rowBytes + 4 * x + 1, G);
                        this.buffer.put(y * rowBytes + 4 * x + 2, B);
                        this.buffer.put(y * rowBytes + 4 * x + 3, (byte) -1);
                    }
                }
                this.bitmap.copyPixelsFromBuffer(this.buffer.position(0));
            } else {
                this.bitmap.copyPixelsFromBuffer(in.position(0));
            }
            return this.bitmap;
        } else {
            return null;
        }

    }
}
