package com.logistics.sj.appv1.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.logistics.sj.appv1.R;
import com.logistics.sj.appv1.adapter.Adapter;
import com.logistics.sj.appv1.database.DBActivities;
import com.logistics.sj.appv1.database.RestSingleton;
import com.logistics.sj.appv1.model.TruckBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Primary extends AppCompatActivity implements SearchView.OnQueryTextListener, Adapter.ItemClickCallBack {

    private RecyclerView recyclerView;
    private Adapter adapter;
    private FloatingActionButton floatingActionButton;
    public List<TruckBean> truckBeanArrayList;
    public final String BUNDLE_EXTRA = "BUNDLE_EXTRA";
    public final String TRUCK_NUMBER = "TRUCK_NUMBER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary);

        //request  permission for storing and calling
        int hasWriteExternalStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int hasCallPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        if ((hasWriteExternalStoragePermission != PackageManager.PERMISSION_GRANTED) || (hasCallPermission != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CALL_PHONE},
                    1);
            return;
        }
        //end request  permission for storing and calling
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Thanks for granting permissions", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Grant permission to save images and make a call", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        //adding search bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //populate rcyclerview
        recyclerView = (RecyclerView) findViewById(R.id.recycler_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
       // truckBeanArrayList = new DBActivities().getAllTrucks(getApplicationContext());

        ///////restcall
        //final ArrayList<TruckBean> truckBeanArrayList=new ArrayList<TruckBean>();
        truckBeanArrayList=new ArrayList<TruckBean>();
        JsonArrayRequest jsonArrayRequest=null;
        String backendurl="https://truckerest.herokuapp.com/rest/main/fetchAll";

        JsonObjectRequest jsonObjectRequest=null;
        try {
            ////json request
            jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, backendurl, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            final ArrayList<TruckBean> fetchedArray=new ArrayList<TruckBean>();

                            try {
                             JSONArray responseArray=response.getJSONArray("records");


                                int length=responseArray.length();



                                for (int index=0;index<length;index++)
                                {
                                    JSONObject jsonObject=responseArray.getJSONObject(index);

                                    TruckBean truckBean=new TruckBean();
                                    truckBean.setTruckNumber(jsonObject.getString("truckNumber"));
                                    truckBean.setTruckOwnerName(jsonObject.getString("ownerName"));
                                    String[] ownerNumbers=jsonObject.getString("ownerNumbers").split("\\|");
                                    truckBean.setTruckOwnerPhoneNumberS(ownerNumbers);
                                    String[] accountNumbers=jsonObject.getString("accountNumbers").split("\\|");
                                    truckBean.setAccountNumbers(accountNumbers);
                                    fetchedArray.add(truckBean);

                                    Boolean b=truckBeanArrayList.add(truckBean);

                                }

//                                for (TruckBean truckBean:fetchedArray)
//                                {
//                                    truckBeanArrayList.add(truckBean);
//                                }


                            }catch (Exception e)
                            {

                                Toast.makeText(Primary.this,"Error populating truckBeanArrayList"+e.getMessage(),Toast.LENGTH_LONG).show();
                            }

                            adapter = new Adapter(truckBeanArrayList, Primary.this);//1st paramter array of truckbean
                            recyclerView.setAdapter(adapter);

                            //for click events on record
                            adapter.setItemClickCallBack(Primary.this);

                            //for swipe
                            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallBack());
                            itemTouchHelper.attachToRecyclerView(recyclerView);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Primary.this,"Error from backend"+error.getMessage(),Toast.LENGTH_LONG).show();
                }
            });




        }catch (Exception e)
        {
            Toast.makeText(Primary.this,"Error before creating request"+e.getMessage(),Toast.LENGTH_LONG).show();
//
        }

        RestSingleton.getInstance(Primary.this).addToRequestQueue(jsonObjectRequest);

       // Toast.makeText(Primary.this,"on create truckBeanArrayList length"+truckBeanArrayList.size(),Toast.LENGTH_LONG).show();
//
    }

    @Override
    public void onItemClick(int p) {

        //Move to Secondary screen
        try {

            TruckBean truckBean = (TruckBean) truckBeanArrayList.get(p);
            Intent secondary=new Intent(this,Secondary.class);
            String [] array=truckBean.getAccountNumbers();
            Toast.makeText(Primary.this,""+array[0],Toast.LENGTH_LONG).show();
            secondary.putExtra("truckDetails",truckBean);
            startActivity(secondary);

        } catch (Exception e) {
            Toast.makeText(this, "Failed to move to detailed screen"+e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(Primary.this);
        return true;
    }

    public void add(View view) {
        //start detailed screen
        Intent secondary = new Intent(this, Secondary.class);
        startActivity(secondary);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String searchText) {

        ArrayList<TruckBean> searchList = new ArrayList<TruckBean>();

        for (TruckBean truck : truckBeanArrayList) {
            if (truck.getTruckNumber().contains(searchText.toUpperCase()))
                searchList.add(truck);
        }
        //update main recyclerviewlist via adapter
        adapter.setFilter(searchList);
        return true;
    }

    private ItemTouchHelper.Callback createHelperCallBack() {
        ItemTouchHelper.SimpleCallback simpleCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
                        @Override
                        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                            return false;
                        }

                        @Override
                        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                            boolean isPermissionGranted=false;
                            // deleteTruckFromView(viewHolder.getAdapterPosition());
                            final RecyclerView.ViewHolder finalviViewHolder = viewHolder;
                            if (direction == ItemTouchHelper.RIGHT) {

                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                TruckBean truckBean = truckBeanArrayList.get(viewHolder.getAdapterPosition());
                                callIntent.setData(Uri.parse("tel:" +
                                                            truckBean.getTruckOwnerPhoneNumberS()[truckBean.getTruckOwnerPhoneNumberS().length]));

                                if (ActivityCompat.checkSelfPermission(Primary.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                                     Manifest.permission.CALL_PHONE},
                                                        1);
                                    return;
                                    }
                               startActivityForResult(callIntent, 123);
                            }
                            else {
                                final RecyclerView.ViewHolder finalvViewHolder = viewHolder;
                                new AlertDialog.Builder(Primary.this)
                                        .setTitle("Delete Truck")
                                        .setMessage("Pakka")
                                        .setPositiveButton("Ok",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        deleteTruckFromView(finalviViewHolder.getAdapterPosition());
                                                        Toast.makeText(Primary.this,"Truck deleted",Toast.LENGTH_SHORT);
                                                    }
                                                }
                                        )
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Toast.makeText(Primary.this, "Truck not deleted", Toast.LENGTH_SHORT).show();
                                                adapter.notifyDataSetChanged();
                                                dialog.dismiss();
                                            }
                                        })
                                        .setOnCancelListener(new DialogInterface.OnCancelListener() {

                                            @Override
                                            public void onCancel(DialogInterface dialog) {
                                                adapter.notifyDataSetChanged();
                                            }
                                        })
                                        .show();
                            }
                        }
                    };
        return  simpleCallback;
    }

    private void deleteTruckFromView(final int adapterPosition) {
        Boolean success=new DBActivities().deleteTruck(truckBeanArrayList.get(adapterPosition).getTruckNumber(),this);
        truckBeanArrayList.remove(adapterPosition);
        adapter.notifyItemRemoved(adapterPosition);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        onStart();
    }
}
