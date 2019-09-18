import com.codurance.training.tasks.actions.HelpAction
import io.mockk.spyk
import io.mockk.verifyOrder
import org.junit.jupiter.api.Test
import java.io.PrintWriter

internal class HelpUnitTest {

    private val output = spyk(PrintWriter(System.out))
    private val action = HelpAction(output)

    @Test
    fun help() {
        action.execute()

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
