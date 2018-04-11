package com.msg.android.lib.app.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.msg.android.lib.core.annotation.template.AnnotationContextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by msg on 16/11/5.
 */
public abstract class MsgBaseAdapter<T> extends BaseAdapter {

    private List<T> datas = new ArrayList<>();

    private Activity context;

    public MsgBaseAdapter(Activity context) {
        this.context = context;
    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        if(position < datas.size()){
            return datas.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Object holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(getItemLayout(),null);
            holder = getHolder();
            AnnotationContextUtils.initViewWithAnnotation(context,convertView,holder);
            convertView.setTag(holder);
        }else{
            holder= convertView.getTag();
        }
        process(convertView,holder,position,datas.get(position));
        return convertView;
    }

    protected abstract Object getHolder();

    protected abstract int getItemLayout();

    protected abstract void process(View itemView, Object holder,int position, T model);

}
