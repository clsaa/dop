import React from 'react'
import Axios from 'axios';
import API from "../../API";
import FoundationSymbol from 'foundation-symbol';
import { Loading } from "@icedesign/base";
import Spinner from '../components/Spinner';
import {injectIntl } from 'react-intl';

import './EditFile.css'
import imgBranch from './imgs/branch.png';

const spinner=(
    <Spinner/>
);

class EditFile extends React.Component{

    constructor(props){
        super(props);

        let {username,projectname,path,ref} =this.props.match.params;

        path=decodeURIComponent(path);

        this.state={
            username:username,
            projectname:projectname,
            projectid:username+"/"+projectname,
            path:path,
            ref:ref,
            lineCount:0,
            blobInfo:{},
            commit_message:"",
            loadingVisible:false,
        }
    }

    changeCode(e){
        const val=e.target.value;
        let lineCount=val.split("\n").length;
        let blobInfo=this.state.blobInfo;
        blobInfo.file_content=val;
        this.setState({
            lineCount:lineCount,
            blobInfo:blobInfo
        })
    }

    codeHeight(){
        let lineCount=this.state.lineCount;
        if(lineCount<28){
            lineCount=28;
        }
        return {height:lineCount*20};
    }


    componentWillMount(){
        let {projectid,path,ref}=this.state;

        let self=this;
        let url=API.code+"/projects/"+projectid+"/repository/blob?file_path="+path+"&ref="+ref+"&userId="+sessionStorage.getItem("user-id");
        Axios.get(url).then(response=>{
            self.setState({
                blobInfo:response.data,
                lineCount:response.data.file_content.split("\n").length
            })
        })

    }

    setCommitMessage(e){
        const val=e.target.value;
        this.setState({
            commit_message:val
        })
    }

    commitUpdate(){

        this.setState({
            loadingVisible:true,
        });

        let {projectid,path,ref,commit_message}=this.state;
        Axios({
            method: "PUT",
            url: API.code+ "/projects/"+projectid+"/repository/blob",
            data: {
                userId:sessionStorage.getItem("user-id"),
                file_path:path,
                branch:ref,
                content:this.state.blobInfo.file_content,
                commit_message:commit_message
            },
            headers: {
                'Content-type':'application/json',
            },
        }).then(response => {

            let {username,path,projectname,ref}=this.state;
            this.props.history.push("/code/"+username+"/"+projectname+"/blob/"+ref+"/"+encodeURIComponent(path));

        })
    }


    render(){
        return (
            <Loading className="loading-edit-file" visible={this.state.loadingVisible} tip={spinner}>
                <div className="edit-file-container">
                    <div className="edit-file-section">
                        <div className="div-edit-file-top">
                            <span className="text-edit-file-title">
                                <FoundationSymbol type="publish"/>
                                &nbsp;{this.props.intl.messages["code.editfile.top"]}
                            </span>

                            <img src={imgBranch}/>
                            <span className="text-edit-file-branch">{this.state.ref}</span>
                            <span className="text-edit-file-name">{this.state.blobInfo.file_name}</span>
                        </div>
                        <div className="div-line-number-edit">

                            {
                                (()=> {
                                    let res = [];
                                    for (let i = 1; i <= this.state.lineCount; i++) {
                                        res.push(<span className="text-line-number">{i}</span>)
                                    }
                                    return res;
                                })()
                            }

                        </div>
                        <textarea wrap="off" value={this.state.blobInfo.file_content} style={this.codeHeight()}  className="input-area-code" onChange={this.changeCode.bind(this)} >
                        </textarea>

                    </div>
                    <div className="edit-file-commit-section">
                        <div className="edit-file-commit-lable">{this.props.intl.messages["code.editfile.msg"]}</div>
                        <textarea className="edit-file-commit-input" onChange={this.setCommitMessage.bind(this)}/>
                    </div>
                    <button className="btn-edit-file-commit" onClick={this.commitUpdate.bind(this)}>{this.props.intl.messages["code.editfile.commit"]}</button>
                </div>
            </Loading>
        )
    }
}

export default injectIntl((props)=><EditFile {...props} key={props.location.pathname} />)
