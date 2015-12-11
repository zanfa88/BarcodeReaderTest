package it.zanfa88.barcodereadertest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;

public class MainActivity extends AppCompatActivity {
    Intent scanIntent;
    @Bind(R.id.btn_scanning) Button mBtnScanning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mBtnScanning.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                scanIntent = new Intent(getApplicationContext(), BarcodeScanner.class);
                startActivity(scanIntent);
            }
        });

    }

}
