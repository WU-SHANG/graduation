package com.jjc.qiqiharuniversity.biz.discover;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
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
 * Description:发现页
 */
public class DiscoverFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private ImageView ivEpidemic;
    private GridView gridView;
    private SimpleAdapter simpleAdapter;
    private List<Map<String, Object>> dataList;
    private int[] icons = {R.drawable.icon_campus_network, R.drawable.icon_lost, R.drawable.icon_market,
            R.drawable.icon_cet, R.drawable.icon_medical, R.drawable.icon_score, R.drawable.icon_classroom, R.drawable.icon_questionnaire,
            R.drawable.icon_seat, R.drawable.icon_recruitment, R.drawable.icon_map, R.drawable.icon_wait, };
    private String[] iconNames = {"办理宽带", "失物招领", "二手市场", "CET查询", "医疗服务", "GPA计算", "空闲教室",
            "调查问卷", "预约选座", "招聘信息", "校园地图", "敬请期待"};
    // 二手交易（齐大学子之声），四六级入口（印象齐大）,表白墙（印象齐大）,GPA计算（印象齐大）

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
        ivEpidemic = view.findViewById(R.id.iv_epidemic);
        ivEpidemic.setOnClickListener(v -> {
            EpidemicWebViewActivity.start(getContext(), EpidemicWebViewActivity.class);
        });
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
        if (getContext() == null) {
            return;
        }
        switch (position) {
            case 0: {
                CampusNetworkActivity.start(getContext(), CampusNetworkActivity.class);
                break;
            }
            case 3: {
                CETWebViewActivity.start(getContext(), CETWebViewActivity.class);
                break;
            }
            case 8: {
                SeatReservationActivity.start(getContext(), SeatReservationActivity.class);
                break;
            }
            case 9: {
                RecruitmentWebViewActivity.start(getContext(), RecruitmentWebViewActivity.class);
                break;
            }
            case 10: {
                SchoolMapWebViewActivity.start(getContext(), SchoolMapWebViewActivity.class);
                break;
            }
            case 1:
            case 2:
            case 4:
            case 5:
            case 6:
            case 7: {
                ToastManager.show(view.getContext(), "暂未开发，敬请期待~");
                break;
            }
            case 11: {
                ToastManager.show(view.getContext(), "更多功能，敬请期待~");
                break;
            }
        }
    }
}
