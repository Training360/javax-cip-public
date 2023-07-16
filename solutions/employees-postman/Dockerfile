FROM postman/newman:5-ubuntu
RUN npm install -g newman-reporter-htmlextra
WORKDIR /tests
COPY collections .
ENTRYPOINT ["newman", "run", "employees.postman_collection.json", "-e", "test.postman_environment.json", "-r", "cli,htmlextra", "--reporter-htmlextra-export", "reports"]