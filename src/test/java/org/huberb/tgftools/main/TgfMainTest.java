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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import picocli.CommandLine;

/**
 *
 * @author berni3
 */
public class TgfMainTest {

    @ParameterizedTest
    @ValueSource(strings = {"--help", "-h"})
    public void testCommandLine_help(String helpOption) {
        final TgfMain app = new TgfMain();
        final CommandLine cmd = new CommandLine(app);

        final StringWriter swOut = new StringWriter();
        cmd.setOut(new PrintWriter(swOut));

        final StringWriter swErr = new StringWriter();
        cmd.setErr(new PrintWriter(swErr));
        //---
        final int exitCode = cmd.execute(helpOption);
        assertEquals(0, exitCode);
        assertEquals("", swErr.toString(), "stderr");
        final String swErrAsString = swOut.toString();
        final String m = String.format("stdout helpOption %s, stderr: %s", helpOption, swErrAsString);
        assertNotEquals(0, swErrAsString, m);
        assertTrue(swErrAsString.contains("Usage:"), m);
        assertTrue(swErrAsString.contains("--convert-"), m);
        assertTrue(swErrAsString.contains("-h"), m);
        assertTrue(swErrAsString.contains("--help"), m);
        assertTrue(swErrAsString.contains("-V"), m);
        assertTrue(swErrAsString.contains("--version"), m);
    }

    @Test
    public void testCommandLine_convert_csv() {
        final TgfMain app = new TgfMain();
        final CommandLine cmd = new CommandLine(app);

        final StringWriter swOut = new StringWriter();
        cmd.setOut(new PrintWriter(swOut));

        final StringWriter swErr = new StringWriter();
        cmd.setErr(new PrintWriter(swErr));
        //---
        final File tgfInputFile = new File("./src/test/resources/", "tgftools-dependency-tree.tgf");
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
            final String m = String.format("commandline %s, stderr: %s", commandline, swOutAsString);
            assertNotEquals(0, swOutAsString, m);
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
}
