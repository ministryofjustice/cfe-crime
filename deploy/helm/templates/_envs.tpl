{{/* vim: set filetype=mustache: */}}
{{/*
Environment variables for web and worker containers
*/}}
{{- define "app.envs" }}
env:
  - name: CMA_API_OAUTH_URL
    value: {{ .Values.cmaApi.oauthUrl }}
  - name: CMA_API_OAUTH_CLIENT_ID
    valueFrom:
      secretKeyRef:
        name: kube-secrets
        key: cma_api_oauth_client_id
  - name: CMA_API_OAUTH_CLIENT_SECRET
    valueFrom:
      secretKeyRef:
        name: kube-secrets
        key: cma_api_oauth_client_secret
{{- end }}
