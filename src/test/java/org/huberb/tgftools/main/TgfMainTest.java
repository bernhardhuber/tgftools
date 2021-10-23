/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.huberb.tgftools.main;

import java.io.PrintWriter;
import java.io.StringWriter;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        cmd.setOut(new PrintWriter(swErr));
        final int exitCode = cmd.execute(helpOption);
        assertEquals(0, exitCode);
        assertEquals("", swOut.toString());
        final String swErrAsString = swErr.toString();
        final String m = String.format("helpOption %s, stderr: %s", helpOption, swErrAsString);
        assertNotEquals(0, swErrAsString, m);
        assertTrue(swErrAsString.contains("Usage:"), m);
        assertTrue(swErrAsString.contains("--convert-"), m);
        assertTrue(swErrAsString.contains("-h"), m);
        assertTrue(swErrAsString.contains("--help"), m);
        assertTrue(swErrAsString.contains("-V"), m);
        assertTrue(swErrAsString.contains("--version"), m);
    }
}
