package kevalpatel2106.urlimagedownloader;

import android.graphics.Bitmap;

/**
 * Created by KP on 9/21/2015.
 *
 */
public interface UrlBitmapListener {
    void onBitmapDownloaded(String url, Bitmap bitmap);
}
