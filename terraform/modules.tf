module "naming" {
  source             = "git::https://git.tmaws.io/AWS/terraform-module-naming.git"
  product_code_tag   = "${var.product_code_tag}"
  environment_tag    = "${var.environment_tag}"
  inventory_code_tag = "${var.inventory_code_tag}"
  product_name       = "${var.product_name}"
  account_tag        = "${var.account_tag}"
  aws_region         = "${var.aws_region}"
}

module "account" {
  source      = "git::https://git.tmaws.io/AWS/terraform-module-account.git"
  account_tag = "${var.account_tag}"
}

module "networks" {
  source = "git::http://git.tm.tmcs/AWS/terraform-module-networks.git"
}
