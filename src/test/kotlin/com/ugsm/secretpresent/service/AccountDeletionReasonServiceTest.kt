import com.ugsm.secretpresent.model.AccountDeletionReason
import com.ugsm.secretpresent.repository.AccountDeletionReasonRepository
import com.ugsm.secretpresent.service.AccountDeletionReasonService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class AccountDeletionReasonServiceTest {

    @InjectMocks
    lateinit var accountDeletionReasonService: AccountDeletionReasonService

    @Mock
    lateinit var accountDeletionReasonRepository: AccountDeletionReasonRepository

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `test findAll`() {
        val reasons = listOf<AccountDeletionReason>(
            AccountDeletionReason("Reason 1",1),
            AccountDeletionReason("Reason 2",2)
        )
        Mockito.`when`(accountDeletionReasonRepository.findAll()).thenReturn(reasons)

        val result = accountDeletionReasonService.findAll()

        assertNotNull(result)
        assertEquals(reasons.size, result.size)
    }
}
