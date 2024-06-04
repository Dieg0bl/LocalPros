
package com.example.localpros.data.repository

import com.example.localpros.data.model.enums.Categoria
import com.google.firebase.database.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.tasks.await

@Singleton
class RepositorioCategorias @Inject constructor(
    db: FirebaseDatabase
) {
    private val refCategorias: DatabaseReference = db.getReference("Categorias")

    suspend fun obtenerCategorias(): List<Categoria> {
        val snapshot = refCategorias.get().await()
        return snapshot.children.mapNotNull { it.getValue(Categoria::class.java) }
    }

    suspend fun agregarCategoria(categoria: Categoria) {
        refCategorias.child(categoria.id).setValue(categoria).await()
    }
}
