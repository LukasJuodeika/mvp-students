package network;

import entities.CreateStudentRequest;
import entities.Student;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;
import java.util.logging.Logger;

public class Network {

    private final static String SERVER_URL = "http://localhost:8080/api/";
    private final Retrofit retrofit;
    private final API api;


    public Network() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(System.out::println);
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.addInterceptor(loggingInterceptor);
        retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(clientBuilder.build())
                .build();
        api = retrofit.create(API.class);
    }

    public Single<List<Student>> getAllStudents() {
        return api.getStudents();
    }

    public Single<Student> createStudent(String name, String surname) {
        return api.createStudent(new CreateStudentRequest(name, surname));
    }

    public Single<Student> updateStudent(Student student) {
        return api.updateStudent(student.getId(), student);
    }

    public Completable deleteStudent(Student student) {
        return api.deleteStudent(student.getId());
    }
}
