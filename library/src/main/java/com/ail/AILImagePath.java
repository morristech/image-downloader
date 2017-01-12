package com.ail;

import android.net.Uri;

import java.net.URL;

/**
 * Created by Keval on 11-Jan-17.
 *
 * @author {@link 'https://github.com/kevalpatel2106'}
 */

class AILImagePath {

    private String path;

    AILImagePath(Uri uri) {

    }

    AILImagePath(String uri) {

    }

    AILImagePath(URL uri) {

    }

    boolean equal(String path) {
        return this.path.equals(path);
    }

    boolean equal(Uri path) {
        return this.path.equals(path.toString());
    }

    boolean equal(URL path) {
        return this.path.equals(path.toString());
    }
}
