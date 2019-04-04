package com.dinesh.hungervalley;

public class Application extends android.app.Application {
    private String someVariable;

    @Override
    public void onCreate() {
        super.onCreate();


    }


    public String getSomeVariable() {
        return someVariable;
    }


    public String setSomeVariable(String someVariable) {
        this.someVariable = someVariable;
        return someVariable;
    }

}
