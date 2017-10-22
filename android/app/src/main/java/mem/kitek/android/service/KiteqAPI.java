package mem.kitek.android.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import autodagger.AutoComponent;
import autodagger.AutoExpose;
import dagger.Module;
import dagger.Provides;
import lombok.SneakyThrows;
import lombok.val;
import mem.kitek.android.MemeApplication;
import mem.kitek.android.data.ApiData;
import mem.kitek.android.meta.scope.AppScope;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.exceptions.Exceptions;

/**
 * Created by cat on 10/21/17.
 */

@AppScope
@Module
public class KiteqAPI {
    private final
    ServiceAPI api;
    private final
    RawServiceAPI rawApi;
    private static final
    ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    public KiteqAPI() {
        val inter = new HttpLoggingInterceptor();
        inter.setLevel(HttpLoggingInterceptor.Level.BODY);
        val okhttp = new OkHttpClient.Builder().addInterceptor(inter).build();

        val retrofit = new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl("http://kitek.kostya.sexy/")
                .client(okhttp)
                .build();

        val retrofitNP = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl("http://kitek.kostya.sexy/")
                .client(okhttp)
                .build();

        api = retrofit.create(ServiceAPI.class);
        rawApi = retrofitNP.create(RawServiceAPI.class);
    }

    @SneakyThrows
    public static <T> T unwrap(Response<T> response) {
        if (response.isSuccessful())
            try {
                return response.body();
            } catch (Exception ignored) {}

        throw new ApiData.ApiException
                (mapper.readValue(response.raw().body().bytes(),
                        ApiData.ApiErr.class));
    }

    @Provides
    @AutoExpose(MemeApplication.class)
    public ServiceAPI getApi() {
        return KiteqAPI.this.api;
    }

    @Provides
    @AutoExpose(MemeApplication.class)
    public RawServiceAPI getRawApi() {
        return KiteqAPI.this.rawApi;
    }

    @Provides
    @AutoExpose(MemeApplication.class)
    public ObjectMapper getMapper() {
        return mapper;
    }
}
