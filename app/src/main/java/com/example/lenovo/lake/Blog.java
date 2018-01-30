package com.example.lenovo.lake;

public class Blog {
    private String postTitle,postDesc,postImage,profileImage,userName,upCounter,downCounter;

    public Blog() {
    }

    public Blog(String postTitle, String postDesc, String postImage,String profileImage,String userName,String upCounter,String downCounter) {
        this.postTitle = postTitle;
        this.postDesc = postDesc;
        this.postImage = postImage;
        this.profileImage = profileImage;
        this.userName = userName;
        this.upCounter = upCounter;
        this.downCounter = downCounter;

    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostDesc() {
        return postDesc;
    }

    public void setPostDesc(String postDesc) {
        this.postDesc = postDesc;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getupCounter() {
        return upCounter;
    }

    public String getdownCounter() {
        return downCounter;
    }

    public void setupCounter() {
        this.upCounter = Integer.toString(Integer.parseInt(upCounter)+1);
    }

    public void setdownCounter() {
        int a = Integer.parseInt(getdownCounter());
        a++;
        this.downCounter = ""+a;
    }

    public void decdownCounter() {
        int a = Integer.parseInt(getdownCounter());
        a--;
        this.downCounter = ""+a;
    }


    public void decreaseup() {
        this.upCounter = Integer.toString(Integer.parseInt(upCounter)-1);
    }
}
