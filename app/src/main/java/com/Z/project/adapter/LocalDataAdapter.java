package com.Z.project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.Z.project.R;

public class LocalDataAdapter extends BaseAdapter {


    private Context context;
    public LocalDataAdapter(Context context){
        this.context=context;

    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.lay_data_item,null);

        }
        TextView tv_index=(TextView)convertView.findViewById(R.id.tv_index);
        tv_index.setText((position+1)+"");
        TextView tv_data=(TextView)convertView.findViewById(R.id.tv_data);
        tv_data.setText("11:11:11~12:12:12");
        return convertView;
    }
}
