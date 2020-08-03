package com.tianji.blockchain.sharepreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.tianji.blockchain.entity.AddressEntity;
import com.tianji.blockchain.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class AddressSharedPreferences {
    public static final String ADDRESS_SP_KEY = "address_sp_key";
    private SharedPreferences.Editor mSharedPreferncesEditor;
    private SharedPreferences mSharedPrefernces;
    private Context context;
    /**
     * 记录
     */
    private String record;
    /**
     * 单例模式
     **/
    private static AddressSharedPreferences instance;

    private JSONObject jsonObj;
    private JSONArray jsonArray;

    private AddressSharedPreferences(Context context) {
        this.context = context;
        mSharedPrefernces = context.getSharedPreferences(ADDRESS_SP_KEY, Context.MODE_PRIVATE);
        mSharedPreferncesEditor = mSharedPrefernces.edit();
        record = mSharedPrefernces.getString(ADDRESS_SP_KEY, "");
        jsonObj = new JSONObject();
        jsonArray = new JSONArray();
        try {
            jsonObj.put("result", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static AddressSharedPreferences getInstance(Context context) {
        if (instance == null) {
            instance = new AddressSharedPreferences(context);
        }
        return instance;
    }

    private String getAddressBook() {
        String addressBook = mSharedPrefernces.getString(ADDRESS_SP_KEY, "");
        LogUtils.log("getAddressBook:" + addressBook);
        return addressBook;
    }

    /**
     * 删除地址
     *
     * @param addressEntityList
     */
    public void removeAddress(List<AddressEntity> addressEntityList) {
        jsonObj = new JSONObject();
        jsonArray = new JSONArray();
        if (addressEntityList != null && addressEntityList.size() > 0) {
            try {
                for (int i = 0; i < addressEntityList.size(); i++) {
                    JSONObject obj = new JSONObject();
                    obj.put("chainType", addressEntityList.get(i).getChainType());
                    obj.put("addressName", addressEntityList.get(i).getAddressName());
                    obj.put("address", addressEntityList.get(i).getAddress());
                    obj.put("addressTips", addressEntityList.get(i).getAddressTips());
                    jsonArray.put(obj);
                }
                jsonObj.put("result", jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mSharedPreferncesEditor.putString(ADDRESS_SP_KEY, jsonObj.toString());
            mSharedPreferncesEditor.commit();
        } else if (addressEntityList.size() == 0) {
            try {
                jsonObj = new JSONObject();
                jsonArray = new JSONArray();
                jsonObj.put("result", jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mSharedPreferncesEditor.clear();
            mSharedPreferncesEditor.commit();
        }
        LogUtils.log("remove以后的数据是" + jsonObj.toString());
    }

    /**
     * 存入地址
     *
     * @param entity
     */
    public void saveAddress(AddressEntity entity) {
        if (entity == null) {
            return;
        }
        record = getAddressBook();
        mSharedPreferncesEditor.commit();
        if (TextUtils.isEmpty(record)) {
            //没有记录
            JSONObject obj = new JSONObject();
            try {
                obj.put("chainType", entity.getChainType());
                obj.put("addressName", entity.getAddressName());
                obj.put("address", entity.getAddress());
                obj.put("addressTips", entity.getAddressTips());
                if (jsonArray != null) {
                    jsonArray.put(obj);
                }

                jsonObj.put("result", jsonArray);
                mSharedPreferncesEditor.putString(ADDRESS_SP_KEY, jsonObj.toString());
                mSharedPreferncesEditor.commit();

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                JSONObject obj_root = new JSONObject(record);
                JSONArray array = obj_root.optJSONArray("result");
                JSONObject obj = new JSONObject();
                obj.put("chainType", entity.getChainType());
                obj.put("addressName", entity.getAddressName());
                obj.put("address", entity.getAddress());
                obj.put("addressTips", entity.getAddressTips());
                if (array != null) {
                    array.put(obj);
                    obj_root.put("result", array);
                }
                jsonObj = obj_root;
                jsonArray = array;
                record = obj_root.toString();
                mSharedPreferncesEditor.putString(ADDRESS_SP_KEY, obj_root.toString());
                mSharedPreferncesEditor.commit();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        LogUtils.log("地址存入" + jsonObj.toString());
    }

    /**
     * 获取地址簿列表
     *
     * @return
     */
    public List<AddressEntity> getAddressList() {
        List<AddressEntity> lists = new ArrayList<>();
        record = getAddressBook();
        LogUtils.log("获取地址列表的JSON是" + record);
        if (TextUtils.isEmpty(record)) {
            return lists;
        } else {
            try {
                JSONObject obj_root = new JSONObject(record);
                JSONArray jsonArray = obj_root.getJSONArray("result");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    AddressEntity entity = new AddressEntity();
                    entity.setAddress(obj.optString("address"));
                    entity.setAddressName(obj.optString("addressName"));
                    entity.setAddressTips(obj.optString("addressTips"));
                    entity.setChainType(obj.optInt("chainType"));
                    lists.add(entity);
                }
                return lists;
            } catch (JSONException e) {
                e.printStackTrace();
                return lists;
            }
        }
    }
}
