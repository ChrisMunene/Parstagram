package com.example.parstagram.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.parstagram.EndlessRecyclerViewScrollListener;
import com.example.parstagram.LoginActivity;
import com.example.parstagram.R;
import com.example.parstagram.adapters.ProfileAdapter;
import com.example.parstagram.model.Post;
import com.example.parstagram.model.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    private RecyclerView rvPosts;
    private ProfileAdapter adapter;
    private List<Post> mPosts;
    private ProgressDialog pd;
    private SwipeRefreshLayout swipeContainer;
    private EndlessRecyclerViewScrollListener scrollListener;
    private Boolean isRefreshing = false;
    private TextView tvFullname;
    private TextView tvBio;
    private TextView tvPostCount;
    private TextView tvFollowersCount;
    private TextView tvFollowingCount;
    private ImageView ivProfileImage;
    private Button logoutBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPosts = view.findViewById(R.id.rvPosts);
        tvFullname = view.findViewById(R.id.tvFullname);
        tvBio = view.findViewById(R.id.tvBio);
        tvPostCount = view.findViewById(R.id.tvPostCount);
        tvFollowersCount = view.findViewById(R.id.tvFollowersCount);
        tvFollowingCount = view.findViewById(R.id.tvFollowingCount);
        ivProfileImage = view.findViewById(R.id.ivProfileImage);
        logoutBtn = view.findViewById(R.id.logoutBtn);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser();
            }
        });

        // Create data source
        mPosts = new ArrayList<>();
        // Create adapter
        adapter = new ProfileAdapter(getContext(), mPosts, getFragmentManager());
        // Set adapter on RecyclerView
        rvPosts.setAdapter(adapter);
        // Set LayoutManager on RecyclerView
        GridLayoutManager gridLayoutManager =  new GridLayoutManager(getContext(), 3);
        rvPosts.setLayoutManager(gridLayoutManager);

        // Initialize Progress Dialog
        pd = new ProgressDialog(getContext());
        pd.setTitle("Loading...");
        pd.setMessage("Please wait.");
        pd.setCancelable(false);

        // Load timeline
        loadPage(0);
    }

    // Loads timeline -- Top 20 posts
    private void loadPage(int page){

        Log.d("CreatePostActivity", String.format("Page: %s", page));

        // Show progress if this is the initial load
        if(!isRefreshing) pd.show();

        // Initialize post query
        final Post.Query postQuery = new Post.Query();

        // Set query parameters -- Get top posts with nested users
        postQuery.getTop().withUser().descending().paginate(page).forCurrentUser();

        // Get posts from parse server -- async
        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if(e == null){

                    // Clear adapter only on refresh
                   // if(isRefreshing) adapter.clear();

                    User user = (User) posts.get(0).getUser();
                    showUserInfo(user);

                    //Success
                    for(int i = 0; i < posts.size(); i++){
                        Log.d("CreatePostActivity", String.format("Post [%s] Username: %s Description: %s Date: %s", i, posts.get(i).getUser().getUsername(), posts.get(i).getDescription(),
                                posts.get(i).getCreatedAt()));
                    }

                    mPosts.addAll(posts);
                    adapter.notifyDataSetChanged();
                } else {
                    //error
                    e.printStackTrace();
                }

                // Dismiss progress dialog
                if(pd.isShowing()) pd.dismiss();

            }
        });
    }

    private void showUserInfo(User user){
        tvFullname.setText(user.getFullName());
        tvBio.setText(user.getBio());
        tvFollowersCount.setText("120");
        tvFollowingCount.setText("300");
        tvPostCount.setText("10");

        ParseFile profileImage = user.getProfileImage();
        if(profileImage != null){
            RequestOptions options = new RequestOptions()
                    .override(120, 120)
                    .centerCrop()
                    .circleCropTransform();
            Glide.with(getContext()).load(profileImage.getUrl()).apply(options).into(ivProfileImage);
        }
    }

    // Logs out the current user
    private void logoutUser(){
        ParseUser.logOut();
        final Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
