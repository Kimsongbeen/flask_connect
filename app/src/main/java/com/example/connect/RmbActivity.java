package com.example.connect;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class RmbActivity extends AppCompatActivity {
    private ImageView imageView;
    private Button button;
    private OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rm_bg);

        button = findViewById(R.id.rm_button);
        imageView = findViewById(R.id.result_image);
        okHttpClient = new OkHttpClient();

        button.setOnClickListener(v -> {
            // 이미지 파일 경로 설정
            int drawableResId = R.drawable.icon_dog;
            Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), drawableResId);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("image", "icon_dog.jpg", RequestBody.create(imageBytes, MediaType.parse("image/jpeg")))
                    .build();

            Request request = new Request.Builder()
                    //.url("http://192.168.0.22:5000/upload_image")
                    .url("http://10.200.123.215:5000/upload_image")
                    .post(requestBody)
                    .build();

            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(
                        @NotNull Call call,
                        @NotNull IOException e) {
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "server down", Toast.LENGTH_SHORT).show());
                }


                @Override
                public void onResponse(Call call, Response response) {
                    if(!response.isSuccessful()){
                        runOnUiThread(() -> Toast.makeText(getApplicationContext(), "서버 응답 실패", Toast.LENGTH_SHORT).show());
                        return;
                    }

                    try {
                        final Bitmap processedBitmap = BitmapFactory.decodeStream(response.body().byteStream());
                        runOnUiThread(() -> {
                            imageView.setImageBitmap(processedBitmap);
                        });
                        System.out.println("변경된 이미지 값: "+processedBitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(() -> {
                                Toast.makeText(getApplicationContext(), "응답 처리 실패", Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            });
        });
    }
}
