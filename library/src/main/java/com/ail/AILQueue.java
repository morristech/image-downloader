package com.ail;

import android.net.Uri;
import android.widget.ImageView;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

/**
 * Created by Keval on 11-Jan-17.
 *
 * @author {@link 'https://github.com/kevalpatel2106'}
 */

class AILQueue {
    private volatile static AILQueue sInstance;

    private ArrayList<AILImage> mAILImages;

    private AILQueue() {
        if (sInstance != null)
            throw new RuntimeException("Cannot initialize " + getClass().getSimpleName() + ".");

        sInstance = new AILQueue();
    }

    static AILQueue getInstance() {
        if (sInstance == null) synchronized (AILQueue.class) {
            if (sInstance == null) sInstance = new AILQueue();
        }
        return sInstance;
    }

    String add(AILImage image) {
        if (mAILImages == null) mAILImages = new ArrayList<>();

        image.setUid(UUID.randomUUID().toString());
        mAILImages.add(image);
        return image.getUid();
    }

    boolean removeByUid(String uid) {
        Iterator<AILImage> iterator = mAILImages.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getUid().equals(uid)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    boolean removeByPath(String path) {
        Iterator<AILImage> iterator = mAILImages.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getImagePath().equal(path)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    boolean removeByUrl(URL url) {
        Iterator<AILImage> iterator = mAILImages.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getImagePath().equal(url)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    boolean removeByUri(Uri uri) {
        Iterator<AILImage> iterator = mAILImages.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getImagePath().equal(uri)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }
}
