package com.jjc.qiqiharuniversity.common.base;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jjc.qiqiharuniversity.R;

import java.util.List;

/**
 * Author jiajingchao
 * Created on 2021/2/19
 * Description:
 */
public abstract class BaseRVAdapter<T> extends RecyclerView.Adapter {

    protected Context mContext;
    private OnItemClick mOnItemClick;
    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_ITEM = 1;
    private static final int VIEW_TYPE_FOOT = 2;
    protected List<T> mDataList;

    public BaseRVAdapter(Context context, List<T> list) {
        mContext = context;
        mDataList = list;
    }

    public interface OnItemClick {
        void onClick(int position);
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        mOnItemClick = onItemClick;
    }

    public int getEmptyLayout() {
        return R.layout.empty_default;
    }

    public int getFootLayout() {
        return 0;
    }

    public abstract int getItemLayout();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_EMPTY) {
            View view = LayoutInflater.from(mContext).inflate(getEmptyLayout(), parent, false);
            return new EmptyViewHolder(view);
        }

        if (viewType == VIEW_TYPE_FOOT) {
            View view = LayoutInflater.from(mContext).inflate(getFootLayout(), parent, false);
            return new FootViewHolder(view);
        }

        View view = LayoutInflater.from(mContext).inflate(getItemLayout(), parent, false);
        return new BaseViewHolder(view);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BaseRVAdapter.BaseViewHolder) {
            if (mOnItemClick != null) {
                ((BaseViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClick.onClick(holder.getAdapterPosition());
                    }
                });
            }
            onBindBaseViewHolder((BaseViewHolder) holder, position);
        }
    }

    public abstract void onBindBaseViewHolder(@NonNull BaseViewHolder holder, int position);

    @Override
    public int getItemCount() {
        if (mDataList == null || mDataList.size() == 0) {
            return 1;
        }

        if (getFootLayout() != 0) {
            return mDataList.size() + 1;
        }

        return mDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mDataList == null || mDataList.size() == 0) {
            return VIEW_TYPE_EMPTY;
        }

        if (getFootLayout() != 0 && position == mDataList.size()) {
            return VIEW_TYPE_FOOT;
        }

        return VIEW_TYPE_ITEM;
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {

        private SparseArray<View> mViews;

        BaseViewHolder(View itemView) {
            super(itemView);
            mViews = new SparseArray<>();
        }

        public <V extends View> V getView(int viewId) {
            View view = mViews.get(viewId);
            if (view == null) {
                view = itemView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            //noinspection unchecked
            return (V) view;
        }

    }

    class EmptyViewHolder extends RecyclerView.ViewHolder {

        EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    class FootViewHolder extends RecyclerView.ViewHolder {

        FootViewHolder(View itemView) {
            super(itemView);
        }
    }
}
