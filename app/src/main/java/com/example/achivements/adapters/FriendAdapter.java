package com.example.achivements.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.achivements.R;
import com.example.achivements.databinding.FriendItemBinding;
import com.example.achivements.models.Friend;

import java.util.ArrayList;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendHolder> {
    private ArrayList<Friend> friendsList = new ArrayList<>();

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

        public void bind(Friend friend){
            friendItemBinding.statusText.setText(friend.getStatus());
            friendItemBinding.achivement.setText(friend.getActiveAchivement());
        }
    }

    public void AddFriend(Friend friend){
        friendsList.add(friend);
        notifyDataSetChanged();
    }
}
