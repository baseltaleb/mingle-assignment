package tech.mingle.basel.assignment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import tech.mingle.basel.assignment.api.AssignmentService
import tech.mingle.basel.assignment.data.dao.AssignmentDao
import tech.mingle.basel.assignment.data.repository.AssignmentRepository

@RunWith(JUnit4::class)
class RepositoryTest {
    private val dao = mock(AssignmentDao::class.java)
    private val service = mock(AssignmentService::class.java)
    private val repo = AssignmentRepository(service, dao)

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun loadData() = runBlocking {
        val result = repo.loadInData()
        assert(result.isSuccess)
    }
}