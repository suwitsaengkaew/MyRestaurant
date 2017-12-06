package com.example.suwitsaengkaew.myrestaurant;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private UserTABLE objUserTABLE;
    private OrderTABLE objOrderTABLE;
    private FoodTABLE objFoodTABLE;
    private EditText edtUser, edtPassword;
    private String strUserChoose, strPasswordChoose, strPasswordTrue, strName ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        objUserTABLE = new UserTABLE(this);
        objOrderTABLE = new OrderTABLE(this);
        objFoodTABLE = new FoodTABLE(this);

        bindWidget();

        // Test
        // testAddValues();

        // Delete All Data
        deleteAllData();

        // Setup Policy
        if (Build.VERSION.SDK_INT > 9) { // If SDK > 9 Must be allow to internet
            StrictMode.ThreadPolicy myPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(myPolicy);
        } // if Build.VERSION.SDK_INT

        // SyncJsonSQLite
        // HttpClientSyncJsonSQLite();
        HttpURLConnectionSyncJsonSQLiteUserTable();
        HttpURLConnectionSyncJsonSQLiteFoodTable();

    } // onCreate

    private void HttpURLConnectionSyncJsonSQLiteFoodTable() {
        String strFoodJson = "";
        InputStream foodStream = null;

        try {

            URL url = new URL("http://info.ytmt.co.th/costcalcapi/getfoodtable");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try {

                foodStream = new BufferedInputStream(urlConnection.getInputStream());
                Log.d("Restaurant", "Food good result" + foodStream.toString());

            } catch (Exception e) {

                Log.d("Restaurant", "Food error result ==> " + e.toString());

            } finally {
                urlConnection.disconnect();
            }

        } catch (Exception e) {

            Log.d("Restaurant", "Error from FoodStream" + e.toString());

        }

        try {

            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = foodStream.read();
            while (i != -1) {
                bo.write(i);
                i = foodStream.read();
            }
            strFoodJson = bo.toString();
            Log.d("Restaurant", "Read good Food stream ==> " + bo.toString() );

        } catch (IOException e) {

            Log.d("Restaurant", "IOException error ==> " + e.toString());

        }

        try {

            final JSONArray objJSONArray = new JSONArray(strFoodJson);
            for (int i = 0; i < objJSONArray.length(); i++) {

                JSONObject objJSONObject = objJSONArray.getJSONObject(i);
                String strFood = objJSONObject.getString("Food");
                String strPrice = objJSONObject.getString("Price");

                long insertValue = objFoodTABLE.addValueToFood(strFood, strPrice);
                Log.d("Restaurant", "Insert Up Value ==> " + strFood + ", " + strPrice);
            }
        } catch (Exception e) {

            Log.d("Restaurant", "Error food stream write to SQLite" + e.toString());

        }

    }


    private void bindWidget() {
        edtUser = (EditText) findViewById(R.id.editText);
        edtPassword = (EditText) findViewById(R.id.editText2);
    } // bindWidget

    private void HttpURLConnectionSyncJsonSQLiteUserTable() {

        String strJson = "";
        InputStream in = null;

        try {
            URL url = new URL("http://info.ytmt.co.th/costcalcapi/getusertable");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                in = new BufferedInputStream(urlConnection.getInputStream());
                Log.d("Restaurant", "Result ==> " + in.toString());
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            Log.d("Restaurant", "Error from InputStream " + e.toString());
        }

        // Change InputStream to String
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = in.read();
            while(i != -1) {
                bo.write(i);
                i = in.read();
            }
            strJson = bo.toString();
            Log.d("Restaurant", "Read from Stream ==> " + bo.toString());
        } catch (IOException e) {
            Log.d("Restaurant", "IOException Error ==> " + e.toString());
        }


        // Up Values to SQLite
        try {

            final JSONArray objJSONArray = new JSONArray(strJson);
            for (int i = 0; i < objJSONArray.length(); i++) {

                JSONObject objJSONObject = objJSONArray.getJSONObject(i);
                String strUser = objJSONObject.getString("User");
                String strPassword = objJSONObject.getString("Password");
                String strOfficer = objJSONObject.getString("Officer");

                long insertValue = objUserTABLE.addValueToUser(strUser, strPassword, strOfficer);
                Log.d("Restaurant", "Insert Up Value ==> " + strUser + ", " + strPassword + ", " + strOfficer);
            }

        } catch (Exception e) {
            Log.d("Restaurant", "Error Up Value ==> " + e.toString());
        }

    }

    private void deleteAllData() {

        SQLiteDatabase objSQLite = openOrCreateDatabase("Restaurant.db", MODE_PRIVATE, null);
        objSQLite.delete("userTABLE", null,null);
        objSQLite.delete("orderTABLE", null,null);
        objSQLite.delete("foodTABLE", null, null);

    } // deleteAllData

    private void HttpClientSyncJsonSQLite() {

        // Setup Policy
        if (Build.VERSION.SDK_INT > 9) { // If SDK > 9 Must be allow to internet
            StrictMode.ThreadPolicy myPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(myPolicy);
        } // if Build.VERSION.SDK_INT

        InputStream objInputStream = null;
        String strJson = "";

        // Create objInputStream

        try {
              HttpClient objHttpClient = new DefaultHttpClient();
              HttpGet objHttpPost = new HttpGet("http://info.ytmt.co.th/costcalcapi/getusertable");
              HttpResponse objHttpResponse = objHttpClient.execute(objHttpPost);
              HttpEntity objHttpEntity = objHttpResponse.getEntity();
              objInputStream = objHttpEntity.getContent();
        } catch (Exception e) {
            Log.d("Restaurant", "Error from InputStream " + e.toString());
        }

        // Change InputStream to String
        try {

            BufferedReader objBufferReader = new BufferedReader(new InputStreamReader(objInputStream, "UTF-8"));
            StringBuilder objStringBuilder = new StringBuilder();
            String strLine = null;

            while ((strLine = objBufferReader.readLine()) != null) {
                objStringBuilder.append(strLine);
            }

            objInputStream.close();
            strJson = objStringBuilder.toString();

        } catch (Exception e) {
            Log.d("Restaurant", "Error Create String ==> " + e.toString());
        }

        // Up Values to SQLite
        try {

            final JSONArray objJSONArray = new JSONArray(strJson);
            for (int i = 0; i < objJSONArray.length(); i++) {

                JSONObject objJSONObject = objJSONArray.getJSONObject(i);
                String strUser = objJSONObject.getString("User");
                String strPassword = objJSONObject.getString("Password");
                String strOfficer = objJSONObject.getString("Officer");

                long insertValue = objUserTABLE.addValueToUser(strUser, strPassword, strOfficer);
                Log.d("Restaurant", "Error Up Value ==> " + strUser + ", " + strPassword + ", " + strOfficer);
            }

        } catch (Exception e) {
            Log.d("Restaurant", "Error Up Value ==> " + e.toString());
        }

    } // SyncJsonSQLite

//    private void readStream(InputStream is) {
//        try {
//            ByteArrayOutputStream bo = new ByteArrayOutputStream();
//            int i = is.read();
//            while(i != -1) {
//                bo.write(i);
//                i = is.read();
//            }
//            Log.d("Restaurant", "Error from ReadStream", bo.toString());
//        } catch (IOException e) {
//            Log.d(e.toString());
//        }
//    }

    private void testAddValues() {

        objUserTABLE.addValueToUser("User", "Password", "Officer");
        objOrderTABLE.addValueToOrder("Officer", "Date", "Food", 4);
        objFoodTABLE.addValueToFood("Food", "Price");

    } // testAddValues

    public void clicktologin(View view) {

        strUserChoose = edtUser.getText().toString().trim();
        strPasswordChoose = edtPassword.getText().toString().trim();

        if (strUserChoose.equals("") || strPasswordChoose.equals("")) {

            MyAlertDialog objMyAlert = new MyAlertDialog();
            objMyAlert.errorDialog(MainActivity.this, "Username not input", "Please input your username!");

        } else {

            checkUser();

        }

        // Log.d("Restaurant", "Click on MyDialog");
    }

    private void checkUser() {

        try {

            String strData[] = objUserTABLE.searchUser(strUserChoose);
            strPasswordTrue = strData[2];
            strName = strData[3];

            Log.d("Restaurant", "Welcome " + strName);

            checkPassword();


        } catch (Exception e) {

            MyAlertDialog objMyAlert = new MyAlertDialog();
            objMyAlert.errorDialog(MainActivity.this, "No this User!","Not found this User " + strUserChoose);

        }

    } // checkUser

    private void checkPassword() {


        if (strPasswordChoose.equals(strPasswordTrue)) {

                // Intent
                WellComeUser();


        } else {

            MyAlertDialog objMyAlerDialog = new MyAlertDialog();
            objMyAlerDialog.errorDialog(MainActivity.this, "Password wrong", "Your password mis match");

        }

    } // checkPassword

    private void WellComeUser() {

        AlertDialog.Builder objAlert = new AlertDialog.Builder(this);
        objAlert.setTitle("WellCome to MyApp");
        objAlert.setMessage("WellCome " + strName + "\n" + "To My App");
        objAlert.setCancelable(false);
        objAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent objIntentOrderActivity = new Intent(MainActivity.this, OrderActivity.class);
                objIntentOrderActivity.putExtra("Name", strName);
                startActivity(objIntentOrderActivity);
                finish();

            }
        });

        objAlert.show();

    } // WellComeUser

} // Main
