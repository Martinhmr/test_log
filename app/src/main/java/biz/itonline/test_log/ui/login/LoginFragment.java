package biz.itonline.test_log.ui.login;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import biz.itonline.test_log.databinding.LoginFragmentBinding;

/**
 * Fragment pro přihlášení uživatele
 */

public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";
    private TextView tvOnlineStatus, tvNick, tvPswd;
    private LoginFragmentBinding binding;

    private LoginViewModel lViewModel;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        lViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        binding = LoginFragmentBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");



        tvNick = binding.tiNick;
        tvPswd = binding.tiPswd;
        tvOnlineStatus = binding.tvOnlineStatus;


        lViewModel.getOnlineState().observe(getViewLifecycleOwner(), aBoolean -> {
            tvOnlineStatus.setText(aBoolean ? "ONLINE" : "OFFLINE");
            if (!aBoolean){
                Toast toast = Toast.makeText(getContext(),
                        "Internet je nedostupný, nelze komunikovat se serverem.",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 0, 50);
                toast.show();

            }
        });

        Button btnLogin = binding.btnLogIn;

        btnLogin.setOnClickListener(view1 -> lViewModel.login(tvNick.getText().toString(), tvPswd.getText().toString()));

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        lViewModel.removeDataObserver();
    }
}
