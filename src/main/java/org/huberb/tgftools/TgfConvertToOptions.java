/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.huberb.tgftools;

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
    @Option(names = {"--convert-csv"},
            description = "convert TGF to csv")
    private boolean convertToCsv;
    @Option(names = {"--convert-json"},
            description = "convert TGF to json")
    private boolean convertToJson;
    @Option(names = {"--convert-yaml"},
            description = "convert TGF to yaml")
    private boolean convertToYaml;

    //---
    public TgfConvertToOptions() {
        this(false, false, false, false);
    }

    public TgfConvertToOptions(boolean convertToPuml, boolean convertToCsv, boolean convertToJson, boolean convertToYaml) {
        this.convertToPuml = convertToPuml;
        this.convertToCsv = convertToCsv;
        this.convertToJson = convertToJson;
        this.convertToYaml = convertToYaml;
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
        result.put(ConvertToFormat.csv, this.convertToCsv);
        result.put(ConvertToFormat.json, this.convertToJson);
        result.put(ConvertToFormat.yaml, this.convertToYaml);
        return result;
    }
}
