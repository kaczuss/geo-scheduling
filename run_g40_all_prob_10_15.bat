set ALGORITHM=g40

set BEST=80

set RUN_ITERATIONS=1000
set ITERATIONS=100

set BASE_REPORT_DIR=G:/dokumenty/magisterka-geo-szer/raport/%ALGORITHM%/%date%


set PROB=1.0
set PROB_DIR_NAME=1_0

java -jar target/scheduling-geo-0.0.1-SNAPSHOT-jar-with-dependencies.jar -f=src/main/resources/%ALGORITHM%.txt -bestMeanWorst=%BASE_REPORT_DIR%/%PROB_DIR_NAME%/iterations_best_%ALGORITHM%.csv -currentBestWorst=%BASE_REPORT_DIR%/%PROB_DIR_NAME%/gauss_best_worst_{0}_%ALGORITHM%.csv -iterToBest=%BASE_REPORT_DIR%/%PROB_DIR_NAME%/iterations_to_best.csv -distribution=%BASE_REPORT_DIR%/%PROB_DIR_NAME%/distribution.csv -best=%BEST% -ri=%RUN_ITERATIONS% -i=%ITERATIONS% -prob=%PROB%  > .run/%ALGORITHM%_%date%_prob_%PROB_DIR_NAME%.out.txt


set PROB=1.5
set PROB_DIR_NAME=1_5

java -jar target/scheduling-geo-0.0.1-SNAPSHOT-jar-with-dependencies.jar -f=src/main/resources/%ALGORITHM%.txt -bestMeanWorst=%BASE_REPORT_DIR%/%PROB_DIR_NAME%/iterations_best_%ALGORITHM%.csv -currentBestWorst=%BASE_REPORT_DIR%/%PROB_DIR_NAME%/gauss_best_worst_{0}_%ALGORITHM%.csv -iterToBest=%BASE_REPORT_DIR%/%PROB_DIR_NAME%/iterations_to_best.csv -distribution=%BASE_REPORT_DIR%/%PROB_DIR_NAME%/distribution.csv -best=%BEST% -ri=%RUN_ITERATIONS% -i=%ITERATIONS% -prob=%PROB%  > .run/%ALGORITHM%_%date%_prob_%PROB_DIR_NAME%.out.txt
