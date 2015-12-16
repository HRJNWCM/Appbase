package org.cj;

import org.cj.logUtil.LogUtil;

import android.app.Activity;
import android.app.ProgressDialog;

public abstract class _AbstractBase implements _IBase
{
	ProgressDialog	progressDialog;
	LogUtil	       logUtil	= LogUtil.HLog();

	abstract Activity getActivity();
}
