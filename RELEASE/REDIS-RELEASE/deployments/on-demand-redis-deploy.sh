#!/bin/bash

bosh -d redis-service deploy paasta_on_demand_redis_service.yml \
   -o addon-monitoring.yml \
   -l property_vars.yml
