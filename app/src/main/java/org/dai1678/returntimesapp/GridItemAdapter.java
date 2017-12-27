package org.dai1678.returntimesapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * <a href="https://ja.icons8.com/icon/6570/ホーム">ホーム アイコンクレジット</a>
 * <a href="https://ja.icons8.com/icon/6570/レストラン">レストラン アイコンクレジット</a>
 * <a href="https://ja.icons8.com/icon/6570/病院">病院 アイコンクレジット</a>
 * <a href="https://ja.icons8.com/icon/10606/銀行"> アイコンクレジット</a>
 * <a href="https://ja.icons8.com/icon/10606/郵便局">郵便局 アイコンクレジット</a>
 * <a href="https://ja.icons8.com/icon/34613/鉄道駅">鉄道駅 アイコンクレジット</a>
 */

public class GridItemAdapter extends ArrayAdapter<GridItem> {

    private int resource;
    private List<GridItem> items;
    private LayoutInflater layoutInflater;


    public GridItemAdapter(Context context, int resource, List<GridItem> items){
        super(context,resource,items);

        this.resource = resource;
        this.items = items;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private static class ViewHolder{
        public ImageView imageView;
        public TextView textView;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public GridItem getItem(int position) {
        return getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup viewGroup) {
        ViewHolder holder;

        if(convertView == null){
            convertView = this.layoutInflater.inflate(this.resource, null);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.imageItem);
            holder.textView = convertView.findViewById(R.id.imageText);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        GridItem item = this.items.get(position);

        holder.imageView.setImageResource(item.getItemImage());
        holder.textView.setText(item.getItemName());

        return convertView;
    }
}
