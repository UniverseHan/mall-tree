package malltree.com.malltree.Api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by pjklo on 2016-10-07.
 */

public interface Api {
  @GET("/test")
  Call<Repo> repo(@Query("age") int age, @Query("content") String content);
}

