package biz.itonline.test_log.support;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import biz.itonline.test_log.MyApplication;

public class NetVolleyTask {
    private static final String TAG = "NetVolleyTask";
    private final String url = "https://coesys.demo.gemalto.com/mbi/";
    private final AccVolley accVolley;
    private final NetResponseResult netResponse;
    private final Context appContext;
    private final String tag;

    public NetVolleyTask(NetResponseResult netResponse, String tag) {
        this.netResponse = netResponse;
        accVolley = AccVolley.getInstance();
        this.appContext = MyApplication.getContext();
        this.tag = tag;

    }

    /**
     * Odeslání POST požadavku
     * @param jsonObject    JSON objekt s požadovanými parametry
     * @throws JSONException
     */
    public synchronized void processPost(JSONObject jsonObject) throws JSONException {
        final String task = jsonObject.getString("task");

        JSONObject jObj = new JSONObject();
        jObj.put("username", jsonObject.getString("user"));
        jObj.put("password", jsonObject.getString("pswd"));

        jObj.put("grant_type", "password");


        String requestURL = url + "auth/v2/oauth/token";
        StringRequest sRequest = new StringRequest(Request.Method.POST, requestURL,
                response -> {
                    Log.d(TAG, "onResponse: " + response);
                    try {
                        processTask(task, new JSONObject(response));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    String body = null;
                    try {
                        body = new String(error.networkResponse.data, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONArray data = new JSONObject(body).getJSONArray("errors");
                        JSONObject errData = (JSONObject) data.get(0);

                        processError(errData.getString("detail"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    params.put("username", jsonObject.getString("user"));
                    params.put("password", jsonObject.getString("pswd"));

                    params.put("grant_type", "password");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("User-agent", System.getProperty("http.agent"));
                return headers;
            }

        };
        sRequest.setTag(tag);
        accVolley.addToRequestQueue(sRequest);
    }

    /**
     * Odeslání GET požadavku
     * @param jsonObject parametry pro volání GET
     * @throws JSONException
     */

    public synchronized void processGet(JSONObject jsonObject) throws JSONException {
        final String getTask = jsonObject.getString("task");
        final String token = jsonObject.getString("accessToken");

        String requestURL = url + "user/v2/account";

        JsonObjectRequest sRequest = new JsonObjectRequest(requestURL,
                response -> {
                    processTask(getTask, response);
                },
                error -> processError("Chyba p5i komunikaci se serverem")
        ) {

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("User-agent", System.getProperty("http.agent"));
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }

        };


        sRequest.setTag(tag);
        accVolley.addToRequestQueue(sRequest);
    }

    /**
     * Zpracování chyby ze sítě
     * @param msg Hlášení k zobrazení
     */

    private void processError(String msg) {
        netResponse.processNetError("logout", msg);
        Toast toast = Toast.makeText(appContext,
                msg,
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 50);
        toast.show();
    }

    /**
     * Zpracování odpovědi ze serveru
     * @param task taks, kterého se odpověď týká
     * @param jsonMsg Přijatý objekt ze serveru
     */

    private void processTask(String task, JSONObject jsonMsg) {
        switch (task) {
            case "login":
                Log.d(TAG, "processTask: Musi me prihlasit");
                netResponse.processNetResponse("login", jsonMsg);

                break;
            case "checkToken":
                netResponse.processNetResponse("checkToken", jsonMsg);
                break;

        }

    }

    public void cancelAll() {
        accVolley.cancelAll(tag);
    }

    /**
     * Kontrola připojení k síti
     * @return
     */

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (cm != null) {
            netInfo = cm.getActiveNetworkInfo();
        }
        return netInfo != null && netInfo.isConnected();
    }


}
