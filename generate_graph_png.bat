@echo off

set graphs=przykladowy_4

for %%i in (%graphs%) do (
	java -cp "target/scheduling-geo-0.0.1-SNAPSHOT-jar-with-dependencies.jar" pl.kaczanowski.graphviz.GraphvizRunner  -f=src/main/resources/%%i.txt -graphviz=src/main/resources/%%i.gv

	dot -Tpng -osrc/main/resources/%%i.png src/main/resources/%%i.gv
	
)
