#!/bin/bash
mkdir -p /tmp/aviatorsbot-scala-deploy

if [ -e /etc/aviatorsbot-scala/aviatorsbot.conf ]
 then mv /etc/aviatorsbot-scala/aviatorsbot.conf /etc/aviatorsbot-scala/aviatorsbot.conf-changed
fi