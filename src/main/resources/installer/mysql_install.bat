@echo off
:: created by Kevin Ji 2016/04/08,modify 2016/05/18 
:: -----------------------------------------------------------------------------
:: Installation Script for the auto-deployment EMM(Windows edition-copy)
:: -----------------------------------------------------------------------------
:: Modify EMM_Install script code for windows edition. Add to automatic configure install directory feature. 
:: creat an source package directory,name as "EMM_SRC" and an destination install directory,name as "EMM_DEST".

set dir=%1
set mysql_path=%2


:: -----------create MySQL configuration file----------------
%dir%:
cd %mysql_path%\
echo.>my.ini
echo  [client] >> my.ini
echo  port=3306 >> my.ini
echo  default-character-set=utf8 >> my.ini
echo  [mysqld] >> my.ini
echo  port=3306 >> my.ini
echo  character_set_server=utf8 >> my.ini
echo  basedir=%mysql_path% >> my.ini
echo  datadir=%mysql_path%\data >> my.ini
echo  sql_mode=NO_ENGINE_SUBSTITUTION,STRICT_TRANS_TABLES >> my.ini
echo  skip-grant-tables >> my.ini
echo  [WinMySQLAdmin] >> my.ini
echo  %mysql_path%\bin\mysqld.exe >> my.ini


:: ------------Config MySQL environment variable---------------
setx /M MYSQL_HOME %mysql_path%
setx /M Path %Path%;%mysql_path%\bin

:: ------------install Mysql as server------------
%dir%:
cd %mysql_path%\bin
mysqld install MySQL --defaults-file=%mysql_path%\my.ini
mysqld --initialize


:: ------------modify MySQL-root password------------------------------
net start mysql
 rem cd %mysql_path%\bin
rem mysql -e "use mysql"
rem mysql -e "update mysql.user set authentication_string=password('root') where user='root' ;"
rem mysql -e "flush privileges;"

echo MySQL Install and configuration complete
exit