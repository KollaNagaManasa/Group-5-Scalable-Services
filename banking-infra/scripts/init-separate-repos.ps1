param(
    [string] $WorkspaceRoot = (Resolve-Path (Join-Path $PSScriptRoot "..\\..")).Path,
    [string] $GithubOrg = "YOUR_GITHUB_USER_OR_ORG"
)

$repos = @(
    @{ Name = "customer-service"; Remote = "https://github.com/$GithubOrg/customer-service.git" },
    @{ Name = "account-service"; Remote = "https://github.com/$GithubOrg/account-service.git" },
    @{ Name = "transaction-service"; Remote = "https://github.com/$GithubOrg/transaction-service.git" },
    @{ Name = "notification-service"; Remote = "https://github.com/$GithubOrg/notification-service.git" },
    @{ Name = "banking-infra"; Remote = "https://github.com/$GithubOrg/banking-infra.git" }
)

foreach ($r in $repos) {
    $path = Join-Path $WorkspaceRoot $r.Name
    if (-not (Test-Path $path)) {
        Write-Warning "Skip $($r.Name): folder not found at $path"
        continue
    }
    Push-Location $path
    try {
        if (-not (Test-Path ".git")) {
            git init
            git add .
            git commit -m "Initial commit: $($r.Name)"
            git branch -M main
            Write-Host "Initialized git in $($r.Name). Add remote and push:"
            Write-Host "  git remote add origin $($r.Remote)"
            Write-Host "  git push -u origin main"
        }
        else {
            Write-Host "$($r.Name) already has a .git directory — skipping init."
        }
    }
    finally {
        Pop-Location
    }
}
