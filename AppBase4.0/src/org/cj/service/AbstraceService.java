package org.cj.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.cj.MyApplication;
import org.cj._IService;
import org.cj.config._Config;
import org.cj.http.AsyncHttpClientManager;
import org.cj.http.protocol._IProtocol;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;

public abstract class AbstraceService extends Service implements _IService
{
	MyBinder	binder	= new MyBinder();

	@Override
	public void onCreate()
	{
		// TODO Auto-generated method stub
		super.onCreate();
		MyApplication.get().getLogUtil()
		        .d(this.getClass().getSimpleName() + " create ");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		// TODO Auto-generated method stub
		MyApplication.get().getLogUtil()
		        .d(this.getClass().getSimpleName() + " start ");
		return super.onStartCommand(intent, flags, startId);
	}

	public class MyBinder extends Binder
	{
		public AbstraceService getService()
		{
			return AbstraceService.this;
		}
	}

	@Override
	public IBinder onBind(Intent arg0)
	{
		// TODO Auto-generated method stub
		return binder;
	}

	@Override
	public Header[] getRequestHeaders(String head)
	{
		List<Header> headers = new ArrayList<Header>();
		String headersRaw = head == null ? null : head;
		if (headersRaw != null && headersRaw.length() > 3)
		{
			String[] lines = headersRaw.split("\\r?\\n");
			for ( String line : lines)
			{
				try
				{
					String[] kv = line.split("=");
					if (kv.length != 2) throw new IllegalArgumentException("Wrong header format, may be 'Key=Value' only");
					headers.add(new BasicHeader(kv[0].trim(), kv[1].trim()));
				} catch (Throwable t)
				{
					MyApplication.get().getLogUtil()
					        .e("Not a valid header line: " + line, t);
				}
			}
		}
		return headers.toArray(new Header[headers.size()]);
	}

	@Override
	public HttpEntity getRequestEntity(String entity)
	{
		if (isRequestBodyAllowed() && entity != null)
		{
			MyApplication.get().getLogUtil().d(entity);
			try
			{
				return new StringEntity(entity);
			} catch (UnsupportedEncodingException e)
			{
				MyApplication.get().getLogUtil()
				        .e("cannot create String entity", e);
			}
		}
		return null;
	}

	@Override
	public HttpEntity getRequestEntity(byte[] entity)
	{
		// TODO Auto-generated method stub
		if (isRequestBodyAllowed() && entity != null) { return new ByteArrayEntity(entity); }
		return null;
	}

	@Override
	public void debugHeaders(String TAG, Header[] headers)
	{
		if (headers != null)
		{
			MyApplication.get().getLogUtil().d("Return Headers:");
			StringBuilder builder = new StringBuilder();
			for ( Header h : headers)
			{
				String _h = String.format(Locale.US, "%s : %s", h.getName(), h
				        .getValue());
				MyApplication.get().getLogUtil().d(_h);
				builder.append(_h);
				builder.append("\n");
			}
		}
	}

	@Override
	public String throwableToString(Throwable t)
	{
		if (t == null) return null;
		StringWriter sw = new StringWriter();
		t.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}

	@Override
	public AsyncHttpClient getAsyncHttpClient()
	{
		return AsyncHttpClientManager.get().getAsyncHttpClient();
	}

	@Override
	public AsyncHttpResponseHandler getResponseHandler(_IProtocol protocol)
	{
		// TODO Auto-generated method stub
		return new AsyncResponesHandler(protocol);
	}

	@Override
	public AsyncHttpResponseHandler getResponseHandler(Class<?> c)
	{
		// TODO Auto-generated method stub
		return new AsyncResponesHandler(c);
	}

	@Override
	public String getDefaultURL()
	{
		// TODO Auto-generated method stub
		return _Config.get().getServer_Url();
	}

	@Deprecated
	@Override
	public AsyncHttpResponseHandler getResponseHandler()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isRequestBodyAllowed()
	{
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isRequestHeadersAllowed()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void execute(String URL, Header[] headers, HttpEntity entity,
	        AsyncHttpResponseHandler responseHandler)
	{
		// TODO Auto-generated method stub
		MyApplication.get().getLogUtil().d(URL);
		getAsyncHttpClient()
		        .post(this, URL, headers, entity, null, responseHandler);
	}

	@Override
	public void delete(String url, AsyncHttpResponseHandler responseHandler)
	{
		// TODO Auto-generated method stub
		getAsyncHttpClient().delete(url, responseHandler);
	}

	@Override
	public void get(String url, AsyncHttpResponseHandler responseHandler)
	{
		// TODO Auto-generated method stub
		getAsyncHttpClient().get(url, responseHandler);
	}

	protected class AsyncResponesHandler extends AsyncHttpResponseHandler
	{
		_IProtocol	protocol;
		Class<?>	c;

		public AsyncResponesHandler()
		{
			// TODO Auto-generated constructor stub
		}

		public AsyncResponesHandler(Class<?> protocol)
		{
			// TODO Auto-generated constructor stub
			this.c = protocol;
		}

		public AsyncResponesHandler(_IProtocol protocol)
		{
			// TODO Auto-generated constructor stub
			this.protocol = protocol;
		}

		@Override
		public void onFailure(int statusCode,
		        cz.msebera.android.httpclient.Header[] headers,
		        byte[] responseBody, Throwable error)
		{
			MyApplication.get().getLogUtil()
			        .e(statusCode + "-" + new String(responseBody));
			MyApplication.get().getLogUtil().e(error);
			onException(error);
		}

		@Override
		public void onSuccess(int statusCode,
		        cz.msebera.android.httpclient.Header[] headers,
		        byte[] responseBody)
		{
			try
			{
				onParse(new String(responseBody));
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				MyApplication.get().getLogUtil().e(e);
				onException(e);
			}
		}

		public void onParse(String content) throws Exception
		{
			if (protocol != null) response(protocol, content);
			if (c != null) response(content, c);
		}

		public void onException(Throwable e)
		{
			if (protocol != null) responseByException(protocol, e);
			if (c != null) responseByException(e, c);
		}
	}

	public void response(_IProtocol protocol, String content) throws Exception
	{
	}

	public void response(String content, Class<?> protocol) throws Exception
	{
	}

	public void responseByException(_IProtocol protocol, Throwable e)
	{
	}

	public void responseByException(Throwable e, Class<?> protocol)
	{
	}
}
