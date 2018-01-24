package com.caishi.zhanghai.im.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


/**
 * Created by shihui on 2017/5/11.
 */

public class ReqSSl {

    private static final String TAG = "ReqSll";
    private static final String TIMEOUT_STR = "Timeout";
    public static ReqSSl reqSSl;


    public static ReqSSl getInstance() {
        if (reqSSl == null) {
            synchronized (ReqSSl.class) {
                if (reqSSl == null) {
                    reqSSl = new ReqSSl();
                    return reqSSl;
                }
            }
        }
        return reqSSl;
    }

    public ReqSSl() {
    }


    /**
     * SDK调后台接口请求   POST 请求
     *
     * @param context
     * @param url
     * @param params
     * @param listener
     */
//    ReqListener listener;
    public void requestPost(final Context context, final String url, final HashMap<String, Object> params, final ReqListener listener) {
        ConnectivityManager connectivityManager =  (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getActiveNetworkInfo() != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String response = doPostRequestSSL(url, params);
                    if (response.equals(ReqSSl.TIMEOUT_STR)) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "连接超时", Toast.LENGTH_LONG).show();
                            }
                        }).start();


                    } else {
                        if (null != response && !TextUtils.isEmpty(response)) {
                            listener.success(response);

                        } else {
                            listener.failed();
                        }
                    }
                }
            }).start();
        } else {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    Looper.prepare();
                    Toast.makeText(context,"网络请求失败，请检查您的网络设置", Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }).start();

            listener.failed();
        }
    }




    private String doPostRequestSSL(String url, HashMap<String, Object> params) {
        String response = "";
        DataOutputStream out = null;
        HttpsURLConnection conns = null;
        HttpURLConnection conn = null;
        BufferedReader reader = null;

        try {
            URL urls = new URL(url);

            if (url.contains("https")) {//HTTPS网络请求处理

                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, new TrustManager[]{new TrustAllManager()}, new java.security.SecureRandom());
                // Open a HTTP connection to the URL
                conns = (HttpsURLConnection) urls.openConnection();
                conns.setReadTimeout(20 * 1000);
                conns.setDoOutput(true);
                conns.setDoInput(true);
                conns.setRequestMethod("POST");
                conns.setUseCaches(false);
                conns.setInstanceFollowRedirects(true);
//                conns.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                // skip SSL validate
                conns.setSSLSocketFactory(sc.getSocketFactory());
                conns.setHostnameVerifier(new TrustAnyHostnameVerifier());
                conns.connect();
                out = new DataOutputStream(conns.getOutputStream());
            } else {//HTTP网络请求处理
                conn = (HttpURLConnection) urls.openConnection();
                conn.setReadTimeout(20 * 1000);
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");
                conn.setUseCaches(false);
                conn.setInstanceFollowRedirects(true);
                conn.connect();
                out = new DataOutputStream(conn.getOutputStream());
            }

            //添加要上传的参数

            if (params == null) {
                params = new HashMap<String, Object>();
            }
            Set<String> key = params.keySet();
            StringBuffer sb = new StringBuffer();
            Iterator<String> iterator = key.iterator();
            while (iterator.hasNext()) {
                String temp = iterator.next();
                sb.append(temp);
                sb.append("=");
                sb.append(URLEncoder.encode((String) params.get(temp), "UTF-8"));
                if (iterator.hasNext()) {
                    sb.append("&");
                }
            }

            Log.d(TAG, url + "?" + sb.toString());
            //将要上传的内容写入流中
//            out.writeBytes(sb.toString());
            out.write(sb.toString().getBytes("UTF-8"));
            //刷新、关闭
            out.flush();
            out.close();
            //获取数据
            if (url.contains("https")) {//HTTPS网络请求处理
                reader = new BufferedReader(new InputStreamReader(conns.getInputStream()));
            }else {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            }


            String inputLine = null;
            //使用循环来读取获得的数据
            while (((inputLine = reader.readLine()) != null)) {
                //我们在每一行后面加上一个"\n"来换行
                response += inputLine;
            }
            reader.close();
            //关闭http连接

        } catch (SocketTimeoutException e) {
            response = TIMEOUT_STR;
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != conn)
                conn.disconnect();
            if (null != conns) {
                conns.disconnect();
            }
        }

//        Log.d(TAG, response);
        return response;
    }






    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    private class TrustAllManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }


    public  interface ReqListener {
        void success(String response);
        void failed();
    }
}

