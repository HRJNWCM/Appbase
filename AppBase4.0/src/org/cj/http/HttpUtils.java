package org.cj.http;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.cj.http.protocol.XmlObj;
import org.cj.logUtil.LogUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络连接管理
 * 
 * @author Eway
 * 
 */
public class HttpUtils
{
	static LogUtil	        logUtil	        = LogUtil.HLog();
	public Context	        mContext	    = null;	      // context
	private String	        mUrl	        = null;	      // url
	private long	        mTotalSize	    = 0;	          // 返回数据大小
	private byte[]	        mRespData	    = null;	      // 返回数据
	private byte[]	        mReqData	    = null;	      // 请求数据
	List<XmlObj>	        images;
	private Header[]	    mReqHeaders	    = null;	      // 请求header
	private Header[]	    mRespHeaders	= null;	      // 返回header
	private HttpClient	    mHttpClient	    = null;	      // 实例对象
	private HttpResponse	mResponse	    = null;
	private HttpRequestBase	mHttpRequest	= null;
	public InputStream	    mInputStream	= null;	      // 输入
	private int	            mRespStatus	    = -1;	          // 响应状
	private int	            mConnectTimeout	= 10000;	      // 连接超时时间
	private int	            MAX_BUFFER_SIZE	= 1024 * 8;	  // 缓存

	public void setConnectTimeout(int connectTimeout)
	{
		mConnectTimeout = connectTimeout;
	}

	public Header[] getRespHeaders()
	{
		return mRespHeaders;
	}

	public int getRespStatus()
	{
		return mRespStatus;
	}

	public byte[] getRespData()
	{
		return mRespData;
	}

	public long getTotalSize()
	{
		return mTotalSize;
	}

	public Context getContext()
	{
		return mContext;
	}

	/**
	 * 下载调用构函数
	 * 
	 * @param url
	 * @param reqHeader
	 * @param context
	 * @throws IOException
	 */
	public HttpUtils(String url, Header[] reqHeader, Context context)
	{
		mContext = context;
		mUrl = url.trim();
		logUtil.i("down load  url=" + mUrl);
		mReqHeaders = reqHeader;
	}

	/**
	 * HttpPost请求xml调用构函数
	 * 
	 * @param url
	 * @param reqHeader
	 * @param data
	 * @param context
	 * @throws IOException
	 */
	public HttpUtils(String url, Header[] reqHeader, byte[] data,
	        List<XmlObj> images, Context context)
	{
		mContext = context;
		mUrl = url.trim();
		mReqData = data;
		mReqHeaders = reqHeader;
		this.images = images;
	}

	/**
	 * 得到输入
	 * 
	 * @return
	 * @throws IllegalStateException
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	@SuppressWarnings("deprecation")
	public InputStream getInputStream() throws IllegalStateException,
	        IOException, URISyntaxException
	{
		logUtil.i("getInputStream  url=" + mUrl);
		if (!isUrl(mUrl)) throw new IOException("<" + mUrl
		        + "> is not available");
		// 获得请求实例
		if (mReqData != null)
		{
			mHttpRequest = new HttpPost(mUrl);
			if (mReqHeaders != null)
			{
				for ( int i = 0; i < mReqHeaders.length; i++)
					mHttpRequest.addHeader(mReqHeaders[i]);
			}
			if (images != null) for ( XmlObj obj : images)
			{
				mHttpRequest.addHeader(obj.getName(), String.valueOf(obj
				        .getLength()));
			}
			HttpEntity httpEntity = new ByteArrayEntity(mReqData);
			((HttpPost) mHttpRequest).setEntity(httpEntity);
		} else
		{
			logUtil.i("http get方法");
			mHttpRequest = new HttpGet();
			mHttpRequest.setURI(new URI(mUrl));
		}
		HttpHost httpHost = null;
		boolean iswap = isWap(mContext);
		/** 判断网络类型，如果是wap网络类型实例化网络代理 */
		if (iswap)
		{
			String proxyHost = android.net.Proxy.getDefaultHost();
			int proxyPort = android.net.Proxy.getDefaultPort();
			httpHost = new HttpHost(proxyHost, proxyPort);
		}
		// 实例化浏览器，获得浏览器参数属
		mHttpClient = new DefaultHttpClient();
		HttpParams params = mHttpClient.getParams();
		/** 如果代理不为空，给参数设置代 */
		if (httpHost != null)
		{
			params.setParameter(ConnRoutePNames.DEFAULT_PROXY, httpHost);
		} else
		{
			params.removeParameter(ConnRoutePNames.DEFAULT_PROXY);
		}
		// 连接超时时间
		HttpConnectionParams.setConnectionTimeout(params, mConnectTimeout);
		// 请求超时时间
		HttpConnectionParams.setSoTimeout(params, mConnectTimeout);
		/** 执行请求获得response */
		mResponse = mHttpClient.execute(mHttpRequest);
		mRespStatus = mResponse.getStatusLine().getStatusCode();
		logUtil.i("RespStatus = " + mRespStatus);
		if (mRespStatus != HttpStatus.SC_OK
		        && mRespStatus != HttpStatus.SC_PARTIAL_CONTENT)
		{
			throw new IOException();
		}
		mRespHeaders = mResponse.getAllHeaders();
		HttpEntity entity = mResponse.getEntity();
		if (entity != null)
		{
			mTotalSize = entity.getContentLength();
			logUtil.i("response contentLength=" + mTotalSize);
			/** 获取输入 */
			mInputStream = entity.getContent();
		}
		return mInputStream;
	}

	/**
	 * 文件下载
	 * 
	 * @param path
	 *            文件路径
	 * @param fileName
	 *            文件名称
	 * @param isContinue
	 * @param fileSize
	 *            下载文件大小
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws IllegalStateException
	 */
	public void get(String path, String fileName, boolean isContinue,
	        float fileSize) throws IOException, IllegalStateException,
	        URISyntaxException
	{
		logUtil.i(path + fileName);
		File file = new File(path, fileName);
		if (file.exists()) file.delete();
		file.createNewFile();
		InputStream inputStream = null;
		inputStream = getInputStream();
		FileOutputStream fos = new FileOutputStream(file, isContinue);
		if (fileSize == 0)
		{
			fileSize = mTotalSize;
		}
		byte[] buffer = new byte[MAX_BUFFER_SIZE];
		int len = 0;
		while ((len = inputStream.read(buffer)) != -1)
		{
			// 将输入流数据读入内存缓冲
			fos.write(buffer, 0, len);
		}
		fos.flush();
		fos.close();
		inputStream.close();
	}

	/**
	 * 执行post
	 * 
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws IllegalStateException
	 */
	public byte[] post() throws ClientProtocolException, IOException,
	        IllegalStateException, URISyntaxException
	{
		logUtil.i("http post");
		byte[] buff = new byte[MAX_BUFFER_SIZE];
		// 获得输入流
		InputStream inStream = getInputStream();
		if (inStream == null) throw new IOException("no response");
		int contentLength = (int) mTotalSize;
		byte[] result = null;
		// 服务器未设置httpbody长度
		if (contentLength == -1)
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while (true)
			{
				int len = inStream.read(buff, 0, buff.length);
				if (len == -1) break;
				baos.write(buff, 0, len);
			}
			result = baos.toByteArray();
			if (baos != null) baos.close();
			baos = null;
		} else
		{
			result = new byte[contentLength];
			int currLen = 0;
			while (currLen < contentLength)
			{
				int len = inStream.read(result, currLen, contentLength
				        - currLen);
				if (len == -1) break;
				currLen += len;
			}
			if (currLen < contentLength)
			{
				mRespData = null;
			}
		}
		mRespData = result;
		if (result.length >= contentLength) return mRespData;
		else
			throw new IOException();
	}

	/** 判断网络类型 */
	@SuppressWarnings("deprecation")
	public static boolean isWap(Context context)
	{
		// 上下文为空，返回false
		if (context == null) return false;
		try
		{
			// 获取网络连接系统服务
			ConnectivityManager connMgr = (ConnectivityManager) context
			        .getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connMgr == null)
			{
				return false;
			}
			// 获取网络连接状信息
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			if (networkInfo == null)
			{
				return false;
			}
			// 获取网络连接类型
			int type = networkInfo.getType();
			if (type == ConnectivityManager.TYPE_MOBILE)
			{
				String proxyHost = android.net.Proxy.getDefaultHost();
				// 判断网络类型是否为WAP
				logUtil.i("proxyHost = " + proxyHost);
				if (proxyHost != null && !proxyHost.equals(""))
				{
					// WAP方式
					logUtil.i("networkType  is wap");
					return true;
				} else
				{
					logUtil.i("networkType  is net");
				}
				// 网络类型为wifi
			} else if (type == ConnectivityManager.TYPE_WIFI)
			{
				logUtil.i("networkType  is wifi");
			}
		} catch (Exception e)
		{
			// TODO: handle exception
			return false;
		}
		return false;
	}

	/**
	 * 检查网络是否连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isConnect(Context context)
	{
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		if (context == null) return false;
		try
		{
			ConnectivityManager connectivity = (ConnectivityManager) context
			        .getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null)
			{
				// 获取网络连接管理的对象
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected())
				{
					// 判断当前网络是否已经连接
					if (info.getState() == NetworkInfo.State.CONNECTED)
					{
						return true;
					}
				}
			}
		} catch (Exception e)
		{
			logUtil.w(e);
			return false;
		}
		return false;
		// TODO: handle exception
	}

	/**
	 * 打开设置网络界面
	 */
	public static void setNetworkMethod(final Activity context)
	{
		// 提示对话
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle("网络设置提示")
		        .setMessage("网络连接不可用，是否进行设置?")
		        .setPositiveButton("设置", new DialogInterface.OnClickListener()
		        {
			        @SuppressLint("NewApi")
			        @Override
			        public void onClick(DialogInterface dialog, int which)
			        {
				        // TODO Auto-generated method stub
				        Intent intent = null;
				        // 判断手机系统的版即API大于10 就是3.0或以上版
				        if (android.os.Build.VERSION.SDK_INT > 10)
				        {
					        intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
				        } else
				        {
					        intent = new Intent();
					        ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
					        intent.setComponent(component);
					        intent.setAction("android.intent.action.VIEW");
				        }
				        context.startActivity(intent);
			        }
		        })
		        .setNegativeButton("取消", new DialogInterface.OnClickListener()
		        {
			        @Override
			        public void onClick(DialogInterface dialog, int which)
			        {
				        // TODO Auto-generated method stub
				        dialog.dismiss();
				        context.finish();
			        }
		        }).show();
		;
	}

	/**
	 * url合法性检查
	 * 
	 * @param pInput
	 * @return
	 */
	public static boolean isUrl(String pInput)
	{
		if (pInput == null)
		{
			return false;
		}
		String regEx = "^(http|https|ftp)\\://([a-zA-Z0-9\\.\\-]+(\\:[a-zA-"
		        + "Z0-9\\.&%\\$\\-]+)*@)?((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{"
		        + "2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}"
		        + "[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|"
		        + "[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-"
		        + "4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|([a-zA-Z0"
		        + "-9\\-]+\\.)*[a-zA-Z0-9\\-]+\\.[a-zA-Z]{2,4})(\\:[0-9]+)?(/"
		        + "[^/][a-zA-Z0-9\\.\\,\\?\\'\\\\/\\+&%\\$\\=~_\\-@]*)*$";
		Pattern p = Pattern.compile(regEx);
		Matcher matcher = p.matcher(pInput);
		return matcher.matches();
	}

	/**
	 * byte array convert to int value.
	 * 
	 * @param b
	 * @return
	 */
	public static int byteArray2Int(byte[] b)
	{
		int intValue = 0;
		for ( int i = 0; i < b.length; i++)
		{
			intValue += (b[i] & 0xFF) << (8 * (3 - i));
		}
		return intValue;
	}
}
