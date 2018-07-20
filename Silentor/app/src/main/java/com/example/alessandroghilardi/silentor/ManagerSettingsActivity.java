package com.example.alessandroghilardi.silentor;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class ManagerSettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_settings);

        EditText rangeTxt = findViewById(R.id.editTextRange);

        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.range_setting), this.MODE_PRIVATE);


        int range = sharedPref.getInt(getString(R.string.range_setting), 100);

        rangeTxt.setText(Integer.toString(range));

    }

    public void saveRange(View view){

        EditText rangeTxt = findViewById(R.id.editTextRange);
        String newRange = rangeTxt.getText().toString();

        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.range_setting), this.MODE_PRIVATE);

        // set range
        sharedPref.edit().putInt(getString(R.string.range_setting), Integer.parseInt(newRange)).apply();

        finish();
    }

}
