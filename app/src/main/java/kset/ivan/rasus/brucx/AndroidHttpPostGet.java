package kset.ivan.rasus.brucx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AndroidHttpPostGet {
    OkHttpClient client;
    MediaType JSON;
    private int data;

    public AndroidHttpPostGet() {

        client = new OkHttpClient();
        JSON = MediaType.parse("application/json; charset=utf-8");


    }


    private void getAllStudents() {

        final String login = "cona";
        final String password = "cona123";
        String credential = Credentials.basic(login, password);

        final Request request = new Request.Builder()
                .url("http://tomcat.marinpetrunic.com:80/brucx-ws/api/v1/students")
                .header("Authorization", credential)
                .build();

        ApiHelper.getInstance().getOkHttpClient().newCall(request).enqueue(getAllStudentsCallBack);

    }

    Callback getAllStudentsCallBack = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {Log.e("cona", e.getMessage());}

        @Override
        public void onResponse(Call call, Response response) throws IOException {

            List<Studenti> studenti = new ArrayList<>();

            String jsonResponse = response.body().string();

            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(jsonResponse);
            JsonArray students = jsonElement.getAsJsonArray();

            Gson gson = new Gson();
            for (JsonElement object : students) {
                studenti.add(gson.fromJson(object, Studenti.class));
            }
            //User retval=users.get();
            Log.d("cona", "studenti:");
            Log.d("cona", studenti.toString());


            Log.d("cona", response.body().string());



        }
    };


    private void getStudentById(){


        final String login = "cona";
        final String password = "cona123";
        String credential = Credentials.basic(login, password);

        final Request request = new Request.Builder()
                .url("http://tomcat.marinpetrunic.com:80/brucx-ws/api/v1/students")
                .header("Authorization", credential)
                .build();

        ApiHelper.getInstance().getOkHttpClient().newCall(request).enqueue(getStudentByIdCallBack);
    }

    Callback getStudentByIdCallBack = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {Log.e("cona", e.getMessage());}

        @Override
        public void onResponse(Call call, Response response) throws IOException {

            List<Studenti> studenti = new ArrayList<>();

            String jsonResponse = response.body().string();

            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(jsonResponse);
            JsonArray students = jsonElement.getAsJsonArray();

            Gson gson = new Gson();
            for (JsonElement object : students) {
                studenti.add(gson.fromJson(object, Studenti.class));
            }

            for(int i = 0; i < studenti.size(); i++) {
                if (studenti.get(i).getNfcId().equals("AFF53F0B"))
                    Log.d("cona", studenti.get(i).toString());

            }


        }
    };

    public void buyTicket(String id) {

        final String login = "cona";
        final String password = "cona123";
        String credential = Credentials.basic(login, password);



        JsonObject json = new JsonObject();
        json.addProperty("type", "test-karta" );
        json.addProperty("price", 20);
        String jsonString = json.toString();
        RequestBody body = RequestBody.create(JSON, jsonString);


        final Request request = new Request.Builder()
                .url("http://tomcat.marinpetrunic.com:80/brucx-ws/api/v1/students/" + id + "/tickets")
                .header("Authorization", credential)
                .post(body)
                .build();



        ApiHelper.getInstance().getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("cona", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {

                    //success
                    //Log.d("cona", response.body().string());
                    setData(response.code());


                } else {
                    Log.d("cona", "fail");
                    //failure
                }
            }
        });
    }

    public void deleteTicket(){

        final String login = "cona";
        final String password = "cona123";
        String credential = Credentials.basic(login, password);

        final Request request = new Request.Builder()
                .method("DELETE", null)
                .url("http://tomcat.marinpetrunic.com:80/brucx-ws/api/v1/students/AFF53F0B/tickets/test-karta")
                .header("Authorization", credential)
                .build();

        ApiHelper.getInstance().getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("cona", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    //success
                    Log.d("cona", response.body().string());
                } else {
                    Log.d("cona", "fail");
                    //failure
                }
            }
        });



    }

    public void useTicket(){

        final String login = "cona";
        final String password = "cona123";
        String credential = Credentials.basic(login, password);

        JsonObject json = new JsonObject();
        json.addProperty("used", 1 );
        String jsonString = json.toString();
        RequestBody body = RequestBody.create(JSON, jsonString);


        final Request request = new Request.Builder()
                .url("http://tomcat.marinpetrunic.com:80/brucx-ws/api/v1/students/AFF53F0B/tickets/test-karta")
                .header("Authorization", credential)
                .put(body)
                .build();

        ApiHelper.getInstance().getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("cona", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    //success
                    Log.d("cona", response.body().string());

                } else {
                    Log.d("cona", "fail");
                    //failure
                }
            }
        });


    }

    public void setData(int data){
        this.data=data;
    }

    public int getData(){
        return data;
    }

}

