# DOP

Emerging from the agile culture, DevOps extremely emphasizes automation and heavily relies on tools in practice. Given the rapidly increasing number and diversity of the tools for DevOps, we aims to simplify the DevOps practices and enable development and operation efficiency by leveraging the combined benefits of tooling and automation. To achieve this aim, we developed and implemented a unified web-based DevOps platform (DOP), through integrating tools to DevOps process and continuous delivery pipeline in an automated manner. DOP provides five basic functionalities: code management, pipeline management, test management, container image management, and application management. Moreover, advanced features important for both practitioners and researchers are also available at DOP, e.g., visual demonstration of execution results of each phase in the whole pipeline for easier use, data collection through logs for process mining research, and logs’ visualization for quicker bug localization. Initial cases successfully adopting DOP indicate the effectiveness of our platform.

# 1. Architecture design

We designed DOP using microservices architecture and decomposed it into microservices based on its business logic and service capabilities. Most of the microservices of DOP communicate with each other through HTTP protocol, and only a small minority use the message queue. The architecture design of DOP includes eight layers:

![微信图片_20190618210227](https://user-images.githubusercontent.com/17808702/59684676-166d4200-920d-11e9-894d-480cb3432fad.png)

**Infrastructure layer**：Applications managed by DOP and services of the DOP itself are all deployed in Kubernetes, and some tools are directly deployed on virtual machines or physical machines.

**Persistence layer**：MySQL and MongoDB are used for persistent storage, and Ceph is used to store distributed files,e.g., some stateful data applications in Kubernetes.

**Middleware layer**：RocketMQ is used as the message queue and Redis is used as the cache database.

**Tool Layer**：Tool layer mainly includes some open source tools, based on which to provide some basic capabilities of DOP. These capabilities can be combined in the Business Service Layer through business logic later. For example, some operations related to CI pipeline (pipeline parallelism, waiting) are implemented through wrapping the functions of Jenkins.

**Fundamental Service Layer**：This layer contains some services of DOP itself. Services in this layer could support some basic business logic, including service discovery, audit, login, authority management, user management,
and message service, which are able to provide some common functions for business Layer services.


**Business layer**：This layer are partitioned into five services: testing, code, application management, pipeline management, and container image management. These services correspond to the various functions used by users, which are introduced later. [See more explanations about the design of these services in this layer.](https://github.com/doporg/dop/blob/master/.doc/manual/mind-map.md)

**Access Layer**：Access Layer is an API gateway, which mainly deals with two authentication processes: client OAuth 2.0 authentication and user authentication.

**Interface Layer**：This layer includes Web UI, SSO, and RESTful API used by users. External systems can call the platform API after it has been authenticated by the client OAuth 2.0.

**DOP system uses the micro-service architecture. We build the basic layer services to support the micro-service architecture and basic business logic. According to the business context, we divide the business into micro-services such as testing, code, application management, pipeline and so on. Most of the services communicate through HTTP protocol, and a few communicate through message queue.**

The relationship between business modules and DevOps processes is shown in the following figure:

![微信图片_20190618210231](https://user-images.githubusercontent.com/17808702/59684675-15d4ab80-920d-11e9-9e21-b64597c532f5.png)

Developers push the code to code repository of an application, triggering the WebHook of the code management service, which can send an HTTP request to the pipeline management service to start a pipeline. When the pipeline is started, the source code of an applicaiton is pulled from the code repository, after which a build task can be executed. Then a test task is submitted to the test service, and a Docker image could be built and pushed to the image management service. Finally, the application can be deployed to the target environment, whose latest container image is pulled by the Kubernetes cluster and starts running.


# 2. Usage

> This chapter introduces the basic functions and usage of DOP. Before using it, you need to do some preparation work, such as creating an account and obtaining permissions

1. Visit：[http://www.dop.clsaa.com](http://www.dop.clsaa.com/)Sign up for a DOP account
2. After receiving the email of registration, click the link to activate the account, find the person in charge of DOP and add access to various functions of DOP
3. Deploy your project using DOP as described in 1.1 or 1.2

## 2.1. Routine use

### 2.1.1. Preparatory phase

#### Prepare the code repository 

> All code pulling in DOP systems is done using Git, so project code must be stored in Git repositories.
>

> You can choose to use DOP's own code management module (code repository) or third-party code repository (such as GitHub, GitLab, etc.).It should be noted that the third-party code repository is used, and the visibility of the repository must be set to be publicly visible (currently the DOP platform is not connected to GitHub and GitLab account system);You can use a private repository if you use DOP's own code repository.
>
> The code management module provided by DOP includes most of the functions of GitLab, and can basically complete daily code management work in the code management module of DOP.

1. Log in DOP and select **Code Management**![select-code-management](https://user-images.githubusercontent.com/17808702/59644753-e2f4ce00-91a0-11e9-8f41-06b74c184941.png)
2. The first time you use DOP code management, it is better to add your computer's SSH public key to facilitate the subsequent development of PUSH code.![ssh1](https://user-images.githubusercontent.com/17808702/59644769-f99b2500-91a0-11e9-96c3-9b4f5f2a65dd.png)
![ssh2](https://user-images.githubusercontent.com/17808702/59644771-fa33bb80-91a0-11e9-8b1c-abc6f422b596.png)
![ssh3](https://user-images.githubusercontent.com/17808702/59644772-fa33bb80-91a0-11e9-9308-f20e7c99b442.png)

3. Create code repository![createcode](https://user-images.githubusercontent.com/17808702/59644808-264f3c80-91a1-11e9-8fd9-4ba1388ca338.png)

4. To view the repository, you can click on the repository name on the repository list page to view the repository.In the repository preview screen, you can see the repository name, icon, visibility, Git repository location, README file, and you can also download or set the repository.![code](https://user-images.githubusercontent.com/17808702/59644879-880fa680-91a1-11e9-812a-8364a905d248.png)

5. To set up the project, click Settings to enter the project Settings page.In the Settings page, you can edit the basic information of the project (name, description, default branch, visible level and delete the project);You can set up the protection branch of the project (to prevent people with low permissions from modifying the content of important branches at will);You can set project members (add other users to the project and set the appropriate roles)![codesetting](https://user-images.githubusercontent.com/17808702/59644902-a2498480-91a1-11e9-8c5b-23cd882c118f.png)
![codebranch](https://user-images.githubusercontent.com/17808702/59644943-d624aa00-91a1-11e9-9dc2-d757eac7d312.png)
![codemembers](https://user-images.githubusercontent.com/17808702/59644961-eb99d400-91a1-11e9-8bea-a9e8d454f37e.png)


6. Browse files, select **Files** to enter the file browsing interface.You can view the file directory, file contents, the latest commit information and time of a file;You can click on a file name to view the contents of the file, or click on a commit information to view the commit changes.![TIM截图20190618082341](https://user-images.githubusercontent.com/17808702/59645079-8e525280-91a2-11e9-9ca1-e50a45a1ef93.png)
![TIM截图20190618082355](https://user-images.githubusercontent.com/17808702/59645080-8e525280-91a2-11e9-8f2f-c37a88f36b26.png)
![TIM截图20190618082416](https://user-images.githubusercontent.com/17808702/59645081-8eeae900-91a2-11e9-8a69-d4fced2c2c2b.png)
![codefiles2](https://user-images.githubusercontent.com/17808702/59645128-e12c0a00-91a2-11e9-8976-c20b0dda1d87.png)


7. Click **Commits** to enter the project submission record page.You can view the commit record list (commit message, time, sha);You can search for a commit;You can view commit by branch;You can view the file information for a commit;Click on a commit to see the changes to all the commit files.![codecommit](https://user-images.githubusercontent.com/17808702/59645142-f30dad00-91a2-11e9-9eb3-22384735066b.png)

8. Click the **Branches** to enter the branch page.Can view all branch information of the current project;Can create, delete, and search branches;You can see the latest commit for a branch.![codebranch1](https://user-images.githubusercontent.com/17808702/59645161-189ab680-91a3-11e9-8bd7-68f396227016.png)
![codenewbranched](https://user-images.githubusercontent.com/17808702/59645193-3a943900-91a3-11e9-9a58-6d9c13039e68.png)


9. Click the **Tags** to enter the tag page.You can view, search, delete, and create labels.You can see the commit corresponding to the label.![codetag](https://user-images.githubusercontent.com/17808702/59645226-63b4c980-91a3-11e9-855b-4494266a158e.png)
![codenewtag](https://user-images.githubusercontent.com/17808702/59645229-657e8d00-91a3-11e9-8c34-486d5eeec69e.png)

10. Click **Merge Requests** to enter merge request processing page.You can see the list of merges that need to be processed (name, time, operator);A merge request can be created;Click the merge name to view the details of the merge request;Can handle (open, close, merge) merge requests.![TIM截图20190618083315](https://user-images.githubusercontent.com/17808702/59645329-d8880380-91a3-11e9-8b9e-16d5fef024c4.png)
![codemerge](https://user-images.githubusercontent.com/17808702/59645333-dc1b8a80-91a3-11e9-8497-6ddaf083a542.png)
![codemerge2](https://user-images.githubusercontent.com/17808702/59645334-dc1b8a80-91a3-11e9-9ce2-3fd7ce973535.png)

#### Prepare the container image repository

> All deployment of DOP is completed with Docker and Kubernetes, so before deployment, the application Docker image needs to be stored, and after the Kubernetes cluster receives the deployment instruction, it will pull and run the image from the image repository.
>
> Your application can use the image management module (image repository) that comes with DOP, or you can use the third-party image repository (DockerHub, ali cloud container image service, etc.), but the access right of the third-party image repository must be set to be public.
>
> Here to explain image management module of the two concepts: namespace (usually on behalf of a project, is a classification of image repository), image repository (generally on behalf of an application, each application image every building general into the same image but with different version number of the repository), image (generally represent a certain version of an application)
>
> docker pull **registry.dop.clsaa.com/dop/application-server:2019032515435beb579**
>
> **registry.dop.clsaa.com** is the image repository domain name，
>
> dop is namespace,representing project
>
> application-server is image's name，representing application management service
>
> 2019032515435beb579 is image tag,representing a version of the docker image once

1. Click **Image management** to enter the image management page.You can create, retrieve, browse, and delete namespaces.Visibility of namespaces can be set (user roles must be the Namespace Manager of this Namespace, if they have permissions).![image](https://user-images.githubusercontent.com/17808702/59645439-53e9b500-91a4-11e9-9ef0-90b96c5b582e.png)



2. To enter the namespace, click the namespace name to enter the namespace page.You can browse, retrieve, and delete the mirror repository in the namespace.You can set namespace members and roles;You can view the operation log for namespaces.![image2](https://user-images.githubusercontent.com/17808702/59645477-81cef980-91a4-11e9-8938-5fd4e390c7fa.png)
![image4](https://user-images.githubusercontent.com/17808702/59645478-81cef980-91a4-11e9-80bd-b5fc9733afbc.png)
![image5](https://user-images.githubusercontent.com/17808702/59647066-5819d080-91ac-11e9-8d05-311a7b31f7b6.png)


3. To view the image repository, click the name of the image repository in the namespace to enter the image repository page.Browse and retrieve the list of different versions of a image (image name, Docker version, size, abstract, can be directly assigned to pull command);You can delete the repository.It is important to note that the image repository cannot be created directly, and when you create a namespace and push an image directly into it, it is automatically created in that namespace![image3](https://user-images.githubusercontent.com/17808702/59645479-82679000-91a4-11e9-852e-3e67f4c97048.png)

### 1.1.2. Usage phase

#### project management

> After the above code repository, image repository preparation, you need to create a project (a project can contain many applications, and an application usually corresponds to a service) where you can manage project members and applications.
>
> Of course, you need to have access to DOP before creating the project. Please contact the responsible person for access.

1. To view all projects, click **All Projects** to enter the list of projects page.You can browse, retrieve, and create project.![project1](https://user-images.githubusercontent.com/17808702/59645529-ea1ddb00-91a4-11e9-9766-beb905950b53.png)

2. View and set the project. Click the project name to enter the project details page.You can view project information, manage members (add and remove members), and manage applications (browse, retrieve, create and delete applications).![project4](https://user-images.githubusercontent.com/17808702/59645551-07eb4000-91a5-11e9-93ad-3ea3b132718b.png)
![project2](https://user-images.githubusercontent.com/17808702/59645552-0883d680-91a5-11e9-8383-3e7050989cd2.png)

#### application management

> After creating the project, you need to create an application in the project, which often represents a Java service, a NodeWeb, and so on 

1. To view the list of applications, click the application TAB in project details.View all application information in the project;You can create applications;You can retrieve applications.![application2](https://user-images.githubusercontent.com/17808702/59645588-48e35480-91a5-11e9-885b-0c5b3c861b41.png)

2. To create an application, click the **Create an application**.Fill in the application name and development mode;Fill in Git repository address (you can fill in the third-party Git address or fill in the DOP internal Git repository address);Fill in the image repository address (you can fill in the third party image repository address or the DOP internal image repository address);For the above two addresses, if you use the modules that come with DOP, you can search the repositories you have just created by searching directly.![TIM截图20190618084405](https://user-images.githubusercontent.com/17808702/59645587-484abe00-91a5-11e9-855a-d9e88c244219.png)



3. To view the application, click the application name in the project.Can view and edit application basic information (application ownership, basic information, URL information, etc.);Browse, create, and delete application environments.You can browse, create, and delete application variables.![application3](https://user-images.githubusercontent.com/17808702/59645589-48e35480-91a5-11e9-85c9-37fef8436b13.png)
![env1](https://user-images.githubusercontent.com/17808702/59645670-c0b17f00-91a5-11e9-9745-eb2fc75000ac.png)


#### Application environment management

1. View the list of application environments (generally divided into development, test, integration and production), and click **Envionment Profile** when entering the application details page.By default, DOP creates a daily development environment for each application.You can create a new environment (currently only available for Kubernetes cluster deployment);You can click **Deployment configuring** for a specific environment configuration;You can click **Deployment History** to see the deployment history of an application in an environment.![env2](https://user-images.githubusercontent.com/17808702/59645671-c14a1580-91a5-11e9-8adc-6d5b662f95b1.png)
![TIM截图20190618084954](https://user-images.githubusercontent.com/17808702/59645744-1a19ae00-91a6-11e9-9004-0d60052e2814.png)


2. Environment profile, click **Deployment configuring** to configure an environment in detail.![cluster1](https://user-images.githubusercontent.com/17808702/59645904-e25f3600-91a6-11e9-95ac-fa0697401a4b.png)


#### Configure the Kubernetes cluster

1. To authenticate the Kubernetes cluster, first fill in the authentication information of the Kubernetes cluster (URL https://masterIP:6443 and Token) and click submit to allow DOP to manipulate the Kubernetes cluster.![TIM截图20190618085805](https://user-images.githubusercontent.com/17808702/59645966-3a963800-91a7-11e9-9dde-55ebeff03c08.png)
2. Select the release strategy, which currently supports only rolling upgrades
3. Select the Yaml file source
      1. Use configuration: DOP will configure you to automatically generate Yaml files based on your options
      2. Use the relative path: fill in a relative path, and DOP will get the Yaml file from the relative path of your project
      ![TIM截图20190618090211](https://user-images.githubusercontent.com/17808702/59646087-d758d580-91a7-11e9-9a0d-ada82036b476.png)
4. When you choose to use configuration, select the namespace, and select existing or created services
      1. Choose to use the original service (without creating a new service): this way you can search for and use the original service.![TIM截图20190618090817](https://user-images.githubusercontent.com/17808702/59646252-adec7980-91a8-11e9-8b66-42018b2b2fd8.png)
      2. Select create new Service: this will create a new Service according to the configuration![cluster3](https://user-images.githubusercontent.com/17808702/59646272-d07e9280-91a8-11e9-81ac-105aba89e6a9.png)
5. When you choose to create a new service, you can select how the service is exposed to the outer network.
      1. Ingress: Access the service as a domain name. Once configured, go to the public cloud and set up DNS resolution to the Master node of the Kubernetes cluster. TargetPort is the port exposed by the container (your service)![cluster2](https://user-images.githubusercontent.com/17808702/59646271-d07e9280-91a8-11e9-91f8-d17ea897542b.png)![cluster3](https://user-images.githubusercontent.com/17808702/59646272-d07e9280-91a8-11e9-81ac-105aba89e6a9.png)

      2. NodePort: Access the service in MasterIP+Port, and after configuration, access the service directly through MasterIP:Port. TargetPort is the Port exposed by the container (your service).
6. Set the number of replicas (how many instances this service runs)

#### Pipeline creation

1. Click pipeline management to enter the pipeline list page and browse all the pipelines.

2. Click "create pipeline" to create a pipeline, set the basic information of assembly line and trigger mode, and finally set the contents of each stage of assembly line as follows:

      1. Pull code: select the appropriate application /Git repository address
      2. Build: choose a Python project, Maven project, or Node project based on your project type
      3. Build Docker image: choose to build Docker image, select environment, image address and other information
      4. Push Docker image: select push Docker image, select environment, image address and other information
      5. Deploy: select deploy and do not do any other configuration![pipeline1](https://user-images.githubusercontent.com/17808702/59646410-674b4f00-91a9-11e9-9101-904c4754cbcc.png)![TIM截图20190618091224](https://user-images.githubusercontent.com/17808702/59646411-674b4f00-91a9-11e9-9afb-a12a72ff12da.png)![pipeline2](https://user-images.githubusercontent.com/17808702/59646412-67e3e580-91a9-11e9-9326-17291ddc40c0.png)![TIM截图20190618091322](https://user-images.githubusercontent.com/17808702/59646413-67e3e580-91a9-11e9-8f07-582a601bd294.png)![TIM截图20190618091300](https://user-images.githubusercontent.com/17808702/59646414-687c7c00-91a9-11e9-803d-ad6a0c0420fb.png)

   

### 2.1.3. run phase

#### Running pipeline

1. Click **Pipeline management** to view pipeline list
2. Click to view a pipeline. If the pipeline has been executed, you can see the visual execution result of pipeline stage
3. Click **Run pipeline**, and the pipeline starts running![TIM截图20190618091702](https://user-images.githubusercontent.com/17808702/59646853-4be14380-91ab-11e9-9769-919ebd7afe11.png)

#### View the runtime log

1. Click the pipeline to run, the page will be updated in real time
2. You can click the pipeline stage in the page to see the log of a stage execution
	3. If the pipeline stage is successfully executed, the stage is shown in green![pipeline4](https://user-images.githubusercontent.com/17808702/59646926-a7abcc80-91ab-11e9-931e-cba4634f659e.png)

	4. If the pipeline stage fails, the stage is shown in red![pipeline6](https://user-images.githubusercontent.com/17808702/59646925-a7133600-91ab-11e9-94db-9f4ce54d1343.png)

### 2.1.4.verification stage

#### View deployment history

1. After successful pipeline execution, you can view the application's deployment history (run id, commitId, mirror version number, snapshot of the environment, deployer, run status). There are three main pieces of information in the deployment history
	  1. Click run id to view the pipeline running log
	  2. Click commitId to see the corresponding code change for this pipeline run
	  3. Click on the image version number to view the deployed docker image
	  4. Mouse over the deployment environment to view a snapshot of the kubernetes environment for this deployment![pipeline7](https://user-images.githubusercontent.com/17808702/59646965-d45fe400-91ab-11e9-87e2-ff1b5ec3a010.png)![pipeline8](https://user-images.githubusercontent.com/17808702/59646966-d4f87a80-91ab-11e9-937b-55d424354154.png)


#### View application information

1. Check the status of containers running in Kubernetes' Dashboard
![k8s](https://user-images.githubusercontent.com/17808702/59647000-fa858400-91ab-11e9-8c95-1340af655b84.png)


2. test.dop.clsaa.com has been deployed successfully
![k8s2](https://user-images.githubusercontent.com/17808702/59647001-fb1e1a80-91ab-11e9-88ac-3fa619e9a6f0.png)
![TIM截图20190618093359](https://user-images.githubusercontent.com/17808702/59647046-3f111f80-91ac-11e9-9599-3c79921e58e5.png)
