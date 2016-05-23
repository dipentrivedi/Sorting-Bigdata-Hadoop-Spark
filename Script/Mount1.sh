#!/bin/bash


eval `ssh-agent -s`

ssh-add hadoop.pem

sudo yum install mdadm

sudo umount -i /dev/xvdb

sudo mdadm --create --verbose /dev/md0 --level=0 --name=MY_RAID --raid-devices=3 /dev/xvdb /dev/xvdc /dev/xvde

sudo mkfs.ext4 -L MY_RAID /dev/md0

sudo mkdir -p /mnt/raid

sudo mount LABEL=MY_RAID /mnt/raid


sudo chmod 777 /mnt/raid/

echo -n "File Number : "
read answer

sudo hostname slave"$answer"

rm -rf hadoop/etc/hadoop/slaves

cd hadoop/etc/hadoop/

touch slaves

echo "slave$answer" >> slaves

sudo rm -rf /etc/hosts

sudo touch /etc/hosts

sudo chmod 777 /etc/hosts

cd /etc/

echo -e "127.0.0.1   localhost localhost.localdomain" >> hosts
echo -e "172.31.8.62 master" >> hosts
echo -e "172.31.8.228 slave1" >> hosts
echo -e "172.31.1.54 slave2" >> hosts
echo -e "172.31.7.100 slave3" >> hosts
echo -e "172.31.12.154 slave4" >> hosts
echo -e "172.31.1.40 slave5" >> hosts
echo -e "172.31.14.45 slave6" >> hosts
echo -e "172.31.14.136 slave7" >> hosts
echo -e "172.31.10.102 slave8" >> hosts
echo -e "172.31.13.77 slave9" >> hosts
echo -e "172.31.9.7 slave10" >> hosts
echo -e "172.31.14.64 slave11" >> hosts
echo -e "172.31.10.70 slave12" >> hosts
echo -e "172.31.12.177 slave13" >> hosts
echo -e "172.31.13.26 slave14" >> hosts
echo -e "172.31.7.254 slave15" >> hosts
echo -e "172.31.11.150 slave16" >> hosts

rm -Rf /mnt/raid/hdfs/*
rm -Rf /mnt/raid/tmp/*

cd /mnt/raid/

mkdir -p hdfs
mkdir -p hdfs/namenode
mkdir -p hdfs/datanode
mkdir -p tmp
mkdir -p tmp/hadoop-ec2-user

