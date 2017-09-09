#******************************************************************************
# Copyright of this product 2013-2023,
# InfiniFlux Corporation(or Inc.) or its subsidiaries.
# All Rights reserved.
#******************************************************************************

# $Id:$
# -Xmx4g

CLASSPATH=".:$(MACHBASE_HOME)/lib/machbase.jar"

SAMPLE_SRC = MakeData.java Sample1Connect.java Sample2Insert.java Sample3PrepareStmt.java Sample4Append.java MakeData.java

all: build

build:
	-@rm -rf *.class
	javac -classpath $(CLASSPATH) -d . $(SAMPLE_SRC)

create_table:
	machsql -s localhost -u sys -p manager -f createTable.sql

select_table:
	machsql -s localhost -u sys -p manager -f selectTable.sql

make_data_file:
	java -classpath $(CLASSPATH) MakeData

run_sample1:
	java -classpath $(CLASSPATH) Sample1Connect

run_sample2:
	java -classpath $(CLASSPATH) Sample2Insert

run_sample3:
	java -classpath $(CLASSPATH) Sample3PrepareStmt

run_sample4:
	java -classpath $(CLASSPATH) Sample4Append

clean:
	rm -rf *.class


