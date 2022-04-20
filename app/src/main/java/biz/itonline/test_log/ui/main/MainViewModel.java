package biz.itonline.test_log.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import biz.itonline.test_log.support.RepoData;

public class MainViewModel extends AndroidViewModel {
    private static final String TAG = "MainViewModel";
    private RepoData repoData;

    public MainViewModel(@NonNull Application application) {
        super(application);
        initData();
    }

    private void initData() {
        repoData = RepoData.getInstance();
    }

    public String getName() {
        return repoData.getName();
    }

    public String getSurname() {
        return repoData.getSurname();
    }

    public String getEmail() {
        return repoData.getEmail();
    }

    public void logOut(){
        repoData.logOut();
    }

}