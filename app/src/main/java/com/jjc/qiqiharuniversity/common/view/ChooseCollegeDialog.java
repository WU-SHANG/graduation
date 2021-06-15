package com.jjc.qiqiharuniversity.common.view;

import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.common.base.BaseRVAdapter;

import java.util.List;

/**
 * Author jiajingchao
 * Created on 2021/2/23
 * Description:选择学院的底部弹窗
 */
public class ChooseCollegeDialog extends AnimDialogFragment {

    private TextView tvCancel, tvDone;
    private onChooseListener listener;
    private RecyclerView rvPicker;
    private CollegeRVAdapter adapter;
    private int selectedItemPosition = 0;
    private List<String> dataList;

    @Override
    protected int getRootLayout() {
        return R.layout.layout_choose_college_dialog;
    }

    @Override
    public int getGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    protected int getDialogAnimationRes() {
        return R.style.animate_dialog;
    }


    @Override
    protected void initView(View view) {
        tvCancel = view.findViewById(R.id.tv_picker_cancel);
        tvDone = view.findViewById(R.id.tv_picker_done);
        rvPicker = view.findViewById(R.id.rv_picker);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissAllowingStateLoss();
            }
        });


        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onSelectDoneClick(selectedItemPosition);
                }
                dismissAllowingStateLoss();
            }
        });

        adapter = new CollegeRVAdapter(getContext(), dataList);
        adapter.setOnItemClick(new BaseRVAdapter.OnItemClick() {
            @Override
            public void onClick(int position) {
                selectedItemPosition = position;
                adapter.setSelectedPosition(position);
                adapter.notifyDataSetChanged();
            }
        });
        rvPicker.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPicker.setAdapter(adapter);
    }

    public void setDataList(List<String> dataList) {
        this.dataList = dataList;
    }

    public void setOnChooseListener(onChooseListener listener) {
        this.listener = listener;
    }

    public interface onChooseListener {

        void onSelectDoneClick(int position);

    }

}
