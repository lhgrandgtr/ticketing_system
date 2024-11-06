# Event Ticketing System

### Setting up the postgresql database
    I pulled a docker image containing a postgresql database for the ticketing system. Make sure you have install docker engine and the docker compose plugin on your system
    and run the below command on you terminal in the directory with the `docker-compose.yml` file in it.(or you can build the container using docker gui.)
```bash
    docker-compose up -d
```
    After the container starts to run, run the below command,
```bash
    docker exec -it postgres psql -U admin
```
    The above command should bring you to the postgresql cli. Type the below commands to create the tables and the grant access

```sql
    CREATE TABLE ticket;
    GRANT ALL PRIVILEGES ON DATABASE "ticket" TO admin;
```