# Migration từ ECS sang EKS

## Các thay đổi chính:

### 1. Jenkins Pipeline
- **ECS**: Sử dụng `backend-job.groovy` và `frontend-job.groovy`
- **EKS**: Sử dụng `backend-job-eks.groovy` và `frontend-job-eks.groovy`

### 2. Deployment Commands
**ECS:**
```bash
aws ecs update-service --cluster ${CLUSTER_NAME} --service ${SERVICE_NAME} --task-definition ${TASK_FAMILY}:${NEW_REVISION} --force-new-deployment
```

**EKS:**
```bash
aws eks update-kubeconfig --region ap-southeast-1 --name ${EKS_CLUSTER_NAME}
kubectl set image deployment/${DEPLOYMENT_NAME} ${DEPLOYMENT_NAME}=${FULL_IMAGE} -n ${NAMESPACE}
kubectl rollout status deployment/${DEPLOYMENT_NAME} -n ${NAMESPACE}
```

### 3. Infrastructure Requirements

#### Cần tạo EKS cluster thay vì ECS:
```bash
# Tạo EKS cluster
aws eks create-cluster --name final-assignment-eks-cluster --version 1.27 --role-arn arn:aws:iam::430950558682:role/eks-service-role --resources-vpc-config subnetIds=subnet-xxx,subnet-yyy,securityGroupIds=sg-xxx

# Tạo node group
aws eks create-nodegroup --cluster-name final-assignment-eks-cluster --nodegroup-name worker-nodes --subnets subnet-xxx subnet-yyy --instance-types t3.medium --ami-type AL2_x86_64 --remote-access ec2SshKey=your-key --node-role arn:aws:iam::430950558682:role/NodeInstanceRole
```

### 4. Jenkins Agent Requirements
Jenkins agent cần có:
- AWS CLI
- kubectl
- Docker
- Quyền truy cập EKS cluster

### 5. Deploy ứng dụng lần đầu
```bash
# Apply tất cả Kubernetes manifests
kubectl apply -f k8s/

# Kiểm tra deployment
kubectl get pods
kubectl get services
kubectl get ingress
```

### 6. Environment Variables thay đổi
- `CLUSTER_NAME` → `EKS_CLUSTER_NAME`
- `SERVICE_NAME` → `DEPLOYMENT_NAME`
- Thêm `NAMESPACE`

### 7. Monitoring & Logging
- ECS: CloudWatch Container Insights
- EKS: Cần cài đặt CloudWatch Container Insights cho EKS hoặc sử dụng Prometheus/Grafana