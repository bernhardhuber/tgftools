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
                return "";
            }
        },
        puml {
            @Override
            String getExtension() {
                return ".puml";
            }
        },
        pumlMindmap {
            @Override
            String getExtension() {
                return ".puml";
            }
        },
        pumlWbs {
            @Override
            String getExtension() {
                return ".puml";
            }
        },
        csv {
            @Override
            String getExtension() {
                return ".csv";
            }
        },
        json {
            @Override
            String getExtension() {
                return ".json";
            }
        },
        yaml {
            @Override
            String getExtension() {
                return ".yaml";
            }
        },
        datalogValue {
            @Override
            String getExtension() {
                return ".dl";
            }
        },
        datalogProperty {
            @Override
            String getExtension() {
                return ".dl";
            }
        };

        abstract String getExtension();
    }

    public List<ConvertToFormat> createConvertToFormatList() {
        final Map<ConvertToFormat, Boolean> convertToFormatBooleanMap = createConvertToFormatMap();
        // filter where map.entry is true
        final List<ConvertToFormat> convertToFormatList = convertToFormatBooleanMap.entrySet().stream()
                .filter((e) -> e.getValue())
                .map((e) -> e.getKey())
                .collect(Collectors.toList());
        return convertToFormatList;
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
