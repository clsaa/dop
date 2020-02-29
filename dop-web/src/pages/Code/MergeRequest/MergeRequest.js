import React from 'react'
import Axios from 'axios';
import API from "../../API";
import {injectIntl,FormattedRelative } from 'react-intl';

import './MergeRequest.css'

import imgOk from './imgs/ok.png';
import imgWarning from './imgs/warning.png';

class MergeRequest extends React.Component{

    constructor(props) {
        super(props);
        let {projectname, username, iid} = this.props.match.params;
        this.state = {
            username: username,
            projectname: projectname,
            projectid: username + "/" + projectname,
            iid:iid,
            mrInfo:{
                state:"",
                created_by:"",
                created_at:"",
                merged_by:"",
                merged_at:"",
                closed_by:"",
                closed_at:"",
                target_branch:"",
                source_branch:"",
                title:"",
                description:"",

            },
            accessInfo:{
                access_level:0,
                visibility:"private",
            },
            target_branch:{
                protected_:true,
                developers_can_merge:false
            },
        }
    }

    loadAccess(){
        let url = API.code+"/projects/"+this.state.projectid+"/access";
        Axios.get(url).then(response=>{
            this.setState({
                accessInfo:response.data
            });
        });
    }

    componentWillMount(){
        this.loadData();
    }

    loadData(){
        let url=API.code+"/projects/"+this.state.projectid+"/merge_requests/"+this.state.iid;
        Axios.get(url).then(response=>{
            this.setState({
                mrInfo:response.data
            },this.loadCanMerge.bind(this));
        });
        this.loadAccess();
    }

    loadCanMerge(){
        let url= API.code+"/projects/"+this.state.projectid+"/repository/branch?branch="+this.state.mrInfo.target_branch;
        Axios.get(url).then(response=>{
            this.setState({
                target_branch:response.data
            })
        });
    }

    updateMR(state_event){
        let url=API.code+"/projects/"+this.state.projectid+"/merge_requests/"+this.state.iid;
        Axios({
            method: 'PUT',
            url: url,
            headers: {
                'Content-type': 'application/x-www-form-urlencoded',
            },
            params:{
                state_event:state_event
            },
        }).then(response=>{
            this.loadData();
        })
    }

    acceptMR(){
        let url=API.code+"/projects/"+this.state.projectid+"/merge_requests/"+this.state.iid+"/merge";
        Axios({
            method: 'PUT',
            url: url,
            headers: {
                'Content-type': 'application/x-www-form-urlencoded',
            },
            params:{},
        }).then(response=>{
            this.loadData();
        })
    }

    render(){
        const {mrInfo,accessInfo,target_branch}=this.state;
        return (
            <div className="mr-container">
                <div className="div-mr-top">
                    <div className="div-mr-author">
                        {
                            (()=>{
                                if(mrInfo.state==="opened"){
                                    return <label className="label-mr-opened">Opened</label>
                                }else if(mrInfo.state==="merged"){
                                    return <label className="label-mr-merged">Merged</label>
                                }else if(mrInfo.state==="closed"){
                                    return <label className="label-mr-closed">Closed</label>
                                }
                            })()
                        }
                        <span className="text-mr-author-time"><FormattedRelative value={new Date(parseInt(mrInfo.created_at))}/></span>{this.props.intl.messages["code.mergerequest.createdby"]}
                        <div className="div-mr-author-avatar">{mrInfo.created_by.charAt(0)}</div><b>{mrInfo.created_by}</b>
                    </div>
                    {
                        (()=>{
                            if(mrInfo.state==="opened"&&accessInfo.access_level>=30){
                                return <button onClick={this.updateMR.bind(this,"close")} className="btn-mr-close">{this.props.intl.messages["code.mergerequest.close"]}</button>;
                            }
                        })()
                    }
                </div>
                <div className="div-mr-title">
                    {mrInfo.title}
                </div>
                <div className="div-mr-description">
                    {mrInfo.description}
                </div>

                <div className="div-mr-msg">{this.props.intl.messages["code.mergerequest.requesttomerge"]}&nbsp;&nbsp;<b>{mrInfo.source_branch}</b>&nbsp;&nbsp;{this.props.intl.messages["code.mergerequest.to"]}&nbsp;&nbsp;<b>{mrInfo.target_branch}</b></div>

                {
                    (()=>{
                        if(mrInfo.state==="opened"){
                            if(mrInfo.merge_status==="can_be_merged"){
                                if(target_branch.protected_==="false"||accessInfo.access_level===40||accessInfo.access_level===30&&target_branch.developers_can_merge) {
                                    return (
                                        <div className="div-mr-operation">
                                            <img className="img-mr-tip" src={imgOk}/>
                                            <button onClick={this.acceptMR.bind(this)} className="btn-mr-merge">Merge</button>
                                            {this.props.intl.messages["code.mergerequest.open.canbemerged"]}
                                        </div>
                                    )
                                }else {
                                    return (
                                        <div className="div-mr-operation">
                                            <img className="img-mr-tip" src={imgWarning}/>
                                            {this.props.intl.messages["code.mergerequest.open.noaccess"]}
                                        </div>
                                    )
                                }
                            }else{
                                if(mrInfo.changes_count===null){
                                    return (
                                        <div className="div-mr-operation">
                                            <img className="img-mr-tip" src={imgWarning}/>
                                            {this.props.intl.messages["code.mergerequest.open.nochanges"]}
                                        </div>
                                    )
                                }else {
                                    return (
                                        <div className="div-mr-operation">
                                            <img className="img-mr-tip" src={imgWarning}/>
                                            {this.props.intl.messages["code.mergerequest.open.conflict"]}
                                        </div>
                                    )
                                }
                            }
                        }
                    })()
                }

                {
                    (()=>{
                        if(mrInfo.state==="merged"){
                            return (
                                <div className="div-mr-operation">
                                    <img className="img-mr-tip" src={imgOk}/>
                                    <span className="text-mr-author-time"><FormattedRelative value={new Date(parseInt(mrInfo.merged_at))}/></span>{this.props.intl.messages["code.mergerequest.mergedby"]}
                                    <div className="div-mr-author-avatar">{mrInfo.merged_by.charAt(0)}</div><b>{mrInfo.merged_by}</b>
                                </div>
                            )
                        }
                    })()
                }

                {
                    (()=>{
                        if(mrInfo.state==="closed"){
                            return (
                                <div className="div-mr-operation">
                                    <img className="img-mr-tip" src={imgWarning}/>
                                    {
                                        (()=>{
                                            if(accessInfo.access_level>=30){
                                                return <button onClick={this.updateMR.bind(this,"reopen")} className="btn-mr-reopen">Reopen</button>;
                                            }
                                        })()
                                    }
                                    <span className="text-mr-author-time"><FormattedRelative value={new Date(parseInt(mrInfo.closed_at))}/></span>{this.props.intl.messages["code.mergerequest.closedby"]}
                                    <div className="div-mr-author-avatar">{mrInfo.closed_by.charAt(0)}</div><b>{mrInfo.closed_by}</b>
                                </div>
                            )
                        }
                    })()
                }

            </div>
        )
    }
}

export default injectIntl((props)=><MergeRequest {...props} key={props.location.pathname} />)
