package kevalpatel2106.urlimagedownloader;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


/**
 * Created by KP on 14-Apr-15.
 */
public class Downloader {

    private final Context context;
    private final ArrayList<String> downloadQueue;
    private static Downloader imageDownloader;
    private final String storagePath;

    private Downloader(Context context) {
        downloadQueue = new ArrayList<>();
        this.context = context;
        storagePath = context.getExternalCacheDir().getAbsolutePath() + "/downloader";
        File dir = new File(storagePath);
        dir.mkdir();
    }

    /* Static 'instance' method */
    public static Downloader getInstance(Context context) {
        if (imageDownloader == null)
            imageDownloader = new Downloader(context);
        return imageDownloader;
    }

    public void addNewUrl(String urlToDownload) {
        downloadQueue.add(urlToDownload);
        if (downloadFileAync.getStatus() != AsyncTask.Status.RUNNING) {
            //if the size is 1, than start the download queue
            downloadFileAync.execute();
        }
    }

    AsyncTask<Void, String, Void> downloadFileAync = new AsyncTask<Void, String, Void>() {
        public File file;

        @Override
        protected Void doInBackground(Void... voids) {
             /*--- this method downloads an Image from the given URL,
              *  then decodes and returns a Bitmap object*/

            for (int i = 0; i < downloadQueue.size(); i++) {
                Log.d("i", i + "");
                URL url = null;
                String urlDownload = downloadQueue.get(i);
                try {
                    url = new URL(urlDownload);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                HttpURLConnection connection = null;
                InputStream input = null;
                OutputStream output = null;
                try {
                    if (url != null)
                        connection = (HttpURLConnection) url.openConnection();

                    if (connection != null) {
                        //Connection
                        connection.setDoInput(true);
                        connection.setReadTimeout(10000);
                        connection.connect();

                        // download the file
                        input = new BufferedInputStream(url.openStream());

                        //Generating the file name
                        String[] s = urlDownload.split("/");
                        String filename = s[s.length - 1];

                        //creating file to store image
                        file = new File(storagePath, filename);
                        output = new FileOutputStream(file);

                        //write to the file
                        int dataInt;
                        byte[] byteArray;
                        while ((dataInt = input.read()) != -1) {
                            output.write(dataInt);
                        }
                    }
                } catch (IOException | NullPointerException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) connection.disconnect();
                    try {
                        if (input != null) input.close();
                        if (output != null) output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }
    };

    class downloadItems {
        String url;
        ImageView imageView;
    }
}