import com.ugsm.secretpresent.dto.CreateLetterDto
import com.ugsm.secretpresent.enums.GiftCategoryReceiptType
import com.ugsm.secretpresent.enums.GiftConfirmedStatus
import com.ugsm.secretpresent.enums.Marketplace
import com.ugsm.secretpresent.enums.OAuth2Type
import com.ugsm.secretpresent.model.AnniversaryImage
import com.ugsm.secretpresent.model.gift.GiftList
import com.ugsm.secretpresent.model.gift.GiftListLetter
import com.ugsm.secretpresent.model.gift.GiftListProduct
import com.ugsm.secretpresent.model.gift.GiftListProductCategory
import com.ugsm.secretpresent.model.product.Product
import com.ugsm.secretpresent.model.NaverShoppingCategory
import com.ugsm.secretpresent.model.User
import com.ugsm.secretpresent.model.UserAnniversary
import com.ugsm.secretpresent.model.product.ProductCategory
import com.ugsm.secretpresent.repository.*
import com.ugsm.secretpresent.service.GiftListLetterService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.time.LocalDate
import java.time.LocalDateTime

class GiftListLetterServiceTest {

    private lateinit var service: GiftListLetterService
    private lateinit var giftListLetterRepository: GiftListLetterRepository
    private lateinit var giftListRepository: GiftListRepository
    private lateinit var giftListProductCategoryRepository: GiftListProductCategoryRepository
    private lateinit var productRepository: ProductRepository
    private lateinit var userRepository: UserRepository
    private lateinit var giftListProductRepository: GiftListProductRepository
    private lateinit var userDibsProductRepository: UserDibsProductRepository

    @BeforeEach
    fun setup() {
        giftListLetterRepository = mock(GiftListLetterRepository::class.java)
        giftListRepository = mock(GiftListRepository::class.java)
        giftListProductCategoryRepository = mock(GiftListProductCategoryRepository::class.java)
        productRepository = mock(ProductRepository::class.java)
        userRepository = mock(UserRepository::class.java)
        giftListProductRepository = mock(GiftListProductRepository::class.java)
        userDibsProductRepository = mock(UserDibsProductRepository::class.java)

        service = GiftListLetterService(
            giftListLetterRepository,
            giftListRepository,
            giftListProductCategoryRepository,
            productRepository,
            userRepository,
            giftListProductRepository,
            userDibsProductRepository,
            "https://example.cloudfront.net" // @Value 로 인해서 constructor injection 으로 변경
        )
    }

    @Test
    fun `getProductsByReceiverId should return correct list`() {
        val receiverId = 1L
        val taker = User(id = receiverId, oauth2Type = OAuth2Type.KAKAO, oauth2Id = "1234")
        val giftList = GiftList(
            availableAt = LocalDateTime.now(),
            expiredAt = LocalDateTime.now(),
            imageUrl = "test",
            taker = taker,
            userAnniversary = UserAnniversary(
                user = taker,
                date = LocalDate.now(),
                image = AnniversaryImage(
                    "123",
                    1
                ),
                name = "test-anniversary"
            )
        )
        val shoppingCategory = NaverShoppingCategory(
            id = 1,
            name = "123",
            parentCategory = null,
            imageUrl = "123",
            isActive = true,
        )

        val product = Product(
            id = 1L,
            name = "Test Product",
            price = 100,
            buyingUrl = "test",
            marketplace = Marketplace.NAVER,
            freeShipping = true,
            timestamp = LocalDateTime.now(),
            likeCount = 0,
            overallRate = 0,
            reviewCount = 0,
            marketplaceProductId = 123,
            productCategories = mutableListOf()
        )

        val productCategory = ProductCategory(
            id = 1,
            product = product,
            shoppingCategory = shoppingCategory
        )
        product.productCategories.add(productCategory)

        val letter = GiftListLetter(
            id = 1,
            giftList = giftList,
            giftListProductCategory = GiftListProductCategory(
                receiptType = GiftCategoryReceiptType.ONLY_ONE, shoppingCategory = shoppingCategory
            ),
            giftListProduct = GiftListProduct(
                id = 1,
                giftList = giftList,
                productCategory = shoppingCategory,
                product = product
            ),
            message = "Test",
            confirmedStatus = GiftConfirmedStatus.NOT_CONFIRMED,
            giver = User(id = 2L, nickname = "Giver", oauth2Type = OAuth2Type.KAKAO, oauth2Id = "123"),
            productId = 1L,
            productName = "Test Product",
            productPrice = 100,
            productCategoryName = "Test Category",
            receiver = taker,
            imageUrl = "test.jpg",
        )

        whenever(giftListLetterRepository.findByReceiverIdOrderByCreatedAtDesc(receiverId))
            .thenReturn(listOf(letter))

        val result = service.getProductsByReceiverId(receiverId)

        assertEquals(1, result.size)
        assertEquals(letter.productId, result[0].productId)
        assertEquals(letter.productName, result[0].productName)
        assertEquals(letter.productPrice, result[0].productPrice)
        assertEquals(letter.confirmedStatus, result[0].confirmedStatus)
        assertEquals(letter.giver.id, result[0].giverId)
        assertEquals(letter.giver.nickname, result[0].giverNickname)
        assertEquals(letter.createdAt, result[0].sentAt)
    }

    @Test
    fun `changeConfirmedStatus should update status correctly`() {
        val receiverId = 1L
        val letterId = 1
        val taker = User(id = receiverId, oauth2Type = OAuth2Type.KAKAO, oauth2Id = "1234")
        val giftList = GiftList(
            availableAt = LocalDateTime.now(),
            expiredAt = LocalDateTime.now(),
            imageUrl = "test",
            taker = taker,
            userAnniversary = UserAnniversary(
                user = taker,
                date = LocalDate.now(),
                image = AnniversaryImage(
                    "123",
                    1
                ),
                name = "test-anniversary"
            )
        )
        val shoppingCategory = NaverShoppingCategory(
            id = 1,
            name = "123",
            parentCategory = null,
            imageUrl = "123",
            isActive = true,
        )

        val product = Product(
            id = 1L,
            name = "Test Product",
            price = 100,
            buyingUrl = "test",
            marketplace = Marketplace.NAVER,
            freeShipping = true,
            timestamp = LocalDateTime.now(),
            likeCount = 0,
            overallRate = 0,
            reviewCount = 0,
            marketplaceProductId = 123,
            productCategories = mutableListOf()
        )

        val productCategory = ProductCategory(
            id = 1,
            product = product,
            shoppingCategory = shoppingCategory
        )
        product.productCategories.add(productCategory)

        val letter = GiftListLetter(
            id = 1,
            giftList = giftList,
            giftListProductCategory = GiftListProductCategory(
                receiptType = GiftCategoryReceiptType.ONLY_ONE, shoppingCategory = shoppingCategory
            ),
            giftListProduct = GiftListProduct(
                id = 1,
                giftList = giftList,
                productCategory = shoppingCategory,
                product = product
            ),
            message = "Test",
            confirmedStatus = GiftConfirmedStatus.NOT_CONFIRMED,
            giver = User(id = 2L, nickname = "Giver", oauth2Type = OAuth2Type.KAKAO, oauth2Id = "123"),
            productId = 1L,
            productName = "Test Product",
            productPrice = 100,
            productCategoryName = "Test Category",
            receiver = taker,
            imageUrl = "test.jpg",
        )

        whenever(giftListLetterRepository.findById(letterId)).thenReturn(java.util.Optional.of(letter))

        service.changeConfirmedStatus(receiverId, letterId, GiftConfirmedStatus.CONFIRMED)

        assertEquals(GiftConfirmedStatus.CONFIRMED, letter.confirmedStatus)
    }

    @Test
    fun `create should create new letter successfully`() {
        val giverId = 2L
        val giftListId = 1
        val receiverId = 1L
        val letterInfo = CreateLetterDto(
            productId = 1L,
            productCategoryId = 1,
            message = "Test message",
            imageFileName = "test.jpg"
        )
        val giver = User(id = 2L, nickname = "Giver", oauth2Type = OAuth2Type.KAKAO, oauth2Id = "123")


        val taker = User(id = receiverId, oauth2Type = OAuth2Type.KAKAO, oauth2Id = "1234")
        val giftList = GiftList(
            availableAt = LocalDateTime.now(),
            expiredAt = LocalDateTime.now(),
            imageUrl = "test",
            taker = taker,
            userAnniversary = UserAnniversary(
                user = taker,
                date = LocalDate.now(),
                image = AnniversaryImage(
                    "123",
                    1
                ),
                name = "test-anniversary"
            )
        )
        val shoppingCategory = NaverShoppingCategory(
            id = 1,
            name = "123",
            parentCategory = null,
            imageUrl = "123",
            isActive = true,
        )

        val product = Product(
            id = 1L,
            name = "Test Product",
            price = 100,
            buyingUrl = "test",
            marketplace = Marketplace.NAVER,
            freeShipping = true,
            timestamp = LocalDateTime.now(),
            likeCount = 0,
            overallRate = 0,
            reviewCount = 0,
            marketplaceProductId = 123,
            productCategories = mutableListOf()
        )

        val productCategory = ProductCategory(
            id = 1,
            product = product,
            shoppingCategory = shoppingCategory
        )

        val giftListProductCategory = GiftListProductCategory(
            receiptType = GiftCategoryReceiptType.ONLY_ONE, shoppingCategory = shoppingCategory
        )
        val giftListProduct = GiftListProduct(
            id = 1,
            giftList = giftList,
            productCategory = shoppingCategory,
            product = product
        )

        product.productCategories.add(productCategory)


        whenever(giftListRepository.findById(giftListId)).thenReturn(java.util.Optional.of(giftList))
        whenever(userRepository.findById(giverId)).thenReturn(java.util.Optional.of(giver))
        whenever(
            giftListProductCategoryRepository.findByGiftListIdAndShoppingCategoryId(
                giftListId,
                letterInfo.productCategoryId
            )
        )
            .thenReturn(giftListProductCategory)
        whenever(productRepository.findById(letterInfo.productId)).thenReturn(java.util.Optional.of(product))
        whenever(giftListProductRepository.findByGiftListIdAndProductId(giftListId, product.id)).thenReturn(
            giftListProduct
        )
        whenever(
            giftListLetterRepository.findByGiftListIdAndProductIdAndConfirmedStatusNot(
                giftListId,
                product.id,
                GiftConfirmedStatus.DENIED
            )
        )
            .thenReturn(emptyList())
        whenever(giftListLetterRepository.save(any<GiftListLetter>())).thenAnswer { invocation -> invocation.arguments[0] }

        val result = service.create(giverId, giftListId, letterInfo)
        assertNotNull(result)
        verify(giftListLetterRepository).save(any())
    }
}