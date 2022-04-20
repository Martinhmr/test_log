package biz.itonline.test_log;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import biz.itonline.test_log.support.HMObservable;
import biz.itonline.test_log.support.HMObserver;
import biz.itonline.test_log.support.RepoData;

public class MainActivityViewModel extends AndroidViewModel implements HMObserver {
    private static final String TAG = "MainActivityViewModel";
    private RepoData repoData; // Zdroj dat
    private final MutableLiveData<Boolean> isLogged = new MutableLiveData<>();

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        isLogged.setValue(false);
        initObservers();
    }

    /**
     * inicializace základních dat a nastavení observeru
     */
    private void initObservers() {
        repoData = RepoData.getInstance();
        repoData.addObserver(this);
        isLogged.setValue(repoData.isLoged());
    }

    public void removeDataObserver(){
        repoData.deleteObserver(this);
    }


    /**
     * Nastavení parametrů podle aktuálních údajů v úložišti dat. Aktuálně statud přihlášení užele => LoginState
     * @param observable
     * @param o
     */
    @Override
    public void update(HMObservable observable, Object o) {
        if (o instanceof RepoData.LoginState) {
            switch (o.toString()) {
                case "AUTHENTICATED":
                    isLogged.setValue(true);
                    break;
                case "LOGEDOUT":
                    isLogged.setValue(false);
                    break;
            }
        }
    }

    public LiveData<Boolean> getLoginStatus() {
        return isLogged;
    }
}
