import React from 'react';
import Axios from 'axios';
import API from "../../API";
import { Dialog } from '@icedesign/base';
import {injectIntl,FormattedRelative } from 'react-intl';


import './SSH.css';


class SSH extends React.Component{

    constructor(props){
        super(props);
        this.state={
            keys:[],//id,title,key,created_at
            title:"",
            key:"",
            visible:false,
        }
    }


    componentWillMount(){
        this.loadData();
    }

    loadData(){
        let url=API.code+"/user/keys?userId="+sessionStorage.getItem("user-id");
        Axios.get(url).then(response=>{
            this.setState({
                keys:response.data,
                title:"",
                key:"",
                visible:false,
            })
        })
    }

    deleteKey(id,title){
        if(window.confirm(this.props.intl.messages["code.ssh.delete.confirm"]+title)) {
            let url = API.code + "/user/keys/" + id + "?userId=" + sessionStorage.getItem("user-id");
            Axios.delete(url).then(response => {
                this.loadData();
            })
        }
    }

    newKey(){
        this.props.history.push("/code/ssh/new");
    }


    readmeLink(){
        this.props.history.push("/code/ssh/README");
    }

    onOpen(title,key){
        this.setState({
            title:title,
            key:key,
            visible:true,
        })
    }

    onClose(){
        this.setState({
            visible:false
        })
    }


    render(){
        const footer = (
            <div></div>
        );
        return (
            <div className="ssh-container">
                <div className="div-ssh-top">
                    <span className="text-ssh-intro">{this.props.intl.messages["code.ssh.intro"]}</span>
                    {/*<a onClick={this.readmeLink.bind(this)} className="text-ssh-help">生成密钥。</a>*/}
                    <button onClick={this.newKey.bind(this)} className="btn-ssh-add">+{this.props.intl.messages["code.ssh.new"]}</button>
                </div>
                <table className="table-ssh-list">
                    <thead>
                        <tr className="tr-ssh-head">
                            <th className="th-ssh-title">{this.props.intl.messages["code.ssh.title"]}</th>
                            <th className="th-ssh-time">{this.props.intl.messages["code.ssh.time"]}</th>
                            <th className="th-ssh-operation"> </th>
                        </tr>
                    </thead>
                    <tbody>
                    {
                        (()=>{
                            return this.state.keys.map((item)=>{
                                return (
                                    <tr className="tr-ssh-body">
                                        <td className="td-ssh-title"><a onClick={this.onOpen.bind(this,item.title,item.key)}>{item.title}</a></td>
                                        <td className="td-ssh-time"><FormattedRelative value={new Date(parseInt(item.created_at))}/></td>
                                        <td className="td-ssh-operation"><button onClick={this.deleteKey.bind(this,item.id,item.title)} className="btn-ssh-delete">{this.props.intl.messages["code.ssh.delete"]}</button></td>
                                    </tr>
                                );
                            })
                        })()
                    }

                    </tbody>
                </table>
                <Dialog
                    visible={this.state.visible}
                    onClose={this.onClose.bind(this)}
                    title={this.state.title}
                    footer={footer}
                >
                    <div className="ssh-dialog">
                        {this.state.key}
                    </div>
                </Dialog>
            </div>
        )
    }
}

export default injectIntl(SSH)
