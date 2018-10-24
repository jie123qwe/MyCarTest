package bw.com.mycartest;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import bw.com.mycartest.adapter.SellerAdapter;
import bw.com.mycartest.net.OkHttpUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView mRecyList;
    private TextView allPriceTxt, allNumTxt;
    private ImageView img_checkAll;
    private List<ShopBean.DataBean> list = new ArrayList<>();
    private String url = "http://www.zhaoapi.cn/product/getCarts?uid=71";
    private SellerAdapter sellerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 控件
        mRecyList = findViewById(R.id.recyclerview);
        allPriceTxt = findViewById(R.id.all_price);
        allNumTxt = findViewById(R.id.sum_price_txt);
        img_checkAll = findViewById(R.id.iv_cricle);
        img_checkAll.setOnClickListener(this);
        // 适配器
        sellerAdapter = new SellerAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyList.setLayoutManager(linearLayoutManager);
        mRecyList.setAdapter(sellerAdapter);
        // 请求接口
        dohttp();
        sellerAdapter.setOnShopChangeListener(new SellerAdapter.OnShopChangeListener() {
            @Override
            public void callBack(List<ShopBean.DataBean> list) {
                int allNum = 0;
                int checkedNum = 0;
                double allPrice = 0;
                for (int i = 0; i < list.size(); i++) {
                    ShopBean.DataBean bean = list.get(i);
                    List<ShopBean.DataBean.ListBean> shopList = bean.getList();
                    for (int j = 0; j < shopList.size(); j++) {
                        ShopBean.DataBean.ListBean listBean = shopList.get(j);
                        boolean check = listBean.isCheck();
                        allNum += listBean.getNum();
                        if (check) {
                            int num = listBean.getNum();
                            double price = listBean.getPrice();
                            checkedNum += num;
                            allPrice += price * num;
                        }

                    }
                    // 已计算出被选中的数量和价格
                    if (checkedNum < allNum) {
                        //此时是非全选状态
                        img_checkAll.setImageResource(R.drawable.cricle_no);
                        isChecked = true;
                    } else {
                        // 此时是全选状态
                        img_checkAll.setImageResource(R.drawable.cricle_yes);
                        isChecked = false;
                    }
                    // 更新价格数量
                    allNumTxt.setText("去结算(" + checkedNum + ")");
                    allPriceTxt.setText("合计：" + allPrice);
                }

            }
        });
    }

    private void dohttp() {
        OkHttpUtils.getOkHttpUtils().get(url).result(new OkHttpUtils.HttpListener() {
            @Override
            public void success(String data) {
                Log.i("sssss", data);
                ShopBean bean = new Gson().fromJson(data, ShopBean.class);
                list = bean.getData();
                list.remove(0);
                sellerAdapter.setList(list);

            }

            @Override
            public void fail(String error) {

            }
        });
    }

    private boolean isChecked = true;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_cricle:
                // 点击全选 再点取消
                if (isChecked) {
                    img_checkAll.setImageResource(R.drawable.cricle_yes);
                    checkAllSeller(true);
                    isChecked = false;
                } else {
                    img_checkAll.setImageResource(R.drawable.cricle_no);
                    checkAllSeller(false);
                    isChecked = true;
                }
                break;
        }
    }

    private void checkAllSeller(boolean b) {
        // 选中状态
        int allNum = 0;
        double allPrice = 0;
        for (int i = 0; i < list.size(); i++) {
            ShopBean.DataBean bean = list.get(i);
            List<ShopBean.DataBean.ListBean> shopList = bean.getList();
            for (int j = 0; j < shopList.size(); j++) {
                // 此处会改变所有商品对象的选中状态
                ShopBean.DataBean.ListBean listBean = shopList.get(j);
                listBean.setCheck(b);
                int num = listBean.getNum();
                double price = listBean.getPrice();
                allNum += num;
                allPrice += price * num;
            }
        }
        if (b) {
            allNumTxt.setText("去结算(" + allNum + ")");
            allPriceTxt.setText("合计：" + allPrice);
        } else {
            allNumTxt.setText("去结算(0)");
            allPriceTxt.setText("合计：0.00");
        }
        sellerAdapter.notifyDataSetChanged();
    }

    // 计算选中状态下的数量
    public int getNum(List<ShopBean.DataBean> list) {
        int allNum = 0;
        double allPrice = 0;
        for (int i = 0; i < list.size(); i++) {
            ShopBean.DataBean bean = list.get(i);
            List<ShopBean.DataBean.ListBean> shopList = bean.getList();
            for (int j = 0; j < shopList.size(); j++) {
                ShopBean.DataBean.ListBean listBean = shopList.get(j);
                boolean check = listBean.isCheck();
                if (check) {
                    int num = listBean.getNum();
                    double price = listBean.getPrice();
                    allNum += num;
                    allPrice += price * num;
                } else {

                }
            }
        }
        return allNum;
    }

    // 计算选中状态下的价格
    public double getPrice() {
        int allNum = 0;
        double allPrice = 0;
        for (int i = 0; i < list.size(); i++) {
            ShopBean.DataBean bean = list.get(i);
            List<ShopBean.DataBean.ListBean> shopList = bean.getList();
            for (int j = 0; j < shopList.size(); j++) {
                ShopBean.DataBean.ListBean listBean = shopList.get(j);
                boolean check = listBean.isCheck();
                if (check) {
                    int num = listBean.getNum();
                    double price = listBean.getPrice();
                    allNum += num;
                    allPrice += price * num;
                } else {

                }
            }
        }
        return allPrice;
    }
}
