package com.example.achivements.api;

import lombok.Getter;
import lombok.Setter;
import okhttp3.*;
import org.example.model.*;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class ServerApi {
    private static final String BASE_URL = "http://localhost:8080";
    @Getter
    @Setter
    private static String jwt = "";
    OkHttpClient client;
    Retrofit retrofit;
    IServerApi serverApi;

    public ServerApi(){
        client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("Authorization", "Bearer " + jwt)
                                .build();
                        return chain.proceed(request);
                    }
                })
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL) // Замените на ваш адрес сервера
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        serverApi = retrofit.create(IServerApi.class);
    }

    public String login(AuthentificationRequest loginParams){
        String res = null;
        Call<ApiResponse> callLogin = serverApi.login(loginParams);
        try{
            res = callLogin.execute().body().getToken();
        }catch (Exception e){
            System.out.println(e.toString());
        }
        return res;
    }

    public List<User> getUsers() {
//        Call<List<User>> callGetUsers = serverApi.getUsers();
//        callGetUsers.enqueue(new Callback<List<User>>() {
//            @Override
//            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
//                if(response.isSuccessful()){
//                    List<User> users = response.body();
//                    assert users != null;
//                    for (User user : users) {
//                        System.out.println(user);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<User>> call, Throwable t) {
//
//            }
//        });
        List<User> res = null;
        Call<List<User>> callGetUsers = serverApi.getUsers();
        try{
            res = callGetUsers.execute().body();
        }catch (Exception e){}
        return res;
    }

    public User getUser(int id) {
        User res = null;
        Call<User> callGetUser = serverApi.getUser(id);
        try{
            res = callGetUser.execute().body();
        }catch (Exception e){}
        return res;
    }

    public User getUserByUsername(String username) {
        User res = null;
        Call<User> callGetUser = serverApi.getUserByUserName(username);
        try{
            res = callGetUser.execute().body();
        }catch (Exception e){}
        return res;
    }

    public User createUser(User user) {
        User res = null;
        Call<User> callCreateUser = serverApi.createUser(user);
        try{
            res = callCreateUser.execute().body();
        }catch (Exception e){}
        return res;
    }

    public User editUser(User user) {
        User res = null;
        Call<User> callEditUser = serverApi.editUser(user);
        try{
            res = callEditUser.execute().body();
        }catch (Exception e){}
        return res;
    }

    public User deleteUser(int id) {
        User res = null;
        Call<User> callDeleteUser = serverApi.deleteUser(id);
        try{
            res = callDeleteUser.execute().body();
        }catch (Exception e){}
        return res;
    }

    public byte[] getAvatar(String path){
        ResponseBody res = null;
        Call<ResponseBody> call = serverApi.getAvatar();
        try{
            res = call.execute().body();
            assert res != null;
            return res.bytes();
        }catch (Exception e){}
        return null;
    }

    public void loadAvatar(String path){
        File fileToUpload = null;
        try {
            fileToUpload = new File(new String(path.getBytes("Windows-1251"), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), fileToUpload);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", fileToUpload.getName(), requestFile);

        Call<Void> call = serverApi.loadAvatar(body);
        try{
            call.execute();
        }catch (Exception e){}
    }

    public byte[] getImageAchivement(Achivement achivement){
        ResponseBody res = null;
        Call<ResponseBody> call = serverApi.getImageAchivement(achivement.getId());
        try{
            res = call.execute().body();
            assert res != null;
            return res.bytes();
        }catch (Exception e){}
        return null;
    }

    public void loadImageAchivement(String path, Achivement achivement){
        File fileToUpload = null;
        try {
            fileToUpload = new File(new String(path.getBytes("Windows-1251"), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
//        File fileToUpload = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), fileToUpload);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", fileToUpload.getName(), requestFile);

        Call<Void> call = serverApi.loadImageAchivement(achivement.getId(), body);
        try{
            call.execute();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public User getNewAchivement(Status statusLastAchivement){
        User res = null;
        Call<User> call = serverApi.getNewAchivement(statusLastAchivement);
        try{
            res = call.execute().body();
        }catch (Exception e){}
        return res;
    }
}
