## 1.Detailed introduction(unfinished)

This chapter will introduce the detailed functions and core business processes of each module in detail

#### 1.1 Application management service
> Application management serviceare used to organize and manage applications (each application can be viewed as a service), which stores all the metadata needed in an application DevOps process, so basically all other modules interact with the application management modules, the most closely connected of which is the pipeline service. In addition, the application management module can configure the deployment environment for the application (currently only Kubernetes deployment is supported, so the Kubernetes cluster can be simply configured through the application management module).

#####  1.1.1 Functions overview
![TIM截图20190620163702](https://user-images.githubusercontent.com/17808702/59833958-b942cf00-9379-11e9-8870-0dffc0fa27a0.png)
##### 1.1.2 Core business implementation
**Create project**
After the user enters the parameters in the web, the controller layer receives the parameters and verifies whether the parameters are legal. If not, the error code and error information are returned. If so, it is forwarded to the **createProjects  function()** in the Service layer.This function also uses the external peer imageService, which is called to create a project in Harbor with the same name.
![TIM截图20190620172301](https://user-images.githubusercontent.com/17808702/59837618-1d689180-9380-11e9-8342-1f3ca2760f25.png)
**Editor application URL**
The web first makes a request for the image warehouse address and code warehouse address that the logged-in user can view to choose from.By ImageController and CodeController receives respectively, forwarded to **ImageService. GetImageUrls () method **and **CodeService. FindProjectUrlList () method**, these two methods respectively called **ImageFeign. GetRepoAddress ()** and **CodeFeign. FindProjectUrlList () method**,Send Http requests to external image-server and code-server, and send back the data to the front end by layers after receiving the response data.At this point, the user can select the image warehouse address and code warehouse address from the drop-down list. In addition to selecting existing, the user can also enter a customized image warehouse address and code warehouse address.In the input after the mandatory and optional URL information, front will send a request to AppUrlInfoController, calibration parameters correctly, call **AppUrlInfoService. UpdateAppUrlInfoByAppId () method**, after the method processing data, build entity by AppUrlInfoRepository updates to the database.

![TIM截图20190620170446](https://user-images.githubusercontent.com/17808702/59836157-9c0fff80-937d-11e9-834b-d5c932edb900.png)

**Adding deployment history**
After they assembly line service deployed in the deployment of all kinds of information to the module **AppEnvLogController. AddLog ()** interface.The interface call AppEnvLogService. **AddLog ()** method, this method also called AppEnvService. **FindEnvironmentDetailById ()** method, query the environment environmental information at this time.After processing and integrating the received information with the local environment information, we read the Template file under the Resources folder, replace the placeholder in the Template file with the information, and then use AppEnvLogRepository to store it in the database.
![TIM截图20190620172839](https://user-images.githubusercontent.com/17808702/59838067-e3e45600-9380-11e9-8e21-da41690e51c6.png)
**Create Kubernetes/Service**
After the user enters data such as the service name, the namespace to which it belongs, the front-end sends the request to the **kubeyamlcontroller.createservicebynamespace () method**, which first verifies that the parameters are correct.Then call **KubeYamlController. CreateServiceByNameSpace () method**, in this method, the first to use **PermissionService. CheckPermission method** to check whether the user has the authority to the operation, and then call **KubeYamlService. GetCoreApi () method**,**Using KubeCredentialService. QueryByAppEnvId URL () method** to query the cluster and Token, after winning CoreApi object, according to the data of the user's filling, call **CoreApi. CreateNamespacedService method**, the method according to the incoming URL when build the API and Token to connect the server, and create the service.After that, it is decided whether to create Ingress or not based on the service exposure mode selected by the user. To create Ingress, **ExtensionsV1beta1Api** is required. Other processes are similar, so it is not necessary to repeat the above.
![TIM截图20190620172839](https://user-images.githubusercontent.com/17808702/59839706-a33a0c00-9383-11e9-8bf1-e022d7828f16.png)

#### 1.2 Pipeline management service

>Pipeline management serviceis used to manage all pipeline in DOP. Its pipeline that relies on Jenkins is simpler and more convenient to use than Jenkins, and the data of each module is broken through in multiple links. We can manage and use both the data required by pipeline operation and the results generated after operation.

