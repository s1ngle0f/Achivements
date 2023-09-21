package com.example.achivements.api;

import com.example.achivements.models.ApiResponse;
import com.example.achivements.models.AuthentificationRequest;
import com.example.achivements.models.Status;
import com.example.achivements.models.User;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface IServerApi {
    @GET("/users")
    Call<List<User>> getUsers();

    @GET("/users")
    Call<List<User>> getUsersByUsername(@Query(value = "username") String username);

    @GET("/user/{id}")
    Call<User> getUser(@Path(value = "id") int id);

    @GET("/user")
    Call<User> getUserByUserName(@Query(value = "username") String username);

    @PUT("/user")
    Call<User> createUser(@Body User user);

    @POST("/user")
    Call<User> editUser(@Body User user);

    @DELETE("/user/{id}")
    Call<User> deleteUser(@Path(value = "id") int id);

    @POST("/login")
    Call<ApiResponse> login(@Body AuthentificationRequest user);

    @GET("/image/avatar")
    Call<ResponseBody> getAvatar();

    @Multipart
    @POST("/image/avatar")
    Call<Void> loadAvatar(@Part MultipartBody.Part file);

    @GET("/image/{id}")
    Call<ResponseBody> getImageAchivement(@Path(value = "id") int id);

    @Multipart
    @POST("/image/{id}")
    Call<Void> loadImageAchivement(@Path(value = "id") int id, @Part MultipartBody.Part file);

    @POST("/get_new_achivement")
    Call<User> getNewAchivement(@Body Status statusLastAchivement);
}
