REM set ALGORITHM=tree15
REM set BEST=9
REM set ALGORITHM=intree15
REM set BEST=9
REM set ALGORITHM=g18
REM set BEST=46
REM set ALGORITHM=g18_8
REM set BEST=24
REM set ALGORITHM=g18_4
REM set BEST=26
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
REM et ALGORITHM=gauss18_4
REM BEST=44
REM set ALGORITHM=tree15_4
REM set BEST=7
REM set ALGORITHM=tree15_8
REM set BEST=7


REM set BASE_REPORT_DIR="G:\dokumenty\magisterka-geo-szer\raport\%ALGORITHM%\bestInIteration\2013-05-06\400 iteracji"
REM set BASE_REPORT_DIR=G:\dokumenty\magisterka-geo-szer\raport\2013-05-02\%ALGORITHM%\2013-05-02
REM set BASE_REPORT_DIR="G:\dokumenty\magisterka-geo-szer\raport\gauss18\bestInIteration\2013-05-06\400 iteracji"
REM set BASE_REPORT_DIR=G:\dokumenty\magisterka-geo-szer\raport\%ALGORITHM%\2013-05-05\proc4
REM set BASE_REPORT_DIR=G:\dokumenty\magisterka-geo-szer\raport\g40_4(500)\2013-05-08\proc4
REM set BASE_REPORT_DIR=G:\dokumenty\magisterka-geo-szer\raport\gauss18_4(1000)\2013-05-08\proc4
REM set BASE_REPORT_DIR=G:\dokumenty\magisterka-geo-szer\raport\%ALGORITHM%\2013-05-06\proc8
REM set BASE_REPORT_DIR=G:\dokumenty\magisterka-geo-szer\raport\g40_8\2013-05-08\1000\proc8
REM set BASE_REPORT_DIR=G:\dokumenty\magisterka-geo-szer\raport\%ALGORITHM%\2013-05-07\proc8
set BASE_REPORT_DIR=G:\dokumenty\magisterka-geo-szer\raport\%ALGORITHM%\2013-05-07(4000)\proc8


java -cp "target/scheduling-geo-0.0.1-SNAPSHOT-jar-with-dependencies.jar" pl.kaczanowski.analyze.StatisticsAnalyzer  -best=%BEST%  -statDir=%BASE_REPORT_DIR% -notFilteredStats=%BASE_REPORT_DIR%/statsNotFiltered.csv

