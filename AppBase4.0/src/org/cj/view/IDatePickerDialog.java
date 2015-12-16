package org.cj.view;

import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.widget.DatePicker;

public class IDatePickerDialog
{
	private ICallback	mICallback;
	private Calendar	mCalendar	= Calendar.getInstance();
	DatePickerDialog	dialog;

	public IDatePickerDialog(Context context)
	{
		// TODO Auto-generated constructor stub
		dialog = new DatePickerDialog(context, listener, mCalendar.get(Calendar.YEAR), mCalendar
		        .get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
	}

	public void show()
	{
		dialog.show();
	}

	public interface ICallback
	{
		public void onChange(Date date);
	}

	public void setICallback(ICallback iCallback)
	{
		this.mICallback = iCallback;
	}
	DatePickerDialog.OnDateSetListener	listener	= new OnDateSetListener()
	                                             {
		                                             @Override
		                                             public void onDateSet(
		                                                     DatePicker arg0,
		                                                     int arg1,
		                                                     int arg2, int arg3)
		                                             {
			                                             mCalendar
			                                                     .set(Calendar.YEAR, arg1);
			                                             mCalendar
			                                                     .set(Calendar.MONTH, arg2);
			                                             mCalendar
			                                                     .set(Calendar.DAY_OF_MONTH, arg3);
			                                             if (mICallback != null) mICallback
			                                                     .onChange(mCalendar
			                                                             .getTime());
		                                             }
	                                             };
}
