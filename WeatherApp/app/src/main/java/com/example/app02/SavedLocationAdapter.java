package com.example.app02;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SavedLocationAdapter extends RecyclerView.Adapter<SavedLocationAdapter.ViewHolder> {
    private List<SavedLocation> locations;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(SavedLocation location);
    }

    public SavedLocationAdapter(List<SavedLocation> locations, OnItemClickListener listener) {
        this.locations = locations;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.saved_location_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SavedLocation location = locations.get(position);
        holder.cityNameTextView.setText(location.getCityName());
        holder.temperatureTextView.setText(String.format("%.1f°C", location.getTemperature()));

        holder.itemView.setOnClickListener(v -> listener.onItemClick(location));
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView cityNameTextView;
        public TextView temperatureTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            cityNameTextView = itemView.findViewById(R.id.savedCityNameTextView);
            temperatureTextView = itemView.findViewById(R.id.savedTemperatureTextView);
        }
    }
}