package com.logistics.sj.appv1.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.logistics.sj.appv1.R;
import com.logistics.sj.appv1.model.TruckBean;
import com.logistics.sj.appv1.ui.Primary;
import com.logistics.sj.appv1.ui.Secondary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DBActivities {

    public List<TruckBean> getAllTrucks(final Context context) {

        final ArrayList<TruckBean> truckBeanArrayList=new ArrayList<TruckBean>();
        JsonArrayRequest jsonArrayRequest=null;
        String backendurl="https://truckerest.herokuapp.com/rest/main/fetchAll";

        try {

                jsonArrayRequest= new JsonArrayRequest(backendurl,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Toast.makeText(context,"inside onresponse",Toast.LENGTH_SHORT).show();

                                final ArrayList<TruckBean> temptruckBeanArrayList=truckBeanArrayList;

                                for (int index=0;index<response.length();index++)
                                {
                                    try {
                                        JSONObject jsonObject=response.getJSONObject(index);
                                        TruckBean truckBean=new TruckBean();
                                        truckBean.setTruckNumber(jsonObject.getString("truckNumber"));
                                        temptruckBeanArrayList.add(truckBean);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                truckBeanArrayList.addAll(temptruckBeanArrayList);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,"Error from backend"+error.getMessage(),Toast.LENGTH_LONG).show();
//
                    }
                });

        }catch (Exception e)
        {
            Toast.makeText(context,"Error from backend"+e.getMessage(),Toast.LENGTH_LONG).show();
//
        }

       // Toast.makeText(context,"array"+truckBeanArrayList.get(0).getTruckNumber(),Toast.LENGTH_LONG).show();
//
        RestSingleton.getInstance(context).addToRequestQueue(jsonArrayRequest);


//
//        DBHelper dbHelper=null;
//        SQLiteDatabase sqLiteDatabase=null;
//        Cursor cursor=null;
//
//        try {
//
//            dbHelper=new DBHelper(context);
//            sqLiteDatabase=dbHelper.getWritableDatabase();
//
//
//            cursor= sqLiteDatabase.query(
//                    Contract.TableColumns.TRUCK_TABLE,
//                    null,null,null,null,null,
//                    Contract.TableColumns._ID
//            );
//
//            while (cursor.moveToNext())
//            {
//                TruckBean truckBean=new TruckBean();
//                truckBean.setTruckNumber(cursor.getString(cursor.getColumnIndex(Contract.TableColumns.TRUCK_NUMBER)));
//                truckBean.setTruckOwnerName(cursor.getString(cursor.getColumnIndex(Contract.TableColumns.TRUCK_OWNER_NAME)));
//                String truckOwnerNumbeDbs=cursor.getString(cursor.getColumnIndex(Contract.TableColumns.TRUCK_OWNER_NUMBER_ARRAY));
//                String accountNumberDb=cursor.getString(cursor.getColumnIndex(Contract.TableColumns.ACCOUNT_NUMBER_ARRAY));
//
//                truckBean.setTruckOwnerPhoneNumberS(truckOwnerNumbeDbs.split(","));
//                truckBean.setAccountNumbers(accountNumberDb.split(","));
//
//                //TODO
//                //truckBean.setPicLocation(cursor.getString(cursor.getColumnIndex(Contract.TableColumns.)));
//                //truckBean.setIcon(android.R.drawable.btn_star);
//                truckBeanArrayList.add(truckBean);
//            }
//
//
//        }catch (Exception e)
//        {
//            Log.i("tagg","Exception in db");
//
//        }finally {
//
//
//            cursor.close();
//            sqLiteDatabase.close();
//            dbHelper.close();
//        }
//
        return  truckBeanArrayList;
       // return truckBeanArrayList;
    }

    public TruckBean getTruck(Context context,String truckNumber) {

        DBHelper dbHelper=new DBHelper(context);
        SQLiteDatabase sqLiteDatabase=dbHelper.getWritableDatabase();

        Cursor cursor= sqLiteDatabase.query(Contract.TableColumns.TRUCK_TABLE,
                                            null,
                                            Contract.TableColumns.TRUCK_NUMBER,new String[]{truckNumber},
                                            null,null,null);

        TruckBean truckBean=new TruckBean();
        cursor.moveToFirst();
        truckBean.setTruckNumber(cursor.getString(cursor.getColumnIndex(Contract.TableColumns.TRUCK_NUMBER)));
        truckBean.setTruckOwnerName(cursor.getString(cursor.getColumnIndex(Contract.TableColumns.TRUCK_OWNER_NAME)));
        String truckOwnerNumbeDbs=cursor.getString(cursor.getColumnIndex(Contract.TableColumns.TRUCK_OWNER_NUMBER_ARRAY));
        String accountNumberDb=cursor.getString(cursor.getColumnIndex(Contract.TableColumns.ACCOUNT_NUMBER_ARRAY));
        truckBean.setTruckOwnerPhoneNumberS(truckOwnerNumbeDbs.split(","));
        truckBean.setAccountNumbers(accountNumberDb.split(","));

        cursor.close();
        sqLiteDatabase.close();
        return truckBean;
    }


    public boolean deleteTruck(String truckNo,final Context context) {

        boolean b=false;
        JsonObjectRequest jsonObjectRequest=null;
        String backendurl=" https://truckerest.herokuapp.com/rest/main/delete?truckNumber="+truckNo;

       try {

           jsonObjectRequest=new JsonObjectRequest(Request.Method.DELETE, backendurl, null,
                   new Response.Listener<JSONObject>() {
                       @Override
                       public void onResponse(JSONObject response) {
                           try {
                               Toast.makeText(context,"deleted "+response.getString("result")+"ly",Toast.LENGTH_LONG).show();

                           } catch (JSONException e) {
                               e.printStackTrace();
                           }
//

                       }
                   },
                   new Response.ErrorListener() {
                       @Override
                       public void onErrorResponse(VolleyError error) {

                       }
                   });

           RestSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);


       }catch (Exception e)
       {

       }
       //delete all file as well
        //TODO
        File storageDir=new File(Environment.getExternalStorageDirectory(),"/Appv1");
        File file=new File(storageDir, (truckNo+"_PAN.jpg"));
        file.delete();
        return b;
    }

    public int updateTruck(TruckBean truckBean,final Context context) {
//        DBHelper dbHelper=new DBHelper(context);
//        SQLiteDatabase sqLiteDatabase=dbHelper.getWritableDatabase();
//
//// New value for one column
//        ContentValues updatedtruck=new ContentValues();
//        updatedtruck.put(Contract.TableColumns.TRUCK_OWNER_NAME,truckBean.getTruckOwnerName());
//
//        String truckOwnerNumbers="";
//        for (String truckOwnerNumber:truckBean.getTruckOwnerPhoneNumberS()) {
//            truckOwnerNumbers.concat(truckOwnerNumber+",");
//        }
//
//        String accountNumbers="";
//        for (String accountNumber:truckBean.getAccountNumbers()) {
//            accountNumbers.concat(accountNumber+",");
//        }
//
//        updatedtruck.put(Contract.TableColumns.TRUCK_OWNER_NUMBER_ARRAY,truckOwnerNumbers);
//        updatedtruck.put(Contract.TableColumns.ACCOUNT_NUMBER_ARRAY,accountNumbers);
//
//// Which row to update, based on the title
//        String selection = Contract.TableColumns.TRUCK_NUMBER + " LIKE ? ";
//        String[] selectionArgs ={truckBean.getTruckNumber()};
//
//        int count = sqLiteDatabase.update(
//                Contract.TableColumns.TRUCK_TABLE,
//                updatedtruck,
//                selection,
//                selectionArgs);
//        sqLiteDatabase.close();
//        dbHelper.close();
//        return count;

        try {
            String truckOwnerNumbers="";
            Boolean isFirst=true;
            for (String truckOwnerNumber:truckBean.getTruckOwnerPhoneNumberS()) {
                if (isFirst){
                    truckOwnerNumbers=truckOwnerNumber;
                    isFirst=false;
                    continue;}else
                    truckOwnerNumbers=truckOwnerNumbers+"|"+truckOwnerNumber;
            }

            String accountNumbers="";
            isFirst=true;
            for (String accountNumber:truckBean.getAccountNumbers()) {
                if (isFirst){
                    accountNumbers=accountNumber;
                    isFirst=false;
                    continue;}
                accountNumbers=accountNumbers+"|"+accountNumbers;
            }


            JSONObject jsonObject=new JSONObject();
            jsonObject.put("truckNumber",truckBean.getTruckNumber().toString());
            jsonObject.put("ownerName",truckBean.getTruckOwnerName().toString());
            jsonObject.put("ownerNumbers",truckOwnerNumbers);
            jsonObject.put("driverNumbers","");
            jsonObject.put("accountNumbers",accountNumbers);
            jsonObject.put("additionalComments","adding");
            jsonObject.put("futureField","futureField");


            JsonObjectRequest jsonObjectRequest=null;
            String backendurl="https://truckerest.herokuapp.com/rest/main/update";

            jsonObjectRequest=new JsonObjectRequest(Request.Method.PUT, backendurl, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Toast.makeText(context,"updated "+response.getString("result")+"ly",Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });

            RestSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);


        }catch (Exception e)
        {
            Toast.makeText(context,"while updating to database"+e.getMessage(),Toast.LENGTH_LONG).show();

        }

        return 1;

    }

    public void addTruck(TruckBean truckBean,final Context context) {

        try {
            String truckOwnerNumbers="";
            Boolean isFirst=true;
            for (String truckOwnerNumber:truckBean.getTruckOwnerPhoneNumberS()) {
                if (isFirst){
                    truckOwnerNumbers=truckOwnerNumber;
                    isFirst=false;
                    continue;}else
                truckOwnerNumbers=truckOwnerNumbers+"|"+truckOwnerNumber;
            }
            Toast.makeText(context,"addTruck added "+truckOwnerNumbers,Toast.LENGTH_LONG).show();

            String accountNumbers="";
            isFirst=true;
            for (String accountNumber:truckBean.getAccountNumbers()) {
                if (isFirst){
                    accountNumbers=accountNumber;
                    isFirst=false;
                    continue;}
                accountNumbers=accountNumbers+"|"+accountNumber;
            }
            Toast.makeText(context,"addTruck added "+accountNumbers,Toast.LENGTH_LONG).show();


            JSONObject jsonObject=new JSONObject();
            jsonObject.put("truckNumber",truckBean.getTruckNumber().toString());
            jsonObject.put("ownerName",truckBean.getTruckOwnerName().toString());
            jsonObject.put("ownerNumbers",truckOwnerNumbers);
            jsonObject.put("driverNumbers","");
            jsonObject.put("accountNumbers",accountNumbers);
            jsonObject.put("additionalComments","adding");
            jsonObject.put("futureField","futureField");


            JsonObjectRequest jsonObjectRequest=null;
            String backendurl=" https://truckerest.herokuapp.com/rest/main/add";

            jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, backendurl, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Toast.makeText(context,"added "+response.getString("result")+"ly",Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });

            RestSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);


        }catch (Exception e)
        {
            Toast.makeText(context,"while adding to database"+e.getMessage(),Toast.LENGTH_LONG).show();

        }

        return ;
    }



}
