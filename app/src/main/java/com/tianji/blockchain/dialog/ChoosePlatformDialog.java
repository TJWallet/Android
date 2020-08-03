package com.tianji.blockchain.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.VolleyError;
import com.tianji.blockchain.R;
import com.tianji.blockchain.adapter.basic.ItemClickableAdapter;
import com.tianji.blockchain.adapter.recyclerView.DappPlatformListAdapter;
import com.tianji.blockchain.entity.DappPlatformEntity;
import com.tianji.blockchain.restful.Api;
import com.tianji.blockchain.restful.bean.PlatformBean;
import com.tianji.blockchain.restful.bean.ResponseBean;
import com.tianji.blockchain.utils.HttpVolley;
import com.tianji.blockchain.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class ChoosePlatformDialog extends Dialog {
    private View.OnClickListener listener;

    private Context context;
    private ImageView ivClose;
    private RecyclerView rvPlatformList;
    private Button btnConfirm;
    private LinearLayout layoutLoading;

    private List<DappPlatformEntity> platformList = new ArrayList<>();
    private DappPlatformEntity currentPlatform;
    private View currentItemView;

    public ChoosePlatformDialog(@NonNull Context context, int themeResId, View.OnClickListener onClickConfirmListener) {
        super(context, themeResId);
        this.context = context;
        this.listener = onClickConfirmListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_choose_platform);
        initView();
        this.fetchPlatformList();
    }

    public DappPlatformEntity getCurrentPlatform() {
        return this.currentPlatform;
    }

    private void initView() {
        this.ivClose = this.findViewById(R.id.ivClose);
        this.rvPlatformList = this.findViewById(R.id.rvPlatformList);
        this.btnConfirm = this.findViewById(R.id.btnConfirm);
        this.layoutLoading = this.findViewById(R.id.layoutLoading);

        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.y = 0;
        window.setAttributes(layoutParams);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnConfirm.setOnClickListener(listener);
        this.rvPlatformList.setLayoutManager(new LinearLayoutManager(getContext()));
        DappPlatformListAdapter platformAdapter = new DappPlatformListAdapter(getContext(), this.platformList);
        platformAdapter.setItemClickListener(new ItemClickableAdapter.OnItemClickListener(){
            @Override
            public void onClickItem(View itemView, Object itemData, int position) {
                if (currentPlatform != null) currentPlatform.setSelected(false);
                if (currentItemView != null) ((ImageView)currentItemView.findViewById(R.id.ivChecked)).setVisibility(View.INVISIBLE);

                currentPlatform = (DappPlatformEntity) itemData;
                currentItemView = itemView;
                ((ImageView)currentItemView.findViewById(R.id.ivChecked)).setVisibility(View.VISIBLE);
            }
        });
        this.rvPlatformList.setAdapter(platformAdapter);
    }

    private void fetchPlatformList() {
        Api.getInstance().fetchPlatformList(new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                layoutLoading.setVisibility(View.GONE);
                ResponseBean<List<PlatformBean>> response = JSON.parseObject(data, new TypeReference<ResponseBean<List<PlatformBean>>>(){});
                if(response.getStatus() == 1) {
                    List<PlatformBean> result = response.getResult();
                    if (result != null && result.size() > 0) {
                        List<DappPlatformEntity> temp = new ArrayList<>();
                        for(PlatformBean item: result) {
                            temp.add(item.transfer());
                        }
                        platformList.addAll(temp);
                        rvPlatformList.getAdapter().notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(getContext(), "获取数据失败啦，请稍后再试", Toast.LENGTH_SHORT).show();
                    LogUtils.e(response.getErrMsg());
                }
            }

            @Override
            public void onFail(VolleyError error) {
                layoutLoading.setVisibility(View.GONE);
                Toast.makeText(getContext(), "获取数据失败，请检查网络连接", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
