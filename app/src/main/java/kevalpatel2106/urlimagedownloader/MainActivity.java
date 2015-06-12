package kevalpatel2106.urlimagedownloader;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {
    private static Downloader imageDownloader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageDownloader = Downloader.getInstance(this);

        String[] textUrls = {"http://www.gettyimages.in/gi-resources/images/Homepage/Category-Creative/UK/UK_Creative_462809583.jpg",
                "https://www.ucl.ac.uk/news/news-articles/1213/muscle-fibres-heart.jpg",
                "http://clarklabs.org/images/carousel-image-6.jpg",
                "http://www.hdwallpapersimages.com/wp-content/uploads/2014/01/Winter-Tiger-Wild-Cat-Images.jpg",
                "http://3.bp.blogspot.com/-rZmCIp0C-hQ/Tx6aCFeweoI/AAAAAAAAAnQ/WqIEVBTIzRk/s1600/Cool-Tiger-Wallpaper-1920x1080-HD.jpg",
                "http://www.zastavki.com/pictures/originals/2013/Photoshop_Image_of_the_horse_053857_.jpg",
                "http://blog.jimdo.com/wp-content/uploads/2014/01/tree-247122.jpg"};
        for (String textUrl : textUrls) {
            imageDownloader.addNewUrl(textUrl);
        }
    }
}
