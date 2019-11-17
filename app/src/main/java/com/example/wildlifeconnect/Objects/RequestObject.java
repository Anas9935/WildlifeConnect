package com.example.wildlifeconnect.Objects;

public class RequestObject {
    private FlagObject flag;
    private Users user;
    private int choice;     //0->flag and 1-> user


    public RequestObject(FlagObject flag, Users user,int ch)
    {
        this.flag = flag;
        this.user = user;
        choice=ch;
    }

    public FlagObject getFlag() {
        return flag;
    }

    public void setFlag(FlagObject flag) {
        this.flag = flag;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public int getChoice() {
        return choice;
    }

    public void setChoice(int choice) {
        this.choice = choice;
    }
}
