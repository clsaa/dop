import React, {Component} from 'react';
import RepoList from '../RepoList'
import {Breadcrumb,Tab} from "@icedesign/base";
import NamespaceLogList from "../../NamespaceLog/NamespaceLogList";
import API from "../../../API";
import Axios from "axios/index";
import MemberList from "../MemberList";
import {injectIntl,FormattedMessage} from 'react-intl';

class Repos extends Component {
    constructor(props) {
        super(props);
        this.state = {
            currentData:[],
            totalCount:0,
            id: props.location.pathname.match("[0-9]+")[0],
            projectName:"",
            userRole:"",

        };
    }

    init(){
        let url = API.image+"/v1/projects/"+this.state.id;
        let _this = this;
        Axios.get(url, {})
            .then(function (response) {
                console.log("空间",response.data);
                _this.setState({
                    projectName:response.data.name,
                    userRole:response.data.currentUserRole
                });
            })

    }
    componentWillMount() {
        this.init();
    }

    render() {
        if(this.state.userRole==="Tourist"){

            return (
                <div>
                    <Breadcrumb style={{marginBottom: "10px"}}>
                        <Breadcrumb.Item style={{color: "#5485F7"}} link="#/image/projects">
                            <FormattedMessage id="image.namespace" defaultMessage="< 命名空间"/>
                        </Breadcrumb.Item>
                    </Breadcrumb>
                    <div className={"repoName"}>
                        {this.state.projectName}
                    </div>
                    <Tab>
                        <Tab.TabPane key={"image"} tab={this.props.intl.messages["image.namespace.repository"]}>
                            <RepoList projectId={this.state.id} />
                        </Tab.TabPane>
                    </Tab>
                </div>
            )
        }else {
            return (
                <div>
                    <Breadcrumb style={{marginBottom: "10px"}}>
                        <Breadcrumb.Item style={{color: "#5485F7"}} link="#/image/projects">
                            <FormattedMessage id="image.namespace" defaultMessage="< 命名空间"/>
                        </Breadcrumb.Item>
                    </Breadcrumb>
                    <div className={"repoName"}>
                        {this.state.projectName}
                    </div>
                    <Tab>
                        <Tab.TabPane key={"image"} tab={this.props.intl.messages["image.namespace.repository"]}>
                            <RepoList projectId={this.state.id} />
                        </Tab.TabPane>
                        <Tab.TabPane key={"member"} tab={this.props.intl.messages["image.namespace.member"]}>
                            <MemberList projectId={this.state.id}/>
                        </Tab.TabPane>
                        <Tab.TabPane key={"labels"} tab={this.props.intl.messages["image.namespace.label"]}>

                        </Tab.TabPane>
                        <Tab.TabPane key={"logs"} tab={this.props.intl.messages["image.namespace.accessLog"]}>
                            <NamespaceLogList projectId={this.state.id} />
                        </Tab.TabPane>
                    </Tab>
                </div>
            )
        }
    }

}


export default injectIntl(Repos);




