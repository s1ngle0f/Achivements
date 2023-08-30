package com.example.achivements.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.achivements.R;
import com.example.achivements.databinding.AchivementItemBinding;
import com.example.achivements.models.Achivement;

import java.util.ArrayList;

public class AchivementAdapter extends RecyclerView.Adapter<AchivementAdapter.AchivementHolder> {
    private ArrayList<Achivement> achivements = new ArrayList<>();

    @NonNull
    @Override
    public AchivementHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View newView = LayoutInflater.from(parent.getContext()).inflate(R.layout.achivement_item, parent, false);
        return new AchivementHolder(newView);
    }

    @Override
    public void onBindViewHolder(@NonNull AchivementHolder holder, int position) {
        holder.bind(achivements.get(position));
    }

    @Override
    public int getItemCount() {
        return achivements.size();
    }

    class AchivementHolder extends RecyclerView.ViewHolder{
        private AchivementItemBinding achivementItemBinding;
        public AchivementHolder(@NonNull View itemView) {
            super(itemView);
            achivementItemBinding = AchivementItemBinding.bind(this.itemView);
        }

        public void bind(Achivement achivement){
            achivementItemBinding.achivementText.setText(achivement.getText());
        }
    }

    public void Add(Achivement achivement){
        achivements.add(achivement);
        notifyDataSetChanged();
    }

    public void Add(ArrayList<Achivement> achivements){
        for (Achivement achivement : achivements) {
             this.achivements.add(achivement);
        }
        notifyDataSetChanged();
    }
}
