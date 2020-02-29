import React from 'react';
import { CascaderSelect } from "@icedesign/base";
import Axios from 'axios';
import API from "../../API";
import { Loading } from "@icedesign/base";
import Spinner from '../components/Spinner';
import {injectIntl } from 'react-intl';

import './FilePathList.css'

import imgFile from './imgs/file.png';

const spinner=(
    <Spinner/>
);

class FilePathList extends React.Component{

    constructor(props){
        super(props);

        let {username,projectname,ref}=this.props.match.params;

        this.state={
            username:username,
            projectname:projectname,
            projectid:username+"/"+projectname,
            ref:ref,
            refOptions:[],
            pathList:[],
            showData:[],
            path:"",
            loadingVisible:true,
        }

    }

    componentWillMount() {

        let{projectid,ref}=this.state;

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

        url=API.code+"/projects/"+projectid+"/repository/filepathlist?ref="+ref+"&userId="+sessionStorage.getItem("user-id");
        Axios.get(url).then(response=>{
            self.setState({
                pathList:response.data,
                showData:response.data
            })
        });

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

    changeRef(value, data, extra){

        let{projectid}=this.state;

        let url=API.code+"/projects/"+projectid+"/repository/filepathlist?ref="+value+"&userId="+sessionStorage.getItem("user-id");
        let self=this;
        Axios.get(url).then(response=>{
            self.setState({
                ref:value,
                path:"",
                pathList:response.data,
                showData:response.data
            })
        });
    }

    changePath(e){

        let path=e.target.value;
        let showData=this.state.pathList.filter((item)=>{
            return item.indexOf(path)!==-1;
        });
        this.setState({
            path:path,
            showData:showData
        });

    }

    blobLink(path){

        let {username,projectname,ref}=this.state;

        this.props.history.push("/code/"+username+"/"+projectname+"/blob/"+ref+"/"+encodeURIComponent(path))

    }

    treeRootLink(){

        let {username,projectname,ref}=this.state;

        this.props.history.push("/code/"+username+"/"+projectname+"/tree/"+ref+"/"+encodeURIComponent("/"));

    }

    render(){
        return (
            <div className="file-list-container">
                <div className="div-file-list-top">
                    <Loading visible={this.state.loadingVisible} className="loading-ref-file-list" tip={spinner}>
                        <CascaderSelect className="select-ref-file-list"  size='large' value={this.state.ref} dataSource={this.state.refOptions} onChange={this.changeRef.bind(this)}/>
                    </Loading>
                    <a onClick={this.treeRootLink.bind(this)}>{this.props.intl.messages["code.filepathlist.root"]}</a>
                    <span className="text-file-list-separator">/</span>
                    <input className="input-file-list-search" placeholder={this.props.intl.messages["code.filepathlist.placeholder"]} value={this.state.path} onChange={this.changePath.bind(this)}/>
                </div>
                <div>
                    {
                        this.state.showData.map(item=> {
                                return (
                                    <div className="div-file-list-item">
                                        <img src={imgFile}/>
                                        <a onClick={this.blobLink.bind(this,item)}>{item}</a>
                                    </div>
                                )
                         })
                    }
                </div>
            </div>
        )
    }
}

export default injectIntl((props)=><FilePathList {...props} key={props.location.pathname} />)
