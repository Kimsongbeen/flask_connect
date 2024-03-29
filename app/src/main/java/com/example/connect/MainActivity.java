package com.example.connect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    private TextView pagenameTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pagenameTextView = findViewById(R.id.pagename);

        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder().url("http://10.200.119.158:5000/").build();
        //Request request = new Request.Builder().url("http://192.168.0.22:5000/").build();

        // making call asynchronously
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            // called if server is unreachable
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "server down", Toast.LENGTH_SHORT).show();
                        pagenameTextView.setText("error connecting to the server");
                    }
                });
            }

            @Override
            public void onResponse(
                    @NotNull Call call,
                    @NotNull Response response)
                    throws IOException {pagenameTextView.setText(response.body().string());
            }
        });
    }
    public void onButton1Clicked(View view) {
        Intent intent = new Intent(this, RmbActivity.class);
        startActivity(intent);
    }
}