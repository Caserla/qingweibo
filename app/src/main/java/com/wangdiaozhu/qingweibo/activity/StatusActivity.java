package com.wangdiaozhu.qingweibo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.emilsjolander.components.StickyScrollViewItems.StickyScrollView;
import com.lidroid.xutils.BitmapUtils;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.CommentsAPI;
import com.sina.weibo.sdk.openapi.models.Comment;
import com.sina.weibo.sdk.openapi.models.CommentList;
import com.sina.weibo.sdk.openapi.models.Status;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.TitlePageIndicator;
import com.wangdiaozhu.qingweibo.Constants.AccessTokenKeeper;
import com.wangdiaozhu.qingweibo.Constants.Constants;
import com.wangdiaozhu.qingweibo.R;
import com.wangdiaozhu.qingweibo.base.BaseActivity;
import com.wangdiaozhu.qingweibo.base.BaseDetailPager;

import com.wangdiaozhu.qingweibo.utils.WeiboTextUtils;
import com.wangdiaozhu.qingweibo.view.CircleImageView;
import com.wangdiaozhu.qingweibo.view.IconButton;
import com.wangdiaozhu.qingweibo.view.NoScrollViewPager;
import com.wangdiaozhu.qingweibo.view.nineimageview.NineGridImageView;
import com.wangdiaozhu.qingweibo.view.nineimageview.NineGridImageViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/6/14.
 */
public class StatusActivity extends BaseActivity {

    ImageView iv_user_icon;
    TextView tv_user_name;
    TextView tv_create_at;
    TextView tv_create_from;
    TextView tv_weibo_text_content;
    NineGridImageView iv_weibo_text_content;
    LinearLayout ll_weibo_ztext_content;
    TextView tv_weibo_ztext_content;
    NineGridImageView iv_weibo_ztext_content;
    Button btn_transmit;
    Button btn_comment;
    Button btn_praise;
    String str;
    LinearLayout ll_button_pagers;
    private String[] CONTENT;
    private StickyScrollView ssl_buttom_pagers;
    private RelativeLayout public_title_left;
    private TextView tv_public_title_left;
    private Oauth2AccessToken mAccessToken;
    private CommentsAPI mCommentsAPI;
    private CommentList comments;
    private TextView tv_zhuanfa;
    private TextView tv_pinglun;
    private TextView tv_zan;
    ArrayList<Comment> commentArrayList;

    Handler handler = new Handler(){


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){

                case 1:
                    commentArrayList  = comments.commentList;
                    tv_zhuanfa.setText("转发 "+status.reposts_count);
                    tv_zan.setText("赞 "+status.attitudes_count);
                    tv_pinglun.setText("评论 "+commentArrayList.size());
                    lv_comment.setAdapter(new CommentAdapter());
                    setListViewHeightBasedOnChildren(lv_comment);
                     ssl_buttom_pagers.smoothScrollTo(0,0);
                    break;
            }

        }
    };
    private ListView lv_comment;
    private Status status;


    @Override
    public void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_status);
        public_title_left = (RelativeLayout) findViewById(R.id.public_title_left);
        iv_user_icon = (ImageView) findViewById(R.id.iv_user_icon);
        tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        tv_create_at = (TextView) findViewById(R.id.tv_create_at);
        ssl_buttom_pagers = (StickyScrollView) findViewById(R.id.ssl_buttom_pagers);
        tv_create_from = (TextView) findViewById(R.id.tv_create_from);
        tv_weibo_text_content = (TextView) findViewById(R.id.tv_weibo_text_content);
        iv_weibo_text_content = (NineGridImageView) findViewById(R.id.iv_weibo_text_content);
        iv_weibo_text_content.setAdapter(mAdapter);
        ll_weibo_ztext_content = (LinearLayout) findViewById(R.id.ll_weibo_ztext_content);
        tv_weibo_ztext_content = (TextView) findViewById(R.id.tv_weibo_ztext_content);
        iv_weibo_ztext_content = (NineGridImageView) findViewById(R.id.iv_weibo_ztext_content);
        tv_public_title_left = (TextView) findViewById(R.id.tv_public_title_left);
        iv_weibo_ztext_content.setAdapter(mAdapter);
        btn_transmit = (IconButton) findViewById(R.id.btn_transmit);
        btn_comment = (IconButton) findViewById(R.id.btn_comment);
        btn_praise = (IconButton) findViewById(R.id.btn_praise);
        tv_zhuanfa = (TextView) findViewById(R.id.tv_zhuanfa);
        tv_pinglun = (TextView) findViewById(R.id.tv_pinglun);
        tv_zan = (TextView) findViewById(R.id.tv_zan);
        lv_comment = (ListView) findViewById(R.id.lv_comment);
    }

    @Override
    public void initData() {

        final Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        status = (Status) bundle.getSerializable("item_data");

        BitmapUtils utils = new BitmapUtils(this);
        utils.display(iv_user_icon, status.user.avatar_large);
        tv_user_name.setText(status.user.screen_name);
        String str = status.created_at;
        String time =   getTime(str);
        tv_create_at.setText(time);
        String str2 = status.source;
        String source = getCreateFrom(str2);
        if (source == null){

            tv_create_from.setText(" ");
        }else {
            tv_create_from.setText("来自" + source);
        }
        SpannableString text1 = WeiboTextUtils.setTextHighLight(status.text,this);
        tv_weibo_text_content.setText(text1);

    if (status.pic_urls != null){
        iv_weibo_text_content.setVisibility(View.VISIBLE);
        iv_weibo_text_content.setImagesData(status.pic_urls);
    }else {
        iv_weibo_text_content.setVisibility(View.GONE);
    }
        if (status.retweeted_status!= null){
            ll_weibo_ztext_content.setVisibility(View.VISIBLE);
            tv_weibo_ztext_content.setVisibility(View.VISIBLE);
           iv_weibo_ztext_content.setVisibility(View.VISIBLE);

            SpannableString text = WeiboTextUtils.setTextHighLight(status.retweeted_status.text,this);

            tv_weibo_ztext_content.setText(text);
            iv_weibo_ztext_content.setImagesData(status.retweeted_status.pic_urls);
        }else {
            ll_weibo_ztext_content.setVisibility(View.GONE);
        }



        if(tv_public_title_left!= null) {
        tv_public_title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        }
        String id =   status.id;
        final long statusid = Long.parseLong(id);
       int  comments_count = status.comments_count;
        refreshComment(statusid,comments_count);
        btn_transmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(),TransmitActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("transmit_status",status);
                intent2.putExtras(bundle1);
                startActivity(intent2);
            }
        });
         btn_comment.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                    Intent intent1 = new Intent(getApplicationContext(),CommentActivity.class);
                 intent1.putExtra("status_id", statusid);
                 startActivity(intent1);

             }
         });



    }


    private void refreshComment(long id,int comments_count){

        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        mCommentsAPI = new CommentsAPI(this, Constants.APP_KEY,mAccessToken);

         RequestListener mListener = new RequestListener() {
            @Override
            public void onComplete(String response) {

                if (!TextUtils.isEmpty(response)) {
                    comments = CommentList.parse(response);
                    if(comments != null && comments.total_number > 0){

                        Message msg = new Message();

                        msg.what = 1;

                        handler.sendEmptyMessage(msg.what);

                    }
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {

            }
        };
        mCommentsAPI.show(id,0L,0L,comments_count,1,0,mListener);

    }



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

    private String getTime(String time) {

        Pattern p = Pattern.compile("([01]?\\d|2[0-3]):[0-5]?\\d:[0-5]?\\d");

        Matcher m = p.matcher(time);
        System.out.println(time);
        if (m.find()) {

            String str = m.group();
            return str;
        } else {
            System.out.println("未匹配到");
        }
        return null;
    }
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
    private String getCreateFrom(String source) {

        Pattern p = Pattern.compile("<.+?>", Pattern.DOTALL);

        Matcher m = p.matcher(source);

        if (m.find()) {

            str = m.replaceAll("");
            System.out.println("匹配到" + str);
            return str;
        } else {
            System.out.println("未匹配到");

        }
        return null;
    }

    class CommentAdapter extends BaseAdapter {


        private BitmapUtils utils;

        public CommentAdapter() {

            utils = new BitmapUtils(getApplicationContext());

        }


        @Override
        public int getCount() {
            return commentArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return commentArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if (convertView == null) {

                convertView = View.inflate(getApplicationContext(), R.layout.list_item_comment, null);
                holder = new ViewHolder();
                holder.iv_user_icon = (CircleImageView) convertView.findViewById(R.id.iv_user_icon);
                holder.tv_user_name = (TextView) convertView.findViewById(R.id.tv_user_name);
                holder.tv_create_at = (TextView) convertView.findViewById(R.id.tv_create_at);
                holder.tv_weibo_text_comment = (TextView) convertView.findViewById(R.id.tv_weibo_text_comment);
                convertView.setTag(holder);
            } else {

                holder = (ViewHolder) convertView.getTag();
            }

            utils.display(holder.iv_user_icon, commentArrayList.get(position).user.avatar_large);
            holder.tv_user_name.setText(commentArrayList.get(position).user.screen_name);

            String str = commentArrayList.get(position).created_at;
            String time = getTime(str);

            holder.tv_create_at.setText(time);
            String str2 = commentArrayList.get(position).text;
            SpannableString text = WeiboTextUtils.setTextHighLight(str2,getApplicationContext());
            holder.tv_weibo_text_comment.setText(text);

            return convertView;
        }


    }
    static   class ViewHolder {

        CircleImageView iv_user_icon;
        TextView tv_user_name;
        TextView tv_create_at;
        TextView tv_weibo_text_comment;
    }
}
