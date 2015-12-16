package org.cj.download;

import android.content.Context;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.cj.MyApplication;
import org.cj.comm.FileUtils;
import org.cj.download.info.DownInfo;
import org.cj.http.HttpUtils;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URI;
import java.util.Observable;

public class DownloadThread extends Observable implements Runnable
{
	DownInfo	     info;
	long	         total;
	long	         downbytes;
	int	             status;
	Context	         context;
	static final int	RETRY_COUNT	= 5;
	Thread	         thread;
	boolean	         isInterrupt	= false;
	HttpRequestBase httpRequest	= new HttpGet();

	public DownloadThread(DownInfo info, int status, Context context)
	{
		// TODO Auto-generated constructor stub
		this.status = status;
		this.info = info;
		this.context = context;
	}

	public int getProgress()
	{
		if (total != 0) return (int) ((double) downbytes * 100 / total);
		return 0;
	}

	void onComplete()
	{
		status = Status.COMPLETE;
		stateChanged();
	}

	public void onStart()
	{
		status = Status.UNKNOW;
		stateChanged();
		download();
	}

	void download()
	{
		thread = new Thread(this);
		thread.start();
	}

	public void cancle()
	{
		if (thread != null && !thread.isInterrupted()) thread.interrupt();
	}

	public void stop()
	{
		isInterrupt = true;
		httpRequest.abort();
	}

	void onDownloading()
	{
		status = Status.DOWNLOADING;
		stateChanged();
	}

	void onError()
	{
		status = Status.ERROR;
		MyApplication.get().getLogUtil().w("down load error");
		stateChanged();
	}

	void stateChanged()
	{
		setChanged();
		notifyObservers();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		String url = info.getUrl();
		InputStream inputStream = null;
		MyApplication.get().getLogUtil().d("down url = " + url);
		if (!HttpUtils.isUrl(url))
		{
			onError();
			return;
		}
		for ( int i = 0; i < RETRY_COUNT; i++)
		{
			if (isInterrupt) break;
			try
			{
				httpRequest.setURI(new URI(url));
				// 设置请求包头
				httpRequest.addHeader("Range", "bytes=" + downbytes + "-");
				HttpHost httpHost = null;
				boolean iswap = HttpUtils.isWap(context);
				/** 判断网络类型，如果是wap网络类型实例化网络代理 */
				if (iswap)
				{
					String proxyHost = android.net.Proxy.getDefaultHost();
					int proxyPort = android.net.Proxy.getDefaultPort();
					httpHost = new HttpHost(proxyHost, proxyPort);
				}
				// 实例化浏览器，获得浏览器参数属�?
				HttpClient httpClient = new DefaultHttpClient();
				HttpParams params = httpClient.getParams();
				/** 如果代理不为空，给参数设置代�? */
				if (httpHost != null)
				{
					params.setParameter(ConnRoutePNames.DEFAULT_PROXY, httpHost);
				} else
				{
					params.removeParameter(ConnRoutePNames.DEFAULT_PROXY);
				}
				// 连接超时时间
				HttpConnectionParams.setConnectionTimeout(params, 10000);
				// 请求超时时间
				HttpConnectionParams.setSoTimeout(params, 10000);
				/** 执行请求获得response */
				HttpResponse response = httpClient.execute(httpRequest);
				int respStatus = response.getStatusLine().getStatusCode();
				MyApplication.get().getLogUtil()
				        .d("response code = " + respStatus);
				if (respStatus != HttpStatus.SC_OK
				        && respStatus != HttpStatus.SC_PARTIAL_CONTENT)
				{
					if (i >= RETRY_COUNT - 1)
					{
						onError();
						break;
					}
					continue;
				}
				HttpEntity entity = response.getEntity();
				if (entity != null)
				{
					total = entity.getContentLength();
					inputStream = entity.getContent();
				}
				File d = new File(FileUtils.DOWNLOAD_TEMP_PATH);
				if (!d.exists()) d.mkdirs();
				File file = new File(FileUtils.DOWNLOAD_TEMP_PATH, info.getName());
				if (!file.exists()) file.createNewFile();
				RandomAccessFile raf = new RandomAccessFile(file, "rw");
				raf.seek(downbytes);
				byte[] buff = new byte[1024 * 8];
				int index = -1;
				while ((index = inputStream.read(buff)) != -1)
				{
					raf.write(buff, 0, index);
					downbytes += index;
					onDownloading();
				}
				// 释放资源
				entity.consumeContent();
				inputStream.close();
				raf.close();
				httpRequest.abort();
				if (downbytes >= total)
				{
					File dir = new File(info.getPath());
					if (dir.exists())
					{
						//						File[] files = dir.listFiles();
						//						if (files.length != 0)
						//						{
						//							for ( File f : files)
						//							{
						//								f.delete();
						//							}
						//						}
					} else
					{
						dir.mkdirs();
					}
					if (FileUtils.Move(FileUtils.DOWNLOAD_TEMP_PATH
					        + info.getName(), info.getPath()))
					{
						onComplete();
						break;
					}
				} else
				{
					if (i < RETRY_COUNT) continue;
					else
						onError();
				}
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				MyApplication.get().getLogUtil().w(e);
				if (i < RETRY_COUNT) continue;
				else
					onError();
			}
		}
	}
}
