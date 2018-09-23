package com.denkarz.taskerapp;

import android.content.Context;
import android.graphics.Paint;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.denkarz.taskerapp.model.Todo;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.TreeSet;

public class SectionedListAdapter extends BaseAdapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private ArrayList<String> mData = new ArrayList<>();
    private ArrayList<Integer> mDataID = new ArrayList<>();
    private ArrayList<Boolean> mDataChecked = new ArrayList<>();

    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();
    private Context appContext;

    private LayoutInflater mInflater;

    public SectionedListAdapter(Context context) {
        appContext =context;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(Todo task) {
        mData.add(task.text);
        mDataID.add(task.id);
        mDataChecked.add(task.is_completed);
        notifyDataSetChanged();
    }

    public void addHeaderItem(final String item, int id) {
        mData.add(item);
        mDataID.add(id);
        mDataChecked.add(false);
        sectionHeader.add(mData.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public String getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            holder = new ViewHolder();
            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.row_item, null);
                    holder.checkBox = convertView.findViewById(R.id.checkBoxBold);
                    holder.checkBox.setTag(mDataID.get(position));
                    holder.checkBox.setChecked(mDataChecked.get(position));
                    if (mDataChecked.get(position)) {
                        holder.checkBox.setPaintFlags(holder.checkBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                    } else {
                        holder.checkBox.setPaintFlags(holder.checkBox.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    }
                    holder.checkBox.setText(mData.get(position));
                    final ViewHolder finalHolder = holder;
                    holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                buttonView.setPaintFlags(buttonView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                            } else {
                                buttonView.setPaintFlags(buttonView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                            }
                            Todo todoInFocus =  new Todo();
                            todoInFocus.id = (int) buttonView.getTag();
                            todoInFocus.text = buttonView.getText().toString();
                            todoInFocus.is_completed=buttonView.isChecked();
                            System.out.println(buttonView.isChecked());
                            System.out.println(isChecked);
                            if (todoInFocus.is_completed != isChecked) {
                                System.out.println("123456");
                                return;
                            }
                            String id = finalHolder.checkBox.getTag().toString();
                            String url = "https://limitless-dawn-57124.herokuapp.com/custom_controller/update?id="+id+"&is_completed="+!isChecked;
                            Ion.with(appContext)
                                    .load(url)
                                    .asString().
                                    setCallback(new FutureCallback<String>() {
                                        @Override
                                        public void onCompleted(Exception e, String result) {

                                        }
                                    });
                        }
                    });
                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater.inflate(R.layout.header_item, null);
                    holder.textView = (TextView) convertView.findViewById(R.id.textSeparator);
                    holder.textView.setText(mData.get(position));
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    public static class ViewHolder {
        public TextView textView;
        public CheckBox checkBox;
    }

}