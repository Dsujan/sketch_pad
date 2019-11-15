package com.example.genesis.sketchpad;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private SketchPadView sketchView;
    private static View bottom_toolbar_view;
    private  static LinearLayout wrapper_layout,brush_holder_layout, color_palette_layout;
    private int selectedColor= Color.BLACK;
    private int selectedBrush = 10;

    //requestCode for the permissions
    final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 1;
    final int MY_PERMISSIONS_WRITE_SETTINGS = 2 ;

   static Boolean isToolBarUp = false;


    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sketchView = (SketchPadView) findViewById(R.id.custom_view);
        bottom_toolbar_view = findViewById(R.id.bottom_toolbar);


        wrapper_layout = (LinearLayout)findViewById(R.id.wrapper_linear_layout);
        color_palette_layout = (LinearLayout)findViewById(R.id.brush_color_linear_layout);
        brush_holder_layout = (LinearLayout)findViewById(R.id.brush_size_linear_layout);


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS)!= PackageManager.PERMISSION_GRANTED){

            //Asking for permission if permission isn't granted

            //Asking permission for writting to external storage
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_SETTINGS},
                    MY_PERMISSIONS_WRITE_SETTINGS);



        }else{

            //Both permissions are granted


        }







    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }


                return;
            }
            case MY_PERMISSIONS_WRITE_SETTINGS:{

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


    //clear sketchpad
    public void clear_Clicked(View view){

        sketchView.clearAll();
    }
    public void undo_Clicked(View view){

        sketchView.undoDrawing();



    }

    public void redo_Clicked(View view){
        sketchView.redoDrawing();
    }

    public void brush_Clicked(View view){

        showButtonToolbar();

//        //hiding toobar after a delay
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                hideButtonToolbar();
//            }
//        },2000);


    }


    public void showButtonToolbar(){

        if(!isToolBarUp){

            isToolBarUp = true;
            bottom_toolbar_view.setVisibility(View.VISIBLE);

            wrapper_layout.setVisibility(View.VISIBLE);
            brush_holder_layout.setVisibility(View.VISIBLE);
            color_palette_layout.setVisibility(View.VISIBLE);

            TranslateAnimation animate = new TranslateAnimation(
                    0,                 // fromXDelta
                    0,                 // toXDelta
                    bottom_toolbar_view.getHeight(),  // fromYDelta
                    0);                // toYDelta
            animate.setDuration(500);
            animate.setFillAfter(true);
            bottom_toolbar_view.startAnimation(animate);
        }


    }
    public static void hideButtonToolbar(){

        if (isToolBarUp){

            isToolBarUp=false;
            bottom_toolbar_view.setVisibility(View.GONE);



            TranslateAnimation animate = new TranslateAnimation(
                    0,                 // fromXDelta
                    0,                 // toXDelta
                    0,                 // fromYDelta
                    bottom_toolbar_view.getHeight()); // toYDelta
            animate.setDuration(500);
            animate.setFillAfter(true);
            bottom_toolbar_view.startAnimation(animate);


            wrapper_layout.setVisibility(View.GONE);
            brush_holder_layout.setVisibility(View.GONE);
            color_palette_layout.setVisibility(View.GONE);

        }


    }

    public void changeBrushSize(View view){

        if(view.getId()==R.id.first_btn_size){

            selectedBrush=getResources().getInteger(R.integer.first_size);
            sketchView.setupPaintBox(selectedBrush,selectedColor);
        }
        if(view.getId()==R.id.second_btn_size){

            selectedBrush=getResources().getInteger(R.integer.second_size);
            sketchView.setupPaintBox(selectedBrush,selectedColor);
        }
        if(view.getId()==R.id.third_btn_size){

            selectedBrush=getResources().getInteger(R.integer.third_size);
            sketchView.setupPaintBox(selectedBrush,selectedColor);
        }
        if(view.getId()==R.id.fourth_btn_size){

            selectedBrush=getResources().getInteger(R.integer.fourth_size);
            sketchView.setupPaintBox(selectedBrush,selectedColor);
        }
        if(view.getId()==R.id.fifth_btn_size){

            selectedBrush=getResources().getInteger(R.integer.fifth_size);
            sketchView.setupPaintBox(selectedBrush,selectedColor);
        }
    }
    public void selectBrushColor(View view){

        if(view.getId()==R.id.colorRed){

            selectedColor = getResources().getColor(R.color.colorRed);
            updateBrushCircleColors(selectedColor);
            sketchView.setupPaintBox(selectedBrush,selectedColor);

        }
        if(view.getId()==R.id.colorGreen){

            selectedColor = getResources().getColor(R.color.colorGreen);
            updateBrushCircleColors(selectedColor);
            sketchView.setupPaintBox(selectedBrush,selectedColor);
        }
        if(view.getId()==R.id.colorPrimaryDark){

            selectedColor = getResources().getColor(R.color.colorPrimaryDark);
            updateBrushCircleColors(selectedColor);
            sketchView.setupPaintBox(selectedBrush,selectedColor);
        }
        if(view.getId()==R.id.colorTeal){

            selectedColor = getResources().getColor(R.color.colorTeal);
            updateBrushCircleColors(selectedColor);
            sketchView.setupPaintBox(selectedBrush,selectedColor);
        }
        if(view.getId()==R.id.colorOrange){

            selectedColor = getResources().getColor(R.color.colorOrange);
            updateBrushCircleColors(selectedColor);
            sketchView.setupPaintBox(selectedBrush,selectedColor);
        }
        if(view.getId()==R.id.colorYellow){

            selectedColor = getResources().getColor(R.color.colorYellow);
            updateBrushCircleColors(selectedColor);
            sketchView.setupPaintBox(selectedBrush,selectedColor);
        }


    }

    public void updateBrushCircleColors(int color){


        findViewById(R.id.first_btn_size).setBackgroundColor(color);
        findViewById(R.id.second_btn_size).setBackgroundColor(color);
        findViewById(R.id.third_btn_size).setBackgroundColor(color);
        findViewById(R.id.fourth_btn_size).setBackgroundColor(color);
        findViewById(R.id.fifth_btn_size).setBackgroundColor(color);

    }
    //select an eraser
    public void eraser_Clicked(View v){

        sketchView.setupPaintBox(selectedBrush,Color.WHITE);

    }

    public void save_Clicked(View v){

        sketchView.saveDrawing();

    }

    public void new_Clicked(View V){

        isToolBarUp = false;
        selectedBrush = 10;
        selectedColor= Color.BLACK;

        updateBrushCircleColors(selectedColor);

        sketchView.reloadView();


    }


    
}
