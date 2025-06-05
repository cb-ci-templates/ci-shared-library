This repo contains sample Jenkins Pipelines, Shared Libraries and Pipeline Template catalogs.

## Requirements for Pipelines Using Ephemeral Pod Agents

- A **Kubernetes cluster** is required since pipelines use ephemeral pod agents.
- Ensure the **Jenkins Kubernetes Plugin** is installed, and a valid Kubernetes cloud configuration is set up.
  - 📘 [Plugin Configuration Guide](https://plugins.jenkins.io/kubernetes/#plugin-content-configuration)
- Agent pod templates rely on a Kubernetes **ConfigMap** to inject environment variables:
  - Create a ConfigMap containing required values (e.g., proxy settings).
  - Name the ConfigMap: `configmap-envvars`




 
