package com.mayassin.android.adivino;

import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

public class InitialActivity extends AppCompatActivity {


    EditText searchText;
    ImageView cameraIconImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideStatusBar();
        setContentView(R.layout.activity_initial);
        initialize();
        testPostRequest();
    }

    private void testPostRequest() {
        new Thread(new Runnable() {
            public void run() {

                try{
                    Server server = new Server();
                    Log.e("RESPONSE", server.run("http://40.114.51.138:9612/get_score"));

                } catch (Exception e) {
                    Log.e("RESPONSE:ERROR", "error");
                }
            }
        }).start();
    }

    private void initialize() {
        setupBackgroundVideo();
        initializeViews();
        initializeOnClickListeners();
    }

    private void initializeViews() {
        searchText = (EditText) findViewById(R.id.search_edit_text);
        cameraIconImage = (ImageView) findViewById(R.id.camera_icon_image);
    }

    private void initializeOnClickListeners() {
        searchText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    v.clearFocus();
                    Intent intent = new Intent(getApplicationContext(), SearchTextActivity.class);
                    startActivity(intent);

                }
            }
        });
        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cameraIconImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OCRActivity.class);
                startActivity(intent);
            }
        });

    }


    private void hideStatusBar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void setupBackgroundVideo() {

        RelativeLayout rootView=(RelativeLayout) findViewById(R.id.rootLayout);
        Display display=getWindowManager().getDefaultDisplay();
        Point size=new Point();
        display.getSize(size);

        FrameLayout.LayoutParams rootViewParams = (FrameLayout.LayoutParams) rootView.getLayoutParams();
        int videoWidth=864;
        int videoHeight=1280;

        if ((float)videoWidth/(float)videoHeight<(float)size.x/(float)size.y) {
            rootViewParams.width=size.x;
            rootViewParams.height=videoHeight*size.x/videoWidth;
            rootView.setX(0);
            rootView.setY((rootViewParams.height-size.y)/2*-1);
        } else {
            rootViewParams.width=videoWidth*size.y/videoHeight;
            rootViewParams.height=size.y;
            rootView.setX((rootViewParams.width-size.x)/2*-1);
            rootView.setY(0);
        }
        rootView.setLayoutParams(rootViewParams);


        final VideoView mVideoView=(VideoView)findViewById(R.id.background_video);
        mVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.introvideo));
        mVideoView.requestFocus();

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mVideoView.start();
                mediaPlayer.setLooping(true);
            }
        });

    }





}
