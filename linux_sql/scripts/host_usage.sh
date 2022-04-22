#!/bin/bash

#read from CLI
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

#Check CLI params
if [ $# -ne 5 ]; then
  echo 'Illegal command'
  echo 'Format should be psql_host psql port db_name psql_user psql_password'
  exit 1
fi

#Save machine stats in MB and hostname of current machine to variables
vmstat_mb=$(vmstat --unit M)
hostname=$(hostname -f)

#Current time in YYYY-mm-dd HH:MM:SS format
timestamp=$(date '+%Y-%m-%d %H:%M:%S')

#usage stats
memory_free=$(echo "$vmstat_mb" | awk 'NR==3 {print $4}' | xargs)
cpu_idle=$(echo "$vmstat_mb" | awk 'NR==3 {print $15}' | xargs)
cpu_kernel=$(echo "$vmstat_mb" | awk 'NR==3 {print $14}' | xargs)
disk_io=$(vmstat -d | awk 'NR==3 {print $10}' | xargs)
disk_available=$(df -BM / | awk 'NR==2 {print substr($4, 1, length($4)-1)}' | xargs)

#Subquery for finding ID by hostname in host_info table
host_id="(SELECT id FROM host_info WHERE hostname='$hostname')";

#PSQL query for inserting data into usage table
insert_stmt="INSERT INTO host_usage(timestamp, host_id, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available) VALUES('$timestamp', $host_id, '$memory_free', '$cpu_idle', '$cpu_kernel', '$disk_io', '$disk_available')"

#Set up environment variable for psql cmd
export PGPASSWORD=$psql_password

#Insert data into table
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"
exit $?