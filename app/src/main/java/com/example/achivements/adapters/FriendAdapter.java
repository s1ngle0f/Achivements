package com.example.achivements.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.achivements.MainActivity;
import com.example.achivements.R;
import com.example.achivements.databinding.FriendItemBinding;
import com.example.achivements.models.Achivement;
import com.example.achivements.models.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendHolder> {
    private List<User> friendsList = new ArrayList<>();
    private Activity activity;

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

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
            loadAvatar(friend);
            if(friend.getActiveAchivement() != null) {
                friendItemBinding.statusText.setText(friend.getUsername());
                friendItemBinding.achivement.setText(friend.getActiveAchivement().getText());
            }else{
                friendItemBinding.statusText.setText("");
                friendItemBinding.achivement.setText("");
            }
            friendItemBinding.friendItemCV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle args = new Bundle();
                    args.putBoolean("isSelfAccount", false);
                    args.putSerializable("account", (Serializable) friend);
                    try{
                        Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_accountFragment, args);
                    }catch (Exception e){
                        Navigation.findNavController(view).navigate(R.id.action_searchFriendsFragment_to_accountFragment, args);
                    }
                }
            });
        }

        private Executor executor = Executors.newSingleThreadExecutor();
        private void loadAvatar(User user){
            if(user != null && user.equals(MainActivity.user)){
                CompletableFuture.supplyAsync(() ->
                                MainActivity.serverApi.getAvatar(), executor)
                        .thenAccept(_bytes -> {
                            if(_bytes != null){
                                Bitmap photoUri = BitmapFactory.decodeByteArray(_bytes, 0, _bytes.length);
                                activity.runOnUiThread(() -> {
                                    friendItemBinding.avatarImage.setImageBitmap(photoUri);
                                });
                            }
                        });
            }else{
                int userId = user.getId();
                CompletableFuture.supplyAsync(() ->
                                MainActivity.serverApi.getAvatarById(userId), executor)
                        .thenAccept(_bytes -> {
                            if(_bytes != null){
                                Bitmap photoUri = BitmapFactory.decodeByteArray(_bytes, 0, _bytes.length);
                                activity.runOnUiThread(() -> {
                                    friendItemBinding.avatarImage.setImageBitmap(photoUri);
                                });
                            }
                        });
            }
        }
    }

    public void Add(User friend){
        friendsList.add(friend);
        notifyDataSetChanged();
    }

    public void Add(List<User> friends){
        for (User friend : friends) {
            friendsList.add(friend);
        }
        notifyDataSetChanged();
    }

    public void Set(List<User> friends){
        friendsList = friends;
        notifyDataSetChanged();
    }
}
