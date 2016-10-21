package malltree.com.malltree.Recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import malltree.com.malltree.R;
import malltree.com.malltree.RetrofitApi.Product;

/**
 * Created by pjklo on 2016-10-19.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductViewholder> {

  //메인에서 adapter 선언시 받아올 context, List<Product>
  private Context mainContext;
  private List<Product> mainListProduct;
  public ProductAdapter (Context mContext , List<Product> mListProduct){
    mainContext = mContext;
    mainListProduct = mListProduct;
  }

  @Override
  public ProductViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
    View baseView = View.inflate(mainContext, R.layout.recycler_product, null);
    ProductViewholder productViewholder = new ProductViewholder(baseView, this);
    return productViewholder;
  }

  @Override
  public void onBindViewHolder(ProductViewholder holder, int position) {
    Product product = mainListProduct.get(position);

    holder.productTxt01.setText(product.getName());
    holder.productTxt02.setText(product.getPrice().toString());
  }

  @Override
  public int getItemCount() {
    return mainListProduct.size();
  }
}
