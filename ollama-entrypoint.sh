#!/usr/bin/env sh
set -e

rm -f /tmp/ready

ollama serve &

echo "Starting Ollama..."
until curl -s http://localhost:11434 > /dev/null; do
  sleep 1
done

MODELS="phi3:mini"

for MODEL in $MODELS; do
  if ! ollama list | grep -q "$MODEL"; then
    echo "⚡️ Pulling model: $MODEL"
    ollama pull "$MODEL"
  else
    echo "⛳️ Model '$MODEL' already present."
  fi
done

touch /tmp/ready

nginx -g "daemon off;"
