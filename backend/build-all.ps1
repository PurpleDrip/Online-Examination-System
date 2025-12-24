# Build all services for Docker deployment
Write-Host "Building all services..." -ForegroundColor Green

# Build Eureka Server
Write-Host "`nBuilding Eureka Server..." -ForegroundColor Yellow
Set-Location eureka-server
mvn clean package -DskipTests
if ($LASTEXITCODE -ne 0) {
    Write-Host "Failed to build Eureka Server" -ForegroundColor Red
    exit 1
}
Set-Location ..

# Build API Gateway
Write-Host "`nBuilding API Gateway..." -ForegroundColor Yellow
Set-Location api-gateway
mvn clean package -DskipTests
if ($LASTEXITCODE -ne 0) {
    Write-Host "Failed to build API Gateway" -ForegroundColor Red
    exit 1
}
Set-Location ..

# Build Question Service
Write-Host "`nBuilding Question Service..." -ForegroundColor Yellow
Set-Location question-service
mvn clean package -DskipTests
if ($LASTEXITCODE -ne 0) {
    Write-Host "Failed to build Question Service" -ForegroundColor Red
    exit 1
}
Set-Location ..

# Build Test Service
Write-Host "`nBuilding Test Service..." -ForegroundColor Yellow
Set-Location test-service
mvn clean package -DskipTests
if ($LASTEXITCODE -ne 0) {
    Write-Host "Failed to build Test Service" -ForegroundColor Red
    exit 1
}
Set-Location ..

# Build Marks Service
Write-Host "`nBuilding Marks Service..." -ForegroundColor Yellow
Set-Location marks-service
mvn clean package -DskipTests
if ($LASTEXITCODE -ne 0) {
    Write-Host "Failed to build Marks Service" -ForegroundColor Red
    exit 1
}
Set-Location ..

Write-Host "`n✅ All services built successfully!" -ForegroundColor Green
Write-Host "`nYou can now run: docker-compose up -d" -ForegroundColor Cyan
