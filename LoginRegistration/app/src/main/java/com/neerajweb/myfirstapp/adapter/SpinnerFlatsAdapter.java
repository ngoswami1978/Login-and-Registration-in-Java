package com.neerajweb.myfirstapp.adapter;

import java.util.List;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.neerajweb.myfirstapp.R;
import com.neerajweb.myfirstapp.model.flat_model;


/**
 * Created by Admin on 10/08/2015.
 */

public class SpinnerFlatsAdapter  extends BaseAdapter {

    public static final String TAG = "SpinnerFlatsAdapter";

    private List<flat_model> mItems;
    private LayoutInflater mInflater;

    public SpinnerFlatsAdapter(Context context, List<flat_model> listFlats) {
        try
        {
            this.setItems(listFlats);
            this.mInflater = LayoutInflater.from(context);
        }
        catch   (Exception e)
        {
            Log.d(TAG, "Error in SpinnerFlatsAdapter method : " + e.getMessage());
        }
    }

    @Override
    public int getCount() {
        return (getItems() != null && !getItems().isEmpty()) ? getItems().size() : 0 ;
    }

    @Override
    public flat_model getItem(int position) {
        return (getItems() != null && !getItems().isEmpty()) ? getItems().get(position) : null ;
    }

    @Override
    public long getItemId(int position) {
        return (getItems() != null && !getItems().isEmpty()) ? getItems().get(position).getId() : position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;
        if(v == null) {
            v = mInflater.inflate(R.layout.spinner_item_flats, parent, false);
            holder = new ViewHolder();
            holder.txtFlatName = (TextView) v.findViewById(R.id.txt_Flat_name);
            holder.txtFlat_type = (TextView) v.findViewById(R.id.txt_flat_type);
            v.setTag(holder);
        }
        else {
            holder = (ViewHolder) v.getTag();
        }

        // fill row data
        flat_model currentItem = getItem(position);
        if(currentItem != null) {
            holder.txtFlatName.setText(currentItem.getName());
            holder.txtFlat_type.setText(currentItem.getFlatType());
        }

        return v;
    }

    public List<flat_model> getItems() {
        return mItems;
    }

    public void setItems(List<flat_model> mItems) {
        this.mItems = mItems;
    }

    class ViewHolder {
        TextView txtFlatName;
        TextView txtFlat_type;
    }
}

