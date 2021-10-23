/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.huberb.tgftools;

import org.huberb.tgftools.TgfModel.TgfEdge;
import org.huberb.tgftools.TgfModel.TgfNode;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
        TgfNode tgfNode = new TgfNode("id1", "name1");
        TgfModel instance = new TgfModel();
        instance.addNode(tgfNode);

        assertEquals(0, instance.tgfEdgeList.size());
        assertEquals(1, instance.tgfNodeList.size());
    }

    /**
     * Test of addEdge method, of class TgfModel.
     */
    @Test
    public void testAddEdge() {
        TgfEdge tgfEdge = new TgfEdge("from1", "to1", "label1");
        TgfModel instance = new TgfModel();
        instance.addEdge(tgfEdge);
        assertEquals(1, instance.tgfEdgeList.size());
        assertEquals(0, instance.tgfNodeList.size());
    }

    /**
     * Test of toString method, of class TgfModel.
     */
    @Test
    public void testToString() {
        TgfNode tgfNode = new TgfNode("id1", "name1");
        TgfEdge tgfEdge = new TgfEdge("from1", "to1", "label1");
        TgfModel instance = new TgfModel();
        instance.addNode(tgfNode);
        instance.addEdge(tgfEdge);
        String expResult = "TgfModel{"
                + "tgfNodeList="
                + "{"
                + "id1=TgfNode{id=id1, name=name1}"
                + "}, "
                + "tgfEdgeList="
                + "["
                + "TgfEdge{from=from1, to=to1, label=label1}"
                + "]"
                + "}";
        String result = instance.toString();
        assertEquals(expResult, result);
    }

    /**
     * Test of hashCode method, of class TgfModel.
     */
    @Test
    public void testHashCode() {
        TgfModel instance = new TgfModel();
        int result = instance.hashCode();
        assertNotEquals(0, result, "" + result);
    }

    /**
     * Test of equals method, of class TgfModel.
     */
    @Test
    public void testEquals() {
        Object obj = new TgfModel();
        TgfModel instance = new TgfModel();
        assertEquals(true, instance.equals(obj));
    }

}
