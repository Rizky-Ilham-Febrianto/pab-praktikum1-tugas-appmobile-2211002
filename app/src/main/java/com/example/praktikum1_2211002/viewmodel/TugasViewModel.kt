package com.example.praktikum1_2211002.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.praktikum1_2211002.data.AppDatabase
import com.example.praktikum1_2211002.data.Tugas
import com.example.praktikum1_2211002.data.TugasRepository
import com.example.praktikum1_2211002.ui.TugasUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TugasViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TugasRepository(AppDatabase.getInstance(application).tugasDao())

    private val filterStatus = MutableStateFlow<String?>(null)
    private val pencarian = MutableStateFlow("")

    // UI STATE: gabungan data Room + filter + pencarian
    val uiState: StateFlow<TugasUiState> =
        combine(repository.semuaTugas, filterStatus, pencarian) { semua, status, cari ->
            TugasUiState(
                daftar = semua.filter { t ->
                    (status == null || t.status == status) &&
                            (cari.isBlank() ||
                                    t.judul.contains(cari, ignoreCase = true) ||
                                    t.mataKuliah.contains(cari, ignoreCase = true))
                },
                filterStatus = status
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TugasUiState())

    // EVENT dari UI
    fun setFilter(status: String?) { filterStatus.value = status }
    fun setPencarian(kata: String) { pencarian.value = kata }

    fun observe(id: Int) = repository.observe(id)
    suspend fun ambil(id: Int) = repository.ambil(id)

    fun tambah(t: Tugas) = viewModelScope.launch { repository.tambah(t) }
    fun ubah(t: Tugas) = viewModelScope.launch { repository.ubah(t) }
    fun hapus(t: Tugas) = viewModelScope.launch { repository.hapus(t) }
}