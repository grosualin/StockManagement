package ro.alingrosu.stockmanagement.data.mapper

import ro.alingrosu.stockmanagement.data.local.entity.SupplierEntity
import ro.alingrosu.stockmanagement.data.remote.dto.SupplierDto
import ro.alingrosu.stockmanagement.domain.model.Supplier

fun SupplierEntity.toDomain(): Supplier = Supplier(
    id, name, contactPerson, phone, email, address
)

fun SupplierEntity.toDto(): SupplierDto = SupplierDto(
    id, name, contactPerson, phone, email, address
)

fun SupplierDto.toDomain(): Supplier = Supplier(
    id, name, contactPerson, phone, email, address
)

fun SupplierDto.toEntity(): SupplierEntity = SupplierEntity(
    id, name, contactPerson, phone, email, address
)

fun Supplier.toDto(): SupplierDto = SupplierDto(
    id, name, contactPerson, phone, email, address
)

fun Supplier.toEntity(): SupplierEntity = SupplierEntity(
    id, name, contactPerson, phone, email, address
)