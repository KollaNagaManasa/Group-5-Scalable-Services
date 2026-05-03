Kubernetes manifests have moved to **`banking-infra/k8s/`** (datastores with PVCs, apps with probes and resource limits).

Apply from the infra repo:

```bash
kubectl apply -f banking-infra/k8s/
```

When using this monorepo layout, paths are relative to the repo root: `banking-infra/k8s/`.
