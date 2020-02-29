import {Breadcrumb, Button, Dialog, Feedback, Icon, Table} from "@icedesign/base";
import AddMemberList from "../AddMemberList/AddMemberList";
import {injectIntl} from "react-intl";
import React, {Component} from 'react';
import API from "../../../../API";
import Axios from "axios/index";
import TopBar from "../ProjectBasicInfo/topbar";
import "./ManageMember.scss"

const Toast = Feedback.toast;

class ManageMember extends Component {

    constructor(props) {
        super(props);
        this.state = {
            addMemberListVisible: false,
            projectId: props.projectId,
            memberData: [],
            memberDataLoading: false
        }
    }

    nameRender(value, index, record) {
        console.log("nameRender", value, index, record)
        console.log(String(record.id) === String(window.sessionStorage.getItem("user-id")) ? "delete-icon hide" : "delete-icon")
        return (

            <div className="name-render-div">{value}
                <Icon
                    className={String(record.id) === String(window.sessionStorage.getItem("user-id")) ? "delete-icon hide" : "delete-icon"}
                    onClick={this.popupConfirm.bind(this, record.id)}
                    type="ashbin"/></div>
        )

    }

    getMemberData() {
        this.setState({
            memberDataLoading: true
        })
        let _this = this
        let url = API.application + "/project/" + this.state.projectId + "/members"
        Axios.get(url, {
            params: {
                organizationId: "1"
            }
        })
            .then((response) => {
                console.log("then", response)
                _this.setState({
                    memberData: response.data,
                    memberDataLoading: false,
                    addMemberListVisible: false
                })
            })
            .catch((response) => {
                console.log("catch", response)
                _this.setState({
                    memberDataLoading: false,
                    addMemberListVisible: false
                })
            })
    }

    onDelete(id) {
        let _this = this
        let deleteUrl = API.application + "/project/" + this.state.projectId + "/members"
        Axios.delete(deleteUrl, {
            params: {
                userId: id,
                organizationId: 123,

            }
        })
            .then((response) => {
                    Toast.success("删除成功")
                _this.getMemberData()
                }
            )
            .catch((response) => {
                console.log(response)
            })

    }

    popupConfirm(id) {

        console.log(id)
        Dialog.confirm({
            content: "你确定要删除该成员吗？",
            title: "确认删除",
            onOk: this.onDelete.bind(this, id)
        });
    }

    onClose = () => {
        this.setState({
            visible: false
        });
    };

    componentDidMount() {
        this.getMemberData()
    }

    addMember() {
        this.setState({
            addMemberListVisible: true
        })

    }

    render() {
        return (
            <div>
                <TopBar
                    extraBefore={<Breadcrumb>
                        <Breadcrumb.Item
                            link="#/project">{this.props.intl.messages['projects.bread.allProject']}</Breadcrumb.Item>
                        <Breadcrumb.Item
                            link={"#/projectDetail?projectId=" + this.state.projectId}>{this.props.intl.messages['projects.bread.project'] + this.state.projectId}</Breadcrumb.Item>
                    </Breadcrumb>}
                    extraAfter={
                        <div>
                            <Button onClick={this.addMember.bind(this)} type="primary"
                                    disabled={this.state.memberDataLoading}>
                                {this.props.intl.messages["projects.text.addMember"]}
                            </Button>
                            <AddMemberList
                                getMemberData={this.getMemberData.bind(this)}
                                memberList={this.state.memberData}
                                visible={this.state.addMemberListVisible}
                                projectId={this.state.projectId}/>
                        </div>}/>

                <Table
                    isLoading={this.state.memberDataLoading}
                    dataSource={this.state.memberData}>
                    <Table.Column cell={this.idRender}
                                  title={this.props.intl.messages['projects.text.dialog.memberId']}
                                  dataIndex="id"/>

                    <Table.Column title={this.props.intl.messages['projects.text.dialog.memberName']}
                                  dataIndex="name"
                                  cell={this.nameRender.bind(this)}/>


                </Table>
            </div>
        )
    }

}

export default injectIntl(ManageMember)