terraform {
  required_version = ">= 1.5.0"
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = ">= 5.0.0"
    }
  }
}

provider "aws" {
  region = var.region
}

#Create a complete VPC using module networking
module "networking" {
  source              = "../modules/networking"
  region              = var.region
  availability_zones  = var.availability_zones
  cidr_block          = var.cidr_block
  public_subnet_ips   = var.public_subnet_ips
  private_subnet_ips  = var.private_subnet_ips
}

module "security" {
  source = "../modules/security"
  region = var.region
  vpc_id = module.networking.vpc_id
}

module "bastion" {
  source = "../modules/bastion"
  region = var.region
  instance_type = "t3.small"
  security_groups = [
    module.security.bastion_security_group_id
  ]
  subnet_id = module.networking.public_subnet_ids[0]
}

module "database"{
  source = "../modules/database"
  region = var.region
  vpc_id = module.networking.vpc_id
  db_subnets = module.networking.private_subnet_ids
  db_security_group_ids = [
    module.security.database_security_group_id
  ]
  db_username = var.db_username
}

# EKS Cluster thay thế ECS
module "eks_cluster"{
  source = "../modules/eks_cluster"
  region = var.region
  vpc_id = module.networking.vpc_id
  eks_subnet_ids = module.networking.private_subnet_ids
  eks_security_group_ids = [
    module.security.private_security_group_id
  ]
}