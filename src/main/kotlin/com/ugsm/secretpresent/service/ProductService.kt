package com.ugsm.secretpresent.service
import com.ugsm.secretpresent.model.product.Product
import com.ugsm.secretpresent.repository.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Slice
import org.springframework.stereotype.Service

@Service
class ProductService(
    @Autowired
    var repository: ProductRepository
) {
    fun getListByCategoryId(id: Int, page: Int, numInPage: Int): Slice<Product> {
        val pageRequest = PageRequest.of(page, numInPage)
        return repository.findSliceByProductCategoriesShoppingCategoryId(id, pageRequest)
    }
}

