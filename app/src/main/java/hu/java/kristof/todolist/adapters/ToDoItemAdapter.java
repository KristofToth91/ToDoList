package hu.java.kristof.todolist.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import hu.java.kristof.todolist.R;
import hu.java.kristof.todolist.model.ToDoItem;

/**
 * Created by Kristof Toth on 2018. 02. 13..
 */

public class ToDoItemAdapter extends ArrayAdapter<ToDoItem> {
    private int resource;

    public ToDoItemAdapter(@NonNull Context context, int resource, @NonNull List<ToDoItem> objects) {
        super(context, resource, objects);
        this.resource = resource;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ToDoItem doItem = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resource, null);
        }
        View view = convertView.findViewById(R.id.vPriority);
        switch (doItem.getItemPriority()) {
            case 1:
                view.setBackgroundColor(Color.rgb(249, 36, 36));
                break;
            case 2:
                view.setBackgroundColor(Color.rgb(249, 164, 36));
                break;
            case 3:
                view.setBackgroundColor(Color.rgb(245, 245, 60));
                break;
            case 4:
                view.setBackgroundColor(Color.rgb(192, 192, 192));
                break;
        }
        TextView tvName = convertView.findViewById(R.id.tvItemName);
        TextView tvDate = convertView.findViewById(R.id.tvDate);

        Date rightNow = new Date();
        if (rightNow.after(doItem.getItemDate())) {
            tvDate.setTextColor(Color.RED);
        } else {
            tvDate.setTextColor(Color.BLACK);
        }
        SimpleDateFormat sv = new SimpleDateFormat("EEEE yyyy.MM.dd HH:mm");
        String date = sv.format(doItem.getItemDate());
        tvName.setText(doItem.getItemName());
        tvDate.setText(date);

        return convertView;
    }
}
