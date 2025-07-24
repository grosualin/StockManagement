package ro.alingrosu.stockmanagement.data.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import ro.alingrosu.stockmanagement.data.local.dao.SupplierDao
import ro.alingrosu.stockmanagement.data.mapper.toDomain
import ro.alingrosu.stockmanagement.data.mapper.toDto
import ro.alingrosu.stockmanagement.data.mapper.toEntity
import ro.alingrosu.stockmanagement.data.service.SupplierService
import ro.alingrosu.stockmanagement.domain.model.Supplier
import ro.alingrosu.stockmanagement.domain.repository.SupplierRepository
import javax.inject.Inject

class SupplierRepositoryImpl @Inject constructor(
    private val localDataSource: SupplierDao,
    private val remoteDataSource: SupplierService
) : SupplierRepository {

    override fun addSupplier(supplier: Supplier): Completable {
        return remoteDataSource.postSupplier(supplier.toDto())
            .andThen(localDataSource.insertSupplier(supplier.toEntity()).onErrorComplete())
    }

    override fun updateSupplier(supplier: Supplier): Completable {
        return remoteDataSource.updateSupplier(supplier.toDto())
            .andThen(localDataSource.updateSupplier(supplier.toEntity()).onErrorComplete())
    }

    override fun deleteSupplierBySupplierId(supplierId: Int): Completable {
        return remoteDataSource.deleteSupplierById(supplierId)
            .andThen(localDataSource.deleteSupplierById(supplierId).onErrorComplete())
    }

    override fun searchSuppliers(query: String): Flowable<List<Supplier>> {
        val local = localDataSource.searchSuppliers(query).map { suppliers -> suppliers.map { it.toDomain() } }.toFlowable()
        val remote = remoteDataSource.searchSuppliers(query)
            .map { suppliers -> suppliers.map { it.toDomain() } }
            .toFlowable()
        return Flowable.concatArrayEager(local, remote)
    }

    override fun getAllSuppliers(): Flowable<List<Supplier>> {
        val local = localDataSource.getAllSuppliers().map { suppliers -> suppliers.map { it.toDomain() } }.toFlowable()
        val remote = remoteDataSource.fetchAllSuppliers()
            .doOnSuccess { suppliers -> localDataSource.insertAll(suppliers.map { it.toEntity() }).subscribe() }
            .map { suppliers -> suppliers.map { it.toDomain() } }
            .toFlowable()
        return Flowable.concatArrayEager(local, remote)
    }

    override fun getSupplierById(id: Int): Flowable<Supplier> {
        val local = localDataSource.getSupplierById(id).map { it.toDomain() }.toFlowable()
        val remote = remoteDataSource.fetchSupplierById(id)
            .doOnSuccess { supplier -> localDataSource.updateSupplier(supplier.toEntity()).subscribe() }
            .map { supplier -> supplier.toDomain() }
            .toFlowable()
        return Flowable.concatArrayEager(local, remote)
    }
}