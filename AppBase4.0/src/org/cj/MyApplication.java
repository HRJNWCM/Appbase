package org.cj;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.cj.comm.FileUtils;
import org.cj.comm.SharePreferenceUtil;
import org.cj.config._Config;
import org.cj.image.ImageLoadUtil;
import org.cj.logUtil.LogUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

public abstract class MyApplication extends Application {
    protected static MyApplication instance;
    protected LogUtil logUtil;
    HashMap<String, Object> objMap = new HashMap<String, Object>();
    HashMap<String, _IBase> uis = new HashMap<>();

    public static MyApplication get() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public void restart(Context context) {
        finish();
        Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        //		PendingIntent restartIntent = PendingIntent
        //		        .getActivity(this, 0, intent, Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // 退出程序
        //		AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //		mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 2000, restartIntent);
        startActivity(intent);
    }

    /**
     * @return the logUtil
     */
    public LogUtil getLogUtil() {
        return logUtil;
    }

    /**
     * @return the objMap
     */
    @SuppressWarnings("unchecked")
    public HashMap<String, Object> getObjects() {
        if (objMap == null) try {
            objMap = (HashMap<String, Object>) SharePreferenceUtil.get()
                    .get(HashMap.class.getSimpleName(), new Object());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return objMap;
    }

    /**
     * map
     *
     * @param key
     * @return
     */
    public Object getObj(String key) {
        return getObjects().get(key);
    }

    /**
     * map
     *
     * @param key
     * @param o
     */
    public void setObj(String key, Object o) {
        getObjects().put(key, o);
    }

    public void removeObj(String key) {
        if (getObjects().containsKey(key)) getObjects().remove(key);
    }

    /**
     * sharepreference
     *
     * @param key
     * @param value
     * @throws IOException
     */
    public void set(String key, Object value) {
        try {
            SharePreferenceUtil.get().set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * sharepreference
     *
     * @param key
     * @throws IOException
     */
    public Object get(String key) {
        try {
            return SharePreferenceUtil.get().get(key, new Object());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * sharepreference
     *
     * @param key
     * @param defaultValue default value
     * @throws IOException
     */
    public Object get(String key, Object defaultValue) {
        try {
            return SharePreferenceUtil.get().get(key, defaultValue);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    /**
     * sharepreference
     *
     * @param key
     * @throws IOException
     */
    public boolean remove(String key) {
        return SharePreferenceUtil.get().remove(key);
    }

    /**
     * @param objMap the objMap to set
     */
    public void setObjMap(HashMap<String, Object> objMap) {
        this.objMap = objMap;
        try {
            SharePreferenceUtil.get()
                    .set(HashMap.class.getSimpleName(), objMap);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 初始化
     *
     */
    public void init() {
        FileUtils.init(_Config.get().getDir());
        LogUtil.initAppLogger(FileUtils.LOG, _Config.get().getAppName());
        logUtil = LogUtil.HLog();
        SharePreferenceUtil.get()
                .init(this, _Config.get().getSharepreference());
        logUtil.d(_Config.get().getC());
    }

    /**
     * 图片缓存路径
     *
     * @param imagecache
     */
    protected void useImage(String imagecache) {
        ImageLoadUtil.get().initImageCacheManager(this, imagecache);
    }

    /**
     * 图片缓存路径
     *
     */
    protected void useImage() {
        ImageLoadUtil.get().initImageCacheManager(this, FileUtils.IMAGE_CACHE);
    }

    public ImageLoader getImageLoader() {
        return ImageLoadUtil.get().getImageLoader();
    }

    public HashMap<String, _IBase> getUis() {
        return uis;
    }

    public void push(_IBase base) {
        this.uis.put(base.getClass().getName(), base);
    }

    public void pull(_IBase base) {
        if (uis.containsKey(base.getClass().getName()))
            this.uis.remove(base.getClass().getName());
    }

    public void finish() {
        clear();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public _IBase getAppointActivity(String key) {
        if (uis.containsKey(key)) return uis.get(key);
        return null;
    }

    public void clearTop(_IBase... bases) {
        for (_IBase b : bases
                ) {
            if (uis.containsKey(b.getClass().getName())) {
                _IBase base = uis.get(b.getClass().getName());
                ((Activity) base).finish();
            }

        }
    }

    public void clear() {
        Iterator<String> iterator = uis.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            _IBase base = uis.get(key);
            ((Activity) base).finish();
        }
    }


    public void clearCache() {
    }
}
