package com.light.redalert;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Event>
{
    private EventDAO eventDAO;
    public EventAdapter(@NonNull Context context, List<Event> events)
    {
        super(context, 0, events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        Event event = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_cell, parent, false);

        TextView eventCellTV = convertView.findViewById(R.id.eventCellTV);
        ImageButton deleteEventBtn = convertView.findViewById(R.id.deleteEventBtn);
        eventDAO = new EventDAO(this.getContext());
        eventDAO.open();

        String eventTitle = event.getName() +" at: "+ CalendarUtils.formattedTime(event.getTime());
        eventCellTV.setText(eventTitle);
        deleteEventBtn.setOnClickListener(v -> {
            // Perform delete operation
            eventDAO.deleteEvent(event.getId());
            AlarmHelper.cancelAlarm(getContext(), event.getId());
            // Remove event from adapter and update ListView
            remove(event);
            notifyDataSetChanged();
        });
        return convertView;
    }
}
