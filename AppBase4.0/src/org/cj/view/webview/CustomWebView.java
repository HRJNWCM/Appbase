/*
 * Zirco Browser for Android
 * 
 * Copyright (C) 2010 J. Devauchelle and contributors.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 3 as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */
package org.cj.view.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.widget.ProgressBar;

import org.cj.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * A convenient extension of WebView.
 */
public class CustomWebView extends WebView {
    private static boolean mBoMethodsLoaded = false;
    private static Method mOnPauseMethod = null;
    private static Method mOnResumeMethod = null;
    private static Method mSetFindIsUp = null;
    private static Method mNotifyFindDialogDismissed = null;
    ProgressBar progressBar;
    private Context mContext;
    private int mProgress = 100;
    private boolean mIsLoading = false;
    private String mLoadedUrl;

    /**
     * Constructor.
     *
     * @param context The current context.
     */
    public CustomWebView(Context context) {
        super(context);
        mContext = context;
        initializeOptions();
        initProgress();
        loadMethods();
    }

    /**
     * Constructor.
     *
     * @param context The current context.
     * @param attrs   The attribute set.
     */
    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initializeOptions();
        initProgress();
        loadMethods();
    }

    /**
     * Load the AdSweep script if necessary.
     *
     * @param context The current context.
     * @return The AdSweep script.
     */
    public static String getAdSweepString(Context context) {
        InputStream is = context.getResources().openRawResource(R.raw.adsweep);
        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                while ((line = reader.readLine()) != null) {
                    if ((line.length() > 0) && (!line.startsWith("//"))) {
                        sb.append(line).append("\n");
                    }
                }
            } catch (IOException e) {
                Log.w("AdSweep", "Unable to load AdSweep: " + e.getMessage());
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    Log.w("AdSweep", "Unable to load AdSweep: "
                            + e.getMessage());
                }
            }
            return sb.toString();
        }
        return "";
    }

    /**
     * Initialize the WebView with the options set by the user through
     * preferences.
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("SetJavaScriptEnabled")
    public void initializeOptions() {
        WebSettings settings = getSettings();
        // User settings
        settings.setJavaScriptEnabled(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(false);
        settings.setSaveFormData(true);
        settings.setSavePassword(true);
        settings.setDefaultZoom(ZoomDensity.MEDIUM);
        settings.setBuiltInZoomControls(false);
        settings.setSupportZoom(false);
        settings.setDisplayZoomControls(false);
        //		 settings.setUserAgentString(Constants.USER_AGENT_DEFAULT);
        CookieManager.getInstance().setAcceptCookie(true);
        settings.setPluginState(PluginState.valueOf(PluginState.ON_DEMAND
                .toString()));
        settings.setSupportZoom(true);
        settings.setSupportMultipleWindows(true);
        setLongClickable(true);
        setScrollbarFadingEnabled(true);
        setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        setDrawingCacheEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);
    }

    void initProgress() {
        progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 3, 0, 0));
        progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.horizontal_progress));
        addView(progressBar);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        if ((action == MotionEvent.ACTION_DOWN)
                || (action == MotionEvent.ACTION_POINTER_DOWN)
                || (action == MotionEvent.ACTION_POINTER_1_DOWN)
                || (action == MotionEvent.ACTION_POINTER_2_DOWN)
                || (action == MotionEvent.ACTION_POINTER_3_DOWN)) {
            if (ev.getPointerCount() > 1) {
                this.getSettings().setBuiltInZoomControls(true);
                this.getSettings().setSupportZoom(true);
            } else {
                this.getSettings().setBuiltInZoomControls(false);
                this.getSettings().setSupportZoom(false);
            }
        } else if ((action == MotionEvent.ACTION_UP)
                || (action == MotionEvent.ACTION_POINTER_UP)
                || (action == MotionEvent.ACTION_POINTER_1_UP)
                || (action == MotionEvent.ACTION_POINTER_2_UP)
                || (action == MotionEvent.ACTION_POINTER_3_UP)) {
            this.getSettings().setBuiltInZoomControls(false);
            this.getSettings().setSupportZoom(false);
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void loadUrl(String url) {
        mLoadedUrl = url;
        super.loadUrl(url);
    }

    /**
     * Inject the AdSweep javascript.
     */
    public void loadAdSweep() {
        super.loadUrl(getAdSweepString(mContext));
    }

    /**
     * Get the current loading progress of the view.
     *
     * @return The current loading progress of the view.
     */
    public int getProgress() {
        return mProgress;
    }

    /**
     * Set the current loading progress of this view.
     *
     * @param progress The current loading progress.
     */
    public void setProgress(int progress) {
        mProgress = progress;
        progressBar.setProgress(progress);
    }

    /**
     * Triggered when a new page loading is requested.
     */
    public void notifyPageStarted() {
        mIsLoading = true;
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * Triggered when the page has finished loading.
     */
    public void notifyPageFinished() {
        mProgress = 100;
        mIsLoading = false;
        progressBar.setVisibility(View.GONE);
    }

    /**
     * Check if the view is currently loading.
     *
     * @return True if the view is currently loading.
     */
    public boolean isLoading() {
        return mIsLoading;
    }

    /**
     * Get the loaded url, e.g. the one asked by the user, without redirections.
     *
     * @return The loaded url.
     */
    public String getLoadedUrl() {
        return mLoadedUrl;
    }

    /**
     * Reset the loaded url.
     */
    public void resetLoadedUrl() {
        mLoadedUrl = null;
    }

    public boolean isSameUrl(String url) {
        if (url != null) {
            return url.equalsIgnoreCase(this.getUrl());
        }
        return false;
    }

    /**
     * Perform an 'onPause' on this WebView through reflexion.
     */
    public void doOnPause() {
        if (mOnPauseMethod != null) {
            try {
                mOnPauseMethod.invoke(this);
            } catch (IllegalArgumentException e) {
                Log.e("CustomWebView", "doOnPause(): " + e.getMessage());
            } catch (IllegalAccessException e) {
                Log.e("CustomWebView", "doOnPause(): " + e.getMessage());
            } catch (InvocationTargetException e) {
                Log.e("CustomWebView", "doOnPause(): " + e.getMessage());
            }
        }
    }

    /**
     * Perform an 'onResume' on this WebView through reflexion.
     */
    public void doOnResume() {
        if (mOnResumeMethod != null) {
            try {
                mOnResumeMethod.invoke(this);
            } catch (IllegalArgumentException e) {
                Log.e("CustomWebView", "doOnResume(): " + e.getMessage());
            } catch (IllegalAccessException e) {
                Log.e("CustomWebView", "doOnResume(): " + e.getMessage());
            } catch (InvocationTargetException e) {
                Log.e("CustomWebView", "doOnResume(): " + e.getMessage());
            }
        }
    }

    public void doSetFindIsUp(boolean value) {
        if (mSetFindIsUp != null) {
            try {
                mSetFindIsUp.invoke(this, value);
            } catch (IllegalArgumentException e) {
                Log.e("CustomWebView", "doSetFindIsUp(): " + e.getMessage());
            } catch (IllegalAccessException e) {
                Log.e("CustomWebView", "doSetFindIsUp(): " + e.getMessage());
            } catch (InvocationTargetException e) {
                Log.e("CustomWebView", "doSetFindIsUp(): " + e.getMessage());
            }
        }
    }

    public void doNotifyFindDialogDismissed() {
        if (mNotifyFindDialogDismissed != null) {
            try {
                mNotifyFindDialogDismissed.invoke(this);
            } catch (IllegalArgumentException e) {
                Log.e("CustomWebView", "doNotifyFindDialogDismissed(): "
                        + e.getMessage());
            } catch (IllegalAccessException e) {
                Log.e("CustomWebView", "doNotifyFindDialogDismissed(): "
                        + e.getMessage());
            } catch (InvocationTargetException e) {
                Log.e("CustomWebView", "doNotifyFindDialogDismissed(): "
                        + e.getMessage());
            }
        }
    }

    /**
     * Load static reflected methods.
     */
    private void loadMethods() {
        if (!mBoMethodsLoaded) {
            try {
                mOnPauseMethod = WebView.class.getMethod("onPause");
                mOnResumeMethod = WebView.class.getMethod("onResume");
            } catch (SecurityException e) {
                Log.e("CustomWebView", "loadMethods(): " + e.getMessage());
                mOnPauseMethod = null;
                mOnResumeMethod = null;
            } catch (NoSuchMethodException e) {
                Log.e("CustomWebView", "loadMethods(): " + e.getMessage());
                mOnPauseMethod = null;
                mOnResumeMethod = null;
            }
            try {
                mSetFindIsUp = WebView.class
                        .getMethod("setFindIsUp", Boolean.TYPE);
                mNotifyFindDialogDismissed = WebView.class
                        .getMethod("notifyFindDialogDismissed");
            } catch (SecurityException e) {
                Log.e("CustomWebView", "loadMethods(): " + e.getMessage());
                mSetFindIsUp = null;
                mNotifyFindDialogDismissed = null;
            } catch (NoSuchMethodException e) {
                Log.e("CustomWebView", "loadMethods(): " + e.getMessage());
                mSetFindIsUp = null;
                mNotifyFindDialogDismissed = null;
            }
            mBoMethodsLoaded = true;
        }
    }
}
