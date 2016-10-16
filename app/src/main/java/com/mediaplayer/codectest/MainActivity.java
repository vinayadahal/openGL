package com.mediaplayer.codectest;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.widget.ImageView;

import org.bytedeco.javacv.AndroidFrameConverter;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;

public class MainActivity extends AppCompatActivity {

    Frame frame;
    ImageView imageView;
    SurfaceView surfaceView;
    String filename = "/storage/sdcard0/AVSEQ02.mpg";
    //    String filename = "/storage/sdcard0/Videos/Roar.mp4";
    FFmpegFrameGrabber fGrabber = new FFmpegFrameGrabber(filename);
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.image_view);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                JavaCVNative();
            }
        }, 1000);
    }

    public void JavaCVNative() {
        try {
            fGrabber.start();
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }
        final AndroidFrameConverter fConvert = new AndroidFrameConverter();
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    frame = fGrabber.grabImage();
                    imageView.setImageBitmap(fConvert.convert(frame));
                    if (fGrabber.getFrameNumber() <= (fGrabber.getLengthInFrames() - 2)) {
                        handler.removeCallbacks(this);
                    }
                } catch (FrameGrabber.Exception e) {
                    e.printStackTrace();
                }
                handler.postDelayed(this, 40);
            }
        });
    }
}
