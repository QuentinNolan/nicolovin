package com.example.quentin.csg_qualifs_18;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.View;
import android.content.Context;
import android.content.Intent;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.*;

public class ListViewItemNote extends ArrayAdapter<Note> {

    private Integer[] tab_images_pour_la_liste = {
            R.drawable.ic_menu_camera,
            R.drawable.ic_menu_gallery,
            R.drawable.ic_menu_manage,
            R.drawable.ic_menu_slideshow,
            R.drawable.ic_menu_share,
            R.drawable.ic_menu_send };

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)
                getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.rowlayout, parent, false);

        TextView textView = (TextView) rowView.findViewById(R.id.notesDescription);
        TextView dateTextView = (TextView) rowView.findViewById(R.id.noteDate);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.noteIcon);

        textView.setText(getItem(position).getNote());
        dateTextView.setText(getItem(position).getTimeStamp());
        imageView.setImageResource(tab_images_pour_la_liste[getItem(position).getImagePosition()]);

        return rowView;
    }

    public ListViewItemNote(Context context, List<Note> values) {
        super(context, R.layout.rowlayout, values);
    }
}