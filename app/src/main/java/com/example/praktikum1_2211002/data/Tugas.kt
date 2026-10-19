package com.example.praktikum1_2211002.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tugas")
data class Tugas(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val mataKuliah: String,
    val judul: String,
    val deskripsi: String,
    val deadline: String,
    val prioritas: String,
    val status: String
)

val daftarStatus = listOf("Belum", "Proses", "Selesai")
val daftarPrioritas = listOf("Rendah", "Sedang", "Tinggi")