import React from 'react'
import { CascaderSelect } from "@icedesign/base";
import Axios from 'axios';
import API from "../../API";
import copy from 'copy-to-clipboard';
import {Feedback} from '@icedesign/base';
import { Balloon } from "@icedesign/base";
import ReactTooltip from 'react-tooltip'
import { Loading } from "@icedesign/base";
import Spinner from '../components/Spinner';
import {injectIntl,FormattedRelative } from 'react-intl';

import imgCopy from './imgs/copy.png';
import imgFolder from './imgs/folder.png';


import "./CommitList.css"

const {toast} = Feedback;

const spinner=(
    <Spinner/>
);

class CommitList extends React.Component{

    constructor(props){
        super(props);
        const {projectname,ref,username}=this.props.match.params;
        this.state={
            username:username,
            projectname:projectname,
            projectid:username+"/"+projectname,
            ref:ref,
            refOptions: [],
            commitList:[],
            showList:[],
            msgInput:"",
            loadingVisible:true,
        }
    }

    componentWillMount() {

        let {projectid,ref} = this.state;
        this.loadData(projectid,ref);

    }

    isCommit(ref,refOptions){

        let branches=refOptions[0].children;
        let tags=refOptions[1].children;

        for(let i=0;i<branches.length;i++){
            if(branches[i].value===ref)
                return false;
        }

        for(let i=0;i<tags.length;i++){
            if(tags[i].value===ref)
                return false;
        }

        return true;

    }

    loadData(projectid,ref){

        let url=API.code+"/projects/"+projectid+"/repository/branchandtag?userId="+sessionStorage.getItem("user-id");
        let self=this;
        Axios.get(url).then(response=>{

            let refOptions=response.data;

            if(this.isCommit(ref,refOptions)){
                refOptions.push(
                    {
                        value:"commit",
                        label:"commit",
                        children:[
                            {
                                value:ref,
                                label:ref
                            }
                        ]
                    }
                )
            }
            self.setState({
                refOptions:response.data,
                loadingVisible:false,
            })
        });

        url=API.code+"/projects/"+projectid+"/repository/commits?path=/&ref_name="+ref+"&userId="+sessionStorage.getItem("user-id");
        Axios.get(url).then(response=>{
            self.setState({
                commitList:response.data,
                showList:response.data
            })
        });

    }

    handleInputChange(e){
        const val=e.target.value;
        if(val!==""){
            const showList=this.state.commitList.filter(function (item) {
                return item.message.indexOf(val)!==-1;
            });
            this.setState({
                msgInput:val,
                showList:showList
            })
        }else {
            this.setState({
                msgInput:val,
                showList:this.state.commitList
            })
        }
    }

    changeRef(value, data, extra) {

        let {username,projectname}=this.state;
        this.props.history.push("/code/"+username+"/"+projectname+"/commitlist/"+value);

    }

    isSameDate(d1,d2){
        return d1.getFullYear()===d2.getFullYear()&&d1.getMonth()===d2.getMonth()&&d1.getDate()===d2.getDate();
    }


    copySha(sha){
        copy(sha);
        toast.success(this.props.intl.messages["code.commitlist.copied"])
    }

    browseFile(commit_id){

        let {username,projectname}=this.state;

        this.props.history.push("/code/"+username+"/"+projectname+"/tree/"+commit_id+"/"+encodeURIComponent("/"));

    }

    rootLink(){
        let {username,projectname,ref}=this.state;
        this.props.history.push("/code/"+username+"/"+projectname+"/tree/"+ref+"/"+encodeURIComponent("/"));
    }

    commitLink(sha){
        let {username,projectname}=this.state;
        this.props.history.push("/code/"+username+"/"+projectname+"/commit/"+encodeURIComponent(sha));
    }


    render(){
        return (
            <div className="commit-list-container">
                <div className="div-commit-list-top">
                    <Loading visible={this.state.loadingVisible} className="loading-ref-commit-list" tip={spinner}>
                        <CascaderSelect className="select-ref-commit-list"  size='large' value={this.state.ref} dataSource={this.state.refOptions} onChange={this.changeRef.bind(this)}/>
                    </Loading>
                    <a className="link-commit-root" onClick={this.rootLink.bind(this)}>{this.props.intl.messages["code.commitlist.root"]}</a>
                    <input value={this.state.msgInput} onChange={this.handleInputChange.bind(this)} className="input-commit-msg-right" placeholder={this.props.intl.messages["code.commitlist.placeholder"]}/>
                </div>
                {
                    (()=>{
                        let res=[];
                        let commitList=this.state.showList;
                        let count;
                        let d1;
                        let d2;


                        for(let i=0;i<commitList.length;i++){
                            let commit=commitList[i];
                            if(i===0){
                                count=1;
                                d1=new Date(commit.authored_date);
                            }else {
                                d2=new Date(commit.authored_date);
                                if(this.isSameDate(d1,d2)){
                                    count++;
                                }else {
                                    res.push(<div className="div-commit-date">{d1.getFullYear()+"-"+(d1.getMonth()+1)+"-"+d1.getDate()+ " " + count + this.props.intl.messages["code.commitlist.count"]}</div>);
                                    for(let j=i-count;j<i;j++){
                                        res.push(
                                            <div className="div-commit-item">
                                                <div className="div-commit-item-avatar">{commitList[j].author_name.substring(0,1).toUpperCase()}</div>
                                                <div className="div-commit-item-content">
                                                    <div className="div-commit-item-content-up"><a onClick={this.commitLink.bind(this,commitList[j].id)}>{commitList[j].message}</a></div>
                                                    <div className="div-commit-item-content-down">
                                                        {commitList[j].author_name+this.props.intl.messages["code.commitlist.committedat"]}
                                                        <FormattedRelative value={new Date(parseInt(commitList[j].authored_time))}/>
                                                    </div>
                                                </div>
                                                <div className="div-commit-operation">
                                                    <span className="text-commit-short-id">{commitList[j].short_id}</span>
                                                    <button data-tip data-for='copy' className="btn-commit-copy-sha" onClick={this.copySha.bind(this,commitList[j].id)}>
                                                        <img src={imgCopy} className="img-commit-copy-sha"/>
                                                    </button>
                                                    <button data-tip data-for='browse' className="btn-commit-browse-file" onClick={this.browseFile.bind(this,commitList[j].id)}>
                                                        <img src={imgFolder} className="img-commit-browse-file"/>
                                                    </button>
                                                    <ReactTooltip id='copy' place="bottom" type='dark' effect='solid'>
                                                        <span>{this.props.intl.messages["code.commitlist.tip.copy"]}</span>
                                                    </ReactTooltip>
                                                    <ReactTooltip id='browse' place="bottom" type='dark' effect='solid'>
                                                        <span>{this.props.intl.messages["code.commitlist.tip.browse"]}</span>
                                                    </ReactTooltip>
                                                </div>
                                            </div>
                                        )
                                    }
                                    d1=d2;
                                    count=1;
                                }
                            }
                        }

                        if(commitList.length!==0) {
                            res.push(<div className="div-commit-date">{d1.getFullYear()+"-"+(d1.getMonth()+1)+"-"+d1.getDate()+ " " + count + this.props.intl.messages["code.commitlist.count"]}</div>);
                            for (let i = commitList.length - count; i < commitList.length; i++) {
                                res.push(
                                    <div className="div-commit-item">
                                        <div className="div-commit-item-avatar">{commitList[i].author_name.substring(0,1).toUpperCase()}</div>
                                        <div className="div-commit-item-content">
                                            <div className="div-commit-item-content-up"><a onClick={this.commitLink.bind(this,commitList[i].id)}>{commitList[i].message}</a></div>
                                            <div className="div-commit-item-content-down">
                                                {commitList[i].author_name+this.props.intl.messages["code.commitlist.committedat"]}
                                                <FormattedRelative value={new Date(parseInt(commitList[i].authored_time))}/>
                                            </div>
                                        </div>
                                        <div className="div-commit-operation">
                                            <span className="text-commit-short-id">{commitList[i].short_id}</span>
                                            <button data-tip data-for='copy' className="btn-commit-copy-sha" onClick={this.copySha.bind(this,commitList[i].id)}>
                                                <img src={imgCopy} className="img-commit-copy-sha"/>
                                            </button>
                                            <button data-tip data-for='browse' className="btn-commit-browse-file" onClick={this.browseFile.bind(this,commitList[i].id)}>
                                                <img src={imgFolder} className="img-commit-browse-file"/>
                                            </button>
                                            <ReactTooltip id='copy' place="bottom" type='dark' effect='solid'>
                                                <span>{this.props.intl.messages["code.commitlist.tip.copy"]}</span>
                                            </ReactTooltip>
                                            <ReactTooltip id='browse' place="bottom" type='dark' effect='solid'>
                                                <span>{this.props.intl.messages["code.commitlist.tip.browse"]}</span>
                                            </ReactTooltip>
                                        </div>
                                    </div>
                                )
                            }
                        }


                        return res;

                    })()
                }


            </div>
        )
    }


}

export default injectIntl((props)=><CommitList {...props} key={props.location.pathname} />)
