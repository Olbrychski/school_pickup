package com.example.settingsscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_SCREEN=5000;

    Animation topAnim,bottomAnim;
    ImageView image;
    Intent intent;
    //Textview logo, slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.activity_splash_screen);

        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        //Hooks
        image = findViewById(R.id.image_logo);
        //logo = findViewById(R.id.textview);
        //slogan = findViewById(R.id.textview2);

        image.setAnimation(topAnim);
        //logo.setAnimation(bottomAnim);
        //slogan.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //if the user is not logged in
                //starting the login activity
                if (!SharedPref.getInstance(SplashScreen.this).isLoggedIn()) {

                    intent = new Intent(SplashScreen.this,Login.class);
                }else{

                    //getting the current user
                    User user = SharedPref.getInstance(SplashScreen.this).getUser();

                    if (user.getRole().equals("Father") || (user.getRole().equals("Mother"))){

                        intent = new Intent(SplashScreen.this, MainActivity.class);

                    }else if (user.getRole().equals("Driver")){

                        intent = new Intent(SplashScreen.this, DriverHomeActivity.class);

                    }


                }


                //Intent intent = new Intent(SplashScreen.this,Login.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View,String>(image, "logo_image");
                //pairs[1]= new Pair<View,String>(logo, "logo_text");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashScreen.this,pairs);
                startActivity(intent,options.toBundle());
            }
        }, SPLASH_SCREEN);




    }
}