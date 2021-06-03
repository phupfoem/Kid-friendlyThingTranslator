package com.example.main;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public class TextToSpeech extends AppCompatActivity {
    private android.speech.tts.TextToSpeech textToSpeech;
    private ImageButton button;

    private TextView textView;
    private String text = "BALL";

    private TextView textDes;
    private String textDescription = "A round object used for in games or sports";
    RadioGroup radioGroup;
    private boolean ready;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texttospeech);
        button = (ImageButton) findViewById(R.id.imageButton);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        textView = (TextView) findViewById(R.id.textView2);
        textView.setText(text);

        textDes = (TextView) findViewById(R.id.textView3);
        textDes.setText(textDescription);

        textToSpeech = new android.speech.tts.TextToSpeech(getApplicationContext(), new android.speech.tts.TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                Log.e("TTS", "TextToSpeech.OnInitListener.onInit...");
                printOutSupportedLanguages();
                setTextToSpeechLanguage();
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setTextToSpeechLanguage();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakOut();
            }
        });
    }

    private void printOutSupportedLanguages()  {
        // Supported Languages
        Set<Locale> supportedLanguages = textToSpeech.getAvailableLanguages();
        if(supportedLanguages!= null) {
            for (Locale lang : supportedLanguages) {
                Log.e("TTS", "Supported Language: " + lang);
            }
        }
    }


    private void speakOut() {
        if (!ready) {
            Toast.makeText(this, "Text to Speech not ready", Toast.LENGTH_LONG).show();
            return;
        }
        // Text to Speak
        String toSpeak = text;
        Toast.makeText(this, toSpeak, Toast.LENGTH_SHORT).show();
        // A random String (Unique ID).
        String utteranceId = UUID.randomUUID().toString();
        textToSpeech.speak(toSpeak, android.speech.tts.TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }


    private void setTextToSpeechLanguage() {
        Locale language = Locale.ENGLISH;;
        int result = textToSpeech.setLanguage(language);
        if (result == android.speech.tts.TextToSpeech.LANG_MISSING_DATA) {
            this.ready = false;
            Toast.makeText(this, "Missing language data", Toast.LENGTH_SHORT).show();
            return;
        } else if (result == android.speech.tts.TextToSpeech.LANG_NOT_SUPPORTED) {
            this.ready = false;
            Toast.makeText(this, "Language not supported", Toast.LENGTH_SHORT).show();
            return;
        } else {
            this.ready = true;
            Locale currentLanguage = textToSpeech.getVoice().getLocale();
            Toast.makeText(this, "Language " + currentLanguage, Toast.LENGTH_SHORT).show();
        }
    }


}