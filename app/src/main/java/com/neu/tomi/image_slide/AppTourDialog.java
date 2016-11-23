package com.neu.tomi.image_slide;

import android.animation.ArgbEvaluator;
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
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.neu.tomi.view.dialog.PromotionDialog;
import com.neu.tomi.R;
import com.neu.tomi.object.PromtionObject;
import com.neu.tomi.ultity.Global;

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

    private RelativeLayout controlsRelativeLayout, backgroundRelativeLayout;
    private View separatorView;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private TextView  tvTitlePromotion;
    private PagerAdapter pagerAdapter;
    private int activeDotColor;
    private int inactiveDocsColor;
    private int numberOfSlides;
    private boolean isTreat = false;
    public static String PROMOTION_SELECTED="";
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
        tvTitlePromotion = (TextView) findViewById(R.id.tvTitlePromotion);
        separatorView = findViewById(R.id.separatorView);
        dotsLayout = (LinearLayout) findViewById(R.id.viewPagerCountDots);

        activeDotColor = Color.RED;
        inactiveDocsColor = Color.WHITE;

        //Instantiate the PagerAdapter.

        init(savedInstanceState);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), fragments);

        introViewPager.setAdapter(pagerAdapter);

        numberOfSlides = fragments.size();
        introViewPager.setCurrentItem(0);
        //Instantiate the indicator dots if there are more than one slide
        if (numberOfSlides > 1) {
            setViewPagerDots();

        }

        if (isTreat) {
            tvTitlePromotion.setVisibility(View.VISIBLE);

        } else {
//            tvPointGift.setText("+" + points.get(0));
//            ivItemGift.setImageResource(itemObjects.get(0).getImageResource());
//            MaterialSlide materialSlide= (MaterialSlide) fragments.get(0);
//            setInvisibleGrapButton(!DataItems.isSave( materialSlide.getCurrentURLImage()));
        }
        setListeners();
    }
    public void setDot(int size){
        numberOfSlides=size;
        setViewPagerDots();
    }
    public void setTreat(boolean isTreat) {
        this.isTreat = isTreat;
    }

    public abstract void init(@Nullable Bundle savedInstanceState);

    /**
     * Perform action when skip button is pressed
     */

    /**
     * Perform action when done button is pressed
     */
    public abstract void onDonePressed();


    /**
     * Add a slide to the intro
     *
     * @param promtionObject info of ad
     * @param color    Background color of the fragment
     */
    public void addSlide(@NonNull PromtionObject promtionObject, @ColorInt int color) {
        Fragment fragment = MaterialSlide.newInstance(promtionObject,isTreat);
        fragments.add(fragment);
        addBackgroundColor(color);
        pagerAdapter.notifyDataSetChanged();
    }


    public int getSlideLength() {
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
     * Get which slide fragment is currently active
     *
     * @return Fragment
     */
    public Fragment getCurrentFragmentSlide() {

        return fragments.get(introViewPager.getCurrentItem());
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
                    if (colors.size() > 0) {
                        int color = colors.get(colors.size() - 1);
                        setColorSlide(color);
                    }


                }
            }

            @Override
            public void onPageSelected(int position) {
                if (!isTreat) {
                    MaterialSlide materialSlide = (MaterialSlide) fragments.get(position);
                    PROMOTION_SELECTED =materialSlide.getPromotionId();
                    if(!isTreat){
                        if(materialSlide.isLoadedImage()) {
                            if (!PromotionDialog.promotionExist(materialSlide.getPromotionId()))
                                PromotionDialog.addViewPromotion(materialSlide.getPromotionObject());
                        }
                    }
                }
//                setInvisibleGrapButton(!DataItems.isSave(getCurrentImageURL()));
                //Hide NEXT button if last slide item and set DONE button
                //visible, otherwise hide Done button and set NEXT button visible


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

    private void setColorSlide(int color) {
        introViewPager.setBackgroundColor(color);
        controlsRelativeLayout.setBackgroundColor(color);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            GradientDrawable gd = new GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[]{color, color});
            gd.setCornerRadius(Global.convertDpToPixel(16, AppTourDialog.this));
            backgroundRelativeLayout.setBackground(gd);
        }
    }


}
