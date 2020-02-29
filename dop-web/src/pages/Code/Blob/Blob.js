import React from 'react'
import { CascaderSelect } from "@icedesign/base";
import Axios from 'axios';
import API from "../../API";
import { Dialog } from '@icedesign/base';
import { Loading } from "@icedesign/base";
import Spinner from '../components/Spinner';
import {injectIntl } from 'react-intl';

import './Blob.css'

import imgSearch from './imgs/search.png';
import imgFile from './imgs/file.png';

const spinner=(
    <Spinner/>
);

class Blob extends React.Component{


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
            refOptions:[],
            lineCount:0,
            blobInfo:{},//file_name,file_size,file_content
            deleteVisible:false,
            commit_message:"",
            loadingVisible:true,
            accessInfo:{
                access_level:0,
                visibility:"private",
            },
        }
    }

    openDelete(){
        this.setState({
            deleteVisible:true
        });
    }

    closeDelete(){
        this.setState({
            deleteVisible:false
        });
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
        if(lineCount<24){
            lineCount=24;
        }
        return {height:lineCount*20};
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
        let {projectid,path,ref}=this.state;

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
                refOptions:refOptions,
                loadingVisible:false,
            })
        });

        url=API.code+"/projects/"+projectid+"/repository/blob?file_path="+path+"&ref="+ref+"&userId="+sessionStorage.getItem("user-id");
        Axios.get(url).then(response=>{
            self.setState({
                blobInfo:response.data,
                lineCount:response.data.file_content.split("\n").length
            })
        });

        this.loadAccess();

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

    hasAccess(){
        let {ref,refOptions,accessInfo}=this.state;

        if(refOptions.length===0)
            return false;

        let branches=refOptions[0].children;
        for(let i=0;i<branches.length;i++){
            if(branches[i].value===ref&&!branches[i].default_&&!branches[i].protected_&&accessInfo.access_level>=30)
                return true;
        }

        return false;


    }



    changeRef(value, data, extra){

        let {username,projectname,path}=this.state;

        this.props.history.push("/code/"+username+"/"+projectname+"/blob/"+value+"/"+encodeURIComponent(path));

    }

    changePath(path){

        let {username,projectname,ref}=this.state;

        this.props.history.push("/code/"+username+"/"+projectname+"/tree/"+ref+"/"+encodeURIComponent(path));

    }

    editFile(){

        let {username,path,projectname,ref}=this.state;

        this.props.history.push("/code/"+username+"/"+projectname+"/edit/"+ref+"/"+encodeURIComponent(path));

    }

    changeCommitMsg(e){
        this.setState({
            commit_message:e.target.value
        });
    }

    deleteFile(){

        let {projectid,path,ref,commit_message,username,projectname} = this.state;
        let url=API.code+"/projects/"+projectid+"/repository/blob?file_path="+path+"&branch="+ref+"&commit_message="+commit_message+"&userId="+sessionStorage.getItem("user-id");
        console.log(url);
        Axios.delete(url).then(response=>{

            let strs=path.split("/");
            if(strs.length===1){
                path="/"
            }else {
                path="";
                for(let i=0;i<strs.length-1;i++){
                    if(i!==0) {
                        path += "/";
                    }
                    path+=strs[i];
                }
            }

            this.props.history.push("/code/"+username+"/"+projectname+"/tree/"+ref+"/"+encodeURIComponent(path));

        })
    }

    findFile(){

        let {username,projectname,ref}=this.state;

        this.props.history.push("/code/"+username+"/"+projectname+"/filepathlist/"+ref);

    }

    render(){

        return (
            <div className="blob-container">
                <div className="div-blob-top">
                    <Loading visible={this.state.loadingVisible} className="loading-ref-blob" tip={spinner}>
                        <CascaderSelect className="select-ref-blob"  size='large' value={this.state.ref} dataSource={this.state.refOptions} onChange={this.changeRef.bind(this)}/>
                    </Loading>
                    {
                        (()=>{
                            let path=this.state.path;
                            let strs = path.split("/");
                            let paths = [];

                            paths.push(<a onClick={this.changePath.bind(this,"/")}>{this.props.intl.messages["code.blob.root"]}</a>);

                            let temp_str = "";
                            for (let i = 0; i < strs.length; i++) {
                                if (i !== 0) {
                                    temp_str += "/";
                                }
                                temp_str += strs[i];
                                paths.push(<span className="text-blob-separator">&nbsp;&nbsp;&nbsp;/&nbsp;&nbsp;&nbsp;</span>);
                                if(i!==strs.length-1)
                                    paths.push(<a onClick={this.changePath.bind(this,temp_str)}>{strs[i]}</a>);
                                else
                                    paths.push(<a>{strs[i]}</a>)
                            }

                            return paths;
                        })()
                    }
                    <button className="btn-search-file" onClick={this.findFile.bind(this)}>
                        <img src={imgSearch} className="img-blob-search"/>
                        <span className="text-blob-search">{this.props.intl.messages["code.blob.findfile"]}</span>
                    </button>
                </div>
                <div className="div-blob-title">
                    <img src={imgFile}/>
                    <span className="text-file-name">{this.state.blobInfo.file_name}</span>
                    <span className="text-file-size">&nbsp;&nbsp;{this.state.blobInfo.file_size}</span>

                    {
                        (()=>{
                            if(this.hasAccess()){
                                return (
                                    <div className="div-blob-operation">
                                        <button className="btn-blob-edit" onClick={this.editFile.bind(this)}>{this.props.intl.messages["code.blob.edit"]}</button>
                                        <button className="btn-blob-delete" onClick={this.openDelete.bind(this)}>{this.props.intl.messages["code.blob.delete"]}</button>
                                    </div>
                                )
                            }else {
                                return (
                                    <div className="div-blob-operation">
                                        <button className="btn-blob-disabled" onClick={this.editFile.bind(this)} disabled="disabled">{this.props.intl.messages["code.blob.edit"]}</button>
                                        <button className="btn-blob-disabled" onClick={this.openDelete.bind(this)} disabled="disabled">{this.props.intl.messages["code.blob.delete"]}</button>
                                    </div>
                                )
                            }
                        })()
                    }

                </div>
                 <div className="div-line-number">

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
                <textarea wrap="off" value={this.state.blobInfo.file_content} style={this.codeHeight()}  className="input-area-code" onChange={this.changeCode.bind(this)} readOnly>
                </textarea>

                <Dialog
                    visible={this.state.deleteVisible}
                    onOk={this.deleteFile.bind(this)}
                    onCancel={this.closeDelete.bind(this)}
                    onClose={this.closeDelete.bind(this)}
                    title={this.props.intl.messages["code.blob.delete"]+this.state.blobInfo.file_name}
                    footerAlign="left"
                    language={this.props.intl.messages["code.blob.language"]}
                >
                    <div className="div-blob-delete-title">{this.props.intl.messages["code.blob.delete.title"]}</div>
                    <textarea value={this.state.commit_message} className="input-area-code-commit" onChange={this.changeCommitMsg.bind(this)} />
                </Dialog>

            </div>
        );
    }

}

export default injectIntl((props)=><Blob {...props} key={props.location.pathname} />)
