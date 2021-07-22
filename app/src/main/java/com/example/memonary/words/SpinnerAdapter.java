package com.example.memonary.words;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.memonary.R;

import java.util.Arrays;
import java.util.List;

public class SpinnerAdapter extends BaseAdapter {

    private List<String> stringArray;
    private Context context;

    public SpinnerAdapter(Context context, String[] strings) {
        this.context = context;
        stringArray = Arrays.asList(strings);
    }

    @Override
    public int getCount() {
        return stringArray.size();
    }

    @Override
    public Object getItem(int i) {
        return stringArray.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null || !view.getTag().toString().equals("NON_DROPDOWN")) {
            view = inflater.inflate(R.layout.
                    spinner_actionbar_item, viewGroup, false);
            view.setTag("NON_DROPDOWN");
        }
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText(stringArray.get(i));
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null || !convertView.getTag().toString().equals("NON_DROPDOWN")) {
            convertView = inflater.inflate(R.layout.
                    spinner_dropdown_item, parent, false);
            convertView.setTag("NON_DROPDOWN");
        }
        TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
        textView.setText(stringArray.get(position));
        return convertView;
    }
}
