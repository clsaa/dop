import React from 'react'
import { CascaderSelect } from "@icedesign/base";
import Axios from 'axios';
import {Link} from 'react-router-dom';
import './Tree.css'
import API from "../../API";
import { Loading } from "@icedesign/base";
import Spinner from '../components/Spinner';

import imgFile from './imgs/file.png';
import imgFolder from './imgs/folder.png';
import imgSearch from './imgs/search.png';

import {injectIntl,FormattedRelative } from 'react-intl';

const spinner=(
    <Spinner/>
);

class Tree extends React.Component{


    constructor(props) {

        super(props);

        let {username,projectname,path,ref}=this.props.match.params;

        path=decodeURIComponent(path);

        this.state = {
            username:username,
            projectname:projectname,
            projectid:username+"/"+projectname,
            ref: ref,
            refOptions: [],
            path: path,
            treeNodeInfo: [],//name,type,path
            treeCommitInfo:[],//commit_id,commit_msg,commit_date,commit_time
            loadingVisible:true,
        }

    }

    componentWillMount() {
        this.loadData();
    }


    loadData(){

        let {projectid,path,ref}=this.state;

        let url=API.code + "/projects/"+projectid+"/repository/tree/nodes?path="+path+"&ref="+ref+"&userId="+sessionStorage.getItem("user-id");
        let self=this;
        Axios.get(url).then(response => {
            self.setState({
                treeNodeInfo:response.data
            })
        });

        url=API.code+"/projects/"+projectid+"/repository/branchandtag?userId="+sessionStorage.getItem("user-id");
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
                loadingVisible:false
            })
        });

        url=API.code + "/projects/"+projectid+"/repository/tree/commits?path="+path+"&ref="+ref+"&userId="+sessionStorage.getItem("user-id");
        Axios.get(url).then(response => {
            self.setState({
                treeCommitInfo:response.data
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


    changeRef(value, data, extra) {

        let {username,projectname}=this.state;
        let path="/";//切换ref将路径设为根目录，以免子目录不存在
        this.props.history.push("/code/"+username+"/"+projectname+"/tree/"+value+"/"+encodeURIComponent(path));

    }

    changePath(path){

        let {username,projectname,ref}=this.state;

        this.props.history.push("/code/"+username+"/"+projectname+"/tree/"+ref+"/"+encodeURIComponent(path));

    }

    readFile(path){

        let {username,projectname,ref}=this.state;

        this.props.history.push("/code/"+username+"/"+projectname+"/blob/"+ref+"/"+encodeURIComponent(path));

    }


    findFile(){

        let {username,projectname,ref}=this.state;

        this.props.history.push("/code/"+username+"/"+projectname+"/filepathlist/"+ref);

    }


    commitLink(sha){
        this.props.history.push("/code/"+this.state.projectid+"/commit/"+sha);
    }


    render(){

        return (
            <div className="file-container">
                <div className="div-tree-top">
                    <Loading visible={this.state.loadingVisible} className="loading-ref-tree" tip={spinner}>
                        <CascaderSelect className="select-ref-tree"  size='large' value={this.state.ref} dataSource={this.state.refOptions} onChange={this.changeRef.bind(this)}/>
                    </Loading>
                    {
                        (()=>{
                            let path=this.state.path;
                            if(path!=="/") {
                                let strs = path.split("/");
                                let paths = [];

                                paths.push(<a onClick={this.changePath.bind(this,"/")}>{this.props.intl.messages["code.tree.root"]}</a>);

                                let temp_str = "";
                                for (let i = 0; i < strs.length; i++) {
                                    if (i !== 0) {
                                        temp_str += "/";
                                    }
                                    temp_str += strs[i];
                                    paths.push(<span className="text-tree-separator">&nbsp;&nbsp;&nbsp;/&nbsp;&nbsp;&nbsp;</span>);
                                    paths.push(<a onClick={this.changePath.bind(this,temp_str)}>{strs[i]}</a>);
                                }

                                return paths;
                            }else {
                                return (<a onClick={this.changePath.bind(this,"/")}>{this.props.intl.messages["code.tree.root"]}</a>);
                            }
                        })()
                    }
                    <button className="btn-search-file" onClick={this.findFile.bind(this)}>
                        <img src={imgSearch} className="img-tree-search"/>
                        <span className="text-tree-search">{this.props.intl.messages["code.tree.findfile"]}</span>
                    </button>
                </div>


                <div className="div-tree-title">
                    <span style={{marginRight:18}} className="text-tree-name-title">{this.props.intl.messages["code.tree.name"]}</span>
                    <span className="text-tree-commit-title">{this.props.intl.messages["code.tree.lastcommit"]}</span>
                    <span className="text-tree-update-title">{this.props.intl.messages["code.tree.lastupdate"]}</span>
                </div>



                {
                    (()=>{
                        //若不是根目录可以返回上一级目录
                        if(this.state.path!=="/"){

                            let path;
                            //一级目录,退回根目录
                            if(this.state.path.indexOf("/")===-1){
                                path="/";
                            }else {//多级目录，截取掉最后一级的目录
                                path=this.state.path.substring(0,this.state.path.lastIndexOf("/"));
                            }



                            return (
                                <div className="div-tree-item">
                                    <span className="text-tree-name">
                                        <a onClick={this.changePath.bind(this,path)}>..</a>
                                    </span>
                                    <span className="text-tree-commit"/>
                                    <span className="text-tree-update"/>
                                </div>
                            )
                        }
                    })()
                }
                {
                    (()=> {

                        const {treeNodeInfo,treeCommitInfo}=this.state;

                        return treeNodeInfo.map((treeNode,index)=>{

                                return (
                                    <div className="div-tree-item">
                                        {
                                            (()=>{
                                                if(treeNode.type==="tree")
                                                    return <img src={imgFolder} className="img-tree-folder"/>;
                                                else
                                                    return <img src={imgFile} className="img-tree-file"/>;
                                            })()
                                        }
                                        <span className="text-tree-name">
                                            <a onClick={
                                                (()=>{
                                                    if(treeNode.type==="tree")
                                                        return this.changePath.bind(this,treeNode.path);
                                                    else
                                                        return this.readFile.bind(this,treeNode.path);
                                                })()
                                            }>{treeNode.name}</a>
                                        </span>

                                        {/*<span className="text-tree-commit">*/}
                                            {/*{*/}
                                                {/*(()=>{*/}
                                                    {/*if(treeCommitInfo.length===0){*/}
                                                        {/*return "";*/}
                                                    {/*}else {*/}
                                                        {/*return treeCommitInfo[index].commit_msg;*/}
                                                    {/*}*/}
                                                {/*})()*/}
                                            {/*}*/}
                                        {/*</span>*/}


                                        {
                                            (()=>{
                                                if(treeCommitInfo.length===0){
                                                    return <span className="text-tree-commit"/>;
                                                }else {
                                                    return <a onClick={this.commitLink.bind(this,treeCommitInfo[index].commit_id)} className="text-tree-commit">{treeCommitInfo[index].commit_msg}</a> ;
                                                }
                                            })()
                                        }


                                        {
                                            (()=>{
                                                if(treeCommitInfo.length===0){
                                                    if(index===0){
                                                        return [
                                                            <Loading className="loading-tree-update" tip={spinner}></Loading>,
                                                            <span className="text-tree-update-loading">{this.props.intl.messages["code.tree.loadingcommit"]}</span>
                                                        ];
                                                    }else {
                                                        return <span className="text-tree-update"></span>;
                                                    }
                                                }else {
                                                    // return <span className="text-tree-update">{treeCommitInfo[index].commit_time}</span>;
                                                    return <span className="text-tree-update"><FormattedRelative value={new Date(parseInt(treeCommitInfo[index].commit_time))}/></span>
                                                }
                                            })()
                                        }

                                    </div>
                                )
                        })
                    })()
                }


            </div>
        );
    }
}

export default injectIntl((props)=><Tree {...props} key={props.location.pathname} />)
