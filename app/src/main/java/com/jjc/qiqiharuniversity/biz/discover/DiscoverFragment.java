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
    private int[] icons = {R.drawable.icon_campus_network, R.drawable.icon_dept, R.drawable.icon_seat,
            R.drawable.icon_cet, R.drawable.icon_mandarin, R.drawable.icon_teacher,
            R.drawable.icon_recruitment, R.drawable.icon_map, R.drawable.icon_weibo, };
    private String[] iconNames = {"办理宽带", "教务处入口","预约选座", "CET查询", "普通话查询", "教资查询",
            "就业讯息", "校园地图", "齐大微博"};
    // 未实现：二手交易（齐大学子之声），表白墙（印象齐大）,垃圾分类手册（华东师范），宅游（华东师范） + 敬请期待
    // 灵感：校园图库（华东师范）

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
            case 1: {
                DeptWebViewActivity.start(getContext(), DeptWebViewActivity.class);
                break;
            }
            case 2: {
                SeatReservationActivity.start(getContext(), SeatReservationActivity.class);
                break;
            }
            case 3: {
                CETWebViewActivity.start(getContext(), CETWebViewActivity.class);
                break;
            }
            case 4: {
                MandarinWebViewActivity.start(getContext(), MandarinWebViewActivity.class);
                break;
            }
            case 5: {
                TeacherQualificationWebViewActivity.start(getContext(), TeacherQualificationWebViewActivity.class);
                break;
            }
            case 6: {
                RecruitmentActivity.start(getContext(), RecruitmentActivity.class);
                break;
            }
            case 7: {
                SchoolMapWebViewActivity.start(getContext(), SchoolMapWebViewActivity.class);
                break;
            }
            case 8: {
                WeiboWebViewActivity.start(getContext(), WeiboWebViewActivity.class);
                break;
            }
            case 9: {
                ToastManager.show(view.getContext(), "更多功能，敬请期待~");
                break;
            }
            default: {
                ToastManager.show(view.getContext(), "暂未开发，敬请期待~");
                break;
            }
        }
    }
}
