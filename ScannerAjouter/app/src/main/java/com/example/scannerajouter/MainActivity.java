package com.example.scannerajouter;


import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;

public class MainActivity extends AppCompatActivity {
    private Button btnScan;
    private ListView lstBc;
    private ArrayAdapter<String> adpBc;
    private GmsBarcodeScanner scanner;
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
        btnScan=findViewById(R.id.btnScan);
        lstBc=findViewById(R.id.lstBc);
        adpBc=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        lstBc.setAdapter(adpBc);
        initScanner();
        ajouterEcouteurs();
    }

    private void initScanner() {
        GmsBarcodeScannerOptions options = new GmsBarcodeScannerOptions.Builder()
                .setBarcodeFormats(
                        Barcode.FORMAT_QR_CODE,
                        Barcode.FORMAT_EAN_13,
                        Barcode.FORMAT_ALL_FORMATS)
                .enableAutoZoom()
                .build();
        scanner = GmsBarcodeScanning.getClient(this);
    }

    private void ajouterEcouteurs() {
        btnScan.setOnClickListener(view -> scan());
    }

    private void scan() {
        scanner.startScan()
                .addOnSuccessListener(
                        barcode -> {
                            String rawValue = barcode.getRawValue();
                            adpBc.add(rawValue);
                        })
                .addOnCanceledListener(
                        () -> {
                            Toast.makeText(this, "Canceled!", Toast.LENGTH_LONG).show();
                        })
                .addOnFailureListener(
                        e -> {
                            Toast.makeText(this, "Failure: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        });
    }
}