package com.ugsm.secretpresent.service

import com.ugsm.secretpresent.dto.giftlist.GiftListDto
import com.ugsm.secretpresent.repository.*
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertIterableEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.springframework.data.domain.PageRequest

class GiftListServiceTest {

    @InjectMocks
    private lateinit var giftListService: GiftListService

    @Mock
    private lateinit var giftListRepository: GiftListRepository

    @Mock
    private lateinit var giftListProductRepository: GiftListProductRepository

    @Mock
    private lateinit var giftListProductRepositorySupport: GiftListProductRepositorySupport
    @Mock
    private lateinit var giftListProductCategoryRepository: GiftListProductCategoryRepository

    @Mock
    private lateinit var userAnniversaryRepository: UserAnniversaryRepository

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var productRepository: ProductRepository

    @Mock
    private lateinit var naverShoppingCategoryRepository: NaverShoppingCategoryRepository

    @Mock
    private lateinit var giftListRepositorySupport: GiftListRepositorySupport

    companion object {
        @JvmStatic
        @BeforeAll
        fun beforeAll(): Unit {

        }


        @AfterAll
        @JvmStatic
        fun afterAll(): Unit {

        }
    }


    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun testGetUserGiftList() {
        val userId = 1L
        val page = 1
        val numInPage = 10
        val pageable = PageRequest.of(page, numInPage)
        Mockito.`when`(giftListRepositorySupport.getAllByUserIdNotExpired(userId, pageable)).thenReturn(listOf())

        val expected:List<GiftListDto> = listOf()
        assertIterableEquals(giftListRepositorySupport.getAllByUserIdNotExpired(userId,pageable), expected)
    }


}