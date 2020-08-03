package com.tianji.blockchain.sharepreferences;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.tianji.blockchain.Constant;

import java.util.ArrayList;
import java.util.List;

public class SearchHistorySharedPreferences {
    private static SearchHistorySharedPreferences instance;
    private PreferenceHelper helper;
    private static final int MAX = 20;

    private SearchHistorySharedPreferences(Context context) {
        this.helper = PreferenceHelper.getInstance(context);
    }

    public static SearchHistorySharedPreferences getInstance(Context context) {
        if (SearchHistorySharedPreferences.instance == null) {
            SearchHistorySharedPreferences.instance = new SearchHistorySharedPreferences(context);
        }
        return SearchHistorySharedPreferences.instance;
    }

    public void pushHistory(String searchKeyword) {
        List<String> history = this.helper.readEntity(Constant.PreferenceKeys.SEARCH_HISTORY, new TypeReference<List<String>>(){});
        if(history == null) {
            history = new ArrayList<>();
        }

        if (history.size() > 0) {
            int index = -1;

            for (int i = 0; i < history.size(); i ++) {
                String item = history.get(i);
                if (item != null && item.equals(searchKeyword)) {
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

        history.add(0, searchKeyword);
        this.helper.saveEntity(Constant.PreferenceKeys.SEARCH_HISTORY, history);
    }

    public void removeHistory(String historyItem) {
        List<String> history = this.helper.readEntity(Constant.PreferenceKeys.SEARCH_HISTORY, new TypeReference<List<String>>(){});
        if (history == null) return;
        if (history.size() > 0) {
            int index = -1;

            for (int i = 0; i < history.size(); i ++) {
                String item = history.get(i);
                if (item != null && item.equals(historyItem)) {
                    index = i;
                }
            }
            if (index >= 0) {
                history.remove(index);
            }
        }
        this.helper.saveEntity(Constant.PreferenceKeys.SEARCH_HISTORY, history);
    }

    public List<String> getHistory() {
        return this.helper.readEntity(Constant.PreferenceKeys.SEARCH_HISTORY, new TypeReference<List<String>>(){});
    }
}
