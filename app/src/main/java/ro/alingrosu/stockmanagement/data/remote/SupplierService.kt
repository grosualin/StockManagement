package ro.alingrosu.stockmanagement.data.remote

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ro.alingrosu.stockmanagement.data.remote.dto.SupplierDto

interface SupplierService {
    fun postSupplier(supplier: SupplierDto): Completable
    fun updateSupplier(supplier: SupplierDto): Completable
    fun deleteSupplierById(supplierId: Int): Completable
    fun fetchAllSuppliers(): Single<List<SupplierDto>>
    fun fetchSupplierById(id: Int): Maybe<SupplierDto>
    fun searchSuppliers(query: String): Single<List<SupplierDto>>
}

class SupplierMockServiceImpl : SupplierService {
    private val mockSuppliers = mutableListOf(
        SupplierDto(
            1,
            "Fresh Produce Ltd.",
            "Jane Doe",
            "+50721123456",
            "contact@freshproduce.ro",
            "123 Maple Street, Springfield, IL, USA"
        ),
        SupplierDto(
            2,
            "Tech Supplies Inc.",
            "John Doe",
            "+30722111222",
            "office@techsupplies.com",
            "45 Rue de la Paix, 75002 Paris, France"
        ),
        SupplierDto(
            3,
            "Beverage Wholesale",
            "Jack Dane",
            "+20723333444",
            "sales@beveragewholesale.ro",
            "789 King's Road, Chelsea, London, SW3 5EZ, UK"
        ),
        SupplierDto(
            4,
            "Stationery & More",
            "Jill Dice",
            "+40724444555",
            "contact@stationerymore.ro",
            "56 Sakura Avenue, Shibuya, Tokyo, 150-0002, Japan"
        ),
        SupplierDto(
            5,
            "Organic Goods Co.",
            "Janet Jason",
            "+10725555666",
            "hello@organicgoods.ro",
            "88 Queenstown Blvd, Auckland, 1010, New Zealand"
        )
    )

    override fun postSupplier(supplier: SupplierDto): Completable {
        return Completable.fromAction {
            mockSuppliers.add(supplier)
        }.subscribeOn(Schedulers.io())
    }

    override fun updateSupplier(supplier: SupplierDto): Completable {
        return Completable.fromAction {
            val index = mockSuppliers.indexOfFirst { it.id == supplier.id }
            if (index != -1) {
                mockSuppliers[index] = supplier
            } else {
                throw IllegalArgumentException("Supplier not found with id ${supplier.id}")
            }
        }.subscribeOn(Schedulers.io())
    }

    override fun deleteSupplierById(supplierId: Int): Completable {
        return Completable.fromAction {
            val removed = mockSuppliers.removeIf { it.id == supplierId }
            if (!removed) {
                throw IllegalArgumentException("Supplier not found with id $supplierId")
            }
        }.subscribeOn(Schedulers.io())
    }

    override fun fetchAllSuppliers(): Single<List<SupplierDto>> {
        return Single.just(mockSuppliers.toList())
            .subscribeOn(Schedulers.io())
    }

    override fun fetchSupplierById(id: Int): Maybe<SupplierDto> {
        return Maybe.create { emitter ->
            val supplier = mockSuppliers.find { it.id == id }
            if (supplier != null) {
                emitter.onSuccess(supplier)
            } else {
                emitter.onComplete()
            }
        }.subscribeOn(Schedulers.io())
    }

    override fun searchSuppliers(query: String): Single<List<SupplierDto>> {
        return Single.fromCallable {
            mockSuppliers.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.contactPerson.contains(query, ignoreCase = true) ||
                        it.address.contains(query, ignoreCase = true)
            }
        }.subscribeOn(Schedulers.io())
    }
}

