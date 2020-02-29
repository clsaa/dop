import React from 'react';
import API from "../../API";
import Axios from 'axios';
import {Select} from "@icedesign/base";
import { Loading } from "@icedesign/base";
import Spinner from '../components/Spinner';
import {injectIntl } from 'react-intl';

import './NewMergeRequest.css';

const spinner=(
    <Spinner/>
);

class NewMergeRequest extends React.Component{

    constructor(props) {
        super(props);
        const {projectname, username} = this.props.match.params;
        const search=this.props.location.search;
        let source_branch="";
        let target_branch="";
        if(search.length>0){
            source_branch=search.split("&")[0].split("=")[1];
            target_branch=search.split("&")[1].split("=")[1];
        }
        this.state = {
            username: username,
            projectname: projectname,
            projectid: username + "/" + projectname,
            branches:[],
            title:"",
            description:"",
            source_branch:source_branch,
            target_branch:target_branch,
            loadingVisible:false,
        }
    }

    componentWillMount(){
        let url = API.code+"/projects/"+this.state.projectid+"/branches";
        Axios.get(url).then(response=>{
            this.setState({
                branches:response.data
            })
        });
    }

    changeTitle(e){
        this.setState({
            title:e.target.value
        })
    }

    changeDescription(e){
        this.setState({
            description:e.target.value
        })
    }

    selectSource(value){
        this.setState({
            source_branch:value
        })
    }

    selectTarget(value){
        this.setState({
            target_branch:value
        })
    }

    cancelLink(){
        this.props.history.push("/code/"+this.state.projectid+"/merge_requests?state=opened");
    }

    addMergeRequest(){
        this.setState({
            loadingVisible:true
        });
        let url=API.code+"/projects/"+this.state.projectid+"/merge_requests";
        let {source_branch,target_branch,title,description}=this.state;
        Axios({
            method: 'POST',
            url: url,
            headers: {
                'Content-type': 'application/x-www-form-urlencoded',
            },
            params:{
                source_branch:source_branch,
                target_branch:target_branch,
                title:title,
                description:description,
            },
        }).then(response=>{
            this.props.history.push("/code/"+this.state.projectid+"/merge_requests?state=opened");
        })
    }

    render(){
        return (
            <div className="new-mr-container">
                <Loading visible={this.state.loadingVisible} className="loading-new-mr" tip={spinner}>
                    <div className="div-new-mr-top">
                        {this.props.intl.messages["code.newmergerequest.top"]}
                    </div>
                    <div>
                        <div className="div-new-mr-input">
                            <span className="text-new-mr-title">{this.props.intl.messages["code.newmergerequest.title"]}</span>
                            <input onChange={this.changeTitle.bind(this)} className="input-new-mr-title"/>
                        </div>
                        <div className="div-new-mr-input">
                            <span className="text-new-mr-description">{this.props.intl.messages["code.newmergerequest.description"]}</span>
                            <textarea onChange={this.changeDescription.bind(this)} className="input-new-mr-description"/>
                        </div>
                        <div className="div-new-mr-input">
                            <span className="text-new-mr-branch">{this.props.intl.messages["code.newmergerequest.source"]}</span>
                            <Select value={this.state.source_branch} onChange={this.selectSource.bind(this)} className="select-new-mr-branch" dataSource={this.state.branches}/>
                        </div>
                        <div className="div-new-mr-input">
                            <span className="text-new-mr-branch">{this.props.intl.messages["code.newmergerequest.target"]}</span>
                            <Select value={this.state.target_branch} onChange={this.selectTarget.bind(this)} className="select-new-mr-branch" dataSource={this.state.branches}/>
                        </div>
                    </div>
                    <div className="div-new-mr-submit">
                        <button className="btn-new-mr-add" onClick={this.addMergeRequest.bind(this)}>{this.props.intl.messages["code.newmergerequest.add"]}</button>
                        <button className="btn-new-mr-cancel" onClick={this.cancelLink.bind(this)}>{this.props.intl.messages["code.newmergerequest.cancel"]}</button>
                    </div>
                </Loading>
            </div>
        );
    }
}

export default injectIntl((props)=><NewMergeRequest {...props} key={props.location.pathname+props.location.search} />)

