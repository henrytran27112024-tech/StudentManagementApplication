variable "region" {
  description = "AWS region"
  type        = string
}

variable "vpc_id" {
  description = "VPC ID"
  type        = string
}

variable "eks_subnet_ids" {
  description = "Subnet IDs for EKS cluster"
  type        = list(string)
}

variable "eks_security_group_ids" {
  description = "Security group IDs for EKS cluster"
  type        = list(string)
}