package com.tianji.blockchain.restful;

import android.content.Context;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;
import com.tianji.blockchainwallet.constant.enums.ResultCode;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.restful.cache.DiskLruCacheController;
import com.tianji.blockchain.restful.cache.OnLoadDiskLruCacheListener;
import com.tianji.blockchain.utils.HttpVolley;
import com.tianji.blockchain.utils.LogUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * author:jason
 **/
public class Api {
    private HttpVolley http;
    private String baseUrl = RestfulConfig.BASE_URL;
    private String coverBaseUrl;
    private DiskLruCacheController cacheController;

    /**
     * 单例模式
     **/
    private static Api instance;

    private Api() {
        this.cacheController = DiskLruCacheController.getInstance(WalletApplication.getApp().getApplicationContext());
    }

    public static Api getInstance() {
        if (Api.instance == null) {
            Api.instance = new Api();
        }
        return Api.instance;
    }

    private enum Method {
        GET, POST
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        this.http = HttpVolley.getInstance(context.getApplicationContext());
    }

    /**
     * 单次覆盖BASE_URL 使用方式如：Api.getInstance().cover("https://api.dapponline.io/").login("login", params, callback);
     *
     * @param coverBaseUrl
     * @return
     */
    public Api cover(String coverBaseUrl) {
        this.coverBaseUrl = coverBaseUrl;
        return Api.instance;
    }

    /**
     * 从缓存读取
     *
     * @param method
     * @param path
     * @param params
     * @param callback
     */
    private void readFromCache(Method method, @NonNull String path, Map<String, String> params, @NonNull HttpVolley.VolleyCallBack callback) {
        if (params == null) {
            params = new HashMap<>();
        }

        final Map<String, String> paramsRef = params;

        final String url = (this.coverBaseUrl != null ? this.coverBaseUrl : this.baseUrl) + path;
        String cacheKey = this.cacheController.createKey(url, params);

        this.cacheController.getCacheString(cacheKey, new OnLoadDiskLruCacheListener() {
            @Override
            public void load(ResultCode resultCode, String result) {
                if (ResultCode.SUCCESS == resultCode) {
                    callback.onSuccess(result);
                } else {
//                    callback.onFail(null);
                }
            }
        });
        this.coverBaseUrl = null;
    }

    /**
     * base post
     *
     * @param path
     * @param params
     * @param callback
     * @throws Exception
     */
    private void basePost(@NonNull String path, Map<String, String> params, @NonNull HttpVolley.VolleyCallBack callback) {
        if (http == null) {
            callback.onFail(new VolleyError("please call init first"));
        }

        String url = (this.coverBaseUrl != null ? this.coverBaseUrl : this.baseUrl) + path;
        String cacheKey = this.cacheController.createKey(url, params);
        this.http.HttpVolleyPost(url, params, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                callback.onSuccess(data);
                cacheController.saveCache(cacheKey, data);
            }

            @Override
            public void onFail(VolleyError error) {
                callback.onFail(error);
            }
        });
        this.coverBaseUrl = null;
    }

    /**
     * base get
     *
     * @param path
     * @param callback
     * @throws Exception
     */
    private void baseGet(@NonNull String path, @NonNull HttpVolley.VolleyCallBack callback) {
        if (http == null) {
            callback.onFail(new VolleyError("please call init first"));
        }

        String url = (this.coverBaseUrl != null ? this.coverBaseUrl : this.baseUrl) + path;
        String cacheKey = this.cacheController.createKey(url, new HashMap<>());
        this.http.HttpVolleyGet(url, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                callback.onSuccess(data);
                cacheController.saveCache(cacheKey, data);
            }

            @Override
            public void onFail(VolleyError error) {
                callback.onFail(error);
            }
        });
        this.coverBaseUrl = null;
    }

    /**
     * 获取平台列表（ETH/TRON...）
     *
     * @param callback
     * @throws Exception
     */
    public void fetchPlatformList(HttpVolley.VolleyCallBack callback) {
        Map<String, String> params = new HashMap<>();
        params.put("responseType", "30");

        this.basePost("wit", params, callback);
    }

    /**
     * 获取平台列表（ETH/TRON...）
     *
     * @param fromCache
     * @param callback
     */
    public void fetchPlatformList(boolean fromCache, HttpVolley.VolleyCallBack callback) {
        Map<String, String> params = new HashMap<>();
        params.put("responseType", "30");

        if (fromCache) {
            this.readFromCache(Method.POST, "wit", params, callback);
        } else {
            this.basePost("wit", params, callback);
        }
    }


    /**
     * 获取对应位置广告列表 （广告位置：16->钱包发现页广告）
     *
     * @param position
     * @param callback
     */
    public void fetchBannerList(int position, HttpVolley.VolleyCallBack callback) {
        Map<String, String> params = new HashMap<>();
        params.put("responseType", "16");
        params.put("position", "" + position);

        this.basePost("wit", params, callback);
    }

    /**
     * 获取对应位置广告列表 （广告位置：16->钱包发现页广告）
     *
     * @param fromCache
     * @param position
     * @param callback
     */
    public void fetchBannerList(boolean fromCache, int position, HttpVolley.VolleyCallBack callback) {
        Map<String, String> params = new HashMap<>();
        params.put("responseType", "16");
        params.put("position", "" + position);

        if (fromCache) {
            this.readFromCache(Method.POST, "wit", params, callback);
        } else {
            this.basePost("wit", params, callback);
        }
    }

    /**
     * 获取DAPP榜单列表
     *
     * @param platformID
     * @param pageIndex
     * @param pageSize
     * @param callback
     * @throws Exception
     */
    public void fetchDappRanking(String platformID, int pageIndex, int pageSize, HttpVolley.VolleyCallBack callback) {
        Map<String, String> params = new HashMap<>();
        params.put("responseType", "18");
        params.put("chain_type", platformID);
        params.put("typeId", "0");
        params.put("order_field", "-daily_life");
        params.put("game_status", "0");
        params.put("isAll", "-1".equals(platformID) ? "1" : "0");
        params.put("page", "" + pageIndex);
        params.put("pre_page", "" + pageSize);

        this.basePost("wit", params, callback);
    }

    /**
     * 获取DAPP榜单列表
     *
     * @param fromCache
     * @param platformID
     * @param pageIndex
     * @param pageSize
     * @param callback
     */
    public void fetchDappRanking(boolean fromCache, String platformID, int pageIndex, int pageSize, HttpVolley.VolleyCallBack callback) {
        Map<String, String> params = new HashMap<>();
        params.put("responseType", "18");
        params.put("chain_type", platformID);
        params.put("typeId", "0");
        params.put("order_field", "-daily_life");
        params.put("game_status", "0");
        params.put("isAll", "-1".equals(platformID) ? "1" : "0");
        params.put("page", "" + pageIndex);
        params.put("pre_page", "" + pageSize);

        if (fromCache) {
            this.readFromCache(Method.POST, "wit", params, callback);
        } else {
            this.basePost("wit", params, callback);
        }
    }

    /**
     * 获取专题列表
     *
     * @param pageIndex
     * @param pageSize
     * @param callback
     * @throws Exception
     */
    public void fetchTopicList(int pageIndex, int pageSize, HttpVolley.VolleyCallBack callback) {
        Map<String, String> params = new HashMap<>();
        params.put("responseType", "21");
        params.put("page", "" + pageIndex);
        params.put("pre_page", "" + pageSize);
        this.basePost("wit", params, callback);
    }

    /**
     * 获取专题列表
     *
     * @param fromCache
     * @param pageIndex
     * @param pageSize
     * @param callback
     */
    public void fetchTopicList(boolean fromCache, int pageIndex, int pageSize, HttpVolley.VolleyCallBack callback) {
        Map<String, String> params = new HashMap<>();
        params.put("responseType", "21");
        params.put("page", "" + pageIndex);
        params.put("pre_page", "" + pageSize);

        if (fromCache) {
            this.readFromCache(Method.POST, "wit", params, callback);
        } else {
            this.basePost("wit", params, callback);
        }
    }

    /**
     * 获取热门专题
     *
     * @param callback
     */
    public void fetchHotTopic(HttpVolley.VolleyCallBack callback) {
        Map<String, String> params = new HashMap<>();
        params.put("responseType", "21");
        params.put("isHot", "" + 1);
        this.basePost("wit", params, callback);
    }

    /**
     * 获取热门专题
     *
     * @param fromCache
     * @param callback
     */
    public void fetchHotTopic(boolean fromCache, HttpVolley.VolleyCallBack callback) {
        Map<String, String> params = new HashMap<>();
        params.put("responseType", "21");
        params.put("isHot", "" + 1);

        if (fromCache) {
            this.readFromCache(Method.POST, "wit", params, callback);
        } else {
            this.basePost("wit", params, callback);
        }
    }

    /**
     * 搜索DAPP
     *
     * @param keyword
     * @param callback
     */
    public void searchDapp(String keyword, HttpVolley.VolleyCallBack callback) {
        Map<String, String> params = new HashMap<>();
        params.put("responseType", "22");
        params.put("search", keyword);
        params.put("type", "0");
        params.put("page", "1");
        params.put("pre_page", "20");
        this.basePost("wit", params, callback);
    }

    /**
     * 搜索DAPP
     *
     * @param fromCache
     * @param keyword
     * @param callback
     */
    public void searchDapp(boolean fromCache, String keyword, HttpVolley.VolleyCallBack callback) {
        Map<String, String> params = new HashMap<>();
        params.put("responseType", "22");
        params.put("search", keyword);
        params.put("type", "0");
        params.put("page", "1");
        params.put("pre_page", "20");

        if (fromCache) {
            this.readFromCache(Method.POST, "wit", params, callback);
        } else {
            this.basePost("wit", params, callback);
        }
    }

    /**
     * 获取DAPP详情
     *
     * @param mid
     * @param callback
     */
    public void fetchDappDetail(String mid, HttpVolley.VolleyCallBack callback) {
        Map<String, String> params = new HashMap<>();
        params.put("responseType", "19");
        params.put("gameId", mid);
        this.basePost("wit", params, callback);
    }

    /**
     * 获取DAPP详情
     *
     * @param fromCache
     * @param mid
     * @param callback
     */
    public void fetchDappDetail(boolean fromCache, String mid, HttpVolley.VolleyCallBack callback) {
        Map<String, String> params = new HashMap<>();
        params.put("responseType", "19");
        params.put("gameId", mid);

        if (fromCache) {
            this.readFromCache(Method.POST, "wit", params, callback);
        } else {
            this.basePost("wit", params, callback);
        }
    }

    /**
     * 获取DAPP评分统计
     *
     * @param mid
     * @param callback
     */
    public void fetchRatingStatistics(String mid, HttpVolley.VolleyCallBack callback) {
        Map<String, String> params = new HashMap<>();
        params.put("responseType", "45");
        params.put("objectId", mid);
        this.basePost("wit", params, callback);
    }

    /**
     * 获取DAPP评分统计
     *
     * @param fromCache
     * @param mid
     * @param callback
     */
    public void fetchRatingStatistics(boolean fromCache, String mid, HttpVolley.VolleyCallBack callback) {
        Map<String, String> params = new HashMap<>();
        params.put("responseType", "45");
        params.put("objectId", mid);

        if (fromCache) {
            this.readFromCache(Method.POST, "wit", params, callback);
        } else {
            this.basePost("wit", params, callback);
        }
    }

    /**
     * 获取DAPP相关新闻
     *
     * @param mid
     * @param callback
     */
    public void fetchRelevantArticle(String mid, HttpVolley.VolleyCallBack callback) {
        Map<String, String> params = new HashMap<>();
        params.put("responseType", "6");
        params.put("gameId", mid);
        params.put("page", "1");
        params.put("pre_page", "5");
        this.basePost("wit", params, callback);
    }

    /**
     * 获取DAPP相关新闻
     *
     * @param fromCache
     * @param mid
     * @param callback
     */
    public void fetchRelevantArticle(boolean fromCache, String mid, HttpVolley.VolleyCallBack callback) {
        Map<String, String> params = new HashMap<>();
        params.put("responseType", "6");
        params.put("gameId", mid);
        params.put("page", "1");
        params.put("pre_page", "5");

        if (fromCache) {
            this.readFromCache(Method.POST, "wit", params, callback);
        } else {
            this.basePost("wit", params, callback);
        }
    }

    /**
     * 获取推荐DApp
     *
     * @param pageIndex
     * @param pageSize
     * @param callback
     */
    public void fetchRecommend(int pageIndex, int pageSize, HttpVolley.VolleyCallBack callback) {
        Map<String, String> params = new HashMap<>();
        params.put("responseType", "226");
        params.put("type", "0");
        params.put("page", "" + pageIndex);
        params.put("pre_page", "" + pageSize);
        this.basePost("wit2", params, callback);
    }

    /**
     * 获取推荐DApp
     *
     * @param fromCache
     * @param pageIndex
     * @param pageSize
     * @param callback
     */
    public void fetchRecommend(boolean fromCache, int pageIndex, int pageSize, HttpVolley.VolleyCallBack callback) {
        Map<String, String> params = new HashMap<>();
        params.put("responseType", "226");
        params.put("type", "0");
        params.put("page", "" + pageIndex);
        params.put("pre_page", "" + pageSize);

        if (fromCache) {
            this.readFromCache(Method.POST, "wit2", params, callback);
        } else {
            this.basePost("wit2", params, callback);
        }
    }

    /**
     * 获取第三方资讯(暂时弃用)
     *
     * @param startIndex
     * @param pageSize
     * @param isReverse
     * @param callback
     */
    @Deprecated
    public void fetchArticleListFromSomewhere(int startIndex, int pageSize, boolean isReverse, HttpVolley.VolleyCallBack callback) {
        String params = "?limit=" + pageSize + "&id=" + startIndex + "&flag=" + (isReverse ? "up" : "down");
        this.cover("https://api.coindog.com/live/list").baseGet(params, callback);
    }

    /**
     * 获取第三方资讯(暂时弃用)
     *
     * @param fromCache
     * @param startIndex
     * @param pageSize
     * @param isReverse
     * @param callback
     */
    @Deprecated
    public void fetchArticleListFromSomewhere(boolean fromCache, int startIndex, int pageSize, boolean isReverse, HttpVolley.VolleyCallBack callback) {
        String params = "?limit=" + pageSize + "&id=" + startIndex + "&flag=" + (isReverse ? "up" : "down");
        if (fromCache) {
            this.cover("https://api.coindog.com/live/list").readFromCache(Method.GET, params, null, callback);
        } else {
            this.cover("https://api.coindog.com/live/list").baseGet(params, callback);
        }
    }

    /**
     * 获取文章列表
     *
     * @param lastId
     * @param callback
     */
    public void fetchArticleList(String lastId, HttpVolley.VolleyCallBack callback) {
        Map<String, String> params = new HashMap<>();
        params.put("responseType", "227");
        params.put("id", lastId);
        this.basePost("wit2", params, callback);
    }

    /**
     * 获取文章列表
     *
     * @param fromCache
     * @param lastId
     * @param callback
     */
    public void fetchArticleList(boolean fromCache, String lastId, HttpVolley.VolleyCallBack callback) {
        Map<String, String> params = new HashMap<>();
        params.put("responseType", "227");
        params.put("id", lastId);

        if (fromCache) {
            this.readFromCache(Method.POST, "wit2", params, callback);
        } else {
            this.basePost("wit2", params, callback);
        }
    }

    public void getIpfsAddressList(boolean fromCache, String id, HttpVolley.VolleyCallBack callback) {
        Map<String, String> params = new HashMap<>();
        params.put("responseType", "228");
        params.put("object_id", id);

        if (fromCache) {
            this.readFromCache(Method.POST, "wit2", params, callback);
        } else {
            this.basePost("wit2", params, callback);
        }
    }
}
