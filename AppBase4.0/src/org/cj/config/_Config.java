package org.cj.config;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.provider.Settings.Secure;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import org.cj.MyApplication;
import org.cj.bean._AbstractObject;

public abstract class _Config {
    public static _Config config;
    Context context;
    $Config c;
    String Session = "";

    public static _Config get() {
        return config;
    }

    public String getServer_Url() {
        return c.debug ? c.getServer_debug() : c.getServer();
    }

    public $Config getC() {
        return c;
    }

    /**
     * @return the context
     */
    public Context getContext() {
        return context;
    }

    public void init(Context context) {
        this.context = context;
        c = new $Config();
        new ConfigParse().parserFromResource(context, "xml", "config");
        c.UA = android.os.Build.MODEL;
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        c.SCREEN_HIGHT = dm.heightPixels;
        c.SCREEN_WIDTH = dm.widthPixels;
        c.DENSITY = dm.density;
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        int screen_size = context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK;

        switch (screen_size) {
            //phone
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)
                    c.IMEI = tm.getDeviceId();
                break;
            //pad;
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)
                    c.IMEI = getDeviceId(context);
                break;
            default:
                break;
        }
        c.mode = screen_size;
        PackageManager pM = context.getPackageManager();
        c.CrashAction = context.getPackageName()
                + ".org.cj.crashservice.action";
        try {
            PackageInfo pi = pM.getPackageInfo(context.getPackageName(), 0);
            c.C_V = pi.versionName;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    String getDeviceId(Context context) {
        String android_id = Secure
                .getString(context.getContentResolver(), Secure.ANDROID_ID);
        return android_id;
    }

    public boolean isLargeScreen() {
        return c.mode == Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    public boolean isPrint() {
        return c.print;
    }

    /**
     * @return the sharepreference
     */
    public String getSharepreference() {
        return c.sharepreference;
    }

    /**
     * @return the isRestart
     */
    public boolean isRestart() {
        return c.isRestart;
    }

    /**
     * @return the session
     */
    public String getSession() {
        return Session;
    }

    /**
     * @param session the session to set
     */
    public final void setSession(String session) {
        Session = session;
        MyApplication.get().getLogUtil().d("session = " + session);
    }

    public String getCrashAction() {
        return c.CrashAction;
    }

    public String getAppName() {
        return c.appName;
    }

    /**
     * @return the c_V
     */
    public String getC_V() {
        return c.C_V;
    }

    /**
     * @return the c_S
     */
    public int getC_S() {
        return c.C_S;
    }

    /**
     * @return the sCREEN_HIGHT
     */
    public int getSCREEN_HIGHT() {
        return c.SCREEN_HIGHT;
    }

    /**
     * @return the sCREEN_WIDTH
     */
    public int getSCREEN_WIDTH() {
        return c.SCREEN_WIDTH;
    }

    /**
     * @return the dENSITY
     */
    public float getDENSITY() {
        return c.DENSITY;
    }

    /**
     * @return the iMEI
     */
    public String getIMEI() {
        return c.IMEI;
    }

    /**
     * @return the uA
     */
    public String getUA() {
        return c.UA;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return c.version;
    }

    /**
     * @return the dir
     */
    public final String getDir() {
        return c.dir;
    }

    /**
     * @return the channel
     */
    public String getChannel() {
        return c.channel;
    }

    public String getEmail() {
        return c.email;
    }

    public boolean isDebug() {
        return c.debug;
    }

    public String getMangerServer() {
        return c.debug ? c.manager_debug_server : c.manager_server;
    }

    final class $Config extends _AbstractObject {
        String C_V = "1.0"; // 客户端版本号
        int C_S = 1;        // 客户端型号
        int SCREEN_HIGHT, SCREEN_WIDTH;    // 屏幕宽高
        float DENSITY;                      // 屏幕密度
        String IMEI = "";
        String UA = "";        // 手机ua
        String appName = "";        //应用名称
        String dir = "";
        String sharepreference = "";
        String CrashAction = "";
        String channel = "";
        boolean isRestart = true;
        String version = "";        //配置版本号
        String server = "";
        String server_debug = "";
        String manager_server = "";        //管理
        String manager_debug_server = "";
        boolean debug = false;
        boolean print = false;
        String email = "";
        int mode;

        /**
         * @param channel the channel to set
         */
        public void setChannel(String channel) {
            this.channel = channel;
        }

        public void setMode(int mode) {
            this.mode = mode;
        }

        /**
         * @param debug the debug to set
         */
        public final void setDebug(boolean debug) {
            this.debug = debug;
        }

        public void setManager_debug_server(String manager_debug_server) {
            this.manager_debug_server = manager_debug_server;
        }

        public void setManager_server(String manager_server) {
            this.manager_server = manager_server;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        /**
         * @param print the print to set
         */
        public final void setPrint(boolean print) {
            this.print = print;
        }

        /**
         * @param sharepreference the sharepreference to set
         */
        public final void setSharepreference(String sharepreference) {
            this.sharepreference = sharepreference;
        }

        /**
         * @param dir the dir to set
         */
        public final void setDir(String dir) {
            this.dir = dir;
        }

        /**
         * @return the server
         */
        public String getServer() {
            return server;
        }

        /**
         * @param server the server to set
         */
        public final void setServer(String server) {
            this.server = server;
        }

        /**
         * @return the server_debug
         */
        public String getServer_debug() {
            return server_debug;
        }

        /**
         * @param server_debug the server_debug to set
         */
        public final void setServer_debug(String server_debug) {
            this.server_debug = server_debug;
        }

        public final void setRestart(boolean isRestart) {
            this.isRestart = isRestart;
        }

        public final void setAppName(String appName) {
            this.appName = appName;
        }

        /**
         * @param version the version to set
         */
        public void setVersion(String version) {
            this.version = version;
        }
    }
}
