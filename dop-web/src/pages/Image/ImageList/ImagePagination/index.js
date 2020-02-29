import React, {Component} from 'react';
import {Balloon, Feedback, Grid, Input, Loading, Pagination, Table} from "@icedesign/base";
import DeleteImageDialog from "../deleteImageDialog";
import {Col} from "@alifd/next/lib/grid";
import API from "../../../API";
import Axios from "axios";
import IceContainer from '@icedesign/container';
import {injectIntl} from "react-intl";
import {CopyToClipboard} from "react-copy-to-clipboard";

const {Row} = Grid;
const Toast = Feedback.toast;
class ImagePagination extends Component{

    constructor(props){
        super(props);
        this.state={
            imageData:[],
            current:1,
            pageSize:10,
            totalCount:0,
            rowSelection: {
                onChange: this.onChange.bind(this),
                selectedRowKeys: []
            },
            loading: true,
            repoName: this.props.repoName,
            queryKey:""
        }
    }

    refreshImageList(current,queryKey){
        let url = API.image + '/v1/repositories/'+this.state.repoName+"/images";
        let _this = this;
        if (queryKey!==""){
            console.log(queryKey)
            Axios.get(url, {
                params:{
                    pageNo:current,
                    pageSize:10,
                    tag:queryKey
                }
            })
                .then(function (response) {
                    console.log("非空镜像信息");
                    console.log(response.data);
                    if (response.data.totalCount!==0){
                        _this.setState({
                            imageData: response.data.pageList,
                            totalCount:response.data.totalCount,
                            loading:false
                        });
                    }else {
                        _this.setState({
                            imageData: [],
                            totalCount:0,
                            loading:false
                        });
                    }

                })
        } else {
            Axios.get(url, {
                params:{
                    pageNo:current,
                    pageSize:10,
                }
            })
                .then(function (response) {
                    console.log("镜像信息");
                    console.log(response.data);
                    if (response.data.totalCount!==0){
                        _this.setState({
                            imageData: response.data.pageList,
                            totalCount:response.data.totalCount,
                            loading:false
                        });
                    } else {
                        _this.setState({
                            imageData: [],
                            totalCount:0,
                            loading:false
                        });
                    }

                })
        }


    }

    componentWillReceiveProps(nextProps, nextContext) {
        this.setState({
            repoName:nextProps.repoName,
        })
    }

    componentWillMount() {
        this.refreshImageList(1,this.state.queryKey)
    }

    onChange(names, records){
        let {rowSelection} = this.state;
        rowSelection.selectedRowKeys = names;
        console.log("onChange",rowSelection.selectedRowKeys, records);
        this.setState({rowSelection});
    }

    onSearch(value){
        this.setState({
            queryKey: value
        })
        this.refreshImageList(1,value);
    }
    handleChange(current,e){
        this.setState({
            current:current
        });
        this.refreshImageList(current,this.state.queryKey)
    }
    onCopy=()=>{
        Toast.success(this.props.intl.messages["image.copySuccess"]);
    }
    pullRender=(value,index,record)=>{
        let pull = <CopyToClipboard onCopy={this.onCopy}
                                    text={"docker pull registry.dop.clsaa.com/" + this.state.repoName + ":" + record.name}>
                         <img className={"imgStyle"} src={require('../../img/copy.png')} alt="" />
                    </CopyToClipboard>
        return(
            <Balloon trigger={pull} triggerType="hover">
                {"docker pull registry.dop.clsaa.com/" + this.state.repoName + ":" + record.name}
            </Balloon>
        );
    }
    digestRender=(value)=> {
        let digest = <CopyToClipboard className="copyDigest" onCopy={this.onCopy} text={value}>
            <span>
                <span>{value.toString().substr(0,10)+'...'}</span>
                <img className={"imgStyle"} src={require('../../img/copy.png')} alt="" />
            </span>
        </CopyToClipboard>;
        return (
            <Balloon trigger={digest} triggerType={"hover"}>
                {value}
            </Balloon>
        )
    }
    //image列表
    render() {
        return (
            <div>
                <IceContainer title={this.props.intl.messages["image.search"]}>
                    <Row wrap>
                        <Input placeholder={this.props.intl.messages["image.searchPlaceholder"]} onChange={this.onSearch.bind(this)}/>
                    </Row>
                </IceContainer>

                <IceContainer title={this.props.intl.messages["image.imageList"]}>
                    <Row wrap className="headRow">
                        <Col l="12">
                            <DeleteImageDialog repoName={this.state.repoName} deleteKeys={this.state.rowSelection.selectedRowKeys} refreshImageList={this.refreshImageList.bind(this)}/>
                        </Col>
                    </Row>
                    <Loading visible={this.state.loading} shape="dot-circle" color="#2077FF">
                        <Table dataSource={this.state.imageData}
                               rowSelection={this.state.rowSelection}
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
                            <Table.Column cell={this.digestRender}
                                          title={this.props.intl.messages["image.imageTable.digest"]}
                                          dataIndex="digest"/>
                        </Table>
                    </Loading>

                    <Pagination language={this.props.intl.locale==='zh-CN'?'zh-cn':'en-us'}
                                className={"body"}
                                current={this.state.current}
                                onChange={this.handleChange.bind(this)}
                                pageSize={this.state.pageSize}
                                total={this.state.totalCount}
                                hideOnlyOnePage={true}/>
                </IceContainer>
            </div>
        )
    }
}
export default injectIntl(ImagePagination)