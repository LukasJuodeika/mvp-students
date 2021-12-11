package network;

import entities.Student;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Action;
import network.API;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;

public class Network {

    private final static String SERVER_URL = "https://www.google.lt/";
    private final Retrofit retrofit;
    private final API api;


    public Network() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
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

    public Completable login(String username, String pass) {
        return Completable.fromAction(() ->
                Thread.sleep(500)
        );
    }

    public Single<List<Student>> getAllStudents() {
        return Single.fromCallable(() ->
        {
            Thread.sleep(500);
            List<Student> students = new ArrayList<>();
            students.add(new Student("vardas", "pavarde"));
            students.add(new Student("vardas2", "pavarde2"));
            return students;
        });
    }

    public Completable createStudent(Student student) {
        return Completable.fromAction(() ->
                Thread.sleep(500)
        );
    }

    public Completable updateStudent(Student student) {
        return Completable.fromAction(() ->
                Thread.sleep(500)
        );
    }

    public Completable deleteStudent(Student student) {
        return Completable.fromAction(() ->
                Thread.sleep(500)
        );
    }
}
