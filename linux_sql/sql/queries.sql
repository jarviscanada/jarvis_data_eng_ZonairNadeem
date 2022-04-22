--Display hosts grouped by CPU number and ordered by CPU number and total memory
SELECT
       cpu_number,
       id AS host_id,
       SUM(total_mem) AS total_mem
FROM host_info
GROUP BY
         cpu_number,
         id
ORDER BY
         cpu_number ASC,
         total_mem DESC;

--Function for rounding timestamp every 5 minutes
CREATE FUNCTION round5(ts timestamp) RETURNS timestamp AS
    $$
    BEGIN
        RETURN date_trunc('hour', ts) + date_part('minute', ts):: INT / 5 * INTERVAL '5 min';
    END
    $$
        LANGUAGE PLPGSQL;

--Retrieve average memory usage of host within 5-min intervals
SELECT
       host_id,
       host_info.hostname,
       round5(host_usage.timestamp) AS rounded_timestamp,
       AVG(host_info.total_mem/1024-memory_free) AS avg_used_mem_percentage
FROM host_usage
    LEFT JOIN host_info ON host_info.id = host_usage.host_id
GROUP BY
         host_id,
         host_info.hostname,
         rounded_timestamp
ORDER BY host_id;

--Query for detecting host failure
--Assume that a server has failed when it inserts less than three data points within 5-min interval.
SELECT
       host_id,
       round5(timestamp) AS rounded_timestamp,
       COUNT(host_id) AS num_data_points
FROM host_usage
GROUP BY
         host_id,
         rounded_timestamp
HAVING COUNT(host_id) < 3
ORDER BY host_id;