package com.ugsm.secretpresent.service

import com.ugsm.secretpresent.dto.GiftListProductCategoryDto
import com.ugsm.secretpresent.dto.GiftListProductDto
import com.ugsm.secretpresent.dto.GiftProductCategoryNotReceivedDto
import com.ugsm.secretpresent.dto.giftlist.*
import com.ugsm.secretpresent.enums.GiftCategoryReceiptType
import com.ugsm.secretpresent.model.gift.GiftList
import com.ugsm.secretpresent.model.gift.GiftListProduct
import com.ugsm.secretpresent.model.gift.GiftListProductCategory
import com.ugsm.secretpresent.repository.*
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
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
    val naverShoppingCategoryRepository: NaverShoppingCategoryRepository,
    @Autowired
    val giftListRepositorySupport: GiftListRepositorySupport,
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

        val availableAt = giftListDto.availableAt.atStartOfDay()
        val expiredAt = giftListDto.expiredAt.atTime(LocalTime.MAX)

        val giftList = GiftList(
            taker = user,
            userAnniversary = anniversary,
            availableAt = availableAt,
            expiredAt = expiredAt,
            imgName = giftListDto.imgName,
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

    fun getUserGiftList(userId: Long, page: Int): List<GiftListDto> {
        val numInPage = 10
        val pageRequest = PageRequest.of(page, numInPage)
        return giftListRepositorySupport.getAllByUserIdNotExpired(userId, pageRequest)
    }

    fun get(giftListId: Int): GiftListDetailDto {
        val giftList = giftListRepository.findById(giftListId).get()
        val anniversary = giftList.userAnniversary
        return GiftListDetailDto(
            takerNickname = anniversary.user.nickname,
            anniversaryTitle = anniversary.name,
            createdAt = giftList.createdAt,
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

    fun getAllGiftsNotReceived(giftListId: Int): GiftProductCategoryNotReceivedDto {
        val giftListProductCategories = giftListProductCategoryRepository.findByGiftListId(giftListId).map {
            val products =
                giftListProductRepositorySupport.getByGiftListIdAndProductCategoryIdNotGiven(giftListId, it.shoppingCategory.id)
                    .map { et ->
                        GiftListProductDto(et.id, et.product.id, et.product.name, et.product.price)
                    }
            GiftListProductCategoryDto(
                it.id!!,
                it.shoppingCategory.id,
                it.shoppingCategory.name,
                it.receiptType,
                products
            )
        }.filter { it.products.isNotEmpty() }

        val categoriesHavingMultipleGifts =
            giftListProductCategories.filter { it.receiptType == GiftCategoryReceiptType.MULTIPLE }
        val categoriesHavingSingleGifts =
            giftListProductCategories.filter { it.receiptType == GiftCategoryReceiptType.SINGLE }

        return GiftProductCategoryNotReceivedDto(
            categoriesHavingMultipleGifts,
            categoriesHavingSingleGifts
        )
    }
}
