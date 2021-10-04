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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import org.huberb.tgftools.TgfConverters.CsvConverter;
import org.huberb.tgftools.TgfConverters.JsonConverter;
import org.huberb.tgftools.TgfConverters.PumlConverter;
import org.huberb.tgftools.TgfConverters.YamlConverter;
import org.huberb.tgftools.TgfMain.ConvertToFormat;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Help.Ansi;
import picocli.CommandLine.Option;

/**
 * Simple commandline application for reading and converting TGF files.
 *
 * @author berni3
 */
@Command(name = "tgfMain",
        mixinStandardHelpOptions = true,
        version = "tgfMain 1.0-SNAPSHOT",
        description = "parse, and convert TGF file format")
public class TgfMain implements Callable<Integer> {

    @Option(names = {"-f", "--file"},
            description = "read from TGF file, if not specified read TGF from stdin")
    private File tgfFile;

    @Option(names = {"-o", "--output"},
            description = "write to file, if not specified write to stdout")
    private File outputFile;
    @Option(names = {"--overwrite-outpufile"},
            description = "overwrite existing output file")
    private boolean overwriteOutputfile;

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

    /**
     * Commandline entry point.
     *
     * @param args
     */
    public static void main(String[] args) {
        int exitCode = new CommandLine(new TgfMain()).execute(args);
        System.exit(exitCode);
    }

    /**
     * Picocli entry point.
     *
     * @return exit-code
     * @throws Exception
     */
    @Override
    public Integer call() throws Exception {
        final TgfParser tgfParser = new TgfParser();
        try (final Reader tgfReader = new ReaderFactory(tgfFile).createUtf8Reader()) {
            final TgfModel tgfModel = tgfParser.parse(tgfReader);
            convertTgfModel(tgfModel);
        }
        return 0;
    }

    /**
     * Define supported conversion formats.
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

    /**
     * Create a map, mapping {@link ConvertToFormat} to value of convert
     * commandline option.
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

    String evaluteWriteToFilenameOrStdin() {
        final String result = Optional.ofNullable(this.tgfFile)
                .map((f) -> f.toString())
                .orElse("stdin");
        return result;
    }

    /**
     * Convert, and write {@link TgfModel}.
     *
     * @param tgfModel
     */
    void convertTgfModel(TgfModel tgfModel) {

        final Map<ConvertToFormat, Boolean> convertToFormatBooleanMap = createConvertToFormatMap();
        // filter where map.entry is true
        final List<ConvertToFormat> convertToFormatList = convertToFormatBooleanMap.entrySet().stream()
                .filter((e) -> e.getValue())
                .map((e) -> e.getKey())
                .collect(Collectors.toList());
        if (convertToFormatList.isEmpty()) {
            String str = Ansi.AUTO.string("@|bold No|@ conversion option specified.");
            System.err.println(str);
            return;
        }
        //---
        Map<ConvertToFormat, Optional<File>> outputFileList = xxx(convertToFormatList);

        //---
        for (ConvertToFormat convertToFormat : convertToFormatList) {

            final Optional<File> outputFile = outputFileList.get(convertToFormat);
            if (!this.overwriteOutputfile && outputFile.isPresent() && outputFile.get().exists()) {
                String str = Ansi.AUTO.string(String.format("Output file %s already exists, don't overwrite it.", outputFile.toString()));
                System.err.println(str);
                continue;
            }
            final String conversionResult;
            if (convertToFormat == ConvertToFormat.puml) {
                conversionResult = new PumlConverter().convert(tgfModel);
            } else if (convertToFormat == ConvertToFormat.csv) {
                conversionResult = new CsvConverter().convert(tgfModel);
            } else if (convertToFormat == ConvertToFormat.json) {
                conversionResult = new JsonConverter().convert(tgfModel);
            } else if (convertToFormat == ConvertToFormat.yaml) {
                conversionResult = new YamlConverter().convert(tgfModel);
            } else {
                conversionResult = null;
            }
            if (conversionResult != null) {
                System.err.println(String.format(">>> file: %s, format: %s",
                        evaluteWriteToFilenameOrStdin(),
                        convertToFormat));
                if (!outputFile.isPresent()) {
                    System.out.println(conversionResult);
                } else {
                    try (final FileOutputStream fos = new FileOutputStream(outputFile.get());
                            final OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8)) {
                        writer.write(conversionResult);
                    } catch (IOException ioex) {
                        System.err.println(String.format("Cannot write to file: %s:%n %s",
                                outputFile.get(),
                                ioex.getMessage()
                        ));
                    }
                }
            }
        }
    }

    Map<ConvertToFormat, Optional<File>> xxx(List<ConvertToFormat> convertToFormatList) {
        int numberOfConversion = convertToFormatList.size();
        // define outputFile(s)
        final Map<ConvertToFormat, Optional<File>> convertToOutputFiles = new HashMap<>();
        for (ConvertToFormat convertToFormat : convertToFormatList) {
            if (this.outputFile != null) {
                if (numberOfConversion > 1) {
                    String extension = convertToFormat.getExtension();
                    File f = new File(this.outputFile.toString() + extension);
                    convertToOutputFiles.put(convertToFormat, Optional.of(f));
                } else {
                    convertToOutputFiles.put(convertToFormat, Optional.of(this.outputFile));
                }
            } else {
                convertToOutputFiles.put(convertToFormat, Optional.empty());
            }
        }
        return convertToOutputFiles;
    }

    /**
     * Factory for creating a {@link Reader}.
     */
    static class ReaderFactory {

        private final File f;

        public ReaderFactory(File f) {
            this.f = f;
        }

        Reader createUtf8Reader() throws FileNotFoundException {
            final Reader r;
            if (f != null) {
                final FileInputStream fis = new FileInputStream(this.f);
                final InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
                r = isr;
            } else {
                final InputStream is = new java.io.BufferedInputStream(System.in);
                final InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
                r = isr;
            }
            return r;
        }
    }
}
