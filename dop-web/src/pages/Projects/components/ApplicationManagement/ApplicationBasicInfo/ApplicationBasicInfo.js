import React, {Component} from 'react';
import {Breadcrumb, Button, Card, Dialog, Feedback, Field, Form, Input, Loading, Select} from '@icedesign/base';
import Axios from "axios";
import API from "../../../../API";
import TopBar from "./topbar";
import "./ApplicationBasicInfo.scss"
import {injectIntl} from "react-intl";

const Option = Select.Option;
const {Combobox} = Select;
const Toast = Feedback.toast;
const FormItem = Form.Item;
const formItemLayout = {
    labelCol: {span: 10},
    wrapperCol: {span: 14}
};


/**
 * 应用基本信息页面
 * @author Bowen
 */

class ApplicationBasicInfo extends Component {

    constructor(props) {
        super(props);
        console.log(props)
        this.field = new Field(this);
        console.log(props)
        this.urlField = new Field(this);
        this.state = {
            urlEditMode: false,
            basicEditMode: false,
            appId: props.appId,
            appBasicData: [],
            userData: [],
            loading: true,
            gitUrlData: [],
            imageUrlData: [],
            projectId: props.projectId
        }
    }

    //加载应用基本信息
    componentDidMount() {
        this.getData()
        this.getUrlData()
    }


    getUrlData() {
        this.setState({
            loading: true
        })
        let projectUrl = API.application + "/project/" + this.state.projectId
        Axios.get(projectUrl)
            .then((response) => {

                let projectData = response.data
                let _this = this
                let gitUrl = API.application + "/git_url_list"
                Axios.get(gitUrl).then((response) => {
                    _this.setState({
                        gitUrlData: response.data
                    })
                    let imageUrl = API.application + "/image_url_list"
                    Axios.get(imageUrl, {
                        params: {
                            projectName: projectData.title
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
                })
                    .catch((response => {
                        console.log(response)
                    }))

            })

    }

    getData() {
        this.setState({
            loading: true
        })
        let _this = this;
        let url = API.application + '/app/' + _this.state.appId + "/urlInfo";

        //获取应用基本信息
        Axios.get(url)
            .then(function (response) {
                console.log(response)

                //获取用户基本信息
                let userUrl = API.gateway + '/user-server/v1/users/' + response.data.ouser;
                let appBasicData = response.data;
                Axios.get(userUrl)
                    .then(function (response) {
                        _this.setState({
                            appBasicData: appBasicData,
                            basicEditMode: false,
                            urlEditMode: false,
                            userData: response.data,
                            loading: false
                        })

                    })

            })
    }

    //切换基本信息的编辑状态
    basicEdit() {
        this.setState({
            basicEditMode: true
        });
    }

    //切换基本信息的编辑状态
    basicEditCancel() {
        this.setState({
            basicEditMode: false
        });
    }

    //切换URL信息的编辑状态
    urlEdit() {
        this.setState({
            urlEditMode: true
        });
    }

    //切换URL信息的编辑状态
    urlEditCancel() {
        this.setState({
            urlEditMode: false
        });
    }

    basicConfirm = () => {
        Dialog.confirm({
            content: this.props.intl.messages['projects.text.confirmSave'],
            title: this.props.intl.messages['projects.title.confirmSave'],
            onOk: this.basicSubmit.bind(this),
            language:'en-us',
        });
    };

    urlConfirm = () => {
        Dialog.confirm({
            content: this.props.intl.messages['projects.text.confirmSave'],
            title: this.props.intl.messages['projects.title.confirmSave'],
            onOk: this.urlSubmit.bind(this),
            language:'en-us',
        });
    };

    //基本信息提交处理函数
    basicSubmit() {
        let _this = this;

        //校验输入
        this.field.validate((errors, values) => {

            console.log(errors, values);


            // 没有异常则提交表单
            if (errors === null) {
                this.setState({
                    loading: true
                })
                console.log("noerros");
                let url = API.application + '/app/' + this.state.appId;
                Axios.put(url, {}, {
                        params: {
                            title: this.field.getValue('title'),
                            description: this.field.getValue('description')
                        }
                    }
                )
                    .then(function (response) {
                        _this.setState({
                            loading: false
                        })
                        _this.getData()
                        Toast.success(_this.props.intl.messages['projects.text.updateSuccessful'])
                    })
                    .catch(function (error) {
                        _this.setState({
                            loading: false
                        })
                    });

            }
        });

    }

//Url信息提交处理函数
    urlSubmit() {
        let _this = this;
        //校验输入
        this.urlField.validate((errors, values) => {

            console.log(errors, values);
            let _this = this
            // 没有异常则提交表单
            if (errors === null) {
                console.log("noerros");
                let url = API.application + '/app/' + this.state.appId + "/urlInfo"
                this.setState({
                    loading: true
                })
                Axios.put(url, {}, {
                        params: {
                            warehouseUrl: this.urlField.getValue("gitUrl"),
                            imageUrl: this.urlField.getValue("imageUrl"),
                            productionDbUrl: this.urlField.getValue("productionDbUrl"),
                            testDbUrl: this.urlField.getValue("testDbUrl"),
                            productionDomain: this.urlField.getValue("productionDomain"),
                            testDomain: this.urlField.getValue("testDomain"),
                        }
                    }
                )
                    .then(function (response) {
                        _this.setState({
                            loading: false
                        })
                        //提交完成后刷新当前页面
                        _this.getData()
                        Toast.success(_this.props.intl.messages['projects.text.updateSuccessful'])
                    })
                    .catch(function (error) {
                        _this.setState({
                            loading: false
                        })
                    });

            }
        });
    }

    onGitUrlChange(e, value) {
        console.log("value0", value)
        this.urlField.setValue("gitUrl", value)
    }

    onImageUrlChange(e, value) {
        console.log("value0", value)
        this.urlField.setValue("imageUrl", value)
    }

    render() {

        const titleOpr = () => {
            const {init} = this.field;
            if (!this.state.basicEditMode) {
                return (
                    <div  {...init('title')}
                          placeholder={this.props.intl.messages['projects.text.applicationName']}
                          className="form-item-text">{this.state.appBasicData.title}</div>)
            } else {
                return (<Input defaultValue={this.state.appBasicData.title}
                               maxLength={25}
                               hasLimitHint
                               {...init('title', {
                    rules: [{
                        required: true,
                        message: this.props.intl.messages['projects.message.cantNull']
                    }]
                })}
                               placeholder={this.props.intl.messages['projects.placeholder.applicationName']}/>)
            }

        }
        const opr = () => {
            const {init} = this.field;
            if (!this.state.basicEditMode) {
                return (
                    <span className="form-item-text">{this.state.appBasicData.description}</span>
                )
            } else {
                return (
                    <Input
                        maxLength={50}
                        hasLimitHint
                        defaultValue={this.state.appBasicData.description} {...init('description')}
                        placeholder={this.props.intl.messages['projects.text.applicationDescription']}/>)
            }
        }

        const urlFormRender = () => {
            const {init} = this.urlField;
            console.log((this.state.appBasicData))
            if (this.state.urlEditMode) {
                return (
                    <div>
                        <FormItem{...formItemLayout} label={this.props.intl.messages['projects.text.gitUrl']}
                                 validateStatus={this.urlField.getError("gitUrl") ? "error" : ""}
                                 help={this.urlField.getError("gitUrl") ? this.props.intl.messages['projects.text.checkGitUrl'] : ""}
                                 required>
                            <Combobox className="form-item-input"
                                      {...init('gitUrl', {
                                initValue: this.state.appBasicData.warehouseUrl,
                                rules: [{
                                    type: "url",
                                    required: true,
                                    message: this.props.intl.messages['projects.message.cantNull']
                                }]
                            })}
                                      placeholder={this.props.intl.messages['projects.placeHolder.gitUrl']}
                                      onChange={this.onGitUrlChange.bind(this)}
                                      onInputBlur={this.onGitUrlChange.bind(this)}
                            >
                                {this.state.gitUrlData.length === 0 ? "" : this.state.gitUrlData.map((item) => {
                                    return (<Option value={String(item)}>{String(item)}</Option>)
                                })}
                            </Combobox>
                        </FormItem>

                        <FormItem{...formItemLayout} label={this.props.intl.messages['projects.text.imageUrl']}
                                 validateStatus={this.urlField.getError("imageUrl") ? "error" : ""}
                                 help={this.urlField.getError("imageUrl") ? this.props.intl.messages['projects.text.checkImageUrl'] : ""}

                                 required>
                            <Combobox className="form-item-input"
                                      {...init('imageUrl', {
                                initValue: this.state.appBasicData.imageUrl,
                                rules: [{
                                    pattern: "[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]",
                                    required: true,
                                    message: this.props.intl.messages['projects.message.cantNull']
                                }]
                            })}
                                      placeholder={this.props.intl.messages['projects.placeHolder.imageUrl']}
                                      onChange={this.onImageUrlChange.bind(this)}
                                      onInputBlur={this.onImageUrlChange.bind(this)}
                                // onInputUpdate={this.onImageUrlChange.bind(this)}
                            >
                                {this.state.imageUrlData.length === 0 ? "" : this.state.imageUrlData.map((item) => {
                                    return (<Option value={String(item)}>{String(item)}</Option>)
                                })}
                            </Combobox>

                        </FormItem>

                        <FormItem{...formItemLayout} label={this.props.intl.messages['projects.text.DevelopDb']}
                                 validateStatus={this.urlField.getError("productionDbUrl") ? "error" : ""}
                                 help={this.urlField.getError("productionDbUrl") ? this.props.intl.messages['projects.text.checkDevelopDb'] : ""}
                        >
                            <Input defaultValue={this.state.appBasicData.productionDbUrl} {...init('productionDbUrl', {
                                rules: [{
                                    type: "url",
                                    message: this.props.intl.messages['projects.message.cantNull']
                                }]
                            })}
                            />
                        </FormItem>

                        <FormItem{...formItemLayout} label={this.props.intl.messages['projects.text.TestDb']}
                                 validateStatus={this.urlField.getError("testDbUrl") ? "error" : ""}
                                 help={this.urlField.getError("testDbUrl") ? this.props.intl.messages['projects.text.checkTestDb'] : ""}
                        >
                            <Input defaultValue={this.state.appBasicData.testDbUrl} {...init('testDbUrl', {
                                rules: [{
                                    type: "url",
                                    message: this.props.intl.messages['projects.message.cantNull']
                                }]
                            })}
                            />
                        </FormItem>

                        <FormItem{...formItemLayout} label={this.props.intl.messages['projects.text.developDomain']}
                                 validateStatus={this.urlField.getError("productionDomain") ? "error" : ""}
                                 help={this.urlField.getError("productionDomain") ? this.props.intl.messages['projects.text.checkDevelopDomain'] : ""}>
                            <Input
                                defaultValue={this.state.appBasicData.productionDomain}  {...init('productionDomain', {
                                rules: [{
                                    pattern: "^([a-z0-9]+.?)+[a-z]+$",
                                    message: this.props.intl.messages['projects.message.cantNull']
                                }]
                            })}
                            />
                        </FormItem>

                        <FormItem{...formItemLayout} label={this.props.intl.messages['projects.text.testDomain']}
                                 validateStatus={this.urlField.getError("testDomain") ? "error" : ""}
                                 help={this.urlField.getError("testDomain") ? this.props.intl.messages['projects.text.checkTestDomain'] : ""}>
                            <Input defaultValue={this.state.appBasicData.testDomain} {...init('testDomain', {
                                rules: [{
                                    pattern: "^([a-z0-9]+.?)+[a-z]+$",
                                    message: this.props.intl.messages['projects.message.cantNull']
                                }]
                            })}
                            />
                        </FormItem>

                    </div>
                )
            } else {
                return (


                    <div>

                        <FormItem{...formItemLayout} label={this.props.intl.messages['projects.text.gitUrl']}>
                            <span className="form-item-text">{this.state.appBasicData.warehouseUrl}</span>
                        </FormItem>

                        <FormItem{...formItemLayout} label={this.props.intl.messages['projects.text.imageUrl']}>
                            <span className="form-item-text">{this.state.appBasicData.imageUrl}</span>
                        </FormItem>

                        <FormItem{...formItemLayout} label={this.props.intl.messages['projects.text.DevelopDb']}>
                            <span
                                className="form-item-text">{this.state.appBasicData.productionDbUrl}</span>
                        </FormItem>

                        <FormItem{...formItemLayout} label={this.props.intl.messages['projects.text.TestDb']}>
                            <span className="form-item-text">{this.state.appBasicData.testDbUrl}</span>
                        </FormItem>

                        <FormItem{...formItemLayout} label={this.props.intl.messages['projects.text.developDomain']}>
                            <span
                                className="form-item-text">{this.state.appBasicData.productionDomain}</span>
                        </FormItem>

                        <FormItem{...formItemLayout} label={this.props.intl.messages['projects.text.testDomain']}>
                            <span className="form-item-text">{this.state.appBasicData.testDomain}</span>
                        </FormItem>
                    </div>
                )
            }
        }


        return (
            <Loading visible={this.state.loading} shape="dot-circle" color="#2077FF"

            >

                <TopBar
                    extraBefore={<Breadcrumb>
                        <Breadcrumb.Item
                            link="#/project">{this.props.intl.messages['projects.bread.allProject']}</Breadcrumb.Item>
                        <Breadcrumb.Item
                            link={"#/projectDetail?projectId=" + this.state.projectId}>{this.props.intl.messages['projects.bread.project'] + this.state.projectId}</Breadcrumb.Item>
                        <Breadcrumb.Item
                            link={"#/applicationDetail?appId=" + this.state.appId + "&projectId=" + this.state.projectId}>{this.props.intl.messages['projects.bread.app'] + this.state.appId}</Breadcrumb.Item>
                    </Breadcrumb>}
                />
                <div className="card-container "
                >

                    <Card
                        className="user-card"
                        title={this.state.userData.name}
                        bodyHeight="100%"
                        subTitle={this.props.intl.messages['projects.text.applicationOwner']}
                        extra={<a href="">{this.props.intl.messages['projects.text.handOverApplication']}&gt;</a>}
                    >
                        <img alt="avatar"
                             src={this.state.userData.avatarURL === "" ? "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAFAAAABQBAMAAAB8P++eAAAABGdBTUEAALGPC/xhBQAAAAFzUkdCAK7OHOkAAAAYUExURcHBwb+/v7+/v76+vujo6OHh4cnJydTU1IOqnXYAAAADdFJOUxPppyMYpxkAAAD6SURBVEjH7dfbDYIwFAbguoHRCYwTKLcBOIUBaHQAIAxQwvwSEQpyaH/FFxP+5y89vacV4uQBOQix86DsxRmDV3HE4EV4YDa4QQRWSjYILKnNzQ0jekY7Yd3B1AVDeiV3wKCHsQPWPUwdkIbYYWSgtsLAwMwKfQNjFCZWWPwBhEcNz+NoZfLfrLXZPYkD+gtd/H6H97UT5+EK0FPY1ZbABaDYygysuTEvtqg9sI9AiyV/o8xgRNj0DLtHaiuszOahxgJLGueeL8Gpa8vnPHx30yEZGKo5lBwMiEnGwIKDKQMVB+UaSGzWwO2psMGPIfxgh78A8KcC/aY8ACmMo3JtJ3ljAAAAAElFTkSuQmCC" : this.state.userData.avatarURL}
                             className="avatar"/>
                    </Card>

                    <Card
                        className="basic-card"
                        title={this.props.intl.messages['projects.text.BasicInfo']}
                        bodyHeight="35%"
                        subTitle={
                            <div className="sub-title">
                                <Button type="primary"
                                        className={this.state.basicEditMode === true ? "edit-button hide" : "edit-button"}
                                        onClick={this.basicEdit.bind(this)}
                                        size="small">
                                    {this.props.intl.messages['projects.button.edit']}
                                </Button>
                                <Button onClick={this.basicConfirm.bind(this)}
                                        type="primary"
                                        size="small"
                                        className={this.state.basicEditMode === true ? "save-button" : "save-button hide"}>
                                    {this.props.intl.messages['projects.button.Save']}
                                </Button>

                                < Button
                                    size="small"
                                    className={this.state.basicEditMode === true ? "cancel-button" : "cancel-button hide"}
                                    onClick={this.basicEditCancel.bind(this)}> {this.props.intl.messages['projects.button.cancel']} </Button>
                            </div>}
                    >
                        <Form labelAlign={"left"}
                              className="card-form">


                            <FormItem{...formItemLayout}
                                     validateStatus={this.field.getError("title") ? "error" : ""}
                                     help={this.field.getError("title") ? this.props.intl.messages['projects.placeholder.applicationName'] : ""}
                                     required
                                     label={this.props.intl.messages['projects.text.applicationName']}>
                                {titleOpr()}

                            </FormItem>

                            <FormItem{...formItemLayout}
                                     label={this.props.intl.messages['projects.text.createTime']}>
                                <div className="form-item-text">{this.state.appBasicData.ctime}</div>
                            </FormItem>

                            <FormItem  {...formItemLayout}
                                       label={this.props.intl.messages['projects.text.applicationDescription']}>
                                {opr()}
                            </FormItem>

                        </Form>
                    </Card>


                    <Card
                        className="url-card"
                        title={this.props.intl.messages['projects.text.urlInfo']}
                        bodyHeight="45%"
                        subTitle={
                            <div className="sub-title">
                                <Button type="primary"
                                        size="small"
                                        className={this.state.urlEditMode === true ? "edit-button hide" : "edit-button"}
                                        onClick={this.urlEdit.bind(this)}>
                                    {this.props.intl.messages['projects.button.edit']}
                                </Button>
                                <Button onClick={this.urlConfirm.bind(this)}
                                        type="primary"
                                        size="small"
                                        className={this.state.urlEditMode === true ? "save-button" : "save-button hide"}
                                >
                                    {this.props.intl.messages['projects.button.Save']}
                                </Button>
                                <Button
                                    size="small"
                                    className={this.state.urlEditMode === true ? "cancel-button" : "cancel-button hide"}
                                    onClick={this.urlEditCancel.bind(this)}>    {this.props.intl.messages['projects.button.cancel']}</Button>
                            </div>}
                    >
                        <Form labelAlign={"left"}
                              className="card-form">
                            {urlFormRender()}
                        </Form>
                    </Card>


                </div>
            </Loading>)
    }
}

export default injectIntl(ApplicationBasicInfo)
