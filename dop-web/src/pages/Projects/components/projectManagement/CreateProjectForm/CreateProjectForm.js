import {Field, Form, Input, Loading} from "@icedesign/base";
import API from "../../../../API";
import Axios from "axios";
import React, {Component} from 'react';

import PrivateController from "../PrivateController"
import "./CreateProjectForm.scss"
import {FormattedMessage, injectIntl} from "react-intl";

const FormItem = Form.Item;


const formItemLayout = {
    labelCol: {span: 8},
    wrapperCol: {span: 16}
};

/**
 *    弹窗中的表单
 *
 * */

class ProjectForm extends Component {
    constructor(props, context) {
        super(props, context);
        this.field = new Field(this);
        this.state = {
            loading: false
        }
    }

    /**
     *    处理来自父组件按钮的提交信息
     *
     * */
    handleSubmit(props) {

        // 校验表单数据
        this.field.validate((errors, values) => {
            console.log(errors, values);
            let _this = this


            // 没有异常则提交表单
            if (errors === null) {
                console.log("noerros");
                this.setState({
                    loading: true
                })
                let url = API.application + '/project'
                Axios.post(url, {}, {
                        params: {
                            organizationId: "123",
                            title: this.field.getValue('title'),
                            status: this.field.getValue('private'),
                            projectDescription: this.field.getValue('description')
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
                        console.log(error);
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
        // const {init, getValue} = this.field;
        console.log(this.props.intl)
        return (
            <Loading visible={this.state.loading} shape="dot-circle" color="#2077FF" className="form-loading">
                <div className="form-container">
                    <Form
                        labelAlign={"left"}
                        className="form"
                    >
                        <FormItem {...formItemLayout}
                                  validateStatus={this.field.getError("title") ? "error" : ""}
                                  help={this.field.getError("title") ? this.props.intl.messages["projects.placeHolder.projectName"] : ""}
                                  label={<FormattedMessage
                                      id="projects.text.projectName"
                                      defaultMessage="项目名称："
                                  />}
                                  required>
                            <Input
                                maxLength={25}
                                hasLimitHint
                                {...init('title', {
                                    rules: [{
                                        required: true, message: this.props.intl.messages["projects.messages.cantNull"]
                                    }]
                                })}
                                placeholder={this.props.intl.messages["projects.placeHolder.projectName"]}/>
                        </FormItem>
                        <FormItem {...formItemLayout}
                                  validateStatus={this.field.getError("private") ? "error" : ""}
                                  help={this.field.getError("private") ? this.props.intl.messages["projects.placeHolder.Openness"] : ""}
                                  label={<FormattedMessage
                                      id="projects.text.Openness"
                                      defaultMessage="公开性："
                                  />}
                                  required>
                            <PrivateController   {...init('private', {
                                rules: [{required: true, initValue: "public"}]
                            })}/>
                        </FormItem>
                        <FormItem {...formItemLayout}
                                  label={<FormattedMessage
                                      id="projects.placeHolder.projectDescription"
                                      defaultMessage="项目描述"
                                  />}>
                            <Input  {...init('description')}
                                    maxLength={50}
                                    hasLimitHint
                                    multiple
                                    placeholder={this.props.intl.messages["projects.text.projectDescription"]}/>
                        </FormItem>
                    </Form>
                </div>
            </Loading>
        )
    }
}

export default injectIntl(ProjectForm)