package biz.itonline.test_log.support;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import biz.itonline.test_log.MyApplication;

/**
 * Jednotné úložiště dat por aplikaci
 */
public class RepoData extends biz.itonline.test_log.support.HMObservable implements biz.itonline.test_log.support.NetResponseResult {
    private static final String TAG = "RepoData";
    private LoginState loginState = LoginState.LOGEDOUT;
    private OnlineState onlineState = OnlineState.UNDEFINED;
    private final Context appContext;
    private static RepoData instance;
    private final NetVolleyTask nt;
    private String AccessToken = null;
    private String name = null;
    private String surname = null;
    private String email = null;

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }


    private RepoData(Context appContext) {
        this.appContext = appContext;
        nt = new NetVolleyTask(this, TAG);
    }


    public static RepoData getInstance() {
        if (instance == null) {
            instance = new RepoData(MyApplication.getContext());
        }
        return instance;
    }

    /**
     * Zpracování odpovědi z NET komunikace
     * @param task zpracovávaný úkol
     * @param js odpověď ze serveru v JSON objektu
     */
    @Override
    public void processNetResponse(String task,  JSONObject js) {
            switch (task) {
                case "login":
                    evaluateLogin(js);
                    break;
                case "checkToken":
                    evaluateToken(js);
                    break;
            }
    }

    /**
     * Zpracování chyby z NET komunikace
     * @param task zpracovávaný úkol
     * @param msg Předaná zpráva (aktuálně nevyužito)
     */

    @Override
    public void processNetError(String task, String msg) {
        switch (task) {
            case "logout":
                loginState = LoginState.LOGEDOUT;
                setChanged();
                notifyObservers(loginState);
                break;
        }

    }

    /**
     * Zpracování platného přihlášení uživatele
     * @param js JSON objekt vrácený ze serveru
     */
    private void evaluateLogin(JSONObject js) {
        try {
            if (js.getString("access_token").length()>0) {
                loginState = LoginState.AUTHENTICATED;
                AccessToken = js.getString("access_token");
                Map<String, String> jsonParams = new HashMap<>();
                jsonParams.put("task", "checkToken");
                jsonParams.put("accessToken", AccessToken);
                nt.processGet(new JSONObject(jsonParams)); // Volání fce pro získání údajů o přihlášeném uřivateli
            } else {
                setChanged();
                loginState = LoginState.INVALID_AUTHENTICATION;
                notifyObservers(loginState);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Zpracování platné odpovědi s informacemi o přihlášeném uživteli
     * @param js Odpověd JSON ze serveru s informacemi o uživateli
     */
    private void evaluateToken(JSONObject js){

        try {
            name = js.getString("firstName");
            surname = js.getString("lastName");
            email = js.getString("email");
            Log.d(TAG, "evaluateToken: Name: " + name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setChanged();
        notifyObservers(loginState);
    }

    /**
     * Přihlášení uživatele
     * @param nick username
     * @param pswd password
     */
    public void logIn(String nick, String pswd) {
        Map<String, String> jsonParams = new HashMap<>();
        jsonParams.put("task", "login");
        jsonParams.put("user", nick);
        jsonParams.put("pswd", pswd);

        if (nt.isOnline()) {
            try {
                nt.processPost(new JSONObject(jsonParams));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            setChanged();
            onlineState = OnlineState.ONLINE;
        } else {
            setChanged();
            onlineState = OnlineState.OFFLINE;
        }
        notifyObservers(onlineState);
    }


    public boolean isLoged() {
        return loginState.equals(LoginState.AUTHENTICATED);
    }

    public void logOut() {
        setChanged();
        loginState = LoginState.LOGEDOUT;
        notifyObservers(loginState);
    }


    public Boolean isOnline() {
        if (nt.isOnline()) {
            setChanged();
            onlineState = OnlineState.ONLINE;
            notifyObservers(onlineState);
            return true;
        } else {
            setChanged();
            onlineState = OnlineState.OFFLINE;
            notifyObservers(onlineState);
            return false;
        }
    }



    public enum OnlineState {
        ONLINE,
        OFFLINE,
        UNDEFINED
    }

    public enum LoginState {
        AUTHENTICATED,
        INVALID_AUTHENTICATION,
        LOGEDOUT
    }

}
