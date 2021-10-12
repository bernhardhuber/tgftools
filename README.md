# tgftools

## Overview

A simple command line tool reading and converting TGF data to

* PLANTUML
* CSV
* JSON
* YAML

## UseCase Maven dependency:tree

You can generate a maven dependency tree in TGF-Format, like

```
mvn -DoutputType=tgf -DoutputFile=dependency_tree.tgf dependency:tree
```

Next you can convert the convert the file dependency_tree.tgf using this
commandline tool, like

```
java -jar target/tgftools-1.0-SNAPSHOT-tgfmain.jar \
  -f dependency_tree.tgf --convert --convert-puml
```

The above comman will read the file dependency_tree.tgf, and 
output a plantuml representation to stdout.



## References

* TGF : https://en.wikipedia.org/wiki/Trivial_Graph_Format
* PLANTUML : https://plantuml.com/
