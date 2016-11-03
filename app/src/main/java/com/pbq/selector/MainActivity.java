package com.pbq.selector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pbq.pickerlib.activity.PhotoMediaActivity;
import com.pbq.pickerlib.entity.PhotoVideoDir;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    /**
     * 存储图像路径的集合
     */
    private ArrayList<String> selectedImagesPaths = new ArrayList<String>();
    /**
     * 存储视频路径的集合
     */
    private ArrayList<String> selectedVedioPaths = new ArrayList<String>();
    private ArrayList<File> files = new ArrayList<File>();
    /**
     * 常量，标识相册选择请求码
     */
    private static final int REQUEST_CODE_GET_PHOTOS = 1000;
    /**
     * 常量，标识录像选择请求码
     */
    private static final int REQUEST_CODE_GET_VEDIOS = 2000;
    private ExpandGridView gv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gv = (ExpandGridView) findViewById(R.id.gv);
    }
    /**
     * 照片选择
     *
     * @param view
     */
    public void choosePhoto(View view) {
        Intent i = new Intent(this, PhotoMediaActivity.class);
        //若传入已选中的路径则在选择页面会呈现选中状态
        i.putStringArrayListExtra("pickerPaths", selectedImagesPaths);
        startActivityForResult(i, REQUEST_CODE_GET_PHOTOS);
    }
    /**
     * 音频选择
     *
     * @param view
     */
    public void chooseAudio(View view) {
        Toast.makeText(getApplicationContext(),"后期加入选择音频.....",Toast.LENGTH_SHORT).show();
    }
    /**
     * 视频选择
     *
     * @param view
     */
    public void chooseVideo(View view) {
        Intent i = new Intent(this, PhotoMediaActivity.class);
        //若传入已选中的路径则在选择页面会呈现选中状态
        i.putStringArrayListExtra("pickerPaths", selectedVedioPaths);
        i.putExtra("loadType", PhotoVideoDir.Type.VEDIO.toString());
        startActivityForResult(i, REQUEST_CODE_GET_VEDIOS);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            //照相选择返回事件
            case REQUEST_CODE_GET_PHOTOS:
                if (resultCode == RESULT_OK) {
                    //取出选择的相片路径
                    selectedImagesPaths = data.getStringArrayListExtra("pickerPaths");
                    MyAdapter adapter = new MyAdapter(selectedImagesPaths);
                    gv.setAdapter(adapter);
                    //将选择的图片路径放入文件中
                    //清空文件
                    files.clear();
                    for (int i=0;i<selectedImagesPaths.size();i++){
                        File fileImage=new File(selectedImagesPaths.get(i));
                        files.add(fileImage);
                        Log.i("TGA", selectedImagesPaths.get(i));
                        Log.i("TGA", fileImage+"");
                    }
                    //上传
                    Toast.makeText(getApplicationContext(),selectedImagesPaths+"",Toast.LENGTH_SHORT).show();
                }
                break;
            //录像选择返回事件
            case REQUEST_CODE_GET_VEDIOS:
                if (resultCode == RESULT_OK) {
                    selectedVedioPaths = data.getStringArrayListExtra("pickerPaths");
                    MyAdapter adapter = new MyAdapter(selectedVedioPaths);
                    gv.setAdapter(adapter);
                    //将选择的视频路径放入文件中
                    //清空视频文件
                    files.clear();
                    for (int i=0;i<selectedVedioPaths.size();i++){
                        File fileVedio=new File(selectedVedioPaths.get(i));
                        files.add(fileVedio);
                        Log.i("TGA", selectedVedioPaths.get(i));
                        Log.i("TGA", fileVedio+"");
                    }
                    //上传
                    Toast.makeText(getApplicationContext(),selectedVedioPaths+"",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private class MyAdapter extends BaseAdapter {

        private List<String> items;

        public MyAdapter(List<String> items) {
            this.items = items;
        }

        public void setData(List<String> items) {
            this.items = items;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public String getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int size = gv.getWidth() / 3;
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(MainActivity.this, R.layout.item, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //把选中图片的资源放在imageView中
            Glide.with(MainActivity.this).load(getItem(position)).placeholder(R.mipmap.default_image).into(holder.imageView);
            return convertView;
        }
    }

    private class ViewHolder {

        private ImageView imageView;

        public ViewHolder(View convertView) {
            imageView = (ImageView) convertView.findViewById(R.id.imageView);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, gv.getWidth() / 3);
            imageView.setLayoutParams(params);
        }
    }
}
