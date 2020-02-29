import React, {Component} from 'react';
import Axios from "axios";
import API from "../../../../API";
import {Breadcrumb, Dialog, Feedback, Field, Form, Icon, Loading, Table} from '@icedesign/base';
import CreateApplicationVariableDialog from "../CreateApplicationVariable/CreateApplicationVariable";
import Input from "@icedesign/base/lib/input";
import TopBar from "./topbar";
import "./ApplicationVariable.scss"
import {injectIntl} from "react-intl";

const FormItem = Form.Item;
const Toast = Feedback.toast;
/**
 * 应用变量管理页面
 * @author Bowen
 **/
class ApplicationVariable extends Component {
    static displayName = 'ApplicationVariable';

    constructor(props) {
        super(props);

        this.field = new Field(this);
        this.state = {
            appId: props.appId,
            varData: [],
            editMode: false,
            loading: true,
            projectId: props.projectId

        }
    }

//刷新变量列表
    refreshApplicationVariableList() {
        this.getApplicationVariableData();
    }

    //获取变量列表的值
    getApplicationVariableData() {
        this.setState({
            loading: true
        })
        let getUrl = API.gateway + "/application-server/app/" + this.state.appId + "/variable";
        let _this = this;
        Axios.get(getUrl)
            .then(function (response) {

                _this.setState({
                    varData: response.data,
                    loading: false,


                })
                let list = {}
                list = response.data.map((item) => {

                    return list[item.varKey] = false
                })
                _this.setState({
                    editMode: list
                })

            })
            .catch((response) => {
                _this.setState({
                    loading: false
                })
            })

    }

    componentDidMount() {
        this.getApplicationVariableData();
    }

    deleteConfirm = (id) => {
        console.log(id)
        Dialog.confirm({
            content: this.props.intl.messages['projects.text.deleteConfirmVar'],
            title: this.props.intl.messages['projects.text.deleteConfirm'],
            onOk: this.onDelete.bind(this, id)
        });
    };

//删除按钮响应函数
    onDelete(id) {
        let deleteUrl = API.gateway + "/application-server/app/variable/" + id;
        let _this = this;
        this.setState({
            loading: true
        })

        Axios.delete(deleteUrl)
            .then(function (response) {
                _this.setState({
                    loading: false
                })
                Toast.success(_this.props.intl.messages['projects.text.deleteSuccessful'])
                _this.refreshApplicationVariableList();
            })
            .catch((response) => {
                _this.setState({
                    loading: false
                })
            })
    }

    submitConfirm = (id) => {
        console.log(id)
        Dialog.confirm({
            content: this.props.intl.messages['projects.text.confirmSave'],
            title: this.props.intl.messages['projects.title.confirmSave'],
            onOk: this.onSubmit.bind(this, id)
        });
    };

//{this.props.intl.messages['projects.button.Save']}按钮响应函数
    onSubmit(id) {
        let putUrl = API.gateway + "/application-server/app/variable/" + id;
        let _this = this;
        this.setState({
            loading: true
        })
        Axios.put(putUrl, {
            varValue: _this.field.getValue(id)
        })
            .then(function (response) {
                _this.setState({
                    loading: false
                })
                Toast.success(_this.props.intl.messages['projects.text.updateSuccessful'])
                _this.refreshApplicationVariableList();
            })
            .catch((response) => {
                _this.setState({
                    loading: false
                })
            })
    }

    //{this.props.intl.messages['projects.button.cancel']}按钮响应函数
    onCancel(name) {
        let temp = this.state.editMode
        temp[name] = false
        this.setState({
            editMode: temp
        })
    }

    //编辑图标响应函数
    onEdit(name) {
        let temp = this.state.editMode
        temp[name] = true
        this.setState({
            editMode: temp
        })
    }


    render() {
        const {init} = this.field;
        const keyRender = (value, index, record) => {
            console.log(record, value);
            return <div>
                <div className="key-text">{record.varKey}</div>
                <Icon
                    className="key-icon"
                    onClick={this.deleteConfirm.bind(this, record.id)}
                      type="ashbin"
                />
            </div>
        }
        const valueRender = (value, index, record) => {
            console.log(record);


            if (this.state.editMode[record.varKey]) {
                return <div>
                    <Form>
                        <FormItem>
                            <Input {...init(record.id)} placeholder="Value"/>
                        </FormItem>
                    </Form>
                    <div
                        onClick={this.submitConfirm.bind(this, record.id)}>{this.props.intl.messages['projects.button.Save']}</div>
                    <div
                        onClick={this.onCancel.bind(this, record.varKey)}>{this.props.intl.messages['projects.button.cancel']}</div>
                </div>
            } else {
                return <div>
                    <div className="value-text">******</div>
                    <Icon type="edit"
                          className="value-icon"
                          onClick={this.onEdit.bind(this, record.varKey)}
                    />
                </div>
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
                    extraAfter={<CreateApplicationVariableDialog

                        refreshApplicationVariableList={this.refreshApplicationVariableList.bind(this)}
                        appId={this.state.appId}/>}
                />
                <div className="table-container">
                    <Table className="table"
                               dataSource={this.state.varData}>
                            <Table.Column cell={keyRender}
                                          title={this.props.intl.messages['projects.text.Key']}
                                          dataIndex="key"/>
                            <Table.Column cell={valueRender}
                                          title={this.props.intl.messages['projects.text.Value']}/>
                        </Table>

                </div>
            </Loading>
        )
    }


}

export default injectIntl(ApplicationVariable)