package com.ail;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by Keval on 11-Jan-17.
 *
 * @author {@link 'https://github.com/kevalpatel2106'}
 */

class AILImage {
    private Context context;

    private String uid;

    private AILImagePath imagePath;

    private ImageView imageView;

    private String tag;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public AILImagePath getImagePath() {
        return imagePath;
    }

    void setImagePath(AILImagePath imagePath) {
        this.imagePath = imagePath;
    }

    public ImageView getImageView() {
        return imageView;
    }

    void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public Context getContext() {
        return context;
    }

    void setContext(Context context) {
        this.context = context;
    }

    public String getTag() {
        return tag;
    }

    void setTag(String tag) {
        this.tag = tag;
    }
}
