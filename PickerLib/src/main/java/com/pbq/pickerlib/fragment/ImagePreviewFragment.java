package com.pbq.pickerlib.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pbq.pickerlib.R;

import java.util.HashSet;
import java.util.List;

/**
 * Created by pengbangqin on 16-10-26.
 */
public class ImagePreviewFragment extends Fragment {
    private View rootView;
    private ViewPager viewPager;
    private TextView tvPage;

    List<String> paths;
    int currentPosition;
    HashSet<String> selectedPaths = new HashSet<String>();
    onImageSelectChangedListener imageSelectedListener;
    int maxCount = 3;
    int otherCount = 0;

    public interface onImageSelectChangedListener {
        public void onIamgeSelectChanged(HashSet<String> paths);

        public void onPageChanged(int index, int total);
    }

    public onImageSelectChangedListener getImageSelectedListener() {
        return imageSelectedListener;
    }

    public void setImageSelectedListener(onImageSelectChangedListener imageSelectedListener) {
        this.imageSelectedListener = imageSelectedListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_preview, null);
        initData();
        initView();
        return rootView;
    }

    private void initData() {
        Bundle bundle = getArguments();
        paths = bundle.getStringArrayList("paths");
        maxCount = bundle.getInt("maxCount");
        otherCount = bundle.getInt("otherCount");
        String currentPath = bundle.getString("currentPath");
        String[] selectedPath = bundle.getStringArray("selectedPaths");

        for (int i = 0; i < selectedPath.length; i++) {
            selectedPaths.add(selectedPath[i]);
        }

        if (currentPath == null) {
            currentPosition = 0;
        } else {
            currentPosition = paths.indexOf(currentPath);
        }
    }

    private void initView() {
        viewPager = (ViewPager) rootView.findViewById(R.id.viewPager_image_switcher);
        tvPage = (TextView) rootView.findViewById(R.id.tv_currentPage);
        initViewPager();
        viewPager.setCurrentItem(currentPosition);
    }

    private void initViewPager() {
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public boolean isViewFromObject(View view, Object obj) {
                return view == obj;
            }

            @Override
            public int getCount() {
                return paths.size();
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                final String path = paths.get(position);
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                View view = inflater.inflate(R.layout.item_viewpager, null);
                ImageView imageView = (ImageView) view.findViewById(R.id.iv_image);
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.cb_isSelected);
                checkBox.setChecked(selectedPaths.contains(paths.get(position)));

                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            if (selectedPaths.size() + otherCount < maxCount) {
                                selectedPaths.add(path);
                            } else {
                                buttonView.setChecked(false);
                                Toast.makeText(getActivity(), "选择张数不能大于" + maxCount, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            selectedPaths.remove(path);
                        }
                        imageSelectedListener.onIamgeSelectChanged(selectedPaths);
                    }
                });

                Glide.with(getActivity())
                        .load(paths.get(position))
                        .placeholder(R.mipmap.default_image)
                        .into(imageView);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                // views.remove(position);
                container.removeView((View) object);
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int pos) {
                imageSelectedListener.onPageChanged(pos + 1, paths.size());
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }
}
