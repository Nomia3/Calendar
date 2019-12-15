package jp.co.individual.nomia.calendar;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.star_zero.eternalviewpager.EternalPagerAdapter;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class CalendarAdapter extends EternalPagerAdapter<LocalDate> {

    private Fragment fragment;

    public CalendarAdapter(FragmentManager fragmentManager, LocalDate initialKey) {
        super(fragmentManager, initialKey);
    }

    @NonNull
    @Override
    public Fragment getItem(LocalDate key) {
        fragment = CalendarFragment.createInstance(key);
        return fragment;
    }

    @Nullable
    @Override
    protected LocalDate getNextKey(@NonNull LocalDate last) {
        return last.plusMonths(1);
    }

    @Nullable
    @Override
    protected LocalDate getPrevKey(@NonNull LocalDate first) {
        return first.minusMonths(1);
    }

    @Nullable
    @Override
    protected Bundle saveKeysState(@NonNull ArrayList<LocalDate> keys) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("keys", keys);
        return bundle;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    protected List<LocalDate> restoreKeysState(@NonNull Bundle bundle) {
        return (ArrayList<LocalDate>) bundle.getSerializable("keys");
    }

    public Fragment getFragment(){
        return fragment;
    }
}