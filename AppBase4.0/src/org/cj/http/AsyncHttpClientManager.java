package org.cj.http;


import com.loopj.android.http.AsyncHttpClient;

public class AsyncHttpClientManager
{
	static AsyncHttpClientManager	manager	= new AsyncHttpClientManager();
	AsyncHttpClient client	= new AsyncHttpClient();

	private AsyncHttpClientManager()
	{
		// TODO Auto-generated constructor stub
	}

	public static AsyncHttpClientManager get()
	{
		return manager;
	}

	public AsyncHttpClient getAsyncHttpClient()
	{
		return client;
	}
}
