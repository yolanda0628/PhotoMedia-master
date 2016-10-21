package com.pbq.selector;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.pbq.pickerlib.PhotoMediaActivity;
import com.pbq.pickerlib.entity.PhotoVideoDir;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    /**
     * 上传的文件集合
     */
    private ArrayList<File> files;
    /**
     * 上传的参数
     */
    private Map<String, String> params;

    /**
     * 存储图像路径的集合
     */
    private ArrayList<String> selectedImagesPaths = new ArrayList<String>();
    /**
     * 存储视频路径的集合
     */
    private ArrayList<String> selectedVedioPaths = new ArrayList<String>();
    /**
     * 常量，标识拍照请求码
     */
    private static final int REQUEST_CODE_TAKE_PHOTOS = 1;
    /**
     * 常量，标识相册选择请求码
     */
    private static final int REQUEST_CODE_GET_PHOTOS = 1000;
    /**
     * 常量，标识录像请求码
     */
    private static final int REQUEST_CODE_TAKE_VEDIOS = 2;
    /**
     * 常量，标识录像选择请求码
     */
    private static final int REQUEST_CODE_GET_VEDIOS = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        files=new ArrayList<File>();
        params=new HashMap<String, String>();
    }

    /**
     * 拍照
     *
     * @param view
     */
    public void takePhoto(View view) {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //采用ForResult打开
        startActivityForResult(i, REQUEST_CODE_TAKE_PHOTOS);
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
     * 录像
     *
     * @param view
     */
    public void takeVideo(View view) {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        //设置录像的质量
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, REQUEST_CODE_TAKE_VEDIOS);
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
        i.putExtra("sizeLimit", 1 * 1024 * 1024);
        startActivityForResult(i, REQUEST_CODE_GET_VEDIOS);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            //照相返回事件
            case REQUEST_CODE_TAKE_PHOTOS:
                if (resultCode == RESULT_OK) {
                    files.clear();
                    if (data != null) {
                        Bundle extras = data.getExtras();
                        Bitmap bitmap = extras.getParcelable("data");
                        if(bitmap!=null){
                            String filePath=saveImageToGallery(MainActivity.this,bitmap);//保存在SD卡中
                            File file=new File(filePath);
                            files.add(file);
                        }
                    }
                }
                break;
            //照相选择返回事件
            case REQUEST_CODE_GET_PHOTOS:
                if (resultCode == RESULT_OK) {
                    //取出选择的相片路径
                    selectedImagesPaths = data.getStringArrayListExtra("pickerPaths");
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
            //录像返回事件
            case REQUEST_CODE_TAKE_VEDIOS:
                if (resultCode == RESULT_OK) {
                    //清除文件集合
                    files.clear();
                    Uri uriVideo = data.getData();
                    Cursor cursor=this.getContentResolver().query(uriVideo, null, null, null, null);
                    if (cursor.moveToNext()) {
					/* _data：文件的绝对路径 ，_display_name：文件名 */
                        String strVideoPath = cursor.getString(cursor.getColumnIndex("_data"));
                        Toast.makeText(this, strVideoPath, Toast.LENGTH_SHORT).show();
                        File file=new File(strVideoPath);
                        files.add(file);
                    }
                    /**
                     * 上传代码
                     */
                }
                break;
            //录像选择返回事件
            case REQUEST_CODE_GET_VEDIOS:
                if (resultCode == RESULT_OK) {
                    selectedVedioPaths = data.getStringArrayListExtra("pickerPaths");
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

    /**
     * 保存文件到图册中
     * @param context
     * @param bmp
     */
    public static String saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "DCIM");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
        // 最后通知图库更新
//		context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
    }
}
