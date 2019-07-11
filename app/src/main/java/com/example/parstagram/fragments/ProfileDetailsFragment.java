package com.example.parstagram.fragments;

import android.util.Log;

import com.example.parstagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.List;

public class ProfileDetailsFragment extends HomeFragment {
    @Override
    protected void loadPosts(int page) {

        // Show progress if this is the initial load
        if(!isRefreshing) pd.show();

        // Initialize post query
        final Post.Query postQuery = new Post.Query();

        // Set query parameters -- Get top posts with nested users
        postQuery.getTop().withUser().descending().forCurrentUser().paginate(page);

        // Get posts from parse server -- async
        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if(e == null){

                    // Clear adapter only on refresh
                    if(isRefreshing) adapter.clear();

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

                // Reset swipe container
                if(isRefreshing){
                    swipeContainer.setRefreshing(false);
                    isRefreshing = false;
                }

            }
        });
    }
}
