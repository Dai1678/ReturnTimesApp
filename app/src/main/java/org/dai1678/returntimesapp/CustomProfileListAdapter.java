package org.dai1678.returntimesapp;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomProfileListAdapter extends ArrayAdapter<CustomProfileListItem> {

    private int resource;
    private List<CustomProfileListItem> items;
    private LayoutInflater layoutInflater;

    public CustomProfileListAdapter(Context context, int resource, List<CustomProfileListItem> items) {
        super(context, resource, items);

        this.resource = resource;
        this.items = items;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private static class ViewHolder{
        public ImageView thumbnail;
        public TextView textView;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent){
        ViewHolder holder;

        if(convertView == null){
            convertView = this.layoutInflater.inflate(this.resource,null);
            holder = new ViewHolder();
            holder.thumbnail = (ImageView)convertView.findViewById(R.id.profileItemImage);
            holder.textView = (TextView)convertView.findViewById(R.id.textProfile);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        CustomProfileListItem item = this.items.get(position);

        holder.thumbnail.setImageBitmap(item.getProfileItemImage());

        holder.textView.setText(item.getProfileHint());

        return convertView;
    }
}
