# Script PowerShell pour vérifier les transactions PayPal Sandbox
# Plus fiable que le batch pour le parsing JSON

param(
    [int]$Days = 30,
    [switch]$Detailed
)

# Configuration PayPal Sandbox (À REMPLACER PAR VOS VRAIS IDENTIFIANTS)
$ClientId = "AeGsAqnl3KqQH5TF_wHqQr8vQ6M2wXqj3dF5SqRxKzJ_HyPzNzMqY9F3Rq8LxP6vQzNqH7K"
$Secret = "EPqvQ7NqH5KzJ_HyPzNzMqY9F3Rq8LxP6vQzNqH7K8SqRxKzJ_HyPzNzMqY9"
$ApiBase = "https://api-m.sandbox.paypal.com"

Write-Host "========================================" -ForegroundColor Cyan
Write-Host " PAYPAL SANDBOX - TRANSACTIONS" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# 1. Obtenir le token d'accès
Write-Host "[1/3] Obtention du token d'accès..." -ForegroundColor Yellow

$authHeader = [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes("${ClientId}:${Secret}"))
$headers = @{
    "Authorization" = "Basic $authHeader"
    "Content-Type" = "application/x-www-form-urlencoded"
}

try {
    $tokenResponse = Invoke-RestMethod -Uri "$ApiBase/v1/oauth2/token" -Method Post -Headers $headers -Body "grant_type=client_credentials"
    $accessToken = $tokenResponse.access_token
    Write-Host "✓ Token obtenu avec succès!" -ForegroundColor Green
} catch {
    Write-Host "✗ ERREUR lors de l'obtention du token:" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
    Write-Host ""
    Write-Host "Vérifiez vos identifiants PayPal Sandbox:" -ForegroundColor Yellow
    Write-Host "1. Connectez-vous à https://developer.paypal.com" -ForegroundColor Yellow
    Write-Host "2. Allez dans 'Apps & Credentials'" -ForegroundColor Yellow
    Write-Host "3. Copiez votre Client ID et Secret de votre app Sandbox" -ForegroundColor Yellow
    Write-Host "4. Remplacez les valeurs dans ce script" -ForegroundColor Yellow
    exit 1
}

Write-Host ""

# 2. Récupérer les transactions
Write-Host "[2/3] Récupération des transactions des $Days derniers jours..." -ForegroundColor Yellow

$startDate = (Get-Date).AddDays(-$Days).ToString("yyyy-MM-ddTHH:mm:ssZ")
$endDate = (Get-Date).ToString("yyyy-MM-ddTHH:mm:ssZ")

$transactionHeaders = @{
    "Authorization" = "Bearer $accessToken"
    "Content-Type" = "application/json"
}

$transactionUrl = "$ApiBase/v1/reporting/transactions?start_date=$startDate&end_date=$endDate&fields=all&page_size=100"

try {
    $transactions = Invoke-RestMethod -Uri $transactionUrl -Method Get -Headers $transactionHeaders
    Write-Host "✓ Transactions récupérées!" -ForegroundColor Green
} catch {
    Write-Host "✗ ERREUR lors de la récupération des transactions:" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
    exit 1
}

Write-Host ""

# 3. Afficher les transactions
Write-Host "[3/3] Affichage des transactions..." -ForegroundColor Yellow
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

if ($transactions.transaction_details -and $transactions.transaction_details.Count -gt 0) {
    Write-Host "Nombre de transactions trouvées: $($transactions.transaction_details.Count)" -ForegroundColor Green
    Write-Host ""
    
    $totalAmount = 0
    
    foreach ($transaction in $transactions.transaction_details) {
        Write-Host "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor DarkGray
        Write-Host "Transaction ID: $($transaction.transaction_info.transaction_id)" -ForegroundColor Cyan
        Write-Host "Date: $($transaction.transaction_info.transaction_initiation_date)" -ForegroundColor White
        Write-Host "Type: $($transaction.transaction_info.transaction_event_code)" -ForegroundColor White
        Write-Host "Statut: $($transaction.transaction_info.transaction_status)" -ForegroundColor $(if($transaction.transaction_info.transaction_status -eq "S") {"Green"} else {"Yellow"})
        
        if ($transaction.transaction_info.transaction_amount) {
            $amount = $transaction.transaction_info.transaction_amount.value
            $currency = $transaction.transaction_info.transaction_amount.currency_code
            Write-Host "Montant: $amount $currency" -ForegroundColor Magenta
            
            if ($amount -match "^-?\d+\.?\d*$") {
                $totalAmount += [decimal]$amount
            }
        }
        
        if ($Detailed) {
            if ($transaction.payer_info) {
                Write-Host "Payeur: $($transaction.payer_info.payer_name.alternate_full_name)" -ForegroundColor White
                Write-Host "Email: $($transaction.payer_info.email_address)" -ForegroundColor White
            }
            
            if ($transaction.transaction_info.transaction_note) {
                Write-Host "Note: $($transaction.transaction_info.transaction_note)" -ForegroundColor Gray
            }
        }
        
        Write-Host ""
    }
    
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host "Total des transactions: $totalAmount EUR" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Cyan
    
} else {
    Write-Host "Aucune transaction trouvée pour la période spécifiée." -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Pour créer des transactions de test:" -ForegroundColor Cyan
    Write-Host "1. Démarrez votre payment-service" -ForegroundColor White
    Write-Host "2. Créez un paiement via: POST http://localhost:8082/payment/create" -ForegroundColor White
    Write-Host "3. Ou connectez-vous à https://www.sandbox.paypal.com pour voir l'interface web" -ForegroundColor White
}

Write-Host ""
Write-Host "Pour plus de détails, utilisez: .\CHECK_PAYPAL_TRANSACTIONS.ps1 -Detailed" -ForegroundColor Gray
Write-Host ""
