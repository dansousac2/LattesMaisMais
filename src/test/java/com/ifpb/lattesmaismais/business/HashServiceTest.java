package com.ifpb.lattesmaismais.business;

import com.ifpb.lattesmaismais.presentation.exception.HashException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashServiceTest {

    private static HashService hashService;

    @BeforeAll
    public static void setUp() {
        hashService = new HashService();
    }

    @Test
    public void testHashingSHA256Ok() {
        String text = "teste";

        assertDoesNotThrow(() -> hashService.hashingSHA256(text));

        try {
            assertNotEquals(text, hashService.hashingSHA256(text));
        } catch (HashException e) {
            fail();
        }
    }
}
