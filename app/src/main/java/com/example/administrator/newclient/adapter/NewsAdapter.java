package com.example.administrator.newclient.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.newclient.R;
import com.example.administrator.newclient.bean.NewsEntity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2017/6/28.
 */

public class NewsAdapter extends BaseAdapter {

    /**
     * 列表显示的新闻数据
     */
    private List<NewsEntity.ResultBean> listDatas;
    private Context context;
    private static final int ITEM_TYPE_WITH_1_IMAGE = 0;
    private static final int ITEM_TYPE_WITH_3_IMAGE = 1;


    //有多少个列表项
    @Override
    public int getCount() {
        return (listDatas == null) ? 0 : listDatas.size();
    }

    public NewsAdapter(Context context, List<NewsEntity.ResultBean> listDatas) {
        this.context = context;
        this.listDatas = listDatas;
    }

    //  返回指定位置的列表项对应的实体数据(javabean)
    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //返回列表项视图，只要显示列表项时，就会调用此方法
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 列表项数据
       // NewsEntity.ResultBean info = (NewsEntity.ResultBean) getItem(position);
        NewsEntity.ResultBean info = listDatas.get(position);


        int itemViewType = getItemViewType(position);

        if (itemViewType==ITEM_TYPE_WITH_1_IMAGE) {
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_news_1, null);
            }

            // 查找列表item中的子控件
            ImageView ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
            TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            TextView tvSource = (TextView) convertView.findViewById(R.id.tv_source);
            TextView tvComment = (TextView) convertView.findViewById(R.id.tv_comment);

            //获取列表项对应的数据（javabean）
            NewsEntity.ResultBean  news = (NewsEntity.ResultBean) getItem(position);

            // 显示列表item中的子控件
            tvTitle.setText(info.getTitle());
            tvSource.setText(info.getSource());
            tvComment.setText(info.getReplyCount() + "跟帖");
            Picasso.with(context).load(info.getImgsrc()).into(ivIcon);
        }else if (itemViewType==ITEM_TYPE_WITH_3_IMAGE){
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_news_2, null);
            }

            // 查找列表item中的子控件
            TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            TextView  tvComment = (TextView) convertView.findViewById(R.id.tv_comment);
            ImageView iv01 = (ImageView) convertView.findViewById(R.id.iv_01);
            ImageView iv02 = (ImageView) convertView.findViewById(R.id.iv_02);
            ImageView iv03 = (ImageView) convertView.findViewById(R.id.iv_03);


            // 3. 获取列表项对应的数据（javabean）
            NewsEntity.ResultBean news = (NewsEntity.ResultBean) getItem(position);

            // 显示列表item中的子控件
            tvTitle.setText(info.getTitle());
            tvComment.setText(info.getReplyCount() + "跟帖");
            try {
                Picasso.with(context).load(info.getImgsrc()).into(iv01);
                Picasso.with(context).load(info.getImgextra().get(0).getImgsrc())
                        .into(iv02);
                Picasso.with(context).load(info.getImgextra().get(1).getImgsrc())
                        .into(iv03);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        return convertView;
    }


    @Override
    public int getItemViewType(int position) {
        NewsEntity.ResultBean item = listDatas.get(position);
        if (item.getImgextra() == null || item.getImgextra().size() == 0) {
            // 只有一张图片的item
            return ITEM_TYPE_WITH_1_IMAGE;
        } else {
            // item中有三张图片
            return ITEM_TYPE_WITH_3_IMAGE;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }


    public void setDatas(List<NewsEntity.ResultBean> listDatas){
        this.listDatas =listDatas;
        notifyDataSetChanged();
    }

    /** 追加数据，并刷新列表显示 */
    public void appendDatas(List<NewsEntity.ResultBean> listDatas) {
        this.listDatas.addAll(listDatas);
        notifyDataSetChanged();     // 刷新列表
    }

}