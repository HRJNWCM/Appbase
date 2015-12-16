package org.cj.http.protocol;

import java.io.InputStream;

import android.content.Context;

public interface _IParser
{
	void parser(Context context, String name);

	void parser(Context context, InputStream inputStream);

	void parser(Context context, byte[] data);
}
