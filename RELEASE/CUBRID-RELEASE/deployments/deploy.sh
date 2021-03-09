#!/bin/bash

# VARIABLES

# DEPLOY
bosh -n -d on-cubrid deploy --no-redact cubrid.yml
#    -o addon.yml
