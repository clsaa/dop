import {Dialog, Feedback, Input, Pagination, Table} from "@icedesign/base";
import React, {Component} from 'react'
import Axios from "axios";
import API from "../../../../API";
import {injectIntl} from "react-intl";
import "./AddMemberList.scss"

const Toast = Feedback.toast

class AddMemberList extends Component {
    constructor(props) {
        super(props);
        console.log("props", props)
        this.state = {
            visible: props.visible,
            loading: true,
            projectId: props.projectId,
            current: 1,
            totalCount: 1,
            getMemberData: props.getMemberData,
            memberList: props.memberList,
            rowSelection: {
                onChange: this.onChange.bind(this),
                // onSelect: function (selected, record, records) {
                //                 //     console.log("onSelect", selected, record, records);
                //                 // },
                //                 // onSelectAll: function (selected, records) {
                //                 //     console.log("onSelectAll", selected, records);
                //                 // },
                selectedRowKeys: [],
                getProps: (record) => {
                    for (let i = 0; i < this.state.memberList.length; i++) {
                        console.log("item", this.state.memberList[i])
                        if (record.id === this.state.memberList[i].id)
                            return {disabled: true}
                    }
                }
            },
            dataSource: []
        };
    }

    componentDidMount() {
        this.getData(this.state.current, this.state.queryKey)
    }

    componentWillReceiveProps(nextProps, nextContext) {
        console.log(nextProps)
        this.setState({
            memberList: nextProps.memberList,
            visible: nextProps.visible,
        })
    }


    getData(current, queryKey) {
        this.setState({
            loading: true
        })
        let url = API.application + "/all_users"
        let _this = this
        Axios.get(url, {
            params: {
                key: queryKey,
                pageNo: current,
                pageSize: 10
            }
        })
            .then((response) => {

                console.log("datasource", response)
                _this.setState({
                    current: current,
                    queryKey: queryKey,
                    loading: false,
                    dataSource: response.data.pageList,
                    totalCount: response.data.totalCount
                })
            })
            .catch((response) => {
                _this.setState({
                    loading: false
                })
            })
    }

    popupConfirm() {

        console.log()
        Dialog.confirm({
            content: this.props.intl.messages["projects.text.addConfirm"],
            title: this.props.intl.messages["projects.text.titleAddConfirm"],
            onOk: this.onAdd.bind(this),
            onCancel: this.onCancelConfirm.bind(this)
        });
    }

    onCancelConfirm() {
        this.setState({
            visible: true
        })
    }

    onAdd() {
        this.setState({
            visible: true,
            loading: true
        })
        let addUrl = API.application + "/project/" + this.state.projectId + "/members?"
        let param = "organizationId=" + 1
        let userListParam = "";
        for (let id in this.state.rowSelection.selectedRowKeys) {

            userListParam = userListParam + ("&userIdList=" + this.state.rowSelection.selectedRowKeys[id])
        }

        addUrl = addUrl + param + userListParam
        console.log(userListParam, addUrl)
        let _this = this
        let row = this.state.rowSelection
        row.selectedRowKeys = []
        console.log(this.state.rowSelection.selectedRowKeys)
        Axios.post(addUrl
            , {}, {
                dataType: "json",
                params: {
                    organizationId: 1
                }
            }
        )
            .then((response) => {
                Toast.success(_this.props.intl.messages['projects.text.addSuccessful'])
                _this.setState({
                    loading: false,
                    visible: false,
                    rowSelection: row
                })
                _this.state.getMemberData()
            })
            .catch((response) => {
                _this.setState({
                    loading: false,
                    visible: false,
                    rowSelection: row
                })
            })

    }

    onSearch(value) {

        this.getData(1, value)
    }

    render() {
        return (
            <Dialog visible={this.state.visible}
                    onOk={this.onOk}
                    onCancel={this.onClose}
                    onClose={this.onClose}
                    className="add-member-list-dialog"
                    locale={{
                        ok: this.props.intl.messages["projects.button.confirm"],
                        cancel: this.props.intl.messages["projects.button.cancel"]
                    }}
            >
                <p>
                    <Input

                        onChange={this.onSearch.bind(this)}
                        placeholder={this.props.intl.messages["projects.search.member"]}/>
                </p>
                <Table

                    dataSource={this.state.dataSource}
                    isLoading={this.state.loading}
                    rowSelection={this.state.rowSelection}
                >
                    <Table.Column title={this.props.intl.messages["projects.text.id"]} dataIndex="id"/>
                    <Table.Column title={this.props.intl.messages["projects.text.dialog.memberName"]} dataIndex="name"/>
                </Table>
                <Pagination
                    locale={{
                        prev: this.props.intl.messages["projects.text.prev"],
                        next: this.props.intl.messages["projects.text.next"],
                        goTo: this.props.intl.messages["projects.text.goto"],
                        page: this.props.intl.messages["projects.text.page"],
                        go: this.props.intl.messages["projects.text.go"],
                    }}
                    current={this.state.current}
                    onChange={this.handleChange.bind(this)}
                    pageSize={10}
                    total={this.state.totalCount}/>
            </Dialog>
        );
    }

    handleChange(current) {

        this.getData(current, this.state.queryKey)

    }

    onClose = () => {
        this.setState({
            visible: false
        });
    };
    onOk = () => {
        this.popupConfirm()
        this.setState({
            visible: false
        });
    };

    onChange(ids, records) {
        let {rowSelection} = this.state;
        rowSelection.selectedRowKeys = ids;
        console.log("onChange", ids, records);
        this.setState({rowSelection});
    }

}

export default injectIntl(AddMemberList)