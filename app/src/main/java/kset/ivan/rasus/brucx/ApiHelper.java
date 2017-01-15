package kset.ivan.rasus.brucx;

import okhttp3.OkHttpClient;

/**
 * Created by cona on 19.12.16..
 */

public final class ApiHelper {

    private static ApiHelper instance;

    private OkHttpClient okHttpClient;

    private ApiHelper() {
        okHttpClient = new OkHttpClient();
    }

    public static ApiHelper getInstance() {
        if (instance == null) {
            instance = new ApiHelper();
        }

        return instance;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }
}
