# Login Service — Demo Microservice

A minimal Spring Boot login microservice for demo purposes, ready to deploy on **Azure Spring Apps**.

---

## What's inside

| File | Purpose |
|---|---|
| `SecurityConfig.java` | Spring Security setup — users, password encoder, URL rules |
| `PageController.java` | Serves the login and dashboard pages |
| `login.html` | Login form (Thymeleaf template) |
| `dashboard.html` | Post-login page showing the user's name and roles |
| `style.css` | Simple clean stylesheet |
| `.github/workflows/deploy.yml` | GitHub Actions pipeline → Azure Spring Apps |

---

## Demo credentials

| Username | Password | Role |
|---|---|---|
| alice | password123 | USER |
| admin | admin123 | ADMIN |

---

## Run locally

```bash
./mvnw spring-boot:run
```

Then open http://localhost:8080

---

## Deploy to Azure Spring Apps

### 1. Create infrastructure (one time)

```bash
# Create a resource group
az group create --name my-resource-group --location eastus

# Create an Azure Spring Apps instance (Standard tier)
az spring create \
  --name my-spring-apps-instance \
  --resource-group my-resource-group \
  --location eastus

# Create the app inside the instance
az spring app create \
  --name login-service \
  --service my-spring-apps-instance \
  --resource-group my-resource-group \
  --runtime-version Java_17
```

### 2. Add GitHub Secrets

Go to your repo → **Settings → Secrets and variables → Actions** and add:

| Secret | Value |
|---|---|
| `AZURE_CREDENTIALS` | Output of the `az ad sp create-for-rbac` command below |
| `AZURE_SUBSCRIPTION_ID` | Your Azure subscription ID |

```bash
# Run this once to create a service principal for GitHub Actions
az ad sp create-for-rbac \
  --name "github-login-service" \
  --role contributor \
  --scopes /subscriptions/<SUBSCRIPTION_ID>/resourceGroups/my-resource-group \
  --sdk-auth
```

### 3. Add GitHub Variables

Go to **Settings → Secrets and variables → Actions → Variables** and add:

| Variable | Value |
|---|---|
| `AZURE_RESOURCE_GROUP` | `my-resource-group` |
| `AZURE_SPRING_APPS_NAME` | `my-spring-apps-instance` |
| `SPRING_APP_NAME` | `login-service` |

### 4. Push to main

```bash
git push origin main
```

GitHub Actions will build and deploy automatically. The app URL will be:

```
https://my-spring-apps-instance-login-service.azuremicroservices.io
```

---

## Health check

Azure Spring Apps uses the actuator health endpoint as a readiness probe:

```
GET /actuator/health
```
