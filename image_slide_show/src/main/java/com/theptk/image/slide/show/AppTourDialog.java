package com.theptk.image.slide.show;

import android.animation.ArgbEvaluator;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * @author Vlonjat Gashi (vlonjatg)
 */
public abstract class AppTourDialog extends FragmentActivity {

    private final ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    private final ArrayList<Integer> colors = new ArrayList<>();
    private final List<Fragment> fragments = new Vector<>();
    private ViewPager introViewPager;
    private RelativeLayout controlsRelativeLayout,backgroundRelativeLayout;
    LinearLayout llGrapPromotion;
//    private Button skipIntroButton;
    private Button doneSlideButton;
    private ImageButton nextSlideImageButton;
    private View separatorView;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private PagerAdapter pagerAdapter;
    private int currentPosition;
    private int activeDotColor;
    private int inactiveDocsColor;
    private int numberOfSlides;
    private boolean isSkipForceHidden;
    private boolean isNextForceHidden;
    private boolean isDoneForceHidden;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_tour);

        introViewPager = (ViewPager) findViewById(R.id.introViewPager);
        controlsRelativeLayout = (RelativeLayout) findViewById(R.id.controlsRelativeLayout);
        backgroundRelativeLayout = (RelativeLayout) findViewById(R.id.backgroundRelativeLayout);
        llGrapPromotion = (LinearLayout) findViewById(R.id.llGrapPromotion);
        nextSlideImageButton = (ImageButton) findViewById(R.id.nextSlideImageButton);
        doneSlideButton = (Button) findViewById(R.id.doneSlideButton);
        separatorView = findViewById(R.id.separatorView);
        dotsLayout = (LinearLayout) findViewById(R.id.viewPagerCountDots);

        activeDotColor = Color.RED;
        inactiveDocsColor = Color.WHITE;

        //Instantiate the PagerAdapter.
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), fragments);
        introViewPager.setAdapter(pagerAdapter);

        init(savedInstanceState);

        numberOfSlides = fragments.size();

        //Instantiate the indicator dots if there are more than one slide
        if (numberOfSlides > 1) {
            setViewPagerDots();

        } else {
            nextSlideImageButton.setVisibility(View.INVISIBLE);

            if (!isDoneForceHidden) {
                doneSlideButton.setVisibility(View.VISIBLE);
            }
        }

        setListeners();
    }

    public abstract void init(@Nullable Bundle savedInstanceState);

    /**
     * Perform action when skip button is pressed
     */
    public abstract void onGrapPressed();

    /**
     * Perform action when done button is pressed
     */
    public abstract void onDonePressed();

    /**
     * Add a slide to the intro
     *
     * @param fragment Fragment of the slide to be added
     */
    public void addSlide(@NonNull Fragment fragment) {
        fragments.add(fragment);
        addBackgroundColor(Color.TRANSPARENT);
        pagerAdapter.notifyDataSetChanged();
    }

    /**
     * Add a slide to the intro
     *
     * @param fragment Fragment of the slide to be added
     * @param color    Background color of the fragment
     */
    public void addSlide(@NonNull Fragment fragment, @ColorInt int color) {
        fragments.add(fragment);
        addBackgroundColor(color);
        pagerAdapter.notifyDataSetChanged();
    }


    public int getSlideLength(){
        return fragments.size();
    }

    /**
     * Return slides
     *
     * @return Return slides
     */
    public List<Fragment> getSlides() {
        return pagerAdapter.getFragments();
    }

    /**
     * Get which slide is currently active
     *
     * @return Returns the current active slide index
     */
    public int getCurrentSlide() {
        return introViewPager.getCurrentItem();
    }

    /**
     * Set the currently selected slide
     *
     * @param position Item index to select
     */
    public void setCurrentSlide(int position) {
        introViewPager.setCurrentItem(position, true);
    }


    /**
     * Set the string value of the done button
     *
     * @param text String value to set
     */
    public void setDoneText(@NonNull String text) {
        doneSlideButton.setText(text);
    }


    /**
     * Set the next button color to white
     */
    public void setNextButtonColorToWhite() {
        nextSlideImageButton.setImageResource(R.drawable.ic_next_white_24dp);
    }

    /**
     * Set the next button color to black
     */
    public void setNextButtonColorToBlack() {
        nextSlideImageButton.setImageResource(R.drawable.ic_next_black_24dp);
    }

    /**
     * Set the text color of the done button
     *
     * @param color Color value to set
     */
    public void setDoneButtonTextColor(@ColorInt int color) {
        doneSlideButton.setTextColor(color);
    }

    /**
     * Set the color of the separator between slide content and bottom controls
     *
     * @param color Color value to set
     */
    public void setSeparatorColor(@ColorInt int color) {
        separatorView.setBackgroundColor(color);
    }

    /**
     * Set the color of the active dot indicator
     *
     * @param color Color value to set
     */
    public void setActiveDotColor(@ColorInt int color) {
        activeDotColor = color;
    }

    /**
     * Set the color of the inactive dot indicator
     *
     * @param color Color value to set
     */
    public void setInactiveDocsColor(@ColorInt int color) {
        inactiveDocsColor = color;
    }



    /**
     * Show the next button
     */
    public void showNext() {
        nextSlideImageButton.setVisibility(View.VISIBLE);
        isNextForceHidden = false;
    }

    /**
     * Hide the next button
     */
    public void hideNext() {
        nextSlideImageButton.setVisibility(View.INVISIBLE);
        isNextForceHidden = true;
    }

    /**
     * Show the done button
     */
    public void showDone() {
        doneSlideButton.setVisibility(View.VISIBLE);
        isDoneForceHidden = false;
    }

    /**
     * Hide the done button
     */
    public void hideDone() {
        doneSlideButton.setVisibility(View.INVISIBLE);
        isDoneForceHidden = true;
    }

    /**
     * Show indicator dots
     */
    public void showIndicatorDots() {
        dotsLayout.setVisibility(View.VISIBLE);
    }

    /**
     * Hide indicator dots
     */
    public void hideIndicatorDots() {
        dotsLayout.setVisibility(View.INVISIBLE);
    }

    protected void addBackgroundColor(@ColorInt int color) {
        colors.add(color);
    }

    private void setListeners() {
        introViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position < (pagerAdapter.getCount() - 1) && position < (colors.size() - 1)) {
                    int color = (Integer)
                            argbEvaluator.evaluate(positionOffset, colors.get(position), colors.get(position + 1));
                    setColorSlide(color);
                } else {
                    int color = colors.get(colors.size() - 1);
                    setColorSlide(color);


                }
            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;


                //Hide NEXT button if last slide item and set DONE button
                //visible, otherwise hide Done button and set NEXT button visible
                if (position == numberOfSlides - 1) {
                    if (!isNextForceHidden) {
                        fadeViewOut(nextSlideImageButton);
                    }

                    if (!isDoneForceHidden) {
                        fadeViewIn(doneSlideButton);
                    }
                } else {
                    if (nextSlideImageButton.getVisibility() == View.INVISIBLE && !isNextForceHidden) {
                        fadeViewIn(nextSlideImageButton);
                    }

                    if (doneSlideButton.getVisibility() == View.VISIBLE && !isDoneForceHidden) {
                        fadeViewOut(doneSlideButton);
                    }
                }

                //Set dots
                if (numberOfSlides > 1) {
                    //Set current inactive dots color
                    for (int i = 0; i < numberOfSlides; i++) {
                        dots[i].setTextColor(inactiveDocsColor);
                    }

                    //Set current active dot color
                    dots[position].setTextColor(activeDotColor);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        llGrapPromotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull View v) {
                onGrapPressed();
            }
        });

        nextSlideImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                introViewPager.setCurrentItem(currentPosition + 1, true);
            }
        });

        doneSlideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull View v) {
                onDonePressed();
            }
        });
    }

    private void setViewPagerDots() {
        dots = new TextView[numberOfSlides];

        //Set first inactive dots color
        for (int i = 0; i < numberOfSlides; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(30);
            dots[i].setTextColor(inactiveDocsColor);
            dotsLayout.addView(dots[i]);
        }

        //Set first active dot color
        dots[0].setTextColor(activeDotColor);
    }

    private void fadeViewOut(final View view) {
        Animation fadeOut = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        view.startAnimation(fadeOut);
    }

    private void fadeViewIn(final View view) {
        Animation fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        view.startAnimation(fadeIn);
    }
    private  void setColorSlide(int color){
        introViewPager.setBackgroundColor(color);
        controlsRelativeLayout.setBackgroundColor(color);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            GradientDrawable gd = new GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[] {color,color});
            gd.setCornerRadius(convertDpToPixel(16));
            backgroundRelativeLayout.setBackground(gd);
        }
    }
    private float convertDpToPixel(float dp){
        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }
}
