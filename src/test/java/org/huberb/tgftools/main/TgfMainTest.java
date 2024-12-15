/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.huberb.tgftools.main;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import picocli.CommandLine;

/**
 *
 * @author berni3
 */
public class TgfMainTest {

    TgfMain app;
    CommandLine cmd;
    StringWriter swOut;
    StringWriter swErr;

    static Function<String, File> createFileFrom = s -> {
        final File infile = new File("./src/test/resources/", s);
        String m = "" + infile;
        assertTrue(infile.exists(), m);
        assertTrue(infile.canRead(), m);
        return infile;
    };

    @BeforeEach
    public void setUp() {
        app = new TgfMain();
        cmd = new CommandLine(app);

        swOut = new StringWriter();
        cmd.setOut(new PrintWriter(swOut));

        swErr = new StringWriter();
        cmd.setErr(new PrintWriter(swErr));
        //---
    }

    @ParameterizedTest
    @ValueSource(strings = {"--help", "-h"})
    public void testCommandLine_help(String helpOption) {
        //---
        final int exitCode = cmd.execute(helpOption);
        assertEquals(0, exitCode);
        assertEquals("", swErr.toString(), "stderr");
        final String swOutAsString = swOut.toString();
        final String m = String.format("stdout helpOption %s, stdout: %s", helpOption, swOutAsString);
        assertTrue(swOutAsString.contains("Usage:"), m);
        assertTrue(swOutAsString.contains("--convert-"), m);
        assertTrue(swOutAsString.contains("-h"), m);
        assertTrue(swOutAsString.contains("--help"), m);
        assertTrue(swOutAsString.contains("-V"), m);
        assertTrue(swOutAsString.contains("--version"), m);
    }

    @Test
    public void testCommandLine_convert_csv() {
        final File tgfInputFile = createFileFrom.apply("tgftools-dependency-tree.tgf");
        final List<String> commandline = Arrays.asList(
                "--convert-csv",
                String.format("--file=%s", tgfInputFile.getAbsolutePath())
        );

        final int exitCode = cmd.execute(commandline.toArray(new String[0]));
        assertEquals(0, exitCode);
        {
            final String swErrAsString = swErr.toString();
            final String m = "" + swErrAsString;
            assertTrue(swErrAsString.contains("tgftools-dependency-tree.tgf, format: csv"), m);
        }
        {
            final String swOutAsString = swOut.toString();
            final String m = String.format("commandline %s, stdout: %s", commandline, swOutAsString);
            assertTrue(swOutAsString.contains("\"type\","
                    + "\"id_from\","
                    + "\"name_to\","
                    + "\"label\""), m);
            assertTrue(swOutAsString.contains("\"node\","), m);
            assertTrue(swOutAsString.contains("\"edge\""), m);
            assertTrue(swOutAsString.contains("compile"), m);
            assertTrue(swOutAsString.contains("test"), m);
            assertTrue(swOutAsString.contains("\"compile\""), m);
            assertTrue(swOutAsString.contains("\"test\""), m);
        }
    }

    @Test
    public void testCommandLine_convert_json() {
        final File tgfInputFile = createFileFrom.apply("tgftools-dependency-tree.tgf");
        final List<String> commandline = Arrays.asList(
                "--convert-json",
                String.format("--file=%s", tgfInputFile.getAbsolutePath())
        );

        final int exitCode = cmd.execute(commandline.toArray(new String[0]));
        assertEquals(0, exitCode);
        {
            final String swErrAsString = swErr.toString();
            final String m = "" + swErrAsString;
            assertTrue(swErrAsString.contains("tgftools-dependency-tree.tgf, format: json"), m);
        }
        {
            final String swOutAsString = swOut.toString();
            final String m = String.format("commandline %s, stdout: %s", commandline, swOutAsString);
            assertTrue(swOutAsString.contains("\"nodes\""), m);
            assertTrue(swOutAsString.contains("\"edges\""), m);
            assertTrue(swOutAsString.contains("node"), m);
            assertTrue(swOutAsString.contains("edge"), m);
            assertTrue(swOutAsString.contains("compile"), m);
            assertTrue(swOutAsString.contains("test"), m);
            assertTrue(swOutAsString.contains("\"compile\""), m);
            assertTrue(swOutAsString.contains("\"test\""), m);
        }
    }

    @Test
    public void testCommandLine_convert_yaml() {
        final File tgfInputFile = createFileFrom.apply("tgftools-dependency-tree.tgf");
        final List<String> commandline = Arrays.asList(
                "--convert-yaml",
                String.format("--file=%s", tgfInputFile.getAbsolutePath())
        );

        final int exitCode = cmd.execute(commandline.toArray(new String[0]));
        assertEquals(0, exitCode);
        {
            final String swErrAsString = swErr.toString();
            final String m = "" + swErrAsString;
            assertTrue(swErrAsString.contains("tgftools-dependency-tree.tgf, format: yaml"), m);
        }
        {
            final String swOutAsString = swOut.toString();
            final String m = String.format("commandline %s, stderr: %s", commandline, swOutAsString);
            assertTrue(swOutAsString.contains("nodes:"), m);
            assertTrue(swOutAsString.contains("edges:"), m);
            assertTrue(swOutAsString.contains("\"compile\""), m);
            assertTrue(swOutAsString.contains("\"test\""), m);
        }
    }

    @Test
    public void testCommandLine_convert_puml() {
        final File tgfInputFile = createFileFrom.apply("tgftools-dependency-tree.tgf");
        final List<String> commandline = Arrays.asList(
                "--convert-puml",
                String.format("--file=%s", tgfInputFile.getAbsolutePath())
        );

        final int exitCode = cmd.execute(commandline.toArray(new String[0]));
        assertEquals(0, exitCode);
        {
            final String swErrAsString = swErr.toString();
            final String m = "" + swErrAsString;
            assertTrue(swErrAsString.contains("tgftools-dependency-tree.tgf, format: puml"), m);
        }
        {
            final String swOutAsString = swOut.toString();
            final String m = String.format("commandline %s, stderr: %s", commandline, swOutAsString);
            assertTrue(swOutAsString.contains("@startuml"), m);
            assertTrue(swOutAsString.contains("@enduml"), m);
            assertTrue(swOutAsString.contains("nodes"), m);
            assertTrue(swOutAsString.contains("node"), m);
            assertTrue(swOutAsString.contains("edges"), m);
            assertTrue(swOutAsString.contains("edge"), m);
            assertTrue(swOutAsString.contains("compile"), m);
            assertTrue(swOutAsString.contains("test"), m);
        }
    }

    @Test
    public void testCommandLine_convert_puml_mindmap() {
        final File tgfInputFile = createFileFrom.apply("tgftools-dependency-tree.tgf");
        final List<String> commandline = Arrays.asList(
                "--convert-puml-mindmap",
                String.format("--file=%s", tgfInputFile.getAbsolutePath())
        );

        final int exitCode = cmd.execute(commandline.toArray(new String[0]));
        assertEquals(0, exitCode);
        {
            final String swErrAsString = swErr.toString();
            final String m = "" + swErrAsString;
            assertTrue(swErrAsString.contains("tgftools-dependency-tree.tgf, format: puml"), m);
        }
        {
            final String swOutAsString = swOut.toString();
            final String m = String.format("commandline %s, stderr: %s", commandline, swOutAsString);
            assertTrue(swOutAsString.contains("@startmindmap"), m);
            assertTrue(swOutAsString.contains("@endmindmap"), m);
            assertTrue(swOutAsString.contains("* root"), m);
            assertTrue(swOutAsString.contains("**"), m);
            assertTrue(swOutAsString.contains("***"), m);
            assertTrue(swOutAsString.contains("compile"), m);
            assertTrue(swOutAsString.contains("test"), m);
        }
    }

    @Test
    public void testCommandLine_convert_puml_wbs() {
        final File tgfInputFile = createFileFrom.apply("tgftools-dependency-tree.tgf");
        final List<String> commandline = Arrays.asList(
                "--convert-puml-wbs",
                String.format("--file=%s", tgfInputFile.getAbsolutePath())
        );

        final int exitCode = cmd.execute(commandline.toArray(new String[0]));
        assertEquals(0, exitCode);
        {
            final String swErrAsString = swErr.toString();
            final String m = "" + swErrAsString;
            assertTrue(swErrAsString.contains("tgftools-dependency-tree.tgf, format: puml"), m);
        }
        {
            final String swOutAsString = swOut.toString();
            final String m = String.format("commandline %s, stderr: %s", commandline, swOutAsString);
            assertTrue(swOutAsString.contains("@startwbs"), m);
            assertTrue(swOutAsString.contains("@endwbs"), m);
            assertTrue(swOutAsString.contains("* root"), m);
            assertTrue(swOutAsString.contains("**"), m);
            assertTrue(swOutAsString.contains("***"), m);
            assertTrue(swOutAsString.contains("compile"), m);
            assertTrue(swOutAsString.contains("test"), m);
        }
    }

}
