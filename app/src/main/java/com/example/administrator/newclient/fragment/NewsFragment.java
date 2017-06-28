package com.example.administrator.newclient.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.administrator.newclient.R;
import com.example.administrator.newclient.adapter.NewsAdapter;
import com.example.administrator.newclient.base.BaseFragment;
import com.example.administrator.newclient.base.URLManager;
import com.example.administrator.newclient.bean.NewsEntity;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;


/**
 * Created by Administrator on 2017/6/27.
 */

public class NewsFragment extends BaseFragment {


    private String channelId;
    private TextView textView;
    private ListView listView;

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_news;
    }

    @Override
    public void initView() {
        listView = (ListView) mRoot.findViewById(R.id.list_view);
        textView = (TextView) mRoot.findViewById(R.id.tv_01);
        textView.setText("类别id:" + channelId);    // 显示新闻类别id，以作区分
    }

    @Override
    public void initListener() {

    }


    @Override
    public void initData() {
        getDataFromServer();
    }

    // 1请求服务器获取页签详细数据
    private void getDataFromServer() {

        // http://c.m.163.com/nc/article/headline/T1348647909107/0-20.html
        String url = URLManager.getUrl(channelId);

        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            //请求成功
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //服务器返回的json数据
                String json = responseInfo.result;
                System.out.println("----服务器返回的json数据:" + json);

                //解析json数据
                json = json.replace(channelId, "result");
                Gson gson = new Gson();
                NewsEntity newsDatas = gson.fromJson(json, NewsEntity.class);
                System.out.println("----解析json:" + newsDatas.getResult().size() + "数据");

                //列表显示的数据集合
                List<NewsEntity.ResultBean> listDates = newsDatas.getResult();

                //3.显示列表，适配器baseadapter,显示数据到列表中
                showDatas(listDates);
            }

            //请求失败
            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
                System.out.println("----------msg:" + msg);
            }
        });
    }

    private void showDatas(List<NewsEntity.ResultBean> listDatas) {
// （1）显示轮播图
        // 第一条新闻
        NewsEntity.ResultBean firstNews = listDatas.get(0);
        // 有轮播图
        if (firstNews.getAds() != null && firstNews.getAds().size() > 0) {
            View headerView = LayoutInflater.from(getContext()).inflate(R.layout
                    .list_header, listView, false);

            // 查找轮播图控件
            SliderLayout sliderLayout = (SliderLayout)
                    headerView.findViewById(R.id.slider_layout);
            // 准备轮播图要显示的数据
            List<NewsEntity.ResultBean.AdsBean> ads = firstNews.getAds();
            // 添加轮播图子界面
            for (int i = 0; i < ads.size(); i++) {
                NewsEntity.ResultBean.AdsBean bean = ads.get(i);

                // 一个TextSliderView表示一个子界面
                TextSliderView textSliderView = new TextSliderView(getContext());
                textSliderView.description(bean.getTitle())  // 显示标题
                        .image(bean.getImgsrc());      // 显示图片

                sliderLayout.addSlider(textSliderView);       // 添加一个子界面
            }

            // 添加到轮播图到列表的头部
            listView.addHeaderView(headerView);

        } else {
            // 没有轮播图的情况
        }

        // （2）显示列表
        NewsAdapter newsAdapter = new NewsAdapter(getContext(), listDatas);
        listView.setAdapter(newsAdapter);
    }


}
