import Controlador.MotionProcessor;
import Modelo.SensorData;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MotionProcessorIntegrationTest {

    @Test
    void testProcessMotionIntegration() {
        SensorData data = new SensorData();
        MotionProcessor processor = new MotionProcessor();

        // simulate acceleration input
        data.setAxFiltered(1.0f);
        data.setAyFiltered(0.5f);
        data.setAzFiltered(-0.2f);
        data.setTimestamp(System.currentTimeMillis());

        // first call initializes lastTime, displacements should be 0
        processor.process(data);
        assertEquals(0.0f, data.getDx());
        assertEquals(0.0f, data.getDy());
        assertEquals(0.0f, data.getDz());

        // wait 100ms and simulate next reading
        try { Thread.sleep(100); } catch (InterruptedException e) {}

        data.setAxFiltered(1.0f);
        data.setAyFiltered(0.5f);
        data.setAzFiltered(-0.2f);
        data.setTimestamp(System.currentTimeMillis());

        processor.process(data);

        // After integration, displacements should be non-zero
        assertTrue(data.getDx() > 0);
        assertTrue(data.getDy() > 0);
        assertTrue(data.getDz() < 0);
    }
}
