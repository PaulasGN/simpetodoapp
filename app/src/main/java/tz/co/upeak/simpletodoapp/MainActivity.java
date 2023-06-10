package tz.co.upeak.simpletodoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText editTextTask;
    private Button btnAdd;
    private ListView listViewTasks;
    private ArrayList<Task> taskList;
    private ArrayAdapter<Task> taskAdapter;
    private TaskDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTask = findViewById(R.id.editTextTask);
        btnAdd = findViewById(R.id.btnAdd);
        listViewTasks = findViewById(R.id.listViewTasks);

        dbHelper = new TaskDBHelper(this);
        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(this, taskList, dbHelper);
        listViewTasks.setAdapter(taskAdapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String taskName = editTextTask.getText().toString().trim();
                if (!taskName.isEmpty()) {
                    addTask(taskName);
                    editTextTask.setText("");
                    updateTaskList();
                }
            }
        });


        updateTaskList();
    }

    private void addTask(String taskName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COLUMN_NAME, taskName);
        values.put(TaskContract.TaskEntry.COLUMN_COMPLETED, false);
        db.insert(TaskContract.TaskEntry.TABLE_NAME, null, values);
    }



    private void updateTaskList(){
        taskList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE_NAME, null,null,null,null,null,null);
        int idColumnIndex = cursor.getColumnIndex(TaskContract.TaskEntry._ID);
        while(cursor.moveToNext()){
            int taskId = cursor.getInt(idColumnIndex);
            int nameColumnIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME);
            int completedColumnIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_COMPLETED);

            String taskName = cursor.getString(nameColumnIndex);
            boolean isCompleted = cursor.getInt(completedColumnIndex) == 1;
            Task task = new Task(taskId, taskName, isCompleted);
            taskList.add(task);
        }
        cursor.close();
        taskAdapter.notifyDataSetChanged();
    }
}