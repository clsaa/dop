import React, {Component} from 'react';
import API from "../../../API"
import Axios from "axios";
import {Input,Table,Grid,Pagination,Loading,Switch,Feedback,Select} from '@icedesign/base';
import {Link} from 'react-router-dom';
import IceContainer from '@icedesign/container';
import DeleteNameSpaceDialog from "../DeleteNameSpaceDialog";
import CreateNamespaceDialog from "../CreateNamespaceDialog";
import "../../Style.scss"
import {injectIntl,FormattedMessage} from 'react-intl';
import PublicStatus from '../PublicStatus'

const {Row,Col} = Grid;
const Toast = Feedback.toast;

const styles = {
    body: {
        position: 'relative',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
    }
};

class NamespacePagination extends Component {
    //构造方法
    constructor(props) {
        super(props);
        this.state = {
            current: 1,
            currentData: [],
            rowSelection: {
                onChange: this.onChange.bind(this),
                selectedRowKeys: []
            },
            pageSize :10,
            totalCount: 0,
            queryKey: "",
            loading: true,
            select: 'all'
        };
    }

    //选择器监听
    onChange(projectIds, records) {
        let {rowSelection} = this.state;
        rowSelection.selectedRowKeys = projectIds;
        console.log("onChange",rowSelection.selectedRowKeys, records);
        this.setState({rowSelection});
    }


    //获取最新数据并刷新
    refreshList(current, searchKey,select) {

        let url = API.image + '/v1/projects';
        let _this = this;

        if (searchKey===""){
            if (select==='private'){
                Axios.get(url, {
                    params:{
                        page:current,
                        pageSize: this.state.pageSize,
                        publicStatus:false
                    }

                })
                    .then(function (response) {
                        console.log("私有镜像信息");
                        console.log(response.data);
                        if (response.data.totalCount!==0){
                            _this.setState({
                                currentData: response.data.pageList,
                                totalCount:response.data.totalCount,
                                loading: false,
                                current: current
                            });
                        } else {
                            _this.setState({
                                currentData: [],
                                totalCount:0,
                                loading: false,
                                current: current
                            });
                        }

                    })
            } else if (select==='public'){
                Axios.get(url, {
                    params:{
                        page:current,
                        pageSize: this.state.pageSize,
                        publicStatus:true
                    }

                })
                    .then(function (response) {
                        console.log("公开命名空间信息");
                        console.log(response.data);
                        if (response.data.totalCount!==0){
                            _this.setState({
                                currentData: response.data.pageList,
                                totalCount:response.data.totalCount,
                                loading: false,
                                current: current
                            });
                        } else {
                            _this.setState({
                                currentData: [],
                                totalCount:0,
                                loading: false,
                                current: current
                            });
                        }

                    })
            }else {

                Axios.get(url, {
                    params:{
                        page:current,
                        pageSize: this.state.pageSize
                    }

                })
                    .then(function (response) {
                        console.log(response.data);
                        if (response.data.totalCount!==0){
                            _this.setState({
                                currentData: response.data.pageList,
                                totalCount:response.data.totalCount,
                                loading: false,
                                current: current
                            });
                        } else {
                            _this.setState({
                                currentData: [],
                                totalCount:0,
                                loading: false,
                                current: current
                            });
                        }
                    })
            }
        } else {
            if (select==='private'){
                //获取数据
                Axios.get(url, {
                    params:{
                        page:current,
                        pageSize: this.state.pageSize,
                        publicStatus:false,
                        name:searchKey
                    }

                })
                    .then(function (response) {
                        console.log("私有镜像信息");
                        console.log(response.data);
                        if (response.data.totalCount!==0){
                            _this.setState({
                                currentData: response.data.pageList,
                                totalCount:response.data.totalCount,
                                loading: false,
                                current: current
                            });
                        } else {
                            _this.setState({
                                currentData: [],
                                totalCount:0,
                                loading: false,
                                current: current
                            });
                        }
                    })
            } else if (select==='public'){
                //获取数据
                Axios.get(url, {
                    params:{
                        page:current,
                        pageSize: this.state.pageSize,
                        publicStatus:true,
                        name:searchKey
                    }

                })
                    .then(function (response) {
                        console.log("公开命名空间信息");
                        console.log(response.data);
                        if (response.data.totalCount!==0){
                            _this.setState({
                                currentData: response.data.pageList,
                                totalCount:response.data.totalCount,
                                loading: false,
                                current: current
                            });
                        } else {
                            _this.setState({
                                currentData: [],
                                totalCount:0,
                                loading: false,
                                current: current
                            });
                        }
                    })
            }else {
                //获取数据
                Axios.get(url, {
                    params:{
                        page:current,
                        pageSize: this.state.pageSize,
                        name:searchKey
                    }

                })
                    .then(function (response) {
                        if (response.data.totalCount!==0){
                            _this.setState({
                                currentData: response.data.pageList,
                                totalCount:response.data.totalCount,
                                loading: false,
                                current: current
                            });
                        } else {
                            _this.setState({
                                currentData: [],
                                totalCount:0,
                                loading: false,
                                current: current
                            });
                        }
                    })
            }
        }

    }

    //初始化
    componentWillMount() {
        this.refreshList(this.state.current,this.state.queryKey,'all');
    }

    //搜索框内容变化
    onSearch(value) {
        this.setState({
            queryKey:value
        })
        this.refreshList(1,value,this.state.select);
    }

    //分页器监听
    handleChange(current,e){
        this.setState({
            current:current
        });
        this.refreshList(current,this.state.queryKey,this.state.select)
    }

    //选择器
    changeSelection(value){
        this.setState({
            select:value
        });
       this.refreshList(1,this.state.queryKey,value)
    }


    //链接跳转到对应的命名空间
    nameRender = function (value,index,record) {
        return <Link to={"/image/projects/" + record.projectId + "/repos"}
        >{value}</Link>
    };
    //命名空间公开状态监听
    renderSwitch = (value,index,record) => {
        return <PublicStatus refreshList={this.refreshList.bind(this)} value={value} record={record}/>
    };

    render() {
        return (
            <div>
                <IceContainer title={this.props.intl.messages["image.search"]}>
                    <Row wrap>
                        <Input placeholder={this.props.intl.messages["image.searchPlaceholder"]} onChange={this.onSearch.bind(this)}/>
                        <Select className={"select"} defaultValue={"all"} onChange={this.changeSelection.bind(this)}>
                            <Select.Option value="all">
                                <FormattedMessage id="image.selectAll"
                                defaultMessage="所有命名空间"/>
                            </Select.Option>
                            <Select.Option value="public">
                                <FormattedMessage id="image.selectPublic"
                                                  defaultMessage="公开命名空间"/>
                            </Select.Option>
                            <Select.Option value="private">
                                <FormattedMessage id="image.selectPrivate"
                                                  defaultMessage="私有命名空间"/>
                            </Select.Option>
                        </Select>
                    </Row>
                </IceContainer>

                <IceContainer title={this.props.intl.messages["image.namespaceList"]}>
                    <Row wrap className="headRow">
                        <Col l="12">
                            <CreateNamespaceDialog refreshProjectList={this.refreshList.bind(this)}/>
                            <DeleteNameSpaceDialog deleteKeys={this.state.rowSelection.selectedRowKeys} refreshProjectList={this.refreshList.bind(this)}/>
                        </Col>
                    </Row>
                    <Loading visible={this.state.loading} shape="dot-circle" color="#2077FF">
                        <Table dataSource={this.state.currentData}
                               rowSelection={this.state.rowSelection}
                               isLoading={this.state.isLoading}
                               primaryKey='projectId'>
                            <Table.Column title={this.props.intl.messages["image.namespaceId"]}
                                          dataIndex="projectId"/>

                            <Table.Column cell={this.nameRender}
                                          title={this.props.intl.messages["image.namespaceName"]}
                                          dataIndex="name"/>

                            <Table.Column title={this.props.intl.messages["image.role"]}
                                          dataIndex="currentUserRole"/>

                            <Table.Column title={this.props.intl.messages["image.publicStatus"]}
                                          dataIndex="metadata.public" width={130} cell={this.renderSwitch}/>

                            <Table.Column title={this.props.intl.messages["image.repoCount"]}
                                          dataIndex="repoCount"/>
                            <Table.Column title={this.props.intl.messages["image.creationTime"]}
                                          dataIndex="creationTime"/>
                        </Table>
                    </Loading>

                    <Pagination language={this.props.intl.locale==='zh-CN'?'zh-cn':'en-us'}
                                style={styles.body}
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
export default injectIntl(NamespacePagination);