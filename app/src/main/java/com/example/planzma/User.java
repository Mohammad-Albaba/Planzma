package com.example.planzma;

public class User {
    private String Uid;
    private String email;
    private String name;
    private String phone;
    private String txtPost;
    private String dateTime;
    private String Like;
    private String comment;
    private String postId;
    private String imageProfile;
    private String imagePost;

    public User() {
    }

    public User(String uid, String email, String name, String phone, String txtPost, String dateTime, String like, String comment, String postId, String imageProfile, String imagePost) {
        Uid = uid;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.txtPost = txtPost;
        this.dateTime = dateTime;
        Like = like;
        this.comment = comment;
        this.postId = postId;
        this.imageProfile = imageProfile;
        this.imagePost = imagePost;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
       Uid = uid;
    }

    public String getEmail(String imageUriProfile) {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTxtPost() {
        return txtPost;
    }

    public void setTxtPost(String txtPost) {
        this.txtPost = txtPost;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getLike() {
        return Like;
    }

    public void setLike(String like) {
        Like = like;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
    }

    public String getImagePost() {
        return imagePost;
    }

    public void setImagePost(String imagePost) {
        this.imagePost = imagePost;
    }
}
