package org.dai1678.returntimesapp;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;

import java.util.List;
import java.util.concurrent.TimeoutException;

class CustomHomeListAdapter extends ArrayAdapter<CustomHomeListItem> {

    private int resource;
    private List<CustomHomeListItem> items;
    private LayoutInflater layoutInflater;

    //コンストラクタ
    CustomHomeListAdapter(Context context, int resource, List<CustomHomeListItem> items) {
        super(context, resource,items);

        this.resource = resource;
        this.items = items;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private static class ViewHolder{
        public ImageView thumbnail;
        public TextView addressName;
        public TextView destination;
        public TextView addressMail;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent){
        ViewHolder holder;

        if(convertView == null){
            convertView = this.layoutInflater.inflate(this.resource,null);
            holder = new ViewHolder();
            holder.thumbnail = (ImageView) convertView.findViewById(R.id.destinationImage);
            holder.addressName = (TextView)convertView.findViewById(R.id.addressName);
            holder.destination = (TextView)convertView.findViewById(R.id.destination);
            holder.addressMail = (TextView)convertView.findViewById(R.id.addressMail);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        CustomHomeListItem item = this.items.get(position);

        //画像のセット
        holder.thumbnail.setImageBitmap(item.getThumbnail());

        //宛先名のセット
        holder.addressName.setText(item.getAddressName());

        //行き先名のセット
        holder.destination.setText(item.getDestination());

        //メアドのセット
        holder.addressMail.setText(item.getAddressMail());

        return convertView;
    }
}
