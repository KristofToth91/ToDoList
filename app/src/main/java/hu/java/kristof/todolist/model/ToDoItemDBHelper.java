package hu.java.kristof.todolist.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Kristof Toth on 2018. 02. 14..
 */

public class ToDoItemDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "todolist.db";
    private static final int DATABASE_VERSION = 1;

    public ToDoItemDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE todoitem " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,verbosetext TEXT,date TEXT,priority INTEGER,done TEXT)");


//        db.execSQL("INSERT INTO todoitem (name,verbosetext,date,priority,done) VALUES ('Mosni','ki kell mosni a fehéreket', '2018.02.10 20:45',4,'false')");
//        db.execSQL("INSERT INTO todoitem (name,verbosetext,date,priority,done) VALUES ('Takarítani','ki kell takarítani a konyhát', '2018.02.12 16:00',3,'false')");
//        db.execSQL("INSERT INTO todoitem (name,verbosetext,date,priority,done) VALUES ('Főzni','Gulyáslevest kell főzni', '2018.02.14 20:00',2,'false')");
//        db.execSQL("INSERT INTO todoitem (name,verbosetext,date,priority,done) VALUES ('Tanulni','meg kell írni a todo appot', '2018.02.13 20:30',1,'false')");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE todoitem");
        onCreate(db);
    }
}
