package com.example.genesis.sketchpad;

import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by Genesis on 12/9/2018.
 */
public class DrawAssist {

    Paint paint;
    Path path;


    //paint obj getter
    public Paint getPaint() {
        return paint;
    }

   // paint obj setter
    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    //path obj getter
    public Path getPath() {
        return path;
    }

    //path obj setter
    public void setPath(Path path) {
        this.path = path;
    }
}
