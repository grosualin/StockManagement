package ro.alingrosu.stockmanagement.presentation.mapper

import ro.alingrosu.stockmanagement.domain.model.Supplier
import ro.alingrosu.stockmanagement.presentation.model.SupplierUi

fun Supplier.toUiModel(): SupplierUi = SupplierUi(id, name, contactPerson, phone, email, address)

fun List<Supplier>.toUiModel(): List<SupplierUi> = this.map { supplier -> supplier.toUiModel() }

fun SupplierUi.toDomainModel(): Supplier = Supplier(id, name, contactPerson, phone, email, address)