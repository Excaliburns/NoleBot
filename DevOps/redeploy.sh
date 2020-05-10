#!/bin/bash

pkill -f 'nole.bot-2.4.jar'
nohup java -jar /home/ec2-user/NoleBot/build/libs/nole.bot-2.4.jar &
