package org.cj.download.info;

public interface _IDownInfo
{
	String getId();

	String getUrl();// 下载地址

	long getSize();

	String getPath();// 本地存放路径

	String getName();

	void setName(String name);
}
