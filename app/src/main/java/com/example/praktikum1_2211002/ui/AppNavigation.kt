package com.example.praktikum1_2211002.ui

import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.praktikum1_2211002.data.Tugas
import com.example.praktikum1_2211002.viewmodel.TugasViewModel

@Composable
fun AppNavigation(vm: TugasViewModel = viewModel()) {
    val nav = rememberNavController()
    val state by vm.uiState.collectAsStateWithLifecycle()
    val argId = listOf(navArgument("id") { type = NavType.IntType })

    NavHost(navController = nav, startDestination = "list") {

        composable("list") {
            DaftarScreen(
                state = state,
                onCari = vm::setPencarian,
                onFilter = vm::setFilter,
                onTambah = { nav.navigate("add") },
                onDetail = { id -> nav.navigate("detail/$id") }
            )
        }

        composable("add") {
            FormScreen(
                judulHalaman = "Tambah Tugas",
                tugasAwal = null,
                onSimpan = { baru -> vm.tambah(baru); nav.popBackStack() },
                onKembali = { nav.popBackStack() }
            )
        }

        composable("detail/{id}", arguments = argId) { entry ->
            val id = entry.arguments!!.getInt("id")
            val alur = remember(id) { vm.observe(id) }
            val tugas by alur.collectAsStateWithLifecycle(initialValue = null)
            DetailScreen(
                tugas = tugas,
                onUbahStatus = { s -> tugas?.let { vm.ubah(it.copy(status = s)) } },
                onEdit = { nav.navigate("edit/$id") },
                onHapus = { tugas?.let { vm.hapus(it) }; nav.popBackStack() },
                onKembali = { nav.popBackStack() }
            )
        }

        composable("edit/{id}", arguments = argId) { entry ->
            val id = entry.arguments!!.getInt("id")
            var tugas by remember { mutableStateOf<Tugas?>(null) }
            LaunchedEffect(id) { tugas = vm.ambil(id) }
            tugas?.let { awal ->
                FormScreen(
                    judulHalaman = "Edit Tugas",
                    tugasAwal = awal,
                    onSimpan = { hasil -> vm.ubah(hasil); nav.popBackStack() },
                    onKembali = { nav.popBackStack() }
                )
            }
        }
    }
}