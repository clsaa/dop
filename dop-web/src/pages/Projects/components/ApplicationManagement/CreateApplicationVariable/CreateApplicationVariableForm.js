import {Field, Form, Input, Loading} from "@icedesign/base";
import API from "../../../../API";
import Axios from "axios";
import {injectIntl} from "react-intl";
import "./CreateApplicationVariableForm.scss"
import React, {Component} from 'react';

const FormItem = Form.Item;

const formItemLayout = {
    labelCol: {span: 8},
    wrapperCol: {span: 16}
};


class CreateApplicationVariableForm extends Component {
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
        this.setState({
            loading: true
        })
        this.field.validate((errors, values) => {
            console.log(errors, values);

            // 没有异常则提交表单
            if (errors === null) {
                console.log("noerros");
                let postUrl = API.gateway + "/application-server/app/" + this.state.appId + "/variable";
                Axios.post(postUrl, {
                        "varKey": _this.field.getValue('key'),
                        "varValue": _this.field.getValue('value')
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
            // console.log((nextProps));

        }
    }

    render() {
        const {init} = this.field;
        return (
            <Loading visible={this.state.loading} shape="dot-circle" color="#2077FF"
            >
                <div className="form-container">
                    <Form
                        labelAlign={"left"}
                        className="create-app-var-form"
                    >
                        <FormItem
                            {...formItemLayout}
                            validateStatus={this.field.getError("key") ? "error" : ""}
                            help={this.field.getError("key") ? this.props.intl.messages['projects.check.Key'] : ""}
                            label={this.props.intl.messages['projects.text.Key']}
                            required>
                            <Input {...init('key', {
                                rules: [{
                                    required: true,
                                    message: this.props.intl.messages['projects.message.cantNull']
                                }]
                            })}
                                   placeholder={this.props.intl.messages['projects.placeholder.Key']}/>
                        </FormItem>

                        <FormItem
                            {...formItemLayout}
                            validateStatus={this.field.getError("value") ? this.props.intl.messages['projects.check.Value'] : ""}
                            label={this.props.intl.messages['projects.text.Value']}
                            required>
                            <Input  {...init('value', {
                                rules: [{
                                    required: true,
                                    message: this.props.intl.messages['projects.message.cantNull']
                                }]
                            })}
                                    placeholder={this.props.intl.messages['projects.placeholder.Value']}/>
                        </FormItem>
                    </Form>
                </div>
            </Loading>
        )
    }
}

export default injectIntl(CreateApplicationVariableForm)