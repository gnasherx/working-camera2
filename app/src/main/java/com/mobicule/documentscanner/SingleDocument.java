package com.mobicule.documentscanner;

import android.graphics.Bitmap;

public class SingleDocument {
    private String name;
    private Bitmap bitmap;

    public SingleDocument(String name, Bitmap bitmap) {
        this.name = name;
        this.bitmap = bitmap;
    }

    public SingleDocument(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
