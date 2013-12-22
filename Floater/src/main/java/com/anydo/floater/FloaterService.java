package com.anydo.floater;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by igor on 15/12/13.
 */
public class FloaterService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("igor", "FloaterService started");

        final WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        final View baseView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.reminder_floater, null,false);


//
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.y = 0;

        final View topLayer = baseView.findViewById(R.id.top_layer);
        final View bottomLayer = baseView.findViewById(R.id.bottom_layer);

        baseView.setAnimation(new AlphaAnimation(1f,0f));

//        bottomLayer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "CLICK", Toast.LENGTH_SHORT).show();
//                ArrayList<Animator> animList = new ArrayList<Animator>();
//                animList.add(ObjectAnimator.ofFloat(bottomLayer,View.TRANSLATION_Y,200f));
//                AnimatorSet animSet = new AnimatorSet();
//                animSet.playTogether(animList);
//                animSet.setDuration(1500);
//                animSet.start();
//
////                v.setTranslationY(v.getY() + 100);
////                v.invalidate();
//            }
//        });
//
//        baseView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                wm.removeView(v);
//                return true;
//            }
//        });

        bottomLayer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        baseView.setAnimation(new AlphaAnimation(1f,0f));
                        Animation animation = baseView.getAnimation();
                        animation.setDuration(500);
                        animation.startNow();
                        wm.updateViewLayout(baseView,baseView.getLayoutParams());
                        Toast.makeText(getApplicationContext(), "DOWN", Toast.LENGTH_SHORT).show();
                        return true;
                    case MotionEvent.ACTION_UP:
                        Toast.makeText(getApplicationContext(), "UP", Toast.LENGTH_SHORT).show();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        return true;
                }


                return true;
            }
        });


        topLayer.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = (int) v.getX();
//                        initialY = (int) v.getY();
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        topLayer.setX(initialX + (int) (event.getRawX() - initialTouchX));
                        baseView.invalidate();
//                        v.setY(initialY + (int) (event.getRawY() - initialTouchY));
//                        v.invalidate();
                        return true;
                }
                return false;
            }
        });


        wm.addView(baseView,params);
    }

}
