package com.example.suwitsaengkaew.myrestaurant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by suwitsaengkaew on 22/11/2017 AD.
 */

public class MyAdaptor extends BaseAdapter{

    // Explicit
    private Context objContext;
    private String[] strNameFood, strPriceFood;
    private int[] intMyTarget;

    public MyAdaptor(Context context, String[] strName, String[] strPrice, int[] targetID) {

        this.objContext = context;
        this.strNameFood = strName;
        this.strPriceFood = strPrice;
        this.intMyTarget = targetID;

    } // Constructor

    @Override
    public int getCount() {
        return strNameFood.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater objLayoutInflater = (LayoutInflater) objContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = objLayoutInflater.inflate(R.layout.list_view_rows, parent, false);

        // Set Text Food
        TextView listViewFood = (TextView) view.findViewById(R.id.txtShowFood);
        listViewFood.setText(strNameFood[position]);

        // Set Text Price
        TextView listViewPrice = (TextView) view.findViewById(R.id.txtShowPrice);
        listViewPrice.setText(strPriceFood[position]);

        // Set Image
        ImageView listImageFood = (ImageView) view.findViewById(R.id.imvfood);
        listImageFood.setBackgroundResource(intMyTarget[position]);

        return view;
    } // get view
} // Main Class
