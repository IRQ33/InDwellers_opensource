import com.irq3.discord.utils.LimitArray
import org.junit.jupiter.api.Test
import kotlin.collections.listOf

class LimitArrayTests {

    @Test
    fun testLimitArrayWorking(){
        val data = LimitArray<Int>(3)
        data.addElement(3)
        data.addElement(2)
        data.addElement(5)
        data.addElement(4)
        assert(data.toList() == listOf(2,5,4))
    }

    @Test
    fun testLimitArrayWorking2(){
        val data = LimitArray<Int>(3)
        data.addElement(3)
        data.addElement(2)
        data.addElement(5)
        data.addElement(4)
        data.addElement(70)
        data.addElement(32)
        assert(data.toList() == listOf(4,70,32))
    }



}