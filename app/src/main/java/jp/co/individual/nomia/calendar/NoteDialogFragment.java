package jp.co.individual.nomia.calendar;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import jp.co.individual.nomia.calendar.databinding.FragmentNoteBinding;

public class NoteDialogFragment extends DialogFragment {

    private FragmentNoteBinding binding;
    private String textDate;

    private reloadListener mListener;
    public interface reloadListener {
        void reloadTag();
        void reloadText();
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        mListener = (reloadListener) getParentFragment();
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        textDate = getArguments().getString("textDate");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_note, null,false);
        builder.setView(binding.getRoot());

        binding.editTag.setText(MainActivity.map.get(new CompositeKey(textDate, "tag")));
        binding.editText.setText(MainActivity.map.get(new CompositeKey(textDate, "text")));

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String tag = binding.editTag.getText().toString();
                        String text = binding.editText.getText().toString();

                        MainActivity.map.put(new CompositeKey(textDate, "tag"), tag);
                        MainActivity.map.put(new CompositeKey(textDate, "text"), text);

                        mListener.reloadTag();
                        mListener.reloadText();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        return builder.create();
    }
}

