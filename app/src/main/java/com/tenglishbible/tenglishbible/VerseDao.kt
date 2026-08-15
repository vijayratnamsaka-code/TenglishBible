package com.tenglishbible.tenglishbible

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface VerseDao {

    // JSON nundi data ni database loki ekkinchadaniki
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllVerses(verses: List<Verse>)

    // Anni Books list thechukodaniki
    @Query("SELECT DISTINCT book_number FROM verses_table")
    suspend fun getAllBooks(): List<Int>

    // Oka Book select chesthe, andulo unna Chapters thechukodaniki
    @Query("SELECT DISTINCT chapter_number FROM verses_table WHERE book_number = :bookId")
    suspend fun getChaptersForBook(bookId: Int): List<Int>

    // Oka Chapter select chesthe, andulo unna Verses (vachanalu) thechukodaniki
    @Query("SELECT * FROM verses_table WHERE book_number = :bookId AND chapter_number = :chapterId")
    suspend fun getVersesForChapter(bookId: Int, chapterId: Int): List<Verse>
}