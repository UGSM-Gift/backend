import com.ugsm.secretpresent.dto.giftlist.CreateGiftListDto
import com.ugsm.secretpresent.dto.giftlist.SelectedCategoryWithProducts
import com.ugsm.secretpresent.dto.giftlist.UpdateGiftListDto
import com.ugsm.secretpresent.enums.GiftCategoryReceiptType
import com.ugsm.secretpresent.enums.GiftConfirmedStatus
import com.ugsm.secretpresent.enums.Marketplace
import com.ugsm.secretpresent.enums.OAuth2Type
import com.ugsm.secretpresent.model.AnniversaryImage
import com.ugsm.secretpresent.model.NaverShoppingCategory
import com.ugsm.secretpresent.model.User
import com.ugsm.secretpresent.model.UserAnniversary
import com.ugsm.secretpresent.model.gift.GiftList
import com.ugsm.secretpresent.model.gift.GiftListProduct
import com.ugsm.secretpresent.model.gift.GiftListProductCategory
import com.ugsm.secretpresent.model.product.Product
import com.ugsm.secretpresent.repository.*
import com.ugsm.secretpresent.service.GiftListService
import org.hibernate.validator.internal.util.Contracts.assertNotNull
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.*
import org.springframework.data.domain.PageRequest
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class GiftListServiceTest {

    @InjectMocks
    lateinit var giftListService: GiftListService

    @Mock
    lateinit var giftListRepository: GiftListRepository

    @Mock
    lateinit var userAnniversaryRepository: UserAnniversaryRepository

    @Mock
    lateinit var userRepository: UserRepository

    @Mock
    lateinit var productRepository: ProductRepository

    @Mock
    lateinit var naverShoppingCategoryRepository: NaverShoppingCategoryRepository

    @Mock
    lateinit var giftListLetterRepository: GiftListLetterRepository

    @Mock
    lateinit var giftListProductRepository: GiftListProductRepository

    @Mock
    lateinit var giftListProductCategoryRepository: GiftListProductCategoryRepository

    @Mock
    lateinit var giftListProductRepositorySupport: GiftListProductRepositorySupport

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `create should create new gift list successfully`() {
        // Given
        val takerId = 1L

        val createGiftListDto = CreateGiftListDto(
            anniversaryId = 1,
            availableAt = LocalDate.now(),
            expiredAt = LocalDate.now().plusDays(7),
            imageFileName = "test.jpg",
            categoriesWithProducts = listOf(
                SelectedCategoryWithProducts(1, listOf(1L, 2L), GiftCategoryReceiptType.ALL)
            )
        )

        val taker = User(id = takerId, oauth2Type = OAuth2Type.KAKAO, oauth2Id = "1234")
        val anniversary = UserAnniversary(
            user = taker,
            date = LocalDate.now(),
            image = AnniversaryImage(
                "123",
                1
            ),
            name = "test-anniversary"
        )
        val giftList = GiftList(
            id = 1,
            taker = taker,
            userAnniversary = anniversary,
            availableAt = createGiftListDto.availableAt.atTime(0, 0, 0),
            expiredAt = createGiftListDto.expiredAt.atTime(23, 59, 59),
            imageUrl = "test_url"
        )

        val shoppingCategory = NaverShoppingCategory(
            id = 1,
            name = "123",
            parentCategory = null,
            imageUrl = "123",
            isActive = true,
        )
        val product1 = Product(
            id = 1L,
            name = "Test Product 1",
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
        val product2 = Product(
            id = 1L,
            name = "Test Product 2",
            price = 100,
            buyingUrl = "test",
            marketplace = Marketplace.NAVER,
            freeShipping = true,
            timestamp = LocalDateTime.now(),
            likeCount = 0,
            overallRate = 0,
            reviewCount = 0,
            marketplaceProductId = 456,
            productCategories = mutableListOf()
        )

        whenever(userRepository.findById(takerId)).thenReturn(Optional.of(taker))
        whenever(userAnniversaryRepository.findById(1)).thenReturn(Optional.of(anniversary))
        whenever(giftListRepository.save(any<GiftList>())).thenReturn(giftList)
        whenever(naverShoppingCategoryRepository.findById(1)).thenReturn(Optional.of(shoppingCategory))
        whenever(productRepository.findById(1L)).thenReturn(Optional.of(product1))
        whenever(productRepository.findById(2L)).thenReturn(Optional.of(product2))

        // When
        val result = giftListService.create(takerId, createGiftListDto)

        // Then
        assertNotNull(result)
        verify(giftListRepository).save(any())
        verify(giftListProductRepository).saveAll(any<List<GiftListProduct>>())
        verify(giftListProductCategoryRepository).saveAll(any<List<GiftListProductCategory>>())
    }

    @Test
    fun `getUserGiftList should return list of gift lists`() {
        // Given
        val takerId = 1L
        val page = 1
        val taker = User(id = takerId, oauth2Type = OAuth2Type.KAKAO, oauth2Id = "1234")
        val anniversary = UserAnniversary(
            user = taker,
            date = LocalDate.now(),
            image = AnniversaryImage(
                "123",
                1
            ),
            name = "test-anniversary"
        )
        val giftList = GiftList(
            id = 1,
            taker = taker,
            userAnniversary = anniversary,
            availableAt = LocalDateTime.now(),
            expiredAt = LocalDateTime.now(),
            imageUrl = "test_url"
        )

        whenever(
            giftListRepository.findSliceByTakerIdAndAvailableAtLessThanAndExpiredAtGreaterThan(
                eq(takerId), any<PageRequest>(), any<LocalDateTime>(), any<LocalDateTime>(),
            )
        ).thenReturn(listOf(giftList))
        whenever(giftListLetterRepository.findByGiftListIdAndConfirmedStatusNot(1, GiftConfirmedStatus.DENIED))
            .thenReturn(emptyList())

        // When
        val result = giftListService.getUserGiftList(takerId, page)

        // Then
        assertNotNull(result)
        assertEquals(1, result.size)
        assertEquals(1, result[0].listId)
    }

    @Test
    fun `get should return gift list detail`() {
        // Given
        val takerId = 1L
        val giftListId = 1
        val taker = User(id = takerId, nickname = "testnickname", oauth2Type = OAuth2Type.KAKAO, oauth2Id = "1234")
        val anniversary = UserAnniversary(
            user = taker,
            date = LocalDate.now(),
            image = AnniversaryImage(
                "123",
                1
            ),
            name = "test-anniversary"
        )
        val giftList = GiftList(
            id = giftListId,
            taker = taker,
            userAnniversary = anniversary,
            availableAt = LocalDateTime.now(),
            expiredAt = LocalDateTime.now(),
            imageUrl = "test_url"
        )

        whenever(giftListRepository.findById(giftListId)).thenReturn(Optional.of(giftList))

        // When
        val result = giftListService.get(giftListId)

        // Then
        assertNotNull(result)
        assertEquals("testnickname", result.takerNickname)
        assertEquals("test-anniversary", result.anniversaryTitle)
    }

    @Test
    fun `update should update gift list successfully`() {
        // Given
        val takerId = 1L
        val giftListId = 1
        val taker = User(id = takerId, oauth2Type = OAuth2Type.KAKAO, oauth2Id = "1234")
        val updateDto = UpdateGiftListDto(
            userAnniversaryId = 1,
            availableAt = LocalDate.now().plusDays(1),
            expiredAt = LocalDate.now().plusDays(8),
            imageFileName = "new_test.jpg"
        )

        val giftList = GiftList(
            id = giftListId,
            taker = taker,
            userAnniversary = UserAnniversary(
                id = 1L,
                user = taker,
                date = LocalDate.now(),
                name = "Test Anniversary",
                image = AnniversaryImage(
                    "123",
                    1
                ),
            ),
            availableAt = LocalDateTime.now(),
            expiredAt = LocalDateTime.now().plusDays(7),
            imageUrl = "test_url"
        )

        val newAnniversary = UserAnniversary(
            id = 2L,
            user = taker,
            date = LocalDate.now().plusYears(1),
            name = "New Anniversary",
            image = AnniversaryImage(
                "new_test.jpg",
                1
            ),
        )

        whenever(giftListRepository.findById(giftListId)).thenReturn(Optional.of(giftList))
        whenever(userAnniversaryRepository.findById(1)).thenReturn(Optional.of(newAnniversary))

        // When
        giftListService.update(takerId, giftListId, updateDto)

        // Then
        verify(giftListRepository).findById(giftListId)
        assertEquals(newAnniversary, giftList.userAnniversary)
        assertEquals(updateDto.availableAt?.atTime(0, 0, 0), giftList.availableAt)
        assertEquals(updateDto.expiredAt?.atTime(23, 59, 59), giftList.expiredAt)
        assertTrue(giftList.imageUrl.endsWith("new_test.jpg"))
    }
}