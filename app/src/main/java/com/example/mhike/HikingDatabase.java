package com.example.mhike;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class HikingDatabase extends SQLiteOpenHelper {
    public static final String databaseName = "mhike.db";

    //hikes
    public static final String table_name = "hikes";
    public static final String id_column_name = "id";
    public static final String name_column_name = "hikename";
    public static final String location_column_name = "location";
    public static final String date_column_name = "date";
    public static final String parking_column_name = "parking";
    public static final String length_column_name = "length";
    public static final String difficulty_column_name = "difficulty";
    public static final String description_column_name = "description";
    public static final String leader_column_name = "leader";

    //observation
    public static final String TABLE_OBSERVATIONS = "observations";
    public static final String OBSERVATION_ID_COLUMN_NAME = "id";
    public static final String OBSERVATION_COLUMN_NAME = "observation";
    public static final String OBSERVATION_TIME_COLUMN_NAME = "time";
    public static final String COMMENT_COLUMN_NAME = "comment";
    public static final String HIKE_ID_COLUMN_NAME = "hike_id";

    private SQLiteDatabase database;

    private static final String DATABASE_CREATE_QUERY = String.format(
            "Create table %s (" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "%s TEXT, "+
                    "%s TEXT, "+
                    "%s TEXT, "+
                    "%s INTEGER, "+
                    "%s TEXT, "+
                    "%s TEXT, "+
                    "%s TEXT, "+
                    "%s TEXT) ",
            table_name, id_column_name, name_column_name, location_column_name, date_column_name,
            length_column_name, parking_column_name,  difficulty_column_name, description_column_name, leader_column_name);

    private static final String CREATE_TABLE_OBSERVATIONS = String.format(
            "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "%s TEXT, %s TEXT, %s TEXT, %s INTEGER, " +
                    "FOREIGN KEY(%s) REFERENCES %s(%s))",
            TABLE_OBSERVATIONS, OBSERVATION_ID_COLUMN_NAME, OBSERVATION_COLUMN_NAME,
            OBSERVATION_TIME_COLUMN_NAME, COMMENT_COLUMN_NAME,
            HIKE_ID_COLUMN_NAME, HIKE_ID_COLUMN_NAME, table_name, id_column_name);

    public HikingDatabase(Context context) {
        super(context, databaseName, null, 1);
        database = getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_QUERY);
        db.execSQL(CREATE_TABLE_OBSERVATIONS);
        db.execSQL("create table allusers(email TEXT primary key, password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + table_name);
        Log.w(this.getClass().getName(), table_name + "database upgrade to version"
                + newVersion + "- old data lost");
    }

    //hiking

    public long insertHike(HikingModel hikingModel){
        ContentValues rowValues = new ContentValues();
        rowValues.put(name_column_name, hikingModel.getHikeName());
        rowValues.put(location_column_name, hikingModel.getLocation());
        rowValues.put(date_column_name, hikingModel.getDate());
        rowValues.put(parking_column_name, hikingModel.getParking());
        rowValues.put(length_column_name, hikingModel.getLength());
        rowValues.put(difficulty_column_name, hikingModel.getDifficulty());
        rowValues.put(description_column_name, hikingModel.getDescription());
        rowValues.put(leader_column_name, hikingModel.getLeaderName());
        return database.insertOrThrow(table_name, null, rowValues);
    }

    public ArrayList<HikingModel> listAll(){
        ArrayList<HikingModel> rel = new ArrayList<>();
        database = this.getWritableDatabase();
        String sql = "SELECT * FROM " + table_name;
        Cursor cursor = database.rawQuery(sql, null);
        cursor.moveToFirst();
        while(cursor.isAfterLast() == false){
            HikingModel hikesModel = new HikingModel(cursor.getInt(0),cursor.getString(1),
                    cursor.getString(2), cursor.getString(3), cursor.getInt(4), cursor.getString(5), cursor.getString(6),
                    cursor.getString(7), cursor.getString(8));
            rel.add(hikesModel);

            cursor.moveToNext();
        }
        database.close();
        return rel;
    }

    public void deleteHike(HikingModel hikesModel){
        database = this.getWritableDatabase();
        database.delete(table_name, id_column_name + " = ?", new String[]{String.valueOf(hikesModel.getId())});
        database.close();
    }

    public void deleteAllHike(){
        database = this.getWritableDatabase();
        database.delete(table_name, null, null);
        database.close();
    }

    public ArrayList<HikingModel> searchDataByName(String searchName) {
        ArrayList<HikingModel> result = new ArrayList<>();
        database = this.getWritableDatabase();

        // Search by name:
        String sql = "SELECT * FROM " + table_name + " WHERE " + name_column_name + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + searchName + "%"};

        Cursor cursor = database.rawQuery(sql, selectionArgs);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            HikingModel hikesModel = new HikingModel(cursor.getInt(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3), cursor.getInt(4),
                    cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8));

            result.add(hikesModel);
            cursor.moveToNext();
        }

        cursor.close();
        database.close();

        return result;
    }

    public void updateHike(HikingModel hikesModel){
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(name_column_name, hikesModel.getHikeName());
        values.put(location_column_name, hikesModel.getLocation());
        values.put(date_column_name, hikesModel.getDate());
        values.put(parking_column_name, hikesModel.getParking());
        values.put(length_column_name, hikesModel.getLength());
        values.put(difficulty_column_name, hikesModel.getDifficulty());
        values.put(description_column_name, hikesModel.getDescription());
        values.put(leader_column_name, hikesModel.getLeaderName());

        database.update(table_name, values, id_column_name + " = ?", new String[] {String.valueOf(hikesModel.getId())});
        database.close();
    }

    //observation
    public long insertObservation(ObservationModel observationModel){
        ContentValues rowValues = new ContentValues();
        rowValues.put(OBSERVATION_COLUMN_NAME, observationModel.getObservation());
        rowValues.put(OBSERVATION_TIME_COLUMN_NAME, observationModel.getTime());
        rowValues.put(COMMENT_COLUMN_NAME, observationModel.getComment());
        rowValues.put(HIKE_ID_COLUMN_NAME, observationModel.getHikeId());
        return database.insertOrThrow(TABLE_OBSERVATIONS, null, rowValues);
    }

    public ArrayList<ObservationModel> listObservation(int hikeId) {
        ArrayList<ObservationModel> result = new ArrayList<>();
        database = this.getReadableDatabase();

        String sql = "SELECT * FROM " + TABLE_OBSERVATIONS + " WHERE " + HIKE_ID_COLUMN_NAME + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(hikeId)};

        Cursor cursor = database.rawQuery(sql, selectionArgs);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            ObservationModel observationModel = new ObservationModel(cursor.getInt(0),
                    cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));

            result.add(observationModel);
            cursor.moveToNext();
        }

        cursor.close();
        database.close();

        return result;
    }

    public void deleteObservation(ObservationModel observationModel) {
        SQLiteDatabase database = this.getWritableDatabase();

        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(observationModel.getId())};

        database.delete(TABLE_OBSERVATIONS, whereClause, whereArgs);

        database.close();
    }

    public void updateObservation(ObservationModel observationModel) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(OBSERVATION_COLUMN_NAME, observationModel.getObservation());
        values.put(OBSERVATION_TIME_COLUMN_NAME, observationModel.getTime());
        values.put(COMMENT_COLUMN_NAME, observationModel.getComment());
        values.put(HIKE_ID_COLUMN_NAME, observationModel.getHikeId());

        database.update(TABLE_OBSERVATIONS, values, OBSERVATION_ID_COLUMN_NAME + " = ?", new String[] {String.valueOf(observationModel.getId())});
        database.close();
    }

    public void deleteAllObservation(int hikeId){
        database = this.getWritableDatabase();
        database.delete(TABLE_OBSERVATIONS, HIKE_ID_COLUMN_NAME + " = ?", new String[]{String.valueOf(hikeId)});
        database.close();
    }

    //login-signup
    public Boolean insertUser(String email, String password){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);
        long result = MyDatabase.insert("allusers", null, contentValues);

        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public Boolean checkEmail(String email){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from allusers where email = ?", new String[]{email});

        if(cursor.getCount() > 0){
            return true;
        }else{
            return false;
        }
    }
    public Boolean checkEmailPassword(String email, String password){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from allusers where email = ? and password = ?", new String[]{email, password});

        if(cursor.getCount() > 0){
            return true;
        }else{
            return false;
        }
    }
}
