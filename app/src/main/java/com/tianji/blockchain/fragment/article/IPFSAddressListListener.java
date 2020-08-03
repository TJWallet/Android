package com.tianji.blockchain.fragment.article;

import com.android.volley.VolleyError;
import com.tianji.blockchain.entity.ErrEntity;
import com.tianji.blockchain.entity.IpfsItemEntity;

import java.util.List;

public interface IPFSAddressListListener {
    void requestStart();

    void requestSuccess(List<IpfsItemEntity> ipfsItemEntityList);

    void requestFail(ErrEntity errEntity);

    void requestFail(VolleyError error);
}
