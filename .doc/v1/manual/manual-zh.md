# 1.介绍

一些重要的地址（请实时更新此部分内容）：

* DOP：[http://www.dop.clsaa.com/](http://www.dop.clsaa.com/)
* APIGateway：[http://open.dop.clsaa.com](http://open.dop.clsaa.com/)
* Eureka：[http://121.42.13.103:30195/](http://121.42.13.103:30195/)
* API Doc：http://open.dop.clsaa.com/xxx-server/swagger-ui.html

* GitHub：https://github.com/doporg/dop
* Harbor：[https://registry.dop.clsaa.com](https://registry.dop.clsaa.com/)
* GitLab：[http://gitlab.dop.clsaa.com/](http://gitlab.dop.clsaa.com/)
* Jenkins：[http://jenkins.dop.clsaa.com/](http://jenkins.dop.clsaa.com/)
* Kubernetes：https://dashboard.k8s.dop.clsaa.com/
* Sonarqube：[http://sonarqube.dop.clsaa.com/](http://sonarqube.dop.clsaa.com/)

## 1.1.什么是DOP?

随着微服务的普及以及企业对需求交付的频率和质量要求的提高, 运维的复杂性和成本大大提高, DevOps是降低运维成本提高交付能力的重要方法. 然而, 虽然市场上有很多用于支持DevOps的开源工具, 但这些工具彼此之间相对独立, 数据不通, 使用起来繁琐、门槛高. 各大厂商也都在研发自己的DevOps平台, 如阿里云的云效, 华为云的DevCloud, 但这些产品均为商业产品, 我们无法获取DevOps整个流程中产生的数据, 更无法做定制化的开发, 因此决定开发一个开源项目--DOP. DOP对标云效和DevCloud两款产品, 提供方便易用的DevOps流程所涉及的功能, 从而可以采集数据、进行定制化开发, 辅助各项DevOps领域的研究的同时, 各项研究成功也能反馈到DOP中.

DOP是一个DevOps平台，你可以在DOP平台完成DevOps流程中的编码、构建、部署、测试等多个环节, 从而实现项目的CI

## 1.2.架构

### 1.2.1.整体架构

DOP整体上来看是一个多层的系统，其架构如图所示：

![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-23-165203.png)

**基础设施层**：平台管理的应用、及平台本身的服务都将部署在kubernetes中，一些工具会直接部署在虚拟机或物理机上。

**持久层**：使用MySQL和MongoDB，使用ceph进行分布式文件存储，对kubernetes中有状态应用的数据进行存储。

**中间件层**：使用阿里的RocketMQ作为消息队列，Redis作为缓存数据库。

**工具层**：接下来在右边是工具层。工具层主要包括一些开源的工具，这些工具提供一些基础的能力。业务层通过业务逻辑对这些能力进行组合整理最终提供给用户。比如jenkins提供流水线操作的能力（流水线的并行、等待）

**基础服务层**：左边是基础服务层，从这层开始是我们自己实现的一些服务。基础层服务为微服务架构和基本业务逻辑提供支持, 包括服务发现、审计、登录、权限管理、用户管理、消息服务，他们为业务层服务提供一些通用的功能。

**业务层**：业务层服务为用户使用的基本功能提供支持, 包括测试、代码管理、应用管理、流水线管理、容器镜像管理等服务，分别对应我们前面提到的用户使用的各种各样的功能。

**接入层**：接入层是一个API网关，主要处理客户端OAuth2.0认证和用户状态认证 两个认证流程。

**接口层**：包括用户使用的WebUI 统一登录 RESTful API。外部系统在通过客户端OAuth2.0认证后可以调用平台API。

**DOP系统使用了微服务架构，我们构建了基础层服务为微服务架构和基本业务逻辑提供支持，并且按照业务上下文将业务划分为测试、代码、应用管理、流水线等微服务，服务之间多通过HTTP协议通信，少数通过消息队列通信**。

### 1.2.1.基础层服务

#### 基础层服务列表

**基础层服务为微服务架构和基本业务逻辑提供支持，如下表所示：**

****

| 序号 | 名称          | 服务及其路径        | 前端及其路径                  | 开发人员                                                     | 功能 |
| ---- | ------------ | ------------------- | :---------------------------- | ------------------------------------------------------------ | -------- |
| 1    | 服务发现      | dop/discovery-server | URL:IP:8761 | 任贵杰([clsaa](https://github.com/clsaa))，实验室2018级 | 服务治理。所有服务的IP、端口信息都会注册到服务发现，服务之间相互调用时会从服务发现中心拉取服务IP、端口列表，通过客户端负载均衡选择实例进行调用。 |
| 2    | API网关      | dop/gateway-server | dop/dop-web/src/pages/Pipeline | 任贵杰([clsaa](https://github.com/clsaa))，实验室2018级 | 转发HTTP请求。将前端的HTTP请求转发到后端服务，转发过程中会进行OAuth2.0校验、用户登录token校验。 |
| 3    | 用户服务 | dop/user-server    | dop/dop-web/src/pages/Code   | 任贵杰([clsaa](https://github.com/clsaa))，实验室2018级 | 用户、账号相关业务逻辑 |
| 4    | 登录服务     | dop/login-server   | dop/dop-web/src/pages/Login  | 任贵杰([clsaa](https://github.com/clsaa))，实验室2018级 | 实现统一登录功能 |
| 5 | 权限管理 | dop/permission-server | dop/dop-web/src/pages/Permissions | 李质颖([552000264](https://github.com/552000264))，实验室2019级 | 权限管理，包括功能权限(基于RBAC)和数据权限(基于规则) |
| 6    | 操作审计服务  | dop/audit-server   | 无 | 任贵杰([clsaa](https://github.com/clsaa))，实验室2018级 | 对用户的操作进行审计，记录用户重要操作，业务方可查询功能使用记录 |
| 7 | 消息服务 | dop/message-server | 无 | 任贵杰([clsaa](https://github.com/clsaa))，实验室2018级 | 实现发送邮件的功能 |

#### API网关的OAuth2.0认证

> OAuth 2.0 是一个行业的标准授权协议。OAuth 2.0 专注于简化客户端开发人员，同时为 Web 应用程序，桌面应用程序，手机和客厅设备提供特定的授权流程。
>
> 它的最终目的是为第三方应用颁发一个有时效性的令牌 token。使得第三方应用能够通过该令牌获取相关的资源。常见的场景就是：第三方登录。当你想要登录某个论坛，但没有账号，而这个论坛接入了如 QQ、Facebook 等登录功能，在你使用 QQ 登录的过程中就使用的 OAuth 2.0 协议。
>
> 如果你想了解更多，其官方网址为：https://oauth.net/2/。下面我们来了解下 OAuth 协议的基本原理

OAuth2.0标准参考[RFC6749](https://tools.ietf.org/html/rfc6749)

**DOP项目当前使用OAuth2.0客户端模式，客户端(当前为dop-web或未来任何客户端)需要使用DOP后端服务的API都先要先接入OAuth2.0认证**（DOP颁发应用的Key和Secret）；在客户端请求DOP后端服务前，需要先调用API网关接口，进行OAuth2.0认证（获取AccessToken）；在客户端请求DOP后端服务时，携带之前获取的AccessToken，API网关会进行OAuth2.0验证(验证AccessToken)。具体可参考[DOP项目OAuth2.0接入文档](https://github.com/doporg/dop/blob/master/.doc/coding/4.OAth2.0%E6%8E%A5%E5%85%A5.md)，文档中有详细的文字说明及代码示例。

#### API网关的请求转发

> API 网关作为后端服务系统的唯一入口。它封装了系统内部架构，为每个客户端提供一个定制的 API 。由它负责服务请求路由、组合及协议转换。API 网关还有其它职责，如客户端验证、用户登录验证、负载均衡。

当你的客户端(web、微信小程序、APP)通过了OAuth2.0认证后，就可以通过API网关访问已注册在服务发现中心的后端服务（通过拉取某个服务的IP:PORT列表，进行客户端负载均衡后，选择一个合适的服务实例，转发HTTP请求）。DOP项目中网关的基本使用说明可参考[DOP项目API网关基本使用说明](https://github.com/doporg/dop/blob/master/.doc/coding/4.OAth2.0%E6%8E%A5%E5%85%A5.md#42%E7%BD%91%E5%85%B3%E5%9F%BA%E6%9C%AC%E4%BD%BF%E7%94%A8)

#### API网关与统一登录

API网关与用户登录、校验及用户注册的逻辑关系如下图所示：

![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-24-Jietu20190128-045817.jpg)

* 上图中黄线所代表的流程为用户注册流程(填入邮箱等信息->进入邮箱点击链接->账户被创建)
* 上图中蓝线所代表的流程为注册后，用户使用用户名/密码登录的流程，用户凭证校验通过后会返回JWT token作为用户已登录的凭证。用户登录后调用后端接口时，应在http请求头中添加键值对："x-login-token:JWTtoken"
* 上图中绿线所代表的流程为登录后，客户端调用后端API时用户登录状态检验的流程，此流程中API网关会将此token转换为用户的userId，并将用户的userId放到http请求头"x-login-user"。具体可以参考[接入统一登录的业务服务如何获取用户id](https://github.com/doporg/dop/blob/master/.doc/coding/1.编码规范.md#1313接入统一登录的业务服务如何获取用户id)。

### 1.2.2.业务层服务

#### 业务服务列表

业务层服务为用户使用的基本功能提供支持，如下表所示：

序号 | 名称 | 服务及其路径 | 项目类型 | 开发人员 | 功能 
-|-|-|:-|-|-
1 | 应用管理模块 | dop/application-server | dop/dop-web/src/pages/Projects |郑博文([zyriix](https://github.com/Zyriix))，实验室2019级|项目管理、应用管理、部署环境管理（Kubernetes集群配置）、部署历史
2 | 流水线管理模块 | dop/pipeline-server |dop/dop-web/src/pages/Pipeline|张富利([zhangfuli](https://github.com/zhangfuli))，实验室2019级|流水线管理与执行
3 | 代码管理模块 | dop/code-server |dop/dop-web/src/pages/Code|王舒瑶([waszqt](https://github.com/waszqt))，南大2019届|代码管理(简易版GitLab/GitHub)
4 | 镜像管理模块 | dop/image-server |dop/dop-web/src/pages/Image|辛志庭([ZhiTingXin](https://github.com/ZhiTingXin))，南大2019届|容器镜像管理(简易版Harbor/DockerHub)
5 | 测试管理模块 | dop/test-server |dop/dop-web/src/pages/TestCases|曾锡豪([151250010](https://github.com/151250010))，南大2019届|自动化接口测试管理与执行、手工测试管理

#### 业务模块与DevOps流程

业务模块与DevOps流程的关系，如下图所示：

![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-24-013749.png)

#### 单业务服务架构

单个业务服务的架构，如下图所示：

![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-24-014534.jpg)

每个服务都使用贫血模型，被设计为分层结构。controller对API网关转发来的请求进行处理，service层相对比较复杂，它可能会使用持久层对数据库进行操作或使用通用处理层调用API。

# 2.快速开始

## 2.1.相关依赖

| 组件        | 版本号         | 用途                                                         |
| ----------- | -------------- | ------------------------------------------------------------ |
| Java        | 1.8_211        | 当前DOP中全部后端服务均为Java项目，注意应使用较高Java版本否则无法识别Docker底层的Cgroup限制。 |
| Maven       | 3.5.2          | Java项目构建                                                 |
| SpringCloud | Finchley.SR2   | 微服务框架全家桶                                             |
| SpringBoot  | 2.1.1.RELEASE  | Spring项目依赖管理、自动配置，注意必须使用2.0.0+，开启WebFlux |
| MySQL       | 5.6            | 关系数据存储                                                 |
| Swagger     | 3.0.0-SNAPSHOT | 提供OpenAPI文档，注意必须使用3.0.0-SNAPSHOT版本支持WebFlux   |
| Node        | 8.0.0+         | 前端                                                         |
| React       | 16.8.6         | 前端                                                         |
| Iceworks    | 2.20.0         | 前端物料GUI，https://ice.work，项目前端均使用阿里飞冰构建    |
| Docker      | 18.03.1-ce     | 容器                                                         |
| Kubernetes  | 1.13.1         | 容器编排                                                     |
| Jenkins     | 2.150.1        | 流水线底层工具                                               |
| GitLab      | 11.6.1         | 代码管理底层工具                                             |
| Harbor      | 1.7.0          | 容器镜像管理底层工具                                         |
| Linux       | 7.3            | 宿主机操作系统版本                                           |

## 2.2.本地运行

1. **从master分支拉取最新代码**：```git clone https://github.com/doporg/dop.git```

2. **将项目导入IDEA**：IDEA->file->open(打开dop目录)

3. **将子项目导入IDEA**：右键子项目（如user-server）->Open Module Settings->Modules->ImportModule（选择user-server文件夹即可）

4. **为子项目设置启动参数**：打开应用配置->添加应用（Maven类型）->选择应用(如user-server)->设置启动类（SpringBoot的启动类）->设置JVM启动参数(不同启动参数会使应用运行时加载不同的配置文件，local为本地启动，加载的配置文件为application-local.yml)```-Dspring.profiles.active=local```![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-26-091124.png)![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-26-091316.png)

5. **调整配置文件**：根据自己的需求调整配置文件（数据库、域名、端口、日志等信息），每个项目的配置文件都在 项目/src/main/resources/application*.yaml中

6. **必须启动的服务**：

   1. discovery-server(必须启动，启动后可以通过localhost:8761访问服务发现页面，查看)
   2. gateway-server(必须启动，启动后可以通过localhost:8888/服务名/路径，访问已启动的服务，如localhost:8888/user-server/swagger-ui.html访问用户服务swagger接口)
   3. user-server(必须启动，业务服务均依赖用户服务)

7. **可选启动的服务**：

   1. login-server(如果需要本地启动前端登录则需启动)
   2. permission-server(如果本地启动的服务需要)
   3. 你想要调试的业务服务

8. **前端**：如果需要在本地启动前端)，进入dop-web目录```npm start```(在此之前将src/pages/API.js中// const host = "http://localhost:8888"的注释符号去掉，保证本地启动的前端项目

   > 如果你不需要在本地调试前端、调试的接口不需要服务之间相互调用，可以只在本地启动一个服务并设置```-Dspring.profiles.active=```为空，项目会使用application.yaml文件作为配置文件（使用服务器已经部署的eureka地址作为服务发现），可以方便启动一个项目进行调试，无需启动其他项目。但同时此方法启动的服务，无法调用服务器上已部署服务，服务器上已部署服务更无法调用到本地服务。

## 2.3.生产部署

> 生产环境使用Docker+Kubernetes部署，项目日志默认输出到项目所在目录的logs目录下，如user-server输出到 user-server.jar所在目录/logs/user-server/日期/xxx.log

1. **调整Dockerfile**配置：根据需求可调整Dockerfile内容(如基础镜像版本、新依赖、端口、挂载等)，Dockerfile在**项目名/Dockerfile**中
2. **调整Kubernetes配置**：子项目的Kubernetes配置文件均在**项目名/k8s.yaml**中，一般需要调整此文件中的镜像版本号、ingress域名、NodePort端口等。如果需要还可以调整实例数、CPU/内存限制、端口号等
3. **调整日志配置**：子项目（Java项目）的日志配置文件在**项目名/logback-spring.xml**中，如有需要可以进行自定义调整(如日志类型、切割规则等)
4. **调整项目配置文件**：生产环境项目配置文件为**项目名/application-production.yml**，一般需要调整此文件中的域名、数据库地址/密码、凭证/密匙信息。如果有需要，项目的任何配置都可以在此文件中进行配置。

> 如果当前生产环境（数据库、网络、文件等）无任何变化，只需在kubernetes集群更新相应的k8s.yaml文件即可（注意更新时镜像版本需要手动更新）。

## 2.4.CI/CD

> DOP项目使用标准的GitHubFlow，并搭建了一整套CI/CD流程，日常开发、部署、维护高度自动化，不论是在当前环境继续DOP开发还是需要重新搭建DOP环境，CD/CD流程与工具必不可少。

### 2.4.1.GitHubFlow

DOP采用标准GitHubFlow，使用部署主干的发布方式：[GitHub flow](http://scottchacon.com/2011/08/31/github-flow.html) 是 GitHub 所使用的一种简单的流程。该流程只使用两类分支，并依托于 GitHub 的 pull request 功能。在 GitHub flow 中，master 分支中包含稳定的代码。该分支已经或即将被部署到生产环境。master 分支的作用是提供一个稳定可靠的代码基础。任何开发人员都不允许把未测试或未审查的代码直接提交到 master 分支。

对代码的任何修改，包括 bug 修复、hotfix、新功能开发等都在单独的分支中进行。不管是一行代码的小改动，还是需要几个星期开发的新功能，都采用同样的方式来管理。当需要进行修改时，从 master 分支创建一个新的分支。新分支的名称应该简单清晰地描述该分支的作用。所有相关的代码修改都在新分支中进行。开发人员可以自由地提交代码和 push 到远程仓库。

当新分支中的代码全部完成之后，通过 GitHub 提交一个新的 pull request。团队中的其他人员会对代码进行审查，提出相关的修改意见。由持续集成服务器（如 Jenkins）对新分支进行自动化测试。当代码通过自动化测试和代码审查之后，该分支的代码被合并到 master 分支。再从 master 分支部署到生产环境。

![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-26-103626.png)

| 分支类型 | 命名规范                | 创建自 | 合并到 | 说明    |
| -------- | ----------------------- | ------ | ------ | ------- |
| feature  | feature_(功能名)        | master | master | 新功能  |
| fix      | fix_(bug名或issues编号) | master | master | bug修复 |

### 2.4.2.JenkinsPipeline

DOP使用JenkinsPipeline自动化拉取代码->构建->编译->打包镜像->推送镜像->部署镜像到Kubernetes集群整个流程。在每个项目的目录中都有一个Jenkinsfile，Jenkinsfile定义了流水线如何运行。文件在**项目名/Jenkinsfile**中。API网关的Jenkinsfile如下所示：

```
#!/groovy
pipeline{
	agent any

	environment {
		REPOSITORY="https://github.com/doporg/dop.git"
		SERVICE_DIR="gateway-server"
		DOCKER_REGISTRY_HOST="registry.dop.clsaa.com"
		DOCKER_REGISTRY="registry.dop.clsaa.com/dop/gateway-server"
	}

	stages {
		stage('pull code') {
			steps {
				echo "start fetch code from git:${REPOSITORY}"
				deleteDir()
				git "${REPOSITORY}"
                script {
                    time = sh(returnStdout: true, script: 'date "+%Y%m%d%H%M"').trim()
                    git_version = sh(returnStdout: true, script: 'git log -1 --pretty=format:"%h"').trim()
                    build_tag = time+git_version
                }
			}
		}

		stage('build maven') {
			steps {
                echo "star compile"
                dir(SERVICE_DIR){
                    sh "ls -l"
                    sh "mvn -U -am clean package"
                }
			}
		}

		stage('build docker') {
			steps {
                echo "start build image"
                echo "image tag : ${build_tag}"
                dir(SERVICE_DIR){
                    sh "ls -l"
                    sh "docker build -t ${DOCKER_REGISTRY}:${build_tag} ."
                }
			}
		}

       stage('push docker') {
            steps {
                echo "start push image"
                dir(SERVICE_DIR){
                  sh "ls -l"
                  withCredentials([usernamePassword(credentialsId: 'docker_registry', passwordVariable: 'docker_registryPassword', usernameVariable: 'docker_registryUsername')]) {
                      sh "docker login -u ${docker_registryUsername} -p ${docker_registryPassword} ${DOCKER_REGISTRY_HOST}"
                      sh "docker push ${DOCKER_REGISTRY}:${build_tag}"
                  }
                }
            }
        }

        stage('update yaml') {
            steps{
                echo "start change yaml image tag"
                dir(SERVICE_DIR){
                    sh "ls -l"
                    sh "sed -i 's/<BUILD_TAG>/${build_tag}/' k8s.yaml"
                    sh "cat k8s.yaml"
                }
            }
        }

		stage('deploy') {
			steps {
				echo "start deploy"
				dir(SERVICE_DIR){
				    sh "ls -l"
				    sh "kubectl apply -f k8s.yaml"
				}
			}
		}
	}
}
```

若更换新的部署环境，你需要改变动这个文件environment中的变量(镜像仓库地址、Git仓库地址等)

当Jenkinsfile调整完成后，可以到Jenkins中 New 任务->选择流水线，填入流水线名称并创建->Pipeline选项选择Pipline Script From SCM模式，填写Git地址与Jenkinsfile路径(如user-server：./user-server/Jenkinsfile)->保存并运行流水线。

# 3.详细介绍

本章会详细介绍各模块详细功能与核心业务流程

## 3.1.应用管理模块

> 应用管理模块用于组织管理应用(每个应用可以看成一个服务)，即保存了一个应用DevOps流程中所需的所有元数据，所以基本上其他所有的模块都会和应用管理模块有交互，其中联系最紧密的是流水线模块。另外，应用管理模块还可以为应用配置部署环境(当前只支持Kubernetes部署，因此可以通过应用管理模块简单配置Kubernetes集群)。

###  3.1.1.功能总览

![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-26-%E5%BA%94%E7%94%A8%E7%AE%A1%E7%90%86%E6%A8%A1%E5%9D%97-README.png)



### 3.1.2.核心业务实现

#### 创建项目

用户在前端输入参数后，由controller层接收参数后，校验参数是否合法，若非法则返回错误码与错误信息，若合法则转发至Service层的createProjects（）函数，该函数将参数构建成实体对象后，调用DAO层将实体存入数据库中。此函数还会用到外部同级服务ImageService，在创建项目的调用ImageService在Harbor中创建一个同名的项目。

![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-26-180258.png)

#### 编辑应用URL

首先前端发出请求，请求已登录用户可查看的镜像仓库地址和代码仓库地址以供其选择。分别由ImageController和CodeController接收，转发给ImageService. getImageUrls()方法与CodeService.findProjectUrlList()方法，这两个方法又分别调用ImageFeign.getRepoAddress()与CodeFeign.findProjectUrlList()方法，发送Http请求给外部的Image-Server与Code-Server，收到响应数据后，将数据按层回传至前端。用户此时就可以在下拉列表中选择镜像仓库地址与代码仓库地址了，除了选择已有的，用户也可以输入自定义的镜像仓库地址与代码仓库地址。在输入完必填和选填的URL信息后，前端将发送请求至AppUrlInfoController，校验参数无误后，调用AppUrlInfoService.updateAppUrlInfoByAppId()方法，在该方法处理数据、构建实体后由AppUrlInfoRepository更新到数据库中。

![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-26-180344.png)

#### 添加部署历史

在流水线服务完成部署后，将本次部署的各类信息传给本模块的AppEnvLogController.addLog()接口。该接口调用AppEnvLogService.AddLog()方法，该方法中同时调用AppEnvService.findEnvironmentDetailById()方法，查询此时该环境的环境信息。将接收的信息与本地环境信息处理、整合后，读入Resources文件夹下的Template文件，用信息替换模板文件中的占位符，随后使用AppEnvLogRepository存入数据库。

![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-26-180615.png)

#### 创建Kubernetes/Service

用户输入服务名称、所属命名空间等数据后，前端将请求发送至KubeYamlController. createServiceByNameSpace()方法，该方法首先校验参数是否无误。然后调用KubeYamlController. createServiceByNameSpace()方法，在该方法中首先使用PermissionService.checkPermission方法校验用户是否有该操作的权限，然后调用KubeYamlService.getCoreApi()方法，使用KubeCredentialService.queryByAppEnvId()方法查询出集群URL和Token，获得CoreApi对象后，根据用户所填的数据，调用CoreApi.createNamespacedService方法，该方法会根据构建API时所传入的URL和Token连接服务器，并创建服务。此后，根据用户选择的服务暴露方式决定是否创建Ingress，创建Ingress需要使用ExtensionsV1beta1Api，其他流程类似，不重复赘述。

![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-26-180819.png)

## 3.2.流水线模块

> 流水线管理模块用于管理DOP中所有的流水线，其底层实现依赖Jenkins的pipeline，相比于Jenkins使用起来更加简单方便，并且在多个环节打通各个模块的数据，不论是流水线运行所需的数据还是运行结束产生的结果，我们都能管理使用。

### 3.2.1.功能总览

![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-26-%E6%B5%81%E6%B0%B4%E7%BA%BF%E7%AE%A1%E7%90%86%E6%A8%A1%E5%9D%97README.png)



### 3.2.2.核心业务实现

#### 创建流水线

当开始创建流水线时，首先将流水线信息入数据库，然后根据流水线信息；向用户服务拿userCredentialV11（内涵用户docker账户名与密码）；向应用管理服务拿appBasicInfo（内涵用户git地址、docker镜像地址）、respositoryVersion（镜像版本号）、deploy（部署的yaml）、kubeCredentialWithTokenV1（内涵集群ip与token），根据这个信息去生成jenkinsfile并且在jenkins里面创建流水线并执行，最后将结果返回。

![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-26-181539.png)

#### 构建流水线

当开始构建流水线时，调用流水线管理服务的build方法，该方法首先在日志数据库中创建一条日志，然后根据流水线信息；向用户服务拿userCredentialV11（内涵用户docker账户名与密码）；向应用管理服务拿appBasicInfo（内涵用户git地址、docker镜像地址）、respositoryVersion（镜像版本号）、deploy（部署的yaml）、kubeCredentialWithTokenV1（内涵集群ip与token），根据这个信息去生成jenkinsfile并且在jenkins里面创建流水线并执行，将运行日志返回，前端拿到日志通知服务端将日志保存。

![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-26-181649.png)

#### 获取流水线运行日志

这个过程主要是blueoceanApi调用过程，首先根据流水线的id拿到所有的运行结果，找到某一运行结果拿到该运行结果的节点，找到某一结点拿到该节点的所有运行步骤，找到该步骤获取该步骤的日志。

![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-26-181738.png)



## 3.3.测试管理模块

> 测试管理模块用于管理测试用例，可以定义手工测试、自动接口测试用例，接口测试用例可以根据定义的策略自动执行。

### 3.3.1.功能总览

![测试管理模块](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-26-181902.png)



### 3.3.2.核心业务实现

#### 接口测试用例执行

如图所示，宏观角度实现并不复杂，在服务器的内存中维护着一个测试任务容器，用户通过系统提供的界面不断进行用例执行测试的时候，当请求到达服务器时候，会首先被InterfaceCaseController 接收，InterfaceCaseController 会将所有的用例相关信息包括用例完整信息，用例执行脚本，此时的执行者，用例执行失败重试次数等都从数据库中查询出来，并将数据包装成一个待执行的任务，成功添加到测试任务容器之后就会马上给用户进行反馈，并不会一直阻塞等待用例执行完成。

在服务器启动之后，会马上启动一定数目的任务执行线程，一直轮询任务容器拉取任务来进行执行，这样就成功将添加测试执行任务和真正的任务执行解耦开。

使用容器将测试任务进行存储再由一定数目的不同线程进行执行

 

![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-26-182113.png)

一个测试用例任务的执行可以分解为按序执行很多个相互之间逻辑独立的操作，但是这些操作之间可能会存在一定的依赖(后续执行的操作可能依赖当前执行完成的操作的响应结果)。如图：

![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-26-183040.png)在测试平台的实现中，每个测试任务主要由两种操作组成：一次HTTP请求，等待操作。等待操作就是简单的等待一定的时长后再继续执行后面的操作，下面阐述一下一次HTTP请求的完整执行过程：

![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-26-183116.png)

#### HTTP请求脚本解析

使用插件模式将解析逻辑进行解耦，在请求脚本操作被执行的过程中，需要进行请求路径参数解析，请求参数解析，请求头部解析，请求体解析等一系列的解析过程，才能得到真正的请求路径，请求参数，请求头部和请求体。

 ![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-26-183235.png)

如图所示，所有的解析操作都依赖插件池来完成，需要扩展解析操作，只需要增加对应的解析插件和定义同类型插件的执行顺序即可。

## 3.4.代码管理模块

> 代码管理模块用于应用代码的管理，其依赖GitLab作为底层实现，提供了GitLab功能子集。

### 3.4.1.功能总览

![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-27-%E4%BB%A3%E7%A0%81%E7%AE%A1%E7%90%86%E6%A8%A1%E5%9D%97README.png)

### 3.4.2.核心业务实现

#### 对接用户服务

1. 注册用户![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-27-020516.png)

2. 修改密码![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-27-020600.png)存在RegisterAccountConsumer和UpdateAccountConsumer两个消费者对象，分别消费注册消息队列和修改密码消息队列中的消息。以注册用户为例，RegisterAccountConsumer获得用户信息之后，先通过UserFeign获得公钥对密码解密，然后调用UserService的addUser方法。在方法中，调用Gitlab API创建用户，获得用户的access token，最后通过UserFeign将access token保存在用户模块数据库中。

#### 常规调用

以查询项目为例

![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-27-020827.png)

## 3.5.镜像管理模块

> 容器镜像管理模块用于管理Docker镜像，其依赖Harbor作为底层实现。

### 3.5.1.功能总览

![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-27-%E9%95%9C%E5%83%8F%E7%AE%A1%E7%90%86%E6%A8%A1%E5%9D%97README.png)

### 3.5.2.核心业务实现

#### 常规调用

#### 

![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-27-072707.png)

## 3.6.权限管理模块

> 权限管理服务是一个基础层服务用于各个业务服务的权限管理，当前已能够实现基于RBAC的功能权限和基于规则的数据权限管理。业务方调用相关接口便能配置、校验权限。

**业务服务接入权限管理应先阅读：[权限接入文档](https://github.com/doporg/dop/blob/master/.doc/coding/权限接入.md)，若有问题可联系[552000264](https://github.com/552000264)**

### 3.6.1.功能总览

![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-27-%E6%9D%83%E9%99%90%E7%AE%A1%E7%90%86%E6%A8%A1%E5%9D%97README.png)

### 3.6.2.核心概念解释

- 功能权限：用户是否可以执行某条功能，如项目管理员可以创建项目，删除项目，但不可以创建流水线，删除流水线。
- 数据权限：用户在使用某个功能权限时（例如查询所有项目），对可以得到的数据的范围的控制，或对当前操作是否可以在某条数据上生效做校验，如用户1身为项目管理员，可以查看自己的项目，但不可以查看用户2的项目。
- 功能点：一项功能，一般对应一个API。如：删除项目
- 角色：功能点集合。
- 数据规则：访问数据的规则，可以看做一个SQL。如 select * from t_project where projectId in (xxx)
- 数据规则作用域：设置数据规则时，规则生效的字段。以上面的select * from t_project where projectId in (xxx)为例，作用于为projectId。
- 用户数据：数据规则对应的真实的值。以上面的select * from t_project where projectId in (xxx)为例，用户数据即填入真实的in条件中数据。表明某个用户有某个项目的权限。

### 3.6.3.核心业务流程

#### 功能点管理

![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-27-080132.png)

其他服务模块在进行操作时，调用权限管理模块中相应的接口来验证用户是否具有功能点，则达到了功能点管理的目的

#### 数据权限管理

![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-27-080204.png)

创建数据规则：作用域名称用来规定这条数据规则对哪些数据起作用，例如想对一些项目起作用，那么根据项目的 D 字段“pro jectid”来制定规则，即根据“pro jectid“”的不同来限制访问数据规则，目前都填写“in”，表示可以操作的数据在某个范围中。选择角色，即哪一个角色可以使用这条规则

在数据规则下添加用户数据：创建完规则后，会有对规则的相应描述。例如身为项目管理员有权操作 projected in{x, x,. }的数据则为此规则添加数据时，选择要添加数据的用户，再填写作用域参数值例如想让用户 xxx 能操作项日 ID 为 1 的项日，则选择用户 xx，作用域参数值填写 1

#### 业务服务权限校验

![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-27-080322.png)

# 4.使用说明

> 本章介绍了DOP的基本功能与使用方法，在使用之前你需要进行一些创建账号、获取权限等准备工作

1. 访问：[http://www.dop.clsaa.com](http://www.dop.clsaa.com/)注册一个DOP账号
2. 接收注册邮件，点击链接激活账号后，找到DOP相关负责人添加DOP各项功能使用权限
3. 按照4.1或4.2中叙述的方式使用DOP部署你的项目

## 4.1.常规使用

### 4.1.1.准备阶段

#### 准备代码仓库

> DOP系统所有的代码拉取均使用Git完成，因此项目的代码必须放在Git仓库中。
>
> 你可以选择使用DOP自带的代码管理模块(代码仓库)，也可使用第三方代码仓库（如GitHub、GitLab等）代码仓库。需要注意的是使用第三方代码仓库，仓库的可见性必须设置为公开可见(当前DOP平台未对接GitHub、GitLab账号系统)；若使用DOP自带代码仓库则可以使用私有仓库。
>
> DOP提供的代码管理模块包括了GitLab的大部分功能，基本上可以在DOP的代码管理模块完成日常的代码管理工作。

1. 登入DOP，选择**代码管理**![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-28-170542.png)
2. 第一次使用DOP的代码管理最好添加你电脑的SSH公钥，方便后续开发时PUSH代码![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-28-170615.png)![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-28-170718.png)![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-28-170743.png)

3. 新建项目(代码仓库)![image-20190529010850042](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-28-170850.png)![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-28-170941.png)

4. 查看项目，你可以在项目列表页点击项目名称查看项目。在项目预览界面，你看到项目名称、图标、可见性、Git仓库地址、README文件，也可以下载或设置项目。![image-20190529011415219](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-28-171415.png)

5. 设置项目，点击设置，进入项目的设置页面。在设置页面可以编辑项目的基本信息(名称、描述、默认分支、可见等级还可以删除项目)；可以设置项目的保护分支(防止低权限人员随意修改重要分支的内容)；可以设置项目成员(将其他用户添加到项目并设置相应的角色)![image-20190529011942994](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-28-171943.png)![image-20190529012451309](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-28-172451.png)![image-20190529012528462](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-28-172529.png)

6. 浏览文件，选择**文件**，进入文件浏览界面。可以查看文件目录、文件内容、某个文件最近的一次commit信息与时间；可以点击某个文件名查看文件内容、可以点击某个commit信息查看本次commit变更。![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-28-174058.png)![image-20190529014125446](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-28-174126.png)![image-20190529014151492](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-28-174152.png)

7. 点击提交，进入项目提交记录页面。可以查看提交记录列表(commit message、时间、sha)；可以搜索某次commit；可以按分支查看commit；可以查看某次commit的文件信息；点击某次commit还可以查看这次commit所有文件的变化。![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-28-174540.png)
8. 点击分支，进入分支页面。可以查看当前项目所有的分支信息；可以新建、删除、搜索分支；可以查看某个分支最新的commit。![image-20190529014919699](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-28-174920.png)![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-28-175058.png)

9. 点击标签，进入标签页面。可以查看、搜索、删除、创建标签；可以查看标签对应的commit。![image-20190529020921950](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-28-180922.png)![image-20190529020950298](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-28-180951.png)

10. 点击合并请求，进入合并请求处理页面。可以看到需要处理的合并列表（名称、时间、操作人员）；可以创建合并请求；点击合并名称可以查看此次合并请求的详细信息；可以处理（打开、关闭、合并）合并请求。![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-28-181934.png)![image-20190529022008816](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-28-182009.png)![image-20190529022043469](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-28-182044.png)

#### 准备容器镜像仓库

> DOP所有的部署均使用Docker+Kubernetes完成，因此在部署之前需要将应用的Docker镜像存储起来，待Kubernetes集群接收到部署指令后，从镜像仓库拉取、运行镜像。
>
> 你的应用可以使用DOP自带的镜像管理模块(镜像仓库)，也可以使用第三方镜像仓库(DockerHub、阿里云容器镜像服务等)，但第三方镜像仓库访问权限必须设置为公开。
>
> 这里要说明镜像管理模块中的两个概念：命名空间(一般代表一个项目，是对镜像仓库的一个分类)、镜像仓库(一般代表一个应用，每个应用每次构建的镜像一般存入同一个镜像仓库中只不过版本号不同)、镜像(一般代表一个应用的某个版本)
>
> 以 docker pull registry.dop.clsaa.com/dop/application-server:2019032515435beb579 为例
>
> registry.dop.clsaa.com为镜像仓库域名，
>
> dop为命名空间，代表dop项目
>
> application-server为镜像名，代表应用管理server
>
> 2019032515435beb579为镜像版本号，代表某次构建docker镜像的版

1. 点击镜像管理，进入镜像管理页面。可以创建、检索、浏览、删除命名空间；可以设置命名空间的可见性(如果有权限的话，用户角色必须为此命名空间的Namespace Manager)。![image-20190529024334402](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-28-184334.png)![image-20190529024450073](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-28-184450.png)



2. 进入命名空间，点击命名空间名称进入命名空间页面。可以浏览、检索、删除命名空间中的镜像仓库；可以设置命名空间成员及角色；可以查看命名空间的操作日志。![image-20190529030720423](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-28-190721.png)![image-20190529030740422](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-28-190741.png)![image-20190529030830858](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-28-190832.png)![image-20190529030757009](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-28-190758.png)

3. 查看镜像仓库，点击命名空间中的镜像仓库名可进入镜像仓库页面。可以浏览、检索一个镜像不同版本的列表（镜像名、Docker版本、大小、摘要、可直接赋值pull命令）；可以删除镜像。要注意的是镜像仓库无法直接创建，当创建命名空间后直接往此命名空间中push镜像，会自动在此命名空间创建![image-20190529031128857](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-28-191129.png)

### 4.1.2.使用阶段

#### 项目管理

> 完成上述代码仓库、镜像仓库的准备工作后，需要创建一个项目(一个项目可以包含很多应用，而一个应用一般对应一个服务)，在你可以在项目中管理项目成员和应用。
>
> 当然，在创建项目之前你需要拥有DOP相应的使用权限，请联系相关负责人获取权限。

1. 查看全部项目，点击全部项目可以进入项目列表页面。可以浏览、检索、创建项目。![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-28-192448.png)

2. 查看并设置项目，点击项目名可以进入项目详情页面。可以查看项目信息、管理成员(添加、删除成员)、管理应用(浏览、检索、创建、删除应用)。![image-20190529032617060](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-28-192617.png)![image-20190529032634800](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-28-192636.png)![image-20190529032707737](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-28-192709.png)![image-20190529032733830](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-28-192734.png)

#### 应用管理

> 创建项目之后，需要在项目中创建一个应用，一个应用往往代表着一个Java服务、一个NodeWeb等

1. 查看应用列表，点击项目详情中的应用tab。可以查看到项目中的全部应用信息；可以创建应用；可以检索应用。![image-20190529033014135](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-28-193014.png)

2. 创建应用，点击创建应用按钮。填写应用名称、开发模式；填写Git仓库地址(可以填写第三方git地址也可以填写DOP内部git仓库地址)；填写镜像仓库地址(可以填写第三方镜像仓库地址也可以填写DOP内部镜像仓库地址)；上述两个地址若使用DOP自带的模块，可以直接以搜索的方式，搜索刚才已经创建过的仓库。![image-20190529033832306](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-28-193832.png)

3. 查看应用，点击项目中应用名称。可以查看、编辑应用基本信息(应用所属、基本信息、URL信息等)；可以浏览、创建、删除应用环境；可以浏览、创建、删除应用的变量。![image-20190529034256461](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-28-194258.png)![image-20190529034322479](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-28-194324.png)![image-20190529034337645](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-28-194339.png)

#### 应用环境管理

1. 查看应用环境列表（环境一般分为开发、测试、集成、生产），进入应用详情页面会点击环境配置。DOP默认会为每个应用创建一个日常开发环境。可以新建一个环境(当前只能选择Kubernetes集群部署);可以点击配置部署对某个环境进行详细的配置;可以点击部署历史查看某个应用在某个环境的部署历史.![image](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-29-154354.png)![image](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-29-154421.png)
2. 环境配置，点击配置部署可以对某个环境进行详细的配置.![image](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-29-154605.png)

#### 配置Kubernetes集群

1. Kubernetes集群认证，首先填入Kubernetes集群的认证信息(URL https://masterIP:6443和Token)，点击提交让DOP得以操控Kubernetes集群![image](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-29-160054.png)![image](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-29-160225.png)
2. 选择发布策略，当前只支持滚动升级
3. 选择Yaml文件来源
   1. 使用配置: DOP将根据你的选项配置为你自动生成Yaml文件![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-29-160503.png)
   2. 使用相对路径: 填写一个相对路径, DOP将从你项目的相对路径获取Yaml文件![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-29-160533.png)
4. 当选择使用配置方式后,选择命名空间,并选择已存在服务或创建服务
   1. 选择使用原有服务(不创建新服务): 该方式可以搜索并使用原有服务.![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-29-161148.png)
   2. 选择创建新服务: 改方式将根据配置创建一个新的Service![](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-29-161221.png)
5. 当选择创建新的服务后可以选择服务暴露到外网的方式.
   1. Ingress: 以域名的方式访问服务, 配置完成后要去公有云设置域名DNS解析到Kubernetes集群的Master节点. TargetPort为容器(你的服务)暴露的端口. 域名填入一个能够解析到master节点的域名![image](https://clsaa-markdown-imgbed-1252032169.cos.ap-shanghai.myqcloud.com/very-java/2019-05-29-161900.png)
   2. NodePort: 以MasterIP+Port的方式访问服务, 配置完成后可以直接通过MasterIP:Port访问服务,TargetPort为容器(你的服务)暴露的端口.NodePort为外网访问的端口(注意Kubernetes对NodePort范围有要求, 还要注意在公有云服务器访问策略中打开相应的端口允许外网访问) 
6. 设置副本数量(本服务运行多少个实例)
#### 创建流水线

1. 点击流水线管理，进入流水线列表页面，可以浏览所有流水线。

2. 点击创建流水线，可以创建一条流水线流水线，设置流水线基本信息及触发方式，最后设置流水线各阶段内容一般如下：

   1. 拉取代码：选择相应的应用/Git仓库地址
   2. 构建：根据你的项目类型 选择 Python项目、Maven项目、Node项目 其中一个
   3. 构建Docker镜像：选择 构建Docker镜像，选择环境、镜像地址等信息
   4. 推送Docker镜像：选择 推送Docker镜像，选择环境、镜像地址等信息
   5. 部署：选择 部署， 不许做其他任何配置![](http://markdown-img-bed-ali.oss-cn-hangzhou.aliyuncs.com/2019-06-01-171454.jpg)![image-20190602011523963](http://markdown-img-bed-ali.oss-cn-hangzhou.aliyuncs.com/2019-06-01-171527.png)

   

### 4.1.3.运行阶段

#### 运行流水线

1. 点击流水线管理，查看流水线列表
2. 点击查看某个流水线，若流水线已经执行过则可以看到流水线阶段可视化执行结果
3. 点击运行流水线，流水线开始运行![](http://markdown-img-bed-ali.oss-cn-hangzhou.aliyuncs.com/2019-06-01-171843.jpg)

#### 查看运行时日志

1. 点击流水线运行后，页面会实时更新
2. 可以点击页面中的流水线阶段，查看某个阶段执行的日志
	3. 若流水线阶段成功执行则阶段显示为绿色![](http://markdown-img-bed-ali.oss-cn-hangzhou.aliyuncs.com/2019-06-01-172124.jpg)
	4. 若流水线阶段执行失败则阶段显示为红色 ![](http://markdown-img-bed-ali.oss-cn-hangzhou.aliyuncs.com/2019-06-01-172133.jpg)

### 4.1.4.验证阶段

#### 查看部署历史

1. 流水线成功执行完后可以查看应用的部署历史(运行id，commitId，镜像版本号，环境快照，部署人，运行状态)部署历史中主要有三个信息
	1. 点击运行id可以查看此次流水线运行日志
	2. 点击commitId可以查看此次流水线运行对应代码变更
	3. 点击镜像版本号可以查看此次部署的docker镜像
	4. 鼠标放在部署环境上，可以查看此次部署的kubernetes环境快照![](http://markdown-img-bed-ali.oss-cn-hangzhou.aliyuncs.com/2019-06-01-172500.jpg)

#### 查看应用信息

1. 在Kubernetes的Dashboard查看容器运行的状态![](http://markdown-img-bed-ali.oss-cn-hangzhou.aliyuncs.com/2019-06-01-172533.jpg)
2. 配置好域名或虚拟机的防火墙、端口后访问服务![](http://markdown-img-bed-ali.oss-cn-hangzhou.aliyuncs.com/2019-06-01-172640.jpg)![](http://markdown-img-bed-ali.oss-cn-hangzhou.aliyuncs.com/2019-06-01-172649.jpg)

## 4.2.GitOps模式

>GitOps是一种持续交付的方式。它的核心思想是将应用系统的声明性基础架构和应用程序存放在Git的版本库中。将Git作为交付流水线的核心，每个开发人员都可以提交拉取请求（Pull Request）并使用Git来加速和简化Kubernetes的应用程序部署和运维任务。通过使用像Git这样的简单熟悉工具，开发人员可以更高效地将注意力集中在创建新功能而不是运维相关任务上（例如，应用系统安装，配置，迁移等）。

DOP在一定程度上支持GitOps，其允许你将所有的Jenkins和Kubernetes配置放在Git仓库中，仅仅使用DOP的流水线引擎(在项目中添加Jenkinsfile和k8s.yaml分别定义)。**当前DOP GitOps只支持DOP自带代码管理的私有仓库和第三方公有仓库**

你可以通过Jenkinsfile和k8s.yaml更精细化的配置应用的jenkins流水线和k8s集群

1. 准备一个项目，额外包含Jenkinsfile（定义流水线）和k8s.yaml![](http://markdown-img-bed-ali.oss-cn-hangzhou.aliyuncs.com/2019-06-01-173145.jpg)
2. 创建流水线，配置流水线选项选择：自带Jenkinsfile，并填写Jenkinsfile在项目的相对路径
3. 运行流水线

## 4.3.案例——部署SLR系统

### 4.3.1.整体流程

![](http://markdown-img-bed-ali.oss-cn-hangzhou.aliyuncs.com/2019-06-01-173458.jpg)

### 4.3.2.准备阶段

#### DOP支持Python项目

在流水线管理中添加Python模版任务以支持Python项目

![](http://markdown-img-bed-ali.oss-cn-hangzhou.aliyuncs.com/2019-06-01-173538.jpg)

#### 改造SLR：标准化

依据GitHub上大多数Python项目，标准化SLR项目。主要解决SLR未声明依赖问题：引入的组件依赖于本机Python版本

>创建项目时多参考标准项目，尽量完善README(说明依赖、如何运行等)

![](http://markdown-img-bed-ali.oss-cn-hangzhou.aliyuncs.com/2019-06-01-173614.jpg)
![](http://markdown-img-bed-ali.oss-cn-hangzhou.aliyuncs.com/2019-06-01-173620.jpg)

#### 改造SLR ：容器化

添加Dockerfile，将SLR容器化。主要解决SLR对本地Python版本的依赖，为应用提供独立的环境。

![](http://markdown-img-bed-ali.oss-cn-hangzhou.aliyuncs.com/2019-06-01-173649.jpg)
![](http://markdown-img-bed-ali.oss-cn-hangzhou.aliyuncs.com/2019-06-01-173657.jpg)

支持Docker镜像构建能为应用提供独立的声明式的部署环境。最好使用Docker部署应用，能届节省很多时间

![](http://markdown-img-bed-ali.oss-cn-hangzhou.aliyuncs.com/2019-06-01-173729.jpg)

### 4.3.3.使用阶段

#### 创建代码仓库推送代码

![](http://markdown-img-bed-ali.oss-cn-hangzhou.aliyuncs.com/2019-06-01-173805.jpg)
![](http://markdown-img-bed-ali.oss-cn-hangzhou.aliyuncs.com/2019-06-01-173812.jpg)

#### 创建镜像命名空间

>命名空间和镜像仓库是上下级关系，一个命名空间中可以有多个镜像仓库

![](http://markdown-img-bed-ali.oss-cn-hangzhou.aliyuncs.com/2019-06-01-173851.jpg)

#### 创建并进入项目

![](http://markdown-img-bed-ali.oss-cn-hangzhou.aliyuncs.com/2019-06-01-173915.jpg)

![](http://markdown-img-bed-ali.oss-cn-hangzhou.aliyuncs.com/2019-06-01-173921.jpg)

#### 创建应用

1. 可根据名称搜索创建好的Git地址和镜像仓库地址。
2. 若镜像地址不存在，则直接选择一个命名空间(slr)，在其后添加需要的镜像地址即可(test-slr-fifth)

![](http://markdown-img-bed-ali.oss-cn-hangzhou.aliyuncs.com/2019-06-01-173957.jpg)

#### 配置应用环境

>可为一个应用创建多个环境，每个环境会绑定一个Kubernetes集群，一般环境之间相互隔离

![](http://markdown-img-bed-ali.oss-cn-hangzhou.aliyuncs.com/2019-06-01-174021.jpg)

#### Kubernetes配置

为环境绑定Kubernetes集群认证信息，保证DOP可以操作Kubernetes集群

![](http://markdown-img-bed-ali.oss-cn-hangzhou.aliyuncs.com/2019-06-01-174133.jpg)

绑定认证信息后DOP可以获取、操作集群的命名空间、服务、外部访问方式，若添加下拉框中不存在的数据，DOP会自动创建，否则会使用已有集群数据

![](http://markdown-img-bed-ali.oss-cn-hangzhou.aliyuncs.com/2019-06-01-174149.jpg)

#### 创建流水线

一般流水线只有第二个步骤会不同，根据不同语言选择不同的构建方式，其他阶段所需参数，会根据你在应用管理模块填写的信息自动补全/选择
![](http://markdown-img-bed-ali.oss-cn-hangzhou.aliyuncs.com/2019-06-01-174229.jpg)
![](http://markdown-img-bed-ali.oss-cn-hangzhou.aliyuncs.com/2019-06-01-174235.jpg)

#### 运行流水线

流水线运行时可查看当前运行阶段、执行命令及命令运行日志

![](http://markdown-img-bed-ali.oss-cn-hangzhou.aliyuncs.com/2019-06-01-174309.jpg)
![](http://markdown-img-bed-ali.oss-cn-hangzhou.aliyuncs.com/2019-06-01-174320.jpg)

#### 查看部署历史

![](http://markdown-img-bed-ali.oss-cn-hangzhou.aliyuncs.com/2019-06-01-174345.jpg)

### 4.3.4.验证阶段

#### 查看SLR运行状态

在Kubernetes Dashboard查看Pod是否为running状态

![](http://markdown-img-bed-ali.oss-cn-hangzhou.aliyuncs.com/2019-06-01-174442.jpg)

#### 设置DNS

在云服务商设置DNS解析，解析到Kubernetes集群的master节点

![](http://markdown-img-bed-ali.oss-cn-hangzhou.aliyuncs.com/2019-06-01-174509.jpg)

### 4.3.5.验证阶段

访问域名，SLR可正常使用

![](http://markdown-img-bed-ali.oss-cn-hangzhou.aliyuncs.com/2019-06-01-174534.jpg)

# 5.编码规范

> 参与DOP项目必须遵循DOP编码规范，只有遵循了DOP编码规范的代码才可以被评审通过合并入master分支

由于篇幅过大不再详述，详情请阅读：[DOP编码规范](https://github.com/doporg/dop/blob/master/.doc/coding/1.编码规范.md)



