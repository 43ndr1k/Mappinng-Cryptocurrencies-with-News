# Backend

This section assumes you have Java 8, Docker, Docker Compose and Python 3 installed.

## Installation

The backend is a spring boot application and requires a database.
First, we have to start the database. Second, the backend can be started.
Finally, a script is executed, which loads some previously crawled data into the application.

### Database

We provide a preconfigured postgresql instance by using a docker container.
To start the container (and thus the database), simply type:

```
cd backend/docker
docker-compose up
```

Now the docker container should be running.

### Start the Backend

The simplest way to build an run the application is to use maven.

Install maven on your system:
```
sudo apt-get install maven
```

Then build and run the application as follows:

```
cd backend/
mvn spring-boot:run
```

### Import some data

At this point you should have a fully functional running application.
However, in order to have some data, you can import some (previously crawled) data for testing purposes.

```
python3 Main.py
```

Of course, the normal workflow would be to use the crawlers for a real use case.

## Other (Optional)

### API Documentation

The API Documentation can be found at the following URL:
http://127.0.0.1:8080/swagger-ui.html
