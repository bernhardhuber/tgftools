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

import java.io.IOException;
import java.io.StringReader;
import org.huberb.tgftools.TgfConverters.CsvConverter;
import org.huberb.tgftools.TgfConverters.JsonConverter;
import org.huberb.tgftools.TgfConverters.PumlMindmapConverter;
import org.huberb.tgftools.TgfConverters.PumlNodeConverter;
import org.huberb.tgftools.TgfConverters.PumlWbsConverter;
import org.huberb.tgftools.TgfConverters.YamlConverter;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class TgfConvertersTest {

    private final boolean outputSystemOut = true;

    @Test
    public void testConvertToPuml_simple_tgf() throws IOException {

        final String tgf1 = ""
                + "1 A\n"
                + "2 B\n"
                + "#\n"
                + "1 2 a\n"
                + "";
        try (StringReader rr = new StringReader(tgf1)) {
            final TgfParser tgfParser = new TgfParser();
            final TgfModel tgfModel = tgfParser.parse(rr);
            final String pumlFromTgf = new PumlNodeConverter().convert(tgfModel);

            final String pumlFromTgfNormalized = pumlFromTgf.replace("\r", "").replace("\n", "");
            System_out_println(String.format("puml from tgf%n%s", pumlFromTgf));
            assertEquals("@startuml"
                    + "' nodes"
                    + "node \"A\" as 1"
                    + "node \"B\" as 2"
                    + "' edges"
                    + "1 --> 2 : a"
                    + "@enduml", pumlFromTgfNormalized);
        }
    }

    @Test
    public void testConvertToPumlMindmap_simple_tgf() throws IOException {

        final String tgf1 = ""
                + "1 A\n"
                + "2 B\n"
                + "#\n"
                + "1 2 a\n"
                + "";
        try (StringReader rr = new StringReader(tgf1)) {
            final TgfParser tgfParser = new TgfParser();
            final TgfModel tgfModel = tgfParser.parse(rr);
            final String pumlFromTgf = new PumlMindmapConverter().convert(tgfModel);

            final String pumlFromTgfNormalized = pumlFromTgf.replace("\r", "").replace("\n", "");
            System_out_println(String.format("puml mindmap from tgf%n%s", pumlFromTgf));
            assertEquals("@startmindmap"
                    + "* root"
                    + "** 1 A"
                    + "*** 2 B"
                    + "@endmindmap", pumlFromTgfNormalized);
        }
    }

    @Test
    public void testConvertToPumlWbs_simple_tgf() throws IOException {

        final String tgf1 = ""
                + "1 A\n"
                + "2 B\n"
                + "#\n"
                + "1 2 a\n"
                + "";
        try (StringReader rr = new StringReader(tgf1)) {
            final TgfParser tgfParser = new TgfParser();
            final TgfModel tgfModel = tgfParser.parse(rr);
            final String pumlFromTgf = new PumlWbsConverter().convert(tgfModel);

            final String pumlFromTgfNormalized = pumlFromTgf.replace("\r", "").replace("\n", "");
            System_out_println(String.format("puml wbs from tgf%n%s", pumlFromTgf));
            assertEquals("@startwbs"
                    + "* root"
                    + "** 1 A"
                    + "*** 2 B"
                    + "@endwbs", pumlFromTgfNormalized);
        }
    }

    @Test
    public void testConvertToPuml_simple_tgf_no_edge_label() throws IOException {

        final String tgf1 = ""
                + "1 A\n"
                + "2 B\n"
                + "#\n"
                + "1 2\n"
                + "";
        try (StringReader rr = new StringReader(tgf1)) {
            final TgfParser tgfParser = new TgfParser();
            final TgfModel tgfModel = tgfParser.parse(rr);
            final String pumlFromTgf = new PumlNodeConverter().convert(tgfModel);

            final String pumlFromTgfNormalized = pumlFromTgf.replace("\r", "").replace("\n", "");
            System_out_println(String.format("puml from tgf%n%s", pumlFromTgf));
            assertEquals("@startuml"
                    + "' nodes"
                    + "node \"A\" as 1"
                    + "node \"B\" as 2"
                    + "' edges"
                    + "1 --> 2"
                    + "@enduml", pumlFromTgfNormalized);
        }
    }

    @Test
    public void testConvertToCsv_simple_tgf() throws IOException {

        final String tgf1 = ""
                + "1 A\n"
                + "2 B\n"
                + "#\n"
                + "1 2 a\n"
                + "";
        try (StringReader rr = new StringReader(tgf1)) {
            final TgfParser tgfParser = new TgfParser();
            final TgfModel tgfModel = tgfParser.parse(rr);
            final String csvFromTgf = new CsvConverter().convert(tgfModel);

            final String csvFromTgfNormalized = csvFromTgf.replace("\r", "").replace("\n", "");
            System_out_println(String.format("csv from tgf%n%s", csvFromTgf));
            assertEquals("\"type\",\"id_from\",\"name_to\",\"label\""
                    + "\"node\",\"1\",\"A\",\"\""
                    + "\"node\",\"2\",\"B\",\"\""
                    + "\"edge\",\"1\",\"2\",\"a\"", csvFromTgfNormalized);
        }
    }

    @Test
    public void testConvertToJson_simple_tgf() throws IOException {

        final String tgf1 = ""
                + "1 A\n"
                + "2 B\n"
                + "#\n"
                + "1 2 a\n"
                + "";
        try (StringReader rr = new StringReader(tgf1)) {
            final TgfParser tgfParser = new TgfParser();
            final TgfModel tgfModel = tgfParser.parse(rr);
            final String jsonFromTgf = new JsonConverter().convert(tgfModel);

            final String jsonFromTgfNormalized = jsonFromTgf.replace("\r", "").replace("\n", "");
            System_out_println(String.format("json from tgf%n%s", jsonFromTgf));
            assertEquals("{\"nodes\": ["
                    + "{\"id\":\"1\",\"name\":\"A\"},"
                    + "{\"id\":\"2\",\"name\":\"B\"}"
                    + "],"
                    + "\"edges\": ["
                    + "{\"from\":\"1\",\"to\":\"2\",\"label\":\"a\"}"
                    + "]"
                    + "}", jsonFromTgfNormalized);
        }
    }

    @Test
    public void testConvertToYaml_simple_tgf() throws IOException {

        final String tgf1 = ""
                + "1 A\n"
                + "2 B\n"
                + "#\n"
                + "1 2 a\n"
                + "";
        try (StringReader rr = new StringReader(tgf1)) {
            final TgfParser tgfParser = new TgfParser();
            final TgfModel tgfModel = tgfParser.parse(rr);
            final String yamlFromTgf = new YamlConverter().convert(tgfModel);

            final String yamlFromTgfNormalized = yamlFromTgf.replace("\r", "").replace("\n", "");
            System_out_println(String.format("yaml from tgf%n%s", yamlFromTgf));
            assertEquals("## YAML Template."
                    + "---"
                    + "nodes:"
                    + "  -"
                    + "    id: \"1\""
                    + "    name: \"A\""
                    + "  -"
                    + "    id: \"2\""
                    + "    name: \"B\""
                    + "edges:"
                    + "  -"
                    + "    from: \"1\""
                    + "    to: \"2\""
                    + "    label: \"a\"", yamlFromTgfNormalized);
        }
    }

    private void System_out_println(String format) {
        if (outputSystemOut) {
            System.out.println(format);
        }
    }
}
