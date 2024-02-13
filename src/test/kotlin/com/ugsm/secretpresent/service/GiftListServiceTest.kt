package com.ugsm.secretpresent.service

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GiftListServiceTest {

    companion object {
        @BeforeAll
        @JvmStatic
        fun beforeAll(){
            println("class made")
        }
    }



    @BeforeEach
    fun setUp() {
        println("Inside before each method")
    }

    @Test
    fun testMethod1(): Unit {
        println("my first test method")

    }

    @Test
    fun testMethod2(): Unit {
        println("my second test method")

    }

    @AfterEach
    fun tearDown() {
        println("After each method")
    }


}