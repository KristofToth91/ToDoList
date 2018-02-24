package hu.java.kristof.todolist.model;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Kristof Toth on 2018. 02. 13..
 */

public class ToDoItem implements Serializable, Comparable<ToDoItem> {
    private String itemName;
    private String itemVerboseText;
    private Date itemDate;
    private int itemPriority;
    private int id;
    private boolean done;

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public ToDoItem(String itemName, String itemVerboseText, Date itemDate, int itemPriority, int id, boolean done) {
        this.itemName = itemName;
        this.itemVerboseText = itemVerboseText;
        this.itemDate = itemDate;
        this.itemPriority = itemPriority;
        this.id = id;
        this.done = done;
    }

    public ToDoItem() {
        this.id = -1;
        done = false;
    }

    public ToDoItem(String itemName, String itemVerboseText, Date itemDate, int itemPriority, int id) {
        this.itemName = itemName;
        this.itemVerboseText = itemVerboseText;
        this.itemDate = itemDate;
        this.itemPriority = itemPriority;
        this.id = id;
    }

    public ToDoItem(String itemName, String itemVerboseText, Date itemDate, int itemPriority) {
        this.itemName = itemName;
        this.itemVerboseText = itemVerboseText;
        this.itemDate = itemDate;
        this.itemPriority = itemPriority;
        this.id = -1;
        this.done = false;
    }

    public ToDoItem(String itemName, Date itemDate, int itemPriority, int id) {
        this.itemName = itemName;
        this.itemDate = itemDate;
        this.itemPriority = itemPriority;
        this.id = id;
    }

    @Override
    public String toString() {
        return "ToDoItem{" +
                "itemName='" + itemName + '\'' +
                ", itemDate=" + itemDate +
                ", itemPriority=" + itemPriority +
                ", id=" + id +
                ", done=" + done +
                '}';
    }

    public String getItemVerboseText() {
        return itemVerboseText;
    }

    public void setItemVerboseText(String itemVerboseText) {
        this.itemVerboseText = itemVerboseText;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Date getItemDate() {
        return itemDate;
    }

    public void setItemDate(Date itemDate) {
        this.itemDate = itemDate;
    }

    public int getItemPriority() {
        return itemPriority;
    }

    public void setItemPriority(int itemPriority) {
        this.itemPriority = itemPriority;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public int compareTo(@NonNull ToDoItem toDoItem) {
        return 0;
    }

    public static class Comparators {
        public static Comparator<ToDoItem> NAME = new Comparator<ToDoItem>() {
            @Override
            public int compare(ToDoItem t1, ToDoItem t2) {
                return t1.getItemName().compareTo(t2.getItemName());
            }
        };
        public static Comparator<ToDoItem> DATEASC = new Comparator<ToDoItem>() {
            @Override
            public int compare(ToDoItem t1, ToDoItem t2) {

                return t1.getItemDate().compareTo(t2.getItemDate());
            }
        };
        public static Comparator<ToDoItem> DATEDESC = new Comparator<ToDoItem>() {
            @Override
            public int compare(ToDoItem t1, ToDoItem t2) {

                return t2.getItemDate().compareTo(t1.getItemDate());
            }
        };
        public static Comparator<ToDoItem> PRIORITY = new Comparator<ToDoItem>() {
            @Override
            public int compare(ToDoItem t1, ToDoItem t2) {
                return t1.getItemPriority() - t2.getItemPriority();
            }
        };

    }
}
