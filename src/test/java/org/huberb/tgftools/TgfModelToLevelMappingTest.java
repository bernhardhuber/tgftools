/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.huberb.tgftools;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.Optional;
import org.huberb.tgftools.TgfConverters.TgfModelToLevelMapping;
import org.huberb.tgftools.TgfModel.TgfNode;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class TgfModelToLevelMappingTest {

    @Test
    public void testCalculateNodeLevel() throws IOException {

        final String tgf1 = ""
                + "1 A\n"
                + "2 B\n"
                + "#\n"
                + "1 2 a\n"
                + "";
        try (StringReader rr = new StringReader(tgf1)) {
            final TgfParser tgfParser = new TgfParser();
            final TgfModel tgfModel = tgfParser.parse(rr);

            final TgfModelToLevelMapping tgfModelToLevelMapping = new TgfModelToLevelMapping(tgfModel);

            final Map<String, Integer> result = tgfModelToLevelMapping.calculateNodeLevel();
            assertNotNull(result);
            final String m = String.format("result %s", result.toString());
            assertEquals(3, result.size(), m);
            assertEquals(0, result.getOrDefault("@root@", -1), m);
            assertEquals(1, result.getOrDefault("1", -1), m);
            assertEquals(2, result.getOrDefault("2", -1), m);

            assertEquals(-1, xxx(tgfModel, result, "@root@"), m);
            assertEquals(1, xxx(tgfModel, result, "A"), m);
            assertEquals(2, xxx(tgfModel, result, "B"), m);
        }
    }

    Integer xxx(TgfModel tgfModel, Map<String, Integer> m, String name) {
        Integer level = -1;
        final Optional<TgfNode> tgfNode = tgfModel.tgfNodeList.values().stream()
                .filter((n) -> name.equals(n.name))
                .findFirst();
        if (tgfNode.isPresent()) {
            String tgfNodeId = tgfNode.get().id;
            level = m.getOrDefault(tgfNodeId, -1);
        }
        return level;
    }
}
