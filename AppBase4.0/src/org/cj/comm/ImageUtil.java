package org.cj.comm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class ImageUtil
{
	public static final int	   CHOOSE_BIG_PICTURE	= 1;
	public static final int	   CHOOSE_SMALL_PICTURE	= 2;
	public static final int	   CAMERA_SUCCESS	    = 2;
	public static final int	   PHOTO_SUCCESS	    = 1;
	public static final int	   MX_PHOTO_SUCCESS	    = 3;
	public static final String	TAG	                = "ImageUtil";

	public static void getImgFromLocal(Activity context)
	{
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		context.startActivityForResult(intent, CAMERA_SUCCESS);
	}

	/**
	 * 从相册截图
	 * 
	 * @param context
	 * @param imageUri
	 */
	public static String cropBigImage(Context context, int outx, int outy,
	        int requestcode)
	{
		if (android.os.Build.BRAND.startsWith("Meizu"))
		{
			getImgFromLocal((Activity) context);
			return "";
		}
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", outx);
		intent.putExtra("outputY", outy);
		intent.putExtra("scale", true);
		intent.putExtra("scaleUpIfNeeded", true);
		intent.putExtra("return-data", false);
		//		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		intent.putExtra("outputFormat", "JPEG");
		intent.putExtra("noFaceDetection", true); // no face detection
		File tempFile = new File(FileUtils.IMAGE_CACHE
		        + Calendar.getInstance().getTimeInMillis() + ".jpg"); // 以时间秒为文件名  
		intent.putExtra("output", Uri.fromFile(tempFile)); // 专入目标文件   
		intent.putExtra("path", tempFile.getAbsolutePath());
		((Activity) context).startActivityForResult(intent, requestcode);
		return tempFile.getAbsolutePath();
	}

	/**
	 * 相册截图
	 * 
	 * @param context
	 * @param imageUri
	 */
	public static String cropBigImage(Context context, String path, int outx,
	        int outy, int requestcode)
	{
		return cropBigImage(context, Uri.fromFile(new File(path)), outx, outy, requestcode);
	}

	/**
	 * 
	 * @param context
	 * @param imageUri
	 *            指定uri截图
	 * @return 返回图片地址
	 */
	public static String cropBigImage(Context context, Uri uri, int outx,
	        int outy, int requestcode)
	{
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", outx);
		intent.putExtra("outputY", outy);
		intent.putExtra("scale", true);
		intent.putExtra("scaleUpIfNeeded", true);
		intent.putExtra("return-data", false);
		//		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		intent.putExtra("outputFormat", "JPEG");
		intent.putExtra("noFaceDetection", true); // no face detection
		File tempFile = new File(FileUtils.IMAGE_CACHE
		        + Calendar.getInstance().getTimeInMillis() + ".jpg"); // 以时间秒为文件名  
		intent.putExtra("output", Uri.fromFile(tempFile)); // 专入目标文件   
		intent.putExtra("path", tempFile.getAbsolutePath());
		((Activity) context).startActivityForResult(intent, requestcode);
		return tempFile.getAbsolutePath();
	}

	/**
	 * 截图程序
	 * 
	 * @param context
	 * @param uri
	 * @param outputX
	 * @param outputY
	 * @param requestCode
	 */
	public static void cropImageUri(Context context, Uri uri, int outputX,
	        int outputY, int requestCode)
	{
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("scale", true);
		intent.putExtra("scaleUpIfNeeded", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("return-data", true);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		((Activity) context).startActivityForResult(intent, requestCode);
	}

	/**
	 * 拍照获取图片
	 * 
	 * @param context
	 * @return
	 */
	public static String getPhotoByCamera(Activity context)
	{
		Intent getImageByCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File dir = new File(Environment.getExternalStorageDirectory()
		        .getAbsolutePath() + "/DCIM/Camera/");
		if (!dir.exists()) dir.mkdirs();
		File file = new File(Environment.getExternalStorageDirectory()
		        .getAbsolutePath()
		        + "/DCIM/Camera/"
		        + Calendar.getInstance().getTimeInMillis() + ".jpg");
		Uri uri = Uri.fromFile(file);
		Log.d("test ", " from camera : " + uri.toString());
		getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		context.startActivityForResult(getImageByCamera, CAMERA_SUCCESS);
		return file.getAbsolutePath();
	}

	/**
	 * 拍照获取图片->存放到系统camera文件夹
	 * 
	 * @param context
	 * @return
	 */
	public static Uri getPhotoByCamera2(Activity context)
	{
		Intent getImageByCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File dir = new File(Environment.getExternalStorageDirectory()
		        .getAbsolutePath() + "/DCIM/Camera/");
		if (!dir.exists()) dir.mkdirs();
		File file = new File(Environment.getExternalStorageDirectory()
		        .getAbsolutePath()
		        + "/DCIM/Camera/"
		        + Calendar.getInstance().getTimeInMillis() + ".jpg");
		Log.d(TAG, file.getAbsolutePath());
		Uri uri = Uri.fromFile(file);
		getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		context.startActivityForResult(getImageByCamera, CAMERA_SUCCESS);
		return uri;
	}

	/**
	 * 拍照获取图片
	 * 
	 * @param context
	 * @param 指定文件夹
	 * @return
	 */
	public static Uri getPhotoByCamera(Activity context, String path)
	{
		Intent getImageByCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File file = new File(path + Calendar.getInstance().getTimeInMillis()
		        + ".jpg");
		Uri uri = Uri.fromFile(file);
		getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		context.startActivityForResult(getImageByCamera, CAMERA_SUCCESS);
		return uri;
	}

	/**
	 * 获取本地图片
	 * 
	 * @param context
	 */
	public static void getPhotoByLocal(Activity context)
	{
		Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
		getImage.addCategory(Intent.CATEGORY_OPENABLE);
		getImage.setType("image/*");
		context.startActivityForResult(getImage, PHOTO_SUCCESS);
	}

	public static String cropImageUri(Context context, Bitmap data,
	        int outputX, int outputY, int requestCode)
	{
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setType("image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("data", data);
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("scale", true);
		intent.putExtra("scaleUpIfNeeded", true);
		intent.putExtra("return-data", false);
		File tempFile = new File(FileUtils.IMAGE_CACHE
		        + Calendar.getInstance().getTimeInMillis() + ".jpg"); // 以时间秒为文件名  
		intent.putExtra("output", Uri.fromFile(tempFile)); // 专入目标文件   
		intent.putExtra("path", tempFile.getAbsolutePath());
		intent.putExtra("outputFormat", "JPEG");
		((Activity) context).startActivityForResult(intent, requestCode);
		return tempFile.getAbsolutePath();
	}

	//
	static String getImagePathFromLocal(Context context, Uri uri)
	{
		String path = "";
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2
		        && DocumentsContract.isDocumentUri(context, uri))
		{
			if (isExternalStorageDocument(uri))
			{
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];
				if ("primary".equalsIgnoreCase(type)) return Environment
				        .getExternalStorageDirectory() + "/" + split[1];
			}
			// DownloadsProvider  
			else if (isDownloadsDocument(uri))
			{
				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(Uri
				        .parse("content://downloads/public_downloads"), Long
				        .valueOf(id));
				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider  
			else if (isMediaDocument(uri))
			{
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];
				Uri contentUri = null;
				if ("image".equals(type))
				{
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				}
				else if ("video".equals(type))
				{
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				}
				else if ("audio".equals(type))
				{
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}
				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };
				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
			else
			{
			}
		}
		else
		{
			if (uri != null)
			{
				String uriStr = uri.toString();
				String p = uriStr.substring(10, uriStr.length());
				if (p.startsWith("com.sec.android.gallery3d")) return "";
			}
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = context.getContentResolver()
			        .query(uri, filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			path = cursor.getString(columnIndex);
			cursor.close();
		}
		return path;
	}

	static String getDataColumn(Context context, Uri uri, String selection,
	        String[] selectionArgs)
	{
		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };
		try
		{
			cursor = context.getContentResolver()
			        .query(uri, projection, selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst())
			{
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally
		{
			if (cursor != null) cursor.close();
		}
		return null;
	}

	//sdk<19获取图片路径
	public static String getImagePath(Activity context, Uri uri)
	{
		String path = "";
		String[] proj = { MediaStore.Images.Media.DATA };
		@SuppressWarnings("deprecation")
		Cursor actualimagecursor = context
		        .managedQuery(uri, proj, null, null, null);
		int actual_image_column_index = actualimagecursor
		        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		actualimagecursor.moveToFirst();
		path = actualimagecursor.getString(actual_image_column_index);
		return path;
	}

	/**
	 * 通过图库返回的uri获取本地图片路径
	 * 
	 * @param context
	 * @param uri
	 * @param maxWidth
	 * @param maxHeight
	 * @return
	 */
	public static String getImagePathFromLocal(Activity context, Uri uri,
	        int maxWidth, int maxHeight)
	{
		String uploadImagePath = "";
		try
		{
			uploadImagePath = getImagePathFromLocal(context, uri);
			Log.d("test", "uploadImagePath : " + uploadImagePath);
			uploadImagePath = savePhoto(createBitmap(uploadImagePath, maxWidth), true, uploadImagePath, maxWidth, maxHeight);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			Log.w("test", e);
		}
		return uploadImagePath;
	}

	/**
	 * 对图片裁剪后保存
	 * 
	 * @param bitmap
	 * @param recycle
	 * @param path
	 * @param maxw
	 * @param maxh
	 */
	@SuppressWarnings("finally")
	@SuppressLint("SimpleDateFormat")
	public static String savePhoto(Bitmap bitmap, boolean recycle, String path,
	        int maxw, int maxh)
	{
		int angle = getExifOrientation(path);
		if (angle != 0)
		{
			Matrix matrix = new Matrix();
			matrix.postRotate(angle);
			bitmap = Bitmap
			        .createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap
			                .getHeight(), matrix, true);
		}
		FileOutputStream fos = null;
		String str = null;
		Date date = null;
		if (bitmap.getWidth() > maxw) bitmap = zoomBitmap(bitmap, maxw, maxh
		        * bitmap.getHeight() / bitmap.getWidth());
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");// 获取当前时间，进一步转化为字符串
		date = new Date(System.currentTimeMillis());
		str = format.format(date);
		path = FileUtils.IMAGE_CACHE + str + ".jpg";
		File photo = new File(path);
		photo.getParentFile().mkdirs();
		if (!photo.exists())
		{
			try
			{
				photo.createNewFile();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try
		{
			fos = new FileOutputStream(path);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fos);// 把数据写入文件
		} catch (FileNotFoundException e)
		{
			Log.w("test", e);
		} finally
		{
			try
			{
				if (recycle) if (bitmap != null && !bitmap.isRecycled()) bitmap
				        .recycle();
				fos.flush();
				fos.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			return path;
		}
	}

	static Bitmap zoomBitmap(Bitmap bitmap, int width, int height)
	{
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) width / w);
		float scaleHeight = ((float) height / h);
		matrix.postScale(scaleWidth, scaleHeight);// 利用矩阵进行缩放不会造成内存溢出
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		return newbmp;
	}

	public static Bitmap createBitmap(String name, int maxw)
	{
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inSampleSize = 1;
		BitmapFactory.decodeFile(name, options);
		int width, height;
		if (options.outWidth > maxw)
		{
			width = 1600;
			height = width * options.outHeight / options.outWidth + width
			        * options.outHeight % options.outWidth;
		}
		else
		{
			width = options.outWidth;
			height = options.outHeight;
		}
		options.inSampleSize = computeSampleSize(options, width, width * height);
		options.inJustDecodeBounds = false;
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		Bitmap bitmap = null;
		try
		{
			bitmap = BitmapFactory
			        .decodeStream(new FileInputStream(name), null, options);
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			Log.w("test", e);
		}
		return bitmap;
	}

	static int computeSampleSize(BitmapFactory.Options options,
	        int minSideLength, int maxNumOfPixels)
	{
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8)
		{
			roundedSize = 1;
			while (roundedSize < initialSize)
			{
				roundedSize <<= 1;
			}
		}
		else
		{
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	static int computeInitialSampleSize(BitmapFactory.Options options,
	        int minSideLength, int maxNumOfPixels)
	{
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
		        .sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math
		        .floor(w / minSideLength), Math.floor(h / minSideLength));
		if (upperBound < lowerBound)
		{
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1))
		{
			return 1;
		}
		else if (minSideLength == -1)
		{
			return lowerBound;
		}
		else
		{
			return upperBound;
		}
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri)
	{
		return "com.android.externalstorage.documents".equals(uri
		        .getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri)
	{
		return "com.android.providers.downloads.documents".equals(uri
		        .getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri)
	{
		return "com.android.providers.media.documents".equals(uri
		        .getAuthority());
	}

	/**
	 * 得到 图片旋转 的角度
	 * 
	 * @param filepath
	 * @return
	 */
	public static int getExifOrientation(String filepath)
	{
		int degree = 0;
		ExifInterface exif = null;
		try
		{
			exif = new ExifInterface(filepath);
		} catch (IOException ex)
		{
		}
		if (exif != null)
		{
			int orientation = exif
			        .getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
			if (orientation != -1)
			{
				switch (orientation)
				{
					case ExifInterface.ORIENTATION_ROTATE_90:
						degree = 90;
						break;
					case ExifInterface.ORIENTATION_ROTATE_180:
						degree = 180;
						break;
					case ExifInterface.ORIENTATION_ROTATE_270:
						degree = 270;
						break;
				}
			}
		}
		return degree;
	}
}
