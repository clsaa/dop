import {Breadcrumb, Button, Field, Form, Loading, Select} from "@icedesign/base";
import React, {Component} from 'react';
import Axios from "axios";
import API from "../../../../API";
import PipelineBindPage from "../PipelineBindPage/PipelineBindPage"
import ClusterInfoForm from "../ClusterInfoForm/ClusterInfoForm"
import TopBar from "./topbar";
import "./ApplicationEnvironmentDetail.scss"
import {injectIntl} from "react-intl";

const FormItem = Form.Item;
const Option = Select.Option;
const formItemLayout = {
    labelCol: {span: 8},
    wrapperCol: {span: 16}
};


/**
 * 展示应用环境详情的列表
 * @author Bowen
 **/
class ApplicationEnvironmentDetail extends Component {

    constructor(props) {
        super(props);
        console.log(props)
        this.field = new Field(this)
        this.state = {
            appId: props.appId,
            appEnvId: props.appEnvId,
            envData: [],
            loading: true,
            projectId: props.projectId,
            switchPage: props.switchPage
        }
    }

    getenvData() {
        let url = API.gateway + "/application-server/app/env/" + this.state.appEnvId;
        Axios.get(url)
            .then((response) => {
                console.log(response)
                if (response.data === "") {
                    this.setState({
                        envData: [],
                        editMode: true,
                        loading: false
                    })
                } else {
                    this.setState({
                        envData: response.data,
                        editMode: false,
                        loading: false
                    })
                }
            })
    }





    componentDidMount() {
        this.getenvData();

    }

    onChange(e, value) {
        this.field.setValue("deploymentStrategy", value)
    }

    clusterInfoRender() {
        if (this.field.getValue('deploymentStrategy') === 'KUBERNETES' && this.state.appEnvId !== undefined) {
            return (<ClusterInfoForm

                appEnvId={this.state.appEnvId}/>)
        }
    }
    render() {
        const {init} = this.field;
        return (
            <div>

                <TopBar
                    extraBefore={<Breadcrumb>
                        <Breadcrumb.Item
                            link="#/project">{this.props.intl.messages['projects.bread.allProject']}</Breadcrumb.Item>
                        <Breadcrumb.Item
                            link={"#/projectDetail?projectId=" + this.state.projectId}>{this.props.intl.messages['projects.bread.project'] + this.state.projectId}</Breadcrumb.Item>
                        <Breadcrumb.Item
                            link={"#/applicationDetail?appId=" + this.state.appId + "&projectId=" + this.state.projectId}>{this.props.intl.messages['projects.bread.app'] + this.state.appId}</Breadcrumb.Item>
                        <Breadcrumb.Item>{this.props.intl.messages['projects.bread.appEnv'] + this.state.appEnvId}</Breadcrumb.Item>
                    </Breadcrumb>}
                />
                <div className="app-env-detail-form-container">

                        <Form className="deployment-strategy-form">
                            <Loading className="form-loading" visible={this.state.loading} shape="dot-circle"
                                     color="#2077FF"
                            >
                            <FormItem label={this.props.intl.messages['projects.text.deploymentStrategy']}
                                      className="deployment-strategy-form-item"
                                      {...formItemLayout}
                                      validateStatus={this.field.getError("deploymentStrategy") ? "error" : ""}
                                      help={this.field.getError("deploymentStrategy") ? this.props.intl.messages['projects.check.deploymentStrategy'] : ""}
                    >

                                <Select placeholder={this.props.intl.messages['projects.check.deploymentStrategy']}
                                        onChange={this.onChange.bind(this)}

                                        {...init('deploymentStrategy', {
                                    initValue: this.state.envData.deploymentStrategy,
                                            rules: [{
                                                required: true,
                                                message: this.props.intl.messages['projects.message.cantNull']
                                            }]
                                })}>
                                    <Option
                                        value="KUBERNETES">{this.props.intl.messages['projects.text.deploymentByKubernetes']}</Option>
                            <Option value="test">测试</Option>
                        </Select>

                    </FormItem>


                            </Loading>
                </Form>

                    <PipelineBindPage appId={this.state.appId} appEnvId={this.state.appEnvId}/>
                {this.clusterInfoRender()}

                    <Button onClick={this.state.switchPage.bind(this, "envList")}
                            type="primary">{this.props.intl.messages['projects.button.returnToEnvList']}</Button>

                </div>
            </div>
        );
    }
}

export default injectIntl(ApplicationEnvironmentDetail)