package hu.bme.mit.spaceship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TorpedoStoreTest {

    @Test
    void fire_Success() {
        // Arrange
        TorpedoStore store = new TorpedoStore(1);

        // Act
        boolean result = store.fire(1);

        // Assert
        assertEquals(true, result);
    }

    @Test
    void fire_SingleTorpedo_DecreasesCount() {
        // Arrange: store with 10 torpedoes, 0% failure -> always succeeds
        TorpedoStore store = new TorpedoStore(10, 0.0);

        // Act
        boolean result = store.fire(1);

        // Assert
        assertTrue(result);
        assertEquals(9, store.getTorpedoCount());
    }

    @Test
    void fire_MultipleTorpedoes_DecreasesCountByAmount() {
        // Arrange
        TorpedoStore store = new TorpedoStore(10, 0.0);

        // Act
        boolean result = store.fire(4);

        // Assert
        assertTrue(result);
        assertEquals(6, store.getTorpedoCount());
    }

    @Test
    void fire_AllTorpedoes_StoreBecomesEmpty() {
        // Arrange
        TorpedoStore store = new TorpedoStore(5, 0.0);

        // Act
        boolean result = store.fire(5);

        // Assert
        assertTrue(result);
        assertEquals(0, store.getTorpedoCount());
        assertTrue(store.isEmpty());
    }

    @Test
    void fire_Failure_CountUnchanged() {
        // Arrange: 100% failure rate -> fire always fails
        TorpedoStore store = new TorpedoStore(10, 1.0);

        // Act
        boolean result = store.fire(1);

        // Assert
        assertFalse(result);
        assertEquals(10, store.getTorpedoCount());
    }

    @Test
    void fire_ZeroTorpedoes_ThrowsException() {
        // Arrange
        TorpedoStore store = new TorpedoStore(10, 0.0);

        // Act + Assert: numberOfTorpedos < 1 is illegal
        assertThrows(IllegalArgumentException.class, () -> store.fire(0));
        // state must be unchanged
        assertEquals(10, store.getTorpedoCount());
    }

    @Test
    void fire_NegativeTorpedoes_ThrowsException() {
        // Arrange
        TorpedoStore store = new TorpedoStore(10, 0.0);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> store.fire(-3));
        assertEquals(10, store.getTorpedoCount());
    }

    @Test
    void fire_MoreThanAvailable_ThrowsException() {
        // Arrange
        TorpedoStore store = new TorpedoStore(5, 0.0);

        // Act + Assert: numberOfTorpedos > torpedoCount is illegal
        assertThrows(IllegalArgumentException.class, () -> store.fire(6));
        assertEquals(5, store.getTorpedoCount());
    }

    @Test
    void isEmpty_NewStoreWithTorpedoes_ReturnsFalse() {
        // Arrange
        TorpedoStore store = new TorpedoStore(3, 0.0);

        // Act + Assert
        assertFalse(store.isEmpty());
    }

    @Test
    void isEmpty_EmptyStore_ReturnsTrue() {
        // Arrange
        TorpedoStore store = new TorpedoStore(0);

        // Act + Assert
        assertTrue(store.isEmpty());
    }

    @Test
    void getTorpedoCount_ReturnsInitialCount() {
        // Arrange
        TorpedoStore store = new TorpedoStore(7, 0.0);

        // Act + Assert
        assertEquals(7, store.getTorpedoCount());
    }

    @Test
    void constructor_SingleArg_DefaultsToNoFailure() {
        // Arrange: single-arg constructor (no IVT_RATE env var set) -> FAILURE_RATE = 0.0
        TorpedoStore store = new TorpedoStore(2);

        // Act: firing should succeed deterministically
        boolean first = store.fire(1);
        boolean second = store.fire(1);

        // Assert
        assertTrue(first);
        assertTrue(second);
        assertEquals(0, store.getTorpedoCount());
    }
}
