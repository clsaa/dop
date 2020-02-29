import {Button, Card, Dialog, Feedback, Field, Form, Icon, Input, Loading} from "@icedesign/base";
import React, {Component} from 'react';
import Axios from "axios";
import API from "../../../../API";
import K8sInfoPage from '../K8sInfoPage/K8sInfoPage'
import "./ClusterInfoForm.scss"
import {injectIntl} from "react-intl";

const FormItem = Form.Item;
const Toast = Feedback.toast;
const formItemLayout = {
    labelCol: {span: 8},
    wrapperCol: {span: 16}
};

class ClusterInfoForm extends Component {
    constructor(props) {
        super(props)
        this.field = new Field(this)
        this.state = {
            editMode: false,
            clusterData: "",
            appEnvId: props.appEnvId,
            loading: true,
            refreshK8sInfo: false

        }
    }

    refreshFinished() {
        this.setState({
            refreshK8sInfo: false
        })
    }


    toggleEditMode() {
        this.setState({
            editMode: !this.state.editMode
        })
    }

    clusterInfoConfirm = () => {
        Dialog.confirm({
            language:'en-us',
            content: this.props.intl.messages['projects.text.saveConfirmClusterInfo'],
            title: this.props.intl.messages['projects.text.submitConfirm'],
            onOk: this.clusterInfoSubmit.bind(this),

        });
    };

    clusterInfoSubmit = () => {
        let _this = this;

        //校验输入
        this.field.validate((errors, values) => {
            console.log(errors);

            // 没有异常则提交表单
            if (errors === null) {
                this.setState({
                    loading: true
                })
                let postUrl = API.gateway + "/application-server/app/env/" + this.state.appEnvId + "/cluster"
                Axios.post(postUrl, {
                    targetClusterUrl: _this.field.getValue("targetClusterUrl"),
                    targetClusterToken: _this.field.getValue("targetClusterToken")
                })
                    .then((response) => {
                        Toast.success(_this.props.intl.messages['projects.text.submitSuccessful'])
                        _this.setState({
                            loading: false,
                            refreshK8sInfo: true
                        })
                            _this.getClusterData()
                        }
                    )
                    .catch(
                        (response) => {
                            _this.setState({
                                loading: false
                            })

                        }
                    )
            }

        })
    }

    getClusterData() {
        let _this = this
        let urlUrl = API.gateway + "/application-server/app/env/" + this.state.appEnvId + "/clusterUrl"
        _this.setState({
            loading: true
        })
        Axios.get(urlUrl)
            .then((response) => {
                console.log("cluster", response)
                if (response.data.targetClusterUrl === "") {
                    _this.setState({
                        clusterData: "",
                        editMode: true,
                        loading: false
                    })
                } else {
                    _this.setState({
                        clusterData: response.data.targetClusterUrl,
                        editMode: false,
                        loading: false
                    })
                }

            })
            .catch((response) => {
                _this.setState({
                    loading: false
                })
            })
    }

    componentDidMount() {
        this.getClusterData()
    }

    k8sInfoRender() {
        if (!(this.state.clusterData === undefined && this.state.clusterData === ""))
            return (<K8sInfoPage
                refreshK8sInfo={this.state.refreshK8sInfo}
                refreshFinished={this.refreshFinished.bind(this)}
                targetCluster={this.state.clusterData}
                appEnvId={this.state.appEnvId}
            />)

    }

    clusterDataRender() {
        const {init, getError} = this.field;
        if (!(this.state.loading)) {
            return (
                <Form className="card-form">
                    <Loading visible={this.state.loading} size='small' className="form-loading"
                             shape="dot-circle" color="#2077FF">
                        <FormItem label={this.props.intl.messages['projects.text.clusterUrl']}
                                  {...formItemLayout}
                                  validateStatus={getError("targetClusterUrl") ? "error" : ""}
                                  help={getError("targetClusterUrl") ? this.props.intl.messages['projects.check.clusterUrl'] : ""}
                        >
                            <Input
                                className={this.state.editMode ? "form-item-input" : "form-item-input hide"}
                                placeholder={this.props.intl.messages['projects.placeholder.clusterUrl']}
                                {...init('targetClusterUrl', {
                                    initValue: this.state.clusterData,
                                    rules: [{
                                        type: "url",
                                        required: true,
                                        message: this.props.intl.messages['projects.message.cantNull']
                                    }]
                                })}>
                            </Input>
                            <div
                                className={this.state.editMode ? "form-item-text-container hide" : "form-item-text-container"}
                            >
                                {this.state.clusterData === "" ? "" : this.state.clusterData}
                                <Icon
                                    className="edit-icon"
                                    type='edit'
                                      visible={!this.state.editMode ? "true" : "false"}
                                      onClick={this.toggleEditMode.bind(this)}/>
                            </div>
                        </FormItem>


                        <FormItem label={this.props.intl.messages['projects.text.clusterToken']}
                                  className={this.state.editMode ? "form-item-input" : "form-item-input hide"}
                                  {...formItemLayout}
                                  validateStatus={getError("targetClusterToken") ? "error" : ""}
                                  help={getError("targetClusterToken") ? this.props.intl.messages['projects.check.clusterToken'] : ""}>
                            <Input className="form-item-input"
                                   multiple placeholder={this.props.intl.messages['projects.placeholder.clusterToken']}
                                   {...init('targetClusterToken', {
                                       rules: [{
                                           required: true,
                                           message: this.props.intl.messages['projects.message.cantNull']
                                       }]
                                   })}>
                            </Input>
                        </FormItem>

                        <Button onClick={this.clusterInfoConfirm.bind(this)}
                                type="primary"
                                className={this.state.editMode ? "save-button" : "save-button hide"}
                        >
                            {this.props.intl.messages['projects.button.submit']}
                        </Button>

                        < Button
                            className={this.state.editMode ? "cancel-button" : "cancel-button hide"}
                            onClick={this.toggleEditMode.bind(this)}>{this.props.intl.messages['projects.button.cancel']} </Button>
                    </Loading>

                </Form>)
        }
    }

    render() {

        return (

            <Card
                className="card"
                title={this.props.intl.messages['projects.text.kubernetesEnvInfo']}
                bodyHeight="40%"
            >

                {this.clusterDataRender()}


                {
                    this.k8sInfoRender()
                }

            </Card>

        )
    }

}

export default injectIntl(ClusterInfoForm)
