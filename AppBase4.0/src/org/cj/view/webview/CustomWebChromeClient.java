package org.cj.view.webview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;

import org.cj.BaseAppCompatFragmentActivity;
import org.cj.R;

public class CustomWebChromeClient extends WebChromeClient
{
	CustomWebView	                                mCurrentWebView;
	private Bitmap	                                mDefaultVideoPoster	= null;
	private View	                                mVideoProgressView	= null;
	BaseAppCompatFragmentActivity context;
	private View	                                mCustomView;
	private FrameLayout	                            mFullscreenContainer;
	protected static final FrameLayout.LayoutParams	COVER_SCREEN_PARAMS	= new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
	WebChromeClient.CustomViewCallback	            mCustomViewCallback;

	public CustomWebChromeClient(BaseAppCompatFragmentActivity context,
	        CustomWebView currentWebView)
	{
		// TODO Auto-generated constructor stub
		mCurrentWebView = currentWebView;
		this.context = context;
	}

	@Override
	public Bitmap getDefaultVideoPoster()
	{
		if (mDefaultVideoPoster == null) mDefaultVideoPoster = BitmapFactory
		        .decodeResource(context.getResources(), R.drawable.default_video_poster);
		return mDefaultVideoPoster;
	}

	@Override
	public View getVideoLoadingProgressView()
	{
		if (mVideoProgressView == null)
		{
			LayoutInflater inflater = LayoutInflater.from(context);
			mVideoProgressView = inflater
			        .inflate(R.layout.video_loading_progress, null);
		}
		return mVideoProgressView;
	}

	public void onShowCustomView(View view,
	        WebChromeClient.CustomViewCallback callback)
	{
		showCustomView(view, callback);
	}

	@Override
	public void onHideCustomView()
	{
		hideCustomView();
	}

	@Override
	public void onProgressChanged(WebView view, int newProgress)
	{
		((CustomWebView) view).setProgress(newProgress);
	}

	@Override
	public boolean onCreateWindow(WebView view, final boolean dialog,
	        final boolean userGesture, final Message resultMsg)
	{
		WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
		transport.setWebView(mCurrentWebView);
		resultMsg.sendToTarget();
		return true;
	}

	private void showCustomView(View view,
	        WebChromeClient.CustomViewCallback callback)
	{
		if (mCustomView != null)
		{
			callback.onCustomViewHidden();
			return;
		}
		context.getWindow().getDecorView();
		FrameLayout decor = (FrameLayout) context.getWindow().getDecorView();
		mFullscreenContainer = new FullscreenHolder(context);
		mFullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
		decor.addView(mFullscreenContainer, COVER_SCREEN_PARAMS);
		mCustomView = view;
		setStatusBarVisibility(false);
		mCustomViewCallback = callback;
	}

	private void setStatusBarVisibility(boolean visible)
	{
		int flag = visible ? 0 : WindowManager.LayoutParams.FLAG_FULLSCREEN;
		context.getWindow()
		        .setFlags(flag, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	private void hideCustomView()
	{
		if (mCustomView == null) return;
		setStatusBarVisibility(true);
		FrameLayout decor = (FrameLayout) context.getWindow().getDecorView();
		decor.removeView(mFullscreenContainer);
		mFullscreenContainer = null;
		mCustomView = null;
		mCustomViewCallback.onCustomViewHidden();
	}

	class FullscreenHolder extends FrameLayout
	{
		public FullscreenHolder(Context ctx)
		{
			super(ctx);
			setBackgroundColor(ctx.getResources()
			        .getColor(android.R.color.black));
		}

		@Override
		public boolean onTouchEvent(MotionEvent evt)
		{
			return true;
		}
	}
}
