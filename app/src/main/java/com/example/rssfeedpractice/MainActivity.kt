package com.example.rssfeedpractice

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var rvMain: RecyclerView
    private lateinit var fab: FloatingActionButton
    private lateinit var adapter: SOFRecyclerViewAdapter
    private lateinit var questionsList: MutableList<SOFData>
    private lateinit var loading: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvMain = findViewById(R.id.rvMain)
        fab = findViewById(R.id.floatingActionButton)
        loading = findViewById(R.id.progressBar)


        questionsList = arrayListOf()
        FetchTopSongs().execute()
        adapter = SOFRecyclerViewAdapter(questionsList)
        rvMain.layoutManager = LinearLayoutManager(this)


        fab.setOnClickListener {
            rvMain.visibility = View.GONE
            loading.visibility = View.VISIBLE
            FetchTopSongs().execute()
        }
    }


    private inner class FetchTopSongs : AsyncTask<Void, Void, MutableList<SOFData>>() {
        val parser = XMLParser()
        override fun doInBackground(vararg params: Void?): MutableList<SOFData> {
            val urlConnection = URL("https://stackoverflow.com/feeds").openConnection() as HttpURLConnection
            questionsList =
                urlConnection.inputStream?.let {
                    parser.parse(it)
                } as MutableList<SOFData>
            return questionsList
        }

        override fun onPostExecute(result: MutableList<SOFData>?) {
            super.onPostExecute(result)
            adapter =
                SOFRecyclerViewAdapter(questionsList)
            rvMain.adapter = adapter
            rvMain.visibility = View.VISIBLE
            loading.visibility = View.GONE
        }

    }
}

