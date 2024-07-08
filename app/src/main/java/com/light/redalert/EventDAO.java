package com.light.redalert;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public EventDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long addEvent(Event event) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, event.getName());
        values.put(DatabaseHelper.COLUMN_DATE, event.getDate().toString());
        values.put(DatabaseHelper.COLUMN_TIME, event.getTime().toString());
        return database.insert(DatabaseHelper.TABLE_EVENTS, null, values);
    }

    public List<Event> getEventsByDate(LocalDate date) {
        List<Event> events = new ArrayList<>();
        String dateStr = date.toString();
        Cursor cursor = database.query(DatabaseHelper.TABLE_EVENTS, null, DatabaseHelper.COLUMN_DATE + "=?", new String[]{dateStr}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));
                String timeStr = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TIME));
                LocalTime time = LocalTime.parse(timeStr);
                events.add(new Event(id, name, date, time));
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return events;
    }
    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_EVENTS, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));
                String dateStr = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE));
                LocalDate date = LocalDate.parse(dateStr);
                String timeStr = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TIME));
                LocalTime time = LocalTime.parse(timeStr);
                events.add(new Event(id, name, date, time));
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return events;
    }

    public void deleteEvent(int id) {
        database.delete(DatabaseHelper.TABLE_EVENTS, DatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }
}
