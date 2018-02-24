package hu.java.kristof.todolist.model;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Kristof Toth on 2018. 02. 14..
 */

public interface DAOinterface {
    public void saveToDoItem(ToDoItem item);

    public void removeAllToDoItems();

    public void removeToDoItem(ToDoItem item);

    public List<ToDoItem> getAllItems();
public List<ToDoItem> listSeparation(List<ToDoItem> itemList,String listType);
}
