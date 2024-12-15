/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.huberb.tgftools;

import org.huberb.tgftools.TgfModel.TgfEdge;
import org.huberb.tgftools.TgfModel.TgfNode;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class TgfModelTest {

    /**
     * Test of addNode method, of class TgfModel.
     */
    @Test
    public void testAddNode() {
        final TgfModel instance = new TgfModel.Builder()
                .node(new TgfNode("id1", "name1"))
                .build();

        assertEquals(0, instance.tgfEdgeList.size());
        assertEquals(1, instance.tgfNodeList.size());
    }

    /**
     * Test of addEdge method, of class TgfModel.
     */
    @Test
    public void testAddEdge() {
        final TgfModel instance = new TgfModel.Builder()
                .edge(new TgfEdge("from1", "to1", "label1"))
                .build();

        assertEquals(1, instance.tgfEdgeList.size());
        assertEquals(0, instance.tgfNodeList.size());
    }

    /**
     * Test of toString method, of class TgfModel.
     */
    @Test
    public void testToString() {
        final TgfModel instance = createSimpleTgfModel1();
        final String expResult = "TgfModel{"
                + "tgfNodeList="
                + "{"
                + "id1=TgfNode{id=id1, name=name1}"
                + "}, "
                + "tgfEdgeList="
                + "["
                + "TgfEdge{from=from1, to=to1, label=label1}"
                + "]"
                + "}";
        final String result = instance.toString();
        assertEquals(expResult, result);
    }

    /**
     * Test of hashCode method, of class TgfModel.
     */
    @Test
    public void testHashCode() {
        final Object obj = new TgfModel();
        final int resultObj = obj.hashCode();
        final TgfModel instance = createSimpleTgfModel1();
        final int resultInstance = instance.hashCode();
        assertNotEquals(0, resultInstance, "" + resultInstance);

        assertNotEquals(resultObj, resultInstance);
    }

    /**
     * Test of equals method, of class TgfModel.
     */
    @Test
    public void testEquals() {
        final Object obj = new TgfModel();
        final TgfModel instance = createSimpleTgfModel1();
        assertTrue(obj.equals(obj));
        assertTrue(instance.equals(instance));
        assertFalse(instance.equals(obj));
    }

    TgfModel createSimpleTgfModel1() {
        TgfModel instance = new TgfModel.Builder()
                .node(new TgfNode("id1", "name1"))
                .edge(new TgfEdge("from1", "to1", "label1"))
                .build();
        return instance;
    }
}
