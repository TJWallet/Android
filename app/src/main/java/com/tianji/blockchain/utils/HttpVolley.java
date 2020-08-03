package com.tianji.blockchain.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HttpVolley {
    private RequestQueue requestQueue;

    private static volatile HttpVolley instance = null;

    public interface VolleyCallBack {
        void onSuccess(String data);

        void onFail(VolleyError error);
    }

    //运行时加载对象
    public static HttpVolley getInstance(Context context) {
        if (instance == null) {
            synchronized (HttpVolley.class) {
                if (instance == null) {
                    instance = new HttpVolley(context);
                }
            }
        }
        return instance;
    }

    private HttpVolley(Context context) {
        // Instantiate the cache
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);

        // Start the queue
        requestQueue.start();
    }

    //网络判断
    public boolean isNet(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (networkInfo != null) {
                return networkInfo.isAvailable();
            }
        }
        return false;
    }

    public void HttpVolleyGet(String url, final VolleyCallBack callBack) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callBack.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.onFail(error);
            }
        });
        //设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        stringRequest.setTag("textPost");
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(15 * 1000, 3, 1.0f));
        //将请求加入全局队列中
        requestQueue.add(stringRequest);
    }

    //post请求
    public void HttpVolleyPost(String url, final Map<String, String> params, final VolleyCallBack callBack) {
        LogUtils.d("HttpVolleyPost");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                LogUtils.i("请求成功，data = " + response);
                callBack.onSuccess(response);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.logError("请求失败，data = " + error.toString());
                callBack.onFail(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        stringRequest.setTag("textPost");

        /**DefaultRetryPolicy(int,int,float);中第一个代表超时时间：即超过20S认为超时，第三个参数代表最大重试次数，这里设置为1.0f代表如果超时，则不重试*/
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(15 * 1000, 3, 1.0f));
        requestQueue.add(stringRequest);
//        LogUtils.d("HttpVolleyPost add");
    }

    //post请求
    public void HttpVolleyPostBody(String url, final Map<String, String> params, final VolleyCallBack callBack) {
        LogUtils.d("HttpVolleyPostBody");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtils.i("请求成功，data = " + response);
                callBack.onSuccess(response);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.e("请求失败，data = " + error.toString());
                callBack.onFail(error);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("Content-Type", "application/json");
                return map;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    JSONObject rootObj = new JSONObject();
                    rootObj.put("addressHash", params.get("addressHash"));
                    rootObj.put("current", Integer.parseInt(params.get("current")));
                    rootObj.put("size", Integer.parseInt(params.get("size")));

                    String bodyString = rootObj.toString();
                    LogUtils.log(" -- POST BODY 请求参数 == " + bodyString);
                    return bodyString.getBytes();
                } catch (JSONException e) {
                    e.printStackTrace();
                    return super.getBody();
                }
            }
        };
        stringRequest.setTag("textPost");
        /**DefaultRetryPolicy(int,int,float);中第一个代表超时时间：即超过20S认为超时，第三个参数代表最大重试次数，这里设置为1.0f代表如果超时，则不重试*/
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(15 * 1000, 3, 1.0f));
        requestQueue.add(stringRequest);
    }
}
