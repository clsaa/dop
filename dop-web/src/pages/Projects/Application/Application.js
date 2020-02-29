import React, {Component} from 'react';
import TopBar from "../components/ApplicationManagement/TopBar";
import {Breadcrumb, Input} from "@icedesign/base";
import CreateApplicationDialog
    from "../components/ApplicationManagement/CreateApplicationDialog/CreateApplicationDialog";
import Pagination from "../components/ApplicationManagement/ApplicationPagination/ApplicationPagination";
import "./Application.scss"
import {injectIntl} from "react-intl";

/**
 * 应用列表
 * @author Bowen
 *
 */
class Application extends Component {
    static displayName = 'Application';

    constructor(props) {
        super(props);


        this.state = {
            projectId: props.projectId,
            searchKey: "",
            createdApplicationNeedRefresh: false,
        }
    }

    //传递给应用列表的回调函数，当页面刷新完成时，调用该函数
    refreshFinished() {
        this.setState({
            createdApplicationNeedRefresh: false
        })
    }

//回调函数，传给创建项目的窗口，创建项目成功后调用该函数刷新项目列表
    refreshApplicationList() {
        this.setState({
            createdApplicationNeedRefresh: true
        });
        console.log("refreshProjectList");
    }

    //当搜索框的值改变时，触发该函数
    onSearch(value) {
        this.setState({
            searchKey: value
        })
    }


    render() {
        return (
            <div>
                {/*创建函数的对话框和搜索框*/}
                <Breadcrumb className="Breadcrumb">
                    <Breadcrumb.Item
                        link="#/project">{this.props.intl.messages['projects.bread.allProject']}</Breadcrumb.Item>
                    <Breadcrumb.Item
                        link={"#/projectDetail?projectId=" + this.state.projectId}>{this.props.intl.messages['projects.bread.project'] + this.state.projectId}</Breadcrumb.Item>
                </Breadcrumb>
                <TopBar
                    extraBefore={
                        <CreateApplicationDialog refreshApplicationList={this.refreshApplicationList.bind(this)}
                                                 projectId={this.state.projectId}/>

                    }
                    extraAfter={
                        <Input className="TopBarInput"
                               size="large"
                               placeholder={this.props.intl.messages['projects.search.application']}
                            // hasClear
                               onChange={this.onSearch.bind(this)}
                        />
                    }

                />
                {/*应用列表及分页器*/}
                <Pagination createdApplicationNeedRefresh={this.state.createdApplicationNeedRefresh}
                            refreshFinished={this.refreshFinished.bind(this)} searchKey={this.state.searchKey}
                            projectId={this.state.projectId}/>
            </div>

        );
    }
}

export default injectIntl(Application)