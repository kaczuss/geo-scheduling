REM set ALGORITHM=tree15
REM set BEST=9
REM set ALGORITHM=intree15
REM set BEST=9
REM set ALGORITHM=g18
REM set BEST=46
REM set ALGORITHM=g18_8
REM set BEST=24
REM set ALGORITHM=g40
REM set BEST=80
REM set ALGORITHM=g40_4
REM set BEST=45
REM set ALGORITHM=g40_8
REM set BEST=33
REM set ALGORITHM=gauss18
REM set BEST=44
set ALGORITHM=gauss18_8
set BEST=44


REM set BASE_REPORT_DIR="G:\dokumenty\magisterka-geo-szer\raport\%ALGORITHM%\bestInIteration\2013-05-06\400 iteracji"
REM set BASE_REPORT_DIR=G:\dokumenty\magisterka-geo-szer\raport\2013-05-02\%ALGORITHM%\2013-05-02
REM set BASE_REPORT_DIR=G:\dokumenty\magisterka-geo-szer\raport\%ALGORITHM%\2013-05-05\proc4
set BASE_REPORT_DIR=G:\dokumenty\magisterka-geo-szer\raport\%ALGORITHM%\2013-05-07\proc8
set BASE_REPORT_DIR=G:\dokumenty\magisterka-geo-szer\raport\%ALGORITHM%\2013-05-07(4000)\proc8

java -cp "target/scheduling-geo-0.0.1-SNAPSHOT-jar-with-dependencies.jar" pl.kaczanowski.analyze.StatisticsAnalyzer  -best=%BEST%  -statDir=%BASE_REPORT_DIR% -notFilteredStats=%BASE_REPORT_DIR%/statsNotFiltered.csv -withoutNotFundedStats=%BASE_REPORT_DIR%/statsWithoutNotFound.csv -withoutZerosStats=%BASE_REPORT_DIR%/statsWithoutZeros.csv -withoutZerosAndNotFundedStats=%BASE_REPORT_DIR%/statsTotalyFiltered.csv

