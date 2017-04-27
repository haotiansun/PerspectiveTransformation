package com.example.lenovo.camera_2;

//import org.opencv.android.OpenCVLoader;


import android.graphics.Bitmap;

/**
 * Created by Lenovo on 2016/12/30.
 */

public class OpenCVHelper {
    static {
        System.loadLibrary("OpenCV");
    }
    public static native int[] gray(int[] buf, int w, int h);
    public static native int[] test(Bitmap image);

}
