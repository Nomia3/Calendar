package jp.co.individual.nomia.calendar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jp.co.individual.nomia.calendar.databinding.FragmentDetailsBinding;


public class DetailsFragment extends Fragment {

    public static FragmentDetailsBinding binding;
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false);

        return binding.getRoot();
    }
}
