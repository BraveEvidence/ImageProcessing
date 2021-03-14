package com.demo.imageprocessing

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private lateinit var btn: Button
    private lateinit var imgView: ImageView



    private val mLoaderCallback: BaseLoaderCallback = object : BaseLoaderCallback(this) {
        override fun onManagerConnected(status: Int) {
            when (status) {
                SUCCESS -> {
                    var img: Mat? = null

                    try {
                        img = Utils.loadResource(applicationContext, R.mipmap.ic_launcher)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    Imgproc.cvtColor(img, img, Imgproc.COLOR_RGB2BGRA)

                    val img_result = img!!.clone()
                    Imgproc.Canny(img, img_result, 80.0, 90.0)
                    val img_bitmap = Bitmap.createBitmap(img_result.cols(), img_result.rows(), Bitmap.Config.ARGB_8888)
                    Utils.matToBitmap(img_result, img_bitmap)
                    imgView.setImageBitmap(img_bitmap)

                }
                else -> {
                    super.onManagerConnected(status)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback)
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS)
        }
    }

    companion object{
        init {
            if (!OpenCVLoader.initDebug()) {
                Log.i("Fargo","'Intiialization error'")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        OpenCVLoader.initDebug()
        btn = findViewById(R.id.btn)
        imgView = findViewById(R.id.img)
    }
}