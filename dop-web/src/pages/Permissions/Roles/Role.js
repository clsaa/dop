import React, { Component } from 'react';

import Axios from 'axios';
import API from '../../API';
import {Button, Dialog, Feedback, Field, Table} from "@icedesign/base";
import Pagination from "@icedesign/base/lib/pagination";
import Form from "@icedesign/base/lib/form";
import BalloonConfirm from "@icedesign/balloon-confirm";
import Input from "@icedesign/base/lib/input";
import Select, {Option} from "@icedesign/base/lib/select";
import { Link } from 'react-router-dom';
import Search from "@icedesign/base/lib/search";
import {injectIntl} from "react-intl";
import {Permission} from "../Permissions/Permission";
const { Item: FormItem } = Form;
/**
 *  角色管理
 *  @author lizhiying
 *
 *
 * */

export class Role extends Component {


    constructor(props) {
        super(props);
        this.field = new Field(this);
        this.state = {
            currentData : [],
            permissionList:[],
            roleSelectList:[],
            currentPermission:[],
            rowSelection: {
                onChange: this.onSelectChange.bind(this),
                onSelectAll: function(selected) {
                    console.log("onSelectAll", selected);
                },
                selectedRowKeys: [],

            },
            permissionVisible:false,
            currentRoleId:0,
            visible:false,
            isLoading:true,
            deleteVisible:false,
            InputInfo:
                {
                    parentId:"",
                    name:"",
                },

            pageNo:1,
            pageSize:8,
            totalCount:0,

           permissionPageNo:1,
           permissionPageSize:8,
           permissionTotalCount:0,

            roleText:[
                this.props.intl.messages[ 'permission.successCreateMessage'],
                this.props.intl.messages[ 'permission.failCreateMessage'],
                this.props.intl.messages[ 'permission.editRole'],
                this.props.intl.messages[ 'permission.newRole'],
                this.props.intl.messages[ 'permission.roleDistribution'],
                this.props.intl.messages[ 'permission.roleName'],
                this.props.intl.messages[ 'permission.parentRole'],
                this.props.intl.messages[ 'permission.rolePermission:'],
                this.props.intl.messages[ 'permission.rolePermissionList'],
                this.props.intl.messages[ 'permission.permissionBelongRole'],
                this.props.intl.messages[ 'permission.allPermission'],
                this.props.intl.messages[ 'permission.roleId'],

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
        let url = API.permission + "/v1/roles" ;
        let params=
            {
                pageNo:this.state.pageNo,
                pageSize:this.state.pageSize,

            }
        Axios.get(url,{params:(params)}).then((response) => {
            this.setState({
                currentData:response.data.pageList,
                pageNo:response.data.pageNo,
                totalCount:response.data.totalCount,
                isLoading:false})
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
        let url = API.permission + "/v1/roles" ;
        let params=
            {
                pageNo:currentPage,
                pageSize:this.state.pageSize
            }
        // console.log(params)
        Axios.get(url,{params:(params)}).then((response) => {
            // handle success
            this.setState({
                currentData:response.data.pageList,
                pageNo:response.data.pageNo,
                totalCount:response.data.totalCount,
                isLoading:false
                }
            );
        }).catch((error)=>{
            // handle error
            console.log(error);
        });
        console.log(this.state.pageNo)
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
            let idList=this.state.rowSelection.selectedRowKeys
            console.log(idList)

            let createRoleUrl=API.permission+"/v1/roles"
            let byNameUrl=API.permission+"/v1/roles/byName"

            let RoleParam={parentId: values.parentId,name:values.name}
            let byNameParams={name:values.name}

            //先检测是否重复
            Axios.get(byNameUrl,{params:(byNameParams)}
            ).then((response) => {
                // handle success
                console.log("监测重复返回的东西："+response.data.name);
                if(response.data.name==values.name)
                {
                    Feedback.toast.error( this.props.intl.messages[ 'permission.repeatName'])
                }
                else {
                    //不重复 则关闭窗口 开始插入数据
                    this.setState({
                        visible: false
                    });
                    //插入角色
                    Axios.post(createRoleUrl, {},{params:(RoleParam)}
                    )
                        .then((response)=>{
                            if((response.data)!='')
                            {Feedback.toast.success( this.props.intl.messages[ 'permission.successCreateMessage'] +response.data)}
                            else
                            {Feedback.toast.error( this.props.intl.messages[ 'permission.failCreateMessage'])}
                            console.log(typeof (response.data))
                            this.componentDidMount()
                            //插入关联关系
                            let createMapUrl=API.permission+"/v1/roles/permissionmap"
                            idList.forEach(permissionid=>{
                                let param={
                                    roleId:response.data,
                                    permissionId:permissionid}
                                Axios.post(createMapUrl,{},{params:(param)}).then(
                                    (response)=>{
                                        console.log("插入关联关系成功")
                                    }
                                )
                            })
                        }).catch((error)=> {
                        console.log(error);
                    });

                }
            })
        })
    }

    //弹出创建角色窗
    onOpen = () => {

        let roleUrl=API.permission+"/v1/roles/roles"
        Axios.get(roleUrl).then(response=>{

            let tmpList=[]
            response.data.forEach(item=>{
                tmpList.push({label:item.name,value:item.id
            })
            this.setState({roleSelectList:tmpList})
            let url=API.permission+"/v1/roles/permissions";

            Axios.get(url).then(response=>{

                this.setState({
                    permissionList:response.data
                })
            }).catch(error=>{
                console.log(error)
            })
        })

        this.setState({
            visible: true
        });
    })
    }
    //关闭创建角色弹窗
    onClose = reason => {
        console.log(reason);
        this.setState({
            visible: false
        });
    };

    //关闭创建角色弹窗
    onPermissionClose = reason => {
        console.log(reason);
        this.setState({
            permissionVisible: false
        });
    };

    //勾选功能点
    onSelectChange(permissionids) {
        let { rowSelection } = this.state;
        rowSelection.selectedRowKeys = permissionids;
        console.log("onSelectChange", permissionids);
        this.setState({ rowSelection });
    }

    //弹出角色功能点窗户
    editRoleOpen=id=>{


        console.log("ID是"+id)
            //获取当前角色功能点
        this.setState({currentRoleId:id ,permissionPageNo:1})
            let getPermissionUrl=API.permission+"/v1/roles/permissions/{id}"
            let param={roleId:id}
            Axios.get(getPermissionUrl,{params:(param)}).then(response=>
            {
                this.setState({currentPermission:response.data})
                console.log(this.state.currentPermission)
                //获取全部功能点
                let url=API.permission+"/v1/permissions";
                let params={
                    pageNo:this.state.permissionPageNo,
                    pageSize:this.state.permissionPageSize
                }
                Axios.get(url,{params:(params)}).then(response=>{
                    this.setState({
                        permissionList:response.data.pageList,
                        permissionPageNo:response.data.pageNo,
                        permissionTotalCount:response.data.totalCount
                    })
                })

            })

        this.setState({
            permissionVisible: true
        });
    }

    //功能点页数变更
    onPermissionChange=currentPermissionPage=>{
        let url=API.permission+"/v1/permissions"
        let params={
            pageNo:currentPermissionPage,
            pageSize:this.state.permissionPageSize
        }
        Axios.get(url,{params:(params)}).then(response=>{
            this.setState({
                permissionList:response.data.pageList,
                permissionPageNo:response.data.pageNo,
                permissionTotalCount:response.data.totalCount
            })
        })
    }

    //确认删除操作以及
    //删除功能点
    onConfirm = id => {

        const { dataSource } = this.state;
        let url = API.permission + "/v1/roles/{id}" ;
        let params= {id:id}
        Axios.delete(url,{params:(params)}
        )
            .then((response)=>{
                Feedback.toast.success( this.props.intl.messages[ 'permission.successDelete'])
                this.onChange(this.state.currentPage)
            }).catch((error)=> {
            console.log(error);
        });
    };
    //取消删除操作
    onCancel = () => {
        console.log('取消删除');
        Feedback.toast.error( this.props.intl.messages[ 'permission.cancelDelete'])

    }

    removePermission=id=>{
        console.log("功能点ID"+id)
        let url=API.permission+"/v1/roles/permissions"
        let param={roleId:this.state.currentRoleId,
                    permissionId:id}
        let getPermissionUrl=API.permission+"/v1/roles/permissions/{id}"
        let roleId={roleId:this.state.currentRoleId}
         Axios.delete(url,{params:(param)}).then(response=>
             //再次获取该角色功能点

            Axios.get(getPermissionUrl,{params:(roleId)}).then(response=>
        {
            Feedback.toast.success( this.props.intl.messages[ 'permission.successDelete'])
            this.setState({currentPermission:response.data})
        })
         )
    }
    addPermission=id=>{
        let url =API.permission+"/v1/roles/permissionmap"
        let param={roleId:this.state.currentRoleId,
            permissionId:id}
        let getPermissionUrl=API.permission+"/v1/roles/permissions/{id}"
        let roleId={roleId:this.state.currentRoleId}
        Axios.post(url,{},{params:(param)}).then(response=>
            Axios.get(getPermissionUrl,{params:(roleId)}).then(response1=>
            {
                Feedback.toast.success( this.props.intl.messages[ 'permission.successAdd'])
                console.log(response)
                this.setState({currentPermission:response1.data})
            })
        )
    }

    //为角色添加功能点时的搜索功能
    onSearchChange=key=>{
        let url=API.permission+"/v1/permissions"
        let params={
            pageNo:1,
            pageSize:this.state.permissionPageSize,
            key:key
        }
        Axios.get(url,{params:(params)}).then(response=>{
            this.setState({
                permissionList:response.data.pageList,
                permissionPageNo:response.data.pageNo,
                permissionTotalCount:response.data.totalCount
            })
        })
    }

    //角色的搜索功能
    onRoleSearchChange=key=>{

        this.setState({isLoading:true})
        let url=API.permission+"/v1/roles"
        let params={
            pageNo:1,
            pageSize:this.state.pageSize,
            key:key
        }
        Axios.get(url,{params:(params)}).then(response=>{
            this.setState({
                currentData:response.data.pageList,
                pageNo:response.data.pageNo,
                totalCount:response.data.totalCount,
                isLoading:false
            })
        })

    }
    render() {
        const { init } = this.field;
        const dialogStyle= {
                width: "60%" ,height:"7000"
            }
        //form样式定义
        const formItemLayout = {
            labelCol: {
                span: 6
            },
            wrapperCol: {
                span: 14
            }
        };
        //窗口按钮定义
        const footer = (
            <a onClick={this.onClose} href="javascript:">
                {this.props.intl.messages[ 'permission.cancel']}
            </a>
        );
        const footer2=(
            <a onClick={this.onPermissionClose} href="javascript:;">
                {this.props.intl.messages[ 'permission.cancel']}
            </a>
        )

        const showPermission =(value, index, record)=>{
            return(
                    <Button
                        shape="normal"
                        size="medium"
                        className="button"
                        onClick={this.editRoleOpen.bind(this,record.id)}>{this.props.intl.messages[ 'permission.editRole']}</Button>
            )
        }

        //删除操作定义
        const renderDelete = (value, index, record) => {
            return (
                <BalloonConfirm
                    onConfirm={this.onConfirm.bind(this, record.id)}
                    onCancel={this.onCancel}
                    title={this.props.intl.messages[ 'permission.confirmDelete']}
                >
                    <Button
                        type="primary"
                        shape="warning"
                        size="medium"
                        className="button"
                    >{this.props.intl.messages[ 'permission.delete']}</Button>
                </BalloonConfirm>

            );
        }
        //删除角色功能点对应按钮
        const deletePermissionMap=(value, index, record)=>{
            return (
                <Button
                    type="primary"
                    shape="warning"
                    size="medium"
                    onClick={this.removePermission.bind(this, record.id)}>{this.props.intl.messages[ 'permission.delete']}</Button>
            );
        }
        //添加角色功能点对应按钮
        const addPermissionMap=(value, index, record)=>{
            return (
                <Button
                    type="primary"
                    shape="normal"
                    size="medium"
                    onClick={this.addPermission.bind(this, record.id)}>{this.props.intl.messages[ 'permission.add']}</Button>
            );
        }

        return(

            <div>
                <Button type="primary"
                        className="topButton"
                        onClick={this.onOpen} >{this.props.intl.messages[ 'permission.newRole']}</Button>

                <Link to='/permission/roles/userwithrole'>
                    <Button type="primary"
                            className="topButton">{this.props.intl.messages[ 'permission.roleDistribution']}</Button>
                </Link>

                <Dialog
                    title={this.props.intl.messages[ 'permission.newRole']}
                    visible={this.state.visible}
                    onClose={this.onClose}
                    style={dialogStyle}
                    minMargin={5}
                    footer={footer}
                    shouldUpdatePosition={true}>

                    <Form field={this.field}>
                        <FormItem
                            label={this.props.intl.messages[ 'permission.roleName']}
                            {...formItemLayout}
                            hasFeedback
                        >
                            <Input
                                maxLength={10}
                                hasLimitHint
                                placeholder={this.props.intl.messages[ 'permission.inputName']}
                                {...init("name", {
                                    rules: [
                                        { required: true, min: 1, message:this.props.intl.messages[ 'permission.nullNameWarning'] },
                                    ]
                                })}
                            />
                        </FormItem>

                        <FormItem label={this.props.intl.messages[ 'permission.parentRole']} {...formItemLayout} required>
                            <Select dataSource={this.state.roleSelectList} style={{width:200}}  {...init("parentId")}>

                            </Select>
                        </FormItem>

                        <FormItem label='  ' {...formItemLayout} required>
                            <Table
                                hasBorder={false}
                                dataSource={this.state.permissionList}
                                primaryKey="id"
                                isTree
                                rowSelection={this.state.rowSelection}

                            >
                                <Table.Column title={this.props.intl.messages[ 'permission.permissionName']} dataIndex="name" />
                                <Table.Column title={this.props.intl.messages[ 'permission.permissionDes']} dataIndex="description" />
                            </Table>
                        </FormItem>

                        <FormItem wrapperCol={{ offset: 6 }}>
                            <Button type="primary" onClick={this.handleSubmit.bind(this)}>
                                {this.props.intl.messages[ 'permission.confirm']}
                            </Button>
                            &nbsp;&nbsp;&nbsp;
                            <Button onClick={this.handleReset.bind(this)}>{this.props.intl.messages[ 'permission.reset']}</Button>
                        </FormItem>

                    </Form>
                </Dialog>


                <Dialog
                    title={this.props.intl.messages[ 'permission.rolePermissionList']}
                    visible={this.state.permissionVisible}
                    onClose={this.onPermissionClose}
                    style={dialogStyle}
                    minMargin={5}
                    footer={footer2}
                    shouldUpdatePosition={true}>
                        <Form>
                            <FormItem>
                                <h2>{this.props.intl.messages[ 'permission.permissionBelongRole']}</h2>
                                    <Table
                                        hasBorder={false}
                                        dataSource={this.state.currentPermission}
                                        primaryKey="id">
                                        <Table.Column width ='20%' title={this.props.intl.messages[ 'permission.permissionName']} dataIndex="name" />
                                        <Table.Column title={this.props.intl.messages[ 'permission.permissionDes']} dataIndex="description" />
                                        <Table.Column title={this.props.intl.messages[ 'permission.deleteTitle']} cell={deletePermissionMap} width="10%" />
                                    </Table>
                            </FormItem>

                            <FormItem>

                                <h2>{this.props.intl.messages[ 'permission.allPermission']}</h2>

                                        <Search
                                        onChange={this.onSearchChange.bind(this)}
                                        dataSource={this.state.dataSource}
                                        placeholder={this.props.intl.messages[ 'permission.defaultSearch']}
                                        hasIcon={false}
                                        autoWidth
                                        />
                                        <Table
                                            hasBorder={false}
                                            dataSource={this.state.permissionList}
                                            primaryKey="id">
                                                <Table.Column width ='20%' title={this.props.intl.messages[ 'permission.permissionName']} dataIndex="name" />
                                                <Table.Column title={this.props.intl.messages[ 'permission.permissionDes']} dataIndex="description" />
                                                <Table.Column title={this.props.intl.messages[ 'permission.add']} cell={addPermissionMap} width="10%" />
                                        </Table>
                                <Pagination total={this.state.permissionTotalCount}
                                            current={this.state.permissionPageNo}
                                            onChange={this.onPermissionChange}
                                            pageSize={this.state.permissionPageSize}
                                            className="pagination" />
                            </FormItem>

                        </Form>
                </Dialog>

                <Search
                    className="search"
                    onChange={this.onRoleSearchChange.bind(this)}
                    dataSource={this.state.dataSource}
                    placeholder={this.props.intl.messages[ 'permission.defaultSearch']}
                    hasIcon={false}
                    autoWidth
                />

                <Table
                    hasBorder={false}
                    isLoading={this.state.isLoading}
                    dataSource={this.state.currentData}>
                    <Table.Column title={this.props.intl.messages[ 'permission.roleId']} dataIndex="id"/>
                    <Table.Column title={this.props.intl.messages[ 'permission.roleName']} dataIndex="name"/>
                    <Table.Column title={this.props.intl.messages[ 'permission.creator']}   dataIndex="userName"/>
                    <Table.Column title={this.props.intl.messages[ 'permission.createTime']} dataIndex="mtime"/>
                    <Table.Column title={this.props.intl.messages[ 'permission.editRole']} cell={showPermission} width="10%"/>
                    <Table.Column title={this.props.intl.messages[ 'permission.deleteTitle']} cell={renderDelete} width="10%" />


                </Table>
                <Pagination total={this.state.totalCount}
                            current={this.state.pageNo}
                            onChange={this.onChange}
                            pageSize={this.state.pageSize}
                            className="pagination"/>
            </div>
        )
    }
}
export default injectIntl(Role)