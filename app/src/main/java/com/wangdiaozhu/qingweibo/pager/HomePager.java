package com.wangdiaozhu.qingweibo.pager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.User;
import com.sina.weibo.sdk.utils.LogUtil;
import com.squareup.picasso.Picasso;
import com.wangdiaozhu.qingweibo.Constants.AccessTokenKeeper;
import com.wangdiaozhu.qingweibo.Constants.Constants;
import com.wangdiaozhu.qingweibo.R;
import com.wangdiaozhu.qingweibo.activity.CommentActivity;
import com.wangdiaozhu.qingweibo.activity.EditActivity;
import com.wangdiaozhu.qingweibo.activity.MainActivity;
import com.wangdiaozhu.qingweibo.activity.StatusActivity;
import com.wangdiaozhu.qingweibo.activity.TransmitActivity;
import com.wangdiaozhu.qingweibo.base.BaseActivity;
import com.wangdiaozhu.qingweibo.base.BasePager;
import com.wangdiaozhu.qingweibo.fragment.ContentFragment;
import com.wangdiaozhu.qingweibo.utils.PrefUtils;
import com.wangdiaozhu.qingweibo.utils.WeiboTextUtils;
import com.wangdiaozhu.qingweibo.view.IconButton;
import com.wangdiaozhu.qingweibo.view.RefreshListView;
import com.wangdiaozhu.qingweibo.view.nineimageview.NineGridImageView;
import com.wangdiaozhu.qingweibo.view.nineimageview.NineGridImageViewAdapter;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;

import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/6/5.
 */
public class HomePager extends BasePager {




    private RefreshListView lv_home;
    private ArrayList<Status> statuses;
    private HomeListAdapter mAdapter;
    private String str;
     private  MainActivity mainActivity;
    private ContentFragment fragment;
    private ArrayList<Status> statuses1;

    public HomePager(Activity activity) {

        super(activity);
    }

    @Override
    public void initData() {

        super.setTitleMiddleText("首页");
        super.showTitleLeftBtn(R.drawable.selector_navbar_home_left);
        super.showTitleRightBtn(R.drawable.selector_navbar_home_right);
        View mHomePager = View.inflate(mActivity, R.layout.pager_home, null);
        fl_content.addView(mHomePager);
        lv_home = (RefreshListView) mHomePager.findViewById(R.id.lv_home);
        lv_home.initHeaderView();
        lv_home.initFooterView();
//        更新数据
        mainActivity = (MainActivity) mActivity;
        fragment = mainActivity.getContentFragment();
        statuses = fragment.getStatuses();
        initAdapter(statuses);
        lv_home.setAdapter(mAdapter);
        lv_home.OnRefreshComplete(true);
        mAdapter.notifyDataSetChanged();


        lv_home.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void OnRefresh() {
             mainActivity.refreshData();

                    statuses = fragment.getStatuses();
                   mAdapter.notifyDataSetChanged();
                    lv_home.OnRefreshComplete(true);

            }
            @Override
            public void loadMore() {

               int count =  mAdapter.getCount();

                if (statuses.size()==mAdapter.getCount()) {

                    Toast.makeText(mActivity, "没有更多数据了", Toast.LENGTH_SHORT).show();

                }else {
                    for (int i = count; i < count + 10; i++) {
                        mAdapter.addItem(statuses.get(i));

                    }
                }
                    mAdapter.notifyDataSetChanged();
                    lv_home.OnRefreshComplete(true);


                }
        });


    }

//正则表达式获取时间格式
private String getTime(String time){

    Pattern p = Pattern.compile("([01]?\\d|2[0-3]):[0-5]?\\d:[0-5]?\\d");

        Matcher m = p.matcher(time);
         System.out.println(time);
    if (m.find()){

        String str = m.group();
        return str;
    }else {
        System.out.println("未匹配到");
    }
    return null;
}
    private String getCreateFrom(String source){

        Pattern p = Pattern.compile("<.+?>", Pattern.DOTALL);

        Matcher m = p.matcher(source);
        if (m.find()){

            str = m.replaceAll("");
            System.out.println("匹配到"+ str);
            return str;
        }else {
            System.out.println("未匹配到");

        }
        return null;
    }
    private void initAdapter ( ArrayList<Status> statuses) {
        ArrayList<Status> statuses1 = new ArrayList<Status>();
        for (int i = 0; i < 10; i++) {

            statuses1.add(statuses.get(i));
        }
        mAdapter = new HomeListAdapter(statuses1);
    }



        class HomeListAdapter extends BaseAdapter implements Serializable{


        private BitmapUtils mBitmapUtils;

        private ArrayList<Status> statuses;

        public HomeListAdapter(ArrayList<Status> statuses){

            this.statuses = statuses;

            mBitmapUtils = new BitmapUtils(mActivity);

        }


        @Override
        public int getCount() {
            return statuses.size();
        }

        @Override
        public Object getItem(int position) {
            return statuses.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if (convertView == null){
                convertView = View.inflate(mActivity,R.layout.list_item_home,null);
                holder = new ViewHolder();

                holder.iv_user_icon = (ImageView) convertView.findViewById(R.id.iv_user_icon);
                holder.tv_user_name = (TextView) convertView.findViewById(R.id.tv_user_name);
                holder.tv_create_at = (TextView) convertView.findViewById(R.id.tv_create_at);
                holder.tv_create_from = (TextView) convertView.findViewById(R.id.tv_create_from);
                holder.tv_weibo_text_content = (TextView) convertView.findViewById(R.id.tv_weibo_text_content);
                holder.iv_weibo_text_content = (NineGridImageView) convertView.findViewById(R.id.iv_weibo_text_content);
                holder.iv_weibo_text_content.setAdapter(holder.mAdapter);
                holder.ll_weibo_ztext_content = (LinearLayout) convertView.findViewById(R.id.ll_weibo_ztext_content);
                holder.tv_weibo_ztext_content = (TextView) convertView.findViewById(R.id.tv_weibo_ztext_content);
                holder.iv_weibo_ztext_content = (NineGridImageView) convertView.findViewById(R.id.iv_weibo_ztext_content);
                holder.iv_weibo_ztext_content.setAdapter(holder.mAdapter);
                holder.btn_transmit = (IconButton) convertView.findViewById(R.id.btn_transmit);
                holder.btn_comment = (IconButton) convertView.findViewById(R.id.btn_comment);
                holder.btn_praise = (IconButton) convertView.findViewById(R.id.btn_praise);
                holder.ll_item_status = (LinearLayout) convertView.findViewById(R.id.ll_item_status);

                convertView.setTag(holder);
            }else {

                holder = (ViewHolder) convertView.getTag();

            }

           mBitmapUtils.display(holder.iv_user_icon, statuses.get(position).user.avatar_large);


            holder.tv_user_name.setText(statuses.get(position).user.screen_name);


        //发微博时间
            String str = statuses.get(position).created_at;
            String time =   getTime(str);
            holder.tv_create_at.setText(time);


            String str2 = statuses.get(position).source;
            String source = getCreateFrom(str2);

         //  发微博的来源
            if (source != null) {
                holder.tv_create_from.setText("来自" + source);
            }else {

                holder.tv_create_from.setText(" ");

            }

            String str5 = statuses.get(position).text;
            SpannableString text = WeiboTextUtils.setTextHighLight(str5,mActivity);
            holder.tv_weibo_text_content.setText(text);
           /*
           *
           * 自发微博
           *
           * */

            if(statuses.get(position).pic_urls!=null) {
                holder.iv_weibo_text_content.setVisibility(View.VISIBLE);

                holder.iv_weibo_text_content.setImagesData(statuses.get(position).pic_urls);

                }else {holder.iv_weibo_text_content.setVisibility(View.GONE);}
            /*
            *
            * 转发微博
            *
            * */

            if (statuses.get(position).retweeted_status != null){
                holder.ll_weibo_ztext_content.setVisibility(View.VISIBLE);
                holder.tv_weibo_ztext_content.setVisibility(View.VISIBLE);
                holder.iv_weibo_ztext_content.setVisibility(View.VISIBLE);
                 String str4 =     statuses.get(position).retweeted_status.text;
                SpannableString text1 = WeiboTextUtils.setTextHighLight(str4,mActivity);
                holder.tv_weibo_ztext_content.setText(text1);
                holder.iv_weibo_ztext_content.setImagesData(statuses.get(position).retweeted_status.pic_urls);
            }else {holder.ll_weibo_ztext_content.setVisibility(View.GONE);}
           /*
           *
           * 底部按钮
           *
           *
           * */
            if (statuses.get(position).reposts_count !=0 ){
                holder.btn_transmit.setText(statuses.get(position).reposts_count +"");
            }else { holder.btn_transmit.setText("转发") ;}
            if(statuses.get(position).comments_count != 0){
                holder.btn_comment.setText(statuses.get(position).comments_count + "");
            }else { holder.btn_comment.setText("评论") ;}
            if (statuses.get(position).attitudes_count!= 0){
                holder.btn_praise.setText(statuses.get(position).attitudes_count + "");
            }else { holder.btn_praise.setText("赞") ;}

            holder.ll_item_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (statuses.get(position)!= null) {
                        Intent intent = new Intent(mActivity,StatusActivity.class);
                        Status status = statuses.get(position);
                              Bundle bundle = new Bundle();
                        bundle.putSerializable("item_data",status);
                        intent.putExtras(bundle);
                        mActivity.startActivity(intent);
                    }else {

                        System.out.println("dsadadada2131333333321313");

                    }
                }
            });
            holder.btn_transmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (statuses.get(position)!= null) {
                        Intent intent = new Intent(mActivity,TransmitActivity.class);
                        Status status = statuses.get(position);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("transmit_status",status);
                        intent.putExtras(bundle);
                        mActivity.startActivity(intent);
                    }else {
                        System.out.println("dsadadada2131333333321313");
                    }


                }
            });



            holder.btn_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (statuses.get(position)!= null) {
                        Intent intent = new Intent(mActivity,StatusActivity.class);
                        Status status = statuses.get(position);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("item_data",status);
                        intent.putExtras(bundle);
                        mActivity.startActivity(intent);
                    }else {

                        System.out.println("dsadadada2131333333321313");

                    }


                }
            });




                return convertView;

        }


        public void addItem(Status status) {
            statuses.add(status);
        }
    }
     static  class ViewHolder {

         ImageView iv_user_icon;
         TextView tv_user_name;
         TextView tv_create_at;
         TextView tv_create_from;
         TextView tv_weibo_text_content;
         NineGridImageView iv_weibo_text_content;
         LinearLayout ll_weibo_ztext_content;
         TextView tv_weibo_ztext_content;
         NineGridImageView iv_weibo_ztext_content;
         IconButton btn_transmit;
         IconButton btn_comment;
         IconButton btn_praise;
         LinearLayout ll_item_status;

         private NineGridImageViewAdapter<String> mAdapter = new NineGridImageViewAdapter<String>() {
             @Override
             protected void onDisplayImage(Context context, ImageView imageView, String s) {
                 Picasso.with(context)
                         .load(s)
                         .placeholder(R.drawable.message_image_default)
                         .into(imageView);
             }

             @Override
             protected ImageView generateImageView(Context context) {
                 return super.generateImageView(context);
             }

             @Override
             protected void onItemImageClick(Context context, int index, List<String> list) {
                 Toast.makeText(context, "image position is " + index, Toast.LENGTH_SHORT).show();

             }
         };

     }
}
