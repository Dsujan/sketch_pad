package com.example.genesis.sketchpad;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.os.Environment;
import android.provider.ContactsContract;
import android.text.method.Touch;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Genesis on 12/4/2018.
 */
public class SketchPadView extends View {




    //path for sketch
    private Path sketchPath;

    //what to draw
    private Paint canvasPaint;

    //how to draw
    private Paint sketchPen;

    private  Bitmap bmp;

    //canvas for drawing
    private Canvas sketchCanvas;

    //bitmap for the canvas
    private Bitmap canvasBmp;

    //brush size
    private float currBrushSize, prevBrushSize;

    private  boolean isSave = false;




    //holds DrawAssist objects
    private ArrayList<DrawAssist> drawAssistsArray = new ArrayList<DrawAssist>();

    //holds removed DrawAssist objects for redo action
    private ArrayList<DrawAssist> prevDrawAssistsArray = new ArrayList<DrawAssist>();

    private  float rX,rY;




    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    String uniqueId = dateFormat.format(new Date());

    String saveIn = Environment.getExternalStorageDirectory().toString()+"/sketches/";
    String fileName= "SKETCH_IMG_"+uniqueId+".jpg";


    private final float TOUCH_TOLERANCE = 1;


    public SketchPadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDrawingCacheEnabled(true);
        setupPaintBox(10,Color.BLACK);
        setBackgroundColor(Color.WHITE);
        setWillNotDraw(false);

    }



    /**
     * Initializing graphics object variables
     * Early initializing speeds up performance
     */
    public void setupPaintBox(int brushSize,int paintColor){


        currBrushSize = brushSize;
        prevBrushSize = currBrushSize;


        sketchPath = new Path();
        sketchPen = new Paint();
        sketchPen.setAntiAlias(true);
        sketchPen.setStyle(Paint.Style.STROKE);
        sketchPen.setStrokeJoin(Paint.Join.ROUND);
        sketchPen.setStrokeCap(Paint.Cap.ROUND);
        sketchPen.setColor(paintColor);
        sketchPen.setStrokeWidth(currBrushSize);

        DrawAssist drawAssistObj = new DrawAssist();

        drawAssistObj.setPath(sketchPath);
        drawAssistObj.setPaint(sketchPen);

        drawAssistsArray.add(drawAssistObj);

        canvasPaint = new Paint(Paint.DITHER_FLAG);

    }

    @Override
    protected void onDraw(Canvas canvas) {



        canvas.drawBitmap(canvasBmp,0,0, canvasPaint);

        //drawing from the array list object

        for(DrawAssist p : drawAssistsArray){

            canvas.drawPath(p.getPath(),p.getPaint());
        }





    }

    /**
     *
     * @param w present width
     * @param h  present height
     * @param oldw old width
     * @param oldh old height
     *
     * Defines what to do when screen size of the view is changed
     *
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        super.onSizeChanged(w, h, oldw, oldh);
        canvasBmp = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
    }

    /**
     *
     * @param event
     * @return
     *
     *
     * Handeling the touch on canvas to create a path and paint it
     *
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float pointX = event.getX();
        float pointY = event.getY();
        float prevPointX;
        float prevPointY;
        boolean isMoving = false;


        //handling different type of touch gestures
        switch(event.getAction()){
            //finger touches for first time
            case MotionEvent.ACTION_DOWN:
                initialTouch(pointX,pointY);
                invalidate();
                Log.d("1","Touchdown");
                break;
            //finger is moved
           case MotionEvent.ACTION_MOVE:
                touchInMotion(pointX,pointY);
                invalidate();
                Log.d("2","TouchMove");
                isMoving = true;
                break;
             //finger is lifted
            case MotionEvent.ACTION_UP:
                touchHalt(pointX,pointY);
                invalidate();
                Log.d("3","TouchUp");
                break;
            default:
                return false;
        }
        //calls onDraw to update the view
        invalidate();

        return true;
    }

    //clear sketchpad
    public void clearAll(){

    sketchPath.reset();

        drawAssistsArray.clear();
        invalidate();
    }

    /**
     *
     * @param pointX
     * @param pointY
     * Handels the touch event when finger is palced on the sketch area
     */
    public  void initialTouch(float pointX,float pointY){


//        prevPaths.clear();
        sketchPath.reset();

        //moving  the path to the area where touch gesture occurs
        //path  starting point is set to this
        sketchPath.moveTo(pointX,pointY);

        //initial touch points are saved as previous points to have a complete map from a(previous point) to b(current point).
        rX = pointX;
        rY = pointY;

        DrawAssist initialDrawObjs = new DrawAssist();
        initialDrawObjs.setPath(sketchPath);
        initialDrawObjs.setPaint(sketchPen);

        drawAssistsArray.add(initialDrawObjs);


        MainActivity.hideButtonToolbar();
        prevDrawAssistsArray.clear();

    }

    /**
     *
     * @param pointX
     * @param pointY
     * Handels drawing touch event when finger is moved
     */
    public  void touchInMotion(float pointX,float pointY){

        //calculating change in points
        float dX = Math.abs(pointX-rX );
        float dY = Math.abs(pointY-rY);

        //touch tolearace is compared to define how much swiping  is a finger movement.
        if(dX >=TOUCH_TOLERANCE || dY>=TOUCH_TOLERANCE ){

            //quadto to draw a curve from the previous point to current point.
            sketchPath.quadTo(rX,rY,(pointX+rX)/2,(pointY+rY)/2);

        }

        //saving current point as previous point.
        rX = pointX;
        rY = pointY;

    }

    /**
     *
     * @param pointX
     * @param pointY
     * Handels finishing drawing when finger is lifted
     */
    public  void touchHalt(float pointX,float pointY){

        //drawing line to last points
        sketchPath.lineTo(rX,rY);
        //new path for next stroke
        sketchPath = new Path();

    }

    //undo aciton
    public void undoDrawing(){



        if(drawAssistsArray.size()>0){

          //removes draw Assists objects and adds it to redo array list(for redo purpose)
          prevDrawAssistsArray.add(drawAssistsArray.remove(drawAssistsArray.size()-1));
          //cals invalidate to update the changes
          invalidate();


        }



    }

    //redo action
    public void redoDrawing(){

        if(prevDrawAssistsArray.size()>0){

            //removes redo array list objects and then adds to drawassists objects for redo action
           drawAssistsArray.add(prevDrawAssistsArray.remove(prevDrawAssistsArray.size()-1));
            //cals invalidate to update the changes
            invalidate();
        }

    }

    public void saveDrawing(){

        makeDirAndSave();
    }

    public void makeDirAndSave(){
        setDrawingCacheEnabled(true);
        //sketch directory file object
        File directory = new File(saveIn);

        //if sketch directory is not found makes one
        if (!directory.exists()){

            Log.d("Save-info","Directory doesn't exits making one and saving...");
           directory.mkdir();

            try {
                //builds drawing cache
                buildDrawingCache();
                //saves the view drawing cache into as an image file
                getDrawingCache().compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(saveIn+fileName));
                isSave = true;
                Log.d("Save-info","Successfully saved in "+ saveIn +"  with name " +fileName);
                Toast.makeText(getContext().getApplicationContext(),"Saved successfully!",Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            }

        }else{
            //saves image file from the drawing cache to the already available sketch directory
            Log.d("Save-info","Directory already exists.Saving File...");
            try {
                buildDrawingCache();
                getDrawingCache().compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(saveIn+fileName));
                isSave = true;
                Log.d("Save-info","Successfully saved in "+ saveIn +"  with name " +fileName);
                Toast.makeText(getContext().getApplicationContext(),"Saved successfully!",Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            }
        }
        destroyDrawingCache();

    }


    //starting a new paint
    public void reloadView(){

        isSave = false;

        //creating a new file name for the new sketch
        dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        uniqueId = dateFormat.format(new Date());
        saveIn = Environment.getExternalStorageDirectory().toString()+"/sketches";
        fileName= "SKETCH_IMG_"+uniqueId+".jpg";

        //clearing all draw assists array.
        drawAssistsArray.clear();
        prevDrawAssistsArray.clear();


        //resets paint properties to original color and shape.
        setupPaintBox(10,Color.BLACK);
        setBackgroundColor(Color.WHITE);
        //for destroying previous cache
        setDrawingCacheEnabled(false);
        //handels new cache
        setDrawingCacheEnabled(true);

        invalidate();

    }




}