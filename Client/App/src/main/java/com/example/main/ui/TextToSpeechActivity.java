package com.example.main.ui;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.main.R;
import com.example.main.data.model.Item;
import com.example.main.util.ImageConverterUtil;

import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public class TextToSpeechActivity extends AppCompatActivity {
    private android.speech.tts.TextToSpeech textToSpeech;

    private ImageButton button;
    private RadioGroup radioGroup;
    private TextView textView;
    private TextView textDes;
    private ImageView imageView;

    private Item item;
    private boolean ready;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texttospeech);

        item = (Item)getIntent().getSerializableExtra("item");

        button = (ImageButton) findViewById(R.id.imageButton);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        textView = (TextView) findViewById(R.id.textView2);
        textView.setText(item.getLabel());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.textsize));

        textDes = (TextView) findViewById(R.id.textView3);
        textDes.setText(item.getDescription());

        imageView = (ImageView) findViewById(R.id.imageView2);
        Bitmap b = ImageConverterUtil.StringToBitMap(item.getImage());
        imageView.setImageBitmap(b);

        textToSpeech = new android.speech.tts.TextToSpeech(getApplicationContext(), status -> {
            if (status == android.speech.tts.TextToSpeech.SUCCESS) {
                Log.e("TTS", "TextToSpeech.OnInitListener.onInit...");
                printOutSupportedLanguages();
                setTextToSpeechLanguage();
            }
            else{
                Log.e("Failure","TextToSpeechFailure");
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
        String toSpeak = item.getLabel();
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