package com.example.visorx;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Path;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XrayActivity extends AppCompatActivity {

    Button takePicButton;
    Button selectFileButton;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    public static final int PICK_IMAGE_REQUEST = 1;
    public static final int PIXEL_WIDTH = 112;

    private static final String MODEL_PATH = "tflite_model.tflite";
    private static final String LABEL_PATH = "labels.txt";

    private static final int INPUT_SIZE = 112*112*3;

    private Classifier classifier;

    ImageView XRayImageView;
    Button detectXRayButton;

    Bitmap storedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xray);

        detectXRayButton = findViewById(R.id.detectXRayButton);
        takePicButton = findViewById(R.id.takePicButton);
        selectFileButton = findViewById(R.id.selectFileButton);
        XRayImageView = findViewById(R.id.XRayImageView);

        takePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= 23) {
                    if (checkSelfPermission(Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA},
                                MY_CAMERA_PERMISSION_CODE);
                    } else {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }
                }
                else{
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }

            }
        });

        selectFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        detectXRayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Do Bitmap Stuff
                //Bitmap resizedImage = Bitmap.createScaledBitmap(storedImage,112,112,true);
                float pixels[][][][] = new float[1][112][112][3];
                //resizedImage.getPixels(pixels,0,0,0,0,112,112);

                //float pixels2[] = new float[storedImage.getWidth()*storedImage.getHeight()];

                for(int i = 0;i<112;i++){
                    for(int j = 0;j<112;j++){
                        int color = storedImage.getPixel(i,j);
                        int red = Color.red(color);
                        int blue = Color.blue(color);
                        int green = Color.green(color);

                        pixels[0][i][j][0] = red;
                        pixels[0][i][j][1] = green;
                        pixels[0][i][j][2] = blue;
                    }
                }


                Classification ans = classifier.recognize(pixels);

                //Toast.makeText(getApplicationContext(),"Label : " + ans.getLabel() +" ,Confidence" + ans.getConf(),Toast.LENGTH_LONG).show();

                //Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(XrayActivity.this);
                alertDialog.setTitle("Result");
                String label = ans.getLabel();
                if(label.equals("1")){
                    label = "Fracture";
                }
                else{
                    label = "No Fracture";
                }
                float confidence = ans.getConf();
                alertDialog.setMessage("Prediction : " + label + "\nPrediction Confidence: " + confidence);
                alertDialog.setPositiveButton("OK",null);
                alertDialog.show();

            }
        });


        loadModel();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new
                        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            storedImage = photo;

            //imageView.setImageBitmap(photo);
            XRayImageView.setImageBitmap(photo);
        }
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                storedImage = bitmap;

                //ImageView imageView = (ImageView) findViewById(R.id.imageView);
                XRayImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "You have not selected and image", Toast.LENGTH_SHORT).show();
        }
    }

    public void loadModel() {
        try {
            classifier = TensorFlowClassifier.create(
                    getAssets(),
                    MODEL_PATH,
                    LABEL_PATH,
                    INPUT_SIZE);
        } catch (IOException e) {
            Log.i("LoadError","Occured"+e.toString());
            e.printStackTrace();
        }
    }



    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left,R.anim.slide_right_out);
    }


}
