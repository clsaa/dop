import {
    Button,
    Dialog,
    Feedback,
    Field,
    Form,
    Icon,
    Input,
    Loading,
    NumberPicker,
    Select,
    Switch
} from "@icedesign/base";
import React, {Component} from 'react';
import API from "../../../../API";
import Axios from "axios"
import "./K8sInfoPage.scss"
import {injectIntl} from "react-intl";

const FormItem = Form.Item;
const Option = Select.Option;
const Toast = Feedback.toast;
const formItemLayout = {
    labelCol: {span: 8},
    wrapperCol: {span: 16}
};
const {Combobox} = Select;

class K8sInfoPage extends Component {

    constructor(props) {
        super(props);
        this.field = new Field(this)
        this.editField = new Field(this)
        this.yamlPathField = new Field(this)
        this.yamlEditorfield = new Field(this)
        this.state = {
            appEnvId: props.appEnvId,
            nameSpaceData: [],
            editMode: false,
            serviceData: [],
            deploymentData: [],
            yamlData: [],
            containerData: [],
            useIngress: "ingress",
            createService: false,
            yamlMode: "profile",
            editDeploymentYaml: false,
            loading: true
        }

    }

    /*
    * 获取集群内所有命名空间
    */
    getNameSpaceData() {
        let _this = this
        let namespaceUrl = API.gateway + "/application-server/app/env/" + this.state.appEnvId + "/cluster/allNamespaces"
        Axios.get(namespaceUrl)
            .then((response) => {
                console.log("nameSpace", response)
                _this.setState({
                    nameSpaceData: response.data
                })
            })
    }

    /*
   * 获取集群内指定命名空间的所有服务
   */
    getServiceData(namespace) {

        if (this.field.getValue("nameSpace") === "") {
            this.setState({
                serviceData: ""
            })
        } else {
            let _this = this;
            let namespaceUrl = API.gateway + "/application-server/app/env/" + this.state.appEnvId + "/cluster/allServices"
            Axios.get(namespaceUrl, {
                params: {
                    namespace: namespace,
                }
            })
                .then((response) => {
                    console.log("services", response);
                    _this.setState({
                        serviceData: response.data
                    })
                })
        }
    }

    /*
      * 获取集群内指定命名空间的对应服务的所有部署
      */
    getDeploymentData() {
        if (this.field.getValue("nameSpace") === "" || this.field.getValue("service") === "") {
            this.setState({
                deploymentData: "",
                containerData: ""
            })
        } else {
            let _this = this;
            let namespaceUrl = API.gateway + "/application-server/app/env/" + this.state.appEnvId + "/cluster/allDeployment"
            Axios.get(namespaceUrl, {
                params: {
                    namespace: _this.field.getValue("nameSpace"),
                    service: _this.field.getValue("service")
                }
            })
                .then((response) => {
                    console.log("response", response);
                    _this.setState({
                        deploymentData: response.data.deployment,
                        containerData: response.data.containers
                    });
                    console.log(_this.field.getValue("deployment"), _this.state.containerData)
                })
        }
    }


    /*
  * 获取该服务在数据库中的属性
  */
    getYamlData() {
        let _this = this;
        this.setState({
            loading: true
        })
        let url = API.gateway + "/application-server/app/env/" + this.state.appEnvId + "/yaml"
        Axios.get(url)
            .then((response) => {
                // console.log("yaml", response);
                // console.log(response.data.yamlFilePath !== "")

                //如果该服务在数据库中无属性
                if (response.data === "") {
                    _this.setState({
                        editMode: true,
                        yamlData: []
                    })
                    //获取所有命名空间
                    _this.getNameSpaceData()
                    _this.setState({loading: false})
                } else {
                    //判断是否使用yaml文件路径 分别赋值
                    if (response.data.yamlFilePath !== "") {

                        _this.setState({
                            editMode: false,
                            yamlData: response.data,
                            yamlMode: 'path',
                            loading: false
                        })


                    }
                    //如果是使用配置，则根据配置去集群获取对应的信息，加载到下拉框以便用户更改
                    else {
                        _this.setState({
                            editMode: false,
                            yamlData: response.data,
                            yamlMode: 'profile',

                        })
                        _this.getNameSpaceData()
                        _this.getServiceData(this.state.yamlData.nameSpace)
                        _this.yamlEditorfield.setValue("deploymentYaml", response.data.deploymentEditableYaml)
                        _this.setState({loading: false})
                    }

                    // _this.field.setValue("yamlMode", this.state.yamlMode)
                    //
                    // if(response.data.deploymentEditableYaml !=="")
                    // {
                    //     _this.setState({
                    //         editDeploymentYaml:true
                    //     })
                    // } else {
                    //     _this.setState({
                    //         yamlMode: false
                    //     })
                    // }

                }
            })
            .catch((response) => {
                _this.setState({
                    editMode: true,
                    yamlData: []
                })
                //获取所有命名空间
                _this.getNameSpaceData()
                _this.setState({loading: false})
            })
    }

    componentDidMount() {
        this.getYamlData()
    }

    switchIngress(e, values) {
        this.setState({
            useIngress: values.value
        })
    }

    componentWillReceiveProps(nextProps, nextContext) {
        if (nextProps.refreshK8sInfo) {
            console.log("will", nextProps)
            this.getYamlData()
            // this.getNameSpaceData()
            // this.getServiceData(this.state.yamlData.nameSpace)
            nextProps.refreshFinished()
        }
    }

    envDetailConfirm = () => {
        Dialog.confirm({
            content: this.props.intl.messages['projects.text.confirmSave'],
            title: this.props.intl.messages['projects.title.confirmSave'],
            onOk: this.envDetailSubmit.bind(this),
            language: 'en-us',
        });
    };

    onNamespaceInputUpdate(e, value) {
        this.field.setValue("nameSpace", value.value)
        this.getServiceData(value.value)

    }

    onInputBlur(e, value) {
        console.log("inputUpdate", value)
        this.field.setValue("service", value)

    }

    onServiceChange(e, value) {
        console.log("onserviceChanged")
        this.field.setValue("service", value.value)
        this.field.setValue("deployment", "")
        this.field.setValue("container", "")
        if (!this.state.createService) {
            this.getDeploymentData()
        }
    }

    onDeploymentChange(e, value) {
        this.field.setValue("deployment", value.value)
    }

    toggleEditMode() {
        this.setState({
            editMode: !this.state.editMode
        })
    }

    toggleCreateService() {
        console.log("toggleCreateService")
        this.setState({
            createService: !this.state.createService
        })
    }

    switchYamlMode(e, values) {
        console.log(values)
        let value = values.value
        if (value === 'profile') {
            this.getNameSpaceData()
        }
        this.setState({
            yamlMode: value
        })
    }

    checkDeploymentData() {
        return this.state.createService ||
            (this.state.deploymentData === null) ||
            (this.state.deploymentData === "") ||
            (this.state.deploymentData.length === 0) ||
            ((this.state.deploymentData.length < 2) &&
                (this.state.containerData.length === 0 || this.state.containerData[this.state.deploymentData[0]].length < 2))
    }


    //提交时的响应函数
    envDetailSubmit() {
        let _this = this;
        //先判断是否使用yaml路径
        if (this.state.yamlMode === 'path') {
            this.yamlPathField.validate((errors, values) => {
                console.log(errors)
                if (errors === null) {
                    this.setState({
                        loading: true
                    })
                    //再判断数据库中是否有yaml信息
                    let existUrl = API.gateway + "/application-server/app/env/" + this.state.appEnvId + "/yamlStatus"
                    Axios.get(existUrl)
                        .then((response) => {
                            console.log("responsssss", response)
                            //有则更新
                            if (response.data) {
                                let url = API.application + '/app/env/' + this.state.appEnvId + '/yaml';
                                Axios.put(url, {}, {
                                        params: {
                                            deploymentStrategy: "KUBERNETES",
                                            releaseStrategy: this.field.getValue('releaseStrategy'),
                                            yamlFilePath: this.yamlPathField.getValue('yamlFilePath')
                                        }
                                    }
                                ).then((response) => {
                                    Toast.success(_this.props.intl.messages['projects.text.updateSuccessful'])
                                    _this.setState({
                                        loading: false
                                    })
                                    //提交完成后刷新当前页面
                                    _this.getYamlData()
                                })
                                    .catch((response) => {
                                        _this.setState({
                                            loading: false
                                        })
                                    })
                            }
                            //无则创建
                            else {
                                let url = API.application + '/app/env/' + this.state.appEnvId + '/yaml';
                                Axios.post(url, {}, {
                                        params: {
                                            deploymentStrategy: "KUBERNETES",
                                            releaseStrategy: this.field.getValue('releaseStrategy'),
                                            yamlFilePath: this.yamlPathField.getValue('yamlFilePath')
                                        }
                                    }
                                ).then((response) => {
                                    Toast.success(_this.props.intl.messages['projects.text.updateSuccessful'])
                                    _this.setState({
                                        loading: false
                                    })
                                    //提交完成后刷新当前页面
                                    _this.getYamlData()
                                }).catch((response) => {
                                    _this.setState({
                                        loading: false
                                    })
                                })
                            }
                        })

                }
            })


        }
        //如果使用配置
        else {
            //判断是否需要新建服务
            this.field.validate((errors, values) => {

                console.log(errors, values);

                // 没有异常则提交表单
                if (errors === null) {
                    if (this.state.createService) {

                        this.editField.validate((errors, values) => {

                            console.log(errors, values);

                            // 没有异常则提交表单
                            if (errors === null) {
                                //如果是则新建服务
                                this.setState({
                                    loading: true
                                })
                                let createServiceUrl = API.gateway + "/application-server/app/env/" + this.state.appEnvId + "/cluster/service"
                                if (this.state.useIngress === "ingress") {
                                    Axios.post(createServiceUrl, {}, {
                                        params: {
                                            name: this.field.getValue("service"),
                                            namespace: this.field.getValue("nameSpace"),
                                            targetPort: this.editField.getValue('targetPort'),
                                            host: this.editField.getValue('host')
                                        }
                                    })
                                        .then(() => {
                                            Toast.success(_this.props.intl.messages['projects.text.createServiceSuccessful'])

                                            //然后判断是否存在yaml属性
                                            let url = API.application + '/app/env/' + this.state.appEnvId + "/yaml";

                                            let existUrl = API.gateway + "/application-server/app/env/" + this.state.appEnvId + "/yamlStatus"
                                            Axios.get(existUrl)
                                                .then((response) => {
                                                    console.log("responsssss", response)
                                                    if (response.data) {
                                                        //存在则更新
                                                        Axios.put(url, {}, {
                                                                params: {
                                                                    deploymentStrategy: "KUBERNETES",
                                                                    nameSpace: this.field.getValue('nameSpace'),
                                                                    service: this.field.getValue('service'),
                                                                    releaseStrategy: this.field.getValue('releaseStrategy'),
                                                                    replicas: this.editField.getValue('replicas'),
                                                                    // imageUrl: this.field.getValue('imageUrl'),
                                                                    releaseBatch: this.field.getValue('releaseBatch')
                                                                }
                                                            }
                                                        )
                                                            .then(function (response) {
                                                                Toast.success(_this.props.intl.messages['projects.text.updateSuccessful'])
                                                                _this.setState({
                                                                    loading: false
                                                                })
                                                                //提交完成后刷新当前页面
                                                                _this.getYamlData()
                                                            })
                                                            .catch(function (error) {
                                                                _this.setState({
                                                                    loading: false
                                                                })
                                                            });

                                                    }
                                                    //不存在则新建
                                                    else {
                                                        Axios.post(url, {}, {
                                                                params: {
                                                                    deploymentStrategy: "KUBERNETES",
                                                                    nameSpace: this.field.getValue('nameSpace'),
                                                                    service: this.field.getValue('service'),
                                                                    releaseStrategy: this.field.getValue('releaseStrategy'),
                                                                    replicas: this.editField.getValue('replicas'),
                                                                    // imageUrl: this.field.getValue('imageUrl'),
                                                                    releaseBatch: this.field.getValue('releaseBatch')
                                                                }
                                                            }
                                                        )
                                                            .then(function (response) {
                                                                Toast.success(_this.props.intl.messages['projects.text.updateSuccessful'])
                                                                _this.setState({
                                                                    loading: false
                                                                })
                                                                //提交完成后刷新当前页面
                                                                _this.getYamlData()
                                                            })
                                                            .catch(function (error) {
                                                                _this.setState({
                                                                    loading: false
                                                                })
                                                            });
                                                    }
                                                })


                                        })
                                } else {
                                    Axios.post(createServiceUrl, {}, {
                                        params: {
                                            name: this.field.getValue("service"),
                                            namespace: this.field.getValue("nameSpace"),
                                            targetPort: this.editField.getValue('targetPort'),
                                            nodePort: this.editField.getValue('nodePort'),

                                        }
                                    })
                                        .then(() => {
                                            Toast.success(_this.props.intl.messages['projects.text.createServiceSuccessful'])

                                            //然后判断是否存在yaml属性
                                            let url = API.application + '/app/env/' + this.state.appEnvId + "/yaml";

                                            let existUrl = API.gateway + "/application-server/app/env/" + this.state.appEnvId + "/yamlStatus"
                                            Axios.get(existUrl)
                                                .then((response) => {
                                                    console.log("responsssss", response)
                                                    if (response.data) {
                                                        //存在则更新
                                                        Axios.put(url, {}, {
                                                                params: {
                                                                    deploymentStrategy: "KUBERNETES",
                                                                    nameSpace: this.field.getValue('nameSpace'),
                                                                    service: this.field.getValue('service'),
                                                                    releaseStrategy: this.field.getValue('releaseStrategy'),
                                                                    replicas: this.editField.getValue('replicas'),
                                                                    // imageUrl: this.field.getValue('imageUrl'),
                                                                    releaseBatch: this.field.getValue('releaseBatch')
                                                                }
                                                            }
                                                        )
                                                            .then(function (response) {
                                                                Toast.success(_this.props.intl.messages['projects.text.updateSuccessful'])
                                                                _this.setState({
                                                                    loading: false
                                                                })
                                                                //提交完成后刷新当前页面
                                                                _this.getYamlData()
                                                            })
                                                            .catch(function (error) {
                                                                _this.setState({
                                                                    loading: false
                                                                })
                                                            });

                                                    }
                                                    //不存在则新建
                                                    else {
                                                        Axios.post(url, {}, {
                                                                params: {
                                                                    deploymentStrategy: "KUBERNETES",
                                                                    nameSpace: this.field.getValue('nameSpace'),
                                                                    service: this.field.getValue('service'),
                                                                    releaseStrategy: this.field.getValue('releaseStrategy'),
                                                                    replicas: this.editField.getValue('replicas'),
                                                                    // imageUrl: this.field.getValue('imageUrl'),
                                                                    releaseBatch: this.field.getValue('releaseBatch')
                                                                }
                                                            }
                                                        )
                                                            .then(function (response) {
                                                                Toast.success(_this.props.intl.messages['projects.text.updateSuccessful'])
                                                                _this.setState({
                                                                    loading: false
                                                                })
                                                                //提交完成后刷新当前页面
                                                                _this.getYamlData()
                                                            })
                                                            .catch(function (error) {
                                                                _this.setState({
                                                                    loading: false
                                                                })
                                                            });
                                                    }
                                                })


                                        })
                                }

                            }
                        })

                    }
                    //如果不需要新建服务则按类似流程执行
                    else {
                        this.postYamlInfo()
                    }
                }
            })


        }
    }

    postYamlInfo() {
        let _this = this
        _this.setState({
            loading: true
        })
        this.field.validate((errors, values) => {

            console.log(errors, values);

            // 没有异常则提交表单
            if (errors === null) {
                let url = API.application + '/app/env/' + this.state.appEnvId + '/yaml'
                let existUrl = API.gateway + "/application-server/app/env/" + this.state.appEnvId + "/yamlStatus"
                Axios.get(existUrl)
                    .then((response) => {
                        console.log(response)
                        if (response.data) {
                            Axios.put(url, {}, {
                                    params: {
                                        deploymentStrategy: "KUBERNETES",
                                        nameSpace: this.field.getValue('nameSpace'),
                                        service: this.field.getValue('service'),
                                        deployment: this.field.getValue('deployment'),
                                        containers: this.field.getValue('container'),
                                        releaseStrategy: this.field.getValue('releaseStrategy'),
                                        // imageUrl: this.field.getValue('imageUrl'),
                                        releaseBatch: this.field.getValue('releaseBatch')
                                    }
                                }
                            )
                                .then(function (response) {
                                    Toast.success(_this.props.intl.messages['projects.text.updateSuccessful'])
                                    _this.setState({
                                        loading: false
                                    })
                                    //提交完成后刷新当前页面
                                    _this.getYamlData()
                                })
                                .catch(function (error) {
                                    _this.setState({
                                        loading: false
                                    })
                                });
                        } else {
                            Axios.post(url, {}, {
                                    params: {
                                        deploymentStrategy: "KUBERNETES",
                                        nameSpace: this.field.getValue('nameSpace'),
                                        service: this.field.getValue('service'),
                                        deployment: this.field.getValue('deployment'),
                                        containers: this.field.getValue('container'),
                                        releaseStrategy: this.field.getValue('releaseStrategy'),
                                        // imageUrl: this.field.getValue('imageUrl'),
                                        releaseBatch: this.field.getValue('releaseBatch')
                                    }
                                }
                            )
                                .then(function (response) {
                                    Toast.success(_this.props.intl.messages['projects.text.updateSuccessful'])
                                    _this.setState({
                                        loading: false
                                    })
                                    //提交完成后刷新当前页面
                                    _this.getYamlData()
                                })
                                .catch(function (error) {
                                    _this.setState({
                                        loading: false
                                    })
                                });
                        }
                    })
            }
        })
    }


    yamlInfoRender() {
        const {init} = this.field
        if (!this.state.loading) {
            if (this.state.yamlMode === "profile") {
                return (
                    <div>
                        <FormItem label={this.props.intl.messages['projects.text.nameSpace']}
                                  {...formItemLayout}
                                  validateStatus={this.field.getError("nameSpace") ? "error" : ""}
                                  help={this.field.getError("nameSpace") ? this.props.intl.messages['projects.check.nameSpace'] : ""}>
                            <div
                                className={this.state.editMode ? "form-item-text hide" : "form-item-text"}
                            >{this.state.yamlData.nameSpace}</div>
                            <Combobox
                                className={this.state.editMode ? "form-item-combobox" : "form-item-combobox hide"}
                                placeholder={this.props.intl.messages['projects.check.nameSpace']}
                                {...init('nameSpace', {
                                    initValue: this.state.yamlData.nameSpace,
                                    rules: [{
                                        required: true,
                                        message: this.props.intl.messages['projects.message.cantNull']
                                    }]
                                })}
                                fillProps="value"
                                hasClear={true}
                                onChange={this.onNamespaceInputUpdate.bind(this)}
                            >
                                {this.state.nameSpaceData.length === 0 ? "" : this.state.nameSpaceData.map((item) => {
                                    return (<Option value={String(item)}>{String(item)}</Option>)
                                })}
                            </Combobox>
                        </FormItem>

                        <FormItem className={this.state.editMode ? "form-item-switch" : "form-item-switch hide"}
                                  label={this.props.intl.messages['projects.text.createNewService']}
                                  {...formItemLayout}>
                            <Switch
                                checkedChildren={this.props.intl.messages['projects.text.yes']}
                                unCheckedChildren={this.props.intl.messages['projects.text.no']}
                                onChange={this.toggleCreateService.bind(this)}
                                defaultChecked={this.state.createService}>
                            </Switch>
                        </FormItem>

                        <FormItem label={this.props.intl.messages['projects.text.service']}
                                  {...formItemLayout}
                                  validateStatus={this.field.getError("service") ? "error" : ""}
                                  help={this.field.getError("service") ? this.props.intl.messages['projects.check.createNewService'] : ""}>
                            <div
                                className={this.state.editMode ? "form-item-text hide" : "form-item-text"}>{this.state.yamlData.service}</div>
                            <Combobox
                                className={this.state.editMode ? "form-item-combobox" : "form-item-combobox hide"}
                                placeholder={this.props.intl.messages['projects.placeholder.service']}
                                {...init('service', {
                                    initValue: this.state.yamlData.service,
                                    rules: [
                                        {
                                            required: true,
                                            message: this.props.intl.messages['projects.message.cantNull']
                                        },
                                        {
                                            pattern: "^([a-z]+\-?)*[a-z]+$"
                                        }
                                    ]
                                })}
                                fillProps="value"
                                hasClear={true}
                                onChange={this.onServiceChange.bind(this)}
                                onInputBlur={this.onInputBlur.bind(this)}

                            >
                                {this.state.serviceData.length === 0 ? "" : this.state.serviceData.map((item) => {
                                    return (<Option value={String(item)}>{String(item)}</Option>)
                                })}
                            </Combobox>

                        </FormItem>

                        <FormItem
                            className={this.state.editMode && this.state.createService ? "form-item-use-ingress" : "form-item-use-ingress hide"}
                            label={this.props.intl.messages['projects.text.exportStrategy']}
                            {...formItemLayout}>
                            <Select
                                className="form-item-select"
                                defaultValue="ingress"
                                fillProps="label"
                                onChange={this.switchIngress.bind(this)}>
                                <Option value="ingress">
                                    {this.props.intl.messages['projects.text.useIngress']}
                                </Option>
                                <Option value="nodePort">
                                    {this.props.intl.messages['projects.text.useNodePort']}
                                </Option>

                            </Select>
                        </FormItem>

                        <FormItem
                            className={this.state.editMode && this.state.createService ? "form-item-create-service" : "form-item-create-service hide"}
                            label={this.props.intl.messages['projects.text.ContainerPort']}
                            {...formItemLayout}
                            validateStatus={this.editField.getError("targetPort") ? "error" : ""}
                            help={this.editField.getError("targetPort") ? this.props.intl.messages['projects.check.ContainerPort'] : ""}
                        >
                            <Input placeholder={this.props.intl.messages['projects.placeholder.ContainerPort']}
                                   defaultValue={0}
                                   className="port-input"
                                   {...this.editField.init("targetPort", {
                                       rules: [{
                                           required: true,
                                           message: this.props.intl.messages['projects.message.cantNull']
                                       }, {
                                           pattern: "^[0-9]{2,5}$"
                                       }]
                                   })}/>

                        </FormItem>

                        <FormItem
                            className={this.state.editMode && this.state.createService && this.state.useIngress === "ingress" ? "form-item-use-ingress" : "form-item-use-ingress hide"}
                            label={this.props.intl.messages['projects.text.host']}
                            {...formItemLayout}
                            validateStatus={this.editField.getError("host") ? "error" : ""}
                            help={this.editField.getError("host") ? this.props.intl.messages['projects.check.host'] : ""}
                        >
                            <Input placeholder={this.props.intl.messages['projects.placeholder.host']}
                                   className="ingress-input"
                                   {...this.editField.init("host", {
                                       rules: [{

                                           required: this.state.useIngress === "ingress",
                                           message: this.props.intl.messages['projects.message.cantNull']
                                       },
                                           {pattern: "^([a-z0-9]+.?)+[a-z]+$"}]
                                   })}/>

                        </FormItem>

                        <FormItem
                            className={this.state.editMode && this.state.createService && this.state.useIngress === "nodePort" ? "form-item-use-ingress" : "form-item-use-ingress hide"}
                            label={this.props.intl.messages['projects.text.NodePort']}
                            {...formItemLayout}
                            validateStatus={this.editField.getError("nodePort") ? "error" : ""}
                            help={this.editField.getError("nodePort") ? this.props.intl.messages['projects.check.NodePort'] : ""}
                        >
                            <Input placeholder={this.props.intl.messages['projects.placeholder.NodePort']}
                                   className="node-port-input"
                                   {...this.editField.init("nodePort", {
                                       rules: [{
                                           required: this.state.useIngress === "nodePort",
                                           message: this.props.intl.messages['projects.message.cantNull']
                                       },
                                           {pattern: "3[0-2]\\d(?<!32[8-9])\\d(?<!327[7-9])\\d(?<!3276[8-9])"}]
                                   })}/>

                        </FormItem>

                        <FormItem
                            className={this.state.editMode && this.state.createService ? "form-item-create-service" : "form-item-create-service hide"}
                            label={this.props.intl.messages['projects.text.replicas']}
                            {...formItemLayout}
                            validateStatus={this.editField.getError("replicas") ? "error" : ""}
                            help={this.editField.getError("replicas") ? this.props.intl.messages['projects.check.replicas'] : ""}>
                            <NumberPicker min={1}
                                          max={99}
                                          placeholder={this.props.intl.messages['projects.placeholder.replicas']}
                                          defaultValue={this.state.editMode && this.state.createService ? 1 : 0}
                                          {...this.editField.init('replicas', {
                                              rules: [{
                                                  required: true,
                                                  message: this.props.intl.messages['projects.message.cantNull']
                                              }]
                                          })}/>
                        </FormItem>


                        <FormItem
                            label={this.props.intl.messages['projects.text.Deployment']}

                            {...formItemLayout}
                            validateStatus={this.field.getError("deployment") ? "error" : ""}
                            help={this.field.getError("deployment") ? this.props.intl.messages['projects.check.Deployment'] : ""}
                            className={this.checkDeploymentData() ? "form-item-deployment-detail hide" : "form-item-deployment-detail"}
                        >
                            <div
                                className={this.state.editMode ? "form-item-text hide" : "form-item-text"}>{this.state.yamlData.deployment}</div>
                            <Combobox
                                className={this.state.editMode ? "form-item-combobox" : "form-item-combobox hide"}
                                placeholder={this.props.intl.messages['projects.placeholder.Deployment']}
                                {...init('deployment', {
                                    initValue: this.state.yamlData.deployment,
                                    rules: this.checkDeploymentData() ? "" : [{
                                        required: true,
                                        message: this.props.intl.messages['projects.message.cantNull']
                                    }]
                                })}
                                fillProps="value"
                                hasClear={true}
                                onChange={this.onDeploymentChange.bind(this)}

                            >
                                {this.state.deploymentData.length === 0 ? "" : this.state.deploymentData.map((item) => {
                                    return (<Option value={String(item)}>{String(item)}</Option>)
                                })}
                            </Combobox>
                        </FormItem>

                        <FormItem label={this.props.intl.messages['projects.text.Deployment']}
                                  {...formItemLayout}
                                  validateStatus={this.field.getError("container") ? "error" : ""}
                                  help={this.field.getError("container") ? this.props.intl.messages['projects.check.Deployment'] : ""}
                                  className={this.checkDeploymentData() ? "form-item-deployment-detail hide" : "form-item-deployment-detail"}

                        >
                            <div
                                className={this.state.editMode ? "form-item-text hide" : "form-item-text"}>{this.state.yamlData.container}</div>
                            <Combobox
                                className={this.state.editMode ? "form-item-combobox" : "form-item-combobox hide"}
                                placeholder={this.props.intl.messages['projects.placeholder.Deployment']}
                                {...init('container', {
                                    initValue: this.state.yamlData.container,
                                    rules: this.checkDeploymentData() ? "" : [{
                                        required: true,
                                        message: this.props.intl.messages['projects.message.cantNull']
                                    }]
                                })}
                                fillProps="value"
                                hasClear={true}
                                // onInputUpdate={this.onInputUpdate.bind(this)}
                            >
                                {
                                    (this.field.getValue("deployment") === undefined || this.field.getValue("deployment") === "") ? [] : this.state.containerData[this.field.getValue("deployment")].map((item) => {
                                        return (<Option value={String(item)}>{String(item)}</Option>)
                                    })}
                            </Combobox>
                        </FormItem>
                    </div>
                )
            } else {
                return (
                    <FormItem label={this.props.intl.messages['projects.text.yamlFilePath']}
                              {...formItemLayout}
                              validateStatus={this.yamlPathField.getError("yamlFilePath") ? "error" : ""}
                              help={this.yamlPathField.getError("yamlFilePath") ? this.props.intl.messages['projects.check.yamlFilePath'] : ""}
                              required
                    >
                        <div
                            className={this.state.editMode ? "form-item-text hide" : "form-item-text"}>{this.state.yamlData.yamlFilePath}</div>
                        <Input placeholder={this.props.intl.messages['projects.placeholder.yamlFilePath']}
                               className={this.state.editMode ? "form-item-yaml-path-input" : "form-item-yaml-path-input hide"}
                               {...this.yamlPathField.init("yamlFilePath", {
                                   initValue: this.state.yamlData.yamlFilePath,
                                   rules: [{
                                       pattern: "^[a-z0-9]([a-z0-9-]*[a-z0-9])?(/[a-z0-9]([a-z0-9-]*[a-z0-9])?)*$",
                                       required: true,
                                       message: this.props.intl.messages['projects.message.cantNull']
                                   }]
                               })}/>

                    </FormItem>
                )
            }

        }
    }

    yamlEditorConfirm() {
        Dialog.confirm({
            content: this.props.intl.messages['projects.text.confirmSave'],
            title: this.props.intl.messages['projects.title.confirmSave'],
            onOk: this.yamlEditorSubmit.bind(this),
            language: 'en-us',
        });

    }

    yamlEditorSubmit() {
        let _this = this
        _this.setState({
            loading: true
        })
        let url = API.gateway + "/application-server/app/env/" + this.state.appEnvId + "/deploymentYaml"
        Axios.put(url, {deploymentEditableYaml: _this.yamlEditorfield.getValue("deploymentYaml")}
        ).then((response) => {
            Toast.success(_this.props.intl.messages['projects.text.updateSuccessful'])
            _this.setState({
                loading: false
            })
        })
            .catch((response) => {
                _this.setState({
                    loading: false
                })
            })
        this.toggleYamlEditor()

    }

    toggleYamlEditor() {
        this.setState({
            editDeploymentYaml: !this.state.editDeploymentYaml
        })
    }

    yamlEditorRender() {
        const {init} = this.yamlEditorfield
        if (!this.state.loading && this.state.yamlData.length !== 0 && this.state.yamlData.deploymentEditableYaml !== "") {
            return <Form className="yaml-editor-form">
                <FormItem label="Deployment Yaml:"
                          className="yaml-editor-form-item"
                          {...formItemLayout}
                          validateStatus={this.field.getError("deploymentYaml") ? "error" : ""}
                          help={this.field.getError("deploymentYaml") ? this.props.intl.messages['projects.message.cantNull'] : ""}>
                    <Input
                        className="yaml-input"
                        // size="large"
                        rows="20"
                        multiple placeholder="Deployment Yaml"
                        readOnly={!this.state.editDeploymentYaml}
                        {...init('deploymentYaml', {
                            initValue: this.state.yamlData.deploymentEditableYaml,
                            rules: [{
                                required: true,
                                message: this.props.intl.messages['projects.message.cantNull']
                            }]
                        })}>
                    </Input>

                    <Icon
                        className={this.state.editDeploymentYaml ? "edit-yaml-icon hide" : "edit-yaml-icon"}
                        type='edit'
                        onClick={this.toggleYamlEditor.bind(this)}/>

                </FormItem>
                <Button
                    className={this.state.editDeploymentYaml ? "save-yaml-button" : "save-yaml-button hide"}

                    onClick={this.yamlEditorConfirm.bind(this)}
                    type="primary">
                    {this.props.intl.messages['projects.button.Save']}
                </Button>
                < Button
                    className={this.state.editDeploymentYaml ? "cancel-yaml-button" : "cancel-yaml-button hide"}
                    onClick={this.toggleYamlEditor.bind(this)}>
                    {this.props.intl.messages['projects.button.cancel']}
                </Button>
            </Form>

        }


    }

    k8sBasicRender() {
        const {init} = this.field
        console.log("yamlData", this.state.yamlData, this.state.yamlData !== [])
        if (!this.state.loading) {
            console.log(this.state.yamlData.yamlFilePath === "")
            return (
                <div>
                    <FormItem label={this.props.intl.messages['projects.text.releaseStrategy']}
                              {...formItemLayout}
                              validateStatus={this.field.getError("releaseStrategy") ? "error" : ""}
                              help={this.field.getError("releaseStrategy") ? this.props.intl.messages['projects.check.releaseStrategy'] : ""}>
                        <div
                            className={this.state.editMode ? "form-item-text hide" : "form-item-text"}>{this.state.yamlData.releaseStrategy}
                            <Icon
                                className={this.state.editMode ? "edit-k8s-icon hide" : "edit-k8s-icon"}
                                type='edit' onClick={this.toggleEditMode.bind(this)}/></div>
                        <Select
                            placeholder={this.props.intl.messages['projects.placeholder.releaseStrategy']}
                            className={this.state.editMode ? "form-item-select" : "form-item-select hide"}
                            language={'en-us'}
                            {...init('releaseStrategy', {
                                initValue: this.state.yamlData.releaseStrategy,
                                rules: [{
                                    required: true,
                                    message: this.props.intl.messages['projects.message.cantNull']
                                }]
                            })}
                        >
                            <Option value="BATCH"
                                    disabled={true}>{this.props.intl.messages['projects.text.batchUpdate']}</Option>
                            <Option value="BLUE_GREEN"
                                    disabled={true}>{this.props.intl.messages['projects.text.blueGreenUpdate']}</Option>
                            <Option
                                value="ROLLING_UPDATE">{this.props.intl.messages['projects.text.rollingUpdate']}</Option>
                        </Select>

                    </FormItem>

                    {/*<FormItem*/}
                    {/*    className={this.field.getValue('releaseBatch') === 'BATCH' ? "form-item-release-batch" : "form-item-release-batch hide"}*/}
                    {/*          label="发布批次:"*/}
                    {/*          {...formItemLayout}*/}
                    {/*          validateStatus={this.field.getError("releaseBatch") ? "error" : ""}*/}
                    {/*          help={this.field.getError("releaseBatch") ? this.props.intl.messages['projects.placeholder.releaseStrategy'] : ""}>*/}
                    {/*    <div*/}
                    {/*        className={this.state.editMode ? "form-item-text hide" : "form-item-text"}>{this.state.yamlData.releaseBatch}</div>*/}
                    {/*    <NumberPicker*/}
                    {/*        className={this.state.editMode ? "form-item-NumberPicker" : "form-item-NumberPicker hide"}*/}
                    {/*        min={1}*/}
                    {/*        max={99}*/}
                    {/*        placeholder="发布批次"*/}
                    {/*        {...init('releaseBatch', {initValue: this.state.yamlData.releaseBatch})}/>*/}
                    {/*</FormItem>*/}


                    {/*<FormItem label="镜像地址"*/}
                    {/*{...formItemLayout}*/}
                    {/*validateStatus={this.field.getError("imageUrl") ? "error" : ""}*/}
                    {/*help={this.field.getError("imageUrl") ? "镜像仓库地址" : ""}>*/}
                    {/*<div style={{display: this.state.editMode ? "None" : ""}}>{this.state.yamlData.imageUrl}</div>*/}
                    {/*<Input placeholder="镜像仓库地址"*/}
                    {/*style={{display: this.state.editMode ? "" : "None"}}*/}

                    {/*{...init('imageUrl', {*/}
                    {/*initValue: this.state.yamlData.imageUrl,*/}
                    {/*rules: [{*/}
                    {/*required: true,*/}
                    {/*message: this.props.intl.messages['projects.message.cantNull']*/}
                    {/*}]*/}
                    {/*})}>*/}
                    {/*</Input>*/}

                    {/*</FormItem>*/}

                    <FormItem className={this.state.editMode ? "form-item-yaml-origin" : "form-item-yaml-origin hide"}
                              label={this.props.intl.messages['projects.text.yamlSource']}
                              {...formItemLayout}>
                        <Select
                            fillProps="label"
                            onChange={this.switchYamlMode.bind(this)}
                            className="form-item-select"
                            defaultValue={(this.state.yamlData.length === 0 || this.state.yamlFilePath === "") ? "profile" : "path"}>
                            <Option value="profile">
                                {this.props.intl.messages['projects.text.userProfile']}
                            </Option>
                            <Option value="path">
                                {this.props.intl.messages['projects.text.userRelativePath']}
                            </Option>

                        </Select>
                    </FormItem>

                </div>
            )
        }
    }


    render() {

        return (
            <div className="form-loading-container">
                <Form className="form">
                    <Loading visible={this.state.loading} shape="dot-circle"
                             color="#2077FF" className="form-loading">


                        {this.k8sBasicRender()}


                        {this.yamlInfoRender()}
                        <Button
                            className={this.state.editMode ? "save-button" : "save-button hide"}
                            onClick={this.envDetailConfirm.bind(this)}
                            type="primary"
                        >
                            {this.props.intl.messages['projects.button.Save']}
                        </Button>
                        < Button
                            className={this.state.editMode ? "cancel-button" : "cancel-button hide"}
                            onClick={this.toggleEditMode.bind(this)}>
                            {this.props.intl.messages['projects.button.cancel']}
                        </Button>


                        {this.yamlEditorRender()}
                    </Loading>
                </Form>
            </div>
        )
    }


}

export default injectIntl(K8sInfoPage)
