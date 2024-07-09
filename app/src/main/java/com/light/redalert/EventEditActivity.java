package com.light.redalert;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class EventEditActivity extends AppCompatActivity {
    private EditText eventNameET;
    private TextView eventDateTV, eventTimeTV;
    private LocalDate date;
    private LocalTime time;
    private EventDAO eventDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        initWidgets();
        eventDAO = new EventDAO(this);
        eventDAO.open();
    }

    private void initWidgets() {
        eventNameET = findViewById(R.id.eventNameET);
        eventDateTV = findViewById(R.id.eventDateTV);
        eventTimeTV = findViewById(R.id.eventTimeTV);

        eventDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate();
            }
        });

        eventTimeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickTime();
            }
        });
    }

    private void pickDate() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                EventEditActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date = LocalDate.of(year, month + 1, dayOfMonth);
                        eventDateTV.setText(date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    private void pickTime() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                EventEditActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        time = LocalTime.of(hourOfDay, minute);
                        eventTimeTV.setText(time.format(DateTimeFormatter.ofPattern("HH:mm")));
                    }
                }, hour, minute, true);
        timePickerDialog.show();
    }

    public void addEvent(String name, LocalDate date, LocalTime time) {
        Event event = new Event(name, date, time);
        long id = eventDAO.addEvent(event);
        if (id != -1) {
            AlarmHelper.setAlarm(this, event);
        }
    }

    public void saveEventAction(View view) {
        String name = eventNameET.getText().toString();
        if (name.isEmpty() || date == null || time == null) {
            Toast.makeText(this, "Event info missing", Toast.LENGTH_LONG).show();
            return;
        }
        addEvent(name, date, time);
        finish(); // Close activity after saving
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventDAO.close();
    }
}
