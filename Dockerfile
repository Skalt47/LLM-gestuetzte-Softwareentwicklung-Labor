FROM ollama/ollama

RUN apt-get update && apt-get install -y curl nginx && rm -rf /var/lib/apt/lists/*

COPY ollama-entrypoint.sh /ollama-entrypoint.sh
COPY ollama-nginx.conf /etc/nginx/nginx.conf

# Ensure Unix line endings inside the image to avoid shebang CR issues
RUN sed -i 's/\r$//' /ollama-entrypoint.sh

RUN chmod +x /ollama-entrypoint.sh

ENTRYPOINT ["/ollama-entrypoint.sh"]
