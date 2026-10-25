#!/usr/bin/env bash
set -euo pipefail

KEYCLOAK_VERSION="26.8.1"
KEYCLOAK_ROOT="${HOME}/.local/share"
KEYCLOAK_HOME="${KEYCLOAK_ROOT}/keycloak-${KEYCLOAK_VERSION}"
KEYCLOAK_ARCHIVE_URL="https://github.com/keycloak/keycloak/releases/download/${KEYCLOAK_VERSION}/keycloak-${KEYCLOAK_VERSION}.tar.gz"

mkdir -p "${KEYCLOAK_ROOT}"

if [[ ! -x "${KEYCLOAK_HOME}/bin/kc.sh" ]]; then
  archive="$(mktemp)"
  trap 'rm -f "${archive}"' EXIT
  curl --fail --location --silent --show-error "${KEYCLOAK_ARCHIVE_URL}" --output "${archive}"
  tar --extract --gzip --file "${archive}" --directory "${KEYCLOAK_ROOT}"
fi

ln -sfn "${KEYCLOAK_HOME}" "${KEYCLOAK_ROOT}/keycloak"

(
  cd apps/api
  ./gradlew --no-daemon testClasses
)

(
  cd apps/web
  npm ci
)
