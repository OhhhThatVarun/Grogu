package com.varun.grogu.data

import app.cash.turbine.test
import com.varun.grogu.data.utils.safeApiCallWithCoroutine
import com.varun.grogu.data.utils.safeApiCallWithFlow
import com.varun.grogu.domain.utils.Resource
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.testng.annotations.AfterTest

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class SafeApiCallTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should return error resource when block throws an error`() = runTest {
        val expectedThrowable = Throwable()
        val result = safeApiCallWithCoroutine(testDispatcher) {
            throw expectedThrowable
        }
        assertTrue(result is Resource.Failure)
        assertEquals(expectedThrowable, (result as Resource.Failure).throwable)
    }

    @Test
    fun `should return success resource when block runs successfully`() = runTest {
        val expected = "test"
        val actual = safeApiCallWithCoroutine(testDispatcher) {
            expected
        }
        assertTrue(actual is Resource.Success)
        assertEquals(expected, (actual as Resource.Success).data)
    }

    @Test
    fun `should return loading and then error resource when block throws an error`() = runTest {
        val expectedThrowable = Throwable()
        safeApiCallWithFlow(testDispatcher) {
            throw expectedThrowable
        }.test {

            var actual = awaitItem()
            assertTrue(actual is Resource.Loading<*>)

            actual = awaitItem()
            assertTrue(actual is Resource.Failure<*>)
            assertEquals(expectedThrowable, (actual as Resource.Failure<*>).throwable)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `should return loading and then success resource when block runs successfully`() = runTest {
        val expected = "test"
        safeApiCallWithFlow(testDispatcher) {
            expected
        }.test {

            var actual = awaitItem()
            assertTrue(actual is Resource.Loading<*>)

            actual = awaitItem()
            assertTrue(actual is Resource.Success<*>)
            assertEquals(expected, (actual as Resource.Success<*>).data)
            cancelAndConsumeRemainingEvents()
        }
    }
}