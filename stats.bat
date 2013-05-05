--set ALGORITHM=tree15
set ALGORITHM=g18
set BEST=46

set ITERATIONS=100


set BASE_REPORT_DIR=G:\dokumenty\magisterka-geo-szer\raport\2013-05-02\%ALGORITHM%\2013-05-02

java -cp "target/scheduling-geo-0.0.1-SNAPSHOT-jar-with-dependencies.jar" pl.kaczanowski.analyze.StatisticsAnalyzer  -best=%BEST%  -statDir=%BASE_REPORT_DIR% -notFilteredStats=%BASE_REPORT_DIR%/statsNotFiltered.csv -withoutNotFundedStats=%BASE_REPORT_DIR%/statsWithoutNotFound.csv -withoutZerosStats=%BASE_REPORT_DIR%/statsWithoutZeros.csv -withoutZerosAndNotFundedStats=%BASE_REPORT_DIR%/statsTotalyFiltered.csv

