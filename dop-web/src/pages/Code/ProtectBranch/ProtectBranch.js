import React from 'react';
import {Select} from "@icedesign/base";
import Axios from 'axios';
import API from "../../API";
import {Loading} from "@icedesign/base";
import {Checkbox} from '@icedesign/base';
import Spinner from '../components/Spinner';
import {injectIntl } from 'react-intl';

import './ProtectBranch.css'

const spinner = (
    <Spinner/>
);

class ProjectBranch extends React.Component{

    constructor(props) {
        super(props);
        let {username, projectname} = this.props.match.params;
        this.state = {
            username: username,
            projectname: projectname,
            projectid: username + "/" + projectname,
            ref:"",
            developers_can_push:false,
            developers_can_merge:false,
            branchList: [],//name,default_,protected_,merged,commit_id,commit_short_id,commit_msg,commit_time,developers_can_push,developers_can_merge
            refOptions: [],
            loadingVisible:true,
            creatingVisible:false,
        };
    }

    componentWillMount(){
        this.loadData();
    }

    loadData(){
        this.setState({
            creatingVisible:false,
            loadingVisible:true
        });
        let url = API.code + "/projects/" + this.state.projectid + "/repository/branches";

        Axios.get(url).then(response=>{

            let branchList=response.data;

            let refOptions=[];
            for(let i=0;i<branchList.length;i++){
                if(branchList[i].protected_==="false") {
                    refOptions.push({
                        value: branchList[i].name,
                        label: branchList[i].name,
                    })
                }
            }
            this.setState({
                branchList: branchList,
                refOptions: refOptions,
                loadingVisible:false,
                ref:"",
                developers_can_push:false,
                developers_can_merge:false,
            })
        })
    }

    changePush(name,mergeChecked,checked){
        this.protectBranch(name,checked,mergeChecked);
    }

    changeMerge(name,pushChecked,checked){
        this.protectBranch(name,pushChecked,checked);
    }

    addProtectedBranch(){
        let {ref,developers_can_push,developers_can_merge}=this.state;
        this.protectBranch(ref,developers_can_push,developers_can_merge);
    }

    protectBranch(branch,developers_can_push,developers_can_merge){
        this.setState({
            creatingVisible:true,
        });

        let url = API.code + "/projects/" + this.state.projectid + "/repository/branches/protect";
        Axios({
            method: 'PUT',
            url: url,
            headers: {
                'Content-type': 'application/x-www-form-urlencoded',
            },
            params:{
                branch:branch,
                developers_can_push:""+developers_can_push,
                developers_can_merge:""+developers_can_merge

            },
        }).then(response=>{
            this.loadData();
        })
    }

    selectRef(value) {
        this.setState({
            ref: value
        })
    }

    selectPush(checked){
        this.setState({
            developers_can_push:checked
        })
    }

    selectMerge(checked){
        this.setState({
            developers_can_merge:checked
        })
    }

    unprotectBranch(branch){
        this.setState({
            creatingVisible:true,
        });

        let url = API.code + "/projects/" + this.state.projectid + "/repository/branches/unprotect";
        Axios({
            method: 'PUT',
            url: url,
            headers: {
                'Content-type': 'application/x-www-form-urlencoded',
            },
            params:{
                branch:branch,

            },
        }).then(response=>{
            this.loadData();
        })
    }


    render(){
        return (
            <div className="protect-branch-container">
                <Loading className="creating-protect-branch" visible={this.state.creatingVisible} tip={spinner}>
                    <div className="div-protect-branch-top">
                        {this.props.intl.messages["code.protectbranch.top"]}
                    </div>
                    <div>
                        <div className="div-protect-branch-input">
                            <span className="text-protect-branch-name">{this.props.intl.messages["code.protectbranch.name"]}</span>
                            <Loading className="loading-protect-branch" visible={this.state.loadingVisible} tip={spinner}>
                                <Select value={this.state.ref} onChange={this.selectRef.bind(this)} className="select-protect-branch" size='large' dataSource={this.state.refOptions} />
                            </Loading>
                        </div>
                        <div className="div-protect-branch-input">
                            <span className="text-protect-branch-name"/>
                            <div className="div-protect-branch-check">
                                <Checkbox checked={this.state.developers_can_push} onChange={this.selectPush.bind(this)}><b>{this.props.intl.messages["code.protectbranch.canpush"]}</b></Checkbox>
                                <Checkbox checked={this.state.developers_can_merge} onChange={this.selectMerge.bind(this)}><b>{this.props.intl.messages["code.protectbranch.canmerge"]}</b></Checkbox>
                            </div>
                        </div>
                        <div className="div-protect-branch-submit">
                            <button onClick={this.addProtectedBranch.bind(this)} className="btn-protect-branch-add">{this.props.intl.messages["code.protectbranch.protect"]}</button>
                        </div>
                    </div>
                    <div className="div-protect-branch-list-top">
                        {this.props.intl.messages["code.protectbranch.list.top"]}
                    </div>
                    <div>
                        <div className="div-protect-branch-list-head">
                            <div>{this.props.intl.messages["code.protectbranch.name"]}</div>
                            <div>{this.props.intl.messages["code.protectbranch.canpush"]}</div>
                            <div>{this.props.intl.messages["code.protectbranch.canmerge"]}</div>
                            <div/>
                        </div>
                        {
                            this.state.branchList.map(item=>{
                                if(item.protected_==="true"){
                                    return (
                                        <div className="div-protect-branch-list-item">
                                            <div>
                                                <b>{item.name}</b>
                                                {
                                                    (()=>{
                                                        if(item.default_==="true"){
                                                            return <label className="label-protect-branch-default">default</label>
                                                        }
                                                    })()
                                                }
                                            </div>
                                            <div><Checkbox defaultChecked={item.developers_can_push} onChange={this.changePush.bind(this,item.name,item.developers_can_merge)}/></div>
                                            <div><Checkbox defaultChecked={item.developers_can_merge} onChange={this.changeMerge.bind(this,item.name,item.developers_can_push)}/></div>
                                            <div><button onClick={this.unprotectBranch.bind(this,item.name)} className="btn-unprotect-branch">{this.props.intl.messages["code.protectbranch.unprotect"]}</button></div>
                                        </div>
                                    )
                                }
                            })
                        }

                    </div>
                </Loading>
            </div>
        )
    }


}


export default injectIntl((props) => <ProjectBranch {...props} key={props.location.pathname}/>)
