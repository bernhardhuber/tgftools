/*
 * Copyright 2021 berni3.
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
package org.huberb.tgftools;

import org.huberb.tgftools.TgfParser.ParsingTgfStatus;
import org.huberb.tgftools.TgfParser.TgfToken;
import org.huberb.tgftools.TgfParser.TgfTokenValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 *
 * @author berni3
 */
public class TgfParserTest {

    @Test
    public void testTokenize_node() {
        final TgfParser tgfParser = new TgfParser();
        TgfTokenValue tgfToken;
        tgfToken = tgfParser.tokenize(ParsingTgfStatus.parsingNodes, "id1 nodeLabel1");
        assertEquals(TgfToken.node, tgfToken.token);
        assertEquals("id1", tgfToken.tgfNode.getId());
        assertEquals("nodeLabel1", tgfToken.tgfNode.getName());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "id1 nodeLabel1",
        " id1 nodeLabel1",
        " id1 nodeLabel1 ",
        "id1  nodeLabel1",})
    public void testTokenize_node_with_label_X(String nodeLine) {
        final TgfParser tgfParser = new TgfParser();
        TgfTokenValue tgfToken;
        tgfToken = tgfParser.tokenize(ParsingTgfStatus.parsingNodes, nodeLine);
        assertEquals(TgfToken.node, tgfToken.token);
        assertEquals("id1", tgfToken.tgfNode.getId());
        assertEquals("nodeLabel1", tgfToken.tgfNode.getName());
    }

    @Test
    public void testTokenize_edge() {
        final TgfParser tgfParser = new TgfParser();
        TgfTokenValue tgfToken;
        tgfToken = tgfParser.tokenize(ParsingTgfStatus.parsingEdges, "fromNode1 toNode1 edgeLabel1");
        assertEquals(TgfToken.edge, tgfToken.token);
        assertEquals("fromNode1", tgfToken.tgfEdge.getFrom());
        assertEquals("toNode1", tgfToken.tgfEdge.getTo());
        assertEquals("edgeLabel1", tgfToken.tgfEdge.getLabel());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "id1",
        " id1",
        " id1 ",})
    public void testTokenize_node_wo_label_X(String nodeLine) {
        final TgfParser tgfParser = new TgfParser();
        TgfTokenValue tgfToken;
        tgfToken = tgfParser.tokenize(ParsingTgfStatus.parsingNodes, nodeLine);
        assertEquals(TgfToken.node, tgfToken.token);
        assertEquals("id1", tgfToken.tgfNode.getId());
        assertEquals("", tgfToken.tgfNode.getName());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "fromNode1 toNode1 edgeLabel1",
        " fromNode1 toNode1 edgeLabel1",
        " fromNode1 toNode1 edgeLabel1 ",
        "fromNode1  toNode1  edgeLabel1"})
    public void testTokenize_edge_with_edgeLabel_X(String edgeLine) {
        final TgfParser tgfParser = new TgfParser();
        TgfTokenValue tgfToken;
        tgfToken = tgfParser.tokenize(ParsingTgfStatus.parsingEdges, edgeLine);
        assertEquals(TgfToken.edge, tgfToken.token);
        assertEquals("fromNode1", tgfToken.tgfEdge.getFrom());
        assertEquals("toNode1", tgfToken.tgfEdge.getTo());
        assertEquals("edgeLabel1", tgfToken.tgfEdge.getLabel());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "fromNode1 toNode1",
        " fromNode1 toNode1",
        " fromNode1 toNode1 ",
        "fromNode1  toNode1",})
    public void testTokenize_edge_wo_edgeLabel_X(String edgeLine) {
        final TgfParser tgfParser = new TgfParser();
        TgfTokenValue tgfToken;
        tgfToken = tgfParser.tokenize(ParsingTgfStatus.parsingEdges, edgeLine);
        assertEquals(TgfToken.edge, tgfToken.token);
        assertEquals("fromNode1", tgfToken.tgfEdge.getFrom());
        assertEquals("toNode1", tgfToken.tgfEdge.getTo());
        assertEquals("", tgfToken.tgfEdge.getLabel());
    }

}
