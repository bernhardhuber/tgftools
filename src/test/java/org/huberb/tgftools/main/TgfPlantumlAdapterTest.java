/*
 * Copyright 2024 pi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.huberb.tgftools.main;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.huberb.tgftools.main.TgfPlantumlAdapter.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *
 * @author pi
 */
public class TgfPlantumlAdapterTest {

    TgfPlantumlAdapter instance;

    @BeforeEach
    void setUp() {
        instance = new TgfPlantumlAdapter();
    }

    /**
     * Test of process method, of class TgfPlantumlAdapter.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testProcess_simple_valid_plantuml() throws Exception {
        String plantuml = "@startuml\n"
                + "Bob -> Alice\n"
                + "@enduml\n";
        Response response = instance.process(plantuml);
        String headers = response.getHeaderList().toString();
        String message = "mismatch " + headers;
        assertAll(
                () -> assertFalse(response.isError()),
                () -> assertTrue(headers.contains("status=HTTP/1.1 200"), message),
                () -> assertTrue(headers.contains("Content-length=2399"), message),
                () -> assertTrue(headers.contains("X-PlantUML-Diagram-Width=108"), message),
                () -> assertTrue(headers.contains("X-PlantUML-Diagram-Height=104"), message),
                () -> assertTrue(headers.contains("-PlantUML-Diagram-Description=(2 participants)"), message)
        );

        byte[] pngImageData = response.getContent();
        assertAll(
                () -> assertNotNull(pngImageData),
                () -> {
                    byte[] pngHeader = Arrays.copyOfRange(pngImageData, 1, 4);
                    String pngImageDataAsString = new String(pngHeader, StandardCharsets.UTF_8);
                    assertEquals("PNG", pngImageDataAsString);
                },
                () -> assertEquals(2399, pngImageData.length)
        );
    }

    @Test
    public void testProcess_simple_invalid_plantuml() throws Exception {
        String plantuml = "@startuml\n"
                + "Bob Bob -> Alice Alice\n"
                + "@enduml\n";
        Response response = instance.process(plantuml);
        String headers = response.getHeaderList().toString();
        String message = "mismatch " + headers;
        assertAll(
                () -> assertTrue(response.isError()),
                () -> assertTrue(headers.contains("status=HTTP/1.1 200"), message),
                () -> assertTrue(headers.contains("Content-length=23190"), message),
                () -> assertTrue(headers.contains("X-PlantUML-Diagram-Width=591"), message),
                () -> assertTrue(headers.contains("X-PlantUML-Diagram-Height=324"), message),
                () -> assertTrue(headers.contains("-PlantUML-Diagram-Description=(Error)"), message)
        );

        byte[] pngImageData = response.getContent();
        assertAll(
                () -> assertNotNull(pngImageData),
                () -> {
                    byte[] pngHeader = Arrays.copyOfRange(pngImageData, 1, 4);
                    String pngImageDataAsString = new String(pngHeader, StandardCharsets.UTF_8);
                    assertEquals("PNG", pngImageDataAsString);
                },
                () -> assertEquals(23190, pngImageData.length)
        );
    }

}
