PhotoMedia-master
====
PhotoMedia-master:android自定义图册,完美解决:
--
####(1)手机中对图片和视频拍照和录像的功能.
####(2)实现了图片和视频单选和多选功能.
####(3)多选限制数量.
####(4)使用Glide完美加载图片.
#####该项目参考了:

* https://github.com/jeasonlzy/ImagePicker
* 后期会根据需求去更新项目。
* 喜欢原作的可以去使用。同时欢迎大家下载体验本项目，如果使用过程中遇到什么问题，欢迎反馈。

1.用法
----

使用前,对于android studio用户:<br>

(1)	下载[PickerLib.aar](http://download.csdn.net/detail/kobe_peng/9663683) 资源,并导入项目的lib文件夹下.

(2)	在build.grade中加入
  
  ```java
  repositories {
  flatDir {
  dirs 'libs'
  }
  }
  ```
  
(3)	在你需要使用的module中添加<br>

```java
  compile 'com.github.bumptech.glide:glide:3.6.1'//图片加载的依赖
  compile(name:'PickerLib', ext:'aar')  //依赖aar文件
  ```
  
当然,你也可以直接依赖library项目:<br>

```java
  compile project(':PickerLib')
  ```
2.代码参考
------
###(1)在Activity中直接使用图片选择和视频选择
```java
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
     * 视频选择
     *
     * @param view
     */
    public void chooseVideo(View view) {
        Intent i = new Intent(this, PhotoMediaActivity.class);
        //若传入已选中的路径则在选择页面会呈现选中状态
        i.putStringArrayListExtra("pickerPaths", selectedVedioPaths);
        i.putExtra("loadType", PhotoVideoDir.Type.VEDIO.toString());
        //可以不用传
        //i.putExtra("sizeLimit", 1 * 1024 * 1024);
        startActivityForResult(i, REQUEST_CODE_GET_VEDIOS);
    }
```

###(2)重写onActivityResult方法,回调结果
```java
   @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
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
        Toast.makeText(getApplicationContext(),selectedImagesPaths+"",Toast.LENGTH_SHORT).show();
                }
                Break;
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
```
3.演示
------
![image](https://github.com/pbq18785109289/PhotoMedia-master/raw/master/logos/p1.jpg)![image](https://github.com/pbq18785109289/PhotoMedia-master/raw/master/logos/p2.jpg)
 ![image](https://github.com/pbq18785109289/PhotoMedia-master/raw/master/logos/p3.jpg)![image](https://github.com/pbq18785109289/PhotoMedia-master/raw/master/logos/p4.jpg)
联系方式
------
* 邮箱地址： 870931145@qq.com
* QQ： 870931145
* 个人能力有限，希望大家一起学习一起进步,非常感谢!


