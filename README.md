\# Hospital Management System



A Java-based Command Line Interface (CLI) application for managing hospital records, patient registrations, doctor profiles, and appointment scheduling using JDBC and MySQL.



\## 🛠️ Tech Stack

\* \*\*Language:\*\* Java

\* \*\*Database:\*\* MySQL

\* \*\*Database Connectivity:\*\* JDBC (`mysql-connector-j`)



\## Features

\* \*\*Patient Management:\*\* Register new patients and view existing records.

\* \*\*Doctor Management:\*\* View available doctors and their specializations.

\* \*\*Appointment System:\*\* Book appointments between patients and doctors.

\* \*\*Database Integration:\*\* Connects securely to local MySQL database instance using dynamic environment variables and fallback defaults.



\## Database Setup



Run the following SQL statements in your MySQL instance before executing the application:



```sql

CREATE DATABASE IF NOT EXISTS hospital;

USE hospital;



CREATE TABLE IF NOT EXISTS patients (

&#x20;   id INT AUTO\_INCREMENT PRIMARY KEY,

&#x20;   name VARCHAR(255) NOT NULL,

&#x20;   age INT NOT NULL,

&#x20;   gender VARCHAR(10) NOT NULL

);



CREATE TABLE IF NOT EXISTS doctors (

&#x20;   id INT AUTO\_INCREMENT PRIMARY KEY,

&#x20;   name VARCHAR(255) NOT NULL,

&#x20;   specialization VARCHAR(255) NOT NULL

);



CREATE TABLE IF NOT EXISTS appointments (

&#x20;   id INT AUTO\_INCREMENT PRIMARY KEY,

&#x20;   patient\_id INT NOT NULL,

&#x20;   doctor\_id INT NOT NULL,

&#x20;   appointment\_date DATE NOT NULL,

&#x20;   FOREIGN KEY (patient\_id) REFERENCES patients(id),

&#x20;   FOREIGN KEY (doctor\_id) REFERENCES doctors(id)

);

```



\## Compilation \& Execution



1\. \*\*Place the MySQL JDBC Driver (`mysql-connector-j-9.6.0.jar`) in the root directory.\*\*

2\. \*\*Compile the project:\*\*

&#x20;  ```bash

&#x20;  javac -cp "mysql-connector-j-9.6.0.jar;." HospitalManagementSystem/\*.java

&#x20;  ```

3\. \*\*Run the application:\*\*

&#x20;  ```bash

&#x20;  java -cp "mysql-connector-j-9.6.0.jar;." HospitalManagementSystem.HospitalManagementSystem

&#x20;  ```



