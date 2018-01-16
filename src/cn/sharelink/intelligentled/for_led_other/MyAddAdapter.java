package cn.sharelink.intelligentled.for_led_other;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.sharelink.intelligentled.R;

public class MyAddAdapter extends BaseAdapter {
    private Context mContext;
    private List<TypeModel> mList;
    private OnCallBack mCallBack;
    private Holder holder;

    public MyAddAdapter(Context context,List<TypeModel> list,OnCallBack callBack){
        this.mContext = context;
        this.mList = list;
        this.mCallBack = callBack;
    }

    public interface OnCallBack{
        void onSelectedListener(int pos);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.my_adapter_item,null);
            holder = new Holder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.select = (LinearLayout) convertView.findViewById(R.id.select);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }

        holder.name.setText(mList.get(position).getPhysicalID());
        if (mList.get(position).getIsSelect().equals("0")){
            holder.select.setSelected(false);
        }else{
            holder.select.setSelected(true);
        }

        holder.select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallBack!=null){
                    mCallBack.onSelectedListener(position);
                }
            }
        });

        return convertView;
    }

    private class Holder{
        TextView name;
        LinearLayout select;
    }
}
