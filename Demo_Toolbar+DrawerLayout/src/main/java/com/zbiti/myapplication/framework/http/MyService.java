package com.zbiti.myapplication.framework.http;

import com.zbiti.myapplication.entity.User;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by admin on 2016/6/21.
 */
public interface MyService {
    @GET("/findUsers")
    public Observable<HttpResult<List<User>>> findUsers(@Query("name") String name);
}
