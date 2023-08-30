package com.example.achivements.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.achivements.R;
import com.example.achivements.databinding.AchivementItemBinding;
import com.example.achivements.models.Achivement;
import com.example.achivements.models.User;

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
            achivementItemBinding.achivementItemCV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle args = new Bundle();
                    args.putBoolean("isSelfAccount", false);
                    args.putSerializable("achivement", achivement);
                    try{
                        Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_achivementFragment, args);
                    }catch (Exception e){
                        Navigation.findNavController(view).navigate(R.id.action_accountFragment_to_achivementFragment, args);
                    }
                }
            });
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
