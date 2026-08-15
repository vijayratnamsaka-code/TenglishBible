package com.tenglishbible.tenglishbible

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStreamReader

@Database(entities = [Verse::class], version = 1, exportSchema = false)
abstract class BibleDatabase : RoomDatabase() {

    abstract fun verseDao(): VerseDao

    companion object {
        @Volatile
        private var INSTANCE: BibleDatabase? = null

        fun getDatabase(context: Context): BibleDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BibleDatabase::class.java,
                    "bible_data.db"
                )
                    // App first time open ayinappudu JSON ni load cheyadaniki callback
                    .addCallback(BibleDatabaseCallback(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class BibleDatabaseCallback(
        private val context: Context
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            CoroutineScope(Dispatchers.IO).launch {
                val database = getDatabase(context)
                loadJsonData(context, database.verseDao())
            }
        }

        suspend fun loadJsonData(context: Context, verseDao: VerseDao) {
            try {
                // Assets folder nundi tenglish_bible.json ni open cheyadam
                val inputStream = context.assets.open("tenglish_bible.json")
                val reader = InputStreamReader(inputStream)

                // JSON ni Verse objects ga marchadam
                val itemType = object : TypeToken<List<Verse>>() {}.type
                val verses: List<Verse> = Gson().fromJson(reader, itemType)

                // Aa verses annitini database loki insert cheyadam
                verseDao.insertAllVerses(verses)

                reader.close()
                inputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}