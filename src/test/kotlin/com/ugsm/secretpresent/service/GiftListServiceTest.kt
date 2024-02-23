package com.ugsm.secretpresent.service

import com.ugsm.secretpresent.repository.*
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired

class GiftListServiceTest {

    @InjectMocks
    private lateinit var giftListService: GiftListService

    @Mock
    private lateinit var giftListRepository: GiftListRepository

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
        Mockito.`when`(giftListRepositorySupport.getAllByUserIdNotExpired(userId)).thenReturn(null)
    }


}