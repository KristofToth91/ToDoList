package hu.java.kristof.todolist.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Kristof Toth on 2018. 02. 14..
 */

public class ToDoItemDAO implements DAOinterface {
    private SQLiteDatabase db;
    private ToDoItemDBHelper helper;

    public ToDoItemDAO(Context context) {
        helper = new ToDoItemDBHelper(context);
    }


    @Override
    public void saveToDoItem(ToDoItem item) {
        db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", item.getItemName());
        cv.put("verbosetext", item.getItemVerboseText());
        cv.put("date", String.valueOf(item.getItemDate()));
        cv.put("priority", item.getItemPriority());
        if (item.isDone()) {
            cv.put("done", "true");

        } else {
            cv.put("done", "false");

        }
        if (item.getId() == -1) {
            long id = db.insert("todoitem", null, cv);
            item.setId((int) id);
        } else {
            db.update("todoitem", cv, "id=?", new String[]{item.getId() + ""});
        }


        db.close();
    }

    @Override
    public void removeAllToDoItems() {
        db = helper.getWritableDatabase();

        db.execSQL("DELETE FROM todoitem");
        db.close();
    }

    @Override
    public void removeToDoItem(ToDoItem item) {
        db = helper.getWritableDatabase();
        db.delete("todoitem", "id=?", new String[]{item.getId() + ""});
        db.close();
    }

    @Override
    public List<ToDoItem> getAllItems() {
        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM todoitem", null);

        cursor.moveToFirst();
        List<ToDoItem> items = new ArrayList<>();
        while (!cursor.isAfterLast()) {

            String name = cursor.getString(cursor.getColumnIndex("name"));
            String verbosetext = cursor.getString(cursor.getColumnIndex("verbosetext"));
            String dateString = cursor.getString(cursor.getColumnIndex("date"));

            Date date = null;
            try {
                date = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(dateString);
            } catch (ParseException e) {
                Log.e("TODOITEM", "ParseException" + e);
            }

            int priority = cursor.getInt(cursor.getColumnIndex("priority"));
            int id = cursor.getInt(cursor.getColumnIndex("id"));

            boolean done = false;
            if (cursor.getString(cursor.getColumnIndex("done")) == null) {
            }
            if (cursor.getString(cursor.getColumnIndex("done")).equals("true")) {
                done = true;
            }

            ToDoItem item = new ToDoItem(name, verbosetext, date, priority, id, done);
            items.add(item);

            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return items;
    }

    @Override
    public List<ToDoItem> listSeparation(List<ToDoItem> itemList, String listType) {
        List<ToDoItem> separatedList = new ArrayList<>();
        switch (listType) {
            case "doneList":
                for (ToDoItem doItem : itemList) {
                    if (doItem.isDone()) {
                        separatedList.add(doItem);
                    }
                }
                return separatedList;
            case "inProgressList":
                for (ToDoItem doItem : itemList) {
                    if (!doItem.isDone()) {
                        separatedList.add(doItem);
                    }
                }
                return separatedList;
        }
        return null;
    }


}
