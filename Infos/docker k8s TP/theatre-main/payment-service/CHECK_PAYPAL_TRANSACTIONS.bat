@echo off
setlocal enabledelayedexpansion

echo ========================================
echo  PAYPAL SANDBOX - VERIFICATION TRANSACTIONS
echo ========================================
echo.

REM Configuration PayPal Sandbox
set PAYPAL_CLIENT_ID=AeGsAqnl3KqQH5TF_wHqQr8vQ6M2wXqj3dF5SqRxKzJ_HyPzNzMqY9F3Rq8LxP6vQzNqH7K
set PAYPAL_SECRET=EPqvQ7NqH5KzJ_HyPzNzMqY9F3Rq8LxP6vQzNqH7K8SqRxKzJ_HyPzNzMqY9
set PAYPAL_API=https://api-m.sandbox.paypal.com

echo [1/3] Obtention du token d'acces...
curl -s -X POST "%PAYPAL_API%/v1/oauth2/token" ^
  -H "Accept: application/json" ^
  -H "Accept-Language: en_US" ^
  -u "%PAYPAL_CLIENT_ID%:%PAYPAL_SECRET%" ^
  -d "grant_type=client_credentials" > token.json

REM Extraire le token (nécessite jq ou PowerShell)
for /f "tokens=*" %%i in ('powershell -command "(Get-Content token.json | ConvertFrom-Json).access_token"') do set ACCESS_TOKEN=%%i

if "!ACCESS_TOKEN!"=="" (
    echo ERREUR: Impossible d'obtenir le token d'acces
    echo Verifiez vos identifiants PayPal dans le fichier
    pause
    exit /b 1
)

echo Token obtenu avec succes !
echo.

echo [2/3] Recuperation des transactions des 30 derniers jours...
REM Calculer la date d'il y a 30 jours (format ISO 8601)
for /f "tokens=*" %%i in ('powershell -command "(Get-Date).AddDays(-30).ToString('yyyy-MM-ddTHH:mm:ss')"') do set START_DATE=%%i
for /f "tokens=*" %%i in ('powershell -command "(Get-Date).ToString('yyyy-MM-ddTHH:mm:ss')"') do set END_DATE=%%i

echo Periode: !START_DATE! a !END_DATE!
echo.

curl -s -X GET "%PAYPAL_API%/v1/reporting/transactions?start_date=!START_DATE!Z&end_date=!END_DATE!Z&fields=all&page_size=100" ^
  -H "Content-Type: application/json" ^
  -H "Authorization: Bearer !ACCESS_TOKEN!" > transactions.json

echo.
echo [3/3] Affichage des transactions...
echo ========================================
echo.

REM Afficher les transactions (format JSON)
type transactions.json

echo.
echo ========================================
echo.
echo Les transactions ont ete sauvegardees dans: transactions.json
echo Vous pouvez les ouvrir avec un editeur JSON pour une meilleure lisibilite
echo.
echo Pour voir un ordre specifique, utilisez:
echo curl -X GET "%PAYPAL_API%/v2/checkout/orders/{ORDER_ID}" -H "Authorization: Bearer !ACCESS_TOKEN!"
echo.

REM Nettoyage
del token.json 2>nul

pause
