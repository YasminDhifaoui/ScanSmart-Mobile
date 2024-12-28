package com.example.detectionvisage;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.io.InputStream;
import java.util.List;

import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceLandmark;

public class MainActivity extends AppCompatActivity {

    private Button btnSuivant;
    private ToggleButton tglVisible;
    private FaceView fView;
    private int[] tImage = new int[] { R.raw.image0, R.raw.image1, R.raw.image2, R.raw.image3, R.raw.image4 ,R.raw.img};
    private int indiceCourant;
  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        btnSuivant = (Button) findViewById(R.id.btnSuivant);
        tglVisible = (ToggleButton) findViewById(R.id.tglVisible);
        fView = (FaceView) findViewById(R.id.fView);
        indiceCourant = 0;
        detecter(tglVisible.isChecked());
        ajouterEcouteur();
    }
    private void ajouterEcouteur() {
        btnSuivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                suivant();
            }
        });
        tglVisible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                detecter(arg1);
            }
        });
    }
    private void detecter(boolean afficherInfo) {
        InputStream stream = getResources().openRawResource(tImage[indiceCourant]);
        Bitmap bitmap = BitmapFactory.decodeStream(stream);
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        if (afficherInfo) {
            FaceDetectorOptions options =
                    new FaceDetectorOptions.Builder()
                            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                            .setMinFaceSize(0.15f)
                            .enableTracking()
                            .build();
            FaceDetector detector = FaceDetection.getClient(options);
            Task<List<Face>> result =
                    detector.process(image)
                            .addOnSuccessListener(
                                    new OnSuccessListener<List<Face>>() {
                                        @Override
                                        public void onSuccess(List<Face> faces) {
                                            // Task completed successfully
                                            // ...
                                            fView.setContent(bitmap, faces);
                                        }
                                    })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Task failed with an exception
                                            // ...
                                            Toast.makeText(MainActivity.this, "Failure: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
        } else
            fView.setContent(bitmap, null);
    }
    private void suivant() {
        indiceCourant = (indiceCourant + 1) % tImage.length;
        detecter(tglVisible.isChecked());
    }

    
}
