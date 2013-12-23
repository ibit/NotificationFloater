package com.anydo.floater;

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
import android.view.ViewTreeObserver;
import android.view.WindowManager;

/**
 * Created by igor on 15/12/13.
 */
public class FloaterService extends Service {

    View mBaseLayer;
    View mTopLayer;
    View mBottomLayer;
    View mExtraOptionsLayer;
    View mLayerContainer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("igor", "FloaterService started");

        final WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        mBaseLayer = LayoutInflater.from(getApplicationContext()).inflate(R.layout.notification_floater, null,false);
        assert mBaseLayer != null;
        mTopLayer = mBaseLayer.findViewById(R.id.reminder_top_layer);
        mBottomLayer = mBaseLayer.findViewById(R.id.reminder_bottom_layer);
        mExtraOptionsLayer = mBaseLayer.findViewById(R.id.reminder_extra_layer);
        mLayerContainer = mBaseLayer.findViewById(R.id.reminder_layer_container);

//
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                /*WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |*/
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.y = 0;
//        params.windowAnimations = android.R.style.Animation_Translucent;

        mTopLayer.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private float initialTouchX;

            @Override public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = (int) v.getX();
                        initialTouchX = event.getRawX();
                        return true;
                    case MotionEvent.ACTION_UP:
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        mTopLayer.setX(initialX + (int) (event.getRawX() - initialTouchX));
                        mBaseLayer.invalidate();
                        return true;
                }
                return false;
            }
        });

        mBottomLayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExtraOptionsLayer.setVisibility(mExtraOptionsLayer.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                ObjectAnimator.ofFloat(mExtraOptionsLayer,View.ALPHA,0f,1f).start();
            }
        });

//        final View topLayer = baseView.findViewById(R.id.top_layer);
//        final View bottomLayer = baseView.findViewById(R.id.bottom_layer);
//
//        baseView.setAnimation(new AlphaAnimation(1f,0f));
//
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
//                filler.setVisibility(View.VISIBLE);
//                v.invalidate();
//
////                v.setTranslationY(v.getY() + 100);
////                v.invalidate();
//            }
//        });
//
//
//        baseView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Log.d("igor","touch");
//                return false;
//            }
//        });
////
////        baseView.setOnLongClickListener(new View.OnLongClickListener() {
////            @Override
////            public boolean onLongClick(View v) {
////                wm.removeView(v);
////                return true;
////            }
////        });
//
////        bottomLayer.setOnTouchListener(new View.OnTouchListener() {
////            @Override
////            public boolean onTouch(View v, MotionEvent event) {
////                switch (event.getAction()) {
////                    case MotionEvent.ACTION_DOWN:
////                        baseView.setAnimation(new AlphaAnimation(1f,0f));
////                        Animation animation = baseView.getAnimation();
////                        animation.setDuration(500);
////                        animation.startNow();
////                        wm.updateViewLayout(baseView,baseView.getLayoutParams());
////                        Toast.makeText(getApplicationContext(), "DOWN", Toast.LENGTH_SHORT).show();
////                        return true;
////                    case MotionEvent.ACTION_UP:
////                        Toast.makeText(getApplicationContext(), "UP", Toast.LENGTH_SHORT).show();
////                        return true;
////                    case MotionEvent.ACTION_MOVE:
////                        return true;
////                }
////
////
////                return true;
////            }
////        });
//
//
//        topLayer.setOnTouchListener(new View.OnTouchListener() {
//            private int initialX;
//            private int initialY;
//            private float initialTouchX;
//            private float initialTouchY;
//
//            @Override public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        initialX = (int) v.getX();
////                        initialY = (int) v.getY();
//                        initialTouchX = event.getRawX();
//                        initialTouchY = event.getRawY();
//                        return true;
//                    case MotionEvent.ACTION_UP:
//                        return true;
//                    case MotionEvent.ACTION_MOVE:
//                        topLayer.setX(initialX + (int) (event.getRawX() - initialTouchX));
//                        baseView.invalidate();
////                        v.setY(initialY + (int) (event.getRawY() - initialTouchY));
////                        v.invalidate();
//                        return true;
//                }
//                return false;
//            }
//        });


        wm.addView(mBaseLayer,params);
        mLayerContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mLayerContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                ObjectAnimator.ofFloat(mLayerContainer, View.TRANSLATION_Y, -mLayerContainer.getHeight(), 0).start();
            }
        });
    }

}
