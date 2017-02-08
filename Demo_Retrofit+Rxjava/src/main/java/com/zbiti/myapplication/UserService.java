package com.zbiti.myapplication;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by admin on 2016/5/25.
 */
public interface UserService {
    @GET("login")
    public Observable<HttpResult<User>> login(@Query("username") String username, @Query("password") String password);
}
