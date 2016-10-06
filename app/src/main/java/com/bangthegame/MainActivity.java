package com.bangthegame;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private View mdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        mdView = getWindow().getDecorView();

        mdView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        cardInit();
    }
        @Override
        public void onWindowFocusChanged(boolean hasFocus) {
            super.onWindowFocusChanged(hasFocus);
            if (hasFocus) {
                mdView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }

    public static class CardFrontFragment extends Fragment {
        private static float x;
        private static float y;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.card_front, container, false);
            //ImageButton cardF = (ImageButton) v.findViewById(R.id.cardFrontI);
            //cardF.setImageResource(R.drawable.cardback);
            v.setX(CardFrontFragment.x);
            v.setY(CardFrontFragment.y);
            return v;
        }
    }

    public static class CardBackFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.card_back, container, false);
        }
    }

    public void cardMoveFlip(View v) {
        float deltaY = findViewById(R.id.cardBack).getY()+findViewById(R.id.launchAnim).getY();
        float deltaX = -(findViewById(R.id.cardBack).getX()-findViewById(R.id.launchAnim).getX());
        View image = findViewById(R.id.cardBack);

        AnimatorSet animSetXY = new AnimatorSet();
        animSetXY.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                findViewById(R.id.launchAnim).setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                CardFrontFragment.x = findViewById(R.id.cardBack).getX();
                CardFrontFragment.y = findViewById(R.id.cardBack).getY();

                getFragmentManager()
                        .beginTransaction()

                        // Replace the default fragment animations with animator resources representing
                        // rotations when switching to the back of the card, as well as animator
                        // resources representing rotations when flipping back to the front (e.g. when
                        // the system Back button is pressed).
                        .setCustomAnimations(R.animator.card_flip_right_in, R.animator.card_flip_right_out)

                        // Replace any fragments currently in the container view with a fragment
                        // representing the next page (indicated by the just-incremented currentPage
                        // variable).
                        .replace(R.id.container, new CardFrontFragment())

                        // Commit the transaction.
                        .commit();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }
        });
        ObjectAnimator y = ObjectAnimator.ofFloat(image, "translationY", deltaY);
        ObjectAnimator x = ObjectAnimator.ofFloat(image, "translationX", deltaX);

        animSetXY.playTogether(x, y);
        animSetXY.setInterpolator(new AccelerateDecelerateInterpolator());
        animSetXY.setDuration(400);
        animSetXY.start();
     }

    public void cardInit(){
        getFragmentManager()
                .beginTransaction()
                .add(R.id.container, new CardBackFragment())
                .commit();

    }

}
