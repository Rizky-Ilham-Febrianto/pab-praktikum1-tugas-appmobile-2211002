package com.example.praktikum1_2211002.data

import kotlinx.coroutines.flow.Flow

class TugasRepository(private val dao: TugasDao) {
    val semuaTugas: Flow<List<Tugas>> = dao.semuaTugas()
    fun observe(id: Int): Flow<Tugas?> = dao.observe(id)
    suspend fun ambil(id: Int): Tugas? = dao.ambil(id)
    suspend fun tambah(t: Tugas) = dao.tambah(t)
    suspend fun ubah(t: Tugas) = dao.ubah(t)
    suspend fun hapus(t: Tugas) = dao.hapus(t)
}