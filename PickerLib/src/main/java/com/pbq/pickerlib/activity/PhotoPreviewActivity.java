package com.pbq.pickerlib.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pbq.pickerlib.R;
import com.pbq.pickerlib.fragment.ImagePreviewFragment;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by pengbangqin on 16-10-26.
 */
public class PhotoPreviewActivity extends AppCompatActivity {
    /**
     * 选择的文件路径
     */
    private ArrayList<String> paths;
    /**
     * 最大选择数
     */
    private int maxCount;
    /**
     * 剩余选择数
     */
    private int otherCount;
    /**
     * 已选择的路径
     */
    private HashSet<String> selectPaths;
    /**
     * 当前位置
     */
    private int currentPosition;
    FrameLayout mFrameLayout;
    TextView mTvTitle;
    Button mBtnNext;
    ImagePreviewFragment fragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_activity);
        selectPaths = new HashSet<String>();
        //接收从PhotoMediaActivity传来的值
        Bundle bundle=getIntent().getBundleExtra("params");
        paths=bundle.getStringArrayList("paths");
        String currentPath=bundle.getString("currentPath");
        String[] selectedPath=bundle.getStringArray("selectedPaths");
        maxCount=bundle.getInt("maxCount");
        otherCount=bundle.getInt("otherCount");
        for(int i=0;i<selectedPath.length;i++){
            selectPaths.add(selectedPath[i]);
        }
        if(currentPath==null){
            currentPosition=0;
        }else{
            currentPosition=paths.indexOf(currentPath);
        }
        initView();
    }

    /**
     * 绑定控件
     */
    private void initView() {
        mFrameLayout=(FrameLayout) findViewById(R.id.img_preview);
        mTvTitle=(TextView) findViewById(R.id.tv_top_bar_title);
        mBtnNext=(Button) findViewById(R.id.btn_next);
        fragment=new ImagePreviewFragment();
        Bundle bundle=getIntent().getBundleExtra("params");
        fragment.setArguments(bundle);
        fragment.setImageSelectedListener(new ImagePreviewFragment.onImageSelectChangedListener() {

            @Override
            public void onIamgeSelectChanged(HashSet<String> paths) {
                selectPaths=paths;
                updateNextButton();
            }

            @Override
            public void onPageChanged(int index, int total) {
                mTvTitle.setText(index+"/"+total);
            }
        });
        FragmentTransaction trans=getSupportFragmentManager().beginTransaction();
        trans.add(R.id.img_preview, fragment);
        trans.commit();
        updateNextButton();
    }

    public void  updateNextButton(){
        if(selectPaths.size()+otherCount>0){
            mBtnNext.setSelected(true);
            mBtnNext.setText("下一步("+(selectPaths.size()+otherCount)+")");
            mBtnNext.setTextColor(Color.WHITE);
        }else{
            mBtnNext.setSelected(false);
            mBtnNext.setText("下一步");
            mBtnNext.setTextColor(Color.BLACK);
        }
    }
    //返回
    public void goBack(View view){
        Intent intent=new Intent();
        intent.putExtra("pickerPaths", selectPaths.toArray(new String[]{}));
        setResult(RESULT_OK, intent);
        finish();
    }
    /**
     * 下一步按钮
     * @param view
     */
    public void goNext(View view){
        Intent intent=new Intent();
        intent.putExtra("pickerPaths", selectPaths.toArray(new String[]{}));
        setResult(RESULT_OK, intent);
        finish();
    }
}
