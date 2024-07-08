package com.light.redalert;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder>
{
    private final ArrayList<LocalDate> days;
    private final OnItemListener onItemListener;
    private List<Event> events;

    public CalendarAdapter(ArrayList<LocalDate> days, OnItemListener onItemListener, List<Event> events)
    {
        this.days = days;
        this.onItemListener = onItemListener;
        this.events = events;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if(days.size() > 15) //month view
            layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        else // week view
            layoutParams.height = (int) parent.getHeight();

        return new CalendarViewHolder(view, onItemListener, days);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        final LocalDate date = days.get(position);
        if (date == null)
            holder.dayOfMonth.setText("");
        else {
            holder.dayOfMonth.setText(String.valueOf(date.getDayOfMonth()));
            // Check if the date has events and change background color
            for (Event event : events) {
                if (event.getDate().equals(date)) {
                    holder.parentView.setBackgroundColor(Color.parseColor("#008080"));
                    holder.parentView.findViewById(R.id.cellDayText).setBackgroundColor(Color.parseColor("#008080"));
                    break; // Exit loop once background is set
                }
            }
            if (date.equals(CalendarUtils.selectedDate)) {
                holder.parentView.setBackgroundColor(Color.parseColor("#D3D3D3"));
                holder.parentView.findViewById(R.id.cellDayText).setBackgroundColor(Color.parseColor("#D3D3D3"));
            }
        }
    }

    @Override
    public int getItemCount()
    {
        return days.size();
    }

    public interface  OnItemListener
    {
        void onItemClick(int position, LocalDate date);
    }
}