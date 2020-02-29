kubectl cluster-info dump --namespace kube-system | grep authorization-mode
如果您--authorization-mode=Node,RBAC:

kubectl apply -f https://getambassador.io/yaml/ambassador/ambassador-rbac.yaml

否则：
kubectl apply -f https://getambassador.io/yaml/ambassador/ambassador-no-rbac.yaml