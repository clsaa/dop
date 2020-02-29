import {Field, Form, Input, Loading} from "@icedesign/base";
import API from "../../../API";
import Axios from "axios";
import React, {Component} from 'react';
import PrivateController from "../PrivateController/index"
import {injectIntl,FormattedMessage} from 'react-intl'

const FormItem = Form.Item;
const formItemLayout = {
    labelCol: {span: 14},
    wrapperCol: {span: 16}
};

/**
 *    弹窗中的表单
 *
 * */
class NamespaceForm extends Component {
    constructor(props, context) {
        super(props, context);
        this.field = new Field(this);
        this.state = {
            loading: false,
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
            let _this = this;

            // 没有异常则提交表单
            if (errors == null) {
                let url = API.image + '/v1/projects';
                Axios.post(url, {}, {
                        params: {
                            name: values.title,
                            status: values.private
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
    }

    componentWillReceiveProps(nextProps, nextContext) {
        if (nextProps.isSubmit) {
            this.handleSubmit(nextProps);
        }
    }

    render() {
        const {init} = this.field;

        return (
            <Loading visible={this.state.loading} shape="dot-circle" color="#2077FF">
                <div>
                    <Form language={this.props.intl.locale==='zh-CN'?'zh-cn':'en-us'}
                        labelAlign={"left"}
                    >
                        <FormItem {...formItemLayout}
                                  validateStatus={this.field.getError("title") ? "error" : ""}
                                  help={this.field.getError("title") ? this.props.intl.messages["image.namespace.nameWarn"] : ""}
                                  label={this.props.intl.messages["image.namespace.name"]}
                                  required>
                            <Input {...init('title', {rules: [{required: true,whiteSpace:true,trigger:["onBlur","onChange"]}]})}
                                   placeholder={this.props.intl.messages["image.namespace.namePlaceholder"]}/>
                        </FormItem>
                        <FormItem {...formItemLayout}
                                  validateStatus={this.field.getError("private") ? "error" : ""}
                                  label={this.props.intl.messages["image.namespace.publicStatus"]}
                                  required>
                            <PrivateController {...init('private', {
                                rules: [{required: true}],initValue:"true"
                            })}/>
                        </FormItem>
                    </Form>
                </div>
            </Loading>
        )
    }
}
export default injectIntl(NamespaceForm);