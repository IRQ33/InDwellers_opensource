import com.irq3.core.services.ConversationService
import com.irq3.core.services.RegisterResult
import com.irq3.core.services.UserService
import com.irq3.database.BannedPeopleDAO
import com.irq3.database.ConversationsDao
import com.irq3.database.UserDAO
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.mockStatic

class UserServiceTest {

    val userService = UserService(
        mock(UserDAO::class.java),
        banManager = mock(BannedPeopleDAO::class.java),
        conservationManager = mock(ConversationsDao::class.java),
        conversationService = mock(ConversationService::class.java)
    )
    @Test
    fun `create banned person`(){
        mockStatic(BannedPeopleDAO::class.java).use { dao ->
            dao.`when`<Pair<Boolean,String>>{ BannedPeopleDAO.isPersonBanned(1L) }
                .thenReturn(Pair(true,"test"))

            val result = userService.registerUser(1L)

            assert(result == RegisterResult.UserBanned("test"))
        }

    }

}