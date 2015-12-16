package org.cj;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.cj.androidexception.EncodeMessageException;
import org.cj.androidexception.ServerErrorException;
import org.cj.androidexception.SessionTimeoutException;
import org.cj.comm.FileUtils;
import org.cj.config._Config;
import org.cj.http.AsyncHttpClientManager;
import org.cj.http.HttpUtils;
import org.cj.http.protocol.XmlRequest;
import org.cj.http.protocol._IProtocol;
import org.cj.upgrade.AppUpgrade.AppUpgradeListener;
import org.cj.view.MyProgressDialog;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpHost;
import cz.msebera.android.httpclient.conn.HttpHostConnectException;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;

public abstract class BaseFragmentActivity extends FragmentActivity implements
        _IBase, AppUpgradeListener {
    Toast toast;
    TextView mTitle;
    MyProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setTheme(R.style.Activity_theme);
        //		setTheme(MResource.getIdByNaome(this, "style", "Activity_theme"));
        if (savedInstanceState != null
                && savedInstanceState.getBoolean("crash")) {
            MyApplication.get().restart(getApplicationContext());
            return;
        }
        initView(savedInstanceState);
        MyApplication.get().push(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("crash", true);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.get().pull(this);
    }

    protected View getRootView(Activity context) {
        return ((ViewGroup) context.findViewById(android.R.id.content))
                .getChildAt(0);
    }

    /**
     * 模拟back键
     */
    public void onBackKeyDown() {
        new Thread() {
            public void run() {
                try {
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                } catch (Exception e) {
                }
            }
        }.start();
        //		try
        //		{
        //			Runtime runtime = Runtime.getRuntime();
        //			runtime.exec("input keyevent " + KeyEvent.KEYCODE_BACK);
        //		} catch (IOException e)
        //		{
        //			Log.e("Exception when doBack", e.toString());
        //		}
    }

    /**
     * 显示toast信息提示
     *
     * @param str
     */
    @Override
    public void showToast(String str) {
        if (toast == null) {
            toast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
        } else toast.setText(str);
        toast.show();
    }

    public void dimissToast() {
        if (toast != null)
            toast.cancel();
        toast = null;
    }

    @Override
    public void showToast(int id) {
        if (id == -1) return;
        String str = getString(id);
        if (str != null) this.showToast(str);
    }

    @Override
    public void buildProgressDialog(int id) {
        if (id == -1) return;
        if (progressDialog == null) progressDialog = new MyProgressDialog(this);
//        if(progressWheel==null)
//        progressWheel = (ProgressWheel)getLayoutInflater().inflate(R.layout.materaial_progress,null);
        progressDialog.show(id);
        progressDialog.setCanceledOnTouchOutside(false);

    }

    @Override
    public void cancleProgressDialog() {
        if (progressDialog != null) progressDialog.dismiss();
    }

    @Override
    public void dismissProgressDialog() {
        // TODO Auto-generated method stub
        if (progressDialog != null) progressDialog.dismiss();
    }

    @Override
    public void onDialogCancled() {
        // TODO Auto-generated method stub
    }

    protected void goHome() {
        // 实现Home键效果
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addCategory(Intent.CATEGORY_HOME);
        startActivity(i);
    }

    @Override
    public void hideInputMethod() {
        try {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(getCurrentFocus()
                    .getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public boolean isWorked(String name) {
        ActivityManager myManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager
                .getRunningServices(100);
        for (int i = 0; i < runningService.size(); i++)
            if (runningService.get(i).service.getClassName().toString()
                    .equals(name)) return true;
        return false;
    }

    @Override
    public Header[] getRequestHeaders(String head) {
        if (!isRequestHeadersAllowed()) return null;
        List<Header> headers = new ArrayList<Header>();
        String[] lines = head.split("\\r?\\n");
        for (String line : lines) {
            try {
                String headersRaw = head == null ? null : head;
                if (headersRaw != null && headersRaw.length() > 3) {
                    String[] kv = line.split("=");
                    if (kv.length != 2) {
                        Log.e("BaseFragmentActivity", "Wrong header format, may be 'Key=Value' only");
                        continue;
                    }
                    MyApplication.get().logUtil.d("request header:" + kv[0].trim() + "    " + kv[1].trim());
                    headers.add(new BasicHeader(kv[0].trim(), kv[1].trim()));
                }
            } catch (Throwable t) {
                Log.e("BaseFragmentActivity", "Not a valid header line: "
                        + line, t);
            }
        }
        return headers.toArray(new Header[headers.size()]);
    }

    @Override
    public HttpEntity getRequestEntity(String entity) {
        if (isRequestBodyAllowed() && entity != null) {
            MyApplication.get().logUtil.d("request body:" + entity.length());
            MyApplication.get().logUtil.d("request body:" + entity);
            try {
                return new StringEntity(entity);
            } catch (UnsupportedEncodingException e) {
                Log.e("BaseFragmentActivity", "cannot create String entity", e);
            }
        }
        return null;
    }

    @Override
    public HttpEntity getRequestEntity(byte[] entity) {
        if (isRequestBodyAllowed() && entity != null) {
            MyApplication.get().logUtil.d("request body:" + entity.length);
            MyApplication.get().logUtil.d("request body:" + entity);
            return new ByteArrayEntity(entity);
        }
        return null;
    }

    @Override
    public void debugHeaders(String TAG, Header[] headers) {
        if (headers != null) {
            Log.d(TAG, "Request Headers:");
            StringBuilder builder = new StringBuilder();
            for (Header h : headers) {
                String _h = String.format(Locale.US, "%s : %s", h.getName(), h
                        .getValue());
                Log.d(TAG, _h);
                builder.append(_h);
                builder.append("\n");
            }
        }
    }

    @Override
    public String throwableToString(Throwable t) {
        if (t == null) return null;
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    @Override
    public AsyncHttpClient getAsyncHttpClient() {
        return AsyncHttpClientManager.get().getAsyncHttpClient();
    }

    @Override
    public ImageLoader getImageLoader() {
        return MyApplication.get().getImageLoader();
    }

    public void LaunchProtocol(XmlRequest req,
                               AsyncHttpResponseHandler responseHandler, int id) {
        hideInputMethod();
        buildProgressDialog(id);
        try {
            execute(req.getRequestUrl(), getRequestHeaders(req.getHeaders()), getRequestEntity(req
                    .getRequestXMLData()), responseHandler == null ? getResponseHandler(req) : responseHandler);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            cancleProgressDialog();
        } catch (EncodeMessageException e) {
            e.printStackTrace();
            cancleProgressDialog();
        }
    }

    public void LaunchProtocol(XmlRequest req) {
        LaunchProtocol(req, null, R.string.loading_message);
    }

    public void LaunchProtocol(XmlRequest req,
                               AsyncHttpResponseHandler responseHandler) {
        LaunchProtocol(req, responseHandler, R.string.loading_message);
    }

    public void LaunchProtocol(XmlRequest req, int id) {
        LaunchProtocol(req, null, id);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void execute(String URL, Header[] headers, HttpEntity entity,
                        AsyncHttpResponseHandler responseHandler) {
        if (HttpUtils.isConnect(this)) {
            MyApplication.get().getLogUtil().d(URL);
            debugHeaders(getPackageName(), headers);
            getAsyncHttpClient()
                    .post(this, URL, headers, entity, null, responseHandler);
            getAsyncHttpClient()
                    .post(getApplicationContext(), URL, headers, entity, "", responseHandler);
        } else {
            cancleProgressDialog();
            ((AsyncResponesHandler) responseHandler)
                    .onException(new HttpHostConnectException(new HttpHost(URL), new ConnectException(getString(R.string.no_internet))));
        }
    }

    @Override
    public String getDefaultURL() {
        return _Config.get().getServer_Url();
    }

    @Deprecated
    @Override
    public AsyncHttpResponseHandler getResponseHandler() {
        return new AsyncResponesHandler();
    }

    @Override
    public AsyncHttpResponseHandler getResponseHandler(final _IProtocol protocol) {
        return new AsyncResponesHandler(protocol);
    }

    @Override
    public AsyncHttpResponseHandler getResponseHandler(Class<?> c) {
        return new AsyncResponesHandler(c);
    }

    @Override
    public boolean isRequestBodyAllowed() {
        return true;
    }

    @Override
    public boolean isRequestHeadersAllowed() {
        return false;
    }

    @Override
    public void delete(String url, AsyncHttpResponseHandler responseHandler) {
        getAsyncHttpClient().delete(url, responseHandler);
    }

    @Override
    public void get(String url, AsyncHttpResponseHandler responseHandler) {
        getAsyncHttpClient().get(url, responseHandler);
    }

    public void response(_IProtocol protocol, String content) throws Exception {
    }

    public void response(String content, Class<?> protocol) throws Exception {
    }

    public void responseByException(_IProtocol protocol, Throwable e) {
        exception(e);
    }

    public void responseByException(Throwable e, Class<?> protocol) {
        exception(e);
    }

    void exception(Throwable e) {
        if (e instanceof SessionTimeoutException) relogin(e);
        else if (e instanceof ServerErrorException) showToast(((ServerErrorException) e)
                .getCauseMsg());
        else if (e instanceof org.apache.http.conn.ConnectTimeoutException)
            showToast(R.string.connect_server_timeout);
        else if (e instanceof org.apache.http.client.ClientProtocolException)
            showToast(R.string.connect_server_error);
        else if (e instanceof org.apache.http.conn.HttpHostConnectException) showToast(HttpUtils
                .isConnect(this) ? R.string.server_not_response : R.string.no_internet);
        else if (e instanceof SocketTimeoutException) showToast(R.string.socket_timeout);
        else if (e instanceof org.apache.http.client.HttpResponseException)
            showToast(R.string.server_not_response);
    }

    @Override
    public void onCancle(int model) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onComplete(int model) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + FileUtils.APK), "application/vnd.android.package-archive");
        startActivity(intent);
        MyApplication.get().finish();
    }

    @Override
    public void onError(Object error) {
        // TODO Auto-generated method stub
    }

    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null
                    && getCurrentFocus().getWindowToken() != null) {
                InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }

    public class AsyncResponesHandler extends AsyncHttpResponseHandler {
        _IProtocol protocol;
        Class<?> c;

        public AsyncResponesHandler() {
            // TODO Auto-generated constructor stub
        }

        public AsyncResponesHandler(_IProtocol protocol) {
            // TODO Auto-generated constructor stub
            this.protocol = protocol;
        }

        public AsyncResponesHandler(Class<?> protocol) {
            // TODO Auto-generated constructor stub
            this.c = protocol;
        }

        @Override
        public void onFailure(int statusCode,
                              cz.msebera.android.httpclient.Header[] headers,
                              byte[] responseBody, Throwable error) {
            dismissProgressDialog();
            MyApplication.get().getLogUtil()
                    .e(statusCode + "-" + new String(responseBody));
            MyApplication.get().getLogUtil().e(error);
            onException(error);
        }

        @Override
        public void onSuccess(int statusCode,
                              cz.msebera.android.httpclient.Header[] headers,
                              byte[] responseBody) {
            dismissProgressDialog();
            try {
                onParse(new String(responseBody));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                MyApplication.get().logUtil.w(e);
                onException(e);
            }
        }

        public void onParse(String content) throws Exception {
            if (protocol != null) response(protocol, content);
            if (c != null) response(content, c);
        }

        public void onException(Throwable e) {
            if (protocol != null) responseByException(protocol, e);
            if (c != null) responseByException(e, c);
        }
    }
}
