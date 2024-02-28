package com.ugsm.secretpresent.controller

import com.ugsm.secretpresent.dto.user.UserInfo
import com.ugsm.secretpresent.model.product.Product
import com.ugsm.secretpresent.response.CustomResponse
import com.ugsm.secretpresent.service.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class ProductController(
    @Autowired
    val productService: ProductService
) {

    @GetMapping("/product/shopping-category/{id}")
    fun getByCategoryIds(
        @PathVariable id: Int,
        @RequestParam page: Int = 1,
        @RequestParam numInPage: Int = 10,
        @RequestParam priceBelow: Int?,
    ): ResponseEntity<CustomResponse<List<Product>>> {
        return ResponseEntity.ok(
            CustomResponse(
                200, productService.getListByCategoryId(id, page, numInPage, priceBelow).content, ""
            )
        )
    }

    @PostMapping("/product/{productId}/dibs")
    fun makeProductDibs(@AuthenticationPrincipal userInfo: UserInfo, @PathVariable productId:Long): ResponseEntity<CustomResponse<Unit>> {
        return ResponseEntity.ok(
            CustomResponse(
                200, productService.createDibs(userInfo.id, productId), ""
            )
        )
    }

    @DeleteMapping("/product/{productId}/dibs")
    fun deleteProductDibs(@AuthenticationPrincipal userInfo: UserInfo, @PathVariable productId:Long): ResponseEntity<CustomResponse<Unit>> {
        return ResponseEntity.ok(
            CustomResponse(
                200, productService.deleteDibs(userInfo.id, productId), ""
            )
        )
    }

    @GetMapping("/user/me/dibs")
    fun getAllDibsProducts(@AuthenticationPrincipal userInfo: UserInfo): ResponseEntity<CustomResponse<List<Product>>> {
        return ResponseEntity.ok(
            CustomResponse(
                200, productService.getAllDibsProduct(userInfo.id), ""
            )
        )
    }
}