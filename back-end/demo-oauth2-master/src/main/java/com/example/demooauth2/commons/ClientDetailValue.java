package com.example.demooauth2.commons;

import java.util.ArrayList;
import java.util.List;

public class ClientDetailValue {
    public static final int TOKEN_VALIDITY_SECONDS = 3600;
    public static final int REFRESH_TOKEN_VALIDITY_SECONDS = 360000;
    public static final List<String> SCOPE_DEFAULT = new ArrayList<>(){
        {
            add("READ");
        }
    };
    public static final List<String> AUTHORIZE_DEFAULT = new ArrayList<>(){
        {
            add("refresh_token");
            add("authorization_code");
        }
    };
    public static final List<String> RESOURCE_ID = new ArrayList<>(){
        {
            add("inventory");
        }
    };
}
