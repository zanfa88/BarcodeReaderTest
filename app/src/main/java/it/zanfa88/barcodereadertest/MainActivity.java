package it.zanfa88.barcodereadertest;

import android.app.VoiceInteractor;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.apache.http.params.HttpParams;
import org.json.JSONException;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.google.android.gms.common.api.CommonStatusCodes.SUCCESS;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "BarcodeReader - Main";
    Intent scanIntent;
    @Bind(R.id.btn_scanning) Button mBtnScanning;
    @Bind(R.id.input_barcode) EditText mInputBarcode;
    @Bind(R.id.btn_invia_post) Button mBtnInviaPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mBtnScanning.setOnClickListener(this);
        mBtnInviaPost.setOnClickListener(this);
    }// On create

    public void onClick(View v) {
        Log.d(TAG, "Clicked");
        if (v.getId() == R.id.btn_scanning) {
            scanIntent = new Intent(getApplicationContext(), BarcodeScanner.class);
            Log.d(TAG, "Starting intent BarcodeScanner");
            startActivityForResult(scanIntent, RC_BARCODE_CAPTURE);
        } else if (v.getId() == R.id.btn_invia_post) {
            Log.d(TAG, "Sending json");

            //if (isNetworkAvailable()){
                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormEncodingBuilder()
                        .add("barcode", String.valueOf(mInputBarcode.getText()))
                        .build();

                Request request = new Request.Builder()
                        .url("http://www.zanfa88.tk/app/prova.php")
                        .post(formBody)
                        .build();

                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {

                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        Log.d(TAG, "RESPONSE");
                    }
                });
            //}
        }

    }

    // Leggo la risposta dell'intent
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "Risposta dall'intent ricevuta");
                mInputBarcode.setText(data.getStringExtra("barcode"));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }



    // Controllo se c'è accesso alla rete internet
    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvaible = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvaible = true;
        }
        return true;
    }
}
