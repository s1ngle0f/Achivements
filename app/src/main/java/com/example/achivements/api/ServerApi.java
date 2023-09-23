package com.example.achivements.api;

import com.example.achivements.models.Achivement;
import com.example.achivements.models.ApiResponse;
import com.example.achivements.models.AuthentificationRequest;
import com.example.achivements.models.Status;
import com.example.achivements.models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.Getter;
import lombok.Setter;
import okhttp3.*;
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
    private static final String BASE_URL = "http://192.168.0.136:8080";
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
            throw new RuntimeException(e);
        }
        return res;
    }

    public List<User> getUsers() {
        List<User> res = null;
        Call<List<User>> callGetUsers = serverApi.getUsers();
        try{
            res = callGetUsers.execute().body();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return res;
    }

    public List<User> getUsersByUsername(String username) {
        List<User> res = null;
        Call<List<User>> callGetUsers = serverApi.getUsersByUsername(username);
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

    public User getUserByAuth() {
        User res = null;
        Call<User> callGetUser = serverApi.getUserByUserName(null);
        try{
            res = callGetUser.execute().body();
        }catch (Exception e){}
        return res;
    }

    public User createUser(User user) {
        User res = null;
        User _user = User.getUserForExport(user);
        Call<User> callCreateUser = serverApi.createUser(_user);
        try{
            res = callCreateUser.execute().body();
        }catch (Exception e){}
        return res;
    }

    public User editUser(User user) {
        User res = null;
        User _user = User.getUserForExport(user);
        Call<User> callEditUser = serverApi.editUser(_user);
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

    public byte[] getAvatar(){
        ResponseBody res = null;
        Call<ResponseBody> call = serverApi.getAvatar();
        try{
            res = call.execute().body();
            assert res != null;
            return res.bytes();
        }catch (Exception e){}
        return null;
    }

    public byte[] getAvatarById(int id){
        ResponseBody res = null;
        Call<ResponseBody> call = serverApi.getAvatarById(id);
        try{
            res = call.execute().body();
            assert res != null;
            return res.bytes();
        }catch (Exception e){}
        return null;
    }

    public void loadAvatar(String path){
        File fileToUpload = null;
        System.out.println("PATH: " + path);
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

    public void loadAvatar(File fileToUpload){
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), fileToUpload);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", fileToUpload.getName(), requestFile);

        Call<Void> call = serverApi.loadAvatar(body);
        try{
            call.execute();
        }catch (Exception e){}
    }

    public void loadAvatar(String filename, byte[] bytes){
        System.out.println("START LOAD: " + filename);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), bytes);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", filename, requestFile);

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

    public void loadImageAchivement(byte[] bytes, Achivement achivement){
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), bytes);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", achivement.getId() + ".png", requestFile);

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

    public boolean validJwt(){
        boolean res = false;
        Call<Boolean> call = serverApi.validJwt();
        try{
            res = call.execute().body();
            return res;
        }catch (Exception e){}
        return false;
    }

    public static String getJwt() {
        return jwt;
    }

    public static void setJwt(String jwt) {
        ServerApi.jwt = jwt;
    }
}
