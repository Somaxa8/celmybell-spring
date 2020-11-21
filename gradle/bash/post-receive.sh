#!/usr/bin/env bash
rm -rf /root/celmybell/project
git clone -b OAuth /root/celmybell/git /root/celmybell/project
bash -x /root/celmybell/project/gradle/bash/deploy.sh