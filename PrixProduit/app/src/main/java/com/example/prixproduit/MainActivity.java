package com.example.prixproduit;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button btnSuivant;
    private Button btnAnalyser;
    private ImageView imgProduit;
    private TextView tvPrix;
    private int[] tImage = new int[] { R.drawable.eau_m1,R.drawable.eau_m2,R.drawable.eau_safia };
    private int indiceCourant;
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
        btnSuivant=findViewById(R.id.btnSuivant);
        btnAnalyser=findViewById(R.id.btnAnalyser);
        imgProduit=findViewById(R.id.imgProduit);
        tvPrix=findViewById(R.id.tvPrix);
        indiceCourant=0;
        ajouterEcouteurs();
    }

    private void ajouterEcouteurs() {
        btnAnalyser.setOnClickListener(view -> analyser());
        btnSuivant.setOnClickListener(view -> suivant());
    }
    private void suivant() {
        indiceCourant = (indiceCourant + 1) % tImage.length;
        imgProduit.setImageResource(tImage[indiceCourant]);
        analyser();
    }

    private void analyser() {
        Bitmap bitmap = ((BitmapDrawable) imgProduit.getDrawable()).getBitmap();
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        BarcodeScanner scanner = BarcodeScanning.getClient();
        Task<List<Barcode>> result = scanner.process(image)
                .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                    @Override
                    public void onSuccess(List<Barcode> barcodes) {
// Task completed successfully
// ...
                        afficherPrix(barcodes);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
// Task failed with an exception
// ...
                        Toast.makeText(MainActivity.this, "Failure:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void afficherPrix(List<Barcode> barcodes) {
        if(barcodes.size()>0){
            if(barcodes.size()==1){
                Barcode bar = barcodes.get(0);
                tvPrix.setText(getPrix(bar.getRawValue()));
            }else
                Toast.makeText(this, "Plusieurs code à barre dans l'image!", Toast.LENGTH_SHORT).show();
        }else
            Toast.makeText(this, "Aucun code à bar dans l'image!", Toast.LENGTH_SHORT).show();
    }

    private String getPrix(String rawValue) {
        String prix="Le prix ";
        if(rawValue.equals("6191577600015"))
            prix+="de Eau Safia est 0.900 DT";
        else if(rawValue.equals("123456789012"))
            prix+="de Eau M1 est 6.700 DT";
        else if(rawValue.equals("3401312345624"))
            prix+="de Eau M2 est 4.300 DT";
        else prix+="Inconnu";
        return prix;
    }


}