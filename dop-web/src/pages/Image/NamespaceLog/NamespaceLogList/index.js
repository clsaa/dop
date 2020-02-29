import React,{Component} from 'react';
import {Input, Loading, Pagination, Table, DatePicker, Select} from '@icedesign/base';
import IceContainer from '@icedesign/container';
import {Grid} from "@icedesign/base/index";
import API from "../../../API";
import Axios from "axios/index";
import {FormattedMessage, injectIntl} from "react-intl";

const {Row} = Grid;
const {RangePicker}  =DatePicker;
class NamespaceLogList extends Component{
    static displayName = "NamespaceLogList";

    constructor(props) {
        super(props);
        this.state = {
            currentData: [],
            current:1,
            totalCount:0,
            pageSize:10,
            loading: true,
            id: this.props.projectId,
            queryKey:"",
            beginTime:"",
            endTime:"",
            select:"all"
        };

    }

    refreshList(current,queryKey,operation,begin,end) {
        let url = API.image + "/v1/projects/" + this.state.id + "/logs";
        let _this = this;
        if (queryKey !== "") {
            if (operation === "all") {
                if (begin === "") {
                    Axios.get(url, {
                        params: {
                            page: current,
                            pageSize: this.state.pageSize,
                            username: queryKey
                        }
                    })
                        .then(function (response) {
                            console.log("查询日志信息");
                            console.log(response.data);
                            if (response.data.totalCount !== 0) {
                                _this.setState({
                                    currentData: response.data.pageList,
                                    totalCount: response.data.totalCount,
                                    loading: false
                                });
                            } else {
                                _this.setState({
                                    currentData: [],
                                    totalCount: 0,
                                    loading: false,
                                })
                            }
                        })
                } else {
                    if (end === "") {
                        Axios.get(url, {
                            params: {
                                page: current,
                                pageSize: this.state.pageSize,
                                username: queryKey,
                                beginTimestamp: begin
                            }
                        })
                            .then(function (response) {
                                console.log("beginTime+查询+日志信息");
                                console.log(response.data);
                                if (response.data.totalCount !== 0) {
                                    _this.setState({
                                        currentData: response.data.pageList,
                                        totalCount: response.data.totalCount,
                                        loading: false
                                    });
                                } else {
                                    _this.setState({
                                        currentData: [],
                                        totalCount: 0,
                                        loading: false,
                                    })
                                }
                            })

                    } else {
                        Axios.get(url, {
                            params: {
                                page: current,
                                pageSize: this.state.pageSize,
                                username: queryKey,
                                beginTimestamp: begin,
                                endTimestamp: end,
                            }
                        })
                            .then(function (response) {
                                console.log("begin+end+query日志信息");
                                console.log(response.data);
                                if (response.data.totalCount !== 0) {
                                    _this.setState({
                                        currentData: response.data.pageList,
                                        totalCount: response.data.totalCount,
                                        loading: false
                                    });
                                } else {
                                    _this.setState({
                                        currentData: [],
                                        totalCount: 0,
                                        loading: false,
                                    })
                                }
                            })

                    }
                }
            } else {
                if (begin === "") {
                    Axios.get(url, {
                        params: {
                            page: current,
                            pageSize: this.state.pageSize,
                            username: queryKey,
                            operation: operation
                        }
                    })
                        .then(function (response) {
                            console.log("pull和查询日志信息");
                            console.log(response.data);
                            if (response.data.totalCount !== 0) {
                                _this.setState({
                                    currentData: response.data.pageList,
                                    totalCount: response.data.totalCount,
                                    loading: false
                                });
                            } else {
                                _this.setState({
                                    currentData: [],
                                    totalCount: 0,
                                    loading: false,
                                })
                            }
                        })
                } else {
                    if (end === "") {
                        Axios.get(url, {
                            params: {
                                page: current,
                                pageSize: this.state.pageSize,
                                username: queryKey,
                                operation: operation,
                                beginTimestamp: begin
                            }
                        })
                            .then(function (response) {
                                console.log("begintime+查询+pull日志信息");
                                console.log(response.data);
                                if (response.data.totalCount !== 0) {
                                    _this.setState({
                                        currentData: response.data.pageList,
                                        totalCount: response.data.totalCount,
                                        loading: false
                                    });
                                } else {
                                    _this.setState({
                                        currentData: [],
                                        totalCount: 0,
                                        loading: false,
                                    })
                                }
                            })

                    } else {
                        Axios.get(url, {
                            params: {
                                page: current,
                                pageSize: this.state.pageSize,
                                username: queryKey,
                                operation: operation,
                                beginTimestamp: begin,
                                endTimestamp: end,
                            }
                        })
                            .then(function (response) {
                                console.log("begin+end+query+pull日志信息");
                                console.log(response.data);
                                if (response.data.totalCount !== 0) {
                                    _this.setState({
                                        currentData: response.data.pageList,
                                        totalCount: response.data.totalCount,
                                        loading: false
                                    });
                                } else {
                                    _this.setState({
                                        currentData: [],
                                        totalCount: 0,
                                        loading: false,
                                    })
                                }
                            })

                    }
                }
            }
        } else {
            //没有查询
            if (operation === "all") {
                //没有操作限制
                if (begin === "") {
                    //没有时间
                    Axios.get(url, {
                        params: {
                            page: current,
                            pageSize: this.state.pageSize
                        }
                    })
                        .then(function (response) {
                            console.log("日志信息");
                            console.log(response.data);
                            if (response.data.totalCount !== 0) {
                                _this.setState({
                                    currentData: response.data.pageList,
                                    totalCount: response.data.totalCount,
                                    loading: false
                                });
                            } else {
                                _this.setState({
                                    currentData: [],
                                    totalCount: 0,
                                    loading: false,
                                })
                            }
                        })
                } else {
                    //有时间
                    if (end === "") {
                        //仅有开始时间
                        Axios.get(url, {
                            params: {
                                page: current,
                                pageSize: this.state.pageSize,
                                beginTimestamp: begin
                            }
                        })
                            .then(function (response) {
                                console.log("beginTime日志信息");
                                console.log(response.data);
                                if (response.data.totalCount !== 0) {
                                    _this.setState({
                                        currentData: response.data.pageList,
                                        totalCount: response.data.totalCount,
                                        loading: false
                                    });
                                } else {
                                    _this.setState({
                                        currentData: [],
                                        totalCount: 0,
                                        loading: false,
                                    })
                                }
                            })

                    } else {
                        //开始结束时间都存在
                        Axios.get(url, {
                            params: {
                                page: current,
                                pageSize: this.state.pageSize,
                                beginTimestamp: begin,
                                endTimestamp: end,
                            }
                        })
                            .then(function (response) {
                                console.log("begin+end+query+pull日志信息");
                                console.log(response.data);
                                if (response.data.totalCount !== 0) {
                                    _this.setState({
                                        currentData: response.data.pageList,
                                        totalCount: response.data.totalCount,
                                        loading: false
                                    });
                                } else {
                                    _this.setState({
                                        currentData: [],
                                        totalCount: 0,
                                        loading: false,
                                    })
                                }
                            })

                    }
                }
            } else {
                //存在操作限制
                if (begin === "") {
                    Axios.get(url, {
                        params: {
                            page: current,
                            pageSize: this.state.pageSize,
                            operation: operation
                        }
                    })
                        .then(function (response) {
                            console.log("单个操作日志信息");
                            console.log(response.data);
                            if (response.data.totalCount !== 0) {
                                _this.setState({
                                    currentData: response.data.pageList,
                                    totalCount: response.data.totalCount,
                                    loading: false
                                });
                            } else {
                                _this.setState({
                                    currentData: [],
                                    totalCount: 0,
                                    loading: false,
                                })
                            }
                        })
                } else {
                    if (end === "") {
                        Axios.get(url, {
                            params: {
                                page: current,
                                pageSize: this.state.pageSize,
                                operation: operation,
                                beginTimestamp: begin
                            }
                        })
                            .then(function (response) {
                                console.log("beginTime+单个操作日志信息");
                                console.log(response.data);
                                if (response.data.totalCount !== 0) {
                                    _this.setState({
                                        currentData: response.data.pageList,
                                        totalCount: response.data.totalCount,
                                        loading: false
                                    });
                                } else {
                                    _this.setState({
                                        currentData: [],
                                        totalCount: 0,
                                        loading: false,
                                    })
                                }
                            })

                    } else {
                        Axios.get(url, {
                            params: {
                                page: current,
                                pageSize: this.state.pageSize,
                                operation: operation,
                                beginTimestamp: begin,
                                endTimestamp: end,
                            }
                        })
                            .then(function (response) {
                                console.log("begin+end+query+pull日志信息");
                                console.log(response.data);
                                if (response.data.totalCount !== 0) {
                                    _this.setState({
                                        currentData: response.data.pageList,
                                        totalCount: response.data.totalCount,
                                        loading: false
                                    });
                                } else {
                                    _this.setState({
                                        currentData: [],
                                        totalCount: 0,
                                        loading: false,
                                    })
                                }
                            })

                    }
                }
            }
        }
    }

    //选择器
    changeSelection(value){
        this.setState({
            select:value
        });
       this.refreshList(1,this.state.queryKey,value,this.state.beginTime,this.state.endTime);
    }

    onSearch(value){
        this.setState({
            queryKey:value
        });
        this.refreshList(this.state.current,value,this.state.select,this.state.beginTime,this.state.endTime);
    }


    handleChange(current,e){
        this.setState({
            current:current
        });
        this.refreshList(current,this.state.queryKey,this.state.select,this.state.beginTime,this.state.endTime);
    }


    componentWillMount() {
        this.refreshList(this.state.current,this.state.queryKey,this.state.select,this.state.beginTime,this.state.endTime);
    }

    dateChange(val,str){
        console.log(val,str);
        let start ="";
        let end = "";
        if (str[0]!==""){
            start = Date.parse(str[0]).toString().substr(0,10);
        }
        if (str[1]!==""){
            end = Date.parse(str[1]).toString().substr(0,10);
        }
        console.log(start,end);
        this.setState({
            beginTime:start,
            endTime:end
        })
        this.refreshList(this.state.current,this.state.queryKey,this.state.select,start,end);

    }
    startDateChange(val,str){
        console.log(val,str);
        let start = "";
        if (str!==""){
            start = Date.parse(str).toString().substr(0,10);
        }
        this.setState({
            beginTime:start,
        })
        this.refreshList(this.state.current,this.state.queryKey,this.state.select,start,this.state.endTime);
    }


    render() {
        return (
            <div>
                <IceContainer title={this.props.intl.messages["image.search"]}>
                    <Row wrap>
                        <Input placeholder={this.props.intl.messages["image.searchPlaceholder"]} onChange={this.onSearch.bind(this)}/>
                        <Select className={"select"} defaultValue={"all"} onChange={this.changeSelection.bind(this)}>
                            <Select.Option value="all">
                                <FormattedMessage id="image.log.selectAll"
                                                  defaultMessage="所有操作"/>
                            </Select.Option>
                            <Select.Option value="push">
                                <FormattedMessage id="image.log.selectPush"
                                                  defaultMessage="推送"/>
                            </Select.Option>
                            <Select.Option value="pull">
                                <FormattedMessage id="image.log.selectPull"
                                                  defaultMessage="拉取"/>
                            </Select.Option>
                            <Select.Option value="create">
                                <FormattedMessage id="image.log.selectCreate"
                                                  defaultMessage="创建"/>
                            </Select.Option>
                        </Select>
                        <RangePicker className="datePicker"
                                     language={this.props.intl.locale==='zh-CN'?'zh-cn':'en-us'}
                                     onChange={this.dateChange.bind(this)}
                                     onStartChange={this.startDateChange.bind(this)}/>

                    </Row>
                </IceContainer>

                <IceContainer title={this.props.intl.messages["image.accessLog"]}>
                    <Loading visible={this.state.loading} shape="dot-circle" color="#2077FF">
                        <Table dataSource={this.state.currentData}
                               isLoading={this.state.isLoading}
                               primaryKey="logId"
                        >

                            <Table.Column title={this.props.intl.messages["image.logTable.user"]}
                                          dataIndex="username"/>

                            <Table.Column title={this.props.intl.messages["image.logTable.repoName"]}
                                          dataIndex="repoName"/>

                            <Table.Column title={this.props.intl.messages["image.logTable.tag"]}
                                          dataIndex="repoTag"/>
                            <Table.Column title={this.props.intl.messages["image.logTable.operation"]}
                                          dataIndex="operation"/>
                            <Table.Column title={this.props.intl.messages["image.logTable.time"]}
                                          dataIndex="opTime"/>
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
    };
}
export default injectIntl(NamespaceLogList)