package kevalpatel2106.urlimagedownloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by KP on 12-Jun-15.
 *
 * This class will download image and handles caching of the image.
 */
public class Downloader {
    private static Downloader sInstance;    //sole instance
    private final ArrayList<DownloadItems> downloadQueue;   //download queue
    private final String storagePath;                       //the path where you need to save downloaded images
    private boolean isAsyncTaskFinished = true;
    private LruCache<String, Bitmap> mMemoryCache;          //memory cache

    /**
     * @param context: context of the caller activity
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private Downloader(Context context) {
        if (sInstance != null) {
            //Throw runtime exception if instance is already not null
            throw new RuntimeException("Cannot use constructor. Use getInstance() instead.");
        }

        //initialize the download queue
        downloadQueue = new ArrayList<>();

        //create the storage dir where we are going to store the downloaded data
        //noinspection ConstantConditions
        if (context.getExternalCacheDir() != null)
            storagePath = context.getExternalCacheDir().getAbsolutePath() + "/downloader";
        else
            storagePath = context.getCacheDir().getAbsolutePath() + "/downloader";

        File dir = new File(storagePath);
        dir.mkdir();


        /******Handling cache memory*****/
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return (bitmap.getRowBytes() * bitmap.getHeight()) / 1024;
            }
        };
    }

    /**
     * Static 'instance' method
     * this will return singleton instance for imageUrlDownloader
     *
     * @param context : context of the caller activity
     */
    public static Downloader getInstance(Context context) {
        //get the singleton instance of the activity
        if (sInstance == null) synchronized (Downloader.class) {
            //perform double check
            if (sInstance == null) sInstance = new Downloader(context);
        }
        return sInstance;
    }

    /**
     * Add image url to display into single download queue
     *
     * @param urlOfImage  : url of the image to download
     * @param imageView   : image view where to display the image.
     *                    If this is "null", that means image is not going to display anywhere.(just downloading image.)
     * @param needToCache : true indicates that downloaded bitmap need to cache in LRU cache
     * @param listener    : A listener to call when bitmap downloaded
     */
    public void addNewUrl(@NonNull final String urlOfImage, @Nullable final ImageView imageView,
                          final boolean needToCache, @Nullable final UrlBitmapListener listener) {

        //Add new url and respective imageView to downloadQueue
        final DownloadItems downloadItems = new DownloadItems();
        downloadItems.imageView = imageView;
        downloadItems.url = urlOfImage;
        downloadItems.listener = listener;
        downloadItems.needToCache = needToCache;

        //clear the download queue if previous task was finished
        if (isAsyncTaskFinished) clearDownloadQueue();

        //Add new download item to queue
        downloadQueue.add(downloadItems);

        if (isAsyncTaskFinished) {
            //if previous async task was finished than... start new task
            AsyncTask<Void, DownloadItems, Void> downloadTask = new AsyncTask<Void, DownloadItems, Void>() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    isAsyncTaskFinished = false;    //update flag indicating that async task queue completed
                }

                @Override
                protected Void doInBackground(Void... voids) {
                    /**
                     * This will check image availability as...
                     * 1.   if the image is available in LruCache?
                     * 2.   if image is available in external cache(downloaded before.)
                     * 3.   download the image using volley if not available
                     *
                     * than downloads the image and publish the update so that
                     * it can show the image in the given image viewer
                     */

                    for (int i = 0; i < downloadQueue.size(); i++) {
                        //url of the image
                        final String finalUrlToDownload = downloadQueue.get(i).url;
                        final ImageView finalImageView = downloadQueue.get(i).imageView;
                        final UrlBitmapListener finalListener = downloadQueue.get(i).listener;

                        final File file = generateFileFromUrl(finalUrlToDownload);

                        //generate file name of the downloaded file. (last part of url)
                        String[] s = finalUrlToDownload.split("/");
                        final String filename = finalUrlToDownload.split("/")[s.length - 1];

                        //Check if the image is already present in cache?
                        Bitmap bitmapFromCache = getBitmapFromMemCache(filename);
                        if (bitmapFromCache == null) {

                            //image is not in cache.Check if image is in storage?
                            if (needDownload(filename)) {

                                //image is not stored. download image
                                final ImageRequest imageRequest = new ImageRequest(finalUrlToDownload,
                                        new Response.Listener<Bitmap>() {
                                            @Override
                                            public void onResponse(Bitmap bitmap) {
                                                //Store the bitmap to storage
                                                FileOutputStream fOut = null;

                                                //write it into file
                                                try {
                                                    fOut = new FileOutputStream(file);
                                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                                                    fOut.flush();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                } finally {
                                                    try {
                                                        if (fOut != null) fOut.close();
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                                //set last modified timestamp so will be useful to clear the cache
                                                //noinspection ResultOfMethodCallIgnored
                                                file.setLastModified(System.currentTimeMillis());

                                                //publishing the update
                                                DownloadItems items = new DownloadItems();
                                                items.url = finalUrlToDownload;
                                                items.imageView = finalImageView;
                                                items.listener = finalListener;
                                                items.imageBitmap = bitmap;
                                                publishProgress(items);

                                                //if image is of need to cache in memory
                                                if (downloadItems.needToCache)
                                                    addBitmapToMemoryCache(filename, bitmap);
                                            }
                                        },
                                        300, 300, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.ARGB_8888,
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError volleyError) {
                                                //what to do if error generated?
                                            }
                                        });
                                imageRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 0, 0));
                                VolleySingleton.getInstance().getRequestQueue().add(imageRequest);
                            } else {
                                //returns the already present file from external storage cache
                                if (file != null && file.exists()) {
                                    //set last modified timestamp so will be useful to clear the unused image from storage dir.
                                    //noinspection ResultOfMethodCallIgnored
                                    file.setLastModified(System.currentTimeMillis());

                                    //publishing the update
                                    DownloadItems items = new DownloadItems();
                                    items.url = downloadQueue.get(i).url;
                                    items.imageView = downloadQueue.get(i).imageView;
                                    items.imageBitmap = decodeBitmap(file);
                                    items.listener = finalListener;
                                    publishProgress(items);
                                }
                            }
                        } else {
                            //image is in cached memory
                            //publishing the update
                            DownloadItems items = new DownloadItems();
                            items.url = finalUrlToDownload;
                            items.imageView = finalImageView;
                            items.listener = finalListener;
                            items.imageBitmap = bitmapFromCache;
                            publishProgress(items);
                        }
                    }
                    //set asyncTask finished
                    isAsyncTaskFinished = true;
                    return null;
                }


                @Override
                protected void onProgressUpdate(DownloadItems... downloadItems) {
                    super.onProgressUpdate(downloadItems);
                    //show the bitmap into imageViewer
                    //if there is an null image view than that means the file is only for downloading.
                    if (downloadItems[0].imageView != null) {
                        //not null, so display bitmap to imageView
                        downloadItems[0].imageView.setImageBitmap(downloadItems[0].imageBitmap);
                    }

                    //call respective listener
                    if (downloadItems[0].listener != null)
                        downloadItems[0].listener.onBitmapDownloaded(downloadItems[0].url, downloadItems[0].imageBitmap);
                }
            };

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                //Multiple parallel async task for > android 3.0
                downloadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                downloadTask.execute();
            }
        }
    }

    /**
     * Add bitmap to Lru cache for faster access
     *
     * @param key    : key of stored bitmap
     * @param bitmap : bitmap to cache
     */
    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    /**
     * Returns bitmap from LruCache
     *
     * @param key : key of bitmap to return
     * @return : bitmap if present in cache, else return null
     */
    private Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    /**
     * this method will clear the download queue
     */
    private void clearDownloadQueue() {
        downloadQueue.clear();
    }

    /**
     * his method will check if the file needs to be download or its already present into the cache
     *
     * @param filename : name of the file needs to check
     * @return : true if not present in storage.(need to download)
     */
    private boolean needDownload(final String filename) {
        //check if file is present???
        return !new File(storagePath + "/" + filename).exists();
    }

    /**
     * this method wil return a file from given url to store image into ext. storage cache
     *
     * @param url : url of an image
     * @return : file to store that image
     */
    private File generateFileFromUrl(final String url) {
        //this method wil return a file from given url
        //Generating the file name
        File dir = new File(storagePath);
        //noinspection ResultOfMethodCallIgnored
        dir.mkdir();

        String[] s = url.split("/");
        String filename = s[s.length - 1];

        //this will generate file from its name not url
        return new File(storagePath, filename);
    }

    /**
     * It will decode the bitmap from given file and also resize it if needed to prevent OutOfMemory errors.
     * For thumbnails it will add bitmap to LruCache.
     *
     * @param file : file to decode
     * @return : decoded bitmap
     */
    private Bitmap decodeBitmap(final File file) {
        //Decoding the file into bitmap
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateInSampleSize(options, 300, 300);
        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
    }

    /**
     * function to calculate sample size in image for batter memory usage.
     *
     * @param options   : bitmap  options
     * @param reqWidth  : required width
     * @param reqHeight : required height
     * @return sample in size
     */
    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    //class for downloaded items
    class DownloadItems {
        String url;
        ImageView imageView;
        Bitmap imageBitmap;
        boolean needToCache;
        UrlBitmapListener listener;
    }
}