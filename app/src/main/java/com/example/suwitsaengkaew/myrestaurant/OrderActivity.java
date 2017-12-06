package com.example.suwitsaengkaew.myrestaurant;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class OrderActivity extends AppCompatActivity {

    private FoodTABLE objFoodTABLE;
    private OrderTABLE objOrderTABLE;
    private Calendar calendar;
    //private DateFormat df = new SimpleDateFormat("dd MM yyyy, HH:mm:ss");
    private String[] strListFood, strListPrice;
    private TextView txtShowOfficer;
    private EditText edtDest;
    private String strMyOfficer, strMyDesk, strMyFood, strAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Log.d("Restaurant", "Welcome ");

        bindWidget();

        objFoodTABLE = new FoodTABLE(this);
        objOrderTABLE = new OrderTABLE(this);
        calendar = Calendar.getInstance();
        strMyDesk = edtDest.getText().toString().trim();

        setUpTextShowOfficer();

        setupAllArray();
        createListView();



    }

    private void setUpTextShowOfficer() {

        strMyOfficer = getIntent().getExtras().getString("Name");
        txtShowOfficer.setText(strMyOfficer);

    } // setUpTextShowOfficer

    private void bindWidget() {

        txtShowOfficer = (TextView) findViewById(R.id.txtShowOfficer);
        edtDest = (EditText) findViewById(R.id.edtDesk);


    } // bindWidget

    private void createListView() {

        int[] myTarget = {R.drawable.food1, R.drawable.food2, R.drawable.food3, R.drawable.food4, R.drawable.food5,
                R.drawable.food6, R.drawable.food7, R.drawable.food8, R.drawable.food9, R.drawable.food10,
                R.drawable.food11, R.drawable.food12, R.drawable.food13, R.drawable.food14, R.drawable.food15,
                R.drawable.food16, R.drawable.food17, R.drawable.food18, R.drawable.food19, R.drawable.food20};

        MyAdaptor objMyAdaptor = new MyAdaptor(OrderActivity.this, strListFood, strListPrice, myTarget);
        ListView objListView = (ListView) findViewById(R.id.foodListView);
        objListView.setAdapter(objMyAdaptor);

        // ListView Active Click
        objListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                strMyDesk = edtDest.getText().toString().trim();
                // Check Zero
                if (strMyDesk.equals("")) {

                    MyAlertDialog objMyAlerDialog = new MyAlertDialog();
                    objMyAlerDialog.errorDialog(OrderActivity.this, "Where your Table??", "Please fill up Table");

                } else {
                    strMyFood = strListFood[position];
                    Log.d("Restaurant", "strMyFood ==> " + strMyFood.toString());
                    try {

                        // checkLog();
                        showChooseItem();
                        // maslong inserttoOrderTable = objOrderTABLE.addValueToOrder(strMyOfficer, strMyDesk ,strMyFood, 1);

                    } catch (Exception e) {

                        Log.d("Restaurant", "Write to SQLite problem ==> " + e.toString());

                    }

                }

            }
        });

    } // createListView

    private void showChooseItem() {

        CharSequence[] charItem = {"1 จาน", "2 จาน", "3 จาน", "4 จาน", "5 จาน"};

        AlertDialog.Builder objBuilder = new AlertDialog.Builder(this);
        //objBuilder.setIcon()
        objBuilder.setTitle("Choose item ?");
        objBuilder.setCancelable(false);
        objBuilder.setSingleChoiceItems(charItem, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: strAmount = "1";
                            break;
                    case 1: strAmount = "2";
                            break;
                    case 2: strAmount = "3";
                            break;
                    case 3: strAmount = "4";
                            break;
                    case 4: strAmount = "5";
                            break;
                }
                checkLog();
                try {

                    long inserttoOrderTable = objOrderTABLE.addValueToOrder(strMyOfficer, strMyDesk ,strMyFood, Integer.parseInt(strAmount));

                } catch (Exception e) {

                    Log.d("Restaurant", "Write to SQLite problem ==> " + e.toString());

                }
                dialog.dismiss();
            }
        });
        AlertDialog objAlertDialog = objBuilder.create();
        objAlertDialog.show();

    }

    private void checkLog() {

        Log.d("Restaurant", "Officer ==> " + strMyOfficer);
        //Log.d("Restaurant", "DateTime ==> " + df.format(calendar));
        Log.d("Restaurant", "Desk ==> " + strMyDesk);
        Log.d("Restaurant", "Food ==> " + strMyFood);
        Log.d("Restaurant", "Item ==> " + strAmount);

    }

    private void setupAllArray() {

        strListFood = objFoodTABLE.listFood();
        strListPrice = objFoodTABLE.listPrice();


    } // setupAllArray
}
