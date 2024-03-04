package com.ugsm.secretpresent.service
import com.ugsm.secretpresent.Exception.BadRequestException
import com.ugsm.secretpresent.Exception.UnauthorizedException
import com.ugsm.secretpresent.model.UserDibsProduct
import com.ugsm.secretpresent.model.product.Product
import com.ugsm.secretpresent.repository.ProductRepository
import com.ugsm.secretpresent.repository.UserDibsProductRepository
import com.ugsm.secretpresent.repository.UserRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Slice
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
    fun getListByCategoryId(id: Int, page: Int, numInPage: Int, priceBelow: Int?): Slice<Product> {
        val pageRequest = PageRequest.of(page - 1, numInPage)
        return if(priceBelow == null) {
            repository.findSliceByProductCategoriesShoppingCategoryId(id,pageRequest)
        } else {
            repository.findSliceByProductCategoriesShoppingCategoryIdAndPriceLessThan(id, pageRequest, priceBelow)
        }
    }

    fun deleteDibs(userId: Long, productId: Long) {
        val dibs = userDibsProductRepository.findByUserIdAndProductId(userId, productId) ?: throw EntityNotFoundException("찜한 이력이 없습니다.")
        if (dibs.user.id != userId) throw UnauthorizedException()
        userDibsProductRepository.delete(dibs)
    }

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

    fun getAllDibsProduct(userId: Long, orderBy: String?): List<Product> {
        val dibs = when(orderBy) {
            "HIGHEST_PRICE" -> userDibsProductRepository.findByUserIdOrderByProductPriceDesc(userId)
            "LOWEST_PRICE" -> userDibsProductRepository.findByUserIdOrderByProductPriceAsc(userId)
            else -> userDibsProductRepository.findByUserIdOrderByCreatedAtDesc(userId)
        }
        return dibs.map { it.product }
    }
}

