import com.google.gson.reflect.TypeToken;
import io.kubernetes.client.ApiClient;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1Namespace;
import io.kubernetes.client.util.Watch;

public class KubernetesTest {
    public static void test() throws Exception {
        ApiClient client = new ApiClient();
        client.setBasePath("http://121.43.191.226:8080");
        client.setAccessToken("eyJhbGciOiJSUzI1NiIsImtpZCI6IiJ9.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJrdWJlLXN5c3RlbSIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJrdWJlcm5ldGVzLWRhc2hib2FyZC10b2tlbi1sY25kOCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50Lm5hbWUiOiJrdWJlcm5ldGVzLWRhc2hib2FyZCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50LnVpZCI6ImIyZDBlYTQzLTA5MzAtMTFlOS1hYmM3LTAwMTYzZTBlYzFjZiIsInN1YiI6InN5c3RlbTpzZXJ2aWNlYWNjb3VudDprdWJlLXN5c3RlbTprdWJlcm5ldGVzLWRhc2hib2FyZCJ9.KlrkaUDeoyWngUwbmGS2C7gpSixEYJYRgv52w9v_YVLe_uDO_SdHAaQanxG8W23RbKxYPRt_0S7haFy-gU5ngbuYPxHVvPMoB8gVrPX8dGOvYpxvs26eOEjibgnfJTmegWBgylSP9ULKqLTgJ3feFiUyMtd_metvaCSJInPDonDFlvNTzLIn8sOxE3Qxq3fAApNgkxNeuHT8vygznoLysv0I3Tzobhn5R78q5D1QL01AxRlAIKm57i6h5X7utoXrnt8JbuLlMk2ZERa8ANTlhTDhFOj4ODiAqWgN2gtDUmX9ACGHr7kbU8HW_COj4QMS6gLNdnI4bBxTCWVSL-er9Q");
        Configuration.setDefaultApiClient(client);
        CoreV1Api api = new CoreV1Api();

        Watch<V1Namespace> watch =
                Watch.createWatch(
                        client,
                        api.listNamespaceCall(
                                null, null, null, null, null, 5, null, null, Boolean.TRUE, null, null),
                        new TypeToken<Watch.Response<V1Namespace>>() {
                        }.getType());

        try {
            for (Watch.Response<V1Namespace> item : watch) {
                System.out.printf("%s : %s%n", item.type, item.object.getMetadata().getName());
            }
        } finally {
            watch.close();
        }

    }
}
