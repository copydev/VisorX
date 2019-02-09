package com.example.visorx;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Splash extends AppCompatActivity {

    Button getStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getStarted = findViewById(R.id.getStarted);



        ((View) getStarted).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Splash.this,MainActivity.class);
                startActivity(intent);
                //overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
            }
        });

        setAlphaAnimation(getStarted);

        new CountDownTimer(2000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {

            }
        }.start();
    }

    public static void setAlphaAnimation(View v) {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(v, "alpha",  1f, .3f);
        fadeOut.setDuration(1000);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(v, "alpha", .3f, 1f);
        fadeIn.setDuration(1000);

        final AnimatorSet mAnimationSet = new AnimatorSet();

        mAnimationSet.play(fadeIn).after(fadeOut);

        mAnimationSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mAnimationSet.start();
            }
        });
        mAnimationSet.start();
    }
}
