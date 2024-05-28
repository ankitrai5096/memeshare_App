package com.example.meme

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.meme.ui.theme.MemeTheme
import org.json.JSONObject

@Suppress("NAME_SHADOWING", "DEPRECATION")
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // content layout imported here
        setContentView(R.layout.activity_main)


        val currentImageUrl : String? = null


        // made a function that loads the memes and also give it to the imageView
        fun loadMeme(){
            val progressBar = findViewById<ProgressBar>(R.id.progressBar1)
            progressBar.visibility = View.VISIBLE
            val url = "https://meme-api.com/gimme"

// Request a string response from the provided URL.
            // json object request from the internet through volley library
            val jsonObject1 = JsonObjectRequest(
                Request.Method.GET, url,null,
                { response ->
                    val imageView = findViewById<ImageView>(R.id.imageView)
                    val currentImageUrl = response.getString("url")

                    //glide library to convert the url into imageView with progress bar hide statements
                    Glide.with(this@MainActivity).load(currentImageUrl).listener(object :
                        RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBar.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable,
                            model: Any,
                            target: Target<Drawable>?,
                            dataSource: DataSource,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBar.visibility = View.GONE
                            return false
                        }


                    }).into(imageView)
                },
                { error ->
                    Toast.makeText(this@MainActivity, "Error loading meme: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            )

// Add the request to the RequestQueue.
            MySingleton.getInstance(this).addToRequestQueue(jsonObject1)
        }

//initialised the load memes function for the the meme to be activity just when opening the app
        loadMeme()


        // next button is set to the load memes function to load meme everytime it is clicked

        val button1 = findViewById<Button>(R.id.button)

        button1.setOnClickListener {
            Toast.makeText(
                this@MainActivity,
                "next Meme",
                Toast.LENGTH_SHORT
            ).show()

            loadMeme()
        }


        // button 2 set to intent send for sending the link to other apps
        val button2 = findViewById<Button>(R.id.button2)
        button2.setOnClickListener {


            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "Text/Plain"
            intent.putExtra(Intent.EXTRA_TEXT, "Hey Checkout this meme $currentImageUrl")

            val chooser = Intent.createChooser(intent, "Share this meme...")
            startActivity(chooser)
        }
    }
}


