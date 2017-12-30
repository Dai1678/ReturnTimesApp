package org.dai1678.returntimesapp;


import android.annotation.SuppressLint;
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

    private static class ViewHolder{
        private ImageView destinationImage;
        private TextView destination;
        private TextView place;
        private TextView addressName;
        private TextView addressMail;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent){
        ViewHolder holder;

        if(convertView == null){
            convertView = this.layoutInflater.inflate(this.resource,null);
            holder = new ViewHolder();
            holder.destinationImage = convertView.findViewById(R.id.destinationImage);
            //holder.listId = convertView.findViewById(R.id.profileId);
            holder.destination = convertView.findViewById(R.id.destination);
            holder.place = convertView.findViewById(R.id.place);
            //holder.nowLatitude = convertView.findViewById(R.id.nowLatitude);
            //holder.nowLongitude = convertView.findViewById(R.id.nowLongitude);
            holder.addressName = convertView.findViewById(R.id.addressName);
            holder.addressMail = convertView.findViewById(R.id.addressMail);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        CustomHomeListItem item = this.items.get(position);

        //画像のセット
        holder.destinationImage.setImageBitmap(item.getDestinationImage());

        //idのセット
        //holder.listId.setText(item.getListId());

        //行き先名のセット
        holder.destination.setText(item.getDestination());

        //場所のセット
        holder.place.setText(item.getPlace());

        //緯度のセット
        //holder.nowLatitude.setText(item.getLatitude());

        //経度のセット
        //holder.nowLongitude.setText(item.getLongitude());

        //宛先名のセット
        holder.addressName.setText(item.getAddressName());

        //メアドのセット
        holder.addressMail.setText(item.getAddressMail());

        return convertView;
    }
}
