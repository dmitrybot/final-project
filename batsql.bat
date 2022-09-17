@echo off
setlocal
set PGPASSWORD=qwerty12
"C:\Program Files\PostgreSQL\14\bin\psql.exe" -h localhost -U postgres -d finalsenla -f C:\Users\Asus\Desktop\mirea\4_sem\SENLA-Java\finalproject\ddl.sql
"C:\Program Files\PostgreSQL\14\bin\psql.exe" -h localhost -U postgres -d finalsenla -f C:\Users\Asus\Desktop\mirea\4_sem\SENLA-Java\finalproject\dml.sql
pause
endlocal