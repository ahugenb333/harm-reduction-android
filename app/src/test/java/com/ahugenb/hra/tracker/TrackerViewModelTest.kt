package com.ahugenb.hra.tracker

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import com.ahugenb.hra.tracker.db.Day
import com.ahugenb.hra.tracker.db.DayRepository
import com.ahugenb.hra.Utils.Companion.idToDateTime
import com.ahugenb.hra.Utils.Companion.toId
import org.joda.time.DateTime
import org.mockito.Mockito.`when`
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.verify
import org.mockito.Mockito.times

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class TrackerViewModelTest {

    // Test dispatcher for coroutines
    @get:Rule
    val testDispatcherRule = TestDispatcherRule()

    @Mock
    private lateinit var mockDayRepository: DayRepository

    private lateinit var viewModel: TrackerViewModel

    // Helper to create a Day object for today
    private fun today(): Day = Day(DateTime.now().toId())
    private fun yesterday(): Day = Day(DateTime.now().minusDays(1).toId())
    private fun tomorrow(): Day = Day(DateTime.now().plusDays(1).toId())

    @Before
    fun setUp() {
        // viewModel = TrackerViewModel(mockDayRepository) // This will be initialized in each test with specific repo responses
    }

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

    @Test
    fun `initDays when repository is empty populates state with current week`() = runTest {
        // Given
        `when`(mockDayRepository.getDays()).thenReturn(flowOf(emptyList()))
        `when`(mockDayRepository.insertDays(org.mockito.kotlin.any())).thenReturn(flowOf(Unit)) // Mock insertDays

        // When
        viewModel = TrackerViewModel(mockDayRepository)
        advanceUntilIdle() // Allow coroutines to complete

        // Then
        val state = viewModel.trackerState.value
        assertTrue(state is TrackerState.TrackerStateAll)
        val allState = state as TrackerState.TrackerStateAll

        assertNotNull(allState.today)
        assertEquals(DateTime.now().toId(), allState.today.id)
        assertEquals(7, allState.daysOfWeek.size)
        assertTrue(allState.daysOfWeek.any { it.id == today().id })
        assertEquals(1, allState.weekBeginnings.size) // Should have one week (current week)
        assertEquals(allState.daysOfWeek.first().id, allState.selectedMonday.id)

        // Verify repository interactions
        val captor = ArgumentCaptor.forClass(List::class.java) as ArgumentCaptor<List<Day>>
        verify(mockDayRepository).insertDays(captor.capture())
        val insertedDays = captor.value
        assertEquals(7, insertedDays.size) // Should insert the generated week
    }

    @Test
    fun `initDays with existing data populates state correctly`() = runTest {
        // Given
        val today = today()
        val yesterday = yesterday()
        val testDays = listOf(yesterday, today)
        `when`(mockDayRepository.getDays()).thenReturn(flowOf(testDays))
        `when`(mockDayRepository.insertDays(org.mockito.kotlin.any())).thenReturn(flowOf(Unit))

        // When
        viewModel = TrackerViewModel(mockDayRepository)
        advanceUntilIdle()

        // Then
        val state = viewModel.trackerState.value
        assertTrue(state is TrackerState.TrackerStateAll)
        val allState = state as TrackerState.TrackerStateAll

        assertEquals(today.id, allState.today.id)
        assertEquals(7, allState.daysOfWeek.size) // One full week around today
        assertTrue(allState.daysOfWeek.any { it.id == today.id })
        assertTrue(allState.daysOfWeek.any { it.id == yesterday.id })

        val expectedMonday = today.id.idToDateTime().withDayOfWeek(1)
        assertEquals(expectedMonday.toId(), allState.selectedMonday.id)
        assertTrue(allState.weekBeginnings.any { it.id == expectedMonday.toId() })

        // Check that allDays contains the original days and generated days to fill the week
        val captor = ArgumentCaptor.forClass(List::class.java) as ArgumentCaptor<List<Day>>
        verify(mockDayRepository).insertDays(captor.capture())
        val insertedDays = captor.value
        // This count can be tricky due to generateAllDays logic filling gaps.
        // For this simple case, it should be at least 7 (the current week)
        assertTrue(insertedDays.size >= 7)
        assertTrue(insertedDays.any { it.id == today.id })
        assertTrue(insertedDays.any { it.id == yesterday.id })
    }

    @Test
    fun `updateSelectedMonday updates state correctly`() = runTest {
        // Given
        val currentDt = DateTime.now()
        val mondayThisWeek = Day(currentDt.withDayOfWeek(1).toId())
        val mondayLastWeek = Day(currentDt.minusWeeks(1).withDayOfWeek(1).toId())

        // Prepare initial state similar to what initDays would produce
        val initialDays = mutableListOf<Day>()
        var tempDate = currentDt.minusWeeks(1).withDayOfWeek(1)
        for (i in 0..13) { // Two weeks of data
            initialDays.add(Day(tempDate.toId()))
            tempDate = tempDate.plusDays(1)
        }
        `when`(mockDayRepository.getDays()).thenReturn(flowOf(initialDays))
        `when`(mockDayRepository.insertDays(org.mockito.kotlin.any())).thenReturn(flowOf(Unit))
        viewModel = TrackerViewModel(mockDayRepository)
        advanceUntilIdle()

        val initialState = viewModel.trackerState.value as TrackerState.TrackerStateAll
        // weekBeginnings is reversed, so index 0 is the most recent Monday
        // index 1 is the Monday of the previous week.
        assertTrue(initialState.weekBeginnings.size >= 2) // Ensure we have at least two Mondays

        // When
        // Simulate calling the lambda
        viewModel.onUpdateSelectedMonday(1) // Select the previous week's Monday
        advanceUntilIdle()

        // Then
        val updatedState = viewModel.trackerState.value as TrackerState.TrackerStateAll
        assertEquals(mondayLastWeek.id, updatedState.selectedMonday.id)
        assertEquals(7, updatedState.daysOfWeek.size)
        assertEquals(mondayLastWeek.id, updatedState.daysOfWeek[0].id) // First day of the week should be the selected Monday
        assertEquals(mondayLastWeek.id.idToDateTime().plusDays(6).toId(), updatedState.daysOfWeek[6].id) // Last day of the week
    }

    @Test
    fun `setSelectedDay updates selectedDay in state`() = runTest {
        // Given
        val today = today()
        val dayToSelect = Day(today.id.idToDateTime().minusDays(1).toId(), drinks = 1.0) // Yesterday

        `when`(mockDayRepository.getDays()).thenReturn(flowOf(listOf(dayToSelect, today)))
        `when`(mockDayRepository.insertDays(org.mockito.kotlin.any())).thenReturn(flowOf(Unit))
        viewModel = TrackerViewModel(mockDayRepository)
        advanceUntilIdle()

        // When
        viewModel.onSetSelectedDay(dayToSelect) // Use the lambda
        advanceUntilIdle()

        // Then
        val state = viewModel.trackerState.value as TrackerState.TrackerStateAll
        assertNotNull(state.selectedDay)
        assertEquals(dayToSelect.id, state.selectedDay!!.id)
        assertEquals(dayToSelect.drinks, state.selectedDay!!.drinks)

        // When selecting null
        viewModel.onSetSelectedDay(null) // Use the lambda
        advanceUntilIdle()
        val stateAfterNull = viewModel.trackerState.value as TrackerState.TrackerStateAll
        assertNull(stateAfterNull.selectedDay)
    }

    @Test
    fun `getLastWeek returns the correct week`() = runTest {
        // Given
        val currentDt = DateTime.now()
        // Prepare enough data for current week and previous week
        val initialDays = mutableListOf<Day>()
        var tempDate = currentDt.minusWeeks(1).withDayOfWeek(1) // Start from Monday of last week
        for (i in 0..13) { // Two full weeks
            initialDays.add(Day(tempDate.toId(), i.toDouble())) // Add some dummy data
            tempDate = tempDate.plusDays(1)
        }

        `when`(mockDayRepository.getDays()).thenReturn(flowOf(initialDays))
        `when`(mockDayRepository.insertDays(org.mockito.kotlin.any())).thenReturn(flowOf(Unit))
        viewModel = TrackerViewModel(mockDayRepository)
        advanceUntilIdle()

        // Ensure current week is selected (default behavior)
        val state = viewModel.trackerState.value as TrackerState.TrackerStateAll
        val currentSelectedMondayId = currentDt.withDayOfWeek(1).toId()
        assertEquals(currentSelectedMondayId, state.selectedMonday.id)

        // When
        val lastWeekDays = viewModel.getLastWeek()
        advanceUntilIdle()

        // Then
        assertEquals(7, lastWeekDays.size)
        val expectedLastWeekMondayId = currentDt.minusWeeks(1).withDayOfWeek(1).toId()
        assertEquals(expectedLastWeekMondayId, lastWeekDays[0].id) // First day of last week
        assertEquals(initialDays.first { it.id == expectedLastWeekMondayId }.drinks, lastWeekDays[0].drinks)

        val expectedLastWeekSundayId = currentDt.minusWeeks(1).withDayOfWeek(7).toId()
        assertEquals(expectedLastWeekSundayId, lastWeekDays[6].id) // Last day of last week
         assertEquals(initialDays.first { it.id == expectedLastWeekSundayId }.drinks, lastWeekDays[6].drinks)
    }

    @Test
    fun `refreshToday updates today's data and selectedDay if it was today`() = runTest {
        // Given
        val initialToday = Day(DateTime.now().toId(), drinks = 1.0)
        val initialDays = listOf(initialToday)
        `when`(mockDayRepository.getDays()).thenReturn(flowOf(initialDays)) // First call for init
        `when`(mockDayRepository.insertDays(org.mockito.kotlin.any())).thenReturn(flowOf(Unit))
        viewModel = TrackerViewModel(mockDayRepository)
        advanceUntilIdle()

        // Initially select today
        viewModel.onSetSelectedDay(initialToday) // Use the lambda
        advanceUntilIdle()
        val stateBeforeRefresh = viewModel.trackerState.value as TrackerState.TrackerStateAll
        assertEquals(initialToday.id, stateBeforeRefresh.selectedDay?.id)
        assertEquals(1.0, stateBeforeRefresh.today.drinks, 0.0)


        // New data for today comes from repository
        val refreshedToday = Day(DateTime.now().toId(), drinks = 2.0)
        `when`(mockDayRepository.getDays()).thenReturn(flowOf(listOf(refreshedToday))) // Subsequent calls for refreshToday

        // When
        viewModel.refreshToday()
        advanceUntilIdle()

        // Then
        val stateAfterRefresh = viewModel.trackerState.value as TrackerState.TrackerStateAll
        assertEquals(refreshedToday.id, stateAfterRefresh.today.id)
        assertEquals(2.0, stateAfterRefresh.today.drinks, 0.0)
        assertEquals(refreshedToday.id, stateAfterRefresh.selectedDay?.id) // SelectedDay should also be updated
        assertEquals(2.0, stateAfterRefresh.selectedDay?.drinks, 0.0)
    }

    @Test
    fun `updateDay updates repository and state`() = runTest {
        // Given
        val dayToUpdate = Day(DateTime.now().toId(), drinks = 1.0, cravings = 1)
        `when`(mockDayRepository.getDays()).thenReturn(flowOf(listOf(dayToUpdate)))
        `when`(mockDayRepository.insertDays(org.mockito.kotlin.any())).thenReturn(flowOf(Unit))
        `when`(mockDayRepository.updateDay(org.mockito.kotlin.any())).thenReturn(flowOf(Unit))
        viewModel = TrackerViewModel(mockDayRepository)
        advanceUntilIdle()

        val updatedDay = dayToUpdate.copy(drinks = 2.0, cravings = 3)

        // When
        viewModel.updateDay(updatedDay)
        advanceUntilIdle()

        // Then
        verify(mockDayRepository).updateDay(updatedDay)
        val state = viewModel.trackerState.value as TrackerState.TrackerStateAll
        assertEquals(updatedDay.id, state.today.id)
        assertEquals(2.0, state.today.drinks, 0.0)
        assertEquals(3, state.today.cravings)
        assertEquals(updatedDay.id, state.selectedDay?.id) // Assuming updateDay also sets selectedDay if it's today
    }

    @Test
    fun `onNavigateBack when not on current week resets to current week and returns true`() = runTest {
        // Given
        val currentDt = DateTime.now()
        val mondayThisWeekId = currentDt.withDayOfWeek(1).toId()
        val mondayLastWeek = Day(currentDt.minusWeeks(1).withDayOfWeek(1).toId())

        val initialDays = mutableListOf<Day>()
        var tempDate = currentDt.minusWeeks(2).withDayOfWeek(1) // Start two weeks ago
        for (i in 0..20) { // Three weeks of data
            initialDays.add(Day(tempDate.toId()))
            tempDate = tempDate.plusDays(1)
        }
        `when`(mockDayRepository.getDays()).thenReturn(flowOf(initialDays))
        `when`(mockDayRepository.insertDays(org.mockito.kotlin.any())).thenReturn(flowOf(Unit))
        viewModel = TrackerViewModel(mockDayRepository)
        advanceUntilIdle()

        // Navigate to last week (index 1, since weekBeginnings is reversed: current, last, two_weeks_ago)
        viewModel.onUpdateSelectedMonday(1)
        advanceUntilIdle()
        var state = viewModel.trackerState.value as TrackerState.TrackerStateAll
        assertEquals(mondayLastWeek.id, state.selectedMonday.id) // Verify we are on last week

        // When
        val handled = viewModel.onNavigateBack()
        advanceUntilIdle()

        // Then
        assertTrue(handled)
        state = viewModel.trackerState.value as TrackerState.TrackerStateAll
        assertEquals(mondayThisWeekId, state.selectedMonday.id) // Should be back to current week's Monday
    }

    @Test
    fun `onNavigateBack when on current week does nothing and returns false`() = runTest {
        // Given
        val currentDt = DateTime.now()
        val mondayThisWeekId = currentDt.withDayOfWeek(1).toId()

        val initialDays = mutableListOf(Day(mondayThisWeekId)) // Minimal data for current week
         `when`(mockDayRepository.getDays()).thenReturn(flowOf(initialDays))
        `when`(mockDayRepository.insertDays(org.mockito.kotlin.any())).thenReturn(flowOf(Unit))
        viewModel = TrackerViewModel(mockDayRepository)
        advanceUntilIdle()

        // Ensure we are on the current week's Monday
        var state = viewModel.trackerState.value as TrackerState.TrackerStateAll
        assertEquals(mondayThisWeekId, state.selectedMonday.id)

        // When
        val handled = viewModel.onNavigateBack()
        advanceUntilIdle()

        // Then
        assertFalse(handled)
        state = viewModel.trackerState.value as TrackerState.TrackerStateAll
        assertEquals(mondayThisWeekId, state.selectedMonday.id) // Should still be on current week's Monday
    }
}

// Minimal DayRepository mock for basic tests, can be expanded or use Mockito
class FakeDayRepository : DayRepository {
    private val daysFlow = MutableStateFlow<List<Day>>(emptyList())
    private var days = mutableListOf<Day>()

    override fun getDays(): Flow<List<Day>> = daysFlow

    override fun getDayById(id: String): Flow<Day?> {
        return flowOf(days.find { it.id == id })
    }

    override suspend fun insertDay(day: Day) {
        days.removeAll { it.id == day.id }
        days.add(day)
        daysFlow.value = days.toList()
    }

    override fun insertDays(daysToInsert: List<Day>): Flow<Unit> {
        val currentDays = days.toMutableList()
        daysToInsert.forEach { newDay ->
            val index = currentDays.indexOfFirst { it.id == newDay.id }
            if (index != -1) {
                currentDays[index] = newDay
            } else {
                currentDays.add(newDay)
            }
        }
        days = currentDays.sortedBy { it.id }.toMutableList() // Keep it sorted
        daysFlow.value = days.toList()
        return flowOf(Unit)
    }


    override fun updateDay(day: Day): Flow<Unit> {
        val index = days.indexOfFirst { it.id == day.id }
        if (index != -1) {
            days[index] = day
        } else {
            days.add(day)
            days.sortBy { it.id } // Keep it sorted
        }
        daysFlow.value = days.toList()
        return flowOf(Unit)
    }

    override suspend fun deleteDay(day: Day) {
        days.removeAll { it.id == day.id }
        daysFlow.value = days.toList()
    }

    fun clear() {
        days.clear()
        daysFlow.value = emptyList()
    }
}
