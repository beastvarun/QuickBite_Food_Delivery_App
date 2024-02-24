package com.example.vegfeul;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vegfeul.Adaptor.CartListAdapter;
import com.example.vegfeul.Helper.ManagementCart;
import com.example.vegfeul.Interface.ChangeNumberItemsListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

public class CartListActivity extends AppCompatActivity implements PaymentResultListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewList;
    private ManagementCart managementCart;
    TextView totalFeeTxt,taxTxt,deliveryTxt,totalTxt,emptyTxt;
    private double tax;
    private ScrollView scrollView;
    TextView paynow;
    TextView pStatus;
    double totalamount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);

        managementCart =new ManagementCart(this);
        initView();
        initList();
        CalculateCart();
        bottomNavigation();
        Checkout.preload(getApplicationContext());

        paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymake();
            }
        });


    }


    private void bottomNavigation() {
        FloatingActionButton floatingActionButton= findViewById(R.id.cartBtn);
        LinearLayout homeBtn=findViewById(R.id.homebtn);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CartListActivity.this,CartListActivity.class));
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CartListActivity.this,MainActivity.class));
            }
        });

    }


    public  void initView()
    {
        recyclerViewList=findViewById(R.id.recyclerView);
        totalFeeTxt=findViewById(R.id.totalFeeTxt);
        taxTxt=findViewById(R.id.taxTxt);
        deliveryTxt=findViewById(R.id.deliveryTxt);
        totalTxt=findViewById(R.id.totalTxt);
        emptyTxt=findViewById(R.id.emptyTxt);
        scrollView=findViewById(R.id.scrollView3);
        recyclerViewList=findViewById(R.id.cartView);


        paynow=findViewById(R.id.CheckOutt);
        pStatus=findViewById(R.id.paymentStatus);

    }


    private void initList(){
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerViewList.setLayoutManager(linearLayoutManager);
        adapter=new CartListAdapter(managementCart.getListCart(), this, new ChangeNumberItemsListener() {
            @Override
            public void changed() {
                CalculateCart();
            }
        });

        recyclerViewList.setAdapter(adapter);
        if(managementCart.getListCart().isEmpty())
        {
            emptyTxt.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        }
        else
        {
            emptyTxt.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }
    }

    private void CalculateCart(){
        double percentTax=0.02;
        double delivery=10;

        tax=Math.round((managementCart.getTotalFee()*percentTax)*100)/100.0;
        double total = Math.round((managementCart.getTotalFee()+tax+delivery)*100)/100.0;
        double itemTotal=Math.round(managementCart.getTotalFee()*100)/100.0;
        totalamount=total;


        totalFeeTxt.setText("$"+itemTotal);
        taxTxt.setText("$"+tax);
        deliveryTxt.setText("$"+delivery);
        totalTxt.setText("$"+total);



    }


    public void paymake(){

        double amount = totalamount * 100;
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_AtO95jSkFXidmw");
        checkout.setImage(R.drawable.paymentbrand);
        final Activity activity=this;

        JSONObject object = new JSONObject();
        try {
            object.put("name", "QuickBite");
            object.put("description", "Pizza Order Payment");
            object.put("theme.color", "");
            object.put("amount", amount);
            object.put("currency", "INR");

            checkout.open(activity, object);
        } catch (JSONException e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        pStatus.setText("Order Successfully. Transaction No :"+s);

    }

    @Override
    public void onPaymentError(int i, String s) {
        pStatus.setText("Order Successfully. Transaction No :"+s);


    }
}