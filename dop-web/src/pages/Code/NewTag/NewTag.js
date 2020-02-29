import React from 'react';
import {Select} from "@icedesign/base";
import Axios from 'axios';
import API from "../../API";
import {Loading} from "@icedesign/base";
import {Feedback} from '@icedesign/base';
import Spinner from '../components/Spinner';
import {injectIntl } from 'react-intl';

import './NewTag.css'

const {toast} = Feedback;
const spinner = (
    <Spinner/>
);

class NewTag extends React.Component{

    constructor(props){
        super(props);
        let {username, projectname} = this.props.match.params;
        this.state = {
            username: username,
            projectname: projectname,
            projectid: username + "/" + projectname,
            refOptions: [],
            tag_name: "",
            message:"",
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
        this.props.history.push("/code/" + this.state.projectid + "/tags");
    }

    selectRef(value) {
        this.setState({
            ref: value
        })
    }

    changeTagName(e) {
        this.setState({
            tag_name: e.target.value
        })
    }

    changeMsg(e){
        this.setState({
            message: e.target.value
        })
    }

    addTag(){
        this.setState({
            creatingVisible:true
        });
        let url=API.code+"/projects/"+this.state.projectid+"/repository/tags";
        Axios({
            method:"POST",
            headers:{'Content-type':'application/x-www-form-urlencoded',},
            url:url,
            params:{
                tag_name:this.state.tag_name,
                ref:this.state.ref,
                message:this.state.message,
            },
        }).then(()=>{
            this.props.history.push("/code/"+this.state.projectid+"/tags");
        })
    }

    render(){
        return (
            <div className="new-tag-container">
                <Loading className="creating-new-tag" visible={this.state.creatingVisible} tip={spinner}>
                    <div className="div-new-tag-top">
                        {this.props.intl.messages["code.newtag.top"]}
                    </div>
                    <div>
                        <div className="div-new-tag-input">
                            <span className="text-new-tag-name-msg">{this.props.intl.messages["code.newtag.name"]}</span>
                            <input onChange={this.changeTagName.bind(this)} className="input-new-tag-name-msg"/>
                        </div>
                        <div className="div-new-tag-input">
                            <span className="text-new-tag-name-msg">{this.props.intl.messages["code.newtag.message"]}</span>
                            <input onChange={this.changeMsg.bind(this)} className="input-new-tag-name-msg"/>
                        </div>
                        <div className="div-new-tag-input">
                            <span className="text-new-tag-source">{this.props.intl.messages["code.newtag.source"]}</span>
                            <Loading className="loading-new-tag" visible={this.state.loadingVisible} tip={spinner}>
                                <Select language={this.props.intl.messages["code.language"]} onChange={this.selectRef.bind(this)} className="select-new-tag" size='large' dataSource={this.state.refOptions}/>
                            </Loading>
                        </div>
                    </div>
                    <div className="div-new-tag-submit">
                        <button onClick={this.addTag.bind(this)} className="btn-new-tag-add">{this.props.intl.messages["code.newtag.add"]}</button>
                        <button onClick={this.cancel.bind(this)} className="btn-new-tag-cancel">{this.props.intl.messages["code.newtag.cancel"]}</button>
                    </div>
                </Loading>
            </div>
        )
    }
}

export default injectIntl((props) => <NewTag {...props} key={props.location.pathname}/>)
