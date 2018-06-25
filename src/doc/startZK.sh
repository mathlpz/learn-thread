#!/bin/bash

SERVERS="172.28.16.45 172.28.16.53 172.28.16.62"
PASSWORD=69Ucc4oL

echo "to start zkServer..."
for SERVER in $SERVERS
do
	echo "starting zkServer..."$SERVER
    ssh root@$SERVER "source /etc/profile;/usr/local/apps/zookeeper-3.4.10/bin/zkServer.cmd start"
done
