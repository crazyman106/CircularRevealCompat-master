package com.vincan.circularrevealcompat.sample;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;

import com.vincan.circularrevealcompat.ViewAnimationCompatUtils;

/**
 * 演示createCircularReveal动画
 *
 * @author vincanyang
 */
public class SecondActivity extends AppCompatActivity {

    public static final String EXTRA_REVEAL_CENTER_X = "EXTRA_REVEAL_CENTER_X";
    public static final String EXTRA_REVEAL_CENTER_Y = "EXTRA_REVEAL_CENTER_Y";
    private int revealCenterX;
    private int revealCenterY;
    private LinearLayout revealLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        revealLayout = (LinearLayout) findViewById(R.id.reveal_layout);
        initView();
    }
    private void initView(){
        revealCenterX = getScreenWidth(this)/2;
        revealCenterY = getScreenHeight(this)/2;
        revealLayout.setVisibility(View.INVISIBLE);
        revealLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                revealActivity(revealLayout, revealCenterX, revealCenterY);
                revealLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                return false;
            }
        });
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        Log.e("height:", outMetrics.heightPixels + "");
        return outMetrics.heightPixels;
    }

    private void revealActivity(View revealView, int startCenterX, int startCenterY) {
        int endCenterX = revealView.getWidth() / 2;
        int endCenterY = revealView.getHeight() / 2;
        float startRadius = 0;
        float finalRadius = Math.max(revealView.getWidth(), revealView.getHeight()) * 1.1f;
        Animator circularReveal = ViewAnimationCompatUtils.createCircularReveal(revealView, startCenterX, startCenterY, startRadius, endCenterX, endCenterY, finalRadius);
        circularReveal.setDuration(500);
        circularReveal.setInterpolator(new AccelerateInterpolator());
        revealView.setVisibility(View.VISIBLE);
        circularReveal.start();
    }

    private void unRevealActivity(final View revealView, int endCenterX, int endCenterY) {
        int startCenterX = revealView.getWidth() / 2;
        int startCenterY = revealView.getHeight() / 2;
        float startRadius = Math.max(revealView.getWidth(), revealView.getHeight()) * 1.1f;
        float finalRadius = 0;
        Animator circularReveal = ViewAnimationCompatUtils.createCircularReveal(revealView, startCenterX, startCenterY, startRadius, endCenterX, endCenterY, finalRadius);
        circularReveal.setDuration(500);
        circularReveal.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                revealView.setVisibility(View.INVISIBLE);
                finish();
            }
        });
        circularReveal.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        unRevealActivity(revealLayout, revealCenterX, revealCenterY);
        return super.onTouchEvent(event);
    }
}