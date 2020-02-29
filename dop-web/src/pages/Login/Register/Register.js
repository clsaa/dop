import React, {Component} from 'react';
import './Register.scss';
import Logo from '../../../components/Logo'
import Footer from "../../../components/Footer";
import {Form, Input, Button, Field, Feedback} from "@icedesign/base";
import API from "../../API";
import Axios from "axios/index";
import {Encryption, PublicKey} from '../index'
import {injectIntl} from "react-intl";


const {Item: FormItem} = Form;
const {toast} = Feedback;


class Register extends Component {
    constructor(props) {
        super(props);
        this.field = new Field(this);
        this.state = {
            access_token: ""
        }
    }

    handleReset(e) {
        e.preventDefault();
        this.field.reset();
    }

    handleSubmit(e) {
        e.preventDefault();
        let self = this;
        self.field.validate((errors, values) => {
            if (errors) {
                toast.show({
                    type: "error",
                    content: self.props.intl.messages["login.forget.form.error"],
                    duration: 1000
                });
                return;
            }
            self.submit(values);
        });
    }

    submit(data) {
        let url = API.gateway + '/user-server/v1/account/register';
        let self = this;
        delete data.rePasswd;
        toast.show({
            type: "prompt",
            content: self.props.intl.messages["login.register.registering"] + "...",
            duration: 5000
        });
        PublicKey().then((publicKey) => {
            console.log(data.passwd)
            data.password = Encryption(data.passwd, publicKey);
            delete data.passwd;
            console.log(data);
            Axios({
                url: url,
                method: 'post',
                data: data
            }).then((response) => {
                if (response.status === 200) {
                    toast.show({
                        type: "success",
                        content: self.props.intl.messages["login.register.verification.send"],
                        duration: 5000
                    });
                } else {
                    toast.show({
                        type: "error",
                        content: self.props.intl.messages["login.register.unknown.error"],
                        duration: 5000
                    });
                }
                self.props.history.push('/login');
            })
        }).catch((error) => {
            toast.show({
                type: "error",
                content: error,
                duration: 2000
            });
        })

    }

    userExists(rule, value, callback) {
        let self = this
        let reg = /^(?!.*?_$)[a-zA-Z0-9_]+$/;
        if (reg.test(value)) {
            callback();
        } else {
            callback([new Error(self.props.intl.messages["login.register.username.tip"])]);
        }
    }

    checkPass(rule, value, callback) {
        const {validate} = this.field;
        let reg = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#.$%=-]).{8,20}$/;
        if (reg.test(value)) {
            validate(["rePasswd"]);
            callback();
        } else {
            callback([new Error(this.props.intl.messages["login.forget.password.tip"])]);
        }

    }

    checkPass2(rule, value, callback) {
        const {getValue} = this.field;
        if (value && value !== getValue("passwd")) {
            callback(this.props.intl.messages["login.forget.password.same"]);
        } else {
            callback();
        }
    }

    render() {
        const {init, getError, getState} = this.field;
        const formItemLayout = {
            labelCol: {
                span: 6
            },
            wrapperCol: {
                span: 14
            }
        };
        return (
            <div className="register-body">
                <div className="header-content">
                    <Logo isDark/>
                </div>
                <div className="register-content">
                    <div className="title">
                        {this.props.intl.messages["login.transfer.RegisterTransfer.title"]}
                    </div>
                    <Form field={this.field}>
                        <FormItem
                            label={this.props.intl.messages["login.register.username"] + "："}
                            {...formItemLayout}
                            hasFeedback
                            help={
                                getState("name") === "validating"
                                    ? this.props.intl.messages["login.register.username.verify"] + "..."
                                    : (getError("name") || []).join(", ")
                            }
                        >
                            <Input
                                hasLimitHint
                                placeholder={this.props.intl.messages["login.register.username.placeholder"]}
                                {...init("name", {
                                    rules: [{
                                            required: true
                                        },
                                        {validator: this.userExists.bind(this)}
                                    ]
                                })}
                            />
                        </FormItem>

                        <FormItem
                            label={this.props.intl.messages["login.forget.email"] + "："}
                            {...formItemLayout}
                            hasFeedback
                        >
                            <Input
                                type="email"
                                placeholder={this.props.intl.messages["login.forget.email.placeholder"]}
                                {...init("email", {
                                    rules: [
                                        {required: true, trigger: "onBlur"},
                                        {
                                            type: "email",
                                            message: <span>{this.props.intl.messages["login.forget.email.tip"]}</span>,
                                            trigger: ["onBlur", "onChange"]
                                        }
                                    ]
                                })}
                            />
                        </FormItem>

                        <FormItem
                            label={this.props.intl.messages["login.register.password"] + "："}
                            {...formItemLayout}
                            hasFeedback
                        >
                            <Input
                                maxLength={20}
                                placeholder={this.props.intl.messages["login.forget.newPassword.placeholder"]}
                                htmlType="password"
                                {...init("passwd", {
                                    rules: [
                                        {required: true, whitespace: true, min: 8, message: this.props.intl.messages["login.forget.newPassword.min"]},
                                        {validator: this.checkPass.bind(this)}
                                    ]
                                })}
                            />
                        </FormItem>

                        <FormItem
                            label={this.props.intl.messages["login.forget.confirmPassword"] + "："}
                            {...formItemLayout}
                            hasFeedback
                        >
                            <Input
                                htmlType="password"
                                placeholder={this.props.intl.messages["login.forget.confirmPassword.placeholder"]}
                                {...init("rePasswd", {
                                    rules: [
                                        {
                                            required: true,
                                            whitespace: true,
                                            message: this.props.intl.messages["login.forget.confirmPassword.again"]
                                        },
                                        {
                                            validator: this.checkPass2.bind(this)
                                        }
                                    ]
                                })}
                            />
                        </FormItem>

                        <FormItem wrapperCol={{offset: 6}}>
                            <Button type="primary" onClick={this.handleSubmit.bind(this)}>
                                {this.props.intl.messages["login.forget.confirm"]}
                            </Button>
                            &nbsp;&nbsp;&nbsp;
                            <Button onClick={this.handleReset.bind(this)}>
                                {this.props.intl.messages["login.forget.clear"]}
                            </Button>
                        </FormItem>
                    </Form>
                </div>
                <Footer/>
            </div>
        )
    }
}

export default injectIntl(Register)
