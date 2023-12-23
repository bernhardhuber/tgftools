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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import org.huberb.tgftools.TgfModel.TgfEdge;
import org.huberb.tgftools.TgfModel.TgfNode;

/**
 *
 * <pre><code>
 * file := node_list
 *    '#'
 *    edge_list
 *
 * node_list := node_item node_list | node_item | []
 * node_item := node_id ' ' [node_name]
 * edge_list := edge_item edge_list | edge_item
 * edge_item := source_node_id ' ' target_node_id ' ' [edge_name]
 * source_node_id := node_id
 * target_node_id := node_id
 * </pre></code>
 * <p>
 * An simple example with 2 nodes, and 1 edge is the following:
 * <pre><code>
 * 1 Alice
 * 2 Bob
 * #
 * 2 1 hello
 * </pre></code>
 *
 * @author berni3
 */
public class TgfParser {

    /**
     * Definition of parsing states
     */
    enum ParsingTgfStatus {
        start,
        parsingNodes,
        parsingEdges,
        end
    }

    /**
     * Current parsing state.
     */
    private ParsingTgfStatus parsingTgfStatus = ParsingTgfStatus.start;
    /**
     * Tokens for line comments.
     */
    final List<String> commentsList = Arrays.asList("--", "'");

    /**
     * Parse tgf creating a {@link TgfModel}.
     *
     * @param rr
     * @return
     * @throws IOException
     */
    public TgfModel parse(Reader rr) throws IOException {
        final TgfModel tgfModel = new TgfModel();

        parsingTgfStatus = ParsingTgfStatus.parsingNodes;

        try (BufferedReader r = new BufferedReader(rr)) {
            for (String line; (line = r.readLine()) != null;) {
                final TgfTokenValue tgfToken = tokenize(parsingTgfStatus, line);
                if (tgfToken.token == TgfToken.empty) {
                    continue;
                } else if (tgfToken.token == TgfToken.hashMark) {
                    switch (parsingTgfStatus) {
                        case parsingNodes:
                            parsingTgfStatus = ParsingTgfStatus.parsingEdges;
                            break;
                        default:
                        // noop
                    }
                } else if (tgfToken.token == TgfToken.node) {
                    tgfModel.addNode(tgfToken.tgfNode);
                } else if (tgfToken.token == TgfToken.edge) {
                    tgfModel.addEdge(tgfToken.tgfEdge);
                } else {
                    // noop
                }
            }
            return tgfModel;
        }
    }

    /**
     * Definition of tokens.
     */
    enum TgfToken {
        empty,
        node,
        hashMark,
        edge

    }

    /**
     * Encapsulate a tgf TgfToken and its value
     */
    static class TgfTokenValue {

        final TgfToken token;
        final TgfEdge tgfEdge;
        final TgfNode tgfNode;

        public TgfTokenValue(TgfToken token) {
            this(token, null, null);
        }

        public TgfTokenValue(TgfNode tgfNode) {
            this(TgfToken.node, tgfNode, null);
        }

        public TgfTokenValue(TgfEdge tgfEdge) {
            this(TgfToken.edge, null, tgfEdge);
        }

        private TgfTokenValue(TgfToken token, TgfNode tgfNode, TgfEdge tgfEdge) {
            this.token = token;
            this.tgfNode = tgfNode;
            this.tgfEdge = tgfEdge;
        }

    }

    /**
     * Tokenize a single input line.
     *
     * @param parsingTgfStatus current parsing state
     * @param line tokenize this line
     * @return {@link TgfTokenValue} of a line
     */
    TgfTokenValue tokenize(ParsingTgfStatus parsingTgfStatus, String line) {
        final TgfToken token;
        TgfEdge tgfEdge = null;
        TgfNode tgfNode = null;
        String trimmedLine = line.trim();

        if (trimmedLine.isEmpty()
                || commentsList.contains(trimmedLine)) {
            token = TgfToken.empty;
        } else if (trimmedLine.startsWith("#")) {
            token = TgfToken.hashMark;
        } else {
            switch (parsingTgfStatus) {
                // nodeId nodeLabel
                case parsingNodes: {
                    final String[] splitted = trimmedLine.split(" +", 2);
                    final String nodeId = splitted[0].trim();
                    final String nodeLabel;
                    if (splitted.length > 1) {
                        nodeLabel = splitted[1].trim();
                    } else {
                        nodeLabel = "";
                    }
                    token = TgfToken.node;
                    tgfNode = new TgfNode(nodeId, nodeLabel);
                }
                break;
                case parsingEdges: // fromNodeId toNodeId [label]
                {
                    final String[] split = trimmedLine.split(" +", 3);
                    final String fromNodeId = split[0].trim();
                    final String toNodeId = split[1].trim();
                    final String edgeLabel;
                    if (split.length > 2) {
                        edgeLabel = split[2].trim();
                    } else {
                        edgeLabel = "";
                    }
                    token = TgfToken.edge;
                    tgfEdge = new TgfEdge(fromNodeId, toNodeId, edgeLabel);
                }
                break;
                default:
                    token = TgfToken.empty;
            }
        }
        final TgfTokenValue result;
        switch (token) {
            case empty:
                result = new TgfTokenValue(token);
                break;
            case hashMark:
                result = new TgfTokenValue(token);
                break;
            case node:
                result = new TgfTokenValue(tgfNode);
                break;
            case edge:
                result = new TgfTokenValue(tgfEdge);
                break;
            default:
                result = new TgfTokenValue(TgfToken.empty);
                break;
        }
        return result;
    }

}
