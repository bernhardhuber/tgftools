# tgftools

## Overview

A simple command line tool reading and converting TGF data to

* PLANTUML
* CSV
* JSON
* YAML

Following command line options are aveailable

```
Usage: tgfMain [-hV] [--convert-csv] [--convert-json] [--convert-puml]
               [--convert-yaml] [--overwrite-outputfile] [-f=<tgfFile>]
               [-o=<outputFile>]
parse, and convert TGF file format
      --convert-csv      convert TGF to csv
      --convert-json     convert TGF to json
      --convert-puml     convert TGF to puml
      --convert-yaml     convert TGF to yaml
  -f, --file=<tgfFile>   read from TGF file, if not specified read TGF from
                           stdin
  -h, --help             Show this help message and exit.
  -o, --output=<outputFile>
                         write to file, if not specified write to stdout
      --overwrite-outputfile
                         overwrite existing output file
  -V, --version          Print version information and exit.
```

## TGF Format

TGF stands for Trivial-Graphic-Format. It simply defines a number of nodes,
and edges.

The file syntax is simple:

```
tgf ::= nodes 
        '#'
        edges

nodes ::= node nodes
node ::= ID LABEL

edges ::= edge edges
edge ::= FROM_NODE_ID TO_NODE_ID [ EDGE_LABEL ]
```

## UseCase Generate TGF from Maven dependency:tree

You can generate a maven dependency tree in TGF-Format, like

```
mvn -DoutputType=tgf -DoutputFile=dependency_tree.tgf dependency:tree
```

Next you can convert the convert the file dependency_tree.tgf using this
commandline tool, like

```
java -jar target/tgftools-1.0-SNAPSHOT-tgfmain.jar \
  --file=dependency_tree.tgf --convert-puml
```

The above comman will read the file dependency_tree.tgf, and 
output a plantuml representation to stdout.



## References

* TGF : https://en.wikipedia.org/wiki/Trivial_Graph_Format
* PLANTUML : https://plantuml.com/
