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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.huberb.tgftools.TgfModel.TgfEdge;
import org.huberb.tgftools.TgfModel.TgfNode;

/**
 * Conversion of {@link TgfModel}to various output formats.
 *
 * @author berni3
 */
public class TgfConverters {

    public interface ITgfConverterToString {

        String convert(TgfModel tgfModel);
    }

    /**
     * Converts {@link TgfModel} to puml node diagram
     */
    public static class PumlNodeConverter implements ITgfConverterToString {

        final String umlTgfNodeElement = "node";

        /**
         * Convert {@link TgfModel} to plant uml.
         *
         * @param tgfModel
         * @return
         */
        public String convert(TgfModel tgfModel) {
            final StringBuilder sb = new StringBuilder();
            sb.append(String.format("@startuml%n%n"));
            // nodes
            sb.append(String.format("' nodes%n"));
            tgfModel.tgfNodeList.values().forEach(tgfNode -> {
                sb.append(String.format("%s \"%s\" as %s%n", umlTgfNodeElement, tgfNode.name, tgfNode.id));
            });
            // edges
            sb.append(String.format("' edges%n"));
            tgfModel.tgfEdgeList.forEach(tgfEdge -> {
                if (stringIsBlank(tgfEdge.label)) {
                    sb.append(String.format("%s --> %s%n", tgfEdge.from, tgfEdge.to));
                } else {
                    sb.append(String.format("%s --> %s : %s%n", tgfEdge.from, tgfEdge.to, tgfEdge.label));
                }
            });
            sb.append(String.format("%n@enduml%n"));
            return sb.toString();
        }
    }

    /**
     * Converts {@link TgfModel} to puml mindmap diagram
     */
    public static class PumlMindmapConverter extends PumlMindmapWbsConverter {

        public PumlMindmapConverter() {
            super("@startmindmap", "@endmindmap");
        }
    }

    /**
     * Converts {@link TgfModel} to puml wbs diagram
     */
    public static class PumlWbsConverter extends PumlMindmapWbsConverter {

        public PumlWbsConverter() {
            super("@startwbs", "@endwbs");
        }

    }

    /**
     * Converts {@link TgfModel} to puml mindmap or wbs diagram
     */
    abstract static class PumlMindmapWbsConverter implements ITgfConverterToString {

        private final String startElement;
        private final String endElement;

        public PumlMindmapWbsConverter(String startElement, String endElement) {
            this.startElement = startElement;
            this.endElement = endElement;
        }

        /**
         * Convert {@link TgfModel} to plant uml mindmap.
         *
         * @param tgfModel
         * @return
         */
        @Override
        public String convert(TgfModel tgfModel) {
            final TgfModelToLevelMapping tgfModelToLevelMapping = new TgfModelToLevelMapping(tgfModel);
            final Map<String, Integer> m = tgfModelToLevelMapping.calculateNodeLevel();

            final StringBuilder sb = new StringBuilder();
            sb.append(String.format("%s%n%n", startElement));

            sb.append(String.format("%s%n", "* root"));

            m.entrySet().stream()
                    .sorted((e1, e2) -> e1.getValue().compareTo(e2.getValue()))
                    .forEach((Entry<String, Integer> e) -> {
                        TgfNode tgfNodeOpt = tgfModel.tgfNodeList.get(e.getKey());
                        if (tgfNodeOpt != null) {
                            final TgfNode tgfNodeFount = tgfNodeOpt;
                            final Integer level = m.getOrDefault(tgfNodeFount.id, -1);
                            if (level > 0) {
                                final String levelAsStringBuilder = levelStars(level + 1);
                                sb.append(String.format("%s %s %s%n",
                                        levelAsStringBuilder,
                                        tgfNodeFount.id,
                                        tgfNodeFount.name)
                                );
                            }
                        }
                    }
                    );
            sb.append(String.format("%n%s%n", endElement));
            return sb.toString();
        }

        protected String levelStars(int level) {
            final StringBuilder levelAsStringBuilder = new StringBuilder();
            for (int i = 1; i <= level; i++) {
                levelAsStringBuilder.append("*");
            }
            return levelAsStringBuilder.toString();
        }
    }

    /**
     * Converts {@link TgfModel} to csv
     */
    public static class CsvConverter implements ITgfConverterToString {

        /**
         * Convert {@link TgfModel} to csv.
         *
         * @param tgfModel
         * @return
         */
        public String convert(TgfModel tgfModel) {
            final StringBuilder sb = new StringBuilder();
            sb.append(String.format("\"type\",\"id_from\",\"name_to\",\"label\"%n"));
            // nodes
            tgfModel.tgfNodeList.values().forEach(tgfNode -> {
                sb.append(String.format("\"node\","
                        + "\"%s\","
                        + "\"%s\","
                        + "\"\"%n", tgfNode.id, tgfNode.name));
            });
            // edges
            tgfModel.tgfEdgeList.forEach(tgfEdge -> {
                sb.append(String.format("\"edge\","
                        + "\"%s\","
                        + "\"%s\","
                        + "\"%s\"%n", tgfEdge.from, tgfEdge.to, tgfEdge.label));
            });
            return sb.toString();
        }
    }

    /**
     * Converts {@link TgfModel} to json
     */
    public static class JsonConverter implements ITgfConverterToString {

        /**
         * Convert {@link TgfModel} to json.
         *
         * @param tgfModel
         * @return
         */
        @Override
        public String convert(TgfModel tgfModel) {
            final StringBuilder sb = new StringBuilder();
            sb.append(String.format("{%n"));
            // nodes
            sb.append(String.format("\"nodes\": [%n"));
            {
                int i = 0;
                for (TgfNode tgfNode : tgfModel.tgfNodeList.values()) {
                    if (i > 0) {
                        sb.append(String.format(",%n"));
                    }
                    sb.append(String.format("{"
                            + "\"id\":\"%s\","
                            + "\"name\":\"%s\""
                            + "}", tgfNode.id, tgfNode.name));
                    i += 1;
                }
            }
            sb.append(String.format("%n],%n"));
            // edges
            sb.append(String.format("\"edges\": [%n"));
            {
                int i = 0;
                for (TgfEdge tgfEdge : tgfModel.tgfEdgeList) {
                    if (i > 0) {
                        sb.append(String.format(",%n"));
                    }
                    sb.append(String.format("{"
                            + "\"from\":\"%s\","
                            + "\"to\":\"%s\","
                            + "\"label\":\"%s\""
                            + "}", tgfEdge.from, tgfEdge.to, tgfEdge.label));
                    i += 1;
                }
            }
            sb.append(String.format("%n]%n"));
            sb.append(String.format("}%n"));
            return sb.toString();
        }
    }

    /**
     * Converts {@link TgfModel} to yaml
     */
    public static class YamlConverter implements ITgfConverterToString {

        /**
         * Convert {@link TgfModel} to json.
         *
         * @param tgfModel
         * @return
         */
        @Override
        public String convert(TgfModel tgfModel) {
            final StringBuilder sb = new StringBuilder();
            sb.append(String.format("" + "## YAML Template.%n" + "---%n"));
            // nodes
            sb.append(String.format("nodes:%n"));
            {
                for (TgfNode tgfNode : tgfModel.tgfNodeList.values()) {
                    sb.append(String.format("  -%n"));
                    sb.append(String.format(""
                            + "    id: \"%s\"%n"
                            + "    name: \"%s\"%n"
                            + "", tgfNode.id, tgfNode.name));
                }
            }
            // edges
            sb.append(String.format("edges:%n"));
            {
                for (TgfEdge tgfEdge : tgfModel.tgfEdgeList) {
                    sb.append(String.format("  -%n"));
                    sb.append(String.format(""
                            + "    from: \"%s\"%n"
                            + "    to: \"%s\"%n"
                            + "    label: \"%s\"%n"
                            + "", tgfEdge.from, tgfEdge.to, tgfEdge.label));
                }
            }
            return sb.toString();
        }
    }

    static boolean stringIsEmpty(String s) {
        return s == null || (s != null && s.isEmpty());
    }

    static boolean stringIsBlank(String s) {
        return s == null || (s != null && s.trim().isEmpty());
    }

    static class TgfModelToLevelMapping {

        final TgfModel tgfModel;

        public TgfModelToLevelMapping(TgfModel tgfModel) {

            this.tgfModel = tgfModel;
        }

        Map<String, Integer> calculateNodeLevel() {
            final Map<String, Integer> tgfNodeLevelMap = new LinkedHashMap<>();

            final TgfNode root = new TgfNode("@root@", "@root@");
            tgfNodeLevelMap.put(root.id, 0);
            for (TgfNode tgfNode : tgfModel.tgfNodeList.values()) {
                tgfNodeLevelMap.put(tgfNode.id, 1);
            }
            for (TgfEdge tgfEdge : tgfModel.tgfEdgeList) {
                final String fromNodeId = tgfEdge.from;
                final String toNodeId = tgfEdge.to;
                final Integer fromNodeLevel = tgfNodeLevelMap.get(fromNodeId);
                final Integer toNodeLevel = tgfNodeLevelMap.get(toNodeId);

                final Integer newToNodeLevel = fromNodeLevel + 1;
                if (newToNodeLevel > toNodeLevel) {
                    tgfNodeLevelMap.put(toNodeId, newToNodeLevel);
                }
            }
            return tgfNodeLevelMap;
        }
    }

    /**
     * Converts {@link TgfModel} to datalog format.
     */
    public static class DatalogValueSchemaConverter implements ITgfConverterToString {

        final String predicateNode = "node";
        final String predicateEdge = "edge";
        final String predicateEdgeLabel = "edgeLabel";

        /**
         * Convert {@link TgfModel} to plant datalog property value.
         *
         * @param tgfModel
         * @return
         */
        public String convert(TgfModel tgfModel) {
            final StringBuilder sb = new StringBuilder();
            sb.append(String.format("%% start%n%n"));
            // nodes
            sb.append(String.format("%% nodes%n"));
            tgfModel.tgfNodeList.values().forEach(tgfNode -> {
                sb.append(String.format("%s(\"%s\",\"%s\").%n", predicateNode, tgfNode.id, tgfNode.name));
            });
            // edges
            sb.append(String.format("%% edges%n"));
            tgfModel.tgfEdgeList.forEach(tgfEdge -> {
                if (stringIsBlank(tgfEdge.label)) {
                    sb.append(String.format("%s(\"%s\", \"%s\").%n", predicateEdge, tgfEdge.from, tgfEdge.to));
                } else {
                     sb.append(String.format("%s(\"%s\", \"%s\").%n", predicateEdge, tgfEdge.from, tgfEdge.to));
                    sb.append(String.format("%s(\"%s\", \"%s\", \"%s\").%n", predicateEdgeLabel, tgfEdge.from, tgfEdge.to, tgfEdge.label));
                }
            });
             sb.append(String.format("%n%% end%n"));
            return sb.toString();
        }
    }

    /**
     * Converts {@link TgfModel} to datalog format.
     */
    public static class DatalogPropertySchemaConverter implements ITgfConverterToString {

        final String predicateData = "tgfdata";

        /**
         * Convert {@link TgfModel} to plant datalog property value.
         *
         * @param tgfModel
         * @return
         */
        public String convert(TgfModel tgfModel) {
            final StringBuilder sb = new StringBuilder();
            sb.append(String.format("%% start%n%n"));
            // nodes
            sb.append(String.format("%% nodes%n"));
            tgfModel.tgfNodeList.values().forEach(tgfNode -> {
                sb.append(String.format("%s(\"%s\", instanceof, \"%s\").%n", predicateData, tgfNode.id, "node"));
                sb.append(String.format("%s(\"%s\", name, \"%s\").%n", predicateData, tgfNode.id, tgfNode.name));

            });
            // edges
            sb.append(String.format("%% edges%n"));
            tgfModel.tgfEdgeList.forEach(tgfEdge -> {
                sb.append(String.format("%s(\"%s\", edge, \"%s\").%n", predicateData, tgfEdge.from, tgfEdge.to));
                String edgeId = tgfEdge.from + "-" + tgfEdge.to;
                if (stringIsBlank(tgfEdge.label)) {
                    sb.append(String.format("%s(\"%s\", instanceof, \"%s\").%n", predicateData, edgeId, "edge"));
                    sb.append(String.format("%s(\"%s\", from, \"%s\").%n", predicateData, edgeId, tgfEdge.from));
                    sb.append(String.format("%s(\"%s\", to, \"%s\").%n", predicateData, edgeId, tgfEdge.to));
                } else {
                    sb.append(String.format("%s(\"%s\", instanceof, \"%s\").%n", predicateData, edgeId, "edge"));
                    sb.append(String.format("%s(\"%s\", from, \"%s\").%n", predicateData, edgeId, tgfEdge.from));
                    sb.append(String.format("%s(\"%s\", to, \"%s\").%n", predicateData, edgeId, tgfEdge.to));
                    sb.append(String.format("%s(\"%s\", label, \"%s\").%n", predicateData, edgeId, tgfEdge.label));
                }
            });
            sb.append(String.format("%n%% end%n"));
            return sb.toString();
        }
    }
}
