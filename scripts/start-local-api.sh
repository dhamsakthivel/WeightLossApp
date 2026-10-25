#!/usr/bin/env bash
set -euo pipefail

export SPRING_PROFILES_ACTIVE=local
exec ./apps/api/gradlew -p apps/api bootRun
