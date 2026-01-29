# My Student Management System

Hệ thống quản lý sinh viên được xây dựng với React, Spring Boot và MongoDB.

## Công nghệ sử dụng

### Backend
- **Spring Boot 2.2.6** - Java framework
- **MongoDB** - NoSQL database
- **Maven** - Dependency management
- **Lombok** - Giảm boilerplate code

### Frontend
- **React 18.3.1** - JavaScript library
- **Bootstrap 5.3.3** - CSS framework
- **Axios** - HTTP client
- **React Router DOM** - Routing

### DevOps & Deployment
- **Docker & Docker Compose** - Containerization
- **Kubernetes** - Container orchestration
- **AWS CodePipeline** - CI/CD
- **Terraform** - Infrastructure as Code

## Cấu trúc Project

```
├── react-student-management/          # React frontend
├── spring-boot-student-app-api/       # Spring Boot backend
├── cicd-using-codepipeline/           # AWS CodePipeline configs
├── cicd-using-jenkins/                # Jenkins configs
├── terraform/                        # Infrastructure code
├── student-app-chart/                 # Helm charts
└── docker-compose.yaml               # Local development
```

## Chạy ứng dụng

### Sử dụng Docker Compose (Khuyến nghị)

```bash
docker-compose up -d
```

Ứng dụng sẽ chạy tại:
- Frontend: http://localhost:3000
- Backend API: http://localhost:8080
- MongoDB: localhost:27017

### Chạy thủ công

#### Backend
```bash
cd spring-boot-student-app-api
mvn spring-boot:run
```

#### Frontend
```bash
cd react-student-management
npm install
npm start
```

## API Endpoints

- `GET /api/students` - Lấy danh sách sinh viên
- `GET /api/students/{id}` - Lấy thông tin sinh viên theo ID
- `POST /api/students` - Tạo sinh viên mới
- `PUT /api/students/{id}` - Cập nhật thông tin sinh viên
- `DELETE /api/students/{id}` - Xóa sinh viên
- `DELETE /api/students` - Xóa tất cả sinh viên

## Deployment

### Kubernetes
```bash
kubectl apply -f cicd-using-codepipeline/backend/k8s-deployment.yaml
kubectl apply -f cicd-using-codepipeline/frontend/k8s-deployment.yaml
```

### AWS với Terraform
```bash
cd terraform/singapore-dev
terraform init
terraform plan
terraform apply
```

## Tác giả

Project được tùy chỉnh và phát triển bởi [Tên của bạn]

## License

MIT License