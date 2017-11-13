java -Xmx512m -Xms512m -XX:+UseSerialGC -jar target/HW04.jar > SerialGCReport.txt
java -Xmx512m -Xms512m -XX:+UseParallelGC -jar target/HW04.jar > ParallelGCReport.txt
java -Xmx512m -Xms512m -XX:+UseConcMarkSweepGC -jar target/HW04.jar > CMSGCReport.txt
java -Xmx512m -Xms512m -XX:+UseG1GC -jar target/HW04.jar > G1Report.txt