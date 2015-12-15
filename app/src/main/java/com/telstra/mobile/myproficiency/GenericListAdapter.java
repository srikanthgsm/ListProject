package com.telstra.mobile.myproficiency;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.ArrayList;

public class GenericListAdapter extends BaseAdapter {

    LayoutInflater inflater;
    Activity context;
    ArrayList<JSONResponseModel> listItem;
    GenericListAdapter(Activity context, ArrayList<JSONResponseModel> itemList) {
        this.context = context;
        listItem=itemList;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {

        return listItem.size();
    }

    @Override
    public Object getItem(int position) {
        return listItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    class ViewHolder {
        TextView title, desc;
        ImageView imageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            assert convertView != null;
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.desc = (TextView) convertView.findViewById(R.id.desc);
            holder.imageView = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(listItem.get(position).title);
        holder.desc.setText(listItem.get(position).description);
        UrlImageViewHelper.setUrlDrawable(holder.imageView,
                listItem.get(position).imageHref, R.color.material_grey_100,
                new UrlImageViewCallback() {
                    @Override
                    public void onLoaded(ImageView loadedImage,
                                         Bitmap loadedBitmap, String url,
                                         boolean loadedFromCache) {
                        if (!loadedFromCache) {
                            ScaleAnimation scale = new ScaleAnimation(
                                    0,
                                    1,
                                    0,
                                    1,
                                    ScaleAnimation.RELATIVE_TO_SELF,
                                    .5f,
                                    ScaleAnimation.RELATIVE_TO_SELF,
                                    .5f);
                            scale.setDuration(300);
                            scale.setInterpolator(new OvershootInterpolator());
                            loadedImage.startAnimation(scale);
                        }
                    }
                }
        );
        return convertView;
    }
}
