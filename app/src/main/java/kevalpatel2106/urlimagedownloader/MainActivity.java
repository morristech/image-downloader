package kevalpatel2106.urlimagedownloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ail.AndroidImageLoader;

/**
 * Created by KP on 12-Jun-15.
 * Main activity.
 */
public class MainActivity extends AppCompatActivity {

    private String[] mImageUrls;
    private ImageView[] mImageViews;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        //initialize view
        ImageView imageView1 = (ImageView) findViewById(R.id.imageView);
        ImageView imageView2 = (ImageView) findViewById(R.id.imageView2);
        ImageView imageView3 = (ImageView) findViewById(R.id.imageView3);
        ImageView imageView4 = (ImageView) findViewById(R.id.imageView4);

        //urls to download (This all urls are for example.)
        mImageUrls = new String[]{
                "http://www.hdwallpapersimages.com/wp-content/uploads/2014/01/Winter-Tiger-Wild-Cat-Images.jpg",
                "http://blog.jimdo.com/wp-content/uploads/2014/01/tree-247122.jpg",
                "http://saturn.jpl.nasa.gov/multimedia/images/moons/images/PIA07759.jpg"};

        //image views to display downloaded images. (you can pass null if you don't want to display images.)
        mImageViews = new ImageView[]{imageView2, imageView3, imageView4};

        AndroidImageLoader
                .with(this)
                .load("http://www.gettyimages.in/gi-resources/images/Homepage/Category-Creative/UK/UK_Creative_462809583.jpg")
                .into(imageView1);
    }

    public void startLoadImage(View view) {
        Downloader imageDownloader = Downloader.getInstance(this);

        //add to the download queue
        for (int i = 0; i < mImageUrls.length; i++) {
            final int finalI = i;
            imageDownloader.addNewUrl(mImageUrls[i], mImageViews[i], true, new UrlBitmapListener() {
                @Override
                public void onBitmapDownloaded(String url, Bitmap bitmap) {
                    //a listener to call when image bitmap is downloaded
                    Toast.makeText(mContext, "Image " + finalI + " downloaded", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
