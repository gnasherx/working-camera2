package com.mobicule.documentscanner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.mobicule.documentscanner.Retrofit.NewClient;
import com.mobicule.documentscanner.Retrofit.Response;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

public class PreviewActivity extends AppCompatActivity {
    private final String TAG = PreviewActivity.class.getName();
    private ImageView mImageView;
    private File filePath;
    private Uri imageUri;
    private Uri finalUri;
    private Button uploadBtn;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        mImageView = findViewById(R.id.imageView);
        uploadBtn = findViewById(R.id.uploadBtn);
        filePath = new File(getIntent().getStringExtra("filePath"));
        type = getIntent().getStringExtra("type");
        imageUri = Uri.fromFile(filePath);

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog dialog = new ProgressDialog(PreviewActivity.this);
                dialog.setTitle("Uploading Image");
                dialog.setMessage("Please wait while we send your image to process...");
                dialog.show();

                try {
                    Bitmap bmp = Utils.getBitmap(PreviewActivity.this, finalUri);
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bmp, 1920, 1080, true);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    scaledBitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
                    byte[] byteArray = stream.toByteArray();
                    bmp.recycle();
                    Uri nextUri = Utils.getUri(PreviewActivity.this,scaledBitmap);
                    uploadData(Base64.encodeToString(byteArray, Base64.DEFAULT), type, nextUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        CropImage.activity(imageUri)
                .start(this);

    }

    private void uploadData(String base64Data, final String type, final Uri uri) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("image", base64Data);
            jsonObject.accumulate("type", type);
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl("http://ed4eeaaf.ngrok.io")
                    .addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit = builder.build();

            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());

            NewClient newClient = retrofit.create(NewClient.class);
            Call<Response> call = newClient.sendData(body);
            call.enqueue(new Callback<Response>() {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                    try {
                        if (response.isSuccessful()) {
                            Toast.makeText(PreviewActivity.this, "Successfull", Toast.LENGTH_LONG).show();
                            Response returnObj = response.body();
                            Log.d("JsonReturn", returnObj.getStatus() + " " + returnObj.getFields().toString());
                            Intent data = new Intent(PreviewActivity.this,EditFormActivity.class);
                            data.putExtra("uri", uri);
                            data.putExtra("fields", returnObj.getFields().toString());
                            data.putExtra("type", type);
                            data.putExtra("name",returnObj.getName());
                            startActivity(data);
                        } else {
                            Toast.makeText(PreviewActivity.this, "Not completely Successful", Toast.LENGTH_LONG).show();
//                            Snackbar snackbar = Snackbar.make(getView(),"Failed!!! Due to high network traffic.",1000);
//                            snackbar.show();
                            Log.d(TAG, "instance initializer: Not completely successful!!");
                        }
                    } catch (Exception e) {
                        Log.d(TAG, "onResponse: Try catch error:");
                        Toast.makeText(PreviewActivity.this, "y catch error", Toast.LENGTH_LONG).show();
//                        Snackbar snackbar = Snackbar.make(getView(),"Failed!!! Due to high network traffic.",1000);
//                        snackbar.show();
                    }
                }


                @Override
                public void onFailure(Call<Response> call, Throwable t) {
                    Log.d(TAG, "onFailure: Throwable: " + t);
//                    Snackbar snackbar = Snackbar.make(getView(),"Failed!!! Your internet connection seems to be slow.",1000);
//                    snackbar.show();
                    Toast.makeText(PreviewActivity.this, "Failed!!! Your internet connection seems to be slow.", Toast.LENGTH_LONG).show();
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                finalUri = result.getUri();
                mImageView.setImageURI(finalUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}
