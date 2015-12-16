package org.cj.upgrade;

import java.io.FileOutputStream;
import java.io.InputStream;

import org.cj.R;
import org.cj.comm.FileUtils;
import org.cj.http.HttpUtils;
import org.cj.logUtil.LogUtil;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

/**
 * App升级
 * 
 * @author Eway
 * 
 */
public class AppUpgrade
{
	public AppUpgradeListener	mListener;
	public Context	          mContext;
	private ProgressBar	      mPb	   = null;
	private AlertDialog	      mDialog;
	private float	          mDownbytes;
	private DownLoadInfo	  mInfo;
	static final int	      PROGRESS	= 1;
	static final int	      UPDATING	= 2;
	static final int	      COMPLETE	= 3;
	static final int	      ERROR	   = -1;
	LogUtil	                  logUtil	= LogUtil.HLog();

	public AppUpgrade(Context context)
	{
		mContext = context;
		init();
	}

	/**
	 * 
	 * @param listener
	 */
	public void setAppUpgradeListener(AppUpgradeListener listener)
	{
		mListener = listener;
	}

	/**
	 * 设置升级
	 * 
	 * @param totalsize
	 * @param upgradeModel
	 */
	public void setUpgradeAttr(DownLoadInfo info)
	{
		// mPb.setMax((int) info.size);
		this.mInfo = info;
	}

	public void downLoad()
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				logUtil.d("download start");
				Looper.prepare();
				if (HttpUtils.isConnect(mContext))
				{
					//下载
					//					AsyncHttpClientManager.get().getAsyncHttpClient()
					//					        .get(mInfo.url, new AsyncHttpResponseHandler()
					//					        {
					//						        @Override
					//						        public void onSuccess(int arg0, Header[] arg1,
					//						                byte[] arg2)
					//						        {
					//							        // TODO Auto-generated method stub
					//							        super.onSuccess(arg0, arg1, arg2);
					//						        }
					//
					//						        @Override
					//						        public void onProgress(int bytesWritten,
					//						                int totalSize)
					//						        {
					//							        // TODO Auto-generated method stub
					//							        super.onProgress(bytesWritten, totalSize);
					//						        }
					//
					//						        @Override
					//						        public void onFailure(Throwable throwable)
					//						        {
					//							        // TODO Auto-generated method stub
					//							        super.onFailure(throwable);
					//						        }
					//					        });
					HttpUtils httpUtils = new HttpUtils(mInfo.url, null, mContext);
					InputStream inputStream;
					try
					{
						inputStream = httpUtils.getInputStream();
						mInfo.size = httpUtils.getTotalSize();
						handler.sendEmptyMessage(PROGRESS);
						FileOutputStream fos = new FileOutputStream(FileUtils.APK, false);
						byte[] buff = new byte[1024 * 2];
						int index = 0;
						while ((index = inputStream.read(buff)) != -1)
						{
							mDownbytes += index;
							fos.write(buff, 0, index);
							handler.sendEmptyMessage(UPDATING);
						}
						fos.flush();
						fos.close();
						inputStream.close();
						if (mDownbytes >= mInfo.size) handler
						        .sendEmptyMessage(COMPLETE);
						else
							handler.sendMessage(handler
							        .obtainMessage(ERROR, "io exception"));
					} catch (Exception e)
					{
						// TODO Auto-generated catch block
						logUtil.w(e);
						mDialog.dismiss();
						handler.sendMessage(handler.obtainMessage(ERROR, e));
					}
				} else
				{
					handler.sendMessage(handler.obtainMessage(ERROR, mContext
					        .getString(R.string.no_internet)));
				}
			}
		}).start();
	}

	private void init()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		View v = LayoutInflater.from(mContext)
		        .inflate(R.layout.dialog_upgrade, null);
		mPb = (ProgressBar) v.findViewById(R.id.pb);
		builder.setView(v);
		mDialog = builder.create();
		mDialog.getWindow()
		        .setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);// 设定为系统级警告
		mDialog.setCancelable(false);
	}

	public void showDialog()
	{
		mDialog.show();
		downLoad();
	}
	private Handler	handler	= new Handler(new Handler.Callback()
	                        {
		                        @Override
		                        public boolean handleMessage(Message msg)
		                        {
			                        // TODO Auto-generated method stub
			                        switch (msg.what)
			                        {
										case PROGRESS:
											mPb.setMax((int) mInfo.size);
											break;
										case UPDATING:
											mPb.setProgress((int) mDownbytes);
											break;
										case COMPLETE:
											logUtil.i("升级成功，进入安装");
											if (mListener != null)
											{
												if (mDialog != null) mDialog.dismiss();
												mListener.onComplete(mInfo
												        .getMode());
											}
											break;
										case ERROR:
											logUtil.i("升级失败!");
											if (mListener != null)
											{
												if (mDialog != null) mDialog
												        .dismiss();
												mListener.onError(msg.obj);
											}
											break;
									}
									return false;
								}
	                        });

	public interface UpgradeMode
	{
		public static final int	NEED_UPGRADE	= 2; // 强制升级
		public static final int	CHOOSE_UPGRADE	= 1; // 可选升级
		public static final int	NO_UPGRADE		= 0; // 无更新
	}

	public interface AppUpgradeListener
	{
		public void onCancle(int model);

		public void onError(Object error);

		public void onComplete(int model);
	}
}
