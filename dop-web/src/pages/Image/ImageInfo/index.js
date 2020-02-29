import React, {Component} from 'react'
import {FormattedMessage, injectIntl} from "react-intl";
import Axios from "axios";
import API from "../../API";
import {Breadcrumb, Feedback, Loading, Table} from "@icedesign/base";
import {CopyToClipboard} from 'react-copy-to-clipboard';
import IceContainer from '@icedesign/container'
import "../Style.scss"

const Toast = Feedback.toast;
class ImageInfo extends Component{

    constructor(props){
        super(props);
        this.state={
            namespace:props.location.pathname.split("/")[2],
            repoName:props.location.pathname.split("/")[3],
            tag:props.location.pathname.split("/")[5],
            namespaceId:null,
            loading:true,
            imageData:[],
        }
    }


    init(){
        let project = API.image + '/v1/projects';
        let _this = this;
        Axios.get(project,{
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
        let url = API.image+"/v1/repositories/"+this.state.namespace+"/"+this.state.repoName+"/images/"+this.state.tag;
        Axios.get(url, {})
            .then(function (response) {
                console.log("镜像详情",response.data);
                let image = [];
                image[0] = response.data;
                _this.setState({
                    loading:false,
                    imageData:image
                })

            })

    }
    onCopy=()=>{
        console.log(this.props.intl)
        Toast.success(this.props.intl.messages["image.copySuccess"])
    }

    pullRender=(value,index,record)=>{
        let pull = "docker pull registry.dop.clsaa.com/" + this.state.namespace + "/" + this.state.repoName + ":" + record.name;
        console.log(pull);
        return <CopyToClipboard className={"copy"} onCopy={this.onCopy} text={pull}>
            <div>
                <img className={"imgStyle"} src={require('../img/copy.png')} alt="" />
                <span>{pull}</span>
            </div>
        </CopyToClipboard>
    }

    componentWillMount() {
        this.init();
    }

    render() {
        console.log("tag",this.state.namespace,this.state.repoName,this.state.tag);
        return(
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
                    <Breadcrumb.Item link={"#/repos/"+this.state.namespace+"/"+this.state.repoName+"/images"}>
                        <FormattedMessage id={"image.imageInfo.imageList"}
                                          defaultMessage={"镜像"}/>
                    </Breadcrumb.Item>
                </Breadcrumb>
                <div className={"repoName"}>
                    {this.state.namespace+"/"+this.state.repoName}
                </div>
                <IceContainer title={this.props.intl.messages["image.imageInfo.imageInfo"]}>
                    <Loading visible={this.state.loading} shape="dot-circle" color="#2077FF">
                        <Table dataSource={this.state.imageData}
                               isLoading={this.state.isLoading}
                               primaryKey="name"
                        >

                            <Table.Column title={this.props.intl.messages["image.imageTable.tag"]}
                                          dataIndex="name"/>

                            <Table.Column title={this.props.intl.messages["image.imageTable.size"]}
                                          dataIndex="size"/>

                            <Table.Column title={this.props.intl.messages["image.imageTable.owner"]}
                                          dataIndex="author"/>
                            <Table.Column cell={this.pullRender}
                                          title={this.props.intl.messages["image.imageTable.pullOperation"]}
                                          dataIndex="pull"/>

                            <Table.Column title={this.props.intl.messages["image.imageTable.docker"]}
                                          dataIndex="dockerVersion"/>
                            <Table.Column title={this.props.intl.messages["image.imageTable.time"]}
                                          dataIndex="created"/>
                            <Table.Column title={this.props.intl.messages["image.imageTable.label"]}
                                          dataIndex="labels"/>
                            <Table.Column title={this.props.intl.messages["image.imageTable.digest"]}
                                          dataIndex="digest"/>
                        </Table>
                    </Loading>
                </IceContainer>
            </div>

        )

    }
}

export default injectIntl(ImageInfo);