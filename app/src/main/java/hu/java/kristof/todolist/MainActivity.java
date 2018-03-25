package hu.java.kristof.todolist;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import hu.java.kristof.todolist.adapters.ToDoItemAdapter;
import hu.java.kristof.todolist.model.ToDoItem;
import hu.java.kristof.todolist.model.ToDoItemDAO;

public class MainActivity extends AppCompatActivity {
    private List<ToDoItem> itemList;
    private List<ToDoItem> doneList;
    private List<ToDoItem> inProgressList;
    private ToDoItemAdapter adapter;
    private ToDoItemDAO dao;
    private boolean inProgressListView;
    private static final int NEW_ITEM_RC = 1;
    private static final int EDIT_ITEM_RC = 2;
    private static final int CHANGED_PREFERENCES_RC = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewItem();
            }
        });

        dao = new ToDoItemDAO(this);


        itemList = dao.getAllItems();
        doneList = dao.listSeparation(itemList, "doneList");
        inProgressList = dao.listSeparation(itemList, "inProgressList");
        listOrderConfiguration();
        adapter = new ToDoItemAdapter(this, R.layout.listview_item, inProgressList);
        inProgressListView = true;
        ListView lvToDoItems = findViewById(R.id.lvToDoItems);
        lvToDoItems.setAdapter(adapter);
        registerForContextMenu(lvToDoItems);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.context_main, menu);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final ToDoItem doItem = adapter.getItem(info.position);

        int id = item.getItemId();

        switch (id) {
            case R.id.miEdit:
                Intent intent = new Intent(this, ToDoItemActivity.class);
                intent.putExtra("doItem", doItem);
                intent.putExtra("index", info.position);


                startActivityForResult(intent, EDIT_ITEM_RC);
                Toast.makeText(this, "Editing item", Toast.LENGTH_LONG).show();
                return true;

            case R.id.miDelete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Deletion conformation").setMessage("Are you sure you want to delete this item?").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        adapter.remove(doItem);
                        dao.removeToDoItem(doItem);
                        Toast.makeText(MainActivity.this, "Item deleted", Toast.LENGTH_LONG).show();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            case R.id.miDone:
                adapter.remove(doItem);
                doItem.setDone(true);
                doItem.setItemPriority(4);
                doneList.add(doItem);
                inProgressList.remove(doItem);
                dao.saveToDoItem(doItem);
                Toast.makeText(MainActivity.this, "Item is done!", Toast.LENGTH_LONG).show();
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivityForResult(intent, CHANGED_PREFERENCES_RC);
            return true;
        } else if (id == R.id.miNewToDoItem) {
            addNewItem();
            return true;
        } else if (id == R.id.miDeleteAll) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Deletion conformation").setMessage("Are you sure you want to delete ALL of the items?").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dao.removeAllToDoItems();
                    itemList.clear();
                    inProgressList.clear();
                    doneList.clear();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Items deleted", Toast.LENGTH_LONG).show();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

            return true;
        } else if (id == R.id.miShowDoneItems) {
            inProgressListView = false;
                        refreshAdapter();
            return true;
        } else if (id == R.id.miInProgressItems) {
            inProgressListView = true;
            refreshAdapter();
            return true;
        }
        return false;

    }

    private void addNewItem() {
        Intent intent = new Intent(this, ToDoItemActivity.class);
        startActivityForResult(intent, NEW_ITEM_RC);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_ITEM_RC) {
            if (resultCode == RESULT_OK) {
                ToDoItem doItem = (ToDoItem) data.getSerializableExtra("doItem");
                adapter.add(doItem);
                dao.saveToDoItem(doItem);
            }


        } else if (requestCode == EDIT_ITEM_RC) {
            if (resultCode == RESULT_OK) {
                ToDoItem doItem = (ToDoItem) data.getSerializableExtra("doItem");
                int index = data.getIntExtra("index", -1);
                if (index > -1) {
                    if (inProgressListView) {
                        if (data.getBooleanExtra("isDoneChanged", false)) {
                            inProgressList.remove(doItem);
                            doneList.add(doItem);

                        } else {
                            inProgressList.set(index, doItem);
                        }


                    } else {
                        if (data.getBooleanExtra("isDoneChanged", false)) {
                            doneList.remove(doItem);
                            inProgressList.add(doItem);

                        } else {
                            doneList.set(index, doItem);
                        }

                    }

                    dao.saveToDoItem(doItem);
                    // adapter.notifyDataSetChanged();
                    refreshAdapter();
                    Toast.makeText(this, "Edited", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "index=-1", Toast.LENGTH_LONG).show();
                }
            }

        } else if (requestCode == CHANGED_PREFERENCES_RC) {
            refreshAdapter();
        }
    }

    public void refreshAdapter() {  //for some reason only this hardreset works on the adapter
        itemList = dao.getAllItems();
        doneList = dao.listSeparation(itemList, "doneList");
        inProgressList = dao.listSeparation(itemList, "inProgressList");
        listOrderConfiguration();
        if (inProgressListView) {
            adapter = new ToDoItemAdapter(this, R.layout.listview_item, inProgressList);
            ListView lvToDoItems = findViewById(R.id.lvToDoItems);

            lvToDoItems.setAdapter(adapter);
            registerForContextMenu(lvToDoItems);
        } else {
            adapter = new ToDoItemAdapter(this, R.layout.listview_item, doneList);
            ListView lvToDoItems = findViewById(R.id.lvToDoItems);

            lvToDoItems.setAdapter(adapter);
            registerForContextMenu(lvToDoItems);
        }
    }

    public void listOrderConfiguration() {
        SharedPreferences shPref = PreferenceManager.getDefaultSharedPreferences(this);
        String order = shPref.getString("order", "1");
        switch (order) {
            case "1":
                Collections.sort(doneList, ToDoItem.Comparators.NAME);
                Collections.sort(inProgressList, ToDoItem.Comparators.NAME);
                break;
            case "2":
                Collections.sort(doneList, ToDoItem.Comparators.DATEDESC);
                Collections.sort(inProgressList, ToDoItem.Comparators.DATEDESC);
                break;
            case "3":
                Collections.sort(doneList, ToDoItem.Comparators.DATEASC);
                Collections.sort(inProgressList, ToDoItem.Comparators.DATEASC);
                break;
            case "4":
                Collections.sort(doneList, ToDoItem.Comparators.PRIORITY);
                Collections.sort(inProgressList, ToDoItem.Comparators.PRIORITY);
                break;
        }
    }

}
