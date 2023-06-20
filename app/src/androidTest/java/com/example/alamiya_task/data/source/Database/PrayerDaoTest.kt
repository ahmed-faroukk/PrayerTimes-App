package com.example.alamiya_task.data.source.Database
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.alamiya_task.data.model.PrayerTimeResponse
import com.example.alamiya_task.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@Suppress("DEPRECATION")
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class PrayerDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: PrayerDatabase
    private lateinit var dao: PrayerDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PrayerDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.getPrayerDao()
    }

    @After
    fun teardown() {
        database.close()
    }
    @Test
    fun savePrayersTimes() = runBlockingTest {
        val response = PrayerTimeResponse(1 , 550 , listOf() , "good")
        dao.savePrayersTimes(response)

        val table = dao.getAllPrayersTimes().getOrAwaitValue()

        assertThat(table).isEqualTo(response)
    }
    @Test
    fun deleteShoppingItem() = runBlockingTest {
        val response = PrayerTimeResponse(1 , 550 , listOf() , "good")
        dao.savePrayersTimes(response)
        dao.deleteAll()

        val allShoppingItems = dao.getAllPrayersTimes().getOrAwaitValue()

        assertThat(allShoppingItems).isNotEqualTo(response)
    }


}

