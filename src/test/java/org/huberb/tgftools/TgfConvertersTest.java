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
import java.util.function.UnaryOperator;
import org.huberb.tgftools.TgfConverters.CsvConverter;
import org.huberb.tgftools.TgfConverters.DatalogPropertySchemaConverter;
import org.huberb.tgftools.TgfConverters.DatalogValueSchemaConverter;
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

    static final String tgf1 = ""
            + "1 A\n"
            + "2 B\n"
            + "#\n"
            + "1 2 a\n"
            + "";
    static final String tgf2 = ""
            + "1 A\n"
            + "2 B\n"
            + "#\n"
            + "1 2\n"
            + "";
    static final UnaryOperator<String> normalizeF = s -> s.replace("\r", "").replace("\n", "");

    @Test
    public void testPumlNodeConverter_simple_tgf() throws IOException {
        try (StringReader rr = new StringReader(tgf1)) {
            final TgfParser tgfParser = new TgfParser();
            final TgfModel tgfModel = tgfParser.parse(rr);
            final String pumlFromTgf = new PumlNodeConverter().convert(tgfModel);

            final String pumlFromTgfNormalized = normalizeF.apply(pumlFromTgf);
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
    public void testPumlMindmapConverter_simple_tgf() throws IOException {
        try (StringReader rr = new StringReader(tgf1)) {
            final TgfParser tgfParser = new TgfParser();
            final TgfModel tgfModel = tgfParser.parse(rr);
            final String pumlFromTgf = new PumlMindmapConverter().convert(tgfModel);

            final String pumlFromTgfNormalized = normalizeF.apply(pumlFromTgf);
            System_out_println(String.format("puml mindmap from tgf%n%s", pumlFromTgf));
            assertEquals("@startmindmap"
                    + "* root"
                    + "** 1 A"
                    + "*** 2 B"
                    + "@endmindmap", pumlFromTgfNormalized);
        }
    }

    @Test
    public void testPumlWbsConverter_simple_tgf() throws IOException {
        try (StringReader rr = new StringReader(tgf1)) {
            final TgfParser tgfParser = new TgfParser();
            final TgfModel tgfModel = tgfParser.parse(rr);
            final String pumlFromTgf = new PumlWbsConverter().convert(tgfModel);

            final String pumlFromTgfNormalized = normalizeF.apply(pumlFromTgf);
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

        try (StringReader rr = new StringReader(tgf2)) {
            final TgfParser tgfParser = new TgfParser();
            final TgfModel tgfModel = tgfParser.parse(rr);
            final String pumlFromTgf = new PumlNodeConverter().convert(tgfModel);

            final String pumlFromTgfNormalized = normalizeF.apply(pumlFromTgf);
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
    public void testCsvConverter_simple_tgf() throws IOException {
        try (StringReader rr = new StringReader(tgf1)) {
            final TgfParser tgfParser = new TgfParser();
            final TgfModel tgfModel = tgfParser.parse(rr);
            final String csvFromTgf = new CsvConverter().convert(tgfModel);

            final String csvFromTgfNormalized = normalizeF.apply(csvFromTgf);
            System_out_println(String.format("csv from tgf%n%s", csvFromTgf));
            assertEquals("\"type\",\"id_from\",\"name_to\",\"label\""
                    + "\"node\",\"1\",\"A\",\"\""
                    + "\"node\",\"2\",\"B\",\"\""
                    + "\"edge\",\"1\",\"2\",\"a\"", csvFromTgfNormalized);
        }
    }

    @Test
    public void testJsonConverter_simple_tgf() throws IOException {
        try (StringReader rr = new StringReader(tgf1)) {
            final TgfParser tgfParser = new TgfParser();
            final TgfModel tgfModel = tgfParser.parse(rr);
            final String jsonFromTgf = new JsonConverter().convert(tgfModel);

            final String jsonFromTgfNormalized = normalizeF.apply(jsonFromTgf);
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
        try (StringReader rr = new StringReader(tgf1)) {
            final TgfParser tgfParser = new TgfParser();
            final TgfModel tgfModel = tgfParser.parse(rr);
            final String yamlFromTgf = new YamlConverter().convert(tgfModel);

            final String yamlFromTgfNormalized = normalizeF.apply(yamlFromTgf);
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

    @Test
    public void testDatalogValueSchemaConverter_simple_tgf() throws IOException {
        try (StringReader rr = new StringReader(tgf1)) {
            final TgfParser tgfParser = new TgfParser();
            final TgfModel tgfModel = tgfParser.parse(rr);
            final String datalogFromTgf = new DatalogValueSchemaConverter().convert(tgfModel);

            final String datalogFromTgfNormalized = normalizeF.apply(datalogFromTgf);
            System_out_println(String.format("DatalogValueSchemaConverter from tgf%n%s", datalogFromTgf));
            assertEquals("% start"
                    + "% nodes"
                    + "node(\"1\",\"A\")."
                    + "node(\"2\",\"B\")."
                    + "% edges"
                    + "edge(\"1\", \"2\")."
                    + "edgeLabel(\"1\", \"2\", \"a\")."
                    + ""
                    + "% end", datalogFromTgfNormalized);
        }
    }

    @Test
    public void testDatalogPropertySchemaConverter_simple_tgf() throws IOException {
        try (StringReader rr = new StringReader(tgf1)) {
            final TgfParser tgfParser = new TgfParser();
            final TgfModel tgfModel = tgfParser.parse(rr);
            final String datalogFromTgf = new DatalogPropertySchemaConverter().convert(tgfModel);

            final String datalogFromTgfNormalized = normalizeF.apply(datalogFromTgf);
            System_out_println(String.format("DatalogPropertySchema from tgf%n%s", datalogFromTgf));
            assertEquals("% start"
                    + "% nodes"
                    + "tgfdata(\"1\", instanceof, \"node\")."
                    + "tgfdata(\"1\", name, \"A\")."
                    + "tgfdata(\"2\", instanceof, \"node\")."
                    + "tgfdata(\"2\", name, \"B\")."
                    + "% edges"
                    + "tgfdata(\"1\", edge, \"2\")."
                    + "tgfdata(\"1-2\", instanceof, \"edge\")."
                    + "tgfdata(\"1-2\", from, \"1\")."
                    + "tgfdata(\"1-2\", to, \"2\")."
                    + "tgfdata(\"1-2\", label, \"a\")."
                    + ""
                    + "% end", datalogFromTgfNormalized);
        }
    }

    private void System_out_println(String format) {
        if (outputSystemOut) {
            System.out.println(format);
        }
    }
}
