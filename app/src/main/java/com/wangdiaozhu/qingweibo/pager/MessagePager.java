package com.wangdiaozhu.qingweibo.pager;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.wangdiaozhu.qingweibo.R;
import com.wangdiaozhu.qingweibo.activity.MainActivity;
import com.wangdiaozhu.qingweibo.base.BasePager;
import com.wangdiaozhu.qingweibo.bean.UserGet;
import com.wangdiaozhu.qingweibo.fragment.ContentFragment;
import com.wangdiaozhu.qingweibo.view.CircleImageView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/5.
 */
public class MessagePager extends BasePager {

    MainActivity mainActivity;
    ContentFragment fragment;

   private UserGet userGet1;
    private UserGet userGet2;
    private UserGet userGet3;
  private ArrayList<UserGet> userGets;
    public MessagePager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        super.setTitleMiddleText("消息");
        super.showTitleLeftText("发现群");
        super.showTitleRightBtn(R.drawable.selector_navbar_home_right);
        View mMessagePager = View.inflate(mActivity, R.layout.pager_message, null);

        ListView lv_message = (ListView) mMessagePager.findViewById(R.id.lv_message);
        fl_content.addView(mMessagePager);
        mainActivity = (MainActivity) mActivity;
        fragment = mainActivity.getContentFragment();
        userGets = new ArrayList<UserGet>();
        userGet1 = new UserGet();
        userGet1.text = " @我的";
        userGet1.imgIds = R.drawable.messagescenter_at;
        userGets.add(userGet1);
        userGet2 = new UserGet();
        userGet2.text = "  评论";
        userGet2.imgIds = R.drawable.messagescenter_comments;
        userGets.add(userGet2);
        userGet3 = new UserGet();
        userGet3.text = "  赞";
        userGet3.imgIds = R.drawable.messagescenter_good;
        userGets.add(userGet3);

        MessageCenterAdapter mAdapter = new MessageCenterAdapter();
        lv_message.setAdapter(mAdapter);


    }

    class MessageCenterAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            return userGets.size();
        }

        @Override
        public Object getItem(int position) {
            return userGets.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

            if(convertView == null){

                convertView = View.inflate(mActivity,R.layout.list_item_message_header,null);
                holder = new ViewHolder();
                holder.clv_icon = (CircleImageView) convertView.findViewById(R.id.clv_icon);
                holder.message_item_text = (TextView) convertView.findViewById(R.id.message_item_text);
                convertView.setTag(holder);
            }else {

             holder  = (ViewHolder) convertView.getTag();

            }

            holder.clv_icon.setImageResource(userGets.get(position).imgIds);
            holder.message_item_text.setText(userGets.get(position).text);


            return convertView;
        }
    }


    static class  ViewHolder{

        CircleImageView clv_icon;
        TextView message_item_text;


    }


}
