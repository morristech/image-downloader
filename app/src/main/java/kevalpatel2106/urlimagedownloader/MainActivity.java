package kevalpatel2106.urlimagedownloader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by KP on 12-Jun-15.
 * Main activity.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Context context = this;

        Downloader imageDownloader = Downloader.getInstance(this);

        //initialize view
        ImageView imageView1 = (ImageView) findViewById(R.id.imageView);
        ImageView imageView2 = (ImageView) findViewById(R.id.imageView2);
        ImageView imageView3 = (ImageView) findViewById(R.id.imageView3);
        ImageView imageView4 = (ImageView) findViewById(R.id.imageView4);
        ImageView imageView5 = (ImageView) findViewById(R.id.imageView5);
        ImageView imageView6 = (ImageView) findViewById(R.id.imageView6);
        ImageView imageView7 = (ImageView) findViewById(R.id.imageView7);

        //urls to download
        String[] textUrls = {"http://buzztcdn.blob.core.windows.net/gpics/group-default-thumb.png",
                "http://buzztcdn.blob.core.windows.net/gpics/fd23032c-23de-4c1b-bbec-d4ff6753c094-thumb.jpg",
                "http://38.media.tumblr.com/avatar_c5bd40de9664_64.png",
                "http://buzztcdn.blob.core.windows.net/gpics/group-default-thumb.png",
                "http://buzztcdn.blob.core.windows.net/gpics/fd23032c-23de-4c1b-bbec-d4ff6753c094-thumb.jpg",
                "http://38.media.tumblr.com/avatar_c5bd40de9664_64.png",
                "http://blog.jimdo.com/wp-content/uploads/2014/01/tree-247122.jpg"};

        //image views to display downloaded images. (you can pass null if you don't want to display images.)
        ImageView[] imageViews = {imageView1, imageView2, imageView3, imageView4, imageView5, imageView6, imageView7};

        //add to the download queue
        for (int i = 0; i < textUrls.length; i++) {
            final int finalI = i;
            imageDownloader.addNewUrl(textUrls[i], imageViews[i], true, new UrlBitmapListener() {
                @Override
                public void onBitmapDownloaded(String url, Bitmap bitmap) {
                    Toast.makeText(context, "Image " + finalI + " downloaded", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
