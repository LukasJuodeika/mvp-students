package network;

import entities.CreateStudentRequest;
import entities.Student;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.*;

import java.util.List;

public interface API {

    @POST("students")
    @Headers("Accept: application/json")
    Single<Student> createStudent(@Body CreateStudentRequest student);

    @GET("students")
    @Headers("Accept: application/json")
    Single<List<Student>> getStudents();

    @PUT("students/{id}")
    @Headers("Accept: application/json")
    Single<Student> updateStudent(@Path("id") Long id, @Body Student student);

    @DELETE("students/{id}")
    @Headers("Accept: application/json")
    Completable deleteStudent(@Path("id") Long id);
}
