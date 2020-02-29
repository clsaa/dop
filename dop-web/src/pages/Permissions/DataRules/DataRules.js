/**
 *  数据规则
 *  @author lizhiying
 *
 * */
import React, { Component } from 'react';
import {Input,Button, Field, Table, Dialog, Feedback,Form} from "@icedesign/base";
import Pagination from "@icedesign/base/lib/pagination";
import API from "../../API";
import Axios from "axios";
import BalloonConfirm from "@icedesign/balloon-confirm";
import {Link} from "react-router-dom";
import Select from "@icedesign/base/lib/select";
import {injectIntl} from "react-intl";
import {UserRoleMapping} from "../Roles/UserRoleMapping/UserRoleMapping";
const { Item: FormItem } = Form;


export class DataRules extends Component
{
    constructor(props) {
        super(props);
        this.field = new Field(this);
        this.dataField=new Field(this);
        this.state = {
            currentData : [],
            roleList:[],
            currentRuleId:0,
            userList:[],

            userSelectList:[],

            isLoading:true,
            userIsLoading:true,
            ruleVisible:false,
            dataVisible:false,
            deleteVisible:false,
            pageNo:1,
            pageSize:8,
            totalCount:0,

            userPageNo:1,
            userPageSize:8,
            userTotalCount:0,

            rowSelection: {
                onChange: this.onSelectChange.bind(this),
                mode:"single",
                selectedRoleId: 0,
            },

            rowUserSelection: {
                onChange: this.onUserSelectChange.bind(this),
                mode:"single",
                selectedUserId: 0,

            },

            dataRuleText:[
                this.props.intl.messages[ 'permission.successCreateData'],
                this.props.intl.messages[ 'permission.newDataRule'],
                this.props.intl.messages[ 'permission.showUserPermission'],
                this.props.intl.messages[ 'permission.fieldName:'],
                this.props.intl.messages[ 'permission.rule:'],
                this.props.intl.messages[ 'permission.defaultRule'],
                this.props.intl.messages[ 'permission.correspondRole:'],
                this.props.intl.messages[ 'permission.addUserData'],
                this.props.intl.messages[ 'permission.user:'],
                this.props.intl.messages[ 'permission.fieldValue:'],
                this.props.intl.messages[ 'permission.defaultFieldValue'],
                this.props.intl.messages[ 'permission.noFieldValueMessage'],
                this.props.intl.messages[ 'permission.fieldName'],
                this.props.intl.messages[ 'permission.rule'],
                this.props.intl.messages[ 'permission.ruleDes'],

                this.props.intl.messages[ 'permission.creator'],
                this.props.intl.messages[ 'permission.createTime'],
                this.props.intl.messages[ 'permission.deleteTitle'],
                this.props.intl.messages[ 'permission.cancelDelete'],
                this.props.intl.messages[ 'permission.repeatName'],
                this.props.intl.messages[ 'permission.successDelete'],
                this.props.intl.messages[ 'permission.successAdd'],
                this.props.intl.messages[ 'permission.confirm'],
                this.props.intl.messages[ 'permission.reset'],
                this.props.intl.messages[ 'permission.cancel'],
                this.props.intl.messages[ 'permission.add'],
                this.props.intl.messages[ 'permission.delete'],
                this.props.intl.messages[ 'permission.confirmDelete'],
                this.props.intl.messages[ 'permission.inputName'],
                this.props.intl.messages[ 'permission.nullNameWarning'],
                this.props.intl.messages[ 'permission.nullDesWarning'],
                this.props.intl.messages[ 'permission.defaultSearch']
            ]
        };

    }

    //每次访问的刷新
    componentDidMount() {
        this.setState({isLoading:true})
        let getRoleUrl=API.permission+"/v1/roles/roles";
            let url = API.permission + "/v1/userRules" ;
            let params=
                {
                    pageNo:this.state.pageNo,
                    pageSize:this.state.pageSize
                }
            Axios.get(url,{params:(params)}).then((response) => {
                console.log(response.data.pageList)
                this.setState({
                    pageNo:response.data.pageNo,
                    totalCount:response.data.totalCount,
                    currentData:response.data.pageList,
                    isLoading:false
                })
            }).catch((error)=>{
                // handle error
                console.log(error);
            }).then(()=>{
                // always executed
            });

    }
    //翻页
    onChange=currentPage=> {
        this.setState({isLoading:true})
        let url = API.permission + "/v1/userRules" ;
        let params=
            {
                pageNo:currentPage,
                pageSize:this.state.pageSize
            }
        // console.log(params)
        Axios.get(url,{params:(params)}).then((response) => {
            // handle success
            this.setState({
                pageNo:response.data.pageNo,
                totalCount:response.data.totalCount,
                currentData:response.data.pageList,
                isLoading:false}
            );
        }).catch((error)=>{
            // handle error
            console.log(error);
        });
        console.log(this.state.pageNo)
    }

//改变页码
    onUserChange=currentPage=> {

        this.setState({userIsLoading:true})
        let url = API.user + "/v1/users/search" ;
        let params=
            {
                pageNo:currentPage,
                pageSize:this.state.userPageSize
            }
        Axios.get(url,{params:(params)}).then((response) => {
            // handle success
            this.setState({
                    userList:response.data.pageList,
                    userPageNo:response.data.pageNo,
                    userTotalCount:response.data.totalCount,
                    userIsLoading:false
                }
            );
        }).catch((error)=>{
            // handle error
            console.log(error);
        });
    }

    //选择角色
    onSelectChange(role) {
        let { rowSelection } = this.state;
        rowSelection.selectedRoleId = role;
        console.log("onSelectChange", role);
        this.setState({ rowSelection });
    }

    //勾选用户
    onUserSelectChange(userids) {
        let { rowUserSelection } = this.state;
        rowUserSelection.selectedUserId = userids;
        console.log("onSelectChange", userids);
        this.setState({ rowUserSelection });
    }
    //重置
    handleReset(e) {
        e.preventDefault();
        this.field.reset();
    }
    //提交
    handleSubmit(e)
    {
        e.preventDefault();
        this.field.validate((errors, values)=>{
            if(errors){
                console.log("Errors in form!!!");
                return;}
            console.log(values)
            let roleId=this.state.rowSelection.selectedRoleId[0]
            console.log(roleId)

            let createUserRuleUrl=API.permission+"/v1/userRules"

            let RuleParams={fieldName: values.fieldName,rule:values.rule,roleId:roleId}

            this.setState({
                ruleVisible: false
            });
            Axios.post(createUserRuleUrl,{},{params:(RuleParams)})
                .then(response=>{
                    this.componentDidMount()
                }
                )
        })
    }

    //弹出创建数据规则窗
    onRuleOpen = () => {

        let url=API.permission+"/v1/roles/roles";
        Axios.get(url).then(response=>{

            this.setState({roleList:response.data})
        }).catch(error=>{
            console.log(error)
        })

        this.setState({
            ruleVisible: true
        });
    };
    //关闭创建数据规则弹窗
    onRuleClose = reason => {
        console.log(reason);
        this.setState({
            ruleVisible: false
        });
    };

    //确认删除操作以及
    //删除功能点
    onConfirm = id => {

        const { dataSource } = this.state;
        let url = API.permission + "/v1/userRules/{id}" ;
        let params= {id:id}
        Axios.delete(url,{params:(params)}
        )
            .then((response)=>{
                Feedback.toast.success(this.props.intl.messages[ 'permission.successDelete'])
                this.onChange(this.state.currentPage)
            }).catch((error)=> {
            console.log(error);
        });
    };
    //取消删除操作
    onCancel = () => {
        console.log('取消删除');
        Feedback.toast.error(this.props.intl.messages[ 'permission.cancelDelete'])

    }

    //弹出添加用户数据窗口
    addUserData=record=>{
        console.log(record)
        //获取当前选择的规则ID并填入
        this.setState({currentRuleId:record.id})

        //获取用户列表
        let url = API.user + "/v1/users/search" ;
        let params=
            {
                pageNo:1,
                pageSize:this.state.userPageSize
            }
        Axios.get(url,{params:(params)}).then((response) => {
            this.setState({
                userList:response.data.pageList,
                userPageNo:response.data.pageNo,
                userTotalCount:response.data.totalCount,
                userIsLoading:false
            })
        }).catch((error)=>{
            // handle error
            console.log(error);
        }).then(()=>{
            // always executed
        });


        //将用户列表填入下拉选项中
        let tmpList=[]
        this.state.userList.forEach(item=>{
            tmpList.push({label:item.name,value:item.id})
        })
        this.setState({
            dataVisible: true,
            userSelectList:tmpList,
        });
        console.log(this.state.userSelectList)
    }
    //关闭添加用户数据窗口
    onDataClose = reason => {
        console.log(reason);
        this.setState({
            dataVisible: false
        });
    };

    //重置用户数据窗口
    handleResetData(e) {
        e.preventDefault();
        this.dataField.reset();
    }
    //提交用户数据
    handleSubmitData(e)
    {
        e.preventDefault();
        this.dataField.validate((errors, values)=>{
            if(errors){
                console.log("Errors in form!!!");
                return;}
            this.setState({dataVisible: false})
            let url=API.permission+"/v1/userData"
            let params={ruleId:this.state.currentRuleId,userId:this.state.rowUserSelection.selectedUserId[0],fieldValue:values.fieldValue}

            Axios.post(url,{},{params:(params)}).then(response=>{
                Feedback.toast.success(this.props.intl.messages[ 'permission.successCreateData'])
            })

        })
    }
    render() {

        const { init } = this.field;
        const initData=this.dataField.init;
        const dialogStyle= {
            width: "60%" ,height:"70"
        }

        const formItemLayout = {
            labelCol: {
                span: 6
            },
            wrapperCol: {
                span: 14
            }
        };
        const footer = (
            <a onClick={this.onRuleClose} href="javascript:">
                {this.props.intl.messages[ 'permission.cancel']}
            </a>
        );
        const footer2 = (
            <a onClick={this.onDataClose} href="javascript:">
                {this.props.intl.messages[ 'permission.cancel']}
            </a>
        );

        //删除操作定义
        const deleteRule = (value, index, record) => {
            return (
                <BalloonConfirm
                    onConfirm={this.onConfirm.bind(this, record.id)}
                    onCancel={this.onCancel}
                    title= {this.props.intl.messages[ 'permission.confirmDelete']}
                >
                    <Button
                        type="primary"
                        shape="warning"
                        size="medium"
                        className="button"> {this.props.intl.messages[ 'permission.delete']}</Button>
                </BalloonConfirm>
            );
        }

        //添加用户数据
        const addUserData = (value, index, record) => {
            return (
                <Button
                    type="primary"
                    shape="normal"
                    size="medium"
                    className="button"
                    onClick={this.addUserData.bind(this,record)}> {this.props.intl.messages[ 'permission.add']}</Button>
            );
        }
        return (
        <div>
            <Button type="primary"
                    className="topButton"
                    onClick={this.onRuleOpen} > {this.props.intl.messages[ 'permission.newDataRule']}</Button>

            <Link to='/permission/roles/userwithrole'>
                <Button type="primary"
                        className="topButton"
                > {this.props.intl.messages[ 'permission.showUserPermission']}</Button>
            </Link>
            <Dialog
                title= {this.props.intl.messages[ 'permission.newDataRule']}
                visible={this.state.ruleVisible}
                onClose={this.onRuleClose}
                style={dialogStyle}
                minMargin={5}
                footer={footer}
                shouldUpdatePosition={true}>

                <Form field={this.field}>
                    <FormItem
                        label= {this.props.intl.messages[ 'permission.fieldName:']}
                        {...formItemLayout}
                        hasFeedback
                    >
                        <Input
                            maxLength={30}
                            hasLimitHint
                            placeholder= {this.props.intl.messages[ 'permission.inputName']}
                            {...init("fieldName", {
                                rules: [
                                    { required: true, min: 1, message:  this.props.intl.messages[ 'permission.nullNameWarning'] },
                                ]
                            })}
                        />
                    </FormItem>

                    <FormItem label= {this.props.intl.messages[ 'permission.rule:']} {...formItemLayout} required>
                        <Input
                            maxLength={10}
                            hasLimitHint
                            placeholder= {this.props.intl.messages[ 'permission.defaultRule']}
                            {...init("rule", {
                                rules: [
                                    { required: true, min: 1, message: this.props.intl.messages[ 'permission.defaultRule'] },
                                ]
                            })}
                        />
                    </FormItem>

                    <FormItem label= {this.props.intl.messages[ 'permission.correspondRole:']} {...formItemLayout} required>
                        <Table
                            hasBorder={false}
                            dataSource={this.state.roleList}
                            primaryKey="id"
                            isTree
                            rowSelection={this.state.rowSelection}
                        >
                            <Table.Column title={this.props.intl.messages[ 'permission.roleId']}  dataIndex="id" />
                            <Table.Column title={this.props.intl.messages[ 'permission.roleName']}  dataIndex="name" />
                        </Table>
                    </FormItem>

                    <FormItem wrapperCol={{ offset: 6 }}>
                        <Button type="primary" onClick={this.handleSubmit.bind(this)}>
                            {this.props.intl.messages[ 'permission.confirm']}
                        </Button>
                        &nbsp;&nbsp;&nbsp;
                        <Button onClick={this.handleSubmit.bind(this)}>{this.props.intl.messages[ 'permission.reset']} </Button>
                    </FormItem>

                </Form>

            </Dialog>

            <Dialog
                title={this.props.intl.messages[ 'permission.addUserData']}
                visible={this.state.dataVisible}
                onClose={this.onDataClose}
                style={dialogStyle}
                minMargin={5}
                footer={footer2}
                shouldUpdatePosition={true}
            >
                <Form field={this.dataField}>
                    <FormItem label={this.props.intl.messages[ 'permission.user:']}  {...formItemLayout} required>
                        <Table
                            isloading={this.state.userIsLoading}
                            hasBorder={false}
                            dataSource={this.state.userList}
                            primaryKey="id"
                            isTree
                            rowSelection={this.state.rowUserSelection}
                        >
                            <Table.Column title={this.props.intl.messages[ 'permission.userName']}  dataIndex="name" />
                            <Table.Column title={this.props.intl.messages[ 'permission.userEmail']}  dataIndex="email" />
                        </Table>
                        <Pagination total={this.state.userTotalCount}
                                    current={this.state.userPageNo}
                                    onChange={this.onUserChange}
                                    pageSize={this.state.userPageSize}
                                    className="pagination" />
                    </FormItem>

                    <FormItem label={this.props.intl.messages[ 'permission.fieldValue']}  {...formItemLayout} required>
                        <Input
                            maxLength={10}
                            hasLimitHint
                            placeholder={this.props.intl.messages[ 'permission.defaultFieldValue']}
                            {...initData("fieldValue", {
                                rules: [
                                    { required: true, min: 1, message: this.props.intl.messages[ 'permission.noFieldValueMessage'] },
                                ]
                            })}
                        />
                    </FormItem>

                    <FormItem wrapperCol={{ offset: 6 }}>
                        <Button type="primary" onClick={this.handleSubmitData.bind(this)}>
                            {this.props.intl.messages[ 'permission.confirm']}
                        </Button>
                        &nbsp;&nbsp;&nbsp;
                        <Button onClick={this.handleResetData.bind(this)}>{this.props.intl.messages[ 'permission.reset']}</Button>
                    </FormItem>

                </Form>
            </Dialog>

            <Table
                hasBorder={false}
                isLoading={this.state.isLoading}
                dataSource={this.state.currentData}>
                <Table.Column title={this.props.intl.messages[ 'permission.ruleId']} dataIndex="id" width="10%"/>
                <Table.Column title={this.props.intl.messages[ 'permission.roleName']}   dataIndex="roleName" width="10%"/>
                <Table.Column title={this.props.intl.messages[ 'permission.fieldName']} dataIndex="fieldName" width="15%"/>
                <Table.Column title={this.props.intl.messages[ 'permission.rule']} dataIndex="rule" width="10%"/>
                <Table.Column title={this.props.intl.messages[ 'permission.ruleDes']} dataIndex="description" />
                <Table.Column title={this.props.intl.messages[ 'permission.addUserData']} cell={addUserData}  width="10%" />
                <Table.Column title={this.props.intl.messages[ 'permission.deleteTitle']} cell={deleteRule}  width="10%" />


            </Table>
            <Pagination total={this.state.totalCount}
                        current={this.state.pageNo}
                        onChange={this.onChange}
                        pageSize={this.state.pageSize
                        }
                        className="pagination" />
        </div>
        );
    }
}
export default injectIntl(DataRules)