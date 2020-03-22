package com.romanoindustries.booksearch.imagetransformation;

import android.widget.ImageView;

import com.romanoindustries.booksearch.networkutils.BookNetworkUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class PicassoHelper {

    private PicassoHelper(){}

    public static void loadThumbnail(String url, ImageView target) {
        int radius = 5;
        int margin = 5;
        Transformation transformation = new RoundedCornersTransformation(radius, margin);
        if (url == null || url.equals("")) {
            Picasso.get().load(BookNetworkUtils.NO_IMAGE_AVAILABLE_URL).transform(transformation).into(target);
        } else {
            Picasso.get().load(url).transform(transformation).into(target);
        }
    }
}
