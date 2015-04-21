package ch.uzh.ifi.seal.soprafs15.group_09_android.service;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.gson.AutoValueAdapterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

public class RestService {

    private final static String baseUrl = "http://10.0.2.2:8080/"; // 10.0.2.2 for Android Emulator
    //private final static String baseUrl = "http://sopra-fs15-group09.herokuapp.com"; // Hiroku URL
    //private final static String baseUrl = "http://private-de094-sopra.apiary-mock.com/"; // Apiary URL

    public RestApiInterface restApiInterface;
    private static RestService instance;

    private RestService(Context context, Client client) {

        Gson gson = new GsonBuilder().registerTypeAdapterFactory(new AutoValueAdapterFactory()).create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(baseUrl)
                .setClient(client)
                .setConverter(new GsonConverter(gson))
                .build();

        boolean isDebuggable = (0 != (context.getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE));

        if (isDebuggable) {
            restAdapter.setLogLevel(RestAdapter.LogLevel.BASIC);
        }

        restApiInterface = restAdapter.create(RestApiInterface.class);
    }

    public static RestApiInterface getInstance(Context context) {

        if (instance == null) {
            instance = new RestService(context, new OkClient());
        }

        return instance.restApiInterface;
    }

}
