package ro.alingrosu.stockmanagement.presentation.mapper

import ro.alingrosu.stockmanagement.domain.model.Product
import ro.alingrosu.stockmanagement.presentation.model.ProductUi

fun Product.toUiModel(): ProductUi =
    ProductUi(id, name, description, price, category, barcode, supplier.toUiModel(), currentStock, minStock)

fun List<Product>.toUiModel(): List<ProductUi> = map { it.toUiModel() }

fun ProductUi.toDomainModel(): Product =
    Product(id, name, description, price, category, barcode, supplier.toDomainModel(), currentStock, minStock)
