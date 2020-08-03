package com.tianji.blockchain.activity.aboutus;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;

import com.android.volley.VolleyError;
import com.tianji.blockchain.R;
import com.tianji.blockchain.activity.basic.BasicActionBarActivity;
import com.tianji.blockchain.activity.basic.BasicConnectShowActivity;
import com.tianji.blockchain.adapter.basic.OnItemClickListener;
import com.tianji.blockchain.adapter.recyclerView.RVAdapterQuestionColumn;
import com.tianji.blockchain.basic.BasicMvpInterface;
import com.tianji.blockchain.entity.QuestionColumnEntity;
import com.tianji.blockchain.entity.QuestionListEntity;
import com.tianji.blockchain.utils.LogUtils;
import com.tianji.blockchain.utils.ViewCommonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelpCenterActivity extends BasicConnectShowActivity implements BasicMvpInterface {
    private EditText edt_question;
    private RelativeLayout rl_search;
    private RecyclerView recyclerView;
    private HelpCenterPresenter presenter;

    private RVAdapterQuestionColumn adapter;

    private SwipeRefreshLayout swipeRefreshLayout;

    List<QuestionColumnEntity> columnList = new ArrayList<>();

    private ListPopupWindow lpw;
    private ArrayList<String> searchList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_center);
    }

    @Override
    protected void setData() {
        if (presenter == null) {
            presenter = new HelpCenterPresenter(this, this);
        }
        presenter.getColumnList();
    }

    private String content;

    @Override
    protected void handleMsg(Message msg) {
        switch (msg.what) {
            case 0:
                if (content.equals("")) {
                    if (lpw != null) {
                        if (lpw.isShowing()) {
                            lpw.dismiss();
                        }
                    }
                } else {
                    if (presenter == null) {
                        presenter = new HelpCenterPresenter(HelpCenterActivity.this, HelpCenterActivity.this);
                    }
                    presenter.searchQuestion(content);
                }
                break;
        }
    }

    @Override
    protected void initReceiver() {

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void initView() {
        mActionBar.setVisibility(View.VISIBLE);
        mActionBar.setActionTitle(getResources().getString(R.string.help_center));
        ViewCommonUtils.buildBackImageView(this, mActionBar);

        edt_question = findViewById(R.id.edt_question);
        rl_search = findViewById(R.id.rl_search);
        recyclerView = findViewById(R.id.recyclerView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setEnabled(false);

        if (!swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(true);
        }

        edt_question.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (lpw != null) {
                    if (lpw.isShowing()) {
                        lpw.dismiss();
                    }
                }
                mHandler.removeMessages(0);
                content = s.toString();
                mHandler.sendEmptyMessageDelayed(0, 1000);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    int requestNum = 0;

    @Override
    public void getDataSuccess(Object object, int type) {
        if (type == HelpCenterPresenter.TYPE_COLUMU) {
            requestNum = 0;
            List<QuestionColumnEntity> list = (List<QuestionColumnEntity>) object;
            columnList.clear();
            columnList.addAll(list);
            LogUtils.log("获取到栏目列表数据" + columnList.size());
            for (int i = 0; i < columnList.size(); i++) {
                if (presenter == null) {
                    presenter = new HelpCenterPresenter(this, this);
                }
                Map<String, String> params = new HashMap<>();
                params.put("columnId", columnList.get(i).getId() + "");
                params.put("page", "0");
                params.put("pageSize", "50");
                presenter.getQuestionList(params);
            }
        } else if (type == HelpCenterPresenter.TYPE_QUESTION_LIST) {
            requestNum += 1;
            QuestionListEntity entity = (QuestionListEntity) object;
            for (int i = 0; i < columnList.size(); i++) {
                if (entity.getContent().size() > 0) {
                    if (entity.getContent().get(0).getColumnId() == columnList.get(i).getId()) {
                        columnList.get(i).setQuestionListEntity(entity);
                    }
                }
            }
            if (requestNum == columnList.size()) {
                adapter = new RVAdapterQuestionColumn(this, columnList);
                adapter.setItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position, Object obj) {
                        QuestionListEntity entity = (QuestionListEntity) obj;
                        Intent intent = new Intent(HelpCenterActivity.this, HelpMoreActivity.class);
                        intent.putExtra("_questionListEntity", entity);
                        intent.putExtra("_columnName", columnList.get(position).getTitle());
                        startActivity(intent);
                    }
                });
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(adapter);
            }
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
        } else if (type == HelpCenterPresenter.TYPE_SEARCH_QUESTION_LIST) {
            final QuestionListEntity entity = (QuestionListEntity) object;
            searchList.clear();
            if (entity.getContent().size() > 0) {
                for (int i = 0; i < entity.getContent().size(); i++) {
                    searchList.add(entity.getContent().get(i).getTitle());
                }
                if (lpw == null) {
                    lpw = new ListPopupWindow(this);
                    lpw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(HelpCenterActivity.this, HelpDetailsActivity.class);
                            intent.putExtra("_questionItemEntity", entity.getContent().get(position));
                            startActivity(intent);
                        }
                    });
                }
                lpw.setAdapter(new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, searchList));
                lpw.setAnchorView(edt_question);
                lpw.setModal(true);
                lpw.show();
                edt_question.setFocusable(true);
            }
        }


    }

    @Override
    public void getDataFail(Object error, int type) {

    }

    @Override
    public void getDataFail(int errCode, String errMsg, int type) {

    }

}
