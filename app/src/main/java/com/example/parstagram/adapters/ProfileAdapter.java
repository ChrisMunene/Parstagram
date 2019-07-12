package com.example.parstagram.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.parstagram.R;
import com.example.parstagram.fragments.ProfileDetailsFragment;
import com.example.parstagram.model.Post;
import com.parse.ParseFile;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder>{

    private Context context;
    private FragmentManager fragmentManager;
    private List<Post> posts;

    public ProfileAdapter(Context context, List<Post> posts, FragmentManager fragmentManager) {
        this.context = context;
        this.posts = posts;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post_profile, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post, position);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{


        private ImageView ivGridImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivGridImage = itemView.findViewById(R.id.ivGridImage);
        }

        // Binds data to the view
        public void bind(Post post, final int position){
            final int pos = position;
            ParseFile image = post.getImage();
            // Show post image
            if(image != null){
                RequestOptions options = new RequestOptions()
                        .override(ivGridImage.getWidth(), ivGridImage.getHeight())
                        .centerCrop();

                Glide.with(context).load(image.getUrl()).apply(options).into(ivGridImage);


                // When user clicks an image go to details view
                ivGridImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Fragment frag = new ProfileDetailsFragment(position);
                        FragmentTransaction ft = fragmentManager.beginTransaction();
                        ft.replace(R.id.flContainer, frag);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                });
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
}

