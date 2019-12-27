package com.giladdev.mediaplayer

import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.song_ticket.view.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    var listofSongs = arrayListOf<SongInfo>()
    lateinit var songsAdapter: MySongAdaptor
    var mp: MediaPlayer?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadUrlOnline()

        CheckUserPermsions()
        var myTacking = mySongTrack()
        myTacking.start()


    }

    fun loadUrlOnline() {
        listofSongs.add(
            SongInfo(
                "Online Content 001", "Classic1",
                "https://www.mfiles.co.uk/mp3-downloads/grieg-holberg-suite-3-gavotte.mp3"
            )
        )
        listofSongs.add(
            SongInfo(
                "Online Content 002", "Gabriel Fauré's Dolly Suite",
                "https://www.mfiles.co.uk/mp3-downloads/faure-dolly-suite-1-berceuse.mp3"
            )
        )
        listofSongs.add(
            SongInfo(
                "Online Content 003", "Saltarello 2 from a Medieval Manuscript",
                "https://www.mfiles.co.uk/mp3-downloads/grieg-holberg-suite-3-gavotte.mp3"
            )
        )

        listofSongs.add(
            SongInfo(
                "local", "Gashash",
                "/storage/emulated/0/WhatsApp/Media/WhatsApp Audio/AUD-20191112-WA0005.mp3"
            )

        )
    }

    inner class MySongAdaptor(var myListSing: ArrayList<SongInfo>) : BaseAdapter() {


        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var myView = layoutInflater.inflate(R.layout.song_ticket, null)
            val song = myListSing[position]
            myView.tvSongName.text = song.title
            myView.tvAuthor.text = song.authorName
            myView.btnPlay.setOnClickListener {

                if (myView.btnPlay.text.equals("STOP")) {

                    mp!!.stop()
                    myView.btnPlay.text = "PLAY"
                } else {
                    mp = MediaPlayer()

                    try {
                        mp!!.setDataSource(applicationContext, song.songURL!!.toUri())
                        mp!!.prepare()
                        mp!!.start()
                        myView.btnPlay.text = "STOP"
                        sbProgress.max = mp!!.duration

                    } catch (ex: Exception) {
                        Toast.makeText(applicationContext, ex.message,Toast.LENGTH_LONG).show()
                    }
                }

            }

            return myView
        }

        override fun getItem(position: Int): Any {
            return myListSing[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount() = myListSing.size
    }

    inner  class  mySongTrack :Thread(){


        override fun run() {
            while(true){
                try{
                    sleep(1000)
                }catch (ex:Exception){}

                runOnUiThread {

                    if (mp!=null){
                        sbProgress.progress = mp!!.currentPosition
                    }
                }
            }

        }
    }

    private val REQUEST_CODE_ASK_PERMISSIONS = 123

    fun CheckUserPermsions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_CODE_ASK_PERMISSIONS)
                return
            }
        }
        loadSong()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_ASK_PERMISSIONS -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadSong()
            } else {
                // Permission Denied
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT)
                    .show()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    fun loadSong(){
        val allSongsURI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        var selection=MediaStore.Audio.Media.IS_MUSIC+"=1"
        val cursor = contentResolver.query(allSongsURI,null,selection,
            null, null)
        val columnIndexID : Int
        var imageId : Long

        if (cursor != null){
            if (cursor!!.moveToFirst()){
                columnIndexID = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                do{
                    imageId = cursor.getLong(columnIndexID)
                    //val uriImage = Uri.withAppendedPath(uriExternal, "" + imageId)
                    val uriImage = ContentUris.withAppendedId(
                        allSongsURI,
                        imageId)
                    val path = cursor.getString(cursor!!.getColumnIndex(MediaStore.Audio.Media.DATA))
                    val fileName = cursor.getString(cursor!!.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
                    val songAuthor = cursor.getString(cursor!!.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    val songName = cursor.getString(cursor!!.getColumnIndex(MediaStore.Audio.Media.TITLE))
                    listofSongs.add(SongInfo( songName, songAuthor, uriImage.toString()))

                } while (cursor!!.moveToNext())
            }

            cursor!!.close()

            songsAdapter = MySongAdaptor(listofSongs)
            lsListSongs.adapter = songsAdapter
        }
    }
}
