# JDBC_Coursework

Simple .db backup tool.

## Description
SCC201 Databases: Assessed Coursework

Write a program (in Java and using JDBC) that takes a database and produces a textual “backup” of the 
contents.

This textual backup should consist of SQL statements that will recreate the contents of the original database i.e. `CREATE TABLE and INSERT .. INTO` instructions. Ideally, you should be able to recreate the original database by using the text files your program produces as input to SQLite.

Your program must NOT use the system schema tables found in SQLite; any access to schema information must use the appropriate JDBC methods. The JDBC documentation is linked from the course Moodle page, but another good starting place to find out how to access metadata via JDBC is to google “JDBC metadata”.

## Submission
If you get past milestone (a), you must create a batch file containing all the instructions necessary for running the files created as output by your program to create and populate the database copy.

Submission (to Moodle) Checklist (for the milestone you have reached)

1. All your Java source files.
2. All the output files created by your code (for the milestone you have reached).
3. The batch files required to create the database copies.

To gain marks you must submit to Moodle and demonstrate your solution as submitted to Moodle. 
Failure to submit, to demo, or demoing code different to that submitted will result in a mark of zero

## 备注
按照作业要求走了走流程，然后得分98%。代码结构随便整了个自己喜欢的结构，看起来有点像Encoder-Decoder或者Serialization-Deserialization？

代码实现仅供参考，不保证完备性。