# Как запустить приложение?
1. Скачайте с [релиза](https://github.com/comprehensiveMethod/TaskManager/releases/tag/publish) TaskManager.zip
2. Убедитесь что у вас стоит Docker и Docker Compose.
3. Распакуйте архив
4. Перейдите в папку проекта и выполните:
    docker-compose up --build
### Проект запускается на порте 8181
---
# Что делать после регистрации(/register) и аунтефикации(/auth)
1. Изначально стоит конечно открыть [swagger](http://localhost:8181/swagger-ui/index.html) и посмотреть как зарегистрироваться и аунтефицироваться
2. Вы получите токен
3. Токен надо скопировать и добавить Header к вашему запросу в API![image](https://github.com/user-attachments/assets/27d33d96-cd33-4c09-84e3-62bcb0e54a40)
4. Чтобы зайти под админом, советую просто в pgAdmin поменять id роли в таблице user_roles на id роли "ADMIN" (порт стандартный - 5050, PGADMIN_DEFAULT_EMAIL: admin@admin.com
      PGADMIN_DEFAULT_PASSWORD: root, добавить сервер connection: service-db, user: postgres, password: postgres)
---
## Надеюсь сильно не косячил и вам понравится :) 
