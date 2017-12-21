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

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent){
        View view;

        if(convertView != null){
            view = convertView;
        }else{
            view = this.layoutInflater.inflate(this.resource,null);
        }
        CustomHomeListItem item = this.items.get(position);

        //TODO ViewHolderを使えば処理の重いfindViewByIdを使わずに済む
        //画像のセット
        ImageView thumbnail = (ImageView)view.findViewById(R.id.destinationImage);
        thumbnail.setImageBitmap(item.getThumbnail());

        //宛先名のセット
        TextView addressName = (TextView)view.findViewById(R.id.addressName);
        addressName.setText(item.getAddressName());

        //行き先名のセット
        TextView destination = (TextView)view.findViewById(R.id.destination);
        destination.setText(item.getDestination());

        //メアドのセット
        TextView addressMail = (TextView)view.findViewById(R.id.addressMail);
        addressMail.setText(item.getAddressMail());

        return view;
    }
}
