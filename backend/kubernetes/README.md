# Kubernetes Deployment Guide

This guide explains how to deploy the Online Examination System to Kubernetes.

## Prerequisites

- Kubernetes cluster (minikube, kind, or cloud provider)
- kubectl configured
- Docker images built

## Step 1: Build Docker Images

```bash
cd backend

# Build all images
docker build -t exam-eureka-server:latest ./eureka-server
docker build -t exam-api-gateway:latest ./api-gateway
docker build -t exam-question-service:latest ./question-service
docker build -t exam-test-service:latest ./test-service
docker build -t exam-marks-service:latest ./marks-service
```

## Step 2: Load Images (for minikube/kind)

If using minikube:
```bash
minikube image load exam-eureka-server:latest
minikube image load exam-api-gateway:latest
minikube image load exam-question-service:latest
minikube image load exam-test-service:latest
minikube image load exam-marks-service:latest
```

If using kind:
```bash
kind load docker-image exam-eureka-server:latest
kind load docker-image exam-api-gateway:latest
kind load docker-image exam-question-service:latest
kind load docker-image exam-test-service:latest
kind load docker-image exam-marks-service:latest
```

## Step 3: Deploy to Kubernetes

```bash
# Create namespace
kubectl apply -f kubernetes/namespace.yaml

# Create ConfigMap and Secrets
kubectl apply -f kubernetes/configmap.yaml
kubectl apply -f kubernetes/secrets.yaml

# Deploy MySQL
kubectl apply -f kubernetes/mysql.yaml

# Wait for MySQL to be ready
kubectl wait --for=condition=ready pod -l app=mysql -n exam-system --timeout=300s

# Deploy Eureka Server
kubectl apply -f kubernetes/eureka-server.yaml

# Wait for Eureka to be ready
kubectl wait --for=condition=ready pod -l app=eureka-server -n exam-system --timeout=300s

# Deploy API Gateway
kubectl apply -f kubernetes/api-gateway.yaml

# Deploy Microservices
kubectl apply -f kubernetes/question-service.yaml
kubectl apply -f kubernetes/test-service.yaml
kubectl apply -f kubernetes/marks-service.yaml
```

## Step 4: Verify Deployment

```bash
# Check all pods
kubectl get pods -n exam-system

# Check services
kubectl get svc -n exam-system

# Check logs
kubectl logs -f deployment/question-service -n exam-system
```

## Step 5: Access Services

### Using NodePort (minikube/kind)

```bash
# Get minikube IP
minikube ip

# Access services
# Eureka: http://<minikube-ip>:30761
# API Gateway: http://<minikube-ip>:30080
```

### Using Port Forward

```bash
# Forward API Gateway
kubectl port-forward svc/api-gateway 8080:8080 -n exam-system

# Forward Eureka
kubectl port-forward svc/eureka-server 8761:8761 -n exam-system
```

## Step 6: Test the System

```bash
# Get question sets
curl http://localhost:8080/api/question-sets

# Start a test
curl -X POST http://localhost:8080/api/test-sessions/start \
  -H "Content-Type: application/json" \
  -d '{
    "usn": "1MS22CS023",
    "studentName": "John Doe",
    "semester": 6,
    "questionSetId": 1
  }'
```

## Scaling

Scale services:
```bash
kubectl scale deployment question-service --replicas=3 -n exam-system
kubectl scale deployment test-service --replicas=3 -n exam-system
kubectl scale deployment marks-service --replicas=3 -n exam-system
```

## Monitoring

View logs:
```bash
# All pods
kubectl logs -f -l app=question-service -n exam-system

# Specific pod
kubectl logs -f <pod-name> -n exam-system
```

## Cleanup

```bash
# Delete all resources
kubectl delete namespace exam-system

# Or delete individually
kubectl delete -f kubernetes/
```

## Troubleshooting

### Pods not starting
```bash
kubectl describe pod <pod-name> -n exam-system
kubectl logs <pod-name> -n exam-system
```

### Services not registering with Eureka
- Check Eureka logs
- Verify network connectivity
- Check environment variables

### Database connection issues
- Verify MySQL is running
- Check secrets are correct
- Verify database initialization

## Production Considerations

1. **Use LoadBalancer** instead of NodePort
2. **Configure Ingress** for external access
3. **Set resource limits** for all pods
4. **Enable autoscaling** (HPA)
5. **Configure persistent volumes** for MySQL
6. **Set up monitoring** (Prometheus, Grafana)
7. **Configure logging** (ELK stack)
8. **Use secrets management** (Vault, Sealed Secrets)
