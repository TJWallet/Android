package com.tianji.blockchain.sharepreferences;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.tianji.blockchain.Constant;
import com.tianji.blockchain.entity.RecentHistoryEntity;

import java.util.ArrayList;
import java.util.List;

public class RecentHistorySharedPreferences {
    private static RecentHistorySharedPreferences instance;
    private PreferenceHelper helper;
    private static final int MAX = 20;

    private RecentHistorySharedPreferences(Context context) {
        this.helper = PreferenceHelper.getInstance(context);
    }

    public static RecentHistorySharedPreferences getInstance(Context context) {
        if (RecentHistorySharedPreferences.instance == null) {
            RecentHistorySharedPreferences.instance = new RecentHistorySharedPreferences(context);
        }
        return RecentHistorySharedPreferences.instance;
    }

    public void pushHistory(RecentHistoryEntity entity) {
        List<RecentHistoryEntity> history = this.helper.readEntity(Constant.PreferenceKeys.RECENT_HISTORY, new TypeReference<List<RecentHistoryEntity>>(){});
        if(history == null) {
            history = new ArrayList<>();
        }

        if (history.size() > 0) {
            int index = -1;

            for (int i = 0; i < history.size(); i ++) {
                RecentHistoryEntity item = history.get(i);
                if (item.getMid() != null && item.getMid().equals(entity.getMid())) {
                    index = i;
                } else if (item.getDescription() != null && item.getDescription().equals(entity.getDescription())) {
                    index = i;
                }
            }
            if (index >= 0) {
                history.remove(index);
            }
        }

        if (history.size() >= MAX) {
            history.remove(history.size() - 1);
        }

        history.add(0, entity);
        this.helper.saveEntity(Constant.PreferenceKeys.RECENT_HISTORY, history);
    }

    public List<RecentHistoryEntity> getHistory() {
        return this.helper.readEntity(Constant.PreferenceKeys.RECENT_HISTORY, new TypeReference<List<RecentHistoryEntity>>(){});
    }
}
