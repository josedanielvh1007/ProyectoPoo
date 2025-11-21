import Modelo.SensorData;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SensorDataTest {

    @Test
    void testSetAndGetRollPitchYaw() {
        SensorData data = new SensorData();
        data.setRoll(10.5f);
        data.setPitch(-3.2f);
        data.setYaw(45.0f);

        assertEquals(10.5f, data.getRoll());
        assertEquals(-3.2f, data.getPitch());
        assertEquals(45.0f, data.getYaw());
    }

    @Test
    void testTapFlags() {
        SensorData data = new SensorData();
        data.setPressed(true);
        data.setShaken(true);
        data.setPressType("single");

        assertTrue(data.isPressed());
        assertTrue(data.isShaken());
        assertEquals("single", data.getPressType());
    }

    @Test
    void testDisplacement() {
        SensorData data = new SensorData();
        data.setDx(1.0f);
        data.setDy(2.0f);
        data.setDz(3.0f);

        assertEquals(1.0f, data.getDx());
        assertEquals(2.0f, data.getDy());
        assertEquals(3.0f, data.getDz());
    }
}
