package malltree.com.malltree;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import malltree.com.malltree.Recycler.ProductAdapter;
import malltree.com.malltree.RetrofitApi.HttpApi;
import malltree.com.malltree.RetrofitApi.Product;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.lang.Integer.parseInt;


public class MainActivity extends AppCompatActivity {

  // 이미지 선택 관련 변수
  final int REQ_CODE_SELECT_IMAGE = 100;

  List<Product> main_productList;
  public void setProduct (List<Product> product){
    this.main_productList = product;
  }

  public Context getContext (){
    return this.getBaseContext();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    //Q) url은 고정?
    String url = "http://192.168.56.1:8080/";

    Button btn01 = (Button)findViewById(R.id.btn01);
    Button btn02 = (Button)findViewById(R.id.btn02);

    Retrofit client = new Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    final HttpApi service = client.create(HttpApi.class);



    //RecyclerView
    final RecyclerView mainRecyclerView = (RecyclerView)findViewById(R.id.recycle_frame);

    //최초 화면 생성시 RecyclerView생성
    Call<List<Product>> listProduct = service.getProductList();
    listProduct.enqueue(new Callback<List<Product>>() {
      @Override
      public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
        if(response.isSuccessful()){
          List<Product> productList = response.body();
          ProductAdapter productAdapter = new ProductAdapter(getContext(), productList);
          mainRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
          mainRecyclerView.setAdapter(productAdapter);
        }else{
          Log.d("reponse_fail","First - listUpProduct fail");
        }
      }
      @Override
      public void onFailure(Call<List<Product>> call, Throwable t) {
        t.getMessage();
      }
    });

    //popup생성
    LayoutInflater inflater = getLayoutInflater();
    View inflaterView = (View)inflater.inflate(R.layout.layout_popup, null);
    addContentView(inflaterView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    final FrameLayout popFrame = (FrameLayout)findViewById(R.id.popup_frame);
    popFrame.setVisibility(View.GONE);

    //product 추가
    btn01.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        popFrame.setVisibility(View.VISIBLE);
      }
    }); //btn01.setOnClickListener

    // popup 등록 버튼
    Button btnAddProduct = (Button)findViewById(R.id.btn_confirmProductAdd);
    btnAddProduct.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        EditText inputProductName = (EditText)findViewById(R.id.input_productName);
        EditText inputProductPrice = (EditText)findViewById(R.id.input_productPrice);

        if (!inputProductName.getText().toString().equals("") && !inputProductPrice.getText().toString().equals("")){
          //입력값
          String valueProductName = inputProductName.getText().toString();
          Integer valueProductPrice = parseInt(inputProductPrice.getText().toString());

          Call<ResponseBody> addProductReq = service.addProduct(new Product(valueProductName,valueProductPrice));
          addProductReq.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
              if(response.isSuccessful()){
                try {
                  String result_addProductReq = response.body().string();
                  TextView txt01 = (TextView)findViewById(R.id.txt01);
                  txt01.setText("상품등록완료 → "+result_addProductReq);
                  Log.d("reponse_success","addProductReq : " + result_addProductReq);

                  // 완료 후 닫기
                  popFrame.setVisibility(View.GONE);

                } catch (IOException e) {
                  e.printStackTrace();
                }
              }else{
                Log.d("reponse_fail","addProductReq fail");
              }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
              t.getMessage();
            }
          }); //addProductReq.enqueue

        } else {
          Toast.makeText(MainActivity.this ,"값을 입력해주세요",Toast.LENGTH_LONG).show();
        } // if (!valueProductName.equals("") ...

      }
    }); //btnAddProduct.setOnClickListener

    // popup 취소 버튼
    Button btnCancelProduct = (Button)findViewById(R.id.btn_cancelProductAdd);
    btnCancelProduct.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        popFrame.setVisibility(View.GONE);
      }
    }); //btnCancelProduct.setOnClickListener

    // popup Dim
    FrameLayout popupDim = (FrameLayout)findViewById(R.id.popup_dim);
    popupDim.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        popFrame.setVisibility(View.GONE);
      }
    }); //popupDim.setOnClickListener



    //List 업데이트
    btn02.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Call<List<Product>> listUpProduct = service.getProductList();
        listUpProduct.enqueue(new Callback<List<Product>>() {
          @Override
          public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
            if(response.isSuccessful()){
              List<Product> productList = response.body();
              MainActivity.this.setProduct(productList);

              TextView txt02 = (TextView)findViewById(R.id.txt02);

              String innerString = "[";
              for ( Integer i = 0; i<productList.size();i++ ){
                innerString = innerString + "{\"name\" :\"" + productList.get(i).getName() + "\",";
                innerString = innerString + "\"price\" :" + productList.get(i).getPrice() + "}";
                if (i != (productList.size()-1)) {
                  innerString = innerString + ",";
                }
              }
              innerString = innerString +"]";
              txt02.setText(innerString);

              //버튼 클릭시 List - RecyclerView 갱신
              ProductAdapter productAdapter = new ProductAdapter(MainActivity.this, productList);
              mainRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
              mainRecyclerView.setAdapter(productAdapter);

            }else{
              Log.d("reponse_fail","listUpProduct fail");
            }
          }

          @Override
          public void onFailure(Call<List<Product>> call, Throwable t) {
            t.getMessage();
          }
        });
      }
    }); //btn02.setOnClickListener



// ##################################################################################

    // Image 등록 버튼
    Button btnAddImage = (Button)findViewById(R.id.btn_findPhoto);
    btnAddImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        int SDK_INT = Build.VERSION.SDK_INT;
        // 갤러리 사용 권한 체크( 사용권한이 없을경우 -1 ) (안드6.0이상만)
        // 권한이 없을경우
        if (SDK_INT>=23 && ContextCompat.checkSelfPermission(MainActivity.this,
            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
          // 최초 권한 요청인지, 혹은 사용자에 의한 재요청인지 확인
          if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
              Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // 사용자가 임의로 권한을 취소시킨 경우
            // 권한 재요청
            ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                REQ_CODE_SELECT_IMAGE);
          } else {
            // 최초로 권한을 요청하는 경우(첫실행)
            ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                REQ_CODE_SELECT_IMAGE);
          }
        } else {
          // 사용 권한이 있음을 확인한 경우 - 갤러리 불러오기
          Intent intent_Imgload = new Intent(Intent.ACTION_PICK);
          intent_Imgload.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
          intent_Imgload.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
          startActivityForResult(intent_Imgload, REQ_CODE_SELECT_IMAGE);
        } // if (SDK_INT>=23 && ...
      }
    }); //btnAddImage.setOnClickListener


// ##################################################################################


  } //onCreate - END

  //사진 받아온 결과
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data){
    if (requestCode == REQ_CODE_SELECT_IMAGE){
      if(resultCode== Activity.RESULT_OK){
        // 이미지뷰 선언
        ImageView popupProductImg = (ImageView)findViewById(R.id.selectedPhoto);

        //Uri로 이미 이름 읽어오기 : 맨아래 만들어둔 getImageNameToUri 메소드 사용
        String imgName_String = getImageNameToUri(data.getData(),"name");
        Log.d("imgName_String", imgName_String);
        String imgName_String_path = getImageNameToUri(data.getData(),"path");
        Log.d("imgName_String_path", imgName_String_path);

        //비트맵으로 읽어오기
        try {
          Bitmap imgName_Bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
          popupProductImg.setImageBitmap(imgName_Bitmap);//이미지 불러오기
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  } // onActivityResult  - END

  // Uri 구하기
  public String getImageNameToUri(Uri data,String returnType){
    String[] proj = { MediaStore.Images.Media.DATA };
    Cursor cursor = managedQuery(data, proj, null, null, null);
    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

    cursor.moveToFirst();

    String imgPath = cursor.getString(column_index);
    String imgName = imgPath.substring(imgPath.lastIndexOf("/")+1);
    if (returnType.equals("path")){
      return imgPath;
    } else {
      return imgName;
    }
  }


}
