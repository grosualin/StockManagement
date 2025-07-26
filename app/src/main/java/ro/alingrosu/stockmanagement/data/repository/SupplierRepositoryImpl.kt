package ro.alingrosu.stockmanagement.data.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
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
            .andThen(
                localDataSource.getMaxId()
                    .map { maxId -> maxId + 1 }
                    .flatMapCompletable { nextId ->
                        val supplierWihId = supplier.toEntity().copy(id = nextId)
                        localDataSource.insertSupplier(supplierWihId).onErrorComplete()
                    }
            )
            .subscribeOn(Schedulers.io())
    }

    override fun updateSupplier(supplier: Supplier): Completable {
        return remoteDataSource.updateSupplier(supplier.toDto())
            .andThen(localDataSource.updateSupplier(supplier.toEntity()).onErrorComplete())
            .subscribeOn(Schedulers.io())
    }

    override fun deleteSupplierBySupplierId(supplierId: Int): Completable {
        return remoteDataSource.deleteSupplierById(supplierId)
            .andThen(localDataSource.deleteSupplierById(supplierId).onErrorComplete())
            .subscribeOn(Schedulers.io())
    }

    override fun searchSuppliers(query: String): Flowable<List<Supplier>> {
        val local = localDataSource.searchSuppliers(query)
            .subscribeOn(Schedulers.io())
            .map { suppliers -> suppliers.map { it.toDomain() } }
        val remote = remoteDataSource.searchSuppliers(query)
            .map { suppliers -> suppliers.map { it.toDomain() } }
        return Single.concatArrayEager(local, remote)
            .map { it.sortedBy { supplier -> supplier.name } }
    }

    override fun getAllSuppliers(): Flowable<List<Supplier>> {
        val local = localDataSource.getAllSuppliers()
            .subscribeOn(Schedulers.io())
            .map { suppliers -> suppliers.map { it.toDomain() } }
        val remote = remoteDataSource.fetchAllSuppliers()
            .doOnSuccess { suppliers -> localDataSource.insertAll(suppliers.map { it.toEntity() }).subscribe() }
            .map { suppliers -> suppliers.map { it.toDomain() } }
        return Single.concatArrayEager(local, remote)
            .map { it.sortedBy { supplier -> supplier.id } }
    }

    override fun getSupplierById(id: Int): Flowable<Supplier> {
        val local = localDataSource.getSupplierById(id)
            .subscribeOn(Schedulers.io())
            .map { it.toDomain() }
        val remote = remoteDataSource.fetchSupplierById(id)
            .doOnSuccess { supplier -> localDataSource.updateSupplier(supplier.toEntity()).subscribe() }
            .map { supplier -> supplier.toDomain() }
        return Maybe.concatArrayEager(local, remote)
    }
}