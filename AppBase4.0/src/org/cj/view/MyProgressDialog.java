package org.cj.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.view.LayoutInflater;
import android.view.View;

import org.cj.R;

public class MyProgressDialog {
    public Dialog mDialog;
    //    MaterialDialog materialDialog;
//    TextView text;
    Context context;
//    private AnimationDrawable animationDrawable = null;

    public MyProgressDialog(Context context) {
        this.context = context;
//        LayoutInflater inflater = LayoutInflater.from(context);
//		View view = inflater.inflate(R.layout.progress_view, null);
//		text = (TextView) view.findViewById(R.id.progress_message);
//		ImageView loadingImage = (ImageView) view
//		        .findViewById(R.id.progress_view);
//		loadingImage.setImageResource(R.anim.loading_animation);
//		animationDrawable = (AnimationDrawable) loadingImage.getDrawable();
//		if (animationDrawable != null)
//		{
//			animationDrawable.setOneShot(false);
//			animationDrawable.start();
//		}
        View view = LayoutInflater.from(context).inflate(R.layout.materaial_progress, null);
//        materialDialog = new MaterialDialog(context);
//        materialDialog.setView(view);
//        materialDialog.setCanceledOnTouchOutside(false);
        mDialog = new Dialog(context, R.style.dialog);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(false);
    }

    public void show(int id) {
//		text.setText(context.getText(id));
        mDialog.show();
    }

    public void show(String str) {
//		text.setText(str);
        mDialog.show();
    }

    public void setCanceledOnTouchOutside(boolean cancel) {
        mDialog.setCanceledOnTouchOutside(cancel);
    }

    public void setOnCancelListener(OnCancelListener l) {
        mDialog.setOnCancelListener(l);
    }

    public void dismiss() {
        if (mDialog.isShowing())
            mDialog.dismiss();
//			animationDrawable.stop();
    }

    public boolean isShowing() {
        if (mDialog.isShowing()) {
            return true;
        }
        return false;
    }
}
