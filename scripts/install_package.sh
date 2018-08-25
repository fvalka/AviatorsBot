#!/bin/bash
dpkg -i /tmp/aviatorsbot-scala-deploy/target/aviatorsbot-scala_1.0_all.deb

CONFIG_DIR=/etc/aviatorsbot-scala

if [ -e "${CONFIG_DIR}/aviatorsbot.conf-changed" ]
then
    if [ ! -e "${CONFIG_DIR}/aviatorsbot.conf-base" ]
     then cp "${CONFIG_DIR}/aviatorsbot.conf" "${CONFIG_DIR}/aviatorsbot.conf-base"
    fi

    cp "${CONFIG_DIR}/aviatorsbot.conf-base" "${CONFIG_DIR}/aviatorsbot.conf-new-base"

    git merge-file --theirs "${CONFIG_DIR}/aviatorsbot.conf" "${CONFIG_DIR}/aviatorsbot.conf-base" "${CONFIG_DIR}/aviatorsbot.conf-changed"

    mv "${CONFIG_DIR}/aviatorsbot.conf-new-base" "${CONFIG_DIR}/aviatorsbot.conf-base"
fi

service aviatorsbot-scala restart