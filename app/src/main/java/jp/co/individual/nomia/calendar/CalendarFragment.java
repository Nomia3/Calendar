package jp.co.individual.nomia.calendar;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import jp.co.individual.nomia.calendar.databinding.FragmentCalendarBinding;


public class CalendarFragment extends Fragment implements NoteDialogFragment.reloadListener {

    private FragmentCalendarBinding binding;
    private LocalDate date;
    private static int selectingId = -1;
    private static View selectedTextView;
    private static int originalColor;
    //色設定
    private TypedValue textAttr = new TypedValue();
    private TypedValue grayBackAttr = new TypedValue();
    private TypedValue defaultBackAttr = new TypedValue();

    public static CalendarFragment createInstance(LocalDate date) {
        Bundle args = new Bundle();
        args.putSerializable("date", date);
        CalendarFragment fragment = new CalendarFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false);
        date = (LocalDate) getArguments().getSerializable("date");
        //行作成
        for (int i = 0; i < 6; i++) {
            getActivity().getLayoutInflater().inflate(R.layout.date_row, binding.calendarTable);
        }

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        drawCalendar();

    }


    private void drawCalendar(){

        //年月表示
        binding.setYearMonth(date.format(DateTimeFormatter.ofPattern("yyyy年MM月")));

        //1日の曜日取得
        LocalDate dateSearch = date.of(date.getYear(),date.getMonth(),1);
        DayOfWeek startDayOfWeek = dateSearch.getDayOfWeek();

        TableRow dateTableRow = (TableRow) binding.calendarTable.getChildAt(0);
        TextView dateTextView = (TextView) dateTableRow.getChildAt(0);

        int dayCount = 1;
        boolean isGray = false;
        int tempId;

        //色取得
        getActivity().getTheme().resolveAttribute(R.attr.customTextColor, textAttr, true);
        getActivity().getTheme().resolveAttribute(R.attr.customGrayBackColor, grayBackAttr, true);
        getActivity().getTheme().resolveAttribute(R.attr.customBackColor, defaultBackAttr, true);

        //前月の日付描画
        if(startDayOfWeek.getValue() != 7) {

            int startDay;
            LocalDate date2 = date;
            date2 = date2.minusMonths(1);
            dayCount = date2.lengthOfMonth();
            startDay = startDayOfWeek.getValue() - 1;

            for (int i = 0; ; i--) {
                dateTextView = (TextView) dateTableRow.getChildAt(startDay + i);
                dateTextView.setText(String.valueOf(dayCount));
                dateTextView.setTextColor(textAttr.data);
                dateTextView.setBackgroundColor(grayBackAttr.data);
                dayCount--;
                if (startDay + i == 0) {
                    dayCount = 1;
                    break;
                }
            }
        }

        //当月の日付描画

        for(int i=0; i < binding.calendarTable.getChildCount(); i++){
            dateTableRow = (TableRow) binding.calendarTable.getChildAt(i);

            for(int j=0; j < dateTableRow.getChildCount(); j++){
                //開始曜日セット
                if(i == 0 && j == 0){
                    if(startDayOfWeek.getValue() != 7) {
                        j = startDayOfWeek.getValue();
                    }
                    else{
                        j = 0;
                    }
                }
                //描画
                dateTextView = (TextView) dateTableRow.getChildAt(j);

                if(!isGray) {
                    dateTextView.setText(String.valueOf(dayCount));
                    tempId = Integer.parseInt(date.format(DateTimeFormatter.ofPattern("yyyyMM")));
                    tempId = tempId*100 + dayCount;
                    dateTextView.setId(tempId);
                    dateTextView.setClickable(true);

                    setButton(dateTextView);

                    setHoliday(dayCount, date, dateTextView);

                    setTag(dateTextView, String.valueOf(tempId));
                }

                //次月の日付描画
                if(isGray){
                    dateTextView.setText(String.valueOf(dayCount));
                    dateTextView.setTextColor(textAttr.data);
                    dateTextView.setBackgroundColor(grayBackAttr.data);
                }
                dayCount++;
                if(dayCount > date.lengthOfMonth()){
                    dayCount = 1;
                    isGray = true;
                }
            }
        }
    }

    public void resetColor (){

        if(selectingId != -1) {
            selectedTextView.setBackgroundColor(originalColor);
        }
    }

    private void setButton (TextView dateTextView){

        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textDate = String.valueOf(v.getId());

                if(selectedTextView == v){
                    DialogFragment newFragment = new NoteDialogFragment();
                    Bundle args = new Bundle();
                    args.putString("textDate", textDate); //引数
                    newFragment.setArguments(args);
                    newFragment.show(getChildFragmentManager(), "Note");
                }
                else {
                    //詳細表示
                    DetailsFragment.binding.setDetailsText(MainActivity.map.get(new CompositeKey(textDate, "text")));
                    //背景色を戻す
                    if (selectingId != -1) {
                        selectedTextView.setBackgroundColor(originalColor);
                    }
                    ColorDrawable colorDrawable = (ColorDrawable) v.getBackground();
                    originalColor = colorDrawable.getColor();
                    //背景色設定
                    v.setBackgroundColor(getResources().getColor(R.color.selectingColor));
                    //選択した場所を記憶
                    selectedTextView = v;
                    selectingId = v.getId();
                }
            }
        });
    }

    private void setHoliday (int dayCount, LocalDate date, TextView dateTextView){
        //休日ファイル読み込み
        LocalDate dateIndex = date.of(date.getYear(), date.getMonth(), dayCount);

        //色読み込み
        TypedValue redTextAttr = new TypedValue();
        TypedValue redBackAttr = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.customRedTextColor, redTextAttr, true);
        getActivity().getTheme().resolveAttribute(R.attr.customSunColor, redBackAttr, true);

        int index = MainActivity.holidayList.indexOf(dateIndex);
        if(index != -1){
            dateTextView.setTextColor(redTextAttr.data);
            dateTextView.setBackgroundColor(redBackAttr.data);
            if(MainActivity.holidayNameList.get(index).length() > 4) {
                dateTextView.append("\n" + MainActivity.holidayNameList.get(index).substring(0, 3));
            }else{
                dateTextView.append("\n" + MainActivity.holidayNameList.get(index));
            }
        }
    }

    private void setTag (TextView dateTextView, String textDate){

        String tempTag = MainActivity.map.get(new CompositeKey(textDate, "tag"));

        if(tempTag != null){
            dateTextView.append("\n" + tempTag);
        }else{
            tempTag = MainActivity.map.get(new CompositeKey(textDate, "text"));
            if(tempTag != null){
                String splitedText[] = tempTag.split("\n", 1);
                dateTextView.append("\n" + splitedText[0]);
            }
        }
    }

    public void reloadTag(){

        TextView textView = (TextView) selectedTextView;
        String textDate = String.valueOf(selectedTextView.getId());

        String tempDay = textDate.substring(6);
        if(tempDay.indexOf("0") == 0){
            tempDay = tempDay.substring(1);
        }

        String tempTag;
        String checkNull = MainActivity.map.get(new CompositeKey(textDate, "tag"));

        if(checkNull != null){
            if(!checkNull.equals("")){
                textView.setText(tempDay + "\n" + MainActivity.map.get(new CompositeKey(textDate, "tag")));
            }else{
                tempTag = MainActivity.map.get(new CompositeKey(textDate, "text"));
                if(tempTag != null){
                    String splitedText[] = tempTag.split("\n", 1);
                    textView.setText(tempDay + "\n" + splitedText[0]);
                }
            }
        }
    }

    public void reloadText(){

        String textDate = String.valueOf(selectedTextView.getId());
        DetailsFragment.binding.setDetailsText(MainActivity.map.get(new CompositeKey(textDate, "text")));
    }

}
