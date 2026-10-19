package com.example.praktikum1_2211002.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import com.example.praktikum1_2211002.data.Tugas
import com.example.praktikum1_2211002.data.daftarPrioritas
import com.example.praktikum1_2211002.data.daftarStatus

data class TugasUiState(
    val daftar: List<Tugas> = emptyList(),
    val filterStatus: String? = null
)

@Composable
fun PilihanChip(opsi: List<String>, terpilih: String, onPilih: (String) -> Unit) {
    Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        opsi.forEach { o ->
            FilterChip(selected = terpilih == o, onClick = { onPilih(o) }, label = { Text(o) })
        }
    }
}

// ================= LAYAR 1: DAFTAR =================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaftarScreen(
    state: TugasUiState,
    onCari: (String) -> Unit,
    onFilter: (String?) -> Unit,
    onTambah: () -> Unit,
    onDetail: (Int) -> Unit
) {
    var cari by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Daftar Tugas") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onTambah) {
                Icon(Icons.Default.Add, contentDescription = "Tambah tugas")
            }
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(horizontal = 16.dp)) {
            OutlinedTextField(
                value = cari,
                onValueChange = { cari = it; onCari(it) },
                label = { Text("Cari judul / mata kuliah") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                Modifier.horizontalScroll(rememberScrollState()).padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(selected = state.filterStatus == null, onClick = { onFilter(null) }, label = { Text("Semua") })
                daftarStatus.forEach { s ->
                    FilterChip(selected = state.filterStatus == s, onClick = { onFilter(s) }, label = { Text(s) })
                }
            }
            if (state.daftar.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Belum ada tugas") }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(state.daftar, key = { it.id }) { t ->
                        Card(Modifier.fillMaxWidth().clickable { onDetail(t.id) }) {
                            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(t.judul, style = MaterialTheme.typography.titleMedium)
                                Text(t.mataKuliah)
                                Text("Deadline: ${t.deadline}")
                                Text("Prioritas: ${t.prioritas}  |  Status: ${t.status}")
                            }
                        }
                    }
                }
            }
        }
    }
}

// ================= LAYAR 2 & 4: FORM TAMBAH / EDIT =================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    judulHalaman: String,
    tugasAwal: Tugas?,
    onSimpan: (Tugas) -> Unit,
    onKembali: () -> Unit
) {
    // STATE input form
    var mataKuliah by rememberSaveable { mutableStateOf(tugasAwal?.mataKuliah ?: "") }
    var judul by rememberSaveable { mutableStateOf(tugasAwal?.judul ?: "") }
    var deskripsi by rememberSaveable { mutableStateOf(tugasAwal?.deskripsi ?: "") }
    var deadline by rememberSaveable { mutableStateOf(tugasAwal?.deadline ?: "") }
    var prioritas by rememberSaveable { mutableStateOf(tugasAwal?.prioritas ?: daftarPrioritas[1]) }
    var status by rememberSaveable { mutableStateOf(tugasAwal?.status ?: daftarStatus[0]) }

    val deadlineValid = Regex("""\d{4}-\d{2}-\d{2}""").matches(deadline)
    val formValid = mataKuliah.isNotBlank() && judul.isNotBlank() && deadlineValid

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(judulHalaman) },
                navigationIcon = {
                    IconButton(onClick = onKembali) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier.padding(padding).padding(16.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(mataKuliah, { mataKuliah = it }, label = { Text("Mata Kuliah") },
                singleLine = true, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(judul, { judul = it }, label = { Text("Judul Tugas") },
                singleLine = true, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(deskripsi, { deskripsi = it }, label = { Text("Deskripsi") },
                minLines = 3, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(
                value = deadline, onValueChange = { deadline = it },
                label = { Text("Deadline (yyyy-MM-dd)") },
                isError = deadline.isNotEmpty() && !deadlineValid,
                supportingText = { if (deadline.isNotEmpty() && !deadlineValid) Text("Format: 2026-12-31") },
                singleLine = true, modifier = Modifier.fillMaxWidth()
            )
            Text("Prioritas")
            PilihanChip(daftarPrioritas, prioritas) { prioritas = it }
            Text("Status")
            PilihanChip(daftarStatus, status) { status = it }

            Button(
                onClick = {
                    onSimpan(
                        Tugas(
                            id = tugasAwal?.id ?: 0,
                            mataKuliah = mataKuliah.trim(), judul = judul.trim(),
                            deskripsi = deskripsi.trim(), deadline = deadline,
                            prioritas = prioritas, status = status
                        )
                    )
                },
                enabled = formValid,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Simpan") }
        }
    }
}

// ================= LAYAR 3: DETAIL =================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    tugas: Tugas?,
    onUbahStatus: (String) -> Unit,
    onEdit: () -> Unit,
    onHapus: () -> Unit,
    onKembali: () -> Unit
) {
    var tampilDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Tugas") },
                navigationIcon = {
                    IconButton(onClick = onKembali) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { padding ->
        if (tugas == null) {
            Box(Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Tugas tidak ditemukan")
            }
        } else {
            Column(
                Modifier.padding(padding).padding(16.dp).verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(tugas.judul, style = MaterialTheme.typography.headlineSmall)
                Text("Mata kuliah : ${tugas.mataKuliah}")
                Text("Deadline    : ${tugas.deadline}")
                Text("Prioritas   : ${tugas.prioritas}")
                Text("Deskripsi   : ${tugas.deskripsi.ifBlank { "-" }}")
                Spacer(Modifier.height(8.dp))
                Text("Ubah status", style = MaterialTheme.typography.titleSmall)
                PilihanChip(daftarStatus, tugas.status, onUbahStatus)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = onEdit) { Text("Edit") }
                    OutlinedButton(onClick = { tampilDialog = true }) { Text("Hapus") }
                }
            }
        }
    }

    if (tampilDialog) {
        AlertDialog(
            onDismissRequest = { tampilDialog = false },
            title = { Text("Hapus tugas?") },
            text = { Text("Tugas yang dihapus tidak dapat dikembalikan.") },
            confirmButton = { TextButton(onClick = { tampilDialog = false; onHapus() }) { Text("Hapus") } },
            dismissButton = { TextButton(onClick = { tampilDialog = false }) { Text("Batal") } }
        )
    }
}