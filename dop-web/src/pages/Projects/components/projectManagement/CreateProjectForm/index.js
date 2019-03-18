import {Field, Form, Input} from "@icedesign/base";
import API from "../../../../API";
import Axios from "axios";
import React, {Component} from 'react';

import PrivateController from "../PrivateController"

const FormItem = Form.Item;
const style = {
    padding: "20px",
    background: "#F7F8FA",
    margin: "20px"
};

const formItemLayout = {
    labelCol: {span: 8},
    wrapperCol: {span: 16}
};

/**
 *    弹窗中的表单
 *
 * */
export default class ProjectForm extends Component {
    constructor(props, context) {
        super(props, context);
        this.field = new Field(this);
    }

    /**
     *    处理来自父组件按钮的提交信息
     *
     * */
    handleSubmit(props) {

        // 校验表单数据
        this.field.validate((errors, values) => {
            console.log(errors, values);

            // 没有异常则提交表单
            if (errors == null) {
                console.log("noerros");
                let url = API.gateway + '/application-server/project/123';
                Axios.post(url, {}, {
                        params: {
                            title: this.field.getValue('title')
                            // private: this.field.getValue('title'),
                            // projectDescription:this.field.getValue('description')}
                        }
                    }
                )
                    .then(function (response) {
                        console.log(response);
                        props.finished();
                    })
                    .catch(function (error) {
                        console.log(error);
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
        const {init, getValue} = this.field;
        // const {init, getValue} = this.field;

        return (
            <div>
                <Form
                    labelAlign={"left"}
                    style={style}
                >
                    <FormItem {...formItemLayout}
                              validateStatus={this.field.getError("title") ? "error" : ""}
                              help={this.field.getError("title") ? "请输入名称" : ""}
                              label="项目名称："
                              required>
                        <Input {...init('title', {rules: [{required: true, message: "该项不能为空"}]})}
                               placeholder="请输入项目名称"/>
                    </FormItem>
                    <FormItem {...formItemLayout}
                              validateStatus={this.field.getError("private") ? "error" : ""}
                              help={this.field.getError("private") ? "请选择公开性" : ""}
                              label="公开性："
                              required>
                        <PrivateController   {...init('private', {
                            rules: [{required: true, initValue: "public"}]
                        })}/>
                    </FormItem>
                    <FormItem {...formItemLayout}
                              label="项目描述：">
                        <Input  {...init('description')}
                                multiple placeholder="项目描述"/>
                    </FormItem>
                </Form>
            </div>
        )
    }
}
