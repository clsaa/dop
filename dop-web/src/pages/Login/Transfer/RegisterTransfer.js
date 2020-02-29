import React, {Component} from 'react';
import './RegisterTransfer.scss';
import Logo from '../../../components/Logo'
import Footer from "../../../components/Footer";
import Axios from 'axios'
import API from '../../API'
import {Feedback} from "@icedesign/base";
import {RSA} from "../index";
import {injectIntl} from "react-intl";
const {toast} = Feedback;

class RegisterTransfer extends Component {

    componentWillMount(){
        let self = this;
        RSA().then((data)=>{
            self.activation()
        }).catch((error)=>{
            toast.show({
                type: "error",
                content: self.props.intl.messages["login.transfer.RegisterTransfer.rsa.error"],
                duration: 1000
            });
        })
    }
    activation(){
        let self = this;
        let data = self.props.location.search.substr(6);
        let url = API.gateway + '/user-server/v1/users?code=' + data;
        Axios.post(url).then((response)=>{
            if(response.status === 200){
                toast.show({
                    type: "success",
                    content: self.props.intl.messages["login.transfer.RegisterTransfer.success"],
                    duration: 5000
                });
            }
        }).catch((error)=>{
            console.log(error.response)
            toast.show({
                type: "error",
                content: error.response.data.message,
                duration: 3000
            });
        });
        self.props.history.push('/login')
    }

    render() {
        return (
            <div className="register-body">
                <div className="header-content">
                    <Logo isDark/>
                </div>
                <div className="register-content">
                    <div className="title">
                        {this.props.intl.messages["login.transfer.RegisterTransfer.title"]}
                    </div>
                    <div className="tip">
                        {this.props.intl.messages["login.transfer.RegisterTransfer.tip"]}
                        <br />  <br />
                        {this.props.intl.messages["login.transfer.RegisterTransfer.tip.waiting"]}......
                    </div>
                </div>
                <Footer/>
            </div>
        )
    }
}
export default injectIntl(RegisterTransfer)
