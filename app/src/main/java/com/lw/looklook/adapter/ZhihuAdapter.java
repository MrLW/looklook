package com.lw.looklook.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lw.looklook.MainActivity;
import com.lw.looklook.R;
import com.lw.looklook.activity.ZhihuDescribeActivity;
import com.lw.looklook.bean.zhihu.ZhihuDailyItem;
import com.lw.looklook.utils.Constant;
import com.lw.looklook.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lw on 2017/2/2.
 * 知乎适配器
 */
public class ZhihuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements MainActivity.LoadingMore {

    private static final int TYPE_LOADING_MORE = 2;
    private Context context;
    float width;
    int widthPx;
    int heighPx;
    private boolean loadingMore;
    private static final int NORMAL_ITEM = 1;

    public List<ZhihuDailyItem> zhihuDailyItemList = new ArrayList<>();

    public ZhihuAdapter(Context context) {
        this.context = context;
        width = context.getResources().getDimension(R.dimen.image_width);
        widthPx = DensityUtil.dip2px(context, width);
        heighPx = widthPx * 3 / 4;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.zhihu_layout_item, null);
        ZhihuViewHolder viewHolder = new ZhihuViewHolder(itemView);
        switch (viewType) {
            case Constant.NORMAL_ITEM:
                return new ZhihuViewHolder(LayoutInflater.from(context).inflate(R.layout.zhihu_layout_item, null));
            case Constant.LOADING_MORE_ITEM:
                return new LoadingMoreHolder(LayoutInflater.from(context).inflate(R.layout.infinite_loading, parent, false));
        }
        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        switch (type) {
            case Constant.NORMAL_ITEM:
                bindNormalViewHolder((ZhihuViewHolder) holder, position);
                break;
            case Constant.LOADING_MORE_ITEM:
                bindLoadingViewHolder((LoadingMoreHolder) holder, position);
                break;
        }

    }

    private void bindLoadingViewHolder(LoadingMoreHolder holder, int position) {
        holder.progressBar.setVisibility(loadingMore == true ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * 绑定普通ViewHolder
     * 缓存策略:
     * all:缓存源资源和转换后的资源
     * none:不作任何磁盘缓存
     * source:缓存源资源
     * result：缓存转换后的资源
     *
     * @param holder
     * @param position
     */
    private void bindNormalViewHolder(final ZhihuViewHolder holder, int position) {
        final ZhihuDailyItem zhihuDailyItem = zhihuDailyItemList.get(holder.getAdapterPosition());

        holder.item_tv.setText(zhihuDailyItem.getTitle());

        holder.item_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goDescribeInfo(holder, zhihuDailyItem);
            }
        });

        holder.zhihu_item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goDescribeInfo(holder, zhihuDailyItem);
            }
        });

        // 开始加载图片
        Glide.with(context)
                .load(zhihuDailyItem.getImages()[0])
                .diskCacheStrategy(DiskCacheStrategy.SOURCE) // 缓存源资源
                .centerCrop().override(widthPx, heighPx)
                .into(holder.item_iv);
    }

    /**
     * 进入详情信息页面
     *
     * @param holder
     * @param zhihuDailyItem
     */
    private void goDescribeInfo(ZhihuViewHolder holder, ZhihuDailyItem zhihuDailyItem) {
        Intent intent = new Intent(context, ZhihuDescribeActivity.class);
        intent.putExtra("id", zhihuDailyItem.getId());
        intent.putExtra("title", zhihuDailyItem.getTitle());
        intent.putExtra("image", zhihuDailyItem.getImages()[0]);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return zhihuDailyItemList.size();
    }

    /**
     * 条目的类型(加载更多item/普通item)
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (position < zhihuDailyItemList.size() && position >= 0) {
            return Constant.NORMAL_ITEM;
        }
        return Constant.LOADING_MORE_ITEM;
    }

    /**
     * 添加数据
     *
     * @param zhihuDailyItems
     */
    public void addItems(ArrayList<ZhihuDailyItem> zhihuDailyItems) {
        zhihuDailyItemList.addAll(zhihuDailyItems);
        // 重新刷新数据
        notifyDataSetChanged();
    }


    class ZhihuViewHolder extends RecyclerView.ViewHolder {

        TextView item_tv;
        ImageView item_iv;
        LinearLayout zhihu_item_layout;

        public ZhihuViewHolder(View itemView) {
            super(itemView);
            item_tv = (TextView) itemView.findViewById(R.id.item_text_id);
            item_iv = (ImageView) itemView.findViewById(R.id.item_iv);
            zhihu_item_layout = (LinearLayout) itemView.findViewById(R.id.zhihu_item_layout);
        }
    }

    public static class LoadingMoreHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingMoreHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView;
        }
    }


    @Override
    public void loadingStart() {
        if (loadingMore) return;
        loadingMore = true;
    }

    @Override
    public void loadingfinish() {
        if (!loadingMore) return;

    }
}
