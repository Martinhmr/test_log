package biz.itonline.test_log.support;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import biz.itonline.test_log.MyApplication;

public class AccVolley {
    private static AccVolley instance;
    private RequestQueue requestQueue;
    private static Context ctx;

    private AccVolley() {
        ctx = MyApplication.getContext();
        requestQueue = getRequestQueue();
    }

    public static synchronized AccVolley getInstance() {
        if (instance == null) {
            instance = new AccVolley();
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(ctx);
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public void cancelAll(String tag) {
        requestQueue.cancelAll(tag);
    }


}
