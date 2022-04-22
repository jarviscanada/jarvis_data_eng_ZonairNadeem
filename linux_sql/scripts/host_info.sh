#!/bin/bash

#Read from CLI
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

lscpu_out=`lscpu`
free_out=`free`

#Machine info
hostname=$(hostname -f)
cpu_number=$(echo "$lscpu_out"  | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out"  | egrep "^Architecture:" | awk '{print $2}' | xargs)
cpu_model=$(echo "$lscpu_out"  | egrep "^Model name:" | awk '{print $3" "s$4" "$5}' | xargs)
cpu_mhz=$(echo "$lscpu_out"  | egrep "^CPU MHz:" | awk '{print $3}' | xargs)
L2_cache=$(echo "$lscpu_out"  | egrep "^L2 cache:" | awk '{print substr($3, 1, length($3)-1)}' | xargs)
total_mem=$(echo "$free_out" | egrep "^Mem:" | awk '{print $2}' | xargs)

#Current time in YYYY-mm-dd HH:MM:SS format
timestamp=$(date '+%Y-%m-%d %H:%M:%S')

#PSQL query for inserting data into info table
insert_stmt="INSERT INTO host_info(hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, L2_cache, total_mem, timestamp) VALUES ('$hostname', '$cpu_number', '$cpu_architecture', '$cpu_model', '$cpu_mhz', '$L2_cache', '$total_mem', '$timestamp')"

#Set up environment variable for psql cmd
export PGPASSWORD=$psql_password

#Insert data into table
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"
exit $?