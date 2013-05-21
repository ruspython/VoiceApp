package ru.hitage.voiceapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener {
    private ListView mList;
    private Button speakButton;
    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        speakButton = (Button) findViewById(R.id.button);
        speakButton.setOnClickListener(this);
        mList = (ListView) findViewById(R.id.listView);
    }

    @Override
    public void onClick(View v) {
        startSpeak();
    }

    public void startSpeak() {
        Intent intent =  new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Произнесите команду");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);

    }

    @SuppressLint("NewApi")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK){
            ArrayList commandList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            mList.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, commandList));

            // для лучшего распознавания английского языка, поставьте в настройках англ. яз как язык системы
            // хотя все то же самое можно проделать и с русскими словами
            if (commandList.contains("red")){
                speakButton.setText("red");
                speakButton.setBackgroundColor(Color.RED);
            }

            if (commandList.contains("blue")){
                speakButton.setText("blue");
                speakButton.setBackgroundColor(Color.BLUE);
            }

            if (commandList.contains("green")){
                speakButton.setText("green");
                speakButton.setBackgroundColor(Color.GREEN);
            }

            if (commandList.contains("yellow")){
                speakButton.setText("yellow");
                speakButton.setBackgroundColor(Color.YELLOW);
            }

            if (commandList.contains("white")){
                speakButton.setText("white");
                speakButton.setBackgroundColor(Color.WHITE);
            }

            if (commandList.contains("black")){
                speakButton.setText("black");
                speakButton.setBackgroundColor(Color.BLACK);
            }

            // выкл
            if (commandList.contains("finish")){
                finish();
            }
            // попробуем открыть гугловские карты
            if (commandList.contains("maps")){
                Intent i = new Intent();
                PackageManager manager = getPackageManager();
                i = manager.getLaunchIntentForPackage("com.google.android.apps.maps");
                i.addCategory(Intent.CATEGORY_LAUNCHER);
                startActivity(i);
            }

            //попросим открыть некоторые сайты

            if (commandList.contains("google")){
                finish();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                startActivity(browserIntent);
            }

            if (commandList.contains("facebook")){
                finish();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com"));
                startActivity(browserIntent);
            }

            // если у Вас есть права суперпользователя
            // Можно как-то и добавив  "android.permission.REBOOT" но я не разобрался еще
            if (commandList.contains("reboot")){
                try {
                    Process proc = Runtime.getRuntime()
                            .exec(new String[]{ "su", "-c", "reboot -p" });
                    proc.waitFor();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}
