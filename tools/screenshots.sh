#!/usr/bin/env bash
# Régénère les captures (docs/marketing/captures/<fr|en>/*.png, 1080x2340) sans émulateur ni téléphone :
# Paparazzi rend les écrans sur la JVM (module screenshots), le script du socle range les PNG.
# Usage : ./tools/screenshots.sh
set -euo pipefail
racine="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
exec "$racine/pilmil-core/marketing/capture.sh" "$racine"
