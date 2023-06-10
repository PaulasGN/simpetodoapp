package tz.co.upeak.simpletodoapp;

import android.provider.BaseColumns;

public class TaskContract {
    private TaskContract(){}

    public static class TaskEntry implements BaseColumns{
        public static final String TABLE_NAME ="tasks";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_COMPLETED = "completed";
    }

}
