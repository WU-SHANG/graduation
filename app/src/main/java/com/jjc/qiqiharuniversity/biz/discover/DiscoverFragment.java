package com.jjc.qiqiharuniversity.biz.discover;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.common.ToastManager;
import com.jjc.qiqiharuniversity.common.base.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Author jiajingchao
 * Created on 2021/2/22
 * Description:
 */
public class DiscoverFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private GridView gridView;
    private SimpleAdapter simpleAdapter;
    private List<Map<String, Object>> dataList;
    private int[] icons = {R.drawable.icon_community, R.drawable.icon_lost, R.drawable.icon_medical,
            R.drawable.icon_classroom, R.drawable.icon_questionnaire, R.drawable.icon_feedback,
            R.drawable.icon_recruitment, R.drawable.icon_map, R.drawable.icon_wait, };
    private String[] iconNames = {"超级社团", "失物招领", "医疗服务", "空闲教室", "调查问卷", "反馈邮箱", "招聘信息", "校园地图", "敬请期待"};

    @Override
    public int getRootLayout() {
        return R.layout.fragment_discover;
    }

    @Override
    public void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        gridView = view.findViewById(R.id.gv_discover);
        dataList = new ArrayList<>();
        getData();
        String[] from = {"image", "text"};
        int[] to = {R.id.iv_img, R.id.tv_text};
        simpleAdapter = new SimpleAdapter(getContext(), dataList, R.layout.item_discover_gridview, from, to);
        gridView.setAdapter(simpleAdapter);
        gridView.setOnItemClickListener(this);
    }

    public void getData() {
        for (int i = 0; i < iconNames.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("image", icons[i]);
            map.put("text", iconNames[i]);
            dataList.add(map);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ToastManager.show(view.getContext(), iconNames[position]);
    }
}
