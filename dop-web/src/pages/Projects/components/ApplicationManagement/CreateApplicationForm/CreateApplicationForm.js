import {Field, Form, Input, Loading, Select} from "@icedesign/base";
import API from "../../../../API";
import Axios from "axios";
import React, {Component} from 'react';
import ProductModeController from "../ProductModeController"
import "./CreateApplicationForm.scss"
import {injectIntl} from "react-intl";

const Option = Select.Option;
const FormItem = Form.Item;
const {Combobox} = Select;
const formItemLayout = {
    labelCol: {span: 8},
    wrapperCol: {span: 16}
};
// const  REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
/**
 *    弹窗中的表单
 *
 * */
class ApplicationForm extends Component {
    constructor(props, context) {
        super(props, context);
        this.field = new Field(this);
        this.state = {
            projectId: props.projectId,
            loading: false,
            gitUrlData: [],
            imageUrlData: [],
            projectData: ""
        }
    }

    getData() {
        this.setState({
            loading: true
        })
        let projectUrl = API.application + "/project/" + this.state.projectId
        Axios.get(projectUrl)
            .then((response) => {
                _this.setState({
                    projectData: response.data
                })
            })
        let _this = this
        let gitUrl = API.application + "/git_url_list"
        Axios.get(gitUrl).then((response) => {
            _this.setState({
                gitUrlData: response.data
            })
        })
            .catch((response => {
                console.log(response)
            }))
        let imageUrl = API.application + "/image_url_list"
        Axios.get(imageUrl, {
            params: {
                projectName: _this.state.projectData.title
            }
        })
            .then((response) => {
                _this.setState({
                    imageUrlData: response.data,
                    loading: false
                })
            })
            .catch((response => {
                console.log(response)
                _this.setState({
                    loading: false
                })
            }))
    }

    /**
     *    处理来自父组件按钮的提交信息
     *
     * */
    handleSubmit(props) {
        let _this = this
        // 校验表单数据
        this.field.validate((errors, values) => {
            console.log(errors, values);

            // 没有异常则提交表单
            if (errors === null) {
                console.log("noerros");
                this.setState({
                    loading: true
                })
                let url = API.application + '/app/' + this.state.projectId;
                Axios.post(url, {}, {
                        params: {
                            title: this.field.getValue('title'),
                            productMode: this.field.getValue('productMode'),
                            appDescription: this.field.getValue('description'),
                            gitUrl: this.field.getValue('gitUrl'),
                            imageUrl: this.field.getValue('imageUrl')
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

    componentDidMount() {
        this.getData()
    }

    onGitUrlChange(e, value) {
        console.log("value0", value)
        this.field.setValue("gitUrl", value.value)
    }

    onGitUrlInputBlur(e, value) {
        console.log("value", value)
        this.field.setValue("gitUrl", value)
    }

    onImageUrlChange(e, value) {
        console.log("value0", value)
        this.field.setValue("imageUrl", value.value)
    }

    onImageUrlInputBlur(e, value) {
        console.log("value", value)
        this.field.setValue("imageUrl", value)
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

            <div className="create-app-form-container">
                    <Form
                        labelAlign={"left"}
                        className="create-app-form"
                    >
                        <Loading visible={this.state.loading} shape="dot-circle" color="#2077FF"
                                 className="create-app-form-loading">

                            <FormItem {...formItemLayout}
                                      validateStatus={this.field.getError("title") ? "error" : ""}
                                      help={this.field.getError("title") ? this.props.intl.messages['projects.placeholder.applicationName'] : ""}
                                      label={this.props.intl.messages['projects.text.applicationName']}
                                      required>
                                <Input
                                    className="form-item-input"
                                    maxLength={25}
                                    hasLimitHint
                                    {...init('title', {
                                        rules: [{
                                            required: true,
                                            message: this.props.intl.messages['projects.message.cantNull']
                                        }]
                                    })}
                                    placeholder={this.props.intl.messages['projects.placeholder.applicationName']}/>
                            </FormItem>

                            <FormItem {...formItemLayout}
                                      validateStatus={this.field.getError("productMode") ? "error" : ""}
                                      help={this.field.getError("productMode") ? this.props.intl.messages['projects.placeHolder.developMode'] : ""}
                                      label={this.props.intl.messages['projects.text.developMode']}
                                      required>

                                <ProductModeController {...init('productMode', {
                                    rules: [{required: true, initValue: "BRANCH"}]
                                })}/>
                            </FormItem>
                            <FormItem {...formItemLayout}
                                      validateStatus={this.field.getError("gitUrl") ? "error" : ""}
                                      label={this.props.intl.messages['projects.text.gitUrl']}
                                      help={this.field.getError("gitUrl") ? this.props.intl.messages['projects.text.checkGitUrl'] : ""}
                                      required>
                                <Combobox className="form-item-select" {...init('gitUrl', {
                                    rules: [{
                                        type: "url",
                                        required: true,
                                        message: this.props.intl.messages['projects.message.cantNull']
                                    }]
                                })}
                                          placeholder={this.props.intl.messages['projects.placeHolder.gitUrl']}
                                          onChange={this.onGitUrlChange.bind(this)}
                                          onInputBlur={this.onGitUrlInputBlur.bind(this)}>
                                    {this.state.gitUrlData.length === 0 ? "" : this.state.gitUrlData.map((item) => {
                                        return (<Option value={String(item)}>{String(item)}</Option>)
                                    })}
                                </Combobox>
                            </FormItem>
                            <FormItem {...formItemLayout}
                                      validateStatus={this.field.getError("form-item-select") ? "error" : ""}
                                      help={this.field.getError("imageUrl") ? this.props.intl.messages['projects.text.checkImageUrl'] : ""}
                                      label={this.props.intl.messages['projects.text.imageUrl']}
                                      required>
                                <Combobox className="form-item-select" {...init('imageUrl', {
                                    rules: [{
                                        pattern: "[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]",
                                        required: true,
                                        message: this.props.intl.messages['projects.message.cantNull']
                                    }]
                                })}
                                          placeholder={this.props.intl.messages['projects.placeHolder.imageUrl']}
                                          onChange={this.onImageUrlChange.bind(this)}
                                          onInputBlur={this.onImageUrlInputBlur.bind(this)}>
                                    {this.state.imageUrlData.length === 0 ? "" : this.state.imageUrlData.map((item) => {
                                        return (<Option value={String(item)}>{String(item)}</Option>)
                                    })}
                                </Combobox>
                            </FormItem>
                            <FormItem {...formItemLayout}
                                      label={this.props.intl.messages['projects.text.applicationDescription']}>
                                <Input
                                    maxLength={50}
                                    hasLimitHint
                                    className="form-item-input"
                                    {...init('description')} multiple
                                    placeholder={this.props.intl.messages['projects.placeHolder.applicationDescription']}/>
                            </FormItem>
                        </Loading>
                    </Form>
                </div>
        )
    }
}

export default injectIntl(ApplicationForm)
