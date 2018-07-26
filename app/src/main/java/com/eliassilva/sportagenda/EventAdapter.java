package com.eliassilva.sportagenda;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Elias on 12/07/2018.
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder>{
    private List<String> mEventsKeys;
    private List<Event> mEvents;
    private EventAdapterOnClickHandler mOnClickHandler;

    public EventAdapter(List<Event> events, EventAdapterOnClickHandler onClickHandler, List<String> eventsKeys) {
        this.mEventsKeys = eventsKeys;
        this.mEvents = events;
        this.mOnClickHandler = onClickHandler;
    }

    public interface EventAdapterOnClickHandler {
        void onClick(Event event, String eventKey);
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.event_item, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        TextView eventSport = holder.mSport;
        TextView eventDate = holder.mDate;
        TextView eventTime = holder.mTime;
        TextView eventLocal = holder.mLocal;
        TextView eventParicipants = holder.mParticipants;

        eventSport.setText(mEvents.get(position).sports);
        eventDate.setText(mEvents.get(position).date);
        eventTime.setText(mEvents.get(position).time);
        eventLocal.setText(mEvents.get(position).local);
        eventParicipants.setText(mEvents.get(position).participants);
    }

    @Override
    public int getItemCount() {
        return mEvents == null ? 0 : mEvents.size();
    }

    public void setEventData(List<Event> events, List<String> eventsKeys) {
        mEventsKeys = eventsKeys;
        mEvents = events;
        notifyDataSetChanged();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.rv_sport_tv) TextView mSport;
        @BindView(R.id.rv_date_tv) TextView mDate;
        @BindView(R.id.rv_time_tv) TextView mTime;
        @BindView(R.id.rv_local_tv) TextView mLocal;
        @BindView(R.id.rv_participants_tv) TextView mParticipants;

        public EventViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Event eventClicked = mEvents.get(getAdapterPosition());
            String eventClickedKey = mEventsKeys.get(getAdapterPosition());
            mOnClickHandler.onClick(eventClicked, eventClickedKey);
        }
    }
}
