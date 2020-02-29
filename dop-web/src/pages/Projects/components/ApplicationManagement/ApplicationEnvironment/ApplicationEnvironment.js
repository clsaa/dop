import React, {Component} from 'react';
import {Breadcrumb, Button, Card, Dialog, Feedback, Icon, Loading} from '@icedesign/base';
import Axios from "axios";
import API from "../../../../API";
import {Col, Row} from "@alifd/next/lib/grid";
import CreateApplicationEnvironmentDialog from "../CreateApplicationEnvrionment/CreateApplicationEnvironment";
import TopBar from "./topbar"
import "./ApplicationEnvironment.scss"
import {injectIntl} from "react-intl";

const Toast = Feedback.toast;

class ApplicationEnvironment extends Component {
    constructor(props) {
        super(props);
        this.state = {
            appId: props.appId,
            appEnvData: [],
            switchPage: props.switchPage,
            loading: true,
            projectId: props.projectId
        }

    }

    getAppEnvData() {
        this.setState({
            loading: true
        })
        let url = API.gateway + "/application-server/app/" + this.state.appId + "/allEnv"
        let _this = this;
        Axios.get(url)
            .then(function (response) {
                console.log(response)
                _this.setState({

                    appEnvData: response.data,
                    loading: false
                })
            })
            .catch(function (response) {
                _this.setState({
                    loading: false
                })
            })
    }

    componentDidMount() {
        this.getAppEnvData();
    }

    popupConfirm = (id) => {
        console.log(id)
        Dialog.confirm({
            content: this.props.intl.messages['projects.text.deleteConfirmEnv'],
            title: this.props.intl.messages['projects.text.deleteConfirm'],
            onOk: this.onDelete.bind(this, id),
            language:'en-us',
        });
    };

    onDelete(id) {
        let deleteUrl = API.gateway + "/application-server/app/env/" + id
        let _this = this;
        this.setState({
            loading: true
        })
        Axios.delete(deleteUrl)
            .then(function (response) {
                _this.getAppEnvData();
                Toast.success(_this.props.intl.messages['projects.text.deleteSuccessful']);
            })
            .catch(function (response) {

                console.log(response);
            })
    }

    titleRender(title, id) {
        return (
            <div>{title}
                <Icon type="ashbin" onClick={this.popupConfirm.bind(this, id)}
                      style={{
                          cursor: "pointer",
                          color: "#FFA003",
                          margin: "5px"
                      }}/>
            </div>
        )
    }

    showEnvLog(id) {
        console.log(id)
        this.state.switchPage("envLog", id)
    }

    showEnvDetailFun(id) {
        console.log(id)
        this.state.switchPage("envDetail", id)
    }

    getYaml(id) {


        let url = API.gateway + "/application-server/app/env/" + {id} + "/yamlFile"
        Axios.get(url)
            .then((response) => {
                console.log(response)///yaml
            })
            .catch((response) => {
                console.log(response)
            })

    }

    cardRender() {

        return (


            <Row wrap gutter="20">
                {this.state.appEnvData.map((item, index) => {
                    console.log(item)
                    return (
                        <Col key={index}>
                            <Card style={{height: "70%"}}>
                                {this.titleRender(item.title, item.id)}
                                <Button onClick={this.showEnvLog.bind(this, item.id)}>
                                    {this.props.intl.messages['projects.button.deploymentHistory']}
                                </Button>
                                <Button onClick={this.showEnvDetailFun.bind(this, item.id)}
                                        style={{margin: "20px"}}>
                                    {this.props.intl.messages['projects.button.configuringDeployment']}
                                </Button>

                            </Card>

                        </Col>
                    )

                })}
            </Row>


        )


    }

    refreshApplicationEnvironmentList() {
        this.getAppEnvData();
    }

    render() {

        return (

            <Loading visible={this.state.loading} shape="dot-circle" color="#2077FF"
            >


                <div>
                    <TopBar
                        extraBefore={<Breadcrumb>
                            <Breadcrumb.Item
                                link="#/project">{this.props.intl.messages['projects.bread.allProject']}</Breadcrumb.Item>
                            <Breadcrumb.Item
                                link={"#/projectDetail?projectId=" + this.state.projectId}>{this.props.intl.messages['projects.bread.project'] + this.state.projectId}</Breadcrumb.Item>
                            <Breadcrumb.Item
                                link={"#/applicationDetail?appId=" + this.state.appId + "&projectId=" + this.state.projectId}>{this.props.intl.messages['projects.bread.app'] + this.state.appId}</Breadcrumb.Item>
                        </Breadcrumb>}
                        extraAfter={<CreateApplicationEnvironmentDialog type="primary"

                                                                        refreshApplicationEnvironmentList={this.refreshApplicationEnvironmentList.bind(this)}
                                                                        appId={this.state.appId}>
                            {this.props.intl.messages['projects.button.createEnv']}
                        </CreateApplicationEnvironmentDialog>}
                    />


                    {this.cardRender()}


                </div>
            </Loading>
        )
    }


}

export default injectIntl(ApplicationEnvironment)
