import React, {Component} from 'react';
import {Tab} from "@icedesign/base";

import ApplicationBasicInfo from "../components/ApplicationManagement/ApplicationBasicInfo/ApplicationBasicInfo"
import ApplicationVariable from "../components/ApplicationManagement/ApplicationVariable/ApplicationVariable"
import ApplicationEnvironment from "../components/ApplicationManagement/ApplicationEnvironment/ApplicationEnvironment"
import ApplicationEnvironmentDetail
    from "../components/ApplicationManagement/ApplicationEnvironmentDetail/ApplicationEnvironmentDeatil";
import "./ApplicationDetail.scss"
import ApplicationEnvironmentLog
    from "../components/ApplicationManagement/ApplicationEnvironmentLog/ApplicationEnvironmentLog";
import {injectIntl} from "react-intl";

const TabPane = Tab.TabPane;



/**
 * 应用详情页面列表
 * @author Bowen
 */
class ApplicationDetail extends Component {
    static displayName = 'ApplicationDetail';

    constructor(props) {
        super(props);
        this.state = {
            //在应用列表点击应用ID跳转到该页面，通过正则表达式解析应用ID
            projectId: props.location.search.match("projectId=[0-9]+")[0].split("=")[1],
            appId: props.location.search.match("appId=[0-9]+")[0].split("=")[1],
            showPage: "envList",
            envId: undefined
        }
    }

    switchPage(page, id) {
        console.log("id", id)
        this.setState({
            showPage: page,
            envId: id === undefined ? "" : id
        })
    }

    envRender() {
        console.log(this.state.showPage, this.state.showPage === "envList")
        if (this.state.showPage === "envList") {
            return (<ApplicationEnvironment
                projectId={this.state.projectId}
                switchPage={this.switchPage.bind(this)}
                appId={this.state.appId}/>)
        }

    }

    envLogRender() {
        if (this.state.showPage === "envLog") {
            return (<ApplicationEnvironmentLog
                projectId={this.state.projectId}
                switchPage={this.switchPage.bind(this)}
                appEnvId={this.state.envId}
                appId={this.state.appId}/>)
        }

    }

    envDetailRender() {
        if (this.state.showPage === "envDetail") {
            return (
                <ApplicationEnvironmentDetail
                    switchPage={this.switchPage.bind(this)}
                    projectId={this.state.projectId}
                    appEnvId={this.state.envId}
                    appId={this.state.appId}/>
            )
        } else {
            return (<div></div>)
        }
    }


    render() {
        return (
            <div>
                {/*<TopBar*/}
                {/*extraBefore={  <Breadcrumb>*/}
                {/*<Breadcrumb.Item link="#/project">{this.props.intl.messages['projects.bread.allProject']}</Breadcrumb.Item>*/}
                {/*<Breadcrumb.Item*/}
                {/*link={"#/application?projectId=" + this.state.projectId}>{this.props.intl.messages['projects.bread.project'] + this.state.projectId}</Breadcrumb.Item>*/}
                {/*<Breadcrumb.Item*/}
                {/*link={"#/applicationDetail?appId=" + this.state.appId + "&projectId=" + this.state.projectId}>{this.props.intl.messages['projects.bread.app'] + this.state.appId}</Breadcrumb.Item>*/}
                {/*</Breadcrumb>}*/}
                {/*// extraAfter={ <CreateApplicationVariableDialog*/}
                {/*//*/}
                {/*//     refreshApplicationVariableList={this.refreshApplicationVariableList.bind(this)}*/}
                {/*//     appId={this.state.appId}/>}*/}
                {/*/>*/}

                <Tab contentStyle={{padding: 20}}
                     className="Tab"
                     lazyLoad={false}>


                    <TabPane tab={this.props.intl.messages['projects.text.BasicInfo']}
                             key={"basic"}
                             className="TabPane"
                    >

                        <ApplicationBasicInfo
                            projectId={this.state.projectId}
                            appId={this.state.appId}/>
                    </TabPane>

                    <TabPane tab={this.props.intl.messages['projects.text.EnvironmentProfile']}
                             key={"env"}
                             className="TabPane">


                        {this.envRender()}
                        {this.envLogRender()}
                        {this.envDetailRender()}
                    </TabPane>

                    <TabPane tab={this.props.intl.messages['projects.text.variableManagement']}
                             key={"var"}
                             className="TabPane"
                    >
                        <ApplicationVariable
                            projectId={this.state.projectId}
                            appId={this.state.appId}/>

                    </TabPane>

                </Tab>


            </div>
        )

    }
}

export default injectIntl(ApplicationDetail)