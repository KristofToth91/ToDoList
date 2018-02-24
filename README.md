<h2>Preface</h2>
<p><br />ToDoList is an app I made to test my android skills. The idea is to have a simple lightweight app that helps the user to manage day-to-day tasks.</p>
<h2><br />Technical description</h2>
<p><br />The data storage behind ToDoList is an SQLite database which is accessed through a class that implements my custom interface using the DAO principle. <br />The base of the whole app is the ToDoItem class. It let's the user to configure the tasks:</p>
<ul>
<li>name</li>
<li>description</li>
<li>priority</li>
<li>deadline (date and time)</li>
</ul>
<p><br />The ToDoItems are displayed in the main frame using an adapter which translates the ToDoItemList that I got from the method from the DAOclass into clickable panels. The adapter also helps to keep track of missed deadlines: if the ToDoItems date is in the past compared to the current time the date will be painted red. There are two views that can be selected from the menu: one for the in progress tasks and one for the finished tasks.</p>
<p><br />The items are clickable with onContextItemSelected function. The ContextMenu consists of three options:</p>
<ul>
<li>Done! : marks the item done and removes from the "In progress view and puts it in the finished task view,</li>
<li>Edit: jumps to the Editing layout with the help of an Intent,</li>
<li>Delete: deletes the item.</li>
</ul>
<p>The OptionsMenu has five options to choose from:</p>
<ul>
<li>Settings: is used to changed Sharedpreferences. Currently the only preference that you can change as a user is the order of the items in the itemlist. It can be sorted by Alphabetical order, Date ascending, Date descending and Priority. The sorting is done by Collections sort using comparators in the ToDoItem class. The options can be selected in a Listpreference view.</li>
<li>New To-Do item: It does the same as the FloatingActionButton in the bottom of the main screen. It opens up the new_todoitem_view layout with the ToDoItemActivity class helping it and helps the user to create a new item. The ItemCreation's elements:<br />The namefield is limited between 3 and 50 characters, it gives you a Toast if its below 3 characters as a result to save and it will block you after 50 characters.<br />The VerboseText gives the opportunity to further elaborate on the task in hand.<br />Then you can select the itempriority within a radiobuttongroup using radiobuttons. The default is toppriority.<br />You can select the time and the date for the task. The default is the current date's 24:00. The selection is done by datepicker and timepicker widgets. <br />It is also possible to just press the Tomorrow button. This adds one day to the already selected (or default) date. <br />There is also an option to set the task done in the creation. It helps the user to document the tasks that were already done in the past and this way the user doesn't have to go through the creation and marking the item Done! on the mainscreen.<br />The rest os the functions is "Cancel" and "Save" which are self explanatory.<br />The Edit ContextMenu option uses the same layout, only with this option the fields are filled/selected according to the information from the Intent.</li>
<li>Delete all items: deletes every item from the database</li>
<li>Show finished tasks: switches to show all the items marked as done. These items could be 'revived' by the edit option</li>
<li>Show tasks in progress: switches to show the tasks in progress</li>
</ul>
<h2>Challenges</h2>
<p><br />There are a couple of challanges in the app the need to be solved. The first is the adapter's refreshing when it comes to reviving and marking items done in the creation process. For some reason the adapter couldn't handle the changes and that's why I had to do a 'hard-reset' and get everything from the database again and initialize the new lists.<br />Another challenge is the buttons in the Editing/NewItem adding layout. For some reason the buttons select date and hour need to be clicked twice to pop up the respective widgets. This needs to be further investigated.</p>
<h2><br />Future implementations</h2>
<p><br />In the future I might expand on this app with the following functionalities:</p>
<ul>
<li>the mainactivity will be transformed into a Tabbed activity to save the user from clicking menu and just be able to one click (or swipe) between the views</li>
<li>adding notification alerts to the deadlines</li>
<li>adding language specification based on the locale</li>
</ul>
