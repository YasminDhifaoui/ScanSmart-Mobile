package com.example.traductionenfr;


import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

public class MainActivity extends AppCompatActivity {
    private EditText edPhraseEn;
    private EditText edPhraseFr;
    private Button btnTraduire;
    private Translator enFrTranslator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initGraphique();
    }

    private void initGraphique() {
        edPhraseEn = findViewById(R.id.edPhraseEn);
        edPhraseFr = findViewById(R.id.edPhraseFr);
        btnTraduire = findViewById(R.id.btnTraduire);
        initTraducteur();
        ajouterEcouteurs();
    }
    private void initTraducteur() {
        TranslatorOptions options =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(TranslateLanguage.FRENCH)
                        .build();
        enFrTranslator =
                Translation.getClient(options);
        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();
        enFrTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(
                        new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Toast.makeText(MainActivity.this, "Succés de téléchargement du modèle En-Fr",
                                        Toast.LENGTH_LONG).show();
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Failure: "+e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
    }
    private void ajouterEcouteurs() {
        btnTraduire.setOnClickListener(view -> traduire());
    }

    private void traduire() {
        if (!edPhraseEn.getText().toString().isEmpty()) {
            enFrTranslator.translate(edPhraseEn.getText().toString())
                    .addOnSuccessListener(
                            new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    if(o!=null)
                                        edPhraseFr.setText(o.toString());
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, "Failure: "+e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
        }else
            edPhraseFr.setText("");
        
    }


}