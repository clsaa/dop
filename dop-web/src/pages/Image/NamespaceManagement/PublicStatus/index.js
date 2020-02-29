import React,{Component} from 'react';
import {Switch,Feedback} from '@icedesign/base'
import API from "../../../API";
import Axios from "axios/index";
import {injectIntl} from 'react-intl'
const Toast = Feedback.toast;
class PublicStatus extends Component{
    constructor(props){
        super(props);
        this.state={
            record:this.props.record,
            value:this.props.value,
            refreshList:this.props.refreshList
        }

    }
    componentWillReceiveProps(nextProps, nextContext) {
        this.setState({
            record: nextProps.record,
            value:nextProps.value,
            refreshList:nextProps.refreshList
        });
    }

    onChange(checked){
        let _this = this;
        let namespaceId = this.state.record.projectId;
        let url = API.image+"/v1/projects/"+namespaceId+"/metadatas/public";
        //修改命名空间状态
        let temp = "";
        if (checked){
            temp = "false";
        }else {
            temp = "true";
        }
        Axios.put(url,{}, {
            params:{
                "publicStatus":temp
            }
        }).then(function (response) {
            Toast.success(_this.props.intl.messages["image.publicStatus.success"]);
            _this.state.refreshList(1,"");
        }).catch(function (error) {
            console.log(error);
            Toast.error(_this.props.intl.messages["image.publicStatus.failed"]);
            _this.state.refreshList(1,"");
        });

    }
    render(){
        if (this.state.record.currentUserRole==="Namespace Manager"){
            if (this.state.value==="true"){
                return(
                    <Switch onChange={this.onChange.bind(this)} defaultChecked={false}/>
                )
            }else {
                return(
                    <Switch onChange={this.onChange.bind(this)} defaultChecked={true}/>
                )
            }
        }else {
            if (this.state.value==="true"){
                return(
                    <Switch onChange={this.onChange.bind(this)} disabled={true} defaultChecked={false}/>
                )
            }else {
                return(
                    <Switch onChange={this.onChange.bind(this)} disabled={true} defaultChecked={true}/>
                )
            }
        }

    }
}

export default injectIntl(PublicStatus)
