package com.ail;

import java.util.UUID;

/**
 * Created by Keval on 11-Jan-17.
 *
 * @author {@link 'https://github.com/kevalpatel2106'}
 */

class AILQueue {
    private static AILQueue sInstance;

    private AILQueue() {
        if (sInstance != null)
            throw new RuntimeException("Cannot initialize " + getClass().getSimpleName() + ".");

        sInstance = new AILQueue();
    }

    static AILQueue getInstance(){
        if (sInstance == null) synchronized (AILQueue.class) {
            if (sInstance == null) sInstance = new AILQueue();
        }
        return sInstance;
    }

    String add(AILImage image){
        return UUID.randomUUID().toString();
    }

}
