package com.example.parstagram.adapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.parstagram.R;
import com.example.parstagram.model.Post;
import com.parse.ParseFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder>{

    private Context context;
    private List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvUsername;
        private TextView tvDescription;
        private TextView tvUsername2;
        private TextView tvTimestamp;
        private ImageView ivPostImage;
        private ImageView ivProfileImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvUsername2 = itemView.findViewById(R.id.tvUsername2);
            ivPostImage = itemView.findViewById(R.id.ivPostImage);
            tvTimestamp = itemView.findViewById(R.id.tvTimetamp);
            ivProfileImg = itemView.findViewById(R.id.ivProfileImg);
        }

        // Binds data to the view
        public void bind(Post post){
            tvUsername.setText(post.getUser().getUsername());
            tvUsername2.setText(post.getUser().getUsername());
            tvDescription.setText(post.getDescription());
            String timestamp = getRelativeTimestamp(post.getCreatedAt().toString());
            tvTimestamp.setText(timestamp);
            ParseFile image = post.getImage();
            ParseFile profileImage = post.getProfileImage();

            // Show post image
            if(image != null){
                Glide.with(context).load(image.getUrl()).into(ivPostImage);
            }

            // Show rounded profile image
            if(profileImage != null){
                RequestOptions options = new RequestOptions()
                        .override(30, 30)
                        .centerCrop()
                        .circleCropTransform();
                Glide.with(context).load(profileImage.getUrl()).apply(options).into(ivProfileImg);
            }
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

    // Calculate relative timestamp
    public static String getRelativeTimestamp(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
