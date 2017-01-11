package com.ail;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import java.net.URL;

/**
 * Created by Keval on 11-Jan-17.
 *
 * @author {@link 'https://github.com/kevalpatel2106'}
 */

public class AndroidImageLoader {

    private AILImage mAILImage;

    private AndroidImageLoader(Context context) {
        mAILImage = new AILImage();
        mAILImage.setContext(context);
    }

    public static AndroidImageLoader with(Context context) {
        return new AndroidImageLoader(context);
    }

    public static AndroidImageLoader with(Activity activity) {
        return new AndroidImageLoader(activity);
    }


    public AndroidImageLoader load(Uri uri) {
        mAILImage.setImagePath(new AILImagePath(uri));
        return this;
    }

    public AndroidImageLoader load(String url) {
        mAILImage.setImagePath(new AILImagePath(url));
        return this;
    }

    public AndroidImageLoader load(URL url) {
        mAILImage.setImagePath(new AILImagePath(url));
        return this;
    }


    public AndroidImageLoader setTag(String tag) {
        mAILImage.setTag(tag);
        return this;
    }

    public AndroidImageLoader into(ImageView imageView) {
        mAILImage.setImageView(imageView);
        return this;
    }
}
