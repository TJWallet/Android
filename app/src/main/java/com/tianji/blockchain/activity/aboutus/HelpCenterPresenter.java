package com.tianji.blockchain.activity.aboutus;

import android.content.Context;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.tianji.blockchain.Constant;
import com.tianji.blockchain.basic.BasicMvpInterface;
import com.tianji.blockchain.basic.BasicPresenter;
import com.tianji.blockchain.entity.QuestionColumnEntity;
import com.tianji.blockchain.entity.QuestionListEntity;
import com.tianji.blockchain.utils.HttpVolley;
import com.tianji.blockchain.utils.LogUtils;

import java.util.List;
import java.util.Map;

public class HelpCenterPresenter extends BasicPresenter {
    public static final int TYPE_COLUMU = 0;
    public static final int TYPE_QUESTION_LIST = 1;
    public static final int TYPE_SEARCH_QUESTION_LIST = 2;

    public HelpCenterPresenter(Context context, BasicMvpInterface basicMvpInterface) {
        super(context, basicMvpInterface);
    }

    @Override
    public void getData(Map<String, String> params) {

    }

    /**
     * 请求栏目列表
     */
    public void getColumnList() {
        HttpVolley.getInstance(context).HttpVolleyGet(Constant.HttpUrl.getQuestionColunmn, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                LogUtils.log("请求栏目列表成功" + data);
                List<QuestionColumnEntity> list = gson.fromJson(data, new TypeToken<List<QuestionColumnEntity>>() {
                }.getType());
                basicMvpInterface.getDataSuccess(list, TYPE_COLUMU);
            }

            @Override
            public void onFail(VolleyError error) {
                LogUtils.log("请求栏目列表失败" + error);
            }
        });
    }

    public void getQuestionList(Map<String, String> params) {
        String url = Constant.HttpUrl.getQuestionList + "columnId=" + params.get("columnId") + "&page=" + params.get("page") + "&size=" + params.get("pageSize");
        LogUtils.log("问题列表请求参数" + url);
        HttpVolley.getInstance(context).HttpVolleyGet(url, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                QuestionListEntity entity = gson.fromJson(data, QuestionListEntity.class);
                LogUtils.log("请求问题列表成功" + data);
                basicMvpInterface.getDataSuccess(entity, TYPE_QUESTION_LIST);
            }

            @Override
            public void onFail(VolleyError error) {
                LogUtils.log("请求问题列表失败" + error);
                basicMvpInterface.getDataFail(error, TYPE_QUESTION_LIST);
            }
        });
    }


    public void searchQuestion(String content) {
        String url = Constant.HttpUrl.getQuestionList + "title=" + content;
        HttpVolley.getInstance(context).HttpVolleyGet(url, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                QuestionListEntity entity = gson.fromJson(data, QuestionListEntity.class);
                basicMvpInterface.getDataSuccess(entity, TYPE_SEARCH_QUESTION_LIST);
            }

            @Override
            public void onFail(VolleyError error) {
                LogUtils.logError("搜索问题列表失败" + error);
                basicMvpInterface.getDataFail(error, TYPE_SEARCH_QUESTION_LIST);
            }
        });
    }
}
