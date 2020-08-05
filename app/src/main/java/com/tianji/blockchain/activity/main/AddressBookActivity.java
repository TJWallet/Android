package com.tianji.blockchain.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tianji.blockchainwallet.entity.WalletInfo;
import com.tianji.blockchain.Constant;
import com.tianji.blockchain.R;
import com.tianji.blockchain.activity.basic.BasicActionBarActivity;
import com.tianji.blockchain.activity.basic.BasicConnectShowActivity;
import com.tianji.blockchain.activity.transfer.TransferActivity;
import com.tianji.blockchain.adapter.basic.OnItemClickListener;
import com.tianji.blockchain.adapter.recyclerView.RVAdapterAddressBooks;
import com.tianji.blockchain.entity.AddressEntity;
import com.tianji.blockchain.entity.AssetsDetailsItemEntity;
import com.tianji.blockchain.sharepreferences.AddressSharedPreferences;
import com.tianji.blockchain.utils.LogUtils;
import com.tianji.blockchain.utils.ViewCommonUtils;

import java.util.ArrayList;
import java.util.List;

public class AddressBookActivity extends BasicConnectShowActivity {
    private RecyclerView recyclerView;
    private List<AddressEntity> addressEntityList = new ArrayList<>();
    private RVAdapterAddressBooks adapter;
    private int startType;
    private RelativeLayout rl_no_assets;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_book);
    }


    @Override
    protected void onStart() {
        super.onStart();
        addressEntityList.clear();
        addressEntityList.addAll(AddressSharedPreferences.getInstance(this).getAddressList());
        if (addressEntityList.size() > 0) {
            rl_no_assets.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            if (adapter == null) {
                adapter = new RVAdapterAddressBooks(this, addressEntityList);
                adapter.setItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position, Object obj) {
                        AddressEntity entity = (AddressEntity) obj;
                        if (startType == Constant.AddressBookMark.TYPE_MAIN) {
                            Intent intent = new Intent(AddressBookActivity.this, AddressDetailsActivity.class);
                            intent.putExtra("_position", position);
                            startActivity(intent);
                        } else if (startType == Constant.AddressBookMark.TYPE_TRANSFER) {
                            Intent resultIntent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putString("addressResult", entity.getAddress());
                            resultIntent.putExtras(bundle);
                            AddressBookActivity.this.setResult(0x20, resultIntent);
                            AddressBookActivity.this.finish();
                        }

                    }
                });
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(adapter);
            } else {
                adapter.updateData(addressEntityList);
            }

        } else {
            rl_no_assets.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }


    }

    @Override
    protected void initView() {
        startType = getIntent().getIntExtra("_address_book_mark", -1);
        mActionBar.setVisibility(View.VISIBLE);
        mActionBar.setActionTitle(getResources().getString(R.string.address_book));
        ViewCommonUtils.buildBackImageView(this, mActionBar);
        ImageView img_add = ViewCommonUtils.buildImageView(this, R.drawable.add_light);
        img_add.setScaleType(ImageView.ScaleType.CENTER);
        mActionBar.addViewToRight(img_add, 1);
        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddressBookActivity.this, EditAddressActivity.class);
                intent.putExtra("_addressType", Constant.AddressType.TYPE_ADDRESS_CREATE);
                startActivity(intent);
            }
        });
        recyclerView = findViewById(R.id.recyclerView);
        rl_no_assets = findViewById(R.id.rl_no_assets);
    }
}
