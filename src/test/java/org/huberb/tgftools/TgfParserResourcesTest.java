/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.huberb.tgftools;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 *
 * @author berni3
 */
public class TgfParserResourcesTest {

    @ParameterizedTest
    @CsvSource(value = {
        "tgftools-dependency-tree.tgf, 10, 9",
        "evaluationtable-dependency-tree.tgf, 19, 18"
    })
    public void testParseResourcesFromClasspath(String resName, int countOfNodes, int countOfEdges) throws IOException {
        final TgfParser tgfParser = new TgfParser();
        try (final InputStream is = this.getClass().getClassLoader().getResourceAsStream(resName)) {
            assertNotNull(is, resName);
            try (final Reader rr = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                final TgfModel tgfModel = tgfParser.parse(rr);
                assertNotNull(tgfModel);
                final String m = "" + tgfModel.toString();
                assertFalse(tgfModel.tgfEdgeList.isEmpty(), m);
                assertFalse(tgfModel.tgfNodeList.isEmpty(), m);
                assertEquals(countOfNodes, tgfModel.tgfNodeList.size(), m);
                assertEquals(countOfEdges, tgfModel.tgfEdgeList.size(), m);
            }
        }
    }
}
