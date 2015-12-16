package org.cj.image;

import java.io.File;

import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

public class ImageLoadUtil
{
	static ImageLoadUtil	instance	= new ImageLoadUtil();
	ImageLoader	         imageLoader	= ImageLoader.getInstance();
	int	                 _count	     = 3;

	private ImageLoadUtil()
	{
		// TODO Auto-generated constructor stub
	}

	public static ImageLoadUtil get()
	{
		return instance;
	}

	public ImageLoader getImageLoader()
	{
		return imageLoader;
	}

	public void setThreadCount(int sum)
	{
		this._count = sum;
	}

	public void initImageCacheManager(Context context, String dir)
	{
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
		        .threadPriority(Thread.NORM_PRIORITY - 2)
		        //				.denyCacheImageMultipleSizesInMemory()
		        //缓存路径
		        .diskCache(new UnlimitedDiskCache(new File(dir)))
		        //		        .discCache(new UnlimitedDiscCache(new File(dir)))
		        .threadPoolSize(_count)
		        //缓存文件数量
		        //		        .discCacheFileCount(100)
		        .memoryCache(new WeakMemoryCache())
		        .tasksProcessingOrder(QueueProcessingType.LIFO)
		        .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000))
		        //		        .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
		        .tasksProcessingOrder(QueueProcessingType.LIFO).build();
		imageLoader.init(config);
	}
}
