package com.example.quentin.csg_qualifs_18

import android.R.*
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import java.security.AccessController.getContext


class AddNoteActivity : AppCompatActivity() {

    private val tab_images_pour_la_liste = arrayOf(R.drawable.ic_menu_camera, R.drawable.ic_menu_gallery, R.drawable.ic_menu_manage, R.drawable.ic_menu_slideshow, R.drawable.ic_menu_share, R.drawable.ic_menu_send)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        val spinner: Spinner = findViewById<View>(R.id.iconSpinner) as Spinner
        val adapter = ArrayAdapter.createFromResource(this, R.array.iconSpinner, layout.simple_spinner_item)
        adapter.setDropDownViewResource(layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            //Performing action onItemSelected and onNothing selected
            override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {

                spinner.setBackgroundResource(tab_images_pour_la_liste[position])

            }

            override fun onNothingSelected(arg0: AdapterView<*>) {
            }

        }

        ///Log.d("selected=", aaa.toString())
    }

    companion object {
        val EXTRA_NOTE_DESCRIPTION = "note"
        val EXTRA_NOTE_IMAGE = "note_image"
    }

    @SuppressLint("WrongViewCast")
    fun doneClicked(view: View) {
        val noteDescription = findViewById<EditText>(R.id.descriptionText).text.toString()
        val noteImage = findViewById<View>(R.id.iconSpinner) as Spinner

        if (!noteDescription.isEmpty()) {
            val result = Intent()
            result.putExtra(EXTRA_NOTE_DESCRIPTION, noteDescription)
            result.putExtra(EXTRA_NOTE_IMAGE, noteImage.selectedItemPosition)
            setResult(Activity.RESULT_OK, result)
        } else {
            setResult(Activity.RESULT_CANCELED)
        }

        finish()
    }
}
