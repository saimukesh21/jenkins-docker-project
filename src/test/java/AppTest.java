import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppTest {
    @Test
    void shouldReturnApplicationMessage() {
        assertEquals("Hello Docker CI/CD!", App.getMessage());
    }
}