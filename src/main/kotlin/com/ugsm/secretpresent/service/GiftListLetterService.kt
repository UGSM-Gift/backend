package com.ugsm.secretpresent.service

import com.ugsm.secretpresent.Exception.CustomException
import com.ugsm.secretpresent.Exception.UnauthorizedException
import com.ugsm.secretpresent.dto.CreateLetterDto
import com.ugsm.secretpresent.dto.GiftListGivenProductDto
import com.ugsm.secretpresent.dto.GiftListLetterDetailsDto
import com.ugsm.secretpresent.dto.GiftListLetterDto
import com.ugsm.secretpresent.enums.GiftConfirmedStatus
import com.ugsm.secretpresent.enums.S3ImageUploadType
import com.ugsm.secretpresent.model.gift.GiftListLetter
import com.ugsm.secretpresent.repository.*
import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class GiftListLetterService(
    @Autowired
    val giftListLetterRepository: GiftListLetterRepository,
    @Autowired
    val giftListRepository: GiftListRepository,
    @Autowired
    val giftListProductCategoryRepository: GiftListProductCategoryRepository,
    @Autowired
    val productRepository: ProductRepository,
    @Autowired
    val userRepository: UserRepository,
    @Autowired
    val giftListProductRepository: GiftListProductRepository
) {

    fun getProductsByReceiverId(receiverId: Long): List<GiftListGivenProductDto> {
        return giftListLetterRepository.findByReceiverId(receiverId).map {
            GiftListGivenProductDto(
                productId = it.productId,
                productName = it.productName,
                productPrice = it.productPrice,
                confirmedStatus = it.confirmedStatus,
                giverId = it.giver.id,
                giverNickname = it.giver.nickname,
                sentAt = it.createdAt
            )
        }
    }

    fun getProductsByGiverId(giverId: Long): List<GiftListGivenProductDto> {
        return giftListLetterRepository.findByGiverId(giverId).map {
            GiftListGivenProductDto(
                productId = it.productId,
                productName = it.productName,
                productPrice = it.productPrice,
                confirmedStatus = it.confirmedStatus,
                giverId = it.giver.id,
                giverNickname = it.giver.nickname,
                sentAt = it.createdAt
            )
        }
    }

    fun getLettersByReceiverId(receiverId: Long, confirmedStatus: GiftConfirmedStatus?): List<GiftListLetterDto> {
        val result = if(confirmedStatus == null){
            giftListLetterRepository.findByReceiverId(receiverId)
        } else {
            giftListLetterRepository.findByReceiverIdAndConfirmedStatusIs(receiverId, confirmedStatus)
        }

        return result.map{
            GiftListLetterDto(
                it.id,
                it.giver.id,
                it.giver.nickname,
                it.giftList.imgName,
                it.createdAt
            )
        }
    }

    fun getLetterDetailsById(id: Int){
        return giftListLetterRepository.findById(id).get().let {
            GiftListLetterDetailsDto(
                it.id!!,
                it.giver.id,
                it.giver.nickname,
                it.giftList.imgName,
                it.productId,
                it.productName,
                it.productPrice,
                it.message,
                it.createdAt
            )
        }
    }

    @Transactional
    fun changeConfirmedStatus(receiverId: Long, letterId: Int, confirmedStatus: GiftConfirmedStatus) {
        val letter = giftListLetterRepository.findById(letterId).get()
        if(letter.receiver.id != receiverId) throw UnauthorizedException()
        if(letter.confirmedStatus != GiftConfirmedStatus.NOT_CONFIRMED) throw CustomException(101, "이미 취소/거절한 선물은 상태를 변경할 수 없습니다.")

        letter.confirmedStatus = confirmedStatus
    }

    @Transactional
    fun create(giverId: Long, giftListId: Int, letterInfo: CreateLetterDto): Int? {
        val giftList = giftListRepository.findById(giftListId).get()
        val giver = userRepository.findById(giverId).get()
        if(giftList.taker.id == giverId) throw CustomException(101, "자기 자신에게 선물을 줄 수 없습니다.")
        val giftListProductCategory =
            giftListProductCategoryRepository.findByGiftListIdAndShoppingCategoryId(giftListId,letterInfo.productCategoryId)
                ?: throw EntityNotFoundException()
        val product = productRepository.findById(letterInfo.productId).get()
        val giftListProduct = giftListProductRepository.findByGiftListIdAndProductId(giftListId, product.id)
            ?: throw EntityNotFoundException()

        val alreadyGiftedProduct = giftListLetterRepository.findByGiftListIdAndProductIdAndConfirmedStatusNot(giftListId, product.id, GiftConfirmedStatus.DENIED)
        if(alreadyGiftedProduct != null) throw CustomException(102, "해당 상품은 이미 다른 사람이 준 내역이 있습니다.")

        val uploadedImageUrl = "${S3ImageUploadType.GIFT_LIST_LETTER.getUrl(giverId)}/${letterInfo.imageFileName}"

        val letter = GiftListLetter(
            giftList=giftList,
            giftListProductCategory=giftListProductCategory,
            giftListProduct = giftListProduct,
            message = letterInfo.message,
            confirmedStatus = GiftConfirmedStatus.NOT_CONFIRMED,
            giver=giver,
            productId = product.id,
            productName = product.name,
            productPrice = product.price,
            productCategoryName = giftListProductCategory.shoppingCategory.name,
            receiver = giftList.taker,
            imageUrl = uploadedImageUrl
        )

        giftListLetterRepository.save(letter)

        return letter.id
    }
}
