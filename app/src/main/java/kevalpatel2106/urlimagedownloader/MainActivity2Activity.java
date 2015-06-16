package kevalpatel2106.urlimagedownloader;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;


public class MainActivity2Activity extends AppCompatActivity {

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
        setContentView(R.layout.activity_main_activity2);
        imageDownloader = Downloader.getInstance(this);

        imageView1 = (ImageView) findViewById(R.id.imageView8);
        imageView2 = (ImageView) findViewById(R.id.imageView9);
        imageView3 = (ImageView) findViewById(R.id.imageView10);
        imageView4 = (ImageView) findViewById(R.id.imageView11);
        imageView5 = (ImageView) findViewById(R.id.imageView12);
        imageView6 = (ImageView) findViewById(R.id.imageView13);
        imageView7 = (ImageView) findViewById(R.id.imageView14);

        loadnew1();
    }

    public void loadnew1() {
        Log.d("main activity2", "loading");
        String[] textUrls = {"http://buzztcdn.blob.core.windows.net/gpics/group-default-thumb.png",
                "http://buzztcdn.blob.core.windows.net/gpics/fd23032c-23de-4c1b-bbec-d4ff6753c094-thumb.jpg",
                "http://38.media.tumblr.com/avatar_c5bd40de9664_64.png",
                "http://buzztcdn.blob.core.windows.net/gpics/group-default-thumb.png",
                "http://buzztcdn.blob.core.windows.net/gpics/fd23032c-23de-4c1b-bbec-d4ff6753c094-thumb.jpg",
                "http://www.15min.lt/images/photos/616321/big/rusijos-okupuotos-ldk-ir-lenkijos-teritorijos-51935383576a6.jpg",
                "http://ritoania.info/wp-content/uploads/2010/10/ldk_paveldo_centrai.jpg"};

        ImageView[] imageViews = {imageView1, imageView2, imageView3, imageView4, imageView5, imageView6, imageView7};
        for (int i = 0; i < textUrls.length; i++) {
            imageDownloader.addNewUrl(textUrls[i], imageViews[i]);
        }
    }
}
