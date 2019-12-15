package jp.co.individual.nomia.calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static List<LocalDate> holidayList = new ArrayList<>();
    public static List<String> holidayNameList = new ArrayList<>();
    public static HashMap<CompositeKey, String> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //loading処理
        new AsyncAppTask().execute();
    }

    class AsyncAppTask extends AsyncTask<Void, Void, Void> {

        LoadingDialog loadingDialog = LoadingDialog.newInstance();

        @Override
        protected void onPreExecute() {
            loadingDialog.show(getSupportFragmentManager(), "");
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            loadHoliday();

            map = readHashMap("map");

            return null;
        }
        @Override

        protected void onPostExecute(Void tmp) {
            loadingDialog.dismiss();
            Intent intent = new Intent(getApplication(), CalendarActivity.class);
            //メイン画面起動
            startActivity(intent);
            finish();
        }
    }


    private void loadHoliday (){

        InputStream inputStream = getResources().openRawResource(R.raw.syukujitsu);
        String str = "def";
        int i = 0;
        String[][] tempStr = new String[65536][];

        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "MS932"));

            while(str != null){
                str = br.readLine();
                //文字コード変換
                if(str == null) {
                    break;
                }else{
                    str = new String(str.getBytes("UTF-8"), "UTF-8");
                }
                tempStr[i] = str.split(",", 2);
                holidayList.add(i, LocalDate.parse(tempStr[i][0], DateTimeFormatter.ofPattern("yyyy/[]M/[]d")));
                holidayNameList.add(i, tempStr[i][1]);
                i++;
            }

            br.close();
        }catch(FileNotFoundException e){
            System.out.println(e);
        }catch(IOException e){
            System.out.println(e);
        }
    }

    private HashMap<CompositeKey, String> readHashMap(String str){

        try{
            ObjectInputStream is = new ObjectInputStream(openFileInput(str));
            return (HashMap<CompositeKey, String>) is.readObject();
        }catch(FileNotFoundException e){
            System.out.println(e);
            return map;
        }catch(IOException e){
            System.out.println(e);
            return map;
        }catch(ClassNotFoundException e){
            System.out.println(e);
            return map;
        }
    }
}


