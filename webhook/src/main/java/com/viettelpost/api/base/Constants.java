package com.viettelpost.api.base;

/**
 * Created by Oto on 16/10/2018.
 */
public class Constants {
    public final static Long expired = 1000L * 60 * 60 * 24 * 30;
    public final static String userId = "UserId";
    public final static String partner = "Partner";
    public final static String token = "Token";
    public final static String from = "FromSource";

    public final static int nullToken = 300;

    public final static int success = 200;
    public final static int tokenInvalid = 201;
    public final static int headerInvalid = 202;
    public final static int dataInvalid = 203;
    public final static int dataDbInvalid = 204;
    public final static int errorSystem = 205;

    public final static String apiURL = "https://graph.facebook.com/v7.0/me/messages";
    public final static String access_token = "EAAJ54aXeoGUBAJLr6KZCtI3w1oYUoyZAfnK895u0c4BGKtd5TzqmZAyobBrZAJOrByvkfgiGThFzauYXSScIHFZCN87RsP9Lz03kGhLcaeToaVZC6VAM8BCXfKQTvSJQwtrWre1cnTIybNiwXBkSLEwri3NnWsl9B94oDZBBj5b2QZDZD";
    public final static String verify_token = "dobietmalagi";
}
