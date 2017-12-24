package com.logistics.sj.appv1.database;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by AJ on 08-06-2017.
 */

public class RestSingleton {

    private static RestSingleton restfulSingleton;
    private RequestQueue requestQueue;
    private  static Context contxt;


    private  RestSingleton(Context context)
    {
        contxt=context;
        requestQueue=getRequestQueue();
    }

    public RequestQueue getRequestQueue()
    {
        if (requestQueue==null)
            requestQueue= Volley.newRequestQueue(contxt.getApplicationContext());
        return  requestQueue;
    }

    public static synchronized RestSingleton getInstance(Context context)
    {
        if (restfulSingleton==null)
            restfulSingleton=new RestSingleton(context);
        return restfulSingleton;
    }

    public<T> void addToRequestQueue(Request<T> request) {
        requestQueue.add(request);
    }

}
