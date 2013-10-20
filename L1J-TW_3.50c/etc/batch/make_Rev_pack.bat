copy /Y ..\..\db\Japan\makedb.sql + ..\..\db\Japan\MyISAM\*.sql ..\..\db\Japan\MyISAM.sql
"C:\Program Files\7-Zip\7z.exe" a -tzip ..\..\..\L1JP_Revjar_db.zip -r @Pack.lst -x@Exclusion.lst -mx=9
del /F ..\..\db\Japan\MyISAM.sql