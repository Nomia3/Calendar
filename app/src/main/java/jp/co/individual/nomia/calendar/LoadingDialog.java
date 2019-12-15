package jp.co.individual.nomia.calendar;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import jp.co.individual.nomia.calendar.databinding.DialogLoadingBinding;

public class LoadingDialog extends DialogFragment {

    public static DialogLoadingBinding binding;

    public static LoadingDialog newInstance() {

        return new LoadingDialog();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = LayoutInflater.from(getActivity());

        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_loading, null, false);

        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(binding.getRoot());
        dialog.setCancelable(false);
        dialog.setTitle("now loading...");
        return dialog;
    }

}