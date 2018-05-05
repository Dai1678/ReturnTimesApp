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

class ProfileListAdapter extends ArrayAdapter<ProfileListModel> {

    private int resource;
    private List<ProfileListModel> items;
    private LayoutInflater layoutInflater;

    ProfileListAdapter(Context context, int resource, List<ProfileListModel> items) {
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
            holder.destination = convertView.findViewById(R.id.destination);
            holder.place = convertView.findViewById(R.id.place);
            holder.addressName = convertView.findViewById(R.id.addressName);
            holder.addressMail = convertView.findViewById(R.id.addressMail);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        ProfileListModel item = this.items.get(position);

        //画像のセット
        holder.destinationImage.setImageBitmap(item.getDestinationImage());

        //行き先名のセット
        holder.destination.setText(item.getDestination());

        //場所のセット
        holder.place.setText(item.getPlace());

        //宛先名のセット
        holder.addressName.setText(item.getAddressName());

        //メアドのセット
        holder.addressMail.setText(item.getAddressMail());

        return convertView;
    }
}
