CREATE TABLE IF NOT EXISTS users (
  user_id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  role VARCHAR(20) NOT NULL DEFAULT 'STAFF',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS patients (
  patient_id INT AUTO_INCREMENT PRIMARY KEY,
  full_name VARCHAR(100) NOT NULL,
  address VARCHAR(255) NOT NULL,
  contact_number VARCHAR(20) NOT NULL UNIQUE,
  email VARCHAR(100),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS dentists (
  dentist_id INT AUTO_INCREMENT PRIMARY KEY,
  dentist_name VARCHAR(100) NOT NULL,
  specialty VARCHAR(100),
  consultation_fee DECIMAL(10,2) NOT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  UNIQUE KEY uk_dentist_name (dentist_name)
);

CREATE TABLE IF NOT EXISTS treatments (
  treatment_id INT AUTO_INCREMENT PRIMARY KEY,
  treatment_code VARCHAR(30) NOT NULL UNIQUE,
  treatment_name VARCHAR(100) NOT NULL,
  treatment_fee DECIMAL(10,2) NOT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS appointments (
  appointment_id INT AUTO_INCREMENT PRIMARY KEY,
  appointment_no VARCHAR(30) NOT NULL UNIQUE,
  patient_id INT NOT NULL,
  dentist_id INT NOT NULL,
  treatment_id INT NOT NULL,
  appointment_date DATE NOT NULL,
  appointment_time TIME NOT NULL,
  consultation_fee DECIMAL(10,2) NOT NULL,
  treatment_fee DECIMAL(10,2) NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED',
  notes VARCHAR(500),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_appointment_patient FOREIGN KEY (patient_id) REFERENCES patients(patient_id),
  CONSTRAINT fk_appointment_dentist FOREIGN KEY (dentist_id) REFERENCES dentists(dentist_id),
  CONSTRAINT fk_appointment_treatment FOREIGN KEY (treatment_id) REFERENCES treatments(treatment_id),
  INDEX idx_appointment_no (appointment_no),
  INDEX idx_appointment_date (appointment_date),
  INDEX idx_dentist_slot (dentist_id, appointment_date, appointment_time)
);

CREATE TABLE IF NOT EXISTS bills (
  bill_id INT AUTO_INCREMENT PRIMARY KEY,
  appointment_id INT NOT NULL UNIQUE,
  consultation_fee DECIMAL(10,2) NOT NULL,
  treatment_fee DECIMAL(10,2) NOT NULL,
  total_amount DECIMAL(10,2) NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'UNPAID',
  paid_at TIMESTAMP NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_bill_appointment FOREIGN KEY (appointment_id) REFERENCES appointments(appointment_id)
);

INSERT IGNORE INTO users (username, password_hash, role)
VALUES ('admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'MANAGER');

INSERT IGNORE INTO dentists (dentist_name, specialty, consultation_fee, active) VALUES
('Dr. Nadeesha Perera', 'General Dentistry', 2500.00, TRUE),
('Dr. Kavindu Silva', 'Orthodontics', 3500.00, TRUE),
('Dr. Imesha Fernando', 'Oral Surgery', 4000.00, TRUE);

INSERT IGNORE INTO treatments (treatment_code, treatment_name, treatment_fee, active) VALUES
('CONSULT', 'Dental Consultation', 1500.00, TRUE),
('CLEAN', 'Teeth Cleaning', 5000.00, TRUE),
('FILL', 'Dental Filling', 7500.00, TRUE),
('EXTRACT', 'Tooth Extraction', 10000.00, TRUE),
('ROOT', 'Root Canal Treatment', 25000.00, TRUE),
('BRACES', 'Orthodontic Braces Review', 12000.00, TRUE);
