package org.cj;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.cj.http.protocol._IProtocol;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;


public interface _IService
{
	Header[] getRequestHeaders(String head);//请求头部

	HttpEntity getRequestEntity(String entity);//请求包体

	HttpEntity getRequestEntity(byte[] entity);//请求包体

	void debugHeaders(String TAG, Header[] headers);

	String throwableToString(Throwable t);

	boolean isRequestBodyAllowed();

	boolean isRequestHeadersAllowed();

	String getDefaultURL();

	AsyncHttpResponseHandler getResponseHandler();

	AsyncHttpResponseHandler getResponseHandler(_IProtocol protocol);

	AsyncHttpResponseHandler getResponseHandler(final Class<?> c);

	AsyncHttpClient getAsyncHttpClient();

	void execute(String URL, Header[] headers, HttpEntity entity,
	        AsyncHttpResponseHandler responseHandler);

	void delete(String url, AsyncHttpResponseHandler responseHandler);

	void get(String url, AsyncHttpResponseHandler responseHandler);
}
