package biz.itonline.test_log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import biz.itonline.test_log.ui.login.LoginFragment;
import biz.itonline.test_log.ui.main.MainFragment;

public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        setContentView(R.layout.main_activity);

        /**
         * Podle stavu přihlášení je zvolen fragment pro zobrazení buď informací o přihlášeném uživateli, nebo login fragment
         */
        mViewModel.getLoginStatus().observe(this, aBoolean -> {
            if (aBoolean){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, MainFragment.newInstance())
                        .commitNow();
            }else{
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, LoginFragment.newInstance())
                        .commitNow();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewModel.removeDataObserver();
    }
}