import React from 'react'
import Axios from 'axios';
import API from "../../API";
import {Feedback} from '@icedesign/base';
import {injectIntl} from 'react-intl';


import './NewSSH.css'

const {toast} = Feedback;

class NewSSH extends React.Component{

    constructor(props){
        super(props);
        this.state={
            key:"",
            title:"",
        };
    }

    changeKey(e){
        let val=e.target.value;
        let strs=val.split(" ");
        if(strs.length===3){
            this.setState({
                key:val,
                title:strs[2]
            })
        }else {
            this.setState({
                key:val
            })
        }

    }

    changeTitle(e){
        let val=e.target.value;
        this.setState({
            title:val
        })
    }

    addKey(){
        let url=API.code+"/user/keys";
        Axios({
            method:"POST",
            headers:{'Content-type':'application/json',},
            url:url,
            data:{
                key:this.state.key,
                title:this.state.title,
                userId:sessionStorage.getItem("user-id")
            },
        }).then(response=>{
            this.props.history.push("/code/ssh");
        })
    }

    cancel(){
        this.props.history.push("/code/ssh");
    }

    render(){
        return (
            <div className="new-ssh-container">
                <div className="div-new-ssh-top">
                    {this.props.intl.messages["code.newssh.top"]}
                </div>
                <div>
                    <div className="div-new-ssh-input">
                        <span className="text-new-ssh-key">{this.props.intl.messages["code.newssh.key"]}</span>
                        <textarea onChange={this.changeKey.bind(this)} value={this.state.key} className="input-new-ssh-key"/>
                    </div>
                    <div className="div-new-ssh-input">
                        <span className="text-new-ssh-title">{this.props.intl.messages["code.newssh.title"]}</span>
                        <input onChange={this.changeTitle.bind(this)} value={this.state.title} className="input-new-ssh-title"/>
                    </div>
                </div>
                <div className="div-new-ssh-submit">
                    <button onClick={this.addKey.bind(this)} className="btn-new-ssh-add">{this.props.intl.messages["code.newssh.add"]}</button>
                    <button onClick={this.cancel.bind(this)} className="btn-new-ssh-cancel">{this.props.intl.messages["code.newssh.cancel"]}</button>
                </div>
            </div>
        )
    }

}

export default injectIntl(NewSSH)
