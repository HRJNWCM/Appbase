package org.cj.comm;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.CollationKey;
import java.text.Collator;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.cj.MyApplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.util.Log;

/**
 * 文件管理
 * 
 * @author Eway
 */
public class FileUtils
{
	public static final String	PATH	          = android.os.Environment
	                                                      .getExternalStorageDirectory()
	                                                      .getAbsolutePath(); // 保存文件的路径
	public static String	   APP	              = PATH;	                  // 应用路径
	public static String	   LOG	              = APP + "Log/";
	public static String	   APK	              = APP + "app.apk";
	public static String	   DOWNLOAD_TEMP_PATH	= APP + "temp/";
	public static String	   IMAGE_CACHE	      = APP + "imageCache/";	  // 图片缓存
	public static String	   USR	              = APP + "usr/";
	public static String	   DOWNLOAD	          = APP + "download/";

	public static void init()
	{
		// 文件目录
		initDirectory(APP);
		// 日志目录
		initDirectory(LOG);
		initDirectory(USR);
		// 图片缓存目录
		initDirectory(IMAGE_CACHE);
		initDownloadDir();
		// 删除apk文件
		deleteFile(APK);
	}

	public static void init(String name)
	{
		APP = PATH + "/" + name + "/";
		LOG = APP + "Log/";
		APK = APP + name + ".apk";
		IMAGE_CACHE = APP + "img/";
		USR = APP + "usr/";
		DOWNLOAD_TEMP_PATH = APP + "temp/";
		DOWNLOAD = APP + "download/";
		// 文件目录
		initDirectory(APP);
		// 日志目录
		initDirectory(LOG);
		initDirectory(USR);
		initDirectory(IMAGE_CACHE);
		initDirectory(DOWNLOAD_TEMP_PATH);
		initDirectory(DOWNLOAD);
		deleteFile(APK);
	}

	/**
	 * 初始化文件夹目录
	 */
	public static void initDirectory(String path)
	{
		File f = new File(path);
		if (!f.exists())
		{
			f.mkdirs();
		}
	}

	/**
	 * 重置缓存目录
	 */
	public static void resetCache()
	{
		deleteFile2(IMAGE_CACHE);
		// deleteFile2(CACHE);
		deleteFile2(DOWNLOAD_TEMP_PATH);
		initDirectory(IMAGE_CACHE);
		initDownloadDir();
	}

	/**
	 * 初始化下载的目录
	 */
	public static void initDownloadDir()
	{
		File f = new File(DOWNLOAD_TEMP_PATH);
		if (!f.exists())
		{
			f.mkdir();
		}
	}

	public static File getFile(String path, String name) throws IOException
	{
		initDirectory(path);
		File file = new File(path, name);
		if (!file.exists())
		{
			file.createNewFile();
		}
		return file;
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param path
	 * @return
	 */
	public static boolean isFileExit(String path)
	{
		File file = new File(path);
		return file.exists();
	}

	public static String getDownDir()
	{
		initDownloadDir();
		return DOWNLOAD_TEMP_PATH;
	}

	/**
	 * 获取rar压缩文件列表
	 * 
	 * @param path
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	public static List<File> getRARFilesByPath(String path)
	{
		List<File> fileList = new ArrayList<File>();
		File dir = new File(path);
		File[] files = dir.listFiles();
		if (null == files)
		{
			return fileList;
		}
		for ( File f : files)
		{
			String name = f.getName().trim().toLowerCase();
			if (name.endsWith(".zip") || name.endsWith(".rar"))
			{
				fileList.add(f);
			}
		}
		return fileList;
	}

	/**
	 * 分割文件
	 * 
	 * @param file
	 * @return
	 */
	public static String getName(String file)
	{
		if (file != null && !"".equals(file))
		{
			int index = file.lastIndexOf("/");
			if (index != -1)
			{
				return file.substring(index + 1);
			} else
			{
				return "";
			}
		}
		return null;
	}

	/**
	 * 移动文件
	 * 
	 * @param tempLocation
	 * @param newLocation
	 * @throws IOException
	 */
	public static void moveFile(String tempLocation, String newLocation)
	        throws IOException
	{
		File oldLocation = new File(tempLocation);
		if (oldLocation != null && oldLocation.exists())
		{
			BufferedInputStream reader = new BufferedInputStream(new FileInputStream(oldLocation));
			BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(newLocation, false));
			try
			{
				byte[] buff = new byte[8192];
				int numChars;
				while ((numChars = reader.read(buff, 0, buff.length)) != -1)
				{
					writer.write(buff, 0, numChars);
				}
				writer.flush();
			} catch (IOException ex)
			{
			} finally
			{
				try
				{
					if (reader != null)
					{
						writer.close();
						reader.close();
					}
				} catch (IOException ex)
				{
				}
			}
		}
	}

	/**
	 * 得到保存文件的路
	 */
	public static String getPath()
	{
		return PATH;
	}

	/**
	 * 根据当前时间生成
	 * 
	 * @param suffix
	 */
	public static String newFileName(String suffix)
	{
		return PATH + "/" + System.currentTimeMillis() + "." + suffix;
	}

	/**
	 * 删除升级apk文件
	 */
	public static void delUpgradeAPKFile()
	{
		deleteFile(APP);
	}

	/**
	 * 删除文件或文件夹
	 * 
	 * @return
	 */
	public static void deleteFile(String fileName)
	{
		File f = new File(fileName);
		if (f.exists())
		{
			if (f.isFile())
			{
				f.delete();
			} else if (f.isDirectory())
			{
				String[] filelist = f.list();
				for ( int i = 0; i < filelist.length; i++)
				{
					deleteFile(fileName + filelist[i]);
				}
			}
		}
	}

	/**
	 * 删除文件或文件夹
	 * 
	 * @return
	 */
	public static boolean deleteFile2(String fileName)
	{
		File f = new File(fileName);
		if (f.exists())
		{
			if (f.isFile())
			{
				f.delete();
			} else if (f.isDirectory())
			{
				String[] filelist = f.list();
				for ( int i = 0; i < filelist.length; i++)
				{
					deleteFile2(fileName + filelist[i]);
				}
			}
		}
		return true;
	}

	/**
	 * 创建新文
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean newFile(String fileName)
	{
		File f = new File(fileName);
		if (!f.exists())
		{
			try
			{
				return f.createNewFile();
			} catch (IOException e)
			{
			}
		}
		return false;
	}

	/**
	 * 获取文件格式
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getExtension(String fileName)
	{
		int length = fileName.indexOf(".");
		return fileName.substring(length + 1);
	}

	/**
	 * 判断文件夹是否存
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean isFileExists(String fileName)
	{
		File f = new File(PATH + fileName);
		return f.isDirectory();
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean isExists(String fileName)
	{
		File f = new File(fileName);
		if (f.exists())
		{
			if (f.length() == 0) f.delete();
		}
		return f.exists();
	}

	public static void createFile(String fileName)
	{
		File f = new File(fileName);
		if (f.exists())
		{
			if (f.length() == 0) f.delete();
		} else
		{
			f.mkdirs();
		}
	}

	public static String newFileName(String fileName, String suffix)
	{
		return PATH + "/" + fileName + "." + suffix;
	}

	/**
	 * 获取sd卡剩余容
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static boolean getAvailableStore()
	{
		// 取得sdcard文件路径
		StatFs statFs = new StatFs(Environment.getExternalStorageDirectory()
		        .getPath());
		// 获取block的SIZE
		long blocSize = statFs.getBlockSize();
		// 获取BLOCK数量
		long totalBlocks = statFs.getBlockCount();
		// 可使用的Block的数
		long availaBlock = statFs.getAvailableBlocks();
		long total = totalBlocks * blocSize;
		long availableSpare = availaBlock * blocSize;
		Log.v("", "sdcard availableSpare:" + availableSpare);
		Log.v("", "sdcard total:" + total);
		float result = availableSpare * 100 / total;
		Log.v("", "sdcard size%:" + result);
		if (result < 5) return false;
		else
			return true;
	}

	public static boolean Move(String srcFile, String destPath)
	{
		// File (or directory) to be moved
		File file = new File(srcFile);
		// Destination directory
		File dir = new File(destPath);
		// Move file to new directory
		boolean retCode = file.renameTo(new File(dir, file.getName()));
		return retCode;
	}

	/**
	 * 文件排序
	 * 
	 * @author Eway
	 */
	public static class FileWrapper implements Comparable<Object>
	{
		File	file;

		public FileWrapper(File file)
		{
			this.file = file;
		}

		public int compareTo(Object obj)
		{
			assert obj instanceof FileWrapper;
			FileWrapper castObj = (FileWrapper) obj;
			if (this.file.getName().compareTo(castObj.getFile().getName()) > 0)
			{
				return -1;
			} else if (this.file.getName().compareTo(castObj.getFile()
			        .getName()) < 0)
			{
				return 1;
			} else
			{
				return 0;
			}
		}

		public File getFile()
		{
			return this.file;
		}
	}

	/**
	 * 判断文件系统目录是否可读
	 * 
	 * @param fileDir
	 *            文件系统目录
	 * @return 文件系统目录可读写返回true, 否则返回false
	 */
	public static boolean isReadAndWrite(String fileDir)
	{
		boolean flag = false;
		try
		{
			File fc = new File(fileDir);
			if (fc.canRead() && fc.canWrite())
			{
				flag = true;
			}
			fc = null;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 判断文件可读
	 * 
	 * @param fileDir
	 * @return
	 */
	public static boolean isCanRead(String fileDir)
	{
		boolean flag = false;
		try
		{
			File fc = new File(fileDir);
			if (fc.canRead())
			{
				flag = true;
			}
			fc = null;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 判断文件可写
	 * 
	 * @param fileDir
	 * @return
	 */
	public static boolean isCanWrite(String fileDir)
	{
		boolean flag = false;
		try
		{
			File fc = new File(fileDir);
			if (fc.canWrite())
			{
				flag = true;
			}
			fc = null;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 文件复制
	 * 
	 * @param oldPath
	 * @param newPath
	 * @return
	 */
	public static boolean Copy(String oldPath, String newPath)
	{
		try
		{
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists())
			{
				InputStream inStream = new FileInputStream(oldPath);
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1)
				{
					bytesum += byteread;
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
				fs.close();
				fs = null;
			}
			return true;
		} catch (Exception e)
		{
			// System.out.println( "error  ");
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * MD5的算法在RFC1321 中定在RFC 1321中，给出了Test suite用来你的实现是否正确MD5 ("")
	 * =d41d8cd98f00b204e9800998ecf8427e MD5 ("a")
	 * =0cc175b9c0f1b6a831c399e269772661 MD5 ("abc")
	 * =900150983cd24fb0d6963f7d28e17f72 MD5 ("message digest")
	 * =f96b697d7cb7938d525a2f31aaf161d0 MD5 ("abcdefghijklmnopqrstuvwxyz")
	 * =c3fcd3d76192e4007dfb496cca67e13b
	 */
	public static class MD5
	{
		static char	hexDigits[]	= { // 用来将字节转换成 16 进制表示的字
		                        '0', '1', '2', '3', '4', '5', '6', '7', '8',
		                                '9', 'a', 'b', 'c', 'd', 'e', 'f' };

		public static String getMD5(String val) throws NoSuchAlgorithmException
		{
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(val.getBytes());
			byte[] m = md5.digest();// 加密
			char str[] = new char[16 * 2];
			int k = 0; // 表示转换结果中对应的字符位置
			for ( int i = 0; i < 16; i++)
			{
				byte byte0 = m[i]; //
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			String s = new String(str);
			return s.substring(8, 24);
		}

		public static String Md5_16(String plainText)
		{
			String result = null;
			try
			{
				MessageDigest md = MessageDigest.getInstance("MD5");
				md.update(plainText.getBytes());
				byte b[] = md.digest();
				int i;
				StringBuffer buf = new StringBuffer("");
				for ( int offset = 0; offset < b.length; offset++)
				{
					i = b[offset];
					if (i < 0) i += 256;
					if (i < 16) buf.append("0");
					buf.append(Integer.toHexString(i));
				}
				result = buf.toString().substring(8, 24);
				System.out.println("mdt 16bit: "
				        + buf.toString().substring(8, 24));
				System.out.println("md5 32bit: " + buf.toString());
			} catch (NoSuchAlgorithmException e)
			{
				e.printStackTrace();
			}
			return result;
		}

		public final static String MD5_32(String str)
		{
			MessageDigest messageDigest = null;
			try
			{
				messageDigest = MessageDigest.getInstance("MD5");
				messageDigest.reset();
				messageDigest.update(str.getBytes("UTF-8"));
			} catch (NoSuchAlgorithmException e)
			{
				System.out.println("NoSuchAlgorithmException caught!");
				System.exit(-1);
			} catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
			byte[] byteArray = messageDigest.digest();
			StringBuffer md5StrBuff = new StringBuffer();
			for ( int i = 0; i < byteArray.length; i++)
			{
				if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) md5StrBuff
				        .append("0")
				        .append(Integer.toHexString(0xFF & byteArray[i]));
				else
					md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
			}
			return md5StrBuff.toString();
		}
	}

	public static class IComparator implements Comparator<Object>
	{
		// 关于Collator。
		private Collator		   collator	= Collator.getInstance(); // 点击查看中文api详解
		private static IComparator	instance;

		public IComparator()
		{
		}

		public static IComparator getInstance()
		{
			if (instance == null) instance = new IComparator();
			return instance;
		}

		/**
		 * compare 实现排序。
		 * 
		 * @param o1
		 *            Object
		 * @param o2
		 *            Object
		 * @return int
		 */
		public int compare(Object o1, Object o2)
		{
			// 把字符串转换为一系列比特，它们可以以比特形式与 CollationKeys 相比较
			CollationKey key1 = collator.getCollationKey(o1.toString());// 要想不区分大小写进行比较用o1.toString().toLowerCase()
			CollationKey key2 = collator.getCollationKey(o2.toString());
			return key1.compareTo(key2);// 返回的分别为1,0,-1
			// 分别代表大于，等于，小于。要想按照字母降序排序的话 加个“-”号
		}
	}

	/**
	 * 文件解压缩类
	 * 
	 * @author Eway
	 */
	public static class ZipUtils
	{
		public static final int	MAX		 = 0xe1;
		public static final int	PROGRESS	= 0xe2;
		public static final int	FINISH		= 0xe3;
		public static final int	ERROR		= 0xe4;

		public static void Ectract(String zipPathFile, String sDestPath)
		        throws Exception
		{
			Ectract(zipPathFile, sDestPath, null);
		}

		/**
		 * @param sZipPathFile
		 *            ：需要解压的文件
		 * @param sDestPath
		 *            : 解压文件存放的路
		 * @throws IOException
		 */
		public static void Ectract(String zipPathFile, String sDestPath,
		        Handler handler) throws Exception
		{
			MyApplication.get().getLogUtil().d("start ectract " + zipPathFile);
			MyApplication.get().getLogUtil().d("des " + sDestPath);
			File zipfile = new File(zipPathFile);
			if (!zipfile.exists()) throw new FileNotFoundException(zipPathFile
			        + " is not exist");
			File file = new File(sDestPath);
			if (!file.exists()) file.mkdirs();
			String[] fileList = file.list();
			if (fileList != null) for ( int i = 0; i < fileList.length; i++)
			{
				if (!fileList[i].endsWith(".zip"))
				{
					File f = new File(sDestPath + fileList[i]);
					f.delete();
				}
			}
			// 先指定压缩档的位置和档名，建立FileInputStream对象
			FileInputStream fins = new FileInputStream(zipPathFile);
			ZipInputStream zins = new ZipInputStream(fins);// 将fins传入ZipInputStream
			ZipEntry ze = null;
			if (handler != null) handler.obtainMessage(MAX, zins.available())
			        .sendToTarget();
			byte ch[] = new byte[8192];
			while ((ze = zins.getNextEntry()) != null)
			{
				File zfile = new File(sDestPath + "/"
				        + ze.getName().replace("\\", "/"));
				File fpath = new File(zfile.getParentFile().getPath());
				if (ze.isDirectory())
				{
					if (!zfile.exists())
					{
						zfile.mkdirs();
					}
					zins.closeEntry();
				} else
				{
					if (!fpath.exists())
					{
						fpath.mkdirs();
					}
					FileOutputStream fouts = new FileOutputStream(zfile, false);
					int i;
					while ((i = zins.read(ch)) != -1)
					{
						fouts.write(ch, 0, i);
					}
					zins.closeEntry();
					fouts.close();
				}
				if (handler != null) handler.obtainMessage(PROGRESS)
				        .sendToTarget();
			}
			if (handler != null) handler.obtainMessage(FINISH).sendToTarget();
			fins.close();
			zins.close();
		}
	}

	public static class StorageUtil
	{
		private static StorageManager	storageManager;
		private static Method		  method;
		public static StorageUtil		instance;

		private StorageUtil()
		{
			// TODO Auto-generated constructor stub
		}

		@SuppressLint("InlinedApi")
		public static StorageUtil getInstance(Context context)
		{
			if (context != null)
			{
				storageManager = (StorageManager) context
				        .getSystemService(Context.STORAGE_SERVICE);
				try
				{
					method = storageManager.getClass()
					        .getMethod("getVolumePaths");
				} catch (NoSuchMethodException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return instance != null ? instance : new StorageUtil();
		}

		public static String[] getStorageList()
		{
			String[] paths = null;
			try
			{
				paths = (String[]) method.invoke(storageManager);
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return paths;
		}
	}

	@SuppressLint("NewApi")
	//4.0外置sdcard
	public File findSDCARD()
	{
		boolean b = Environment.MEDIA_MOUNTED.equals(Environment
		        .getExternalStorageState());
		if (!b) return null;
		File extFile = Environment.getExternalStorageDirectory();
		File[] files = extFile.listFiles();
		if (files == null) return null;
		for ( File f : files)
		{
			if (extFile.isDirectory()
			        && f.canWrite()//sd卡一定是可写的.如果去掉这条件的话会得到隐藏的系统专用文件夹
			        && Math.abs(extFile.getTotalSpace() - f.getTotalSpace()) > 2 * 1024 * 1024)
			{
				//外部存储器与子目录的在未挂载的情况下有相同的存储容量.子目录是挂载的话.就会出现不同的存储容量
				//2 * 1024 * 1024 是用来指出两存储器的存储容量大小差异 当然可以不用这么大 随意
				return f;
			}
		}
		return null;
	}

	/**
	 * 返回挂载sdcard所有路径
	 * 
	 * @return
	 */
	public static String[] getExterPath()
	{
		String sdcard_path = "";
		//得到路径
		try
		{
			Runtime runtime = Runtime.getRuntime();
			Process proc = runtime.exec("mount");
			InputStream is = proc.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			String line;
			BufferedReader br = new BufferedReader(isr);
			while ((line = br.readLine()) != null)
			{
				if (line.contains("secure")) continue;
				if (line.contains("asec")) continue;
				if (line.contains("fat"))
				{
					String columns[] = line.split(" ");
					if (columns != null && columns.length > 1)
					{
						sdcard_path = sdcard_path.concat(" " + columns[1]);
					}
				} else if (line.contains("fuse"))
				{
					String columns[] = line.split(" ");
					if (columns != null && columns.length > 1)
					{
						sdcard_path = sdcard_path.concat(columns[1]);
					}
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return sdcard_path.split(" ");
	}

	/**
	 * AES方式解密文件
	 * 
	 * @param sourceFile
	 * @return
	 */
	public static File decryptFile(File sourceFile, String fileType, String sKey)
	{
		File decryptFile = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try
		{
			decryptFile = File.createTempFile(sourceFile.getName(), fileType);
			Cipher cipher = initAESCipher(sKey, Cipher.DECRYPT_MODE);
			inputStream = new FileInputStream(sourceFile);
			outputStream = new FileOutputStream(decryptFile);
			CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, cipher);
			byte[] buffer = new byte[1024];
			int r;
			while ((r = inputStream.read(buffer)) >= 0)
			{
				cipherOutputStream.write(buffer, 0, r);
			}
			cipherOutputStream.close();
		} catch (IOException e)
		{
			e.printStackTrace(); // To change body of catch statement use File |
			                     // Settings | File Templates.
		} finally
		{
			try
			{
				inputStream.close();
			} catch (IOException e)
			{
				e.printStackTrace(); // To change body of catch statement use
				                     // File | Settings | File Templates.
			}
			try
			{
				outputStream.close();
			} catch (IOException e)
			{
				e.printStackTrace(); // To change body of catch statement use
				                     // File | Settings | File Templates.
			}
		}
		return decryptFile;
	}

	/**
	 * 对文件进行AES加密
	 * 
	 * @param sourceFile
	 * @param fileType
	 * @param sKey
	 * @return
	 */
	public static File encryptFile(File sourceFile, String fileType, String sKey)
	{
		// 新建临时加密文件
		File encrypfile = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try
		{
			inputStream = new FileInputStream(sourceFile);
			encrypfile = File.createTempFile(sourceFile.getName(), fileType);
			outputStream = new FileOutputStream(encrypfile);
			Cipher cipher = initAESCipher(sKey, Cipher.ENCRYPT_MODE);
			// 以加密流写入文件
			CipherInputStream cipherInputStream = new CipherInputStream(inputStream, cipher);
			byte[] cache = new byte[1024];
			int nRead = 0;
			while ((nRead = cipherInputStream.read(cache)) != -1)
			{
				outputStream.write(cache, 0, nRead);
				outputStream.flush();
			}
			cipherInputStream.close();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace(); // To change body of catch statement use File |
			                     // Settings | File Templates.
		} catch (IOException e)
		{
			e.printStackTrace(); // To change body of catch statement use File |
			                     // Settings | File Templates.
		} finally
		{
			try
			{
				inputStream.close();
			} catch (IOException e)
			{
				e.printStackTrace(); // To change body of catch statement use
				                     // File | Settings | File Templates.
			}
			try
			{
				outputStream.close();
			} catch (IOException e)
			{
				e.printStackTrace(); // To change body of catch statement use
				                     // File | Settings | File Templates.
			}
		}
		return encrypfile;
	}

	/**
	 * 初始化 AES Cipher
	 * 
	 * @param sKey
	 * @param cipherMode
	 * @return
	 */
	@SuppressLint("TrulyRandom")
	public static Cipher initAESCipher(String sKey, int cipherMode)
	{
		// 创建Key gen
		KeyGenerator keyGenerator = null;
		Cipher cipher = null;
		try
		{
			keyGenerator = KeyGenerator.getInstance("AES");
			keyGenerator.init(128, new SecureRandom(sKey.getBytes()));
			SecretKey secretKey = keyGenerator.generateKey();
			byte[] codeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(codeFormat, "AES");
			cipher = Cipher.getInstance("AES");
			// 初始化
			cipher.init(cipherMode, key);
		} catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace(); // To change body of catch statement use File |
			                     // Settings | File Templates.
		} catch (NoSuchPaddingException e)
		{
			e.printStackTrace(); // To change body of catch statement use File |
			                     // Settings | File Templates.
		} catch (InvalidKeyException e)
		{
			e.printStackTrace(); // To change body of catch statement use File |
			                     // Settings | File Templates.
		}
		return cipher;
	}

	public static byte[] toByteArray(String filename)
	{
		File f = new File(filename);
		if (!f.exists())
		{
			return null;
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
		BufferedInputStream in = null;
		try
		{
			in = new BufferedInputStream(new FileInputStream(f));
			int buf_size = 1024;
			byte[] buffer = new byte[buf_size];
			int len = 0;
			while (-1 != (len = in.read(buffer, 0, buf_size)))
			{
				bos.write(buffer, 0, len);
			}
			return bos.toByteArray();
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				in.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			try
			{
				bos.close();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public static class FileSizeUtil
	{
		public static final int	SIZETYPE_B	= 1;	//获取文件大小单位为B的double值
		public static final int	SIZETYPE_KB	= 2;	//获取文件大小单位为KB的double值
		public static final int	SIZETYPE_MB	= 3;	//获取文件大小单位为MB的double值
		public static final int	SIZETYPE_GB	= 4;	//获取文件大小单位为GB的double值

		/**
		 * 获取文件指定文件的指定单位的大小
		 * 
		 * @param filePath
		 *            文件路径
		 * @param sizeType
		 *            获取大小的类型1为B、2为KB、3为MB、4为GB
		 * @return double值的大小
		 */
		public static double getFileOrFilesSize(String filePath, int sizeType)
		{
			File file = new File(filePath);
			long blockSize = 0;
			try
			{
				if (file.isDirectory())
				{
					blockSize = getFileSizes(file);
				} else
				{
					blockSize = getFileSize(file);
				}
			} catch (Exception e)
			{
				e.printStackTrace();
				Log.e("获取文件大小", "获取失败!");
			}
			return FormetFileSize(blockSize, sizeType);
		}

		/**
		 * 调用此方法自动计算指定文件或指定文件夹的大小
		 * 
		 * @param filePath
		 *            文件路径
		 * @return 计算好的带B、KB、MB、GB的字符串
		 */
		public static String getAutoFileOrFilesSize(String filePath)
		{
			File file = new File(filePath);
			long blockSize = 0;
			try
			{
				if (file.isDirectory())
				{
					blockSize = getFileSizes(file);
				} else
				{
					blockSize = getFileSize(file);
				}
			} catch (Exception e)
			{
				e.printStackTrace();
				Log.e("获取文件大小", "获取失败!");
			}
			return FormetFileSize(blockSize);
		}

		/**
		 * 获取指定文件大小
		 * 
		 * @param f
		 * @return
		 * @throws Exception
		 */
		@SuppressWarnings("resource")
        private static long getFileSize(File file) throws Exception
		{
			long size = 0;
			if (file.exists())
			{
				FileInputStream fis = null;
				fis = new FileInputStream(file);
				size = fis.available();
			} else
			{
				file.createNewFile();
				Log.e("获取文件大小", "文件不存在!");
			}
			return size;
		}

		/**
		 * 获取指定文件夹
		 * 
		 * @param f
		 * @return
		 * @throws Exception
		 */
		private static long getFileSizes(File f) throws Exception
		{
			long size = 0;
			File flist[] = f.listFiles();
			for ( int i = 0; i < flist.length; i++)
			{
				if (flist[i].isDirectory())
				{
					size = size + getFileSizes(flist[i]);
				} else
				{
					size = size + getFileSize(flist[i]);
				}
			}
			return size;
		}

		/**
		 * 转换文件大小
		 * 
		 * @param fileS
		 * @return
		 */
		private static String FormetFileSize(long fileS)
		{
			DecimalFormat df = new DecimalFormat("#.00");
			String fileSizeString = "";
			String wrongSize = "0B";
			if (fileS == 0)
			{
				return wrongSize;
			}
			if (fileS < 1024)
			{
				fileSizeString = df.format((double) fileS) + "B";
			} else if (fileS < 1048576)
			{
				fileSizeString = df.format((double) fileS / 1024) + "KB";
			} else if (fileS < 1073741824)
			{
				fileSizeString = df.format((double) fileS / 1048576) + "MB";
			} else
			{
				fileSizeString = df.format((double) fileS / 1073741824) + "GB";
			}
			return fileSizeString;
		}

		/**
		 * 转换文件大小,指定转换的类型
		 * 
		 * @param fileS
		 * @param sizeType
		 * @return
		 */
		private static double FormetFileSize(long fileS, int sizeType)
		{
			DecimalFormat df = new DecimalFormat("#.00");
			double fileSizeLong = 0;
			switch (sizeType)
			{
				case SIZETYPE_B:
					fileSizeLong = Double.valueOf(df.format((double) fileS));
					break;
				case SIZETYPE_KB:
					fileSizeLong = Double.valueOf(df
					        .format((double) fileS / 1024));
					break;
				case SIZETYPE_MB:
					fileSizeLong = Double.valueOf(df
					        .format((double) fileS / 1048576));
					break;
				case SIZETYPE_GB:
					fileSizeLong = Double.valueOf(df
					        .format((double) fileS / 1073741824));
					break;
				default:
					break;
			}
			return fileSizeLong;
		}
	}
}
