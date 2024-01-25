package com.ugsm.secretpresent.service

import com.ugsm.secretpresent.dto.CreateGiftListDto
import com.ugsm.secretpresent.dto.GiftListCategoryWithSelectedProducts
import com.ugsm.secretpresent.dto.GiftListDetailDto
import com.ugsm.secretpresent.dto.ProductInfo
import com.ugsm.secretpresent.model.gift.GiftList
import com.ugsm.secretpresent.model.gift.GiftListCategory
import com.ugsm.secretpresent.model.gift.GiftListCategoryProduct
import com.ugsm.secretpresent.repository.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

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
    val giftListRepositorySupport: GiftListRepositorySupport
) {


    fun create(takerId: Long, giftListDto: CreateGiftListDto) {
        val anniversary = userAnniversaryRepository.findById(giftListDto.anniversaryId).get()
        val user = userRepository.findById(takerId).get()

        val giftList = GiftList(
            taker = user,
            userAnniversary = anniversary,
            expiredAt = giftListDto.expiredAt,
            packageImgName = giftListDto.packageImgName,
        )

        val giftListCategories = giftListDto.categoriesWithProducts.map {
            val shoppingCategory = naverShoppingCategoryRepository.findById(it.categoryId).get()
            val giftListCategory = GiftListCategory(
                shoppingCategory = shoppingCategory,
                receiptType = it.receiptType,
                giftList = giftList
            )
            val categoryProducts = it.productIds.map { productId ->
                val product = productRepository.findById(productId).get()
                GiftListCategoryProduct(
                    product = product,
                    giftListCategory = giftListCategory
                )
            }

            giftListCategory.products = categoryProducts

            giftListCategory
        }

        giftList.categories = giftListCategories


        giftListRepository.save(giftList)
    }

    fun getUserGiftList(userId: Long) = giftListRepositorySupport.getAllByUserIdNotExpired(userId)
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
                    products = it.products.map { categoryProduct ->
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
}
