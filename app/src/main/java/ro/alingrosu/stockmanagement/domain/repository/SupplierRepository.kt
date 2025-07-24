package ro.alingrosu.stockmanagement.domain.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import ro.alingrosu.stockmanagement.domain.model.Supplier

interface SupplierRepository {
    fun addSupplier(supplier: Supplier): Completable
    fun updateSupplier(supplier: Supplier): Completable
    fun deleteSupplierBySupplierId(supplierId: Int): Completable
    fun searchSuppliers(query: String): Single<List<Supplier>>
    fun getAllSuppliers(): Single<List<Supplier>>
    fun getSupplierById(id: Int): Maybe<Supplier>
}