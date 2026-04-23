@echo off
echo ========================================
echo Installation des dependances du projet
echo ========================================
echo.

cd backend

echo Nettoyage du projet...
call mvn clean

echo.
echo Installation des dependances...
call mvn install -DskipTests

echo.
echo Compilation du projet...
call mvn compile

echo.
echo ========================================
echo Installation terminee !
echo ========================================
echo.
echo Pour demarrer l'application :
echo   cd backend
echo   mvn spring-boot:run
echo.
pause
