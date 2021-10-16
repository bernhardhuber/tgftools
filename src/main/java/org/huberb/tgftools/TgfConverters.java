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

import org.huberb.tgftools.TgfModel.TgfEdge;
import org.huberb.tgftools.TgfModel.TgfNode;

/**
 * Conversion of {@link TgfModel}to various output formats.
 *
 * @author berni3
 */
public class TgfConverters {

    public static interface ITgfConverterToString {

        String convert(TgfModel tgfModel);
    }

    /**
     * Converts {@link TgfModel} to puml
     */
    public static class PumlConverter implements ITgfConverterToString {

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
                sb.append(String.format("node \"%s\" as %s%n", tgfNode.name, tgfNode.id));
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
        boolean result = s == null || (s != null && s.isEmpty());
        return result;
    }

    static boolean stringIsBlank(String s) {
        boolean result = s == null || (s != null && s.trim().isEmpty());
        return result;
    }

}
