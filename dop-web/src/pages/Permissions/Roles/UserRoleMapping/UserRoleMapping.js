import React, { Component } from 'react';
import {Dialog, Feedback, Field, Table} from "@icedesign/base";
import API from "../../../API";
import Axios from "axios";
import Button from "@icedesign/base/lib/button";
import Pagination from "@icedesign/base/lib/pagination";
import Search from "@icedesign/base/lib/search";
import {injectIntl} from "react-intl";
import {Role} from "../Role";



/**
 *  用户关联角色管理
 *  @author lizhiying
 *
 *  @since 3019.3.7
 *
 *
 * */

export class UserRoleMapping extends Component {


    constructor(props) {
        super(props);
        this.state = {
            /*此时按当前角色来查找用户，因为只有拥有此角色，用户才能进行相应的操作，才能规定范围*/
            currentData : [],
            currentUserId:0,
            currentRoles:[],
            currentPermissions:[],
            currentUserData:[],
            roleList:[],
            visible:false,
            permissionVisible:false,
            userDataVisible:false,

            isLoading:true,
            permissionIsLoading:true,
            roleIsLoading:true,
            dataIsLoading:true,
            userIsLoading:true,

            rolePageNo:1,
            rolePageSize:8,
            roleTotalCount:0,

            dataPageNo:1,
            dataPageSize:8,
            dataTotalCount:0,

            permissionPageNo:1,
            permissionPageSize:8,
            permissionTotalCount:0,

            userPageNo:1,
            userPageSize:8,
            userTotalCount:0,

            addRolePermission:"false",

            userRoleMappingText:[
                this.props.intl.messages[ 'permission.addRole'],
                this.props.intl.messages[ 'permission.showPermission'],
                this.props.intl.messages[ 'permission.showUserData'],
                this.props.intl.messages[ 'permission.userRoleMapTable'],
                this.props.intl.messages[ 'permission.roleBelongUser'],
                this.props.intl.messages[ 'permission.allRole'],
                this.props.intl.messages[ 'permission.userPermissionMap'],
                this.props.intl.messages[ 'permission.userDataTable:'],
                this.props.intl.messages[ 'permission.defaultDataSearch'],
                this.props.intl.messages[ 'permission.ID'],
                this.props.intl.messages[ 'permission.ruleId'],
                this.props.intl.messages[ 'permission.dataDes'],
                this.props.intl.messages[ 'permission.fieldValue'],
                this.props.intl.messages[ 'permission.userName'],
                this.props.intl.messages[ 'permission.userEmail'],
                this.props.intl.messages[ 'permission.modifyTime'],

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

    };

    //每次访问的刷新，查询当前用户的信息
    componentDidMount() {
        let getPermissionUrl=API.permission+"/v1/users/permissions/ByCurrent"
        Axios.get(getPermissionUrl).then(response=>
        {
            let permissionTmp= response.data.map(x=>{return x.name})
            if(permissionTmp.indexOf("创建角色")!=-1)
            {
                console.log("可以创建角色！")
                this.setState({addRolePermission:"true"})};

            this.setState({userIsLoading:true})
            let url = API.user + "/v1/users/search" ;
            let params=
                {
                    pageNo:this.state.userPageNo,
                    pageSize:this.state.userPageSize
                }
            Axios.get(url,{params:(params)}).then((response) => {
                this.setState({
                    currentData:response.data.pageList,
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
            this.setState({userIsLoading:false})

        })



    }

    //改变页码
    onChange=currentPage=> {

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
                    currentData:response.data.pageList,
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
   //弹出角色功能点窗户
    editUserOpen=id=>{

        console.log(id)
        //获取当前用户角色
        this.setState({
            currentUserId:id ,
            currentRoles:[],
                roleIsLoading:true,
        })
        let getRoleUrl=API.permission+"/v1/users/roles/{id}"
        let param={userId:id}
        Axios.get(getRoleUrl,{params:(param)}).then(response=>
        {
            this.setState({currentRoles:response.data,roleIsLoading:false})
            console.log(this.state.currentRoles)
            //获取全部角色
            let url=API.permission+"/v1/roles";
            Axios.get(url).then(response=>{
                this.setState({
                    roleList:response.data.pageList,
                    rolePageNo:response.data.pageNo,
                    roleTotalCount:response.data.totalCount})
            })

        })

        this.setState({visible:true
        });
    }

    //用户角色页数变更
    onRoleChange=currentRolePage=>{
        let url=API.permission+"/v1/roles"
        let params={
            pageNo:currentRolePage,
            pageSize:this.state.pageSize
        }
        Axios.get(url,{params:(params)}).then(response=>{
            this.setState({
                roleList:response.data.pageList,
                rolePageNo:response.data.pageNo,
                roleTotalCount:response.data.totalCount
            })
        })
    }

    onPermissionClose=reason=>{
        this.setState({permissionVisible:false})
    }

    onUserDataClose=reason=>{
        this.setState({userDataVisible:false})
    }

    //关闭用户角色弹窗
    onClose = reason => {
        console.log(reason);
        this.setState({
            visible: false
        });
    };
    removeRole=id=>{
        console.log("用户ID"+id)
        let url=API.permission+"/v1/users/roles"
        let param={userId:this.state.currentUserId,
            roleId:id}
        let getRoleUrl=API.permission+"/v1/users/roles/{id}"
        let userId={userId:this.state.currentUserId}
        Axios.delete(url,{params:(param)}).then(response=>
            //再次获取该用户角色

            Axios.get(getRoleUrl,{params:(userId)}).then(response=>
            {
                Feedback.toast.success( this.props.intl.messages[ 'permission.succesDelete'])
                this.setState({currentRoles:response.data})
            })
        )
    }
    addRole=id=>{
        let url =API.permission+"/v1/users/rolemap"
        let param={userId:this.state.currentUserId,
            roleId:id}
        let getRoleUrl=API.permission+"/v1/users/roles/{id}"
        let userId={userId:this.state.currentUserId}
        Axios.post(url,{},{params:(param)}).then(response=>
            Axios.get(getRoleUrl,{params:(userId)}).then(response1=>
            {
                Feedback.toast.success( this.props.intl.messages[ 'permission.successAdd'])

                this.setState({currentRoles:response1.data})
            })
        )
    }

    //弹出用户功能点窗户
    showPermissions=id=>{

        //获取当前用户的功能点
        this.setState({
            currentPermissions:[],
            currentUserId:id ,
            permissionIsLoading:true})
        let getPermissionUrl=API.permission+"/v1/users/permissions/{id}"
        let param={userId:id}
        Axios.get(getPermissionUrl,{params:(param)}).then(response=>
        {
            this.setState({currentPermissions:response.data ,permissionIsLoading:false})
        })

        this.setState({permissionVisible:true
        });
    }

    //用户功能点页数变更


    //弹出用户数据窗口
    showUserData=id=>{

        //获取当前用户的所有可操作数据
        this.setState({
            currentUserId:id,
            currentUserData:[],
            dataIsLoading:true
        })
        let getDataUrl=API.permission+"/v1/userData/byUser"
        let param={userId:id}
        Axios.get(getDataUrl,{params:(param)}).then(response=>
        {
            this.setState({currentUserData:response.data ,dataIsLoading:false})
        })

        this.setState({userDataVisible:true
        });
    }
    //删除用户数据
    deleteUserData=id=>{
        console.log("用户ID"+id)
        let url=API.permission+"/v1/userData/{id}"
        let param={id:id}
        let getDataUrl=API.permission+"/v1/userData/byUser"
        let userId={userId:this.state.currentUserId}
        Axios.delete(url,{params:(param)}).then(response=>
            //再次获取该用户所有数据

            Axios.get(getDataUrl,{params:(userId)}).then(response=>
            {
                Feedback.toast.success( this.props.intl.messages[ 'permission.successDelete'])
                this.setState({currentUserData:response.data})
            })
        )
    }

    //为用户添加角色时的搜索功能
    onSearchChange=key=>{

        let url=API.permission+"/v1/roles"
        let params={
            pageNo:1,
            pageSize:this.state.pageSize,
            key:key
        }
        Axios.get(url,{params:(params)}).then(response=>{
            this.setState({
                roleList:response.data.pageList,
                rolePageNo:response.data.pageNo,
                roleTotalCount:response.data.totalCount
            })
        })

    }

    //查看用户数据时的搜索功能
    onDataSearchChange=key=>{

        let getDataUrl=API.permission+"/v1/userData/byUser"
        let params={userId:this.state.currentUserId,
                    key:key}

        Axios.get(getDataUrl,{params:(params)}).then(response=>{
            this.setState({
                currentUserData:response.data
            })
        })

    }
    render() {
        const dialogStyle= {
            width: "60%" ,height:"70%"
        }
        const showRoles =(value, index, record)=>{

            if(this.state.addRolePermission=="true")
            {
                return(
                    <Button
                        type="primary"
                        shape="normal"
                        size="medium"
                        className="button"
                        onClick={this.editUserOpen.bind(this,record.id)}> {this.props.intl.messages[ 'permission.addRole']}</Button>
                )
            }

        }
        const showPermissions =(value, index, record)=>{
            return(
                <Button onClick={this.showPermissions.bind(this,record.id)}>{this.props.intl.messages[ 'permission.showPermission']}</Button>
            )
        }
        const showUserData =(value, index, record)=>{
            return(
                <Button onClick={this.showUserData.bind(this,record.id)}>{this.props.intl.messages[ 'permission.showUserData']}</Button>
            )
        }
        const footer=(
            <a onClick={this.onClose} href="javascript:">
                {this.props.intl.messages[ 'permission.cancel']}
            </a>
        )
        const footer2=(
            <a onClick={this.onPermissionClose} href="javascript:">
                {this.props.intl.messages[ 'permission.cancel']}
            </a>
        )

        const footer3=(
            <a onClick={this.onUserDataClose} href="javascript:">
                {this.props.intl.messages[ 'permission.cancel']}
            </a>
        )
        //删除角色按钮
        const deleteRoleMap=(value, index, record)=>{
            return (
                <Button
                    type="primary"
                    shape="warning"
                    size="medium"
                    className="button"
                    onClick={this.removeRole.bind(this, record.id)}>{this.props.intl.messages[ 'permission.delete']}</Button>
            );
        }
        //添加角色按钮
        const addRoleMap=(value, index, record)=>{
            return (
                <Button
                    type="primary"
                    shape="normal"
                    size="medium"
                    className="button"
                    onClick={this.addRole.bind(this, record.id)}>{this.props.intl.messages[ 'permission.add']}</Button>
            );
        }

        const deleteUserData=(value, index, record)=>{
            return (
                <Button
                    type="primary"
                    shape="warning"
                    size="medium"
                    className="button"
                    onClick={this.deleteUserData.bind(this, record.id)}>{this.props.intl.messages[ 'permission.delete']}</Button>
            );
        }

        return(
            <div>
                <Dialog
                    title={this.props.intl.messages[ 'permission.userRoleMapTable']}
                    visible={this.state.visible}
                    onClose={this.onClose}
                    style={dialogStyle}
                    minMargin={5}
                    footer={footer}
                    shouldUpdatePosition={true}
                >
                            <h2>{this.props.intl.messages[ 'permission.roleBelongUser']}</h2>
                            <Table
                                hasBorder={false}
                                dataSource={this.state.currentRoles}
                                primaryKey="id"
                                isLoading={this.state.roleIsLoading}>
                                <Table.Column  title={this.props.intl.messages[ 'permission.roleName']} dataIndex="name" />
                                <Table.Column title={this.props.intl.messages[ 'permission.deleteTitle']}cell={deleteRoleMap} width="10%" />
                            </Table>



                            <h2>{this.props.intl.messages[ 'permission.allRole']}</h2>
                            <Search
                            onChange={this.onSearchChange.bind(this)}
                            placeholder={this.props.intl.messages[ 'permission.defaultSearch']}
                            hasIcon={false}
                            autoWidth
                            />
                            <Table
                                hasBorder={false}
                                dataSource={this.state.roleList}
                                primaryKey="id">
                                <Table.Column  title={this.props.intl.messages[ 'permission.roleName']} dataIndex="name" />
                                <Table.Column title={this.props.intl.messages[ 'permission.add']} cell={addRoleMap} width="10%" />
                            </Table>
                            <Pagination total={this.state.roleTotalCount}
                                current={this.state.rolePageNo}
                                onChange={this.onRoleChange}
                                pageSize={this.state.rolePageSize}
                                className="pagination" />
                </Dialog>

                <Dialog
                    title={this.props.intl.messages[ 'permission.userPermissionMapTable']}
                    visible={this.state.permissionVisible}
                    onClose={this.onPermissionClose}
                    style={dialogStyle}
                    minMargin={5}
                    footer={footer2}
                    shouldUpdatePosition={true}>
                    <Table
                        hasBorder={false}
                        isLoading={this.state.permissionIsLoading}
                        dataSource={this.state.currentPermissions}>
                        <Table.Column  title={this.props.intl.messages[ 'permission.permissionName']} dataIndex="name" />
                        <Table.Column  title={this.props.intl.messages[ 'permission.permissionDes']} dataIndex="description" />
                    </Table>

                </Dialog>

                <Dialog
                    title={this.props.intl.messages[ 'permission.userDataTable']}
                    visible={this.state.userDataVisible}
                    onClose={this.onUserDataClose}
                    style={dialogStyle}
                    minMargin={5}
                    footer={footer3}
                    shouldUpdatePosition={true}>
                    <Search
                        onChange={this.onDataSearchChange.bind(this)}
                        placeholder={this.props.intl.messages[ 'permission.defaultDataSearch']}
                        hasIcon={false}
                        autoWidth
                    />
                    <Table
                        hasBorder={false}
                        dataSource={this.state.currentUserData}
                        isLoading={this.state.dataIsLoading}>
                        <Table.Column  title={this.props.intl.messages[ 'permission.ID']} dataIndex="id" width="10%" />
                        <Table.Column  title={this.props.intl.messages[ 'permission.ruleId']} dataIndex="ruleId" width="10%" />
                        <Table.Column  title={this.props.intl.messages[ 'permission.dataDes']} dataIndex="description" />
                        <Table.Column  title={this.props.intl.messages[ 'permission.fieldValue']} dataIndex="fieldValue" width="15%" />
                        <Table.Column  title={this.props.intl.messages[ 'permission.deleteTitle']} cell={deleteUserData}  width="10%" />
                    </Table>

                </Dialog>

                <Table
                    hasBorder={false}
                    isLoading={this.state.userIsLoading}
                    dataSource={this.state.currentData}>
                    <Table.Column title={this.props.intl.messages[ 'permission.userName']} dataIndex="name"/>
                    <Table.Column title={this.props.intl.messages[ 'permission.userEmail']}   dataIndex="email"/>
                    <Table.Column title={this.props.intl.messages[ 'permission.modifyTime']}  dataIndex="mtime"/>
                    <Table.Column title={this.props.intl.messages[ 'permission.roleDistribution']}  cell={showRoles} width="10%"/>
                    <Table.Column title={this.props.intl.messages[ 'permission.showUserData']} cell={showUserData} width="10%"/>
                    <Table.Column title={this.props.intl.messages[ 'permission.showPermission']} cell={showPermissions} width="10%"/>

                </Table>
                <Pagination total={this.state.userTotalCount}
                            current={this.state.userPageNo}
                            onChange={this.onChange}
                            pageSize={this.state.userPageSize}
                            className="pagination" />
            </div>
        )
    }

}
export default injectIntl(UserRoleMapping)