package com.example.main.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class ImageConverterUtil {
    /**
     * @param bitmap
     * @return converting bitmap and return a string
     */
    public static String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, byteArrayOutputStream);
        byte[] b = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    /**
     * @param encodedString
     * @return bitmap (from given string)
     */
    public static Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte = Base64.decode(encodedString,Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch(Exception e){
            Log.e("StringToBitMap", Log.getStackTraceString(e));
            return null;
        }
    }
}
