# java
Machbase Sample Source Code in java language
JDBC sample source and addtional APIs
# environment variable
```
MACHBASE_HOME must be set
```

# build 
```
make build
```
# create schema
```
machsql -s 127.0.0.1 -u sys -p manager -f createTable.sql 
```
# execute test case
```
make run
```
