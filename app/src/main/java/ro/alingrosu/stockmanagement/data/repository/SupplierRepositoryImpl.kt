package ro.alingrosu.stockmanagement.data.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import ro.alingrosu.stockmanagement.data.local.dao.SupplierDao
import ro.alingrosu.stockmanagement.data.mapper.toDomain
import ro.alingrosu.stockmanagement.data.mapper.toEntity
import ro.alingrosu.stockmanagement.domain.model.Supplier
import ro.alingrosu.stockmanagement.domain.repository.SupplierRepository
import javax.inject.Inject

class SupplierRepositoryImpl(private val dao: SupplierDao) : SupplierRepository {

    override fun addSupplier(supplier: Supplier): Completable =
        dao.insertSupplier(supplier.toEntity())

    override fun updateSupplier(supplier: Supplier): Completable =
        dao.updateSupplier(supplier.toEntity())

    override fun deleteSupplierBySupplierId(supplierId: Int): Completable =
        dao.deleteSupplierById(supplierId)

    override fun searchSuppliers(query: String): Single<List<Supplier>> =
        dao.searchSuppliers(query).map { list -> list.map { it.toDomain() } }

    override fun getAllSuppliers(): Single<List<Supplier>> =
        dao.getAllSuppliers().map { list -> list.map { it.toDomain() } }

    override fun getSupplierById(id: Int): Maybe<Supplier> =
        dao.getSupplierById(id).map { it.toDomain() }
}