import com.codurance.training.tasks.Action
import com.codurance.training.tasks.HelpAction
import com.codurance.training.tasks.TaskList
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifyOrder
import org.junit.jupiter.api.Test
import java.io.BufferedReader
import java.io.PrintWriter

internal class HelpUnitTest {

    private val output = spyk(PrintWriter(System.out))
    private val service = HelpAction(output)

    @Test
    fun help() {
        service.execute(null)

        verifyOrder {
            output.println("Commands:")
            output.println("  show")
            output.println("  add project <project name>")
            output.println("  add task <project name> <task description>")
            output.println("  check <task ID>")
            output.println("  uncheck <task ID>")
            output.println()
        }
    }
}
