package kevalpatel2106.urlimagedownloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
 * Created by KP on 12-Jun-15.
 */
public class Downloader {

    private final Context context;
    private final ArrayList<DownloadItems> downloadQueue;
    private static Downloader imageDownloader;
    private final String storagePath;
    private boolean isAsyncTaskFinished = true;

    /*@param: context of the caller activity
     */
    private Downloader(Context context) {
        downloadQueue = new ArrayList<DownloadItems>();
        this.context = context;

        //create the storage dir where we are going to store the downloaded data
        storagePath = context.getExternalCacheDir().getAbsolutePath() + "/downloader";
        File dir = new File(storagePath);
        dir.mkdir();
    }

    /* Static 'instance' method
    * this is a singleton function
    */
    public static Downloader getInstance(Context context) {
        //get the singleton instance of the activity
        if (imageDownloader == null)
            imageDownloader = new Downloader(context);
        return imageDownloader;
    }

    public void addNewUrl(final String urlOfImage, final ImageView imageView) {
        //Add new url and respective imageView to downloadQueue
        Log.d("downloader Adding item", urlOfImage + " " + imageView + " " + downloadQueue.size());
        DownloadItems downloadItems = new DownloadItems();
        downloadItems.imageView = imageView;
        downloadItems.url = urlOfImage;
        downloadQueue.add(downloadItems);
        if (isAsyncTaskFinished) {
            Log.d("downloader starting", "Task" + downloadQueue.size());
            //if no async task running than...
            new AsyncTask<Void, DownloadItems, Void>() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    isAsyncTaskFinished = false;
                }

                @Override
                protected Void doInBackground(Void... voids) {
                    /* this method downloads an Image from the given URL,
                    * than downloads the image and publish the update so that
                    * it can show the image in the given image viewer
                    */

                    for (int i = 0; i < downloadQueue.size(); i++) {
                        Log.d("downloader image number", i + "");
                        HttpURLConnection connection = null;
                        InputStream input = null;
                        OutputStream output = null;

                        //url of the image
                        final String urlDownload = downloadQueue.get(i).url;
                        File file = null;   //file that will store image downloaded

                        try {
                            if (needDownload(urlDownload)) {
                                Log.d("downloader file", "downloading " + needDownload(urlDownload));
                                //create URL
                                URL url = null;
                                try {
                                    url = new URL(urlDownload);
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }

                                if (url != null)
                                    connection = (HttpURLConnection) url.openConnection();

                                if (connection != null) {
                                    //Connection
                                    connection.setDoInput(true);
                                    connection.setReadTimeout(15000);
                                    connection.setConnectTimeout(15000);
                                    connection.connect();

                                    // download the file
                                    input = new BufferedInputStream(url.openStream());

                                    //write to the file
                                    file = generateFile(urlDownload);
                                    output = new FileOutputStream(file);
                                    int dataInt;
                                    while ((dataInt = input.read()) != -1) {
                                        output.write(dataInt);
                                    }
                                }
                            } else {
                                Log.d("downloader file", "skipping");
                                file = generateFile(urlDownload);
                            }

                            //set last modified timestamp so will be useful to clear the cache
                            file.setLastModified(System.currentTimeMillis());

                            //publishing the update
                            DownloadItems items = new DownloadItems();
                            items.imageView = downloadQueue.get(i).imageView;
                            items.imageBitmap = decodeBitmap(file);
                            publishProgress(items);

                            //clear the download queue if all the urls are downloaded
                            if (i == downloadQueue.size() - 1) {
                                Log.d("downloader clear", "task");
                                clearDownloadQueue();
                            }
                        } catch (IOException | NullPointerException e) {
                            //delete the file if any exception generated
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


                @Override
                protected void onProgressUpdate(DownloadItems... downloadItemes) {
                    super.onProgressUpdate(downloadItemes);
                    Log.d("downloader update", "published" + downloadItemes[0].imageView + downloadItemes[0].imageBitmap);
                    //show the bitmap into imageViewer
                    downloadItemes[0].imageView.setImageBitmap(downloadItemes[0].imageBitmap);
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    isAsyncTaskFinished = true;
                }
            }.execute();
        }
    }

    public void clearDownloadQueue() {
        //clear the download queue
        downloadQueue.clear();
    }

    public boolean deleteCacheFile(final String url) {
        return generateFile(url).delete();
    }

    private boolean needDownload(final String url) {
        //Generating the file name
        String[] s = url.split("/");
        String filename = s[s.length - 1];
        return !new File(storagePath + "/" + filename).exists();
    }

    private File generateFile(final String url) {
        //Generating the file name
        String[] s = url.split("/");
        String filename = s[s.length - 1];

        //creating file to store image
        return new File(storagePath, filename);
    }

    private Bitmap decodeBitmap(final File file) {
        //Decoding the file into bitmap
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDither = false;                     //Disable Dithering mode
        //noinspection deprecation
        options.inPurgeable = true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
    }

    public void deleteUnusedFile() {
        //delete all unused files
        String[] fileList = new File(storagePath).list();
        for (String s : fileList) {
            File f = new File(storagePath + "/" + s);
            if ((System.currentTimeMillis() - f.lastModified()) > 1000) {
                f.delete();
            }
        }

    }

    class DownloadItems {
        String url;
        ImageView imageView;
        Bitmap imageBitmap;
    }
}