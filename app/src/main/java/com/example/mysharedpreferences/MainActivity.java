package com.example.mysharedpreferences;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tvName, tvAge, tvPhoneNo, tvEmail, tvIsLoveMU;
    private Button btnSave;
    private UserPreference mUserPreference;

    private boolean isPreferenceEmpty = false;
    private UserModel userModel;

    final ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getData() != null && result.getResultCode() == FormUserPreferenceActivity.RESULT_CODE) {
                    userModel = result.getData().getParcelableExtra(FormUserPreferenceActivity.EXTRA_RESULT);
                    populateView(userModel);
                    checkForm(userModel);
                }
            });

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

        tvName = findViewById(R.id.tv_name);
        tvAge = findViewById(R.id.tv_age);
        tvPhoneNo = findViewById(R.id.tv_phone);
        tvEmail = findViewById(R.id.tv_email);
        tvIsLoveMU = findViewById(R.id.tv_is_love_mu);
        btnSave = findViewById(R.id.btn_save);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("My User Preference");
        }

        mUserPreference = new UserPreference(this);

        showExistingPreference();

        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_save) {
            Intent intent = new Intent(MainActivity.this, FormUserPreferenceActivity.class);
            if (isPreferenceEmpty) {
                intent.putExtra(FormUserPreferenceActivity.EXTRA_TYPE_FORM, FormUserPreferenceActivity.TYPE_ADD);
            } else {
                intent.putExtra(FormUserPreferenceActivity.EXTRA_TYPE_FORM, FormUserPreferenceActivity.TYPE_EDIT);
            }
            intent.putExtra("USER", userModel);
            resultLauncher.launch(intent);
        }
    }

    private void showExistingPreference() {
        userModel = mUserPreference.getUser();
        populateView(userModel);
        checkForm(userModel);
    }

    private void populateView(UserModel userModel) {
        tvName.setText(userModel.getName().isEmpty() ? "Tidak Ada" : userModel.getName());
        tvAge.setText(String.valueOf(userModel.getAge()).isEmpty() ? "Tidak Ada" : String.valueOf(userModel.getAge()));
        tvIsLoveMU.setText(userModel.isLove() ? "Ya" : "Tidak");
        tvEmail.setText(userModel.getEmail().isEmpty() ? "Tidak Ada" : userModel.getEmail());
        tvPhoneNo.setText(userModel.getPhoneNumber().isEmpty() ? "Tidak Ada" : userModel.getPhoneNumber());
    }

    private void checkForm(UserModel userModel) {
        if (!userModel.getName().isEmpty()) {
            btnSave.setText(getString(R.string.change));
            isPreferenceEmpty = false;
        } else {
            btnSave.setText(getString(R.string.save));
            isPreferenceEmpty = true;
        }
    }
}