package com.example.quentin.csg_qualifs_18

import android.app.Activity
import android.app.AlertDialog
import android.database.sqlite.*;
import android.content.BroadcastReceiver
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.view.View
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.content.*;
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Environment
import android.provider.BaseColumns
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import java.text.SimpleDateFormat
import java.util.*
import android.util.Log
import com.example.quentin.csg_qualifs_18.FeedReaderContract.FeedEntry
import com.example.quentin.csg_qualifs_18.FeedReaderContract.FeedEntry.TABLE_NAME
import java.io.File
import java.io.FileWriter
import android.Manifest

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    private var MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 0

    private val PREFS_NOTES = "prefs_notes"
    private val KEY_NOTES_LIST = "notes_list"
    private val ADD_NOTE_REQUEST = 1

    private val notesList: MutableList<Note> = mutableListOf()
    private val adapter by lazy { makeAdapter(notesList) }

    private val dbHelper = FeedReaderDbHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        //val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        //navigationView.setNavigationItemSelectedListener(this)

        val notesListView = findViewById<ListView>(R.id.notes_list_view)

        notesListView.adapter = adapter

        notesListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            noteSelected(position)
        }

        loadNotes();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE)
        }
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ADD_NOTE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val noteDescription = data?.getStringExtra(AddNoteActivity.EXTRA_NOTE_DESCRIPTION)
                var noteImage = data?.getIntExtra(AddNoteActivity.EXTRA_NOTE_IMAGE, 0)
                noteDescription?.let {
                    val db = dbHelper.writableDatabase

                    val values = ContentValues().apply {
                        put(FeedEntry.COLUMN_NAME_TITLE, noteDescription)
                        put(FeedEntry.COLUMN_NAME_DATE, getCurrentTimeStamp())
                        put(FeedEntry.COLUMN_NAME_FILENAME, noteImage)
                    }
                    val newRowId = db?.insert(FeedEntry.TABLE_NAME, null, values)

                    loadNotes();
                }
            }
        }
    }

    override fun onBackPressed() {
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.my_notes) {

        } else if (id == R.id.export) {
            exportDb()
        }
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    fun exportDb() {
        val exportDir = File(Environment.getExternalStorageDirectory(), "")
        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }

        val file = File(exportDir, "mynotes.csv")

        file.createNewFile();
        val csvWrite = CSVWriter(FileWriter(file))
        val db = dbHelper.readableDatabase
        val projection = arrayOf(BaseColumns._ID, FeedEntry.COLUMN_NAME_TITLE, FeedEntry.COLUMN_NAME_DATE, FeedEntry.COLUMN_NAME_FILENAME)

        val sortOrder = "${FeedEntry.COLUMN_NAME_DATE} DESC"

        val cursor = db.query(
                FeedEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        )

        with(cursor) {
            while(moveToNext()){
                val arrStr = arrayOf<String>(cursor.getString(0), cursor.getString(1), cursor.getString(2))
                csvWrite.writeNext(arrStr);
            }
        }

        csvWrite.close();
        db.close();
    }

    fun addNoteClicked(view: View) {
        val intent = Intent(this, AddNoteActivity::class.java)
        startActivityForResult(intent, ADD_NOTE_REQUEST)
    }

    private fun makeAdapter(list: MutableList<Note>): ListViewItemNote = ListViewItemNote(this, notesList)
        //ArrayAdapter(this, R.layout.rowlayout, list)

    private fun noteSelected(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Task")
            .setMessage(notesList[position].getNote())
            .setPositiveButton("Delete", { _, _ ->
                deleteNote(position)
            })
            .setNegativeButton("Cancel", {
                dialog, _ -> dialog.cancel()
            })
            .create()
            .show()
    }

    private fun deleteNote(position: Int) {
        val db = dbHelper.writableDatabase

        db.delete(FeedEntry.TABLE_NAME, BaseColumns._ID + " = ?", arrayOf(notesList[position].id.toString()));

        loadNotes()
    }

    private fun getCurrentTimeStamp(): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CANADA_FRENCH)
        simpleDateFormat.timeZone = TimeZone.getTimeZone("EST")
        val now = Date()
        return simpleDateFormat.format(now)
    }

    private fun loadNotes() {
        val db = dbHelper.readableDatabase

        val projection = arrayOf(BaseColumns._ID, FeedEntry.COLUMN_NAME_TITLE, FeedEntry.COLUMN_NAME_DATE, FeedEntry.COLUMN_NAME_FILENAME)

        val sortOrder = "${FeedEntry.COLUMN_NAME_DATE} DESC"

        val cursor = db.query(
                FeedEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        )

        notesList.clear()
        with(cursor) {
            while(moveToNext()){
                val note = Note(
                        getLong(getColumnIndexOrThrow(BaseColumns._ID)),
                        getString(getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_TITLE)),
                        getString(getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_DATE)),
                        getInt(getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_FILENAME)))
                notesList.add(note)
            }
        }

        adapter.notifyDataSetChanged()
    }
}

private fun SQLiteDatabase.delete(TABLE_NAME: String, selection: String, selectionArgs: Array<Long>) {}

private fun <E> MutableList<E>.addAll(elements: List<String>) {}
