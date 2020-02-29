/**
 * 项目管理主页面
 * @author Bowen
 */

import React, {Component} from 'react';
import {Breadcrumb, Input} from '@icedesign/base';
import TopBar from './components/projectManagement/TopBar';
import Pagination from './components/projectManagement/ProjectPagination/ProjectPagination'
import CreateProjectDialog from './components/projectManagement/CreateProjectDialog/CreateProjectDialog'
import {injectIntl} from "react-intl";

class Projects extends Component {
    static displayName = 'Projects';

    constructor(props) {
        super(props);
        this.state = {
            //通知子组件刷新的boolean变量
            //因为获取项目列表数据需要翻页器的数据，如果在父组件中操作比较复杂，我这里就直接传一个消息让子组件刷新一下
            createdProjectNeedRefresh: false,
            searchKey: ""
        }
    }


    refreshFinished() {
        this.setState({
            createdProjectNeedRefresh: false
        })
    }

//回调函数传给创建项目的窗口，创建项目成功后调用该函数刷新项目列表
    refreshProjectList() {
        this.setState({
            createdProjectNeedRefresh: true
        });
        console.log("refreshProjectList");
    }

    onSearch(value) {
        this.setState({
            searchKey: value
        })
    }

    render() {
        return (
            <div>
                <Breadcrumb style={{marginBottom: "10px"}}>
                    <Breadcrumb.Item
                        link="#/project">{this.props.intl.messages['projects.bread.allProject']}</Breadcrumb.Item>
                </Breadcrumb>
                <TopBar
                    extraBefore={
                        <Input
                            size="large"
                            style={{width: '240px'}}
                            onChange={this.onSearch.bind(this)}
                            placeholder={this.props.intl.messages["projects.search.project"]}
                        />
                    }
                    extraAfter={< CreateProjectDialog refreshProjectList={this.refreshProjectList.bind(this)}/>
                    }
                />
                <Pagination createdProjectNeedRefresh={this.state.createdProjectNeedRefresh}
                            refreshFinished={this.refreshFinished.bind(this)}
                            searchKey={this.state.searchKey}/>
            </div>

        );
    }
}

export default injectIntl(Projects)