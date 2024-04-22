package com.ugsm.secretpresent.service

import aws.smithy.kotlin.runtime.util.length
import com.ugsm.secretpresent.Exception.CustomException
import com.ugsm.secretpresent.Exception.UnauthorizedException
import com.ugsm.secretpresent.dto.*
import com.ugsm.secretpresent.dto.giftlist.*
import com.ugsm.secretpresent.enums.GiftCategoryReceiptType
import com.ugsm.secretpresent.enums.GiftConfirmedStatus
import com.ugsm.secretpresent.enums.S3ImageUploadType
import com.ugsm.secretpresent.model.gift.GiftList
import com.ugsm.secretpresent.model.gift.GiftListProduct
import com.ugsm.secretpresent.model.gift.GiftListProductCategory
import com.ugsm.secretpresent.repository.*
import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.LocalTime

@Service
class GiftListService(
    @Autowired
    var giftListRepository: GiftListRepository,
    @Autowired
    var userAnniversaryRepository: UserAnniversaryRepository,
    @Autowired
    var userRepository: UserRepository,
    @Autowired
    var productRepository: ProductRepository,
    @Autowired
    var productCategoryRepository: NaverShoppingCategoryRepository,
    @Autowired
    val naverShoppingCategoryRepository: NaverShoppingCategoryRepository,
    @Autowired
    val giftListLetterRepository: GiftListLetterRepository,
    @Autowired
    val giftListProductRepository: GiftListProductRepository,
    @Autowired
    val giftListProductRepositorySupport: GiftListProductRepositorySupport,
    @Autowired
    val giftListProductCategoryRepository: GiftListProductCategoryRepository
) {


    @Transactional
    fun create(takerId: Long, giftListDto: CreateGiftListDto): Int? {
        val anniversary = userAnniversaryRepository.findById(giftListDto.anniversaryId).get()
        val user = userRepository.findById(takerId).get()

        val availableAt = giftListDto.availableAt.atTime(LocalTime.of(0, 0, 0))
        val expiredAt = giftListDto.expiredAt.atTime(LocalTime.of(23, 59, 59))

        val uploadedImageUrl = getUploadedImageUrl(takerId, giftListDto.imageFileName)

        val giftList = GiftList(
            taker = user,
            userAnniversary = anniversary,
            availableAt = availableAt,
            expiredAt = expiredAt,
            imageUrl = uploadedImageUrl,
        )
        giftListRepository.save(giftList)

        val giftListCategories = giftListDto.categoriesWithProducts.map {
            val shoppingCategory = naverShoppingCategoryRepository.findById(it.categoryId).get()
            val giftListProductCategory = GiftListProductCategory(
                shoppingCategory = shoppingCategory,
                receiptType = it.receiptType,
                giftList = giftList
            )
            val giftListProducts = it.productIds.map { productId ->
                val product = productRepository.findById(productId).get()
                GiftListProduct(
                    product = product,
                    giftList = giftList,
                    productCategory = shoppingCategory,
                )
            }
            giftListProductRepository.saveAll(giftListProducts)

            giftListProductCategory
        }

        giftListProductCategoryRepository.saveAll(giftListCategories)

        return giftList.id
    }

    @Transactional
    fun getUserGiftList(userId: Long, page: Int): List<GiftListDto> {
        val numInPage = 10
        val pageRequest = PageRequest.of(page - 1, numInPage)
        val now = LocalDateTime.now()
        val giftList = giftListRepository.findSliceByTakerIdAndAvailableAtLessThanAndExpiredAtGreaterThan(
            userId,
            pageRequest,
            now,
            now
        )
        return giftList.map {
            val selectedProducts = it.giftListProducts
            val receivedProducts =
                giftListLetterRepository.findByGiftListIdAndConfirmedStatusNot(it.id, GiftConfirmedStatus.DENIED)
            GiftListDto(
                it.id,
                it.createdAt,
                it.availableAt,
                it.expiredAt,
                it.userAnniversary.image.imageUrl,
                it.userAnniversary.name,
                selectedProducts.count(),
                receivedProducts.count()
            )
        }

    }

    @Transactional
    fun get(giftListId: Int): GiftListDetailDto {
        val giftList = giftListRepository.findById(giftListId).get()
        val anniversary = giftList.userAnniversary
        return GiftListDetailDto(
            takerNickname = anniversary.user.nickname,
            anniversaryTitle = anniversary.name,
            createdAt = giftList.createdAt,
            availableAt = giftList.availableAt,
            expiredAt = giftList.expiredAt
        ).apply {
            categories = giftList.categories.map {
                GiftListCategoryWithSelectedProducts(
                    id = it.shoppingCategory.id,
                    name = it.shoppingCategory.name,
                    receiptType = it.receiptType,
                    products = giftListProductRepository.findByGiftListIdAndProductCategoryId(
                        giftListId,
                        it.shoppingCategory.id
                    ).map { categoryProduct ->
                        ProductInfo(
                            id = categoryProduct.product.id,
                            name = categoryProduct.product.name,
                            price = categoryProduct.product.price
                        )
                    }
                )
            }
        }
    }

    @Transactional
    fun getInfoWithAllGiftsNotReceived(giftListId: Int): GiftListInfoDto {
        val giftListProductCategories = giftListProductCategoryRepository.findByGiftListId(giftListId).filter {
            if (it.receiptType == null || it.receiptType == GiftCategoryReceiptType.ALL) {
                true
            } else {
                val notDeniedReceivedLetters =
                    giftListLetterRepository.findByGiftListProductCategoryIdAndConfirmedStatusNot(
                        it.id!!,
                        GiftConfirmedStatus.DENIED
                    )
                notDeniedReceivedLetters.isEmpty()
            }
        }
        val giftListProductCategoriesDto = giftListProductCategories.map {
            val products =
                giftListProductRepositorySupport.getByGiftListIdAndProductCategoryIdNotGiven(
                    giftListId,
                    it.shoppingCategory.id
                ).map { et -> GiftListProductDto(et.product.id, et.product.name, et.product.price, false) }
            GiftListProductCategoryDto(
                it.shoppingCategory.id,
                it.shoppingCategory.name,
                it.receiptType,
                products
            )
        }.filter { it.products.isNotEmpty() }
        val giftList = giftListRepository.findById(giftListId).get()
        val categoriesHavingMultipleGifts = giftListProductCategoriesDto.filter { it.receiptType != null }

        val categoriesHavingSingleGifts =
            giftListProductCategoriesDto.filter { it.receiptType == null }

        val selectedProducts = giftList.giftListProducts
        val receivedProducts = giftListLetterRepository.findByGiftListIdAndConfirmedStatusNot(giftListId, GiftConfirmedStatus.DENIED)


        return GiftListInfoDto(
            giftListId,
            giftList.taker.id!!,
            giftList.taker.nickname,
            giftList.imageUrl,
            giftList.userAnniversary.name,
            giftList.userAnniversary.image.imageUrl,
            giftList.availableAt,
            giftList.expiredAt,
            categoriesHavingMultipleGifts,
            categoriesHavingSingleGifts,
            selectedProducts.count(),
            receivedProducts.count()
        )
    }

    @Transactional
    fun getInfo(giftListId: Int): GiftListInfoDto {
        val giftListProductCategories = giftListProductCategoryRepository.findByGiftListId(giftListId)
        val giftListProductCategoriesDto = giftListProductCategories.map {
            val products =
                giftListProductRepository.getByGiftListIdAndProductCategoryId(giftListId, it.shoppingCategory.id)
                    .map { et ->
                        val received = giftListLetterRepository.findByGiftListIdAndProductIdAndConfirmedStatusNot(
                            giftListId, et.product.id, GiftConfirmedStatus.DENIED
                        ).isNotEmpty();
                        GiftListProductDto(et.product.id, et.product.name, et.product.price, received)
                    }
            GiftListProductCategoryDto(
                it.shoppingCategory.id,
                it.shoppingCategory.name,
                it.receiptType,
                products
            )
        }.filter { it.products.isNotEmpty() }
        val giftList = giftListRepository.findById(giftListId).get()
        val categoriesHavingMultipleGifts =
            giftListProductCategoriesDto.filter { it.receiptType != null }
        val categoriesHavingSingleGifts =
            giftListProductCategoriesDto.filter { it.receiptType == null }

        val selectedProducts = giftList.giftListProducts
        val receivedProducts = giftListLetterRepository.findByGiftListIdAndConfirmedStatusNot(giftListId, GiftConfirmedStatus.DENIED)

        return GiftListInfoDto(
            giftListId,
            giftList.taker.id!!,
            giftList.taker.nickname,
            giftList.imageUrl,
            giftList.userAnniversary.name,
            giftList.userAnniversary.image.imageUrl,
            giftList.availableAt,
            giftList.expiredAt,
            categoriesHavingMultipleGifts,
            categoriesHavingSingleGifts,
            selectedProducts.count(),
            receivedProducts.count()
        )
    }

    @Transactional
    fun update(userId: Long, giftListId: Int, dto: UpdateGiftListDto) {
        val giftList = giftListRepository.findById(giftListId).get()
        if (giftList.taker.id != userId) throw UnauthorizedException(message = "내 선물리스트가 아닙니다.")

        val newUserAnniversary = if (dto.userAnniversaryId != null) {
            userAnniversaryRepository.findById(dto.userAnniversaryId).get()
        } else null

        val newAvailableAt = dto.availableAt?.atTime(LocalTime.of(0, 0, 0))
        val newExpiredAt = dto.expiredAt?.atTime(LocalTime.of(23, 59, 59))
        val newImageUrl = if (dto.imageFileName != null) {
            getUploadedImageUrl(userId, dto.imageFileName)
        } else null

        giftList.apply {
            userAnniversary = newUserAnniversary ?: this.userAnniversary
            availableAt = newAvailableAt ?: this.availableAt
            expiredAt = newExpiredAt ?: this.expiredAt
            imageUrl = newImageUrl ?: this.imageUrl
        }
    }

    private fun getUploadedImageUrl(takerId: Long, fileName: String): String {
        return "${S3ImageUploadType.GIFT_LIST.getUrl(takerId)}/$fileName"
    }

    fun deleteProduct(userId: Long, giftListId: Int, productCategoryId: Int, productId: Long) {
        val giftList = giftListRepository.findById(giftListId).get()
        if (giftList.taker.id != userId) throw UnauthorizedException(message = "내 선물리스트가 아닙니다.")

        val giftListProduct: GiftListProduct
        try {
            giftListProduct =
                giftListProductRepository.findByGiftListIdAndProductCategoryId(giftListId, productCategoryId)
                    .last { it.product.id == productId }

        } catch (_: NoSuchElementException) {
            throw CustomException(code = 101, message = "해당 선물리스트의 카테고리에 상품이 존재하지 않습니다.")
        }

        val receivedLetters = giftListLetterRepository.findByGiftListIdAndProductIdAndConfirmedStatusNot(
            giftListId,
            productId,
            GiftConfirmedStatus.DENIED
        )
        if (receivedLetters.isNotEmpty()) throw CustomException(code = 102, message = "이미 받은 이력이 있는 상품을 삭제할 수 없습니다.")

        giftListProductRepository.delete(giftListProduct)


    }

    @Transactional
    fun updateProductCategoryReceiptType(
        userId: Long,
        giftListId: Int,
        productCategoryId: Int,
        receiptType: GiftCategoryReceiptType
    ) {
        val giftList = giftListRepository.findById(giftListId).get()
        if (giftList.taker.id != userId) throw UnauthorizedException(message = "내 선물리스트가 아닙니다.")

        val giftListProductCategory =
            giftListProductCategoryRepository.findByGiftListIdAndShoppingCategoryId(giftListId, productCategoryId)
                ?: throw EntityNotFoundException("선물리스트에 해당 카테고리가 존재하지 않습니다.")

        if (giftListProductCategory.receiptType == null) throw CustomException(
            code = 101,
            message = "복수상품을 포함한 카테고리가 아닙니다."
        )

        val giftListLetter = giftListLetterRepository.findByGiftListProductCategoryIdAndConfirmedStatus(
            giftListProductCategory.id!!, GiftConfirmedStatus.DENIED
        )

        if (giftListLetter.isNotEmpty()) throw CustomException(code = 102, message = "이미 선물한 내역이 있는 경우 변경이 불가능합니다.")

        giftListProductCategory.receiptType = receiptType
    }

    fun getFinalGiftListBeforeSubmit(dto: List<FinalGiftListBeforeSubmitRequestDto>): FinalGiftListBeforeSubmitResponseDto {
        val grouped = dto.groupBy { it.categoryId }
        val single = arrayListOf<CategoryWithProductsDto>()
        val multiple = arrayListOf<CategoryWithProductsDto>()
        grouped.forEach {
            val productIds = it.value.map { b -> b.productId }
            val products = productRepository.findAllByIdIn(productIds)
            val category = productCategoryRepository.findById(it.key).get()
            val categoryWithProductDto = CategoryWithProductsDto(
                categoryId = category.id,
                categoryName = category.name,
                products = products.map { p ->
                    ProductDto(
                        p.id,
                        p.name,
                        category.id,
                        p.price,
                        p.thumbnailImgUrl,
                        p.brandName,
                        p.buyingUrl,
                        p.freeShipping,
                        p.isSoldOut
                    )
                }
            )

            if (products.size >= 2) {
                multiple.add(categoryWithProductDto)
            } else {
                single.add(categoryWithProductDto)
            }
        }
        return FinalGiftListBeforeSubmitResponseDto(
            single, multiple
        )
    }
}
