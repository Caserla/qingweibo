package com.wangdiaozhu.qingweibo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.wangdiaozhu.qingweibo.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/5/26.
 */
public class RefreshListView extends ListView implements AbsListView.OnScrollListener,AdapterView.OnItemClickListener{

    private  static  final int STATE_PULL_TO_REFRESH = 1;
    private  static  final int STATE_RELEASE_TO_REFRESH = 2;
    private  static  final int STATE_REFRESHING_TO_REFRESH = 3;
    private View mHeaderView;
    private int mHeaderViewHeight;
    private int startY = -1;
    private int endY;
    private int mCurrentState = STATE_PULL_TO_REFRESH;
    private TextView tvTitle;
    private ImageView iv_arrow;
    private ProgressBar pb_loading;
    private RotateAnimation animUp;
    private RotateAnimation animDown;
    private TextView tv_time;
    private View mFooterView;
    private int mFooterViewHeight;
    private  boolean isLoadingMore;


    public RefreshListView(Context context) {
        super(context);
        initHeaderView();
        initFooterView();
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
        initFooterView();
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
        initFooterView();
    }

   public   void initHeaderView(){

       mHeaderView = View.inflate(getContext(), R.layout.list_refresh_header, null);

       this.addHeaderView(mHeaderView);


       mHeaderView.measure(0, 0);
       mHeaderViewHeight = mHeaderView.getMeasuredHeight();

              mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
       tvTitle = (TextView) mHeaderView.findViewById(R.id.tv_title);

       iv_arrow = (ImageView) mHeaderView.findViewById(R.id.iv_arrow);

       pb_loading = (ProgressBar) mHeaderView.findViewById(R.id.pb_loading);

       tv_time = (TextView) mHeaderView.findViewById(R.id.tv_time);

       initAnim();
       setCurrentTime();
   }

    private   void initAnim(){

        animUp = new RotateAnimation(0,-180, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animUp.setDuration(500);
      animUp.setFillAfter(true);

        animDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animDown.setDuration(500);
        animDown.setFillAfter(true);

    }

    public void initFooterView(){

        mFooterView = View.inflate(getContext(), R.layout.list_refresh_footer, null);
        this.addFooterView(mFooterView);
            ProgressBar footer_pb_loading = (ProgressBar) mFooterView.findViewById(R.id.pb_loading);
           TextView footer_tv_title = (TextView) mFooterView.findViewById(R.id.tv_title);

               mFooterView.measure(0, 0);
        mFooterViewHeight = mFooterView.getMeasuredHeight();

        mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);
        this.setOnScrollListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()){

            case MotionEvent.ACTION_DOWN:

                startY = (int) ev.getY();

                break;

            case MotionEvent.ACTION_MOVE:
                if(startY == -1){

                    startY = (int) ev.getY();

                }

                if(mCurrentState ==STATE_REFRESHING_TO_REFRESH){

                    break;


                }

                endY = (int) ev.getY();

                int dy = endY - startY;

                if(dy>0&& getFirstVisiblePosition() ==0){

                    int paddingTop = dy - mHeaderViewHeight;

                    if(paddingTop>=0 && mCurrentState != STATE_REFRESHING_TO_REFRESH){

                        mCurrentState = STATE_RELEASE_TO_REFRESH;
                        refreshState();
                    }else if(paddingTop<0 && mCurrentState != STATE_PULL_TO_REFRESH){

                        mCurrentState = STATE_PULL_TO_REFRESH;
                             refreshState();

                    }
                    mHeaderView.setPadding(0,paddingTop,0,0);
                 return true;
                }
                break;
            case MotionEvent.ACTION_UP:

                startY = -1;
                if(mCurrentState == STATE_RELEASE_TO_REFRESH){

                    mCurrentState =STATE_REFRESHING_TO_REFRESH;
                    mHeaderView.setPadding(0,0,0,0);
                    refreshState();

                    if(mListener != null){

                        mListener.OnRefresh();

                    }
                }else  if(mCurrentState == STATE_PULL_TO_REFRESH){

                    mHeaderView.setPadding(0,-mHeaderViewHeight,0,0);

                }
                break;
        }

        return super.onTouchEvent(ev);
    }

    private void refreshState(){

        switch (mCurrentState){

            case STATE_PULL_TO_REFRESH:
                   tvTitle.setText("下拉刷新");
                iv_arrow.startAnimation(animDown);
                pb_loading.setVisibility(View.INVISIBLE);
                iv_arrow.setVisibility(View.VISIBLE);
                break;
            case STATE_REFRESHING_TO_REFRESH:
                tvTitle.setText("正在刷新");
                pb_loading.setVisibility(View.VISIBLE);
                iv_arrow.clearAnimation();
                iv_arrow.setVisibility(View.INVISIBLE);

                break;
            case STATE_RELEASE_TO_REFRESH:
                tvTitle.setText("释放刷新");
               iv_arrow.startAnimation(animUp);

                pb_loading.setVisibility(View.INVISIBLE);
                break;
        }


    }

    private OnRefreshListener mListener;

    public void  setOnRefreshListener(OnRefreshListener  listener){

        mListener = listener;

    }

    private void setCurrentTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     String time =   format.format(new Date());
          tv_time.setText(time);


    }

    public void OnRefreshComplete( boolean success){

        if(!isLoadingMore) {
            mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
            mCurrentState = STATE_PULL_TO_REFRESH;
            pb_loading.setVisibility(View.INVISIBLE);
            iv_arrow.setVisibility(View.VISIBLE);
            tvTitle.setText("下拉刷新");
            if (success) {
                setCurrentTime();
            }
        }
         else {

             mFooterView.setPadding(0,-mFooterViewHeight,0,0);
               isLoadingMore = false;
         }

    }



    public interface  OnRefreshListener{


        public void OnRefresh();

        public  void loadMore();

    }
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        if(scrollState == SCROLL_STATE_IDLE){
         int lastVisiblePosition = getLastVisiblePosition();
          if(lastVisiblePosition >=getCount()-1 && !isLoadingMore ){

       isLoadingMore = true;
              mFooterView.setPadding(0,0,0,0);
          setSelection(getCount()-1);

              if(mListener != null){

                  mListener.loadMore();

              }
          }

        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }


    private OnItemClickListener mItemClickListener;

    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
        super.setOnItemClickListener(this);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if(mItemClickListener !=null){

           mItemClickListener.onItemClick(parent,view,position-getHeaderViewsCount(),id);

        }
    }
}
