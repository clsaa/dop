import React, {Component} from 'react';
import {Breadcrumb, Card, Loading} from '@icedesign/base';
import TopBar from "./topbar"
import Axios from "axios";
import API from "../../../../API";
import "./ProjectBasicInfo.scss"

import {injectIntl} from "react-intl";

/**
 * 项目基本信息页面
 * @author Bowen
 */


class ProjectBasicInfo extends Component {
    static displayName = 'ProjectBasicInfo';

    constructor(props) {
        super(props);
        this.state = {
            //在应用列表点击应用ID跳转到该页面，通过正则表达式解析应用id
            projectId: props.projectId,
            projectData: undefined,
            loading: true,
            userData: [],
        }

    }

    //加载应用基本信息
    componentDidMount() {
        this.getData()
        // this.getMemberData()
    }





    getData() {
        this.setState({
            loading: true
        })
        let _this = this
        let url = API.application + '/project/' + this.state.projectId;

        //获取应用基本信息
        Axios.get(url)
            .then(function (response) {
                console.log(response)
                let projectData = response.data
                //获取用户基本信息
                let userUrl = API.application + "/userInfo";
                Axios.get(userUrl, {
                    params: {
                        userId: projectData.cuser
                    }
                })
                    .then(function (response) {
                        console.log(response)
                        _this.setState({
                            projectData: projectData,
                            userData: response.data,
                            loading: false
                        })

                    })
                    .catch((response) => {
                        console.log(response)
                        _this.setState({
                                loading: false
                            }
                        )
                    })


            })
            .catch((response) => {
                    console.log(response)
                _this.setState({
                    loading: false
                })
                }
            )


    }






    render() {
        return (
            <Loading visible={this.state.loading} shape="dot-circle" color="#2077FF">
                <div>
                    <TopBar
                        extraBefore={<Breadcrumb>
                            <Breadcrumb.Item
                                link="#/project">{this.props.intl.messages['projects.bread.allProject']}</Breadcrumb.Item>
                            <Breadcrumb.Item
                                link={"#/projectDetail?projectId=" + this.state.projectId}>{this.props.intl.messages['projects.bread.project'] + this.state.projectId}</Breadcrumb.Item>
                        </Breadcrumb>}/>
                    <Card
                        className="user-card"
                        title={this.state.userData.name}
                        bodyHeight="100%"
                        subTitle={this.props.intl.messages["projects.text.projectOwner"]}
                        extra={<a href="">{this.props.intl.messages["projects.text.handOverApplication"]}&gt;</a>}
                    >
                        <img alt="avatar"
                             src={this.state.userData.avatarURL === "" ? "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAFAAAABQBAMAAAB8P++eAAAABGdBTUEAALGPC/xhBQAAAAFzUkdCAK7OHOkAAAAYUExURcHBwb+/v7+/v76+vujo6OHh4cnJydTU1IOqnXYAAAADdFJOUxPppyMYpxkAAAD6SURBVEjH7dfbDYIwFAbguoHRCYwTKLcBOIUBaHQAIAxQwvwSEQpyaH/FFxP+5y89vacV4uQBOQix86DsxRmDV3HE4EV4YDa4QQRWSjYILKnNzQ0jekY7Yd3B1AVDeiV3wKCHsQPWPUwdkIbYYWSgtsLAwMwKfQNjFCZWWPwBhEcNz+NoZfLfrLXZPYkD+gtd/H6H97UT5+EK0FPY1ZbABaDYygysuTEvtqg9sI9AiyV/o8xgRNj0DLtHaiuszOahxgJLGueeL8Gpa8vnPHx30yEZGKo5lBwMiEnGwIKDKQMVB+UaSGzWwO2psMGPIfxgh78A8KcC/aY8ACmMo3JtJ3ljAAAAAElFTkSuQmCC" : this.state.userData.avatarURL}
                             className="avatar"/>
                        <span>

        </span>
                    </Card>
                </div>
            </Loading>
        )
    }
}

export default injectIntl(ProjectBasicInfo)