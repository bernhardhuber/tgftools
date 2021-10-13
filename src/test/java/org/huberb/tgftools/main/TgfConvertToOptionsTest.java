/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.huberb.tgftools.main;

import org.huberb.tgftools.main.TgfConvertToOptions;
import java.util.EnumSet;
import java.util.List;
import org.huberb.tgftools.main.TgfConvertToOptions.ConvertToFormat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class TgfConvertToOptionsTest {

    /**
     * Test of createConvertToFormatList method, of class TgfConvertToOptions.
     */
    @Test
    public void testCreateConvertToFormatList() {
        final TgfConvertToOptions instance = new TgfConvertToOptions();
        final List<ConvertToFormat> result = instance.createConvertToFormatList();
        assertEquals(0, result.size());
    }

    /**
     * Test of createConvertToFormatList method, of class TgfConvertToOptions.
     */
    @Test
    public void testCreateConvertToFormatList_all_values() {
        final TgfConvertToOptions instance = new TgfConvertToOptions(true, true, true, true);
        final List<ConvertToFormat> result = instance.createConvertToFormatList();
        assertEquals(4, result.size());
        // assert result contains all ConvertToFormat values except noformat
        EnumSet.allOf(ConvertToFormat.class)
                .stream()
                .filter((convertToFormatSet) -> convertToFormatSet != ConvertToFormat.noformat)
                .forEach((convertToFormatSet) -> {
                    assertTrue(result.contains(convertToFormatSet), "" + convertToFormatSet);
                });
    }
}
