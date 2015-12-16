package org.cj.config;

import org.cj.MyApplication;
import org.cj.R;
import org.cj.http.protocol.resource.XmlResourceParserUtil;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.widget.Toast;

class ConfigParse extends XmlResourceParserUtil
{
	protected void parserContent(Context context, XmlResourceParser parser)
	{
		//获取标签名称  
		String name = parser.getName();
		try
		{
			if (name.equals("version")) _Config.get().getC()
			        .setVersion(parser.nextText());
			if (name.equals("server")) _Config.get().getC()
			        .setServer(parser.nextText());
			if (name.equals("server_debug")) _Config.get().getC()
			        .setServer_debug(parser.nextText());
			if (name.equals("debug")) _Config.get().getC()
			        .setDebug(Boolean.parseBoolean(parser.nextText()));
			if (name.equals("restart")) _Config.get().getC()
			        .setRestart(Boolean.parseBoolean(parser.nextText()));
			if (name.equals("print")) _Config.get().getC()
			        .setPrint(Boolean.parseBoolean(parser.nextText()));
			if (name.equals("app")) _Config.get().getC()
			        .setAppName(parser.nextText());
			if (name.equals("app_dir")) _Config.get().getC()
			        .setDir(parser.nextText());
			if (name.equals("sharepreference")) _Config.get().getC()
			        .setSharepreference(parser.nextText());
			if (name.equals("channel")) _Config.get().getC()
			        .setChannel(parser.nextText());
			if (name.equals("email")) _Config.get().getC()
			        .setEmail(parser.nextText());
			if (name.equals("manager_server")) _Config.get().getC()
			        .setManager_server(parser.nextText());
			if (name.equals("manager_server_debug")) _Config.get().getC()
			        .setManager_debug_server(parser.nextText());
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(context, R.string.config_error, Toast.LENGTH_LONG)
			        .show();
			MyApplication.get().finish();
		}
	}
}
