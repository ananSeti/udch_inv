package com.toobeeinfo.qrcode

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.SparseArray
import android.view.SurfaceHolder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.util.isNotEmpty
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val requestCodeCameraPermission = 1001
    private lateinit var cameraSource: CameraSource
    private lateinit var detector:BarcodeDetector
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
            ){
            askForCameraPermission()
            }else{
            setupControls()
        }
    }

   private fun setupControls(){
       detector = BarcodeDetector.Builder(this@MainActivity).build()
       cameraSource = CameraSource.Builder(this@MainActivity,detector)
           .setAutoFocusEnabled(true)
           .build()
       cameraSurfaceView.holder.addCallback(surfaceCallBack)
       detector.setProcessor(processor)
   }

    private fun askForCameraPermission(){
        ActivityCompat.requestPermissions(
            this@MainActivity,
             arrayOf(Manifest.permission.CAMERA),
            requestCodeCameraPermission
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == requestCodeCameraPermission && grantResults.isNotEmpty()){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                setupControls()
            }else{
                Toast.makeText(applicationContext,"Permission Denied",Toast.LENGTH_SHORT).show()
            }
        }
    }

   private val surfaceCallBack = object : SurfaceHolder.Callback{
        override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
           // TODO("Not yet implemented")
            try{
                /*if (ActivityCompat.checkSelfPermission(
                        this@MainActivity,
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    cameraSource.start(surfaceHolder)
                    return
                }*/
                cameraSource.start(surfaceHolder)

            }catch (exception:Exception){
                Toast.makeText(applicationContext,"Somethin went wrong",Toast.LENGTH_SHORT).show()
            }

        }

        override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
           // TODO("Not yet implemented")
        }

        override fun surfaceDestroyed(p0: SurfaceHolder) {
           // TODO("Not yet implemented")
            cameraSource.stop()
        }
    }
   private val processor = object: Detector.Processor<Barcode>{
       override fun release() {
           //TODO("Not yet implemented")

       }

       override fun receiveDetections(detections: Detector.Detections<Barcode>?) {
           //TODO("Not yet implemented")
           if(detections != null && detections.detectedItems.isNotEmpty()){
               val qrCodes: SparseArray<Barcode> = detections.detectedItems
               val code = qrCodes.valueAt(0)
               textScanResult.text = code.displayValue
           }else{
               textScanResult.text =""
           }
       }
   }
}