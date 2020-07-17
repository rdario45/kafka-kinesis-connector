variable "aws_region" { default = "us-east-2" }
variable "aws_profile" { default = "tm-sandbox-Ops-Techops" }
variable "vpc" { default = "dev"}
variable "environment_tag" {}
variable "product_name" {}
variable "inventory_code_tag" { default = "offering-stream-consumer"}
variable "account_tag" {}
variable "product_code_tag" { default = "PRD18XX" }
variable "terraformer_bucket" { default = "terraform.nonprod1.us-east-1.tmaws" }

//variable "kube_region" {}
//variable "kubernetes_environment" {}
//variable "lambda_artifact_name" { default = "discovery-s3-cleaner" }
//variable "lambda_handler"       { default = "com.ticketmaster.sponsorship.DiscoveryS3Cleaner::run" }
//variable "lambda_memory_size"   { default = 512 }
//variable "lambda_timeout"       { default = 30 }
//variable "cleaner_schedule_expression" { default = "rate(7 days)" }
//
