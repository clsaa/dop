import React, { Component } from 'react';
import {Breadcrumb} from '@icedesign/base';
import ImagePagination from './ImagePagination'
import API from "../../API";
import Axios from "axios";
import {FormattedMessage} from "react-intl";

export default class ImageList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            namespaceId:"",
            repoName:props.location.pathname.split("/")[2]+"/"+props.location.pathname.split("/")[3]
        };
    }

    init(){
        let url = API.image + '/v1/projects';
        let _this = this;
        Axios.get(url,{
            params:{
                name:this.props.location.pathname.split("/")[2],
                page:1,
                pageSize:10
            }
        }).then(
            function (response) {
                _this.setState({
                    namespaceId:response.data.pageList[0].projectId
                })
            }
        )
    }


    componentDidMount() {
        this.init();
    }

    render() {
        return (
            <div>
                <Breadcrumb style={{marginBottom: "10px"}}>
                    <Breadcrumb.Item link="#/image/projects">
                        <FormattedMessage id="image.imageInfo.namespace"
                                          defaultMessage="命名空间"/>
                    </Breadcrumb.Item>
                    <Breadcrumb.Item link={"#/image/projects/"+this.state.namespaceId+"/repos"}>
                        <FormattedMessage id="image.namespace.repository"
                                          defaultMessage="镜像仓库"/>
                    </Breadcrumb.Item>
                </Breadcrumb>
                <div className={"repoName"}>
                    {this.state.repoName}
                </div>
                <ImagePagination repoName={this.state.repoName}/>
            </div>
        )
    };

}
