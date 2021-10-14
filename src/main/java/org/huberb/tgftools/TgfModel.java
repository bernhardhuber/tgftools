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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

/**
 * Encapsulate a model of TGF.
 *
 * <p>
 * A {@link TgfModel} consists of nodes {@link TgfNode}, and edges
 * {@link TgfEdge}.
 */
public class TgfModel implements Serializable {

    private static final long serialVersionUID = 20211001L;
    final LinkedHashMap<String, TgfNode> tgfNodeList;
    final List<TgfEdge> tgfEdgeList;

    TgfModel() {
        this.tgfNodeList = new LinkedHashMap<>();
        this.tgfEdgeList = new ArrayList<>();
    }

    void addNode(TgfNode tgfNode) {
        this.tgfNodeList.putIfAbsent(tgfNode.id, tgfNode);
    }

    void addEdge(TgfEdge tgfEdge) {
        this.tgfEdgeList.add(tgfEdge);
    }

    @Override
    public String toString() {
        return "TgfModel{" + "tgfNodeList=" + tgfNodeList + ", tgfEdgeList=" + tgfEdgeList + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.tgfNodeList);
        hash = 89 * hash + Objects.hashCode(this.tgfEdgeList);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TgfModel other = (TgfModel) obj;
        if (!Objects.equals(this.tgfNodeList, other.tgfNodeList)) {
            return false;
        }
        if (!Objects.equals(this.tgfEdgeList, other.tgfEdgeList)) {
            return false;
        }
        return true;
    }

    /**
     * Encapsulate a TGF node.
     */
    public static class TgfNode implements Serializable {

        private static final long serialVersionUID = 20211001L;
        final String id;
        final String name;

        public TgfNode(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        //---
        @Override
        public String toString() {
            return "TgfNode{" + "id=" + id + ", name=" + name + '}';
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 13 * hash + Objects.hashCode(this.id);
            hash = 13 * hash + Objects.hashCode(this.name);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final TgfNode other = (TgfNode) obj;
            if (!Objects.equals(this.id, other.id)) {
                return false;
            }
            if (!Objects.equals(this.name, other.name)) {
                return false;
            }
            return true;
        }
    }

    /*
     * Encapsulate a TGF edge.
     */
    public static class TgfEdge implements Serializable {

        private static final long serialVersionUID = 20211001L;
        final String from;
        final String to;
        final String label;

        public TgfEdge(String from, String to, String label) {
            this.from = from;
            this.to = to;
            this.label = label;
        }

        public String getFrom() {
            return from;
        }

        public String getTo() {
            return to;
        }

        public String getLabel() {
            return label;
        }

        //---
        @Override
        public String toString() {
            return "TgfEdge{" + "from=" + from + ", to=" + to + ", label=" + label + '}';
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 61 * hash + Objects.hashCode(this.from);
            hash = 61 * hash + Objects.hashCode(this.to);
            hash = 61 * hash + Objects.hashCode(this.label);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final TgfEdge other = (TgfEdge) obj;
            if (!Objects.equals(this.from, other.from)) {
                return false;
            }
            if (!Objects.equals(this.to, other.to)) {
                return false;
            }
            if (!Objects.equals(this.label, other.label)) {
                return false;
            }
            return true;
        }

    }

}
