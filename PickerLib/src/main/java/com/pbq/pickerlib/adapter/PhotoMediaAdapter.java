package com.pbq.pickerlib.adapter;


import android.app.Activity;
import android.content.Context;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.bumptech.glide.Glide;
import com.pbq.pickerlib.R;
import com.pbq.pickerlib.entity.PhotoVideoDir;

/**
 * 图片显示适配器
 */
public class PhotoMediaAdapter extends BaseAdapter {
    /**
     * 图片路径实体
     */
	PhotoVideoDir imageDir;
    /**
     * 上下文
     */
	Context context;
    /**
     * 反射器
     */
	LayoutInflater inflator;
    /**
     * 点击事件监听
     */
	onItemCheckedChangedListener itemCheckListener;
    //定义选择事件接口
	public interface onItemCheckedChangedListener {
		public void onItemCheckChanged(CompoundButton chBox, boolean isCheced, PhotoVideoDir iamgeDir, String path);
        //照相
		public void onTakePicture(PhotoVideoDir imageDir);
        //显示图片
		public void onShowPicture(String path);
	}

	public void setOnItemCheckdedChangedListener(onItemCheckedChangedListener listener) {
		this.itemCheckListener = listener;
	}

	public PhotoMediaAdapter(Activity context, PhotoVideoDir imageDir) {
		this.imageDir = imageDir;
		this.context = context;
		this.inflator = LayoutInflater.from(context);
	}

    //加1  因为第一个显示的是相机
	@Override
	public int getCount() {
		return imageDir.getFiles().size() + 1;
	}

	@Override
	public String getItem(int position) {
		return imageDir.getFiles().get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHodler viewHolder;
		if (convertView == null) {
			convertView = inflator.inflate(R.layout.grid_item_photo, null);
			viewHolder = new ViewHodler();
			viewHolder.chSelect = (CheckBox) convertView.findViewById(R.id.ch_photo_select);
			viewHolder.photoView = (ImageView) convertView.findViewById(R.id.img_photo);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHodler) convertView.getTag();
		}

		if (position == 0) {
            //第一个显示相机
			viewHolder.photoView.setImageResource(R.mipmap.grid_camera);
			viewHolder.photoView.setScaleType(ScaleType.CENTER_INSIDE);
			viewHolder.chSelect.setVisibility(View.GONE);
		} else {
			viewHolder.photoView.setScaleType(ScaleType.CENTER_CROP);
			viewHolder.chSelect.setVisibility(View.VISIBLE);
			viewHolder.chSelect.setTag(position - 1);
			String path = getItem(position - 1);
			viewHolder.chSelect.setOnCheckedChangeListener(null);
			viewHolder.chSelect.setChecked(imageDir.selectedFiles.contains(path));
            //复选框的选择事件，在PhotoSelectorActivity中实现
			viewHolder.chSelect.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					itemCheckListener.onItemCheckChanged(buttonView, isChecked, imageDir, getItem(position - 1));
				}
			});

			if (imageDir.getType() == PhotoVideoDir.Type.VEDIO) {
				viewHolder.photoView.setImageBitmap(ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MINI_KIND));
			} else {
                //减1 因为第一个显示的是相机  getItem(position - 1)表示当前位置显示的图片路径
				Glide.with(context).load(getItem(position - 1)).placeholder(R.mipmap.default_image).into(viewHolder.photoView);
            }

		}

		viewHolder.photoView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (position != 0) {
					itemCheckListener.onShowPicture(getItem(position - 1));
				} else {
					itemCheckListener.onTakePicture(imageDir);
				}
			}
		});
		return convertView;
	}

	public static class ViewHodler {
		ImageView photoView;
		CheckBox chSelect;
	}

}
