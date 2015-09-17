
package javajo.sample.tests.bulldog;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import javajo.sample.TestTime;

import static org.assertj.core.api.Assertions.assertThat;

public class ChihuahuaTest {

    @Rule
    public final TestTime testTime = new TestTime();

    @After
    public void teardown() {
        System.out.println(testTime.getStamp());
    }

    @Test
    public void testName() throws InterruptedException {
        Thread.sleep(1200l);
        assertThat(testTime.getTestName())
                .isEqualTo("testName");
    }

    @Test
    public void className() throws InterruptedException {
        Thread.sleep(1200l);
        assertThat(testTime.getClassName())
                .isEqualTo("javajo.sample.tests.bulldog.ChihuahuaTest");
    }

    @Test
    public void stamp() throws InterruptedException {
        Thread.sleep(1200l);
        assertThat(testTime.getStamp())
                .contains("javajo.sample.tests.bulldog.ChihuahuaTest#stamp");
    }
}
