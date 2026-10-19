# Praktikum 1 - Rancangan Aplikasi Manajemen Tugas Mahasiswa - Rizky Ilham Febrianto

## Kebutuhan
Mahasiswa mencatat, memantau, dan memperbarui status tugas tanpa internet.

## User Flow
- Daftar Tugas → Tambah Tugas → Simpan → Daftar Tugas
- Daftar Tugas → Detail Tugas → Edit / Hapus → Daftar Tugas

## Layar
1. Daftar Tugas (search + filter status)
2. Form Tambah Tugas
3. Detail Tugas (ubah status, edit, hapus)
4. Form Edit Tugas

## Model Data (tabel `tugas`)
id, mataKuliah, judul, deskripsi, deadline (yyyy-MM-dd), prioritas, status

## Arsitektur
Compose UI → ViewModel → Repository → Room DAO → Database