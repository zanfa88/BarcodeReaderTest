package it.zanfa88.barcodereadertest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.jar.Manifest;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BarcodeScanner extends AppCompatActivity {
    private static final String TAG = "BarcodeReader - Scanner";
    @Bind(R.id.camera_preview) SurfaceView mCameraPreview;
    @Bind(R.id.barcode_preview) TextView mBarcodePreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);
        ButterKnife.bind(this);

        // First AutoFocus, second Flash
        createCameraSource(true, false);

    }

    private void createCameraSource(boolean autoFocus, boolean useFlash) {
        Context context = getApplicationContext();
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(context)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() != 0) {
                    Log.d(TAG, "Barcode detection ");
                    mBarcodePreview.post(new Runnable() {
                        @Override
                        public void run() {
                            mBarcodePreview.setText(barcodes.valueAt(0).displayValue);
                        }
                    });
                    Intent data = new Intent();
                    data.putExtra("barcode", barcodes.valueAt(0).displayValue);
                    setResult(Activity.RESULT_OK, data);
                    finish();
                }
            }
        });


        final CameraSource cameraSource = new CameraSource.Builder(context, barcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .build();

        mCameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    cameraSource.start(mCameraPreview.getHolder());
                } catch (IOException ie) {
                    Log.e(TAG, ie.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });
    }

}
