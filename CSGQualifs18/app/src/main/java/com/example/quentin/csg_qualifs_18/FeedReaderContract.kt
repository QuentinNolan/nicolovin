package com.example.quentin.csg_qualifs_18

import android.provider.BaseColumns

object FeedReaderContract {
    // Table contents are grouped together in an anonymous object.
    object FeedEntry : BaseColumns {
        const val TABLE_NAME = "notes"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_DATE = "date"
        const val COLUMN_NAME_FILENAME = "filename"
    }
}

const val SQL_CREATE_ENTRIES =
        "CREATE TABLE ${FeedReaderContract.FeedEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE} TEXT," +
                "${FeedReaderContract.FeedEntry.COLUMN_NAME_DATE} TEXT," +
                "${FeedReaderContract.FeedEntry.COLUMN_NAME_FILENAME} INT)"

const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${FeedReaderContract.FeedEntry.TABLE_NAME}"