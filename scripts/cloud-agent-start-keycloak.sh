#!/usr/bin/env bash
set -euo pipefail

: "${KEYCLOAK_ADMIN_PASSWORD:?KEYCLOAK_ADMIN_PASSWORD must be configured as an environment secret}"

KEYCLOAK_HOME="${HOME}/.local/share/keycloak"
REALM_SOURCE="infra/keycloak/realm-weight-management-dev.json"
REALM_TARGET="${KEYCLOAK_HOME}/data/import/realm-weight-management-dev.json"

if [[ ! -x "${KEYCLOAK_HOME}/bin/kc.sh" ]]; then
  echo "Keycloak is not installed. Run scripts/cloud-agent-install.sh first." >&2
  exit 1
fi

mkdir -p "$(dirname "${REALM_TARGET}")"
cp "${REALM_SOURCE}" "${REALM_TARGET}"

export KC_BOOTSTRAP_ADMIN_USERNAME="${KEYCLOAK_ADMIN_USERNAME:-admin}"
export KC_BOOTSTRAP_ADMIN_PASSWORD="${KEYCLOAK_ADMIN_PASSWORD}"

exec "${KEYCLOAK_HOME}/bin/kc.sh" start-dev --http-port=8081 --import-realm
