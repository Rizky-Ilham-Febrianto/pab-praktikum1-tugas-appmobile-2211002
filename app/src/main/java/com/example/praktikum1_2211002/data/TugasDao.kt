package com.example.praktikum1_2211002.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TugasDao {
    @Query("SELECT * FROM tugas ORDER BY deadline ASC")
    fun semuaTugas(): Flow<List<Tugas>>

    @Query("SELECT * FROM tugas WHERE id = :id")
    fun observe(id: Int): Flow<Tugas?>

    @Query("SELECT * FROM tugas WHERE id = :id")
    suspend fun ambil(id: Int): Tugas?

    @Insert suspend fun tambah(tugas: Tugas)
    @Update suspend fun ubah(tugas: Tugas)
    @Delete suspend fun hapus(tugas: Tugas)
}