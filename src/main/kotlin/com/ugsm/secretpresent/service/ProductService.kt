package com.ugsm.secretpresent.service
import com.ugsm.secretpresent.Exception.BadRequestException
import com.ugsm.secretpresent.Exception.UnauthorizedException
import com.ugsm.secretpresent.dto.ProductDto
import com.ugsm.secretpresent.dto.RecommendedProductByAgeDto
import com.ugsm.secretpresent.dto.RecommendedProductDto
import com.ugsm.secretpresent.dto.user.UserInfo
import com.ugsm.secretpresent.enums.ProductCategoryCodeByAge
import com.ugsm.secretpresent.model.UserDibsProduct
import com.ugsm.secretpresent.repository.ProductRepository
import com.ugsm.secretpresent.repository.UserDibsProductRepository
import com.ugsm.secretpresent.repository.UserRepository
import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class ProductService(
    @Autowired
    var repository: ProductRepository,
    @Autowired
    var userDibsProductRepository: UserDibsProductRepository,
    @Autowired
    val userRepository: UserRepository,
    @Autowired
    val productRepository: ProductRepository
) {
    @Transactional
    fun getListByCategoryId(id: Int, page: Int, numInPage: Int, priceBelow: Int?): List<ProductDto> {
        val pageRequest = PageRequest.of(page - 1, numInPage)
        val result = if(priceBelow == null) {
            repository.findSliceByProductCategoriesShoppingCategoryIdOrderByTimestampDesc(id,pageRequest)
        } else {
            repository.findSliceByProductCategoriesShoppingCategoryIdAndPriceLessThanOrderByTimestampDesc(id, pageRequest, priceBelow)
        }

        return result.toList().map{
            ProductDto(
                it.id,
                it.name,
                id,
                it.price,
                it.thumbnailImgUrl,
                it.brandName,
                it.buyingUrl,
                it.freeShipping,
                it.isSoldOut
            )
        }
    }

    @Transactional
    fun deleteDibs(userId: Long, productId: Long) {
        val dibs = userDibsProductRepository.findByUserIdAndProductId(userId, productId) ?: throw EntityNotFoundException("찜한 이력이 없습니다.")
        if (dibs.user.id != userId) throw UnauthorizedException()
        userDibsProductRepository.delete(dibs)
    }

    @Transactional
    fun createDibs(userId: Long, productId: Long) {
        if(userDibsProductRepository.findByUserIdAndProductId(userId, productId) != null){
            throw BadRequestException(101, message = "이미 찜한 상품입니다.")
        }

        val user = userRepository.findById(userId).get()
        val product = productRepository.findById(productId).get()
        userDibsProductRepository.save(
            UserDibsProduct(user = user, product = product)
        )
    }

    @Transactional
    fun getAllDibsProduct(userId: Long, orderBy: String?): List<ProductDto> {
        val dibs = when(orderBy) {
            "HIGHEST_PRICE" -> userDibsProductRepository.findByUserIdOrderByProductPriceDesc(userId)
            "LOWEST_PRICE" -> userDibsProductRepository.findByUserIdOrderByProductPriceAsc(userId)
            else -> userDibsProductRepository.findByUserIdOrderByCreatedAtDesc(userId)
        }
        return dibs.map { ProductDto(
            it.product.id,
            it.product.name,
            null,
            it.product.price,
            it.product.thumbnailImgUrl,
            it.product.brandName,
            it.product.buyingUrl,
            it.product.freeShipping,
            it.product.isSoldOut
        ) }
    }

    fun getUserRecommendedProducts(userInfo: UserInfo): RecommendedProductByAgeDto {
        val age = userInfo.getAge()
        val categories = when(age){
            10 -> listOf(ProductCategoryCodeByAge.TEENAGE_MALE, ProductCategoryCodeByAge.TEENAGE_FEMALE)
            20, 30 -> listOf(ProductCategoryCodeByAge.TWEN_TO_THI_MALE, ProductCategoryCodeByAge.TWEN_TO_THI_FEMALE)
            else -> listOf(ProductCategoryCodeByAge.FOUR_TO_FIFTH_MALE, ProductCategoryCodeByAge.FOUR_TO_FIFTH_FEMALE)
        }.map{it.code}
        val pageRequest = PageRequest.of(0, 20)
        val productsPriceLowerThan10K = productRepository.findSliceByProductCategoriesShoppingCategoryIdInAndPriceBetweenOrderByTimestampDesc(
            categories,pageRequest,0,9999
        )
        val productsPriceIn10KTo30K = productRepository.findSliceByProductCategoriesShoppingCategoryIdInAndPriceBetweenOrderByTimestampDesc(
            categories,pageRequest,10000,29999
        )
        val productsPriceIn30KTo50K = productRepository.findSliceByProductCategoriesShoppingCategoryIdInAndPriceBetweenOrderByTimestampDesc(
            categories,pageRequest,30000,49999
        )
        val productsPriceLargerThan50K = productRepository.findSliceByProductCategoriesShoppingCategoryIdInAndPriceBetweenOrderByTimestampDesc(
            categories,pageRequest,50000,1000000000
        )

        val productIds = (productsPriceLowerThan10K + productsPriceIn10KTo30K + productsPriceIn30KTo50K + productsPriceLargerThan50K)
            .map{it.id}
        val dibbs = userDibsProductRepository.findByUserIdAndProductIdIn(userInfo.id, productIds)

        val productsPriceLowerThan10KDto = productsPriceLowerThan10K.toList().map{product->
            val dibbed = !dibbs.none { it.product.id == product.id }
            RecommendedProductDto(product.id, product.name, product.thumbnailImgUrl, product.buyingUrl, product.price, dibbed)
        }

        val productsPriceIn10KTo30KDto = productsPriceIn10KTo30K.toList().map{product->
            val dibbed = !dibbs.none { it.product.id == product.id }
            RecommendedProductDto(product.id, product.name, product.thumbnailImgUrl, product.buyingUrl, product.price, dibbed)
        }

        val productsPriceIn30KTo50KDto = productsPriceIn30KTo50K.toList().map{product->
            val dibbed = !dibbs.none { it.product.id == product.id }
            RecommendedProductDto(product.id, product.name, product.thumbnailImgUrl, product.buyingUrl, product.price, dibbed)
        }

        val productsPriceLargerThan50KDto = productsPriceLargerThan50K.toList().map{product->
            val dibbed = !dibbs.none { it.product.id == product.id }
            RecommendedProductDto(product.id, product.name, product.thumbnailImgUrl, product.buyingUrl, product.price, dibbed)
        }

        return RecommendedProductByAgeDto(
            productsPriceLowerThan10KDto,
            productsPriceIn10KTo30KDto,
            productsPriceIn30KTo50KDto,
            productsPriceLargerThan50KDto
        )
    }
}

