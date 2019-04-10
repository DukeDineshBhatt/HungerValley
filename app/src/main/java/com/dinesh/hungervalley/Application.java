package com.dinesh.hungervalley;

public class Application extends android.app.Application {
    private String someVariable;
    private String cartVariable;

    @Override
    public void onCreate() {
        super.onCreate();


    }

    public String getCartVariable() {
        return cartVariable;
    }

    public String setCartVariable(String cartVariable) {
        this.cartVariable = cartVariable;
        return cartVariable;
    }

    public String getSomeVariable() {
        return someVariable;
    }


    public String setSomeVariable(String someVariable) {
        this.someVariable = someVariable;
        return someVariable;
    }

}
