package jp.co.individual.nomia.calendar;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import org.threeten.bp.LocalDate;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import jp.co.individual.nomia.calendar.databinding.ActivityCalendarBinding;

public class CalendarActivity extends AppCompatActivity {

    private ActivityCalendarBinding binding;
    private LocalDate date = LocalDate.now();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_calendar);

        final CalendarAdapter adapter = new CalendarAdapter(getSupportFragmentManager(), date);
        binding.viewpager.setAdapter(adapter);

        binding.viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (ViewPager.SCROLL_STATE_IDLE == state) {
                    ((CalendarFragment)adapter.getFragment()).resetColor();
                }
            }
        });
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        saveHashMap(MainActivity.map);
        finishAndRemoveTask();
    }

    private void saveHashMap(HashMap<CompositeKey, String> map){

        try {
            ObjectOutputStream os = new ObjectOutputStream(openFileOutput("map", MODE_PRIVATE));
            os.writeObject(map);
        }catch(IOException e){
            System.out.println(e);
        }
    }
}
