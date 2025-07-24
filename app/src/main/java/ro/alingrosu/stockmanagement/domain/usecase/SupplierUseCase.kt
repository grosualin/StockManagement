package ro.alingrosu.stockmanagement.domain.usecase

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import ro.alingrosu.stockmanagement.domain.model.Supplier
import ro.alingrosu.stockmanagement.domain.repository.SupplierRepository
import javax.inject.Inject

interface SupplierUseCase {
    fun addSupplier(supplier: Supplier): Completable
    fun updateSupplier(supplier: Supplier): Completable
    fun deleteSupplierBySupplierId(id: Int): Completable
    fun getAllSuppliers(): Flowable<List<Supplier>>
    fun getSupplierById(id: Int): Flowable<Supplier>
    fun searchSuppliers(query: String): Flowable<List<Supplier>>
}

class SupplierUseCaseImpl @Inject constructor(
    private val supplierRepository: SupplierRepository
) : SupplierUseCase {
    override fun addSupplier(supplier: Supplier): Completable {
        return supplierRepository.addSupplier(supplier)
    }

    override fun updateSupplier(supplier: Supplier): Completable {
        return supplierRepository.updateSupplier(supplier)
    }

    override fun deleteSupplierBySupplierId(id: Int): Completable {
        return supplierRepository.deleteSupplierBySupplierId(id)
    }

    override fun getAllSuppliers(): Flowable<List<Supplier>> {
        return supplierRepository.getAllSuppliers()
    }

    override fun getSupplierById(id: Int): Flowable<Supplier> {
        return supplierRepository.getSupplierById(id)
    }

    override fun searchSuppliers(query: String): Flowable<List<Supplier>> {
        return supplierRepository.searchSuppliers(query)
    }
}