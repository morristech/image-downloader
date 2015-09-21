package kevalpatel2106.urlimagedownloader;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.NoCache;

/**
 * Created by KP on 05-Jul-15.
 * This class is singleton class for the volley initialization.
 * It will initialize volley request queue.
 */
public class VolleySingleton {
    private static VolleySingleton instance;
    private RequestQueue mRequestQueue;

    /**
     * Custom request queue.
     * Most code copied from "Volley.newRequestQueue(..)", we only changed cache directory
     */
    private static RequestQueue newRequestQueue() {
        // define cache folder
        RequestQueue queue = new RequestQueue(new NoCache(), new BasicNetwork(new HurlStack()));
        queue.start();
        return queue;
    }

    /**
     * private constructor
     */
    private VolleySingleton() {
        //initialize the request queue
        mRequestQueue = newRequestQueue();
    }

    /**
     * get singleton instance of the volley singleton
     *
     * @return : instance if this class
     */
    public static VolleySingleton getInstance() {
        //using double check method for making singleton thread safe
        if (instance == null) synchronized (VolleySingleton.class) {
            if (instance == null) instance = new VolleySingleton();
        }
        return instance;
    }

    /**
     * get the request queue object
     *
     * @return : single request queue
     */
    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }
}
