package mem.kitek.android.service;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by cat on 10/22/17.
 */

public interface RawServiceAPI {
    @GET("categories/{cid}/{pid}.txt")
    Observable<Response<String>> getDesc(@Path("cid") Integer cid, @Path("pid") Integer pid);
}
