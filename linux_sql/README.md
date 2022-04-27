# Linux Cluster Monitoring Agent

# Introduction:
The Linux Cluster Monitoring Agent was created as a tool for the LCA team to monitor the resource usage of a Cluster of Linux nodes/servers running CentOS 7. It is designed to monitor each of these Devices via a local switch port communicating through internal IPV4 addresses. The test were done through a virtual machine, that acted as a node, created and hosted on the Google Cloud Platform. The data is collected and stored in a containerized RDBMS on one of the nodes in the network to be used as analytical data that's purpose is to check for hardware failures and plan for hardware usage optimization. A crontab was implemented to see the current usage of the nodes in the network in a set 1 minute interval. The data is tested and observed using Postgres SQL. The Application was built and managed using Git and uploaded to Github through an SSH connection.

Technologies used listed :

1. Linux (Centos 7)
2. Docker
3. Postgres SQL
4. Bash
5. Git
6. Github
7. Google Cloud platform
8. Crontab

# Quick Start:
- Start a psql instance using psql_docker.sh

```
1. Run a script to create a docker container to hold the SQL database
   ./scripts/psql_docker.sh create [db_username][db_password]
   ```
```
2.  Rerun the script to start the docker container
    ./scripts/psql_docker.sh start [db_username][db_password]
```

```
3.  Connect to the psql instances and create the database host_agent
    psql -h localhost -U postgres -W
    postgres=# CREATE DATABASE host_agent;
```

```
4.  Execute the ddl.sql script on the host_agent database
    psql -h localhost -U postgres -d host_agent -f sql/ddl.sql
```

```
5. Insert the hardware specification data in host_info table using the host_info.sh script.
   ./scripts/host_info.sh psql_host psql_port db_name psql_user psql_password
```

```
6. Insert the hardware usage data into the host_usage table using the host_usage.sh script.
    ./scripts/host_usage.sh psql_host psql_port db_name psql_user psql_password
```

```
7. Enter in the Crontab editor.
    Crontab -e
```

```
8. In the Crontab editor, automated the host_usage script in 1 minute interval and log the stout into a log file.
    * * * * * bash ~/linux_sql/host_agent/scripts/host_usage.sh localhost 5432 host_agent postgres password > /tmp/host_usage.log
```

```
9. Use queries.sql script to test the database against different queries (more details on the queries below).
    psql -h localhost -U postgres -d host_agent -f sql/queries.sql
```

# Implementation:
To begin the project, we started by setting up our GitHub repo to save every version of our project. Then we continued by setting up a docker instance for us to store our database. Then we proceeded to set up our tables where we would store the data received from our nodes. The next step was to code our script to fetch the required data from said nodes (host_info and host_usage). Once everything was verified and working, the crontab was implemented to periodically get the resource usage of the node. We could then proceed to create the SQL queries to filter the data and simplify the information received. Finally, we created the README file.

# Scripts:
- **psql_docker.sh**
  
    This scripts creates, start, or stop the docker container that holds the PSQL database
  
    ```./scripts/psql_docker.sh create|start|stop [db_username][db_password] ```


- **host_info.sh**

  Retrieves the information of the node's hardware specification : formatted as ; hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache, total_memory, and timestamp and inserts it into the PSQL database.
  
    ```./scripts/host_usage.sh psql_host psql_port db_name psql_user psql_password ```


- **host_usage.sh**

    Retrieves the current use of the node's hardware : formatted as ; timestamp, hostname, free_memory, cpu_idle, cpu_kernel, disk_io, and disk_available

    ```./scripts/host_usage.sh psql_host psql_port db_name psql_user psql_password ```


- **ddl.sql**
  
    The Sql file changes to host agent database then creates host_info and host_usage tables
  
    ```psql -h localhost -U postgres -d host_agent -f sql/ddl.sql```


- **Crontab** 
  
    The Cron Job automates the collection of the nodes hardware usage per 1 minute intervals and logs the stout into /tmp/host_usage.log

# Database Modeling:
* host_info
  - id : SERIAL (PRIMARY KEY)
  - hostname : VARCHAR
  - cpu_number : INT
  - cpu_architecture : VARCHAR
  - cpu_model : VARCHAR
  - cpu_mhz : FLOAT
  - L2_cache : INT
  - total_mem : INT
  - timestamp : TIMESTAMP
  
* host usage
  - timestamp : TIMESTAMP
  - host_id : SERIAL (FOREIGN KEY)
  - memory_free : INT
  - cpu_idle : INT
  - cpu_kernel : INT
  - disk_io : INT
  - disk_available : INT
  
# Tests:
1. The psql_docker.sh script was tested by building and running the container and using docker ps -a to verify that this was the case. Tests were also done to make sure all the arguments were provided.

2. The host_info and host_usage scripts were tested by compparing inserted data to the data from /proc/cpuinfo, free, lscpu, vmstat , and uname commands.

3. The ddl.sql script was tested by running it in the terminal and checking if the tables were created in the database

4. The queries.sql script was tested by inserting sample values into the host_info and host_usage tables and checking if the tables were successfully created.


