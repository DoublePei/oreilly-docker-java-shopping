docker + k8s + ingress(ambassador api getway) + java(springboot) to build service



首先启动ambassador服务 

然后添加 
```bash
---
apiVersion: v1
kind: Service
metadata:
  labels:
    service: ambassador
  name: ambassador
  annotations:
    getambassador.io/config: |
      ---
      apiVersion: ambassador/v0
      kind: Mapping
      name: shopfront_stable
      prefix: /shopfront/
      service: shopfront:8010
      rate_limits:
        - descriptor: Example descriptor
      ---
      apiVersion: ambassador/v2
      kind: AuthService
      name: auth-server-service
      auth_service: "auth-service:8040"
      path_prefix: "/shopfront/"
      allowed_headers: []
spec:
  type: LoadBalancer
  ports:
  - name: ambassador
    port: 80
    targetPort: 80
  selector:
    service: ambassador

```

使用AuthService服务来做认证和限流，在springboot的项目的拦截器中。限流认真使用redis+lua 做认证和限流 一次搞定避免多次链接redis 
