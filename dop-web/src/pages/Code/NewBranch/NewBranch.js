import React from 'react';
import {Select} from "@icedesign/base";
import Axios from 'axios';
import API from "../../API";
import {Loading} from "@icedesign/base";
import {Feedback} from '@icedesign/base';
import Spinner from '../components/Spinner';
import {injectIntl } from 'react-intl';

import './NewBranch.css'

const {toast} = Feedback;
const spinner = (
    <Spinner/>
);

class NewBranch extends React.Component {

    constructor(props) {
        super(props);
        let {username, projectname} = this.props.match.params;
        this.state = {
            username: username,
            projectname: projectname,
            projectid: username + "/" + projectname,
            refOptions: [],
            branch: "",
            ref: "",
            loadingVisible: true,
            creatingVisible:false,
        };
    }

    componentWillMount() {
        let url = API.code + "/projects/" + this.state.projectid + "/repository/branchandtag";
        Axios.get(url).then(response => {
            this.setState({
                refOptions: response.data,
                loadingVisible: false
            })
        })
    }

    cancel() {
        this.props.history.push("/code/" + this.state.projectid + "/branches");
    }

    selectRef(value) {
        this.setState({
            ref: value
        })
    }

    changeBranchName(e) {
        this.setState({
            branch: e.target.value
        })
    }

    addBranch() {
        this.setState({
            creatingVisible:true
        });
        let url=API.code+"/projects/"+this.state.projectid+"/repository/branches";
        Axios({
            method:"POST",
            headers:{'Content-type':'application/x-www-form-urlencoded',},
            url:url,
            params:{
                branch:this.state.branch,
                ref:this.state.ref
            },
        }).then(()=>{
            this.props.history.push("/code/"+this.state.projectid+"/branches");
        })
    }


    render() {
        return (
            <div className="new-branch-container">
                <Loading className="creating-new-branch" visible={this.state.creatingVisible} tip={spinner}>
                    <div className="div-new-branch-top">
                        {this.props.intl.messages["code.newbranch.top"]}
                    </div>
                    <div>
                        <div className="div-new-branch-input">
                            <span className="text-new-branch-name">{this.props.intl.messages["code.newbranch.name"]}</span>
                            <input onChange={this.changeBranchName.bind(this)} className="input-new-branch-name"/>
                        </div>
                        <div className="div-new-branch-input">
                            <span className="text-new-branch-source">{this.props.intl.messages["code.newbranch.source"]}</span>
                            <Loading className="loading-new-branch" visible={this.state.loadingVisible} tip={spinner}>
                                <Select language={this.props.intl.messages["code.language"]} onChange={this.selectRef.bind(this)} className="select-new-branch" size='large' dataSource={this.state.refOptions}/>
                            </Loading>
                        </div>
                    </div>
                    <div className="div-new-branch-submit">
                        <button onClick={this.addBranch.bind(this)} className="btn-new-branch-add">{this.props.intl.messages["code.newbranch.add"]}</button>
                        <button onClick={this.cancel.bind(this)} className="btn-new-branch-cancel">{this.props.intl.messages["code.newbranch.cancel"]}</button>
                    </div>
                </Loading>
            </div>
        )
    }
}

export default injectIntl((props) => <NewBranch {...props} key={props.location.pathname}/>)
