import React from 'react';
import {Select} from "@icedesign/base";
import Axios from 'axios';
import API from "../../API";
import {Loading} from "@icedesign/base";
import Spinner from '../components/Spinner';
import {injectIntl } from 'react-intl';

import './MemberList.css'

import imgDelete from './imgs/delete.png';

const spinner = (
    <Spinner/>
);

class MemberList extends React.Component{

    constructor(props){
        super(props);
        let {username, projectname} = this.props.match.params;
        this.state = {
            username: username,
            projectname: projectname,
            projectid: username + "/" + projectname,
            user_name:"",
            user_role:"",
            roleOptions:[
                {value:"Guest",label:"Guest"},
                {value:"Reporter",label:"Reporter"},
                {value:"Developer",label:"Developer"},
                {value:"Maintainer",label:"Maintainer"}
                ],
            memberList:[],//id,username,access_level
            loadingVisible:true,
        }
    }


    componentWillMount(){
        this.loadData();
    }

    loadData(){
        let url=API.code+"/projects/"+this.state.projectid+"/members";
        Axios.get(url).then(response=>{
            this.setState({
                user_name:"",
                user_role:"",
                memberList:response.data,
                loadingVisible:false,
            })
        })
    }

    selectRole(value){
        this.setState({
            user_role:value
        })
    }

    inputUser_name(e){
        this.setState({
            user_name:e.target.value
        })
    }

    getRoleByAccessLevel(access_level){
        if(access_level===10){
            return "Guest"
        }else if(access_level===20){
            return "Reporter"
        }else if(access_level===30){
            return "Developer"
        }else {
            return "Maintainer"
        }
    }

    getAccessLevelByRole(role){
        if(role==="Guest"){
            return 10;
        }else if(role==="Reporter"){
            return 20;
        }else if(role==="Developer"){
            return 30;
        }else {
            return 40;
        }
    }

    addMember(){
        this.setState({
            loadingVisible:true
        });
        let url=API.code+"/projects/"+this.state.projectid+"/members";
        Axios({
            method:"POST",
            headers:{'Content-type':'application/x-www-form-urlencoded',},
            url:url,
            params:{
                user_name:this.state.user_name,
                access_level:this.getAccessLevelByRole(this.state.user_role),
            }
        }).then(response=>{
            this.loadData();
        })
    }


    deleteMember(user_id,user_name){
        if(window.confirm(this.props.intl.messages["code.memberlist.delete.confirm"]+":"+user_name+"?")) {
            this.setState({
                loadingVisible: true
            });
            let url = API.code + "/projects/" + this.state.projectid + "/members?user_id=" + user_id;
            Axios.delete(url).then(response => {
                this.loadData();
            })
        }
    }

    changeRole(user_id,value){
        this.setState({
            loadingVisible:true
        });
        let url=API.code+"/projects/"+this.state.projectid+"/members";
        Axios({
            method:"PUT",
            headers:{'Content-type':'application/x-www-form-urlencoded',},
            url:url,
            params:{
                user_id:user_id,
                access_level:this.getAccessLevelByRole(value),
            }
        }).then(response=>{
            this.loadData();
        })
    }


    render(){
        return (
            <div className="member-list-container">
                <Loading className="loading-member-list" visible={this.state.loadingVisible} tip={spinner}>
                    <div className="div-member-list-top">
                        {this.props.intl.messages["code.memberlist.top"]}
                    </div>
                    <div>
                        <div className="div-member-list-input">
                            <span className="text-member-list-name">{this.props.intl.messages["code.memberlist.username"]}</span>
                            <input value={this.state.user_name} onChange={this.inputUser_name.bind(this)} className="input-member-list-name"/>
                        </div>
                        <div className="div-member-list-input">
                            <span className="text-member-list-name">{this.props.intl.messages["code.memberlist.role"]}</span>
                            <Select value={this.state.user_role} onChange={this.selectRole.bind(this)} className="select-member-list" size='large' dataSource={this.state.roleOptions} />
                        </div>
                        <div className="div-member-list-submit">
                            <button onClick={this.addMember.bind(this)} className="btn-member-list-add">{this.props.intl.messages["code.memberlist.add"]}</button>
                        </div>
                    </div>
                    <div className="div-member-list-middle">
                        {this.props.intl.messages["code.memberlist.list.top"]}
                    </div>
                    {
                        this.state.memberList.map(item=>{
                            return (
                                <div className="div-member-list-item">
                                    <div className="div-member-list-item-intro">
                                        <div className="div-member-list-item-avatar">
                                            {item.username.charAt(0).toUpperCase()}
                                        </div>
                                        <b>{item.username}</b>
                                        {
                                            (()=>{
                                                if(item.username===sessionStorage.getItem("user-name")){
                                                    return <label className="label-member-list-item-self">It's you</label>
                                                }
                                            })()
                                        }
                                    </div>
                                    <div className="div-member-list-item-operation">
                                        {
                                            (()=>{
                                                if(item.username===sessionStorage.getItem("user-name")){
                                                    return <span className="text-member-list-item-role">{this.getRoleByAccessLevel(item.access_level)}</span>
                                                }else {
                                                    return [<a onClick={this.deleteMember.bind(this,item.id,item.username)} className="btn-member-list-item-delete"><img className="img-member-list-item-delete" src={imgDelete}/></a>,
                                                    <Select onChange={this.changeRole.bind(this,item.id)} value={this.getRoleByAccessLevel(item.access_level)} className="select-member-list-item" size='large' dataSource={this.state.roleOptions} />]
                                                }
                                            })()
                                        }
                                    </div>
                                </div>
                            )
                        })
                    }
                </Loading>
            </div>
        )
    }
}

export default injectIntl((props) => <MemberList {...props} key={props.location.pathname}/>)

