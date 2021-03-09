#!/bin/bash

# SET VARIABLES

# DEPLOY
echo 'Y' | bosh -d test deploy --no-redact manifest/test.yml
    
