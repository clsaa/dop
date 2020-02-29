/* eslint-disable react/no-unused-state, no-plusplus */
import React, { Component } from 'react';
import {Table, Switch, Icon, Button, Grid, Pagination, Dialog, Select, Input, Feedback} from '@icedesign/base';
import IceContainer from '@icedesign/container';
import API from "../../../API";
import Axios from "axios";
import {Link} from "react-router-dom";
import {FormBinder, FormBinderWrapper} from "@icedesign/form-binder";
import {withRouter} from "react-router-dom";
import Balloon from "@alifd/next/lib/balloon";
import {injectIntl} from "react-intl";

const { Row, Col } = Grid;
const Toast = Feedback.toast;

class GroupTable extends Component {
    static displayName = 'CustomTable';

    static propTypes = {};

    static defaultProps = {};

    constructor(props) {
        super(props);
        this.state = {
            formValue: {},
            current: 1,
            createdCaseNeedRefresh: false,
            createManualDialogVisiable: false,
            isSubmit: false,
            total: 1,
            currentData: [{}],
            searchValue: {
                owner: '',
                type: 'interface',
                group: '',
                result: '',
                cuser: ''
            },
        };

        this.handlePaginationChange = this.handlePaginationChange.bind(this);
        this.refreshList(1);
    }

    formChange = (value) => {
        console.log('changed value', value);
        this.setState({
            formValue: value,
        });
    };

    onChange = (...args) => {

    };

    handlePaginationChange = (current) => {
        this.refreshList(current);
    };

    refreshList(current) {
        if (!current) {
            current = 1;
        }
        let url = API.test + '/group/page';
        let _this = this;
        Axios.get(url, {
            params: {
                pageSize: 10,
                pageNo: current
            }
        }).then(function (response) {
            _this.setState({
                current: current,
                total: response.data.totalCount,
                currentData: response.data.pageList
            });
        }).catch(function (error) {
            console.log(error);
        });
    }

    renderOper = (value, index, record) => {
        let MoveTarget = <Icon
            type="search"
            size="small"
            style={{...styles.icon, ...styles.deleteIcon}}
            onClick={() => {
                this.props.history.push('/test/groupLogs/' + record.id);
            }}
        />;

        let edit = <Icon
            type="edit"
            size="small"
            style={{...styles.icon, ...styles.editIcon}}
            onClick={() => {
                this.props.history.push('/test/editGroups/' + record.id);
            }}
        />;
        return (
            <div style={styles.oper}>
                <Balloon.Tooltip trigger={edit} triggerType="hover" align='l'>
                    {this.props.intl.messages['test.createGroup.edit']}
                </Balloon.Tooltip>
                <Balloon.Tooltip trigger={MoveTarget} triggerType="hover" align='r'>
                    {this.props.intl.messages['test.createGroup.logs']}
                </Balloon.Tooltip>
            </div>
        );
    };


    renderSwitch = (value,index,record) => {
        let groupId = record.id;
        let _this = this;
        return <Switch onChange={(checked) => {
            if (checked) {
                Toast.success(_this.props.intl.messages['test.createGroup.execute.message']);
                this.execute(groupId);
            }
        }
        }/>;
    };

    execute = (id) => {
        // only interface script is executable
        let url = API.test + '/group/execute/' + id;
        Axios.get(url).then(function (response) {
            console.log(response);
        }).catch(function (error) {
            console.log(error);
        });
    };

    renderWay = (value, index, record) => {
        let way = record.executeWay;
        if (way === 'SERIAL') return this.props.intl.messages['test.createGroup.executeWay.serial'];
        else return this.props.intl.messages['test.createGroup.executeWay.parallel'];
    };

    onOpen = () =>{
        this.props.history.push('/test/createGroup');
    };

    onClose = () =>{
        this.setState({
            createManualDialogVisiable: false,
            isSubmit: false
        })
    };

    onOk = ()=>{
        this.setState({
            isSubmit: true
        })
    };

    render() {
        const {searchValue} = this.state.searchValue;

        return (
            <div>
                <IceContainer title={this.props.intl.messages['test.createGroup.search.title']}>
                    <FormBinderWrapper value={this.state.searchValue} onChange={this.formChange}>
                        <Row wrap>
                            <Col xxs="24" l="8" style={styles.formCol}>
                                <span style={styles.label}>{this.props.intl.messages['test.createGroup.search.title']}</span>
                                <FormBinder name="cuser">
                                    <Input />
                                </FormBinder>
                            </Col>

                            <Col xxs="24" l="8" style={styles.formCol}>
                                <span style={styles.label}>{this.props.intl.messages['test.createGroup.search.creator']}</span>
                                <FormBinder name="cuser">
                                    <Input />
                                </FormBinder>
                            </Col>
                            <Col xxs="24" l="8" style={styles.formCol}>
                                <Button type="primary" style={styles.submitButton}>
                                    {this.props.intl.messages['test.exeLogs.search']}
                                </Button>
                            </Col>

                        </Row>
                    </FormBinderWrapper>
                </IceContainer>

                <IceContainer title={this.props.intl.messages['test.createGroup.table.title']}>
                    <Row wrap style={styles.headRow}>
                        <Col l="12">
                            <Button style={styles.button} onClick={this.onOpen.bind(this)} >
                                <Icon type="add" size="xs" style={{ marginRight: '4px' }} />{this.props.intl.messages['test.createGroup.table.createGroup']}
                            </Button>

                        </Col>
                        <Col l="12" style={styles.center}>
                            <Button type="normal" style={styles.button}>
                                {this.props.intl.messages['test.createGroup.table.delete']}
                            </Button>
                        </Col>
                    </Row>

                    <Table
                        dataSource={this.state.currentData}
                        rowSelection={{ onChange: this.onChange }}
                    >
                        <Table.Column title={this.props.intl.messages['test.createGroup.table.id']} dataIndex="id" width={100} />
                        <Table.Column title={this.props.intl.messages['test.createGroup.table.groupName']} dataIndex="groupName" width={100} />
                        <Table.Column title={this.props.intl.messages['test.createGroup.table.comment']} dataIndex="comment" width={100} />
                        <Table.Column title={this.props.intl.messages['test.createGroup.table.executeWay']} width={100} cell={this.renderWay}/>
                        <Table.Column title={this.props.intl.messages['test.createGroup.table.creator']} dataIndex="createUserName" width={100} />
                        <Table.Column title={this.props.intl.messages['test.createGroup.table.caseCount']} dataIndex="caseCount" width={100} />
                        <Table.Column title={this.props.intl.messages['test.createGroup.table.execute']} width={100} cell={this.renderSwitch} />
                        <Table.Column title={this.props.intl.messages['test.createGroup.table.operations']} width={100} cell={this.renderOper} />
                    </Table>
                    <Pagination
                        style={styles.pagination}
                        current={this.state.current}
                        onChange={this.handlePaginationChange}
                        total={this.state.total}
                    />
                </IceContainer>
            </div>
        );
    }
}

const styles = {
    headRow: {
        marginBottom: '10px',
    },
    icon: {
        color: '#2c72ee',
        cursor: 'pointer',
    },
    deleteIcon: {
        marginLeft: '20px',
    },
    center: {
        textAlign: 'right',
    },
    button: {
        borderRadius: '4px',
        color: '#5485F7'
    },
    pagination: {
        marginTop: '20px',
        textAlign: 'right',
    },
    formRow: {
        marginBottom: '18px',
    },
    formCol: {
        display: 'flex',
        alignItems: 'center',
        marginBottom: '20px',
    },
    label: {
        lineHeight: '28px',
        paddingRight: '10px',
    },
};

export default injectIntl(withRouter(GroupTable));
