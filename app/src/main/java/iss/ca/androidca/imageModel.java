
package iss.ca.androidca;

import android.graphics.Bitmap;

public class imageModel {
    int mThumbIds;
    Bitmap bitmap;

    public imageModel(){

    }

    public imageModel(Bitmap bitmap, int mThumbIds){
        this.bitmap = bitmap;
        this.mThumbIds = mThumbIds;
    }

    public int getmThumbIds() {
        return mThumbIds;
    }
    public void setmThumbIds(int mThumbIds) {
        this.mThumbIds = mThumbIds;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
