package com.example.administrator.newclient.fragment;

import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.administrator.newclient.R;
import com.example.administrator.newclient.activity.NewsDetailActivity;
import com.example.administrator.newclient.adapter.NewsAdapter;
import com.example.administrator.newclient.base.BaseFragment;
import com.example.administrator.newclient.base.URLManager;
import com.example.administrator.newclient.bean.NewsEntity;
import com.google.gson.Gson;
import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.MeituanHeader;
import com.liaoinstan.springview.widget.SpringView;
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

    private TextView textView;
    private ListView listView;

    /*新闻列表*/
    private String channelId;
    private SpringView springView;
    private NewsAdapter newsAdapter;
    private int pageNo = 1;

    private List<NewsEntity.ResultBean> listDatas;
    private View headerView;

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

        newsAdapter = new NewsAdapter(getContext(), null);
        listView.setAdapter(newsAdapter);

        textView.setText("类别id:" + channelId);    // 显示新闻类别id，以作区分

        initSpringView();
    }


    private void initSpringView() {
        springView = (SpringView) mRoot.findViewById(R.id.spring_View);

        // 设置SpringView头部和尾部

       /* DefaultHeader (com.liaoinstan.springview.container)
        AcFunHeader (com.liaoinstan.springview.container)
        RotationHeader (com.liaoinstan.springview.container)
       AliHeader (com.liaoinstan.springview.container)
        MeituanHeader (com.liaoinstan.springview.container)*/

//        springView.setHeader(new AcFunHeader(getContext(), R.drawable.ad_new_version1_img1));
        springView.setHeader(new MeituanHeader(getContext()));
        springView.setFooter(new DefaultFooter(getContext()));

        // springView.setType(SpringView.Type.OVERLAP);
        springView.setType(SpringView.Type.FOLLOW);

        // 设置监听器
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {       // 下拉，刷新第一页数据
               // showToast("下拉");
                // 请求服务器第一页数据,然后刷新
                getDataFromServer(true);

            }

            @Override
            public void onLoadmore() {      // 上拉，加载下一页数据
               // showToast("上拉");
                // 请求服务器下一页数据
                getDataFromServer(false);

               /* new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 2秒后，隐藏springView控件上拉和下拉提示
                        springView.onFinishFreshAndLoad();
                    }
                }, 2000);*/
            }
        });
    }

    @Override
    public void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 用户点击的新闻
                /*NewsEntity.ResultBean newsBean = (NewsEntity.ResultBean)
                        parent.getItemAtPosition(position);*/
                int index = position;
                if (listView.getHeaderViewsCount() > 0) {
                    index = index - 1;
                }

                NewsEntity.ResultBean newsBean = listDatas.get(index);

                Intent intent = new Intent(mActivity, NewsDetailActivity.class);
                intent.putExtra("news", newsBean);
                startActivity(intent);
            }
        });
    }


    @Override
    public void initData() {
        getDataFromServer(true);
    }

    /**
     * 获取服务器新闻数据
     *
     * @param refresh true表示下拉刷新，false表示加载下一页数据
     */
    // 1请求服务器获取页签详细数据
    private void getDataFromServer(final boolean refresh) {

        if (refresh) { // 如果是下拉刷新
            pageNo = 1;
        }
        // http://c.m.163.com/nc/article/headline/T1348647909107/0-20.html
        String url = URLManager.getUrl(channelId, pageNo);

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
                listDatas = newsDatas.getResult();

                //3.显示列表，适配器baseadapter,显示数据到列表中
                if (refresh) {
                    showDatas(listDatas);
                } else {// 上拉加载下一页数据
                    newsAdapter.appendDatas(listDatas);
                }
                pageNo++;       // 页码自增1

                //  隐藏SpringView的下拉和上拉显示
                springView.onFinishFreshAndLoad();

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

        // 如果列表已经添加了头部布局，则先移除
        if (listView.getHeaderViewsCount() > 0) {
            System.out.println("----------我进来了");
            listView.removeHeaderView(headerView);
        }

        // 第一条新闻
        NewsEntity.ResultBean firstNews = listDatas.get(0);
        // 有轮播图
        if (firstNews.getAds() != null && firstNews.getAds().size() > 0) {
            headerView = LayoutInflater.from(getContext()).inflate(R.layout
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
        /*NewsAdapter newsAdapter = new NewsAdapter(getContext(), listDatas);
        listView.setAdapter(newsAdapter);*/
        newsAdapter.setDatas(listDatas);
    }


}
