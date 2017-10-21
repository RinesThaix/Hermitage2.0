package mem.kitek.android.service;

import mem.kitek.android.data.ApiData;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by cat on 10/21/17.
 */

public interface ServiceAPI {
    @GET("getPictures")
    Observable<Response<ApiData.PictureData>> getPictureInfo(@Query("amount") int amount);

    @GET("getHallById")
    Observable<Response<ApiData.HallInfo>> getHallInfo(@Query("hall_id") int hallId);

    // typeme!
    @GET("categories/{cid}/{pid}.jpg")
    Observable<Response<?>> getPicture(@Path("cid") int cid, @Path("pid") int pid);

    @GET("bunchPeopleInHalls")
    Observable<Response<ApiData.PeopleList>> getBatchPop(@Query("halls") String halllist);
}
