package ro.alingrosu.stockmanagement.domain.usecase

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import ro.alingrosu.stockmanagement.domain.model.Supplier
import ro.alingrosu.stockmanagement.domain.repository.SupplierRepository
import javax.inject.Inject

interface SupplierUseCase {
    fun addSupplier(supplier: Supplier): Completable
    fun updateSupplier(supplier: Supplier): Completable
    fun deleteSupplierBySupplierId(id: Int): Completable
    fun getAllSuppliers(): Single<List<Supplier>>
    fun getSupplierById(id: Int): Maybe<Supplier>
    fun searchSuppliers(query: String): Single<List<Supplier>>
}

class SupplierUseCaseImpl(
    private val supplierRepository: SupplierRepository
) : SupplierUseCase {
    override fun addSupplier(supplier: Supplier) = supplierRepository.addSupplier(supplier)
    override fun updateSupplier(supplier: Supplier) = supplierRepository.updateSupplier(supplier)
    override fun deleteSupplierBySupplierId(id: Int) = supplierRepository.deleteSupplierBySupplierId(id)
    override fun getAllSuppliers() = supplierRepository.getAllSuppliers()
    override fun getSupplierById(id: Int) = supplierRepository.getSupplierById(id)
    override fun searchSuppliers(query: String) = supplierRepository.searchSuppliers(query)
}