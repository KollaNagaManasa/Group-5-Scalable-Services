# Splitting into separate GitHub repositories

The rubric requires **at least four microservices**, each in **its own repository**. Use one additional repo for shared deployment assets.

| Repository | Contents |
|------------|----------|
| `customer-service` | Customer Spring Boot app + Dockerfile + README |
| `account-service` | Account Spring Boot app + Dockerfile + README |
| `transaction-service` | Transaction Spring Boot app + Dockerfile + README |
| `notification-service` | Notification Spring Boot app + Dockerfile + README |
| `banking-infra` | `docker-compose.yml`, `k8s/`, `seed-data/`, docs |

## Initialize remotes (PowerShell)

From your workspace parent folder (where all repos are siblings):

```powershell
.\banking-infra\scripts\init-separate-repos.ps1 -GithubOrg YOUR_USER
```

Or manually, for each application repo:

```powershell
cd customer-service
git init
git add .
git commit -m "Initial commit: customer-service"
git branch -M main
git remote add origin https://github.com/YOUR_USER/customer-service.git
git push -u origin main
```

Repeat for `account-service`, `transaction-service`, and `notification-service`. Push `banking-infra` separately.

## Notes

- Do **not** share databases or tables across services; replicate read fields (e.g. `customer_name` on accounts) via APIs at write time.
- OpenAPI JSON is served by each service at `/v3/api-docs` (SpringDoc).
