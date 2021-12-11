package network;

import entities.LoginRequest;
import entities.LoginResponse;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface API {

    @POST("token")
    @Headers("Accept: application/json")
    Single<LoginResponse> login(@Body LoginRequest loginRequest);

}
