package com.example.praktikum6;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    LinearLayout llOn, llOff;
    RecyclerView recyclerView;
    TextView tvHead, tvBody;
    ImageView ivOff;
    Button btnMore, btnRetry;
    ProgressBar pbOn, pbOff;
    private ApiService apiService;
    private UserAdapter userAdapter;
    private int currentPage = 1;
    private boolean isLoading = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        llOn = findViewById(R.id.internetOn);
        llOff = findViewById(R.id.internetOff);
        tvHead = findViewById(R.id.tvHead);
        tvBody = findViewById(R.id.tvBody);
        ivOff = findViewById(R.id.ivOff);
        btnMore = findViewById(R.id.btnMore);
        btnRetry = findViewById(R.id.btnRetry);
        pbOff = findViewById(R.id.pbOff);
        pbOn = findViewById(R.id.pbOn);
        apiService = RetrofitClient.getClient().create(ApiService.class);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadData();

        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnMore.setVisibility(View.GONE);

                if (!isLoading) {
                    isLoading = true;
                    pbOn.setVisibility(View.VISIBLE);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(100);
                                currentPage++;

                                loadData();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        });

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivOff.setVisibility(View.GONE);
                tvHead.setVisibility(View.GONE);
                tvBody.setVisibility(View.GONE);
                btnRetry.setVisibility(View.GONE);
                pbOff.setVisibility(View.VISIBLE);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        loadData();
                    }
                }).start();
            }
        });
    }

    private void loadData() {
        btnMore.setVisibility(View.GONE);
        pbOn.setVisibility(View.VISIBLE);
        Call<UserResponse> call = apiService.getUsers(currentPage);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                pbOn.setVisibility(View.GONE);
                isLoading = false;
                if (response.isSuccessful()) {
                    llOn.setVisibility(View.VISIBLE);
                    btnMore.setVisibility(View.VISIBLE);
                    List<User> users = response.body().getData();
                    if (userAdapter == null) {
                        userAdapter = new UserAdapter(users);
                        recyclerView.setAdapter(userAdapter);
                    } else {
                        userAdapter.addUsers(users);
                        userAdapter.notifyDataSetChanged();
                    }
                    llOff.setVisibility(View.GONE);
                } else {
                    showError("Failed to fetch data. Please try again later.");
                }
            }
            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                pbOn.setVisibility(View.GONE);
                btnMore.setVisibility(View.GONE);
                llOn.setVisibility(View.GONE);
                isLoading = false;
                llOff.setVisibility(View.VISIBLE);
                ivOff.setVisibility(View.VISIBLE);
                tvHead.setVisibility(View.VISIBLE);
                tvBody.setVisibility(View.VISIBLE);
                btnRetry.setVisibility(View.VISIBLE);
                pbOff.setVisibility(View.GONE);
            }
        });
    }

    private void showError(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}