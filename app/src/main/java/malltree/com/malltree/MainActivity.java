package malltree.com.malltree;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import malltree.com.malltree.Api.Api;
import malltree.com.malltree.Api.Repo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {



  @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      //  서버 주소 (현재 localhost ip)
      final String GET_POST = "http://192.168.43.214:8080/";

      final TextView txt01 = (TextView)findViewById(R.id.txt01);
      final Button btn01 = (Button)findViewById(R.id.btn01);

      Retrofit client = new Retrofit.Builder()
          .baseUrl(GET_POST)
          .addConverterFactory(GsonConverterFactory.create()).build();

      final Api service = client.create(Api.class);

      final Call<Repo> repo = service.repo(111,"test");
      Log.d("repo", repo.toString());


      btn01.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

          Log.d("repo", repo.request().toString());

          repo.enqueue(new Callback<Repo>() {
            @Override
            public void onResponse(Call<Repo> call, Response<Repo> response) {
              if (response.isSuccessful()) {
                Repo repo = response.body();
                txt01.setText(String.valueOf(repo.getContent()));
              } else {

              }
            }

            @Override
            public void onFailure(Call<Repo> call, Throwable t) {

            }

          });


        }
      }); //setOnClickListener

    }

}
