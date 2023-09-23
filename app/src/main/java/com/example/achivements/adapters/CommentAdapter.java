package com.example.achivements.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.achivements.MainActivity;
import com.example.achivements.R;
import com.example.achivements.databinding.CommentItemBinding;
import com.example.achivements.models.Comment;
import com.example.achivements.models.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {
    private Executor executor = Executors.newSingleThreadExecutor();
    private ArrayList<Comment> commentList = new ArrayList<>();
    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View newView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new CommentHolder(newView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        holder.bind(commentList.get(position));
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class CommentHolder extends RecyclerView.ViewHolder {
        private CommentItemBinding commentItemBinding;
        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            commentItemBinding = CommentItemBinding.bind(this.itemView);
        }

        public void bind(Comment comment){
            if(comment != null) {
                CompletableFuture.supplyAsync(() ->
                                MainActivity.serverApi.getUser(comment.getUserId()), executor)
                        .thenAccept(_user -> {
                            commentItemBinding.commentLogin.setText(_user.getUsername());
                            commentItemBinding.commentText.setText(comment.getText());
                        });
            }
        }
    }

    public void Add(Comment comment){
        commentList.add(comment);
        notifyDataSetChanged();
    }

    public void Add(ArrayList<Comment> comments){
        for (Comment comment : comments) {
            commentList.add(comment);
        }
        notifyDataSetChanged();
    }

    public void Set(ArrayList<Comment> comments){
        commentList = comments;
        notifyDataSetChanged();
    }
}
