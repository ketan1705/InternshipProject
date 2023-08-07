package android.com.destek.demoproject1.repository;

import android.com.destek.demoproject1.model.TaskModel;
import android.com.destek.demoproject1.model.UserModel;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "demo_project.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_USER = "user_detail";
    private static final String TABLE_TASKS = "user_task";

    private static final String COLUMN_USERNAME = "user_name";
    private static final String COLUMN_USEREMAIL = "user_email";
    private static final String COLUMN_USERPASSWORD = "user_password";
    private static final String COLUMN_USERMOBILENO = "user_mobileNo";
    private static final String COLUMN_USERGENDER = "user_gender";
    private static final String COLUMN_USERDOB = "user_dob";
    private static final String COLUMN_USERTASK = "user_task";
    private static final String COLUMN_DATE = "user_date";
    private static final String COLUMN_TIME = "user_time";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_USERPASSWORD + " TEXT,"
                + COLUMN_USERMOBILENO + " TEXT,"
                + COLUMN_USERGENDER + " TEXT,"
                + COLUMN_USERDOB + " TEXT,"
                + COLUMN_USEREMAIL + " TEXT UNIQUE "
                + ")";

        sqLiteDatabase.execSQL(CREATE_USER_TABLE);

        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
                + COLUMN_USEREMAIL + " TEXT,"
                + COLUMN_USERTASK + " TEXT,"
                + COLUMN_DATE + " TEXT,"
                + COLUMN_TIME + " TEXT"
                + ")";
        sqLiteDatabase.execSQL(CREATE_TASKS_TABLE);


    }

    //insert user data
    public long insertUser(UserModel userModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, userModel.getUserName());
        values.put(COLUMN_USERPASSWORD, userModel.getUserPassword());
        values.put(COLUMN_USEREMAIL, userModel.getUserEmail());
        values.put(COLUMN_USERMOBILENO, userModel.getUserMobileNo());
        values.put(COLUMN_USERGENDER, userModel.getUserGender());
        values.put(COLUMN_USERDOB, userModel.getUserDob());
        return db.insert(TABLE_USER, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(sqLiteDatabase);
    }

    //check authentication for user
    // use this function in SignIn Activity
    public boolean authenticateUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = DatabaseHelper.COLUMN_USEREMAIL + " = ? AND " + DatabaseHelper.COLUMN_USERPASSWORD + " = ?";
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(DatabaseHelper.TABLE_USER, null, selection, selectionArgs, null, null, null);
        boolean isAuthenticated = cursor != null && cursor.getCount() > 0;
        if (cursor != null) {
            cursor.close();
        }
        return isAuthenticated;
    }

    /*get user detail by their email
        use this function in Profile Activity*/
    public UserModel getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USERNAME, COLUMN_USEREMAIL, COLUMN_USERMOBILENO, COLUMN_USERDOB, COLUMN_USERGENDER};
        String selection = COLUMN_USEREMAIL + " = ? ";
        String[] selectionArgs = {email};
        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);
        UserModel userModel = new UserModel();
        if (cursor != null && cursor.moveToFirst()) {

            int userNameIndex = cursor.getColumnIndex(COLUMN_USERNAME);
            int userEmailIndex = cursor.getColumnIndex(COLUMN_USEREMAIL);
            int userGenderIndex = cursor.getColumnIndex(COLUMN_USERGENDER);
            int userMobileNoIndex = cursor.getColumnIndex(COLUMN_USERMOBILENO);
            int userDobIndex = cursor.getColumnIndex(COLUMN_USERDOB);

            if (userNameIndex != -1 && userEmailIndex != -1
                    && userGenderIndex != -1 && userMobileNoIndex != -1 && userDobIndex != -1) {
                String userName = cursor.getString(userNameIndex);
                String userEmail = cursor.getString(userEmailIndex);
                String userGender = cursor.getString(userGenderIndex);
                String userMobileNo = cursor.getString(userMobileNoIndex);
                String userDOB = cursor.getString(userDobIndex);

                userModel.setUserName(userName);
                userModel.setUserEmail(userEmail);
                userModel.setUserMobileNo(userMobileNo);
                userModel.setUserGender(userGender);
                userModel.setUserDob(userDOB);


            }


            cursor.close();
        }
        db.close();
        return userModel;
    }

    // Adding the Task In Database
    public void addTask(TaskModel taskModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USEREMAIL, taskModel.getEmail());
        values.put(COLUMN_USERTASK, taskModel.getTaskTxt());
        values.put(COLUMN_DATE, taskModel.getDateTxt());
        values.put(COLUMN_TIME, taskModel.getTimeTxt());
        db.insert(TABLE_TASKS, null, values);
    }

    /* check email exists in database or not
   use this function SignUp Activity*/
    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorUser = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?",
                new String[]{TABLE_USER});
        boolean isUserTableExists = cursorUser != null && cursorUser.moveToFirst();
        if (cursorUser != null) {
            cursorUser.close();
        }
        if (isUserTableExists) {
            String selection = COLUMN_USEREMAIL + " = ?";
            String[] selectionArgs = {email};
            try (Cursor cursor = db.query(TABLE_USER, null, selection, selectionArgs, null, null, null)) {
                return cursor != null && cursor.getCount() > 0;
            }
        } else {
            return false;
        }
    }


    // get all the task
    public List<TaskModel> getAllTask(String email) {

        List<TaskModel> taskList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USERTASK, COLUMN_USEREMAIL, COLUMN_DATE, COLUMN_TIME};
        String selection = COLUMN_USEREMAIL + " = ? ";
        String[] selectionArgs = {email};
        Cursor cursor = db.query(TABLE_TASKS, columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                TaskModel taskModel = new TaskModel();

                int userTaskIndex = cursor.getColumnIndex(COLUMN_USERTASK);
                int userEmailIndex = cursor.getColumnIndex(COLUMN_USEREMAIL);
                int userDateIndex = cursor.getColumnIndex(COLUMN_DATE);
                int userTimeIndex = cursor.getColumnIndex(COLUMN_TIME);


                if (userTaskIndex != -1 && userEmailIndex != -1
                        && userDateIndex != -1 && userTimeIndex != -1) {
                    String userTask = cursor.getString(userTaskIndex);
                    String userEmail = cursor.getString(userEmailIndex);
                    String userDate = cursor.getString(userDateIndex);
                    String userTime = cursor.getString(userTimeIndex);

                    taskModel.setTaskTxt(userTask);
                    taskModel.setEmail(userEmail);
                    taskModel.setDateTxt(userDate);
                    taskModel.setTimeTxt(userTime);
                    taskList.add(taskModel);


                }

            } while (cursor.moveToNext());


            cursor.close();
        }
        db.close();


        return taskList;

    }


}
