package jp.ac.titech.itpro.sdl.camera_high

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.provider.MediaStore
import java.text.SimpleDateFormat
import android.os.Environment
import android.net.Uri
import java.io.File
import java.util.*
import androidx.core.content.FileProvider
import android.util.Log

class MainActivity : AppCompatActivity() {
    private var photoImage: Bitmap? = null
    private var cameraUri: Uri? = null
    private lateinit var path: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val photoButton = findViewById<Button>(R.id.photo_button)
        photoButton.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraUri = createSaveFileUri()
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri)
            val manager = packageManager
            val activities: List<*> = manager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
            if (!activities.isEmpty()) {
                startActivityForResult(intent, REQ_PHOTO)
            } else {
                Toast.makeText(this@MainActivity, R.string.toast_no_activities, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showPhoto() {
        /*
        if (photoImage == null) {
            return
        }
        val photoView = findViewById<ImageView>(R.id.photo_view)
        photoView.setImageBitmap(photoImage)
        */
        val photoView = findViewById<ImageView>(R.id.photo_view)
        if(cameraUri != null) {
            photoView.setImageURI(cameraUri);
            Log.d("debug", "startActivityForResult()")
        }

    }
    /*
    override fun onActivityResult(reqCode: Int, resCode: Int, data: Intent?) {
        super.onActivityResult(reqCode, resCode, data)
        if (reqCode == REQ_PHOTO) {
            if (resCode == Activity.RESULT_OK) {
                //photoImage = data?.extras?.get("data") as? Bitmap
            }
        }
    }
    */


    private fun createSaveFileUri(): Uri {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.JAPAN).format(Date())
        val imagefilename = timestamp

        //val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/casalack")
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        //if (!storageDir?.exists()){
            //storageDir?.mkdir()
        //}
        val file = File.createTempFile(imagefilename, ".jpg", storageDir)
        path = file.absolutePath
        return FileProvider.getUriForFile(this, "jp.ac.titech.itpro.sdl.camera_high", file)
    }

    override fun onResume() {
        super.onResume()
        showPhoto()
    }

    companion object {
        private const val REQ_PHOTO = 1234
    }
}