package biz.itonline.test_log.ui.login;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import biz.itonline.test_log.support.HMObservable;
import biz.itonline.test_log.support.HMObserver;
import biz.itonline.test_log.support.RepoData;

public class LoginViewModel extends AndroidViewModel  implements HMObserver {
    private static final String TAG = "LoginViewModel";
    private RepoData repoData;
    private final MutableLiveData<Boolean> isOnline = new MutableLiveData<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
        initObservers();
    }

    LiveData<Boolean> getOnlineState() {
        return isOnline;
    }

    public void login(String username, String password) {
        repoData.logIn(username, password);
    }


    private void initObservers() {
        repoData = RepoData.getInstance();
        HMObserver observer = this;
        repoData.addObserver(observer);
        isOnline.setValue(repoData.isOnline());
    }

    public void removeDataObserver(){
        repoData.deleteObserver(this);
    }

    /**
     * Nastavení informací o stavu připojení k síti.
     * Aktuálně je stav testován při odesílání login informací, jinak by měl být napojený na informaci o změně stavu připojení získaný ze systému.
     * @param observable
     * @param o
     */
    @Override
    public void update(HMObservable observable, Object o) {
        switch (o.toString()){

            case "ONLINE":
                isOnline.setValue(true);
                break;
            case "OFFLINE":
                isOnline.setValue(false);
                break;
        }

    }
}