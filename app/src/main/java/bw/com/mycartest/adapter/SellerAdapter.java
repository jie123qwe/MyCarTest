package bw.com.mycartest.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bw.com.mycartest.R;
import bw.com.mycartest.ShopBean;

public class SellerAdapter extends RecyclerView.Adapter<SellerAdapter.MyHoler>{
    private Context context;
    private List<ShopBean.DataBean> list = new ArrayList<>();

    public SellerAdapter(Context context) {
        this.context = context;
    }
    public void setList(List<ShopBean.DataBean> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyHoler onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(context, R.layout.shop_seller_item,null);
        MyHoler myHoler = new MyHoler(view);
        return myHoler;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHoler myHoler, int i) {
        ShopBean.DataBean dataBean = list.get(i);
        String sellerName = dataBean.getSellerName();
        myHoler.sellerName.setText(sellerName);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        myHoler.shopRecyList.setLayoutManager(manager);
        ShopAdapter adapter = new ShopAdapter(context,dataBean.getList());
        myHoler.shopRecyList.setAdapter(adapter);
        adapter.setOnCheckedChangeListener(new ShopAdapter.onCheckedChangeListener() {
            @Override
            public void callBack() {
                listener.callBack(list);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyHoler extends RecyclerView.ViewHolder{
        TextView sellerName;
        RecyclerView shopRecyList;
        public MyHoler(@NonNull View itemView) {
            super(itemView);
            sellerName = itemView.findViewById(R.id.seller_name);
            shopRecyList = itemView.findViewById(R.id.seller_recyclerview);
        }
    }
    private OnShopChangeListener listener;
    public void setOnShopChangeListener(OnShopChangeListener listener){
        this.listener = listener;
    }
    public interface OnShopChangeListener{
        void callBack(List<ShopBean.DataBean> list);
    }
}
