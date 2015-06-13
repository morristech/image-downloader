package kevalpatel2106.urlimagedownloader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {
    private static Downloader imageDownloader;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;
    private ImageView imageView5;
    private ImageView imageView6;
    private ImageView imageView7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageDownloader = Downloader.getInstance(this);

        imageView1 = (ImageView) findViewById(R.id.imageView);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView3 = (ImageView) findViewById(R.id.imageView3);
        imageView4 = (ImageView) findViewById(R.id.imageView4);
        imageView5 = (ImageView) findViewById(R.id.imageView5);
        imageView6 = (ImageView) findViewById(R.id.imageView6);
        imageView7 = (ImageView) findViewById(R.id.imageView7);
    }

    public void loadnew(View view) {

        String[] textUrls = {"http://buzztcdn.blob.core.windows.net/gpics/group-default-thumb.png",
                "http://buzztcdn.blob.core.windows.net/gpics/fd23032c-23de-4c1b-bbec-d4ff6753c094-thumb.jpg",
                "http://38.media.tumblr.com/avatar_c5bd40de9664_64.png",
                "http://buzztcdn.blob.core.windows.net/gpics/group-default-thumb.png",
                "http://buzztcdn.blob.core.windows.net/gpics/fd23032c-23de-4c1b-bbec-d4ff6753c094-thumb.jpg",
                "http://38.media.tumblr.com/avatar_c5bd40de9664_64.png",
                "http://blog.jimdo.com/wp-content/uploads/2014/01/tree-247122.jpg"};

        ImageView[] imageViews = {imageView1, imageView2, imageView3, imageView4, imageView5, imageView6, imageView7};
        for (int i = 0; i < textUrls.length; i++) {
            imageDownloader.addNewUrl(textUrls[i], imageViews[i]);
        }
    }
}
