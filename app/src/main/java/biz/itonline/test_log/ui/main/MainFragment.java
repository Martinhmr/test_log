package biz.itonline.test_log.ui.main;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import biz.itonline.test_log.databinding.MainFragmentBinding;

/**
 * Fragment použitý pro zobrazení údajů o přihlášeném uživateli a případně jeho odhlášení
 */

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    private MainFragmentBinding binding;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding = MainFragmentBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvName = binding.tvName;
        TextView tvSurname = binding.tvSurname;
        TextView tvEmail = binding.tvEmail;

        Button btnLogout = binding.btnLogOut;

        tvName.setText(mViewModel.getName());
        tvSurname.setText(mViewModel.getSurname());
        tvEmail.setText(mViewModel.getEmail());

        btnLogout.setOnClickListener(view1 -> mViewModel.logOut());

    }

}