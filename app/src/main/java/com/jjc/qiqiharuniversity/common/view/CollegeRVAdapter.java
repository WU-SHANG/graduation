package com.jjc.qiqiharuniversity.common.view;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.common.base.BaseRVAdapter;

import java.util.List;

/**
 * Author jiajingchao
 * Created on 2021/2/23
 * Description:选择学院的底部弹窗的列表适配器
 */
public class CollegeRVAdapter extends BaseRVAdapter {

    private int selectedPosition = 0;

    public CollegeRVAdapter(Context context, List<String> list) {
        super(context, list);
    }

    @Override
    public int getItemLayout() {
        return R.layout.item_picker_listview;
    }

    @Override
    public void onBindBaseViewHolder(@NonNull BaseViewHolder holder, int position) {
        TextView tvCollegeName = (TextView) holder.getView(R.id.tv_college_name);
        tvCollegeName.setText(mDataList.get(position).toString());
        if (position == selectedPosition) {
            tvCollegeName.setTextColor(mContext.getResources().getColor(R.color.dark_blue));
        } else {
            tvCollegeName.setTextColor(mContext.getResources().getColor(R.color.common_text_gray));
        }
    }

    public void setSelectedPosition(int position) {
        this.selectedPosition = position;
    }
}
