package tz.co.upeak.simpletodoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class TaskAdapter extends ArrayAdapter {
    private Context context;
    private List<Task>  taskList;

    private TaskDBHelper dbHelper;
    public TaskAdapter(@NonNull Context context, List<Task> taskList,TaskDBHelper dbHelper) {
        super(context, 0, taskList);
        this.context = context;
        this.taskList = taskList;
        this.dbHelper = dbHelper;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(context).inflate(R.layout.list_item_task,parent, false);
        }

        final Task currentTask = taskList.get(position);

        CheckBox checkBoxTask = listItemView.findViewById(R.id.checkBoxTask);
        checkBoxTask.setChecked(currentTask.isCompleted());
        checkBoxTask.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                currentTask.setCompleted(isChecked);
                updateTaskCompletion(currentTask);
            }
        });

        TextView textViewTaskName = listItemView.findViewById(R.id.textViewTaskName);
        textViewTaskName.setText(currentTask.getName());

        Button btnDelete = listItemView.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                deleteTask(currentTask);
                taskList.remove(position);
                notifyDataSetChanged();
            }
        });

        return listItemView;
    }

    private void deleteTask(Task task){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TaskContract.TaskEntry.TABLE_NAME, TaskContract.TaskEntry._ID+" = ?", new String[]{String.valueOf(task.getId())});
    }

    private void updateTaskCompletion(Task task) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COLUMN_COMPLETED, task.isCompleted() ? 1 : 0);
        db.update(TaskContract.TaskEntry.TABLE_NAME, values, TaskContract.TaskEntry._ID + " = ?", new String[]{String.valueOf(task.getId())});
    }
}
