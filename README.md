# PixelDrop – Cloud-Native Backend Architecture (AWS)

PixelDrop is a cloud-native backend system built to support a wallpaper application with a strong focus on **scalability, reliability, performance, and real-world cloud architecture practices**.

This project started as a simple backend deployment on AWS and was later evolved to solve **real performance bottlenecks** related to large image delivery using additional AWS services like **Lambda and CloudFront**.

The repository demonstrates how a production-style backend can be designed, deployed, optimized, and iteratively improved using AWS and DevOps best practices.

---

## High-Level Project Overview

* Backend API built with **Node.js**
* Containerized using **Docker** and stored in **Amazon ECR**
* Deployed on **EC2 instances managed by Auto Scaling Group**
* Fronted by **Application Load Balancer (ALB)**
* Uses **Amazon RDS (PostgreSQL)** for metadata storage
* Stores wallpaper images in **Amazon S3**
* Secured and isolated using **VPC, public/private subnets, IAM, and security groups**

The project is divided into two architectural phases:

1. **Initial architecture (without Lambda & CloudFront)**
2. **Optimized architecture (with Lambda & CloudFront)**

---

## Phase 1: Initial Architecture (Without Lambda & CloudFront)

This phase represents the first production-style deployment of PixelDrop on AWS. The focus here was on **correct infrastructure design, networking, security, and scalability**.

### Architecture Goals

* Deploy backend securely in private subnets
* Enable horizontal scaling using Auto Scaling Group
* Separate compute, database, and storage layers
* Ensure controlled access using IAM and security groups

### Architecture Diagram (Initial Version)

> **Diagram:** PixelDrop Architecture – Core AWS Infrastructure (Without Lambda & CloudFront)

![Initial Architecture Diagram](https://github.com/rohanan07/pixeldrop-cloud-architecture/blob/main/pixeldrop-architecture-diagram.jpg)

### Architecture Explanation

* **VPC** spanning multiple Availability Zones (ap-south-1a, ap-south-1b)
* **Public Subnets**

  * Application Load Balancer
  * NAT Gateway
  * Bastion Host (for admin access)
* **Private Subnets (Application Layer)**

  * EC2 instances running Node.js backend
  * Managed by an Auto Scaling Group
* **Private Subnets (Database Layer)**

  * Amazon RDS PostgreSQL (Multi-AZ)
* **Amazon S3**

  * Stores original wallpaper images
* **Amazon ECR**

  * Stores Docker images for backend

### Request Flow (Phase 1)

1. Mobile app sends API requests to ALB
2. ALB routes traffic to EC2 backend instances
3. Backend fetches metadata from RDS
4. Backend returns S3 image URLs to the client
5. Client downloads images directly from S3

### Limitation Identified

Although this architecture was **stable and scalable**, a major issue was observed:

* Wallpaper images were large in size
* Some images loaded slowly or failed on mobile networks
* Repeated requests always hit S3 directly

This led to the next phase of optimization.

---

## Phase 2: Optimized Architecture (With Lambda & CloudFront)

To solve the performance and reliability issues related to large image delivery, the architecture was enhanced using **serverless image processing and CDN-based caching**.

### Optimization Goals

* Reduce image size dynamically
* Improve image load time and consistency
* Cache frequently accessed images at edge locations
* Reduce repeated load on S3

### Architecture Diagram (Optimized Version)

> **Diagram:** PixelDrop Architecture – Optimized Image Delivery with Lambda & CloudFront

![Optimized Architecture Diagram](./3775ec03-682a-49ce-bca0-f2cc702e0780.png)

### Added Components

* **AWS Lambda (Image Resizer)**

  * Triggered on S3 object uploads
  * Generates resized/optimized thumbnails
* **Amazon CloudFront**

  * CDN placed in front of S3
  * Caches optimized images globally

### Updated S3 Structure

* `originals/` – High-resolution images
* `thumbnails/` – Lambda-generated optimized images

### Request Flow (Phase 2)

1. Mobile app requests images via CloudFront
2. CloudFront checks edge cache
3. If cached → image served instantly
4. If not cached → CloudFront fetches from S3
5. Lambda processes and stores resized images
6. Subsequent requests are served from CloudFront edge locations

### Result

* Significantly faster image load times
* No partial or failed image loads
* Better performance on slow or unstable networks
* Reduced load on backend and S3

---

## Security & IAM

* EC2 instances use **IAM roles** (no hardcoded credentials)
* Least-privilege permissions for:

  * S3 access
  * ECR image pulls
* Database access restricted via security groups
* Private subnets prevent direct internet exposure

---

## Key Learnings

* Infrastructure design directly impacts application performance
* CDNs are critical for large static asset delivery
* Serverless services can solve specific performance problems elegantly
* Auto Scaling and health checks improve system resilience
* Cloud architecture evolves through real bottlenecks, not theory

---

## Tech Stack

* **Cloud:** AWS (EC2, VPC, ALB, ASG, RDS, S3, ECR, IAM, Lambda, CloudFront)
* **Containers:** Docker
* **Backend:** Node.js
* **Database:** PostgreSQL
* **OS & Tools:** Linux, Bash

---

## Future Improvements

* Replace Bastion Host with AWS SSM Session Manager
* Move secrets to AWS Secrets Manager / Parameter Store
* Add CloudWatch dashboards and alarms
* Provision entire infrastructure using Terraform
* Introduce CI/CD using GitHub Actions

---

## Conclusion

PixelDrop is not just a backend deployment project — it is an **evolving cloud architecture** built to understand how real-world systems scale, fail, and improve over time. The transition from a basic AWS setup to an optimized, CDN-backed, serverless-enhanced architecture reflects practical cloud engineering principles.

