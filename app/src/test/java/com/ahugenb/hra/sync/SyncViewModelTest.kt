package com.ahugenb.hra.sync

import com.google.android.gms.ads.identifier.AdvertisingIdClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.Mockito.times
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SyncViewModelTest {

    // Test dispatcher for coroutines
    @get:Rule
    val testDispatcherRule = TestDispatcherRule()

    @Mock
    private lateinit var mockSyncRepository: SyncRepository

    @Mock
    private lateinit var mockAdvertisingIdInfo: AdvertisingIdClient.Info // Mock the data class

    private lateinit var viewModel: SyncViewModel

    // Custom TestRule for setting Main dispatcher
    class TestDispatcherRule(private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()) : org.junit.rules.TestRule {
        override fun apply(base: org.junit.runners.model.Statement, description: org.junit.runner.Description): org.junit.runners.model.Statement {
            return object : org.junit.runners.model.Statement() {
                override fun evaluate() {
                    Dispatchers.setMain(testDispatcher)
                    try {
                        base.evaluate()
                    } finally {
                        Dispatchers.resetMain()
                    }
                }
            }
        }
    }

    @Before
    fun setUp() {
        viewModel = SyncViewModel(mockSyncRepository)
        // Mock default behavior for AdvertisingIdClient.Info if needed, e.g., getId()
        `when`(mockAdvertisingIdInfo.id).thenReturn("test-id")
        `when`(mockAdvertisingIdInfo.isLimitAdTrackingEnabled).thenReturn(false)
    }

    @Test
    fun `initial state is SyncStateEmpty`() = runTest {
        val initialState = viewModel.firebaseInfo.first()
        assertTrue(initialState is SyncState.SyncStateEmpty)
    }

    @Test
    fun `getFirebaseId updates state to SyncStateAll on successful fetch`() = runTest {
        // Given
        `when`(mockSyncRepository.getFirebaseInfo()).thenReturn(flowOf(mockAdvertisingIdInfo))

        // When
        viewModel.getFirebaseId()
        advanceUntilIdle() // Ensure coroutines launched by getFirebaseId complete

        // Then
        val state = viewModel.firebaseInfo.value
        assertTrue(state is SyncState.SyncStateAll)
        assertEquals(mockAdvertisingIdInfo, (state as SyncState.SyncStateAll).info)
        verify(mockSyncRepository).getFirebaseInfo()
    }

    @Test
    fun `getFirebaseId handles exception from repository and state remains empty`() = runTest {
        // Given
        val errorMessage = "Test repository exception"
        `when`(mockSyncRepository.getFirebaseInfo()).thenReturn(flow { throw RuntimeException(errorMessage) })

        // When
        viewModel.getFirebaseId()
        advanceUntilIdle()

        // Then
        val state = viewModel.firebaseInfo.value
        assertTrue(state is SyncState.SyncStateEmpty) // State should remain empty or unchanged
        verify(mockSyncRepository).getFirebaseInfo()
        // Log verification is tricky in unit tests without specific log appenders.
        // We trust the .catch operator handles logging as shown in ViewModel.
    }

    @Test
    fun `getFirebaseId does not call repository if info already fetched`() = runTest {
        // Given: First successful call
        `when`(mockSyncRepository.getFirebaseInfo()).thenReturn(flowOf(mockAdvertisingIdInfo))
        viewModel.getFirebaseId()
        advanceUntilIdle()

        // Sanity check: verify state and that repository was called once
        val stateAfterFirstCall = viewModel.firebaseInfo.value
        assertTrue(stateAfterFirstCall is SyncState.SyncStateAll)
        verify(mockSyncRepository, times(1)).getFirebaseInfo()

        // When: Call getFirebaseId again
        viewModel.getFirebaseId()
        advanceUntilIdle()

        // Then: Verify repository was not called a second time
        verify(mockSyncRepository, times(1)).getFirebaseInfo()
        val stateAfterSecondCall = viewModel.firebaseInfo.value
        assertTrue(stateAfterSecondCall is SyncState.SyncStateAll) // State should remain the same
        assertEquals(mockAdvertisingIdInfo, (stateAfterSecondCall as SyncState.SyncStateAll).info)
    }
}
