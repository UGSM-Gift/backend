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
        val dibs = userDibsProductRepository.findByUserIdAndProductId(userId, productId) ?: throw EntityNotFoundException("User has not had dibs on this product yet.")
        if (dibs.user.id != userId) throw UnauthorizedException()
        userDibsProductRepository.delete(dibs)
    }

    fun createDibs(userId: Long, productId: Long) {
        if(userDibsProductRepository.findByUserIdAndProductId(userId, productId) != null){
            throw BadRequestException(message = "User has already had dibs on this product")
        }

        val user = userRepository.findById(userId).get()
        val product = productRepository.findById(productId).get()
        userDibsProductRepository.save(
            UserDibsProduct(user = user, product = product)
        )
    }

    fun getAllDibsProduct(userId:Long): List<Product> {
        val dibs = userDibsProductRepository.findByUserId(userId)
        return dibs.map { it.product }
    }
}

