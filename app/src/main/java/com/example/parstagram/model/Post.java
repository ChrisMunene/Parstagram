package com.example.parstagram.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("Post")
public class Post extends ParseObject {
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_USER = "user";
    private static final String KEY_CREATED_AT = "createdAt";
    private static final String KEY_PROFILE_IMAGE = "profileImage";
    private static final String KEY_LIKES = "likes";
    private static final int PAGE_SIZE = 20;

    public String getDescription(){
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description){
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage(){
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image){
        put(KEY_IMAGE, image);
    }

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }

    public ParseFile getProfileImage() {return getUser().getParseFile(KEY_PROFILE_IMAGE);}

    public void likePost(ParseUser user){put(KEY_LIKES, user);}

    public static class Query extends ParseQuery<Post>{
        public Query(){
            super(Post.class);
        }

        public Query getTop(){
            setLimit(PAGE_SIZE);
            return this;
        }

        public Query withUser(){
            include(KEY_USER);
            return this;
        }

        public Query ascending(){
            orderByAscending(KEY_CREATED_AT);
            return this;
        }

        public Query descending(){
            orderByDescending(KEY_CREATED_AT);
            return this;
        }

        public Query paginate(int page){
            setSkip(page * PAGE_SIZE);
            return this;
        }

        public Query forCurrentUser(){
            whereEqualTo(KEY_USER, ParseUser.getCurrentUser());
            return this;
        }
    }
}
