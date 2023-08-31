package com.example.achivements.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.achivements.R;
import com.example.achivements.databinding.FriendItemBinding;
import com.example.achivements.models.User;

import java.io.Serializable;
import java.util.ArrayList;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendHolder> {
    private ArrayList<User> friendsList = new ArrayList<>();
    @NonNull
    @Override
    public FriendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View newView = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item, parent, false);
        return new FriendHolder(newView);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendHolder holder, int position) {
        holder.bind(friendsList.get(position));
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    public class FriendHolder extends RecyclerView.ViewHolder {
        private FriendItemBinding friendItemBinding;
        public FriendHolder(@NonNull View itemView) {
            super(itemView);
            friendItemBinding = FriendItemBinding.bind(this.itemView);
        }

        public void bind(User friend){
            if(friend.GetActiveAchivement() != null) {
                friendItemBinding.statusText.setText(friend.GetActiveAchivement().getStatus());
                friendItemBinding.achivement.setText(friend.GetActiveAchivement().getText());
            }else{
                friendItemBinding.statusText.setText("");
                friendItemBinding.achivement.setText("");
            }
            friendItemBinding.friendItemCV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle args = new Bundle();
                    args.putBoolean("isSelfAccount", false);
                    args.putSerializable("account", friend);
                    try{
                        Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_accountFragment, args);
                    }catch (Exception e){
                        Navigation.findNavController(view).navigate(R.id.action_searchFriendsFragment_to_accountFragment, args);
                    }
                }
            });
        }
    }

    public void Add(User friend){
        friendsList.add(friend);
        notifyDataSetChanged();
    }

    public void Add(ArrayList<User> friends){
        for (User friend : friends) {
            friendsList.add(friend);
        }
        notifyDataSetChanged();
    }

    public void Set(ArrayList<User> friends){
        friendsList = friends;
        notifyDataSetChanged();
    }
}
