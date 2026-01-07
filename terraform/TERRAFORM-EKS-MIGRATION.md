# Terraform Migration từ ECS sang EKS

## Các thay đổi chính:

### 1. Module mới
- Tạo module `eks_cluster` thay thế `ecs_cluster`
- Loại bỏ module `load_balance` (EKS sử dụng AWS Load Balancer Controller)

### 2. File cấu hình
- `main.tf` → `main-eks.tf` (backup file cũ)
- Cập nhật `student-app-ingress.yaml` để sử dụng ALB

### 3. Infrastructure Components

#### ECS → EKS:
```hcl
# ECS (cũ)
module "ecs_cluster" {
  source = "../modules/ecs_cluster"
  # ... ECS config
}

# EKS (mới)
module "eks_cluster" {
  source = "../modules/eks_cluster"
  region = var.region
  vpc_id = module.networking.vpc_id
  eks_subnet_ids = module.networking.private_subnet_ids
  eks_security_group_ids = [
    module.security.private_security_group_id
  ]
}
```

### 4. Deployment Process

#### Trước khi migrate:
```bash
# Backup ECS resources
terraform state pull > ecs-state-backup.json

# Destroy ECS resources
terraform destroy -target=module.ecs_cluster
terraform destroy -target=module.load_balance
```

#### Deploy EKS:
```bash
# Sử dụng file main-eks.tf
cp main-eks.tf main.tf

# Deploy EKS
terraform init
terraform plan
terraform apply

# Cấu hình kubectl
aws eks update-kubeconfig --region ap-southeast-1 --name final-assignment-eks-cluster

# Deploy applications
kubectl apply -f ../../k8s/
```

### 5. Variables cần cập nhật
Loại bỏ các variables liên quan đến ECS:
- `frontend_ecr_repo_url`
- `backend_ecr_repo_url`

### 6. Post-deployment
```bash
# Cài đặt AWS Load Balancer Controller
kubectl apply -k "github.com/aws/eks-charts/stable/aws-load-balancer-controller//crds?ref=master"

# Verify deployment
kubectl get pods -A
kubectl get ingress
kubectl get services
```

### 7. Cost Optimization
- EKS: $0.10/hour cho control plane + EC2 instances
- ECS Fargate: Pay per vCPU/memory usage
- EKS thường cost-effective hơn cho workloads lâu dài