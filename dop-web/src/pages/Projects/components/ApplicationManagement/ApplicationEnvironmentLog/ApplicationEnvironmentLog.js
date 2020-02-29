import React, {Component} from 'react';
import TopBar from "./topbar";
import {Breadcrumb, Button} from "@icedesign/base";
import Pagination from "./ApplicationEnvironmentLogPagination";
import {injectIntl} from "react-intl";

/**
 * 应用列表
 * @author Bowen
 *
 */
class ApplicationEnvironmentLog extends Component {
    static displayName = 'ApplicationEnvironmentLog';

    constructor(props) {
        super(props);

        //从项目列表点击项目ID 跳转到该页面，从URL中通过正则表达式解析出项目ID

        this.state = {
            projectId: props.projectId,
            appId: props.appId,
            appEnvId: props.appEnvId,
            switchPage: props.switchPage
        }
    }


    render() {
        return (
            <div>
                {/*创建函数的对话框和搜索框*/}
                <TopBar
                    extraBefore={<Breadcrumb>
                        <Breadcrumb.Item
                            link="#/project">{this.props.intl.messages['projects.bread.allProject']}</Breadcrumb.Item>
                        <Breadcrumb.Item
                            link={"#/projectDetail?projectId=" + this.state.projectId}>{this.props.intl.messages['projects.bread.project'] + this.state.projectId}</Breadcrumb.Item>
                        <Breadcrumb.Item
                            link={"#/applicationDetail?appId=" + this.state.appId + "&projectId=" + this.state.projectId}>{this.props.intl.messages['projects.bread.app'] + this.state.appId}</Breadcrumb.Item>
                        <Breadcrumb.Item>{this.props.intl.messages['projects.bread.appEnv'] + this.state.appEnvId + this.props.intl.messages['projects.bread.appEnvLog']}</Breadcrumb.Item>
                    </Breadcrumb>}
                />
                {/*应用列表及分页器*/}
                <Pagination appEnvId={this.state.appEnvId}/>
                <Button onClick={this.state.switchPage.bind(this, "envList")}
                        type="primary">{this.props.intl.messages['projects.button.returnToEnvList']}</Button>
            </div>

        );
    }
}

export default injectIntl(ApplicationEnvironmentLog)