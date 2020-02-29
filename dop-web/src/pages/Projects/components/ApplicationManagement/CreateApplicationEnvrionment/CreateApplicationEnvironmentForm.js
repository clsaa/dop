import {Field, Form, Input, Loading, Select} from "@icedesign/base";
import API from "../../../../API";
import Axios from "axios";
import {injectIntl} from "react-intl";
import React, {Component} from 'react';
import "./CreateApplicationEnvironmentForm.scss"

const FormItem = Form.Item;

const Option = Select.Option;


const formItemLayout = {
    labelCol: {span: 8},
    wrapperCol: {span: 16}
};

/**
 *  创建应用变量的弹窗
 *  @author Bowen
 *
 * */

class ApplicationEnvironmentForm extends Component {
    constructor(props, context) {
        super(props, context);
        this.field = new Field(this);
        this.state = {
            appId: props.appId,
            loading: false
        }
    }

    /**
     *    处理来自父组件按钮的提交信息
     *
     * */
    handleSubmit(props) {

        // 校验表单数据
        let _this = this;

        this.field.validate((errors, values) => {
            console.log(errors, values);
            console.log(_this.field.getValue('environmentLevel'))
            console.log(_this.field.getValue('deploymentStrategy'))

            // 没有异常则提交表单
            if (errors === null) {
                this.setState({
                    loading: true
                })
                console.log("noerros");
                let postUrl = API.gateway + "/application-server/app/" + this.state.appId + "/env/";
                Axios.post(postUrl, {}, {
                        params: {
                            "title": _this.field.getValue('title'),
                            "environmentLevel": _this.field.getValue('environmentLevel'),
                            "deploymentStrategy": _this.field.getValue('deploymentStrategy')
                        }
                    }
                )
                    .then(function (response) {
                        console.log(response);
                        _this.setState({
                            loading: false
                        })
                        props.finished();
                    })
                    .catch(function (error) {
                        _this.setState({
                            loading: false
                        })
                    });

            }
        });

        //
        // console.log("handleSubmit");
    }

    componentWillReceiveProps(nextProps, nextContext) {
        if (nextProps.isSubmit) {
            this.handleSubmit(nextProps);
        }
    }

    render() {
        const {init} = this.field;
        return (
            <Loading visible={this.state.loading}
                     shape="dot-circle"
                     color="#2077FF"
                     className="form-container">
                <Form
                    labelAlign={"left"}
                    className="create-app-env-form"
                >
                    <FormItem
                        {...formItemLayout}
                        validateStatus={this.field.getError("title") ? "error" : ""}
                        help={this.field.getError("title") ? this.props.intl.messages['projects.check.envName'] : ""}
                        label={this.props.intl.messages['projects.text.envName']}
                        required>
                        <Input
                            maxLength={10}
                            hasLimitHint
                            {...init('title', {
                                rules: [{
                                    required: true,
                                    message: this.props.intl.messages['projects.message.cantNull']
                                }]
                            })}
                            placeholder={this.props.intl.messages['projects.check.envName']}/>
                    </FormItem>

                    <FormItem
                        {...formItemLayout}
                        validateStatus={this.field.getError("environmentLevel") ? this.props.intl.messages['projects.text.envNameLevel'] : ""}
                        label={this.props.intl.messages['projects.text.envNameLevel']}
                        required>
                        <Select  {...init('environmentLevel', {
                            rules: [{
                                required: true,
                                message: this.props.intl.messages['projects.message.cantNull']
                            }]
                        })}
                                 placeholder={this.props.intl.messages['projects.text.envNameLevel']}>
                            <Option value="DAILY">{this.props.intl.messages['projects.text.dailyEnv']}</Option>
                            <Option value="PRERELEASE">{this.props.intl.messages['projects.text.preRelease']}</Option>
                            <Option value="RELEASE">{this.props.intl.messages['projects.text.release']}</Option>
                        </Select>
                    </FormItem>
                    <FormItem
                        {...formItemLayout}
                        validateStatus={this.field.getError("deploymentStrategy") ? this.props.intl.messages['projects.check.deploymentStrategy'] : ""}
                        label={this.props.intl.messages['projects.text.deploymentStrategy']}
                        required>
                        <Select className="form-item-select" {...init('deploymentStrategy', {
                            rules: [{
                                required: true,
                                message: this.props.intl.messages['projects.message.cantNull']
                            }]
                        })}
                                placeholder={this.props.intl.messages['projects.text.deploymentStrategy']}>
                            <Option
                                value="KUBERNETES">{this.props.intl.messages['projects.text.deploymentByKubernetes']}</Option>
                        </Select>
                    </FormItem>

                </Form>
            </Loading>)
    }
}

export default injectIntl(ApplicationEnvironmentForm)