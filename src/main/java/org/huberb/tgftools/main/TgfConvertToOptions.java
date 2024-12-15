/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.huberb.tgftools.main;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import picocli.CommandLine.Option;

/**
 *
 * @author berni3
 */
public class TgfConvertToOptions {
    //---

    @Option(names = {"--convert-puml"},
            description = "convert TGF to puml")
    private boolean convertToPuml;
    @Option(names = {"--convert-puml-mindmap"},
            description = "convert TGF to puml mindmap")
    private boolean convertToPumlMindmap;
    @Option(names = {"--convert-puml-wbs"},
            description = "convert TGF to puml wbs")
    private boolean convertToPumlWbs;
    @Option(names = {"--convert-csv"},
            description = "convert TGF to csv")
    private boolean convertToCsv;
    @Option(names = {"--convert-json"},
            description = "convert TGF to json")
    private boolean convertToJson;
    @Option(names = {"--convert-yaml"},
            description = "convert TGF to yaml")
    private boolean convertToYaml;
    @Option(names = {"--convert-datalog-value"},
            description = "convert TGF to datalog value schema")
    private boolean convertToDatalogValue;
    @Option(names = {"--convert-datalog-property"},
            description = "convert TGF to datalog property schema")
    private boolean convertToDatalogProperty;

    //---
    public TgfConvertToOptions() {
        this(false, false, false,
                false,
                false,
                false,
                false, false);
    }

    public TgfConvertToOptions(
            boolean convertToPuml, boolean convertToPumlMindmap, boolean convertToPumlWbs,
            boolean convertToCsv,
            boolean convertToJson,
            boolean convertToYaml,
            boolean convertToDatalogValue,
            boolean convertToDatalogProperty) {
        this.convertToPuml = convertToPuml;
        this.convertToPumlMindmap = convertToPumlMindmap;
        this.convertToPumlWbs = convertToPumlWbs;
        this.convertToCsv = convertToCsv;
        this.convertToJson = convertToJson;
        this.convertToYaml = convertToYaml;
        this.convertToDatalogValue = convertToDatalogValue;
        this.convertToDatalogProperty = convertToDatalogProperty;
    }

    //---
    /**
     * Define supported conversion formats, and its extensions.
     */
    enum ConvertToFormat {
        noformat {
            @Override
            String getExtension() {
                return NO_EXTENSION;
            }
        },
        puml {
            @Override
            String getExtension() {
                return PUML_EXTENSION;
            }
        },
        pumlMindmap {
            @Override
            String getExtension() {
                return PUML_EXTENSION;
            }
        },
        pumlWbs {
            @Override
            String getExtension() {
                return PUML_EXTENSION;
            }

        },
        csv {
            @Override
            String getExtension() {
                return CSV_EXTENSION;
            }
        },
        json {
            @Override
            String getExtension() {
                return JSON_EXTENSION;
            }
        },
        yaml {
            @Override
            String getExtension() {
                return YAML_EXTENSION;
            }
        },
        datalogValue {
            @Override
            String getExtension() {
                return DL_EXTENSION;
            }
        },
        datalogProperty {
            @Override
            String getExtension() {
                return DL_EXTENSION;
            }
        };
        private static final String NO_EXTENSION = "";
        private static final String PUML_EXTENSION = ".puml";
        private static final String CSV_EXTENSION = ".csv";
        private static final String JSON_EXTENSION = ".json";
        private static final String YAML_EXTENSION = ".yaml";
        private static final String DL_EXTENSION = ".dl";

        abstract String getExtension();
    }

    List<ConvertToFormat> createConvertToFormatList() {
        final Map<ConvertToFormat, Boolean> convertToFormatBooleanMap = createConvertToFormatMap();
        // filter where map.entry is true
        return convertToFormatBooleanMap.entrySet().stream()
                .filter(e -> e.getValue())
                .map(e -> e.getKey())
                .collect(Collectors.toList());
    }

    /**
     * Create a map, mapping {@link ConvertToFormat} to value of convert-to
     * option.
     *
     * @return
     */
    Map<ConvertToFormat, Boolean> createConvertToFormatMap() {
        final Map<ConvertToFormat, Boolean> result = new LinkedHashMap<>();
        result.put(ConvertToFormat.puml, this.convertToPuml);
        result.put(ConvertToFormat.pumlMindmap, this.convertToPumlMindmap);
        result.put(ConvertToFormat.pumlWbs, this.convertToPumlWbs);
        result.put(ConvertToFormat.csv, this.convertToCsv);
        result.put(ConvertToFormat.json, this.convertToJson);
        result.put(ConvertToFormat.yaml, this.convertToYaml);
        result.put(ConvertToFormat.datalogValue, this.convertToDatalogValue);
        result.put(ConvertToFormat.datalogProperty, this.convertToDatalogProperty);
        return result;
    }
}
