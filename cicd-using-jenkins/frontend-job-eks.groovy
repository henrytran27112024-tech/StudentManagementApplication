pipeline {
    agent any
    environment{
        FULL_IMAGE = "430950558682.dkr.ecr.ap-southeast-1.amazonaws.com/devops-final-assignment-frontend:latest"
        EKS_CLUSTER_NAME = "final-assignment-eks-cluster"
        NAMESPACE = "default"
        DEPLOYMENT_NAME = "student-app-client"
    }
    stages {
        stage('Build') {
            steps {
                sh 'docker build -t final-assignment-frontend:latest .'
            }
        }
        stage('Upload image to ECR') {
            steps {
                sh 'aws ecr get-login-password --region ap-southeast-1 | docker login --username AWS --password-stdin 430950558682.dkr.ecr.ap-southeast-1.amazonaws.com'
                sh 'docker tag final-assignment-frontend:latest ${FULL_IMAGE}'
                sh 'docker push ${FULL_IMAGE}'
            }
        }
        
        stage('Deploy to EKS') {
            steps {
                sh '''
                    # Update kubeconfig
                    aws eks update-kubeconfig --region ap-southeast-1 --name ${EKS_CLUSTER_NAME}
                    
                    # Update deployment image
                    kubectl set image deployment/${DEPLOYMENT_NAME} ${DEPLOYMENT_NAME}=${FULL_IMAGE} -n ${NAMESPACE}
                    
                    # Wait for rollout to complete
                    kubectl rollout status deployment/${DEPLOYMENT_NAME} -n ${NAMESPACE}
                '''
            }
        }
    }
}