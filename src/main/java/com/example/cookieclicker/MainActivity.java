package com.example.cookieclicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.icu.number.Scale;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    TextView textView2;
    ImageView imageView;
    ConstraintLayout layout;
    Button b;
    AtomicInteger counter;
    int numBakers=0;
    TextView textInCode;
    public final static String SAVESTATE_COUNT = "count";
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(SAVESTATE_COUNT, counter.get());
        super.onSaveInstanceState(outState);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        counter=new AtomicInteger(0);
        if(savedInstanceState != null) {
            int oldc = savedInstanceState.getInt(SAVESTATE_COUNT);
            counter.getAndSet(oldc);
        }
        layout = findViewById(R.id.layout);
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        imageView = findViewById(R.id.imageView2);
        Context context = this;
        b = findViewById(R.id.button);
        b.setEnabled(false);
        final ScaleAnimation scaleAnimation = new ScaleAnimation(.85f, 1.0f, .85f, 1.0f, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
        scaleAnimation.setDuration(200);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
        Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);
        final ScaleAnimation number = new ScaleAnimation(1.0f, 2.0f, 1.0f, 2.0f, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF,5f);
        number.setDuration(200);
        ObjectAnimator rotate = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f);
        rotate.setRepeatCount(100000);
        rotate.setDuration(750);
        rotate.start();
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter.getAndSet(counter.get()-10);
                numBakers++;
                Runnable r = new Runnable(){
                  public void run(){
                      while(1==1) {
                          try{
                              Thread.sleep(1000);
                          }catch(Exception e)
                          {}
                          counter.getAndAdd(numBakers);
                          textView.post(new Runnable(){
                              @Override
                              public void run() {
                                  outputScore();
                              }
                          });
                      }
                  }
                };
                if(numBakers==1)
                    new Thread(r).start();
                ConstraintLayout parentLayout = (ConstraintLayout)findViewById(layout.getId());
                ConstraintSet set = new ConstraintSet();
                set.clone(layout);
                ImageView iv = new ImageView(context);
                iv.setId(View.generateViewId());
                iv.setImageDrawable(getDrawable(R.drawable.chef));
                parentLayout.addView(iv, 0);
                set.clone(parentLayout);
                set.connect(iv.getId(), ConstraintSet.TOP, parentLayout.getId(), ConstraintSet.TOP);
                set.connect(iv.getId(), ConstraintSet.LEFT, parentLayout.getId(), ConstraintSet.LEFT);
                set.connect(iv.getId(), ConstraintSet.RIGHT, parentLayout.getId(), ConstraintSet.RIGHT);
                set.connect(iv.getId(), ConstraintSet.BOTTOM, parentLayout.getId(), ConstraintSet.BOTTOM);
                set.setVerticalBias(iv.getId(), 0.65f);
                set.setHorizontalBias(iv.getId(), 0.5f);
                set.applyTo(parentLayout);
                iv.startAnimation(animation);
                iv.startAnimation(animation2);
                if(numBakers>1)
                    textView2.setText(numBakers+" Bakers");
                else textView2.setText(numBakers+" Baker");
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.startAnimation(scaleAnimation);
                counter.getAndAdd(1);
                layout.removeView(textInCode);
                floatyText(number, context);
                outputScore();
            }
        });
    }
    public void outputScore()
    {
        if(counter.get()<10)
            b.setEnabled(false);
        else
            b.setEnabled(true);
        if(counter.get()>1)
            textView.setText(counter+" Cookies");
        else textView.setText(counter+" Cookie");
    }
    public void floatyText(ScaleAnimation number, Context context)
    {
        textInCode = new TextView(context);
        textInCode.setId(View.generateViewId());
        textInCode.setText("+1");
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        textInCode.setLayoutParams(params);
        layout.addView(textInCode);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(layout);
        constraintSet.connect(textInCode.getId(), ConstraintSet.TOP, layout.getId(), ConstraintSet.TOP);
        constraintSet.connect(textInCode.getId(), ConstraintSet.LEFT, layout.getId(), ConstraintSet.LEFT);
        constraintSet.connect(textInCode.getId(), ConstraintSet.RIGHT, layout.getId(), ConstraintSet.RIGHT);
        constraintSet.connect(textInCode.getId(), ConstraintSet.BOTTOM, layout.getId(), ConstraintSet.BOTTOM);
        constraintSet.setVerticalBias(textInCode.getId(), 0.3f);
        float r = (float) ((float)Math.random()*0.2+0.45);
        constraintSet.setHorizontalBias(textInCode.getId(), r);
        constraintSet.applyTo(layout);
        textInCode.startAnimation(number);
        number.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                layout.removeView(textInCode);
                Log.d("random", String.valueOf(layout.getChildCount()));
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }
}