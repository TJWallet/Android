package com.tianji.blockchain.activity.smartcontract;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.tianji.blockchain.R;
import com.tianji.blockchain.activity.basic.BasicMvpActivity;
import com.tianji.blockchain.activity.dapplist.DappListPresenter;
import com.tianji.blockchain.adapter.basic.ItemClickableAdapter;
import com.tianji.blockchain.adapter.recyclerView.DappSmartContractListAdapter;
import com.tianji.blockchain.entity.SmartContractEntity;

import java.util.ArrayList;
import java.util.List;

public class SmartContractListActivity extends BasicMvpActivity implements View.OnClickListener {
    private ImageButton ivBack;
    private RecyclerView rvListItems;

    private List<SmartContractEntity> listData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle extras = this.getIntent().getExtras();
        Object data = extras.getSerializable("data");
        List<SmartContractEntity> temp = (ArrayList<SmartContractEntity>) data;
        if (temp != null && temp.size() > 0) {
            this.listData.addAll(temp);
        }
        this.rvListItems.getAdapter().notifyDataSetChanged();
    }

    @Override
    protected int setResourceId() {
        return R.layout.activity_smart_contract_list;
    }

    @Override
    protected void handleMsg(Message msg) {

    }

    @Override
    protected void setData() {
        if (presenter == null) {
            presenter = new DappListPresenter(this.getApplicationContext(), this);
        }
    }

    @Override
    protected void initReceiver() {

    }

    @Override
    protected void initView() {
        this.ivBack = findViewById(R.id.ivBack);
        this.rvListItems = findViewById(R.id.rvListItems);

        this.rvListItems.setLayoutManager(new LinearLayoutManager(this));
        DappSmartContractListAdapter adapter = new DappSmartContractListAdapter(this, this.listData);
        this.rvListItems.setAdapter(adapter);
        adapter.setItemClickListener(new ItemClickableAdapter.OnItemClickListener() {
            @Override
            public void onClickItem(View itemView, Object itemData, int position) {
                // TODO 跳转到区块链浏览器
            }
        });

        this.ivBack.setOnClickListener(this);
    }

    @Override
    public void getDataSuccess(Object object, int type) {

    }

    @Override
    public void getDataFail(Object error, int type) {

    }

    @Override
    public void getDataFail(int errCode, String errMsg, int type) {

    }

    @Override
    public void onClick(View v) {
        int resourceID = v.getId();
        switch(resourceID) {
            case R.id.ivBack:
                this.finish();
                break;
        }
    }
}