package bw.com.mycartest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;

import bw.com.mycartest.adapter.ShopAdapter;

public class MathView extends RelativeLayout implements View.OnClickListener{
    private Context context;
    private EditText et_number;
    public MathView(Context context) {
        super(context);
        init(context);
    }

    public MathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View view = View.inflate(context,R.layout.math_layout,null);
        et_number = view.findViewById(R.id.edit_shop_car);
        ImageView add = view.findViewById(R.id.add_car);
        ImageView min = view.findViewById(R.id.jian_car);
        add.setOnClickListener(this);
        min.setOnClickListener(this);
        addView(view);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_car:
                // 加1
                num++;
                et_number.setText(num+"");
                // 通知改变
                list.get(position).setNum(num);
                listener.callBack();
                shopAdapter.notifyItemChanged(position);
                break;
            case R.id.jian_car:
                //减1
                if (num>1){
                    num--;
                }else {
                    Toast.makeText(context,"到底了喂",Toast.LENGTH_SHORT).show();
                }
                et_number.setText(num+"");
                // 通知改变
                list.get(position).setNum(num);
                listener.callBack();
                shopAdapter.notifyItemChanged(position);
                break;
        }
    }


    private OnNumChangedListener listener;
    private int num;
    private ShopAdapter shopAdapter;
    private List<ShopBean.DataBean.ListBean> list;
    private int position;
    // 获取数据方法
    public void setList(ShopAdapter shopAdapter, List<ShopBean.DataBean.ListBean> list, int i) {
        this.position = i;
        this.shopAdapter = shopAdapter;
        this.list = list;
        // 设置初始数量
        num = list.get(i).getNum();
        et_number.setText(num+"");
    }
    // 传递接口
    public void setOnNumChangedListener(OnNumChangedListener listener){
        this.listener = listener;
    }
    public interface OnNumChangedListener{
        void callBack();
    }
}
