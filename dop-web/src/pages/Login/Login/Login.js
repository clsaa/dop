import React, {Component} from 'react';
import Logo from '../../../components/Logo'
import './Login.scss';
import Footer from "../../../components/Footer";
import {Button, Feedback, Field, Form, Input, Loading} from "@icedesign/base";
import {Encryption, PublicKey, RSA} from '../index'
import API from "../../API";
import Axios from "axios/index";
import jsonp from "jsonp";
import {injectIntl} from "react-intl";

const {Item: FormItem} = Form;
const {toast} = Feedback;

class Login extends Component {
    constructor(props) {
        super(props);
        this.field = new Field(this);
        this.state = {
            visible: true,
            ip: '0.0.0.0'
        }
    }

    componentWillMount() {
        let self = this;
        window.sessionStorage.clear();
        document.addEventListener("keydown", this.handleEnterKey);
        RSA().then(() => {
            self.setState({
                visible: false
            });
        }).catch((error) => {
            toast.show({
                type: "error",
                content: error.message,
                duration: 1000
            });
        });
        self.loginIp()
    }

    handleEnterKey = (e) => {
        if(e.keyCode === 13){
            this.handleSubmit(e)
        }
    };

    handleSubmit(e) {
        e.preventDefault();
        let self = this;
        self.setState({
            visible: true
        });
        this.field.validate((errors, values) => {
            if (errors) {
                toast.show({
                    type: "error",
                    content: self.props.intl.messages["login.login.info.tip"],
                    duration: 1000
                });
                return;
            }
            self.submit(values);
        });
    }

    register() {
        this.props.history.push('/register');
    }

    modify() {
        this.props.history.push('/modifyPwd');
    }

    loginIp() {
        let self = this;
        jsonp('https://api.ipify.org?format=jsonp', (error, data) => {
            if (!error) {
                self.setState({
                    ip: data
                })
            }
        })
    }

    getUser(){
        let url = API.gateway + '/user-server/v1/users';
        let self = this;
        Axios.get(url).then((response)=>{
            window.sessionStorage.setItem('user-id', response.data.id);
            window.sessionStorage.setItem('user-name', response.data.name);
            window.sessionStorage.setItem('user-email', response.data.email);
        }).catch((error)=>{
            toast.show({
                type: "error",
                content: error.message,
                duration: 1000
            });
        })
    }

    login(data) {
        let url = API.gateway + '/login-server/v1/login';
        let self = this;
        return new Promise((resolve, reject)=>{
            PublicKey().then((publicKey) => {
                data.password = Encryption(data.passwd, publicKey);
                delete data.passwd;
                data.client = "DOP_WEB";
                data.deviceId = data.email;
                Axios({
                    url: url,
                    method: 'post',
                    data: data
                }).then((response) => {
                    if (response.status === 200) {
                        Axios.defaults.headers.common['x-login-token'] = response.data;
                        Axios.defaults.headers.common['x-login-user'] = response.data;
                        window.sessionStorage.setItem("x-login-token", response.data);
                        window.sessionStorage.setItem("x-login-user", response.data);
                        resolve()
                    } else {
                        toast.show({
                            type: "error",
                            content: self.props.intl.messages["login.login.info.error"],
                            duration: 1000
                        });
                        reject()
                    }
                    self.setState({
                        visible: false
                    });
                }).catch((error) => {
                    self.setState({
                        visible: false
                    })
                })
            }).catch((error) => {
                self.setState({
                    visible: false
                })
            })
        })

    }
    submit(data){
        let self = this;
        this.login(data).then(()=>{
            self.getUser()
        }).then(()=>{
            self.props.history.push('/project')
        })
    }

    render() {
        const {init} = this.field;
        const formItemLayout = {
            labelCol: {
                span: 6,
            },
            wrapperCol: {
                span: 14,
            },
        };
        return (
            <div className="login-body">
                <div className="header-content">
                    <Logo isDark/>
                </div>
                <div className="login-content">
                    <div className="left">
                        <div className="img">
                            {(()=>{
                                if(window.sessionStorage.getItem('language') === "zh-CN"){
                                    return (
                                        <img src={require('./images/logo-zh.png')} alt="logo" width="100%"/>
                                    )
                                }else{
                                    return (
                                        <img src={require('./images/logo-en.png')} alt="logo" width="100%"/>
                                    )
                                }
                            })()}

                        </div>
                    </div>
                    <div className="right">
                        <Loading shape="fusion-reactor" visible={false}
                                 className="next-loading my-loading"
                        >
                            <div className="form">
                                <div className="title">
                                    {this.props.intl.messages["login.login.title"]}
                                </div>
                                <Form field={this.field} className="form-body">

                                    <FormItem
                                        label={this.props.intl.messages["login.login.email"]+ "："}
                                        {...formItemLayout} hasFeedback
                                    >
                                        <Input
                                            type="email"
                                            placeholder=""
                                            {...init("email", {
                                                rules: [
                                                    {required: true, trigger: "onBlur"},
                                                    {
                                                        type: "email",
                                                        message: <span>{this.props.intl.messages["login.login.email.tip"]}</span>,
                                                        trigger: ["onBlur", "onChange"]
                                                    }
                                                ]
                                            })}
                                        />
                                    </FormItem>

                                    <FormItem
                                        label={this.props.intl.messages["login.login.password"]+ "："}
                                        {...formItemLayout} hasFeedback>
                                        <Input
                                            htmlType="password"
                                            {...init("passwd", {
                                                rules: [
                                                    {required: true, whitespace: true, message: this.props.intl.messages["login.login.password.tip"]},
                                                ]
                                            })}
                                        />
                                    </FormItem>

                                    <FormItem className="submit">
                                        <Button type="primary"
                                                onClick={this.handleSubmit.bind(this)}

                                                size="large"
                                                className="login-form-button"
                                        >
                                            {this.props.intl.messages["login.login.login"]}
                                        </Button>
                                        <div className="register-content">
                                            <span onClick={this.modify.bind(this)}>{this.props.intl.messages["login.login.forget"]}</span>
                                            <span onClick={this.register.bind(this)}>{this.props.intl.messages["login.login.register"]}</span>
                                        </div>
                                    </FormItem>
                                </Form>
                            </div>
                        </Loading>
                    </div>

                </div>
                <Footer/>
            </div>
        )
    }
}
export default injectIntl(Login)
