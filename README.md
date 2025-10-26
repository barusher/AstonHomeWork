## 1 ВАРИАНТ 
git clone <repo>

cd AstonHomeWork

docker-compose up -d postgres

docker run -it --network=astonhomework_default user-service-app
## 2 ВАРИАНТ 
mvn clean package

java -jar target/AstonHomeWork-1.0-SNAPSHOT.jar

# ВАЖНО!!!
#### При запуске локально, в файле hibernate.cfg.xml поменять строку c
#### <property name="hibernate.connection.url">jdbc:postgresql://postgres:5432/users_db</property>
#### на 
#### <property name="hibernate.connection.url">jdbc:postgresql://localhost:5432/users_db</property> 