package malltree.com.malltree.Recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import malltree.com.malltree.R;

/**
 * Created by pjklo on 2016-10-19.
 */

public class ProductViewholder extends RecyclerView.ViewHolder {
  public ImageView productImg;
  public TextView productTxt01,productTxt02;

  private ProductAdapter viewHolderAdapter;


  public ProductViewholder(View baseView, ProductAdapter productAdapter) {
    super(baseView);
    viewHolderAdapter = productAdapter;
    productImg = (ImageView)baseView.findViewById(R.id.recycle_img);
    productTxt01 = (TextView)baseView.findViewById(R.id.recycle_txt01);
    productTxt02 = (TextView)baseView.findViewById(R.id.recycle_txt02);

  }
}
