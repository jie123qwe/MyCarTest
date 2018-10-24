package bw.com.mycartest.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import bw.com.mycartest.MathView;
import bw.com.mycartest.R;
import bw.com.mycartest.ShopBean;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.MyHolder>{
    private Context context;
    private List<ShopBean.DataBean.ListBean> list = new ArrayList<>();

    public ShopAdapter(Context context, List<ShopBean.DataBean.ListBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(context,R.layout.shop_item,null);
        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, final int i) {
        ShopBean.DataBean.ListBean bean = list.get(i);
        double price = bean.getPrice();
        String title = bean.getTitle();
        final boolean isChecked = bean.isCheck();
        String[] arr = bean.getImages().split("\\|");
        String imgPath = arr[0];
        // fen
        myHolder.carTitle.setText(title);
        myHolder.carPrice.setText(price+"");
        Glide.with(context).load(imgPath).into(myHolder.carImage);
        if (isChecked){
            myHolder.carCricle.setImageResource(R.drawable.cricle_yes);
        }else {
            myHolder.carCricle.setImageResource(R.drawable.cricle_no);
        }
        myHolder.carCricle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isChecked){
                    list.get(i).setCheck(false);
                }else {
                    list.get(i).setCheck(true);
                }
                notifyItemChanged(i);
                listener.callBack();
            }
        });

        // 加减的
        myHolder.mathView.setList(this,list,i);
        myHolder.mathView.setOnNumChangedListener(new MathView.OnNumChangedListener() {
            @Override
            public void callBack() {
                listener.callBack();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        TextView carTitle,carPrice;
        ImageView carImage,carCricle;
        MathView mathView;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            carImage=(ImageView)itemView.findViewById(R.id.car_image);
            carTitle=(TextView)itemView.findViewById(R.id.car_title);
            carPrice=(TextView)itemView.findViewById(R.id.car_price);
            carCricle=(ImageView)itemView.findViewById(R.id.car_cricle);
            mathView = itemView.findViewById(R.id.mathLayout);
        }
    }

    // 传递接口
    private onCheckedChangeListener listener;
    public void setOnCheckedChangeListener(onCheckedChangeListener listener){
        this.listener = listener;
    }
    public interface onCheckedChangeListener{
        void callBack();
    }
}
