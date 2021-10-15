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
package org.huberb.tgftools.main;

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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import org.huberb.tgftools.TgfConverters.CsvConverter;
import org.huberb.tgftools.TgfConverters.JsonConverter;
import org.huberb.tgftools.TgfConverters.PumlConverter;
import org.huberb.tgftools.TgfConverters.YamlConverter;
import org.huberb.tgftools.TgfModel;
import org.huberb.tgftools.TgfParser;
import org.huberb.tgftools.main.TgfConvertToOptions.ConvertToFormat;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Help.Ansi;
import picocli.CommandLine.Mixin;
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
    @Option(names = {"--overwrite-outputfile"},
            description = "overwrite existing output file")
    private boolean overwriteOutputfile;

    @Mixin
    private TgfConvertToOptions tgfConvertToOptions;

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
        final List<ConvertToFormat> convertToFormatList = tgfConvertToOptions.createConvertToFormatList();
        if (convertToFormatList.isEmpty()) {
            final String str = Ansi.AUTO.string("@|bold No|@ conversion option specified.");
            System.err.println(str);
            return;
        }
        //---
        final Map<ConvertToFormat, Optional<File>> outputFileList = createMappingConvertToFormatToOutputFile(convertToFormatList);

        //---
        for (ConvertToFormat convertToFormat : convertToFormatList) {

            final Optional<File> outputFile = outputFileList.get(convertToFormat);
            if (!this.overwriteOutputfile && outputFile.isPresent() && outputFile.get().exists()) {
                final String str = Ansi.AUTO.string(
                        String.format("Output file %s already exists, don't overwrite it.", outputFile.toString())
                );
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

    /**
     * Map {@link ConvertToFormat} to a conversion output file.
     *
     * @param convertToFormatList
     * @return
     */
    Map<ConvertToFormat, Optional<File>> createMappingConvertToFormatToOutputFile(List<ConvertToFormat> convertToFormatList) {
        final int numberOfConversion = convertToFormatList.size();
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
     *
     * <p>
     * Use this reader for reading TGF data.
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
