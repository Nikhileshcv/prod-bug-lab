# 🚨 ProdBug Lab
### How to Diagnose and Resolve Bugs That Occur in Production but Not in Development

**“If there is a bug in production but not in the development environment, how would you resolve it?”**

This project provides a **visual, interactive answer** to that question instead of a theoretical explanation.

---

## 🧠 Short Answer (Non-Technical)

A bug that appears only in production usually happens **not because the code is wrong**, but because **production is different from development**.

Differences can include:
- Configuration values
- Security rules
- Infrastructure (gateways, load balancers)
- Real data vs test data

This application demonstrates **how engineers identify, fix, and prevent such issues**.

---

## 🎯 What This Application Does

ProdBug Lab is an interactive full-stack application that allows users to:

- Toggle between **DEV** and **PROD** environments
- Run scenarios where:
    - The feature works in DEV
    - The same feature fails in PROD
- See a clear explanation of:
    - Root cause
    - Immediate mitigation
    - Permanent fix
    - Prevention strategy

The goal is to **make production debugging understandable to both technical and non-technical users**.

---

## 🖥️ How to Use the Application

1. Select **DEV** or **PROD** using the environment toggle
2. Choose a scenario
3. Click **Run**
4. Observe the result:
    - DEV → Success
    - PROD → Failure
5. Review the explanation provided for:
    - Why it failed
    - How it was fixed
    - How future failures are prevented

---

## 🧩 Scenarios Demonstrated

### 1️⃣ Database Authentication Failure (Prod Only)
- DEV uses local credentials → works
- PROD uses secure secrets → fails due to configuration mismatch

**Lesson:**  
Production failures often come from configuration drift, not broken logic.

---

### 2️⃣ API Gateway Route Mismatch (404 in Prod)
- DEV directly calls the service
- PROD routes traffic through an API gateway with a different path

**Lesson:**  
Infrastructure differences can cause production-only issues.

---

### 3️⃣ CORS / Security Restriction (Browser Fails in Prod)
- DEV allows all requests
- PROD enforces strict security policies

**Lesson:**  
Security rules frequently differ between environments and can block real users.

---

## 🧠 How Production Bugs Are Resolved (Process)

| Step | Description |
|----|----|
| Reproduce | Safely reproduce the issue in a controlled environment |
| Compare | Identify differences between DEV and PROD |
| Diagnose | Find the true root cause |
| Mitigate | Reduce impact on users immediately |
| Fix | Apply a permanent correction |
| Prevent | Add checks, tests, or monitoring to avoid recurrence |

This is the same approach used in real production systems.

---

## 🚀 Why This Project Matters

Most candidates *describe* how they would handle production issues.

This project:
- Shows the process visually
- Explains it in simple terms
- Demonstrates real-world engineering thinking
- Focuses on reliability, not just code

---

## 📌 Who This Project Is For

- Interviewers evaluating problem-solving skills
- Non-technical stakeholders seeking clarity
- Engineers learning real production debugging workflows

# 🚨 ProdBug Lab  
### How to Diagnose and Resolve Bugs That Occur in Production but Not in Development

🔗 **Live Demo:**  
https://prod-bug-pmq7ep8ov-nikhileshs-projects-08db730e.vercel.app/

🔗 **Backend API:**  
https://prod-bug-lab.onrender.com/api/scenarios

## ✅ Summary

If you understand this application, you understand how real production issues are identified, fixed, and prevented.

This project exists to make that process clear.
