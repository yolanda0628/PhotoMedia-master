package com.pbq.pickerlib.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by pengbangqin on 16-08-21.
 * 存放图片和视频的实体
 */
public class PhotoVideoDir {

    /**
     * 定义一个枚举类型，将相关常量放入，其它地方能够直接取到
     */
    public enum Type {
        IMAGE, VEDIO, AUDIO
    }

    /**
     * 路径名
     */
    public String dirName;
    /**
     * 路径地址
     */
    public String dirPath;
    /**
     * 图片或视频文件的集合
     */
    public List<String> files = new ArrayList<String>();
    /**
     * 已选图片或视频文件的集合
     */
    public HashSet<String> selectedFiles = new HashSet<String>();

    /**
     * 存储每个文件夹的图片路径或文件的视频路径
     */
    public String firstPath;
    /**
     * 类型为图片类型
     */
    public Type type= Type.IMAGE;

    public PhotoVideoDir(String dirPath) {
        super();
        this.dirPath = dirPath;
    }

    public void addFile(String file) {
        files.add(file);
    }

    public String getDirName() {
        return dirName;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
