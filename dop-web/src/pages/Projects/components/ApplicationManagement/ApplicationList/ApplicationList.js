import React, {Component} from 'react';
import {Dialog, Feedback, Grid, Icon, Table} from '@icedesign/base';
import API from "../../../../API.js"
import {Col} from "@alifd/next/lib/grid";
import Axios from "axios";
import {Link} from "react-router-dom";
import "./ApplicationList.scss"
import {injectIntl} from "react-intl";

const {Row} = Grid;
const Toast = Feedback.toast;

/**
 * 展示应用的列表
 * @author Bowen
 **/

class ApplicationList extends Component {

    static displayName = 'ApplicationList';


    constructor(props) {
        super(props);

        console.log("applicationList", props)
        //接受来自分页器的参数即当前页数据
        this.state = {
            projectId: props.projectId,
            isVisible: false,
            currentData: props.currentData,
            deletedCallRefresh: props.deletedCallRefresh,
            searchKey: props.searchKey === undefined ? "" : props.searchKey
        };
        console.log("constructor");

    }

    //接受来自分页器的参数即当前页数据
    componentWillReceiveProps(nextProps, nextContext) {
        this.setState({
            currentData: nextProps.currentData
        });
    }

    popupConfirm = (id) => {
        console.log(id)
        Dialog.confirm({
            language:'en-us',
            content: this.props.intl.messages['projects.text.deleteConfirmApplication'],
            title: this.props.intl.messages['projects.text.deleteConfirm'],
            onOk: this.onDelete.bind(this, id)
        });
    };

    //删除按钮响应函数
    onDelete = (id) => {
        let url = API.application + '/app/' + id;
        let _this = this;
        Axios.delete(url)
            .then(function (response) {
                    Toast.success(_this.props.intl.messages['projects.text.deleteSuccessful'])
                    _this.state.deletedCallRefresh();
                }
            )
            .catch(function (error) {
                console.log(error);
            });


    }

    //渲染表格中的ID项，添加超链接至该项目ID下的应用ID列表
    nameRender(value, index, record) {
        let _this = this
        return <Link to={"/applicationDetail?appId=" + record.id + "&projectId=" + _this.state.projectId}
        >{value}</Link>
    }

    render() {
        const renderOpr = (value, index, record) => {
            console.log("record:", record, value)

            return <div>{record.ctime}
                <Icon onClick={this.popupConfirm.bind(this, record.id)} type="ashbin" className="delete-icon"/>

            </div>
        }

        return (
            <Row wrap gutter="20">
                <Col>
                    <Table
                        dataSource={this.state.currentData}
                        language="en-us"
                    >
                        <Table.Column
                            title={this.props.intl.messages['projects.text.id']}
                            dataIndex="id"/>

                        <Table.Column title={this.props.intl.messages['projects.text.applicationName']}
                                      cell={this.nameRender.bind(this)}
                                      dataIndex="title"/>

                        <Table.Column title={this.props.intl.messages['projects.text.Owner']}
                                      dataIndex="ouserName"/>

                        <Table.Column cell={renderOpr}
                                      title={this.props.intl.messages['projects.text.createTime']}
                                      dataIndex="ctime"/>
                    </Table>
                </Col>
            </Row>
        );
    }

}

export default injectIntl(ApplicationList)
