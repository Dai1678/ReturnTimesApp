package org.dai1678.returntimesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * <a href="https://ja.icons8.com/icon/6570/ホーム">ホーム アイコンクレジット</a>
 * <a href="https://ja.icons8.com/icon/6570/レストラン">レストラン アイコンクレジット</a>
 * <a href="https://ja.icons8.com/icon/6570/病院">病院 アイコンクレジット</a>
 * <a href="https://ja.icons8.com/icon/10606/銀行"> アイコンクレジット</a>
 * <a href="https://ja.icons8.com/icon/10606/郵便局">郵便局 アイコンクレジット</a>
 * <a href="https://ja.icons8.com/icon/34613/鉄道駅">鉄道駅 アイコンクレジット</a>
 */

public class GridItemAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private String[] itemNameArray = {"家","レストラン","病院","銀行","郵便局","駅"};
    private Integer[] itemImageArray = {R.mipmap.ic_house,R.mipmap.ic_restaurant,R.mipmap.ic_hospital,R.mipmap.ic_bank,R.mipmap.ic_postoffice,R.mipmap.ic_station};
    private static class ViewHolder{
        public ImageView imageView;
        public TextView textView;
    }

    public GridItemAdapter(Context context){
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return itemNameArray.length;
    }

    @Override
    public Object getItem(int position) {
        return itemNameArray[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if(view == null){
            view = mLayoutInflater.inflate(R.layout.grid_item,null);
            holder = new ViewHolder();
            holder.imageView = (ImageView)view.findViewById(R.id.imageItem);
            holder.textView = (TextView)view.findViewById(R.id.imageText);
            view.setTag(holder);
        }else{
            holder = (ViewHolder)view.getTag();
        }

        holder.imageView.setImageResource(itemImageArray[position]);
        holder.textView.setText(itemNameArray[position]);

        return view;
    }
}
