package com.seta.common.views.recyclerViewEx;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.seta.setabelt.R;

public class LoadingMoreFooter extends LinearLayout {

    private View loading_view_layout;
    private TextView end_layout;
    private TextView errot_layout;
    private ImageView mIvLoading;


    public LoadingMoreFooter(Context context) {
        super(context);
        initView(context);
    }

    public void initView(Context context) {
        setGravity(Gravity.CENTER);
        setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        View view = LayoutInflater.from(context).inflate(R.layout.layout_footer, (ViewGroup) getRootView(), false);
        loading_view_layout = view.findViewById(R.id.footer_loading);
        end_layout = (TextView) view.findViewById(R.id.footer_end);
        errot_layout = (TextView) view.findViewById(R.id.footer_error);
        mIvLoading = (ImageView) view.findViewById(R.id.iv_loading);
        addView(view);
        Glide.with(getContext())
                .load(R.mipmap.footer_loading)
//                .asGif()
                .into(mIvLoading);
    }

    //设置已经没有更多数据
    public void setEnd() {
        setVisibility(VISIBLE);
        loading_view_layout.setVisibility(GONE);
        errot_layout.setVisibility(GONE);
        end_layout.setVisibility(VISIBLE);
    }

    public void setLoading() {
        setVisibility(VISIBLE);
        loading_view_layout.setVisibility(VISIBLE);
        errot_layout.setVisibility(GONE);
        end_layout.setVisibility(GONE);
    }

    public void setError() {
        setVisibility(VISIBLE);
        loading_view_layout.setVisibility(GONE);
        errot_layout.setVisibility(VISIBLE);
        end_layout.setVisibility(GONE);
    }

    public void setVisible() {
        setVisibility(VISIBLE);
        loading_view_layout.setVisibility(VISIBLE);
        end_layout.setVisibility(GONE);
        errot_layout.setVisibility(GONE);
    }


    public void setGone() {
        setVisibility(GONE);
    }


}