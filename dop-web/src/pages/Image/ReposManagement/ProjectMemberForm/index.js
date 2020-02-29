import {Field, Form, Input, Loading,Grid,Feedback} from "@icedesign/base";
import API from "../../../API";
import Axios from "axios";
import React, {Component} from 'react';
import RoleController from '../RoleController'
import {injectIntl} from "react-intl";

const FormItem = Form.Item;
const {Col} = Grid;

const formItemLayout = {
    labelCol: {span: 14},
    wrapperCol: {span: 20}
};

const Toast =Feedback.toast;
/**
 *    弹窗中的表单
 *
 * */
class ProjectMemberForm extends Component {
    constructor(props, context) {
        super(props, context);
        this.field = new Field(this);
        this.state = {
            loading: false,
            projectId:this.props.projectId
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
                let url = API.image + '/v1/projects/'+_this.state.projectId+"/members";
                Axios.post(url, {}, {
                        params: {
                            userName: values.userName,
                            roleId: values.role
                        }
                    }
                )
                    .then(function (response) {
                        console.log(response);
                        _this.setState({
                            loading: false
                        })
                        props.finished("success");
                    })
                    .catch(function (error) {
                        props.finished("failed");
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
                    <Form
                        language={this.props.intl.locale==='zh-CN'?'zh-cn':'en-us'}
                        labelAlign={"left"}
                    >
                        <Col>
                            <FormItem {...formItemLayout}
                                      validateStatus={this.field.getError("userName") ? "error" : ""}
                                      help={this.field.getError("userName") ? this.props.intl.messages["image.addMember.userNameMessage"] : ""}
                                      label={this.props.intl.messages["image.addMember.userName"]}
                                      required>
                                <Input {...init('userName', {rules: [{required: true, message: "该项不能为空"}]})}
                                       placeholder={this.props.intl.messages["image.addMember.userNameMessage"]}/>
                            </FormItem>
                            <FormItem {...formItemLayout}
                                      validateStatus={this.field.getError("role") ? "error" : ""}
                                      help={this.field.getError("role") ? "请选择用户角色" : ""}
                                      label={this.props.intl.messages["image.addMember.userRole"]}
                                      required>
                                <RoleController   {...init('role', {
                                    rules: [{required: true}],initValue:"0"
                                })}/>
                            </FormItem>
                        </Col>
                    </Form>
                </div>
            </Loading>
        )
    }
}
export default injectIntl(ProjectMemberForm)