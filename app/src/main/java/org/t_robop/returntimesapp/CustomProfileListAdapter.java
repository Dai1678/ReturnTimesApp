package org.t_robop.returntimesapp;


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

    private int mResource;
    private List<CustomProfileListItem> mItems;
    private LayoutInflater mInflater;

    public CustomProfileListAdapter(Context context, int resource, List<CustomProfileListItem> items) {
        super(context, resource, items);

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
        CustomProfileListItem item = mItems.get(position);

        //TODO ViewHolderを使えば処理の重いfindViewByIdを使わずに済む
        ImageView thumbnail = (ImageView)view.findViewById(R.id.profileItemImage);
        thumbnail.setImageBitmap(item.getProfileItemImage());

        TextView textView = (TextView)view.findViewById(R.id.textProfile);
        textView.setText(item.getProfileHint());

        return view;
    }
}
