CREATE TABLE IF NOT EXISTS userEntities (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50),
    email VARCHAR(100) UNIQUE NOT NULL,
    age INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO userEntities (name, email, age) VALUES
('Sasha', 'nbk2615@yandex.ru', 24),
('Test', 'test@mail.com', 22)
ON CONFLICT (email) DO NOTHING;