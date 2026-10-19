package com.example.praktikum1_2211002.data

data class Tugas(
    val id: Int = 0,
    val mataKuliah: String,
    val judul: String,
    val deskripsi: String,
    val deadline: String,
    val prioritas: String,
    val status: String
)

val daftarStatus = listOf("Belum", "Proses", "Selesai")
val daftarPrioritas = listOf("Rendah", "Sedang", "Tinggi")