import com.ugsm.secretpresent.dto.CreateLetterDto
import com.ugsm.secretpresent.enums.GiftConfirmedStatus
import com.ugsm.secretpresent.enums.OAuth2Type
import com.ugsm.secretpresent.model.gift.GiftList
import com.ugsm.secretpresent.model.gift.GiftListLetter
import com.ugsm.secretpresent.model.gift.GiftListProduct
import com.ugsm.secretpresent.model.gift.GiftListProductCategory
import com.ugsm.secretpresent.model.product.Product
import com.ugsm.secretpresent.model.NaverShoppingCategory
import com.ugsm.secretpresent.model.User
import com.ugsm.secretpresent.repository.*
import com.ugsm.secretpresent.service.GiftListLetterService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
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
            "https://example.cloudfront.net"
        )
    }

    @Test
    fun `getProductsByReceiverId should return correct list`() {
        val receiverId = 1L
        val letter = GiftListLetter(
            id = 1,
            giftList = GiftList(),
            giftListProductCategory = GiftListProductCategory(),
            giftListProduct = GiftListProduct(),
            message = "Test",
            confirmedStatus = GiftConfirmedStatus.NOT_CONFIRMED,
            giver = User(id = 2L, nickname = "Giver"),
            productId = 1L,
            productName = "Test Product",
            productPrice = 100,
            productCategoryName = "Test Category",
            receiver = User(id = receiverId, oauth2Type = OAuth2Type.KAKAO, oauth2Id = "1234"),
            imageUrl = "test.jpg",
            createdAt = LocalDateTime.now()
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
        val letter = GiftListLetter(
            id = letterId,
            giftList = GiftList(),
            giftListProductCategory = GiftListProductCategory(),
            giftListProduct = GiftListProduct(),
            message = "Test",
            confirmedStatus = GiftConfirmedStatus.NOT_CONFIRMED,
            giver = User(),
            productId = 1L,
            productName = "Test Product",
            productPrice = 100,
            productCategoryName = "Test Category",
            receiver = User(id = receiverId),
            imageUrl = "test.jpg"
        )

        whenever(giftListLetterRepository.findById(letterId)).thenReturn(java.util.Optional.of(letter))

        service.changeConfirmedStatus(receiverId, letterId, GiftConfirmedStatus.CONFIRMED)

        assertEquals(GiftConfirmedStatus.CONFIRMED, letter.confirmedStatus)
    }

    @Test
    fun `create should create new letter successfully`() {
        val giverId = 1L
        val giftListId = 1
        val letterInfo = CreateLetterDto(
            productId = 1L,
            productCategoryId = 1,
            message = "Test message",
            imageFileName = "test.jpg"
        )

        val giftList = GiftList(id = giftListId, taker = User(id = 2L))
        val giver = User(id = giverId)
        val product = Product(id = 1L, name = "Test Product", price = 100)
        val giftListProductCategory = GiftListProductCategory(shoppingCategory = ShoppingCategory(name = "Test Category"))
        val giftListProduct = GiftListProduct()

        whenever(giftListRepository.findById(giftListId)).thenReturn(java.util.Optional.of(giftList))
        whenever(userRepository.findById(giverId)).thenReturn(java.util.Optional.of(giver))
        whenever(giftListProductCategoryRepository.findByGiftListIdAndShoppingCategoryId(giftListId, letterInfo.productCategoryId))
            .thenReturn(giftListProductCategory)
        whenever(productRepository.findById(letterInfo.productId)).thenReturn(java.util.Optional.of(product))
        whenever(giftListProductRepository.findByGiftListIdAndProductId(giftListId, product.id)).thenReturn(giftListProduct)
        whenever(giftListLetterRepository.findByGiftListIdAndProductIdAndConfirmedStatusNot(giftListId, product.id, GiftConfirmedStatus.DENIED))
            .thenReturn(emptyList())
        whenever(giftListLetterRepository.save(any())).thenAnswer { invocation -> invocation.arguments[0] }

        val result = service.create(giverId, giftListId, letterInfo)

        assertNotNull(result)
        verify(giftListLetterRepository).save(any())
    }
}