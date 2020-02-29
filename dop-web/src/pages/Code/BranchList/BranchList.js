import React from 'react';
import Axios from 'axios';
import API from "../../API";
import {injectIntl,FormattedRelative } from 'react-intl';

import { Loading } from "@icedesign/base";
import Spinner from '../components/Spinner';

import './BranchList.css'
import imgBranch from './imgs/branch.png';
import imgCommit from './imgs/commit.png';
import imgDelete from './imgs/delete.png';
import imgDeleteDisabled from './imgs/delete-disabled.png';

const spinner=(
    <Spinner/>
);

class BranchList extends React.Component{

    constructor(props){
        super(props);
        const {projectname,username}=this.props.match.params;
        this.state={
            username:username,
            projectname:projectname,
            projectid:username+"/"+projectname,
            branchList:[],//name,default_,protected_,merged,commit_id,commit_short_id,commit_msg,commit_time
            showData:[],
            nameInput:"",
            defaultBranch:"",
            loadingVisible:true,
            accessInfo:{
                access_level:0,
                visibility:"private",
            },
        };
    }

    componentWillMount(){
        this.loadData();
    }

    loadAccess(){
        let url = API.code+"/projects/"+this.state.projectid+"/access";
        Axios.get(url).then(response=>{
            this.setState({
                accessInfo:response.data
            });
        });
    }

    loadData(){
        let url=API.code+"/projects/"+this.state.projectid+"/repository/branches";
        Axios.get(url).then(response=>{
            let defaultBranch;
            for(let i=0;i<response.data.length;i++){
                if(response.data[i].default_==="true"){
                    defaultBranch=response.data[i].name;
                }
            }
            this.setState({
                branchList:response.data,
                showData:response.data,
                nameInput:"",
                defaultBranch:defaultBranch,
                loadingVisible:false,
            })
        });
        this.loadAccess();
    }

    changeBranchName(e){
        let val=e.target.value;
        if(val!==""){
            let showData=this.state.branchList.filter(item=>{
                return item.name.indexOf(val)!==-1;
            });
            this.setState({
                nameInput:val,
                showData:showData
            })

        }else{
            this.setState({
                nameInput:val,
                showData:this.state.branchList
            })
        }
    }

    settingsLink(){
        let {username,projectname}=this.state;
        this.props.history.push("/code/"+username+"/"+projectname+"/protected_branches");
    }

    deleteMergedBranch(){
        if(window.confirm(this.props.intl.messages["code.branchlist.deletemerged.confirm"])) {
            this.setState({
                loadingVisible:true
            });
            let url = API.code + "/projects/" + this.state.projectid + "/repository/merged_branches";
            Axios.delete(url).then(() => {
                setTimeout(()=>{
                    this.loadData();
                },1000);
            })
        }
    }

    deleteBranch(branch){
        if(window.confirm(this.props.intl.messages["code.branchlist.delete.confirm"]+branch+"?")) {
            this.setState({
                loadingVisible:true
            });
            let url = API.code + "/projects/" + this.state.projectid + "/repository/branches?branch=" + branch;
            Axios.delete(url).then(() => {
                this.loadData();
            })
        }
    }

    newBranch(){
        this.props.history.push("/code/"+this.state.projectid+"/branches/new");
    }

    commitLink(sha){
        this.props.history.push("/code/"+this.state.projectid+"/commit/"+sha);
    }

    newMergeRequest(source){
        this.props.history.push("/code/"+this.state.projectid+"/merge_requests/new?source="+source+"&target="+this.state.defaultBranch);
    }
    render(){
        const accessInfo=this.state.accessInfo;
        return (
            <div className="branch-list-container">
                <div className="div-branch-list-top">
                    {
                        (()=>{
                            if(accessInfo.access_level===40){
                                return <span className="text-branch-list-top">{this.props.intl.messages["code.branchlist.delete.top"]}</span>
                            }
                        })()
                    }
                    {
                        (()=>{
                            if(accessInfo.access_level>=30){
                                return [
                                    <button onClick={this.newBranch.bind(this)} className="btn-new-branch">+{this.props.intl.messages["code.branchlist.btn.new"]}</button>,
                                    <button onClick={this.deleteMergedBranch.bind(this)} className="btn-delete-merged-branches">{this.props.intl.messages["code.branchlist.btn.deletemerged"]}</button>
                                ];
                            }
                        })()
                    }

                    <input value={this.state.nameInput} onChange={this.changeBranchName.bind(this)} className="input-branch-list-name" placeholder={this.props.intl.messages["code.branchlist.placeholder"]}/>
                </div>
                <Loading visible={this.state.loadingVisible} className="loading-branch-list" tip={spinner}>
                    <div>
                        {
                            this.state.showData.map(item=>{
                                return (
                                    <div className="div-branch-list-item">
                                        <div className="div-branch-item-intro">
                                            <div className="div-branch-item-name">
                                                <img className="img-branch-item-intro" src={imgBranch}/>
                                                <span className="text-branch-item-name">{item.name}</span>
                                                {
                                                    (() => {
                                                        if(item.default_==="true") return <label className="label-branch-item label-branch-item-default">default</label>;

                                                    })()
                                                }
                                                {
                                                    (() => {
                                                        if(item.protected_==="true") return <label className="label-branch-item label-branch-item-protected">protected</label>;
                                                    })()
                                                }
                                                {
                                                    (()=>{
                                                        if(item.merged==="true") return <label className="label-branch-item label-branch-item-merged">merged</label>;
                                                    })()
                                                }

                                            </div>
                                            <div className="div-branch-item-commit">
                                                <img className="img-branch-item-intro" src={imgCommit}/>
                                                <a className="text-branch-item-primary" onClick={this.commitLink.bind(this,item.commit_id)}>{item.commit_short_id}</a>
                                                <a onClick={this.commitLink.bind(this,item.commit_id)}>&nbsp;·&nbsp;{item.commit_msg}&nbsp;·&nbsp;</a>
                                                <label><FormattedRelative value={new Date(parseInt(item.commit_time))}/></label>
                                            </div>
                                        </div>
                                        <div className="div-branch-item-operation">
                                            {
                                                (()=>{
                                                    if(accessInfo.access_level>=30) {
                                                        if (item.default_ === "true" || item.protected_ === "true") {
                                                            return (
                                                                <a className="btn-branch-item-delete-disabled">
                                                                    <img className="img-branch-item-delete"
                                                                         src={imgDeleteDisabled}/>
                                                                </a>
                                                            )
                                                        } else {
                                                            return (
                                                                <a onClick={this.deleteBranch.bind(this, item.name)}
                                                                   className="btn-branch-item-delete">
                                                                    <img className="img-branch-item-delete"
                                                                         src={imgDelete}/>
                                                                </a>
                                                            )
                                                        }
                                                    }
                                                })()
                                            }

                                            {
                                                (() => {
                                                    if (item.default_ === "false"&&accessInfo.access_level>=30) {
                                                        return <a onClick={this.newMergeRequest.bind(this,item.name)} className="btn-branch-item-merge">{this.props.intl.messages["code.branchlist.mergerequest"]}</a>
                                                    }
                                                })()
                                            }
                                        </div>
                                    </div>
                                )
                            })
                        }

                    </div>
                </Loading>



            </div>
        )
    }
}

export default injectIntl((props)=><BranchList {...props} key={props.location.pathname} />)
