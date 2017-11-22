package org.t_robop.returntimesapp;


import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

class CustomHomeListAdapter extends ArrayAdapter<CustomHomeListItem> {

    private int mResource;
    private List<CustomHomeListItem> mItems;
    private LayoutInflater mInflater;

    CustomHomeListAdapter(Context context, int resource, List<CustomHomeListItem> items) {
        super(context, resource,items);

        mResource = resource;
        mItems = items;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent){
        View view;

        if(convertView != null){
            view = convertView;
        }else{
            view = mInflater.inflate(mResource,null);
        }
        Log.d("test",view.toString());
        CustomHomeListItem item = mItems.get(position);

        ImageView thumbnail = (ImageView)view.findViewById(R.id.thumbnail);
        Log.d("test",thumbnail.toString());
        thumbnail.setImageBitmap(item.getThumbnail());

        TextView destination = (TextView)view.findViewById(R.id.destination);
        destination.setText(item.getDestination());

        TextView address = (TextView)view.findViewById(R.id.address);
        address.setText(item.getAddress());

        return view;
    }
}
