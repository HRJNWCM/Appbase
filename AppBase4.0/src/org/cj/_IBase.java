package org.cj;

import android.os.Bundle;

import com.nostra13.universalimageloader.core.ImageLoader;

public interface _IBase extends _IService
{
	void initView(Bundle savedInstanceState);//初始化组件

	void showToast(String str);

	void showToast(int id);

	void buildProgressDialog(int id);

	public void cancleProgressDialog();

	public void dismissProgressDialog();

	public void onDialogCancled();

	public void hideInputMethod();

	ImageLoader getImageLoader();

	void relogin(Throwable e);
}
