package com.example.tradeoff;

public class Post {
    private String give;
    private String take;
    private String mail;
    private String keyPost;


    public Post() {
        this.mail = "";
        this.give = "";
        this.take = "";
        this.keyPost = "";
    }


    public Post(String GiveAsk, String TakeAsk, String _mail, String _key) {
        give = GiveAsk;
        take = TakeAsk;
        mail = _mail;
        keyPost = _key;
    }

    public void setGive(String give) {
        this.give = give;
    }

    public void setTake(String take) {
        this.take = take;
    }

    public String getGive() {
        return give;
    }

    public String getTake() {
        return take;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setKeyPost(String keyPost) {
        this.keyPost = keyPost;
    }

    public String getKeyPost() {
        return keyPost;
    }


}