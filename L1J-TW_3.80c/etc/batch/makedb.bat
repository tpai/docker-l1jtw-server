SET MYSQL=mysql
SET USER=root
SET PASSWORD=
%MYSQL%  --user=%USER% --password=%PASSWORD% --default-character-set=sjis < ..\..\db\makedb.sql
%MYSQL%  --user=%USER% --password=%PASSWORD% --default-character-set=sjis < ..\..\db\make_tables.sql
