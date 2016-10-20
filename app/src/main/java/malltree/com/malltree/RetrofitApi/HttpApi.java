package malltree.com.malltree.RetrofitApi;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by pjklo on 2016-10-07.
 */

public interface HttpApi {

  @POST("/get-items")
  Call<ResponseBody> addProduct(@Body Product info);

  @GET("/list-items")
  Call<List<Product>> getProductList();
}

