import React, { Component } from 'react';
import {Table, Pagination, Icon, Input, Button} from '@icedesign/base';
import Axios from "axios";
import Balloon from "@alifd/next/lib/balloon";
import Dialog from "@alifd/next/lib/dialog";
import {Col, Row} from "@alifd/next/lib/grid";
import IcePanel from '@icedesign/panel';
import IceContainer from '@icedesign/container';
import {withRouter} from "react-router-dom";
import API from "../../API";
import {injectIntl} from "react-intl";

class DetailCaseLogTable extends Component {
    static displayName = 'DetailCaseLogTable';

    constructor(props) {
        super(props);
        this.state = {
            current: 1,
            currentData: [],
            showDetailLog: false,
            detailLogData: [],
            groupLogId: this.props.match.params.groupLogId,
            groupId: this.props.match.params.groupId
        };

        this.getDetailData();
    }

    getDetailData() {
        let url = API.test + "/group/logs/" + this.state.groupLogId;
        let _this = this;
        Axios.get(url).then(function (response) {
            _this.setState({
                currentData: response.data.logs
            });
        }).catch(function (error) {
            console.log(error);
        });
    }

    renderState = (value, index, record) => {
        if (value) {
            return (
                <div style={styles.state}>
                    <span style={styles.circle} />
                    <span style={styles.stateText}>{this.props.intl.messages['test.exeLogs.search.status.success']}</span>
                </div>
            );
        }else {
            return (
                <div style={styles.state}>
                    <span style={styles.circleFail} />
                    <span style={styles.stateTextFail}>{this.props.intl.messages['test.exeLogs.search.status.fail']}</span>
                </div>
            )
        }
    };

    renderType = (value, index, record) => {
        if (value === 'INTERFACE') {
            return this.props.intl.messages['test.caseLists.table.type.interface'];
        }else {
            return this.props.intl.messages['test.caseLists.table.type.manual'];
        }
    };

    renderOper = (value, index, record) => {
        let MoveTarget = <Icon
            type="search"
            size="small"
            style={styles.icon}
            onClick={() => {
                this.setState({
                    showDetailLog: true,
                    detailLogData: record.operationExecuteLogs
                })
            }
            }
        />;

        let info = <Icon
            type="edit"
            size="small"
            style={{...styles.icon, marginRight: '10px'}}
            onClick={() => {
                this.props.history.push('/test/editCases/' + record.caseId);
            }}
        />;

        return (
            <div>
                <Balloon.Tooltip trigger={info} triggerType="hover" align='l'>
                    {this.props.intl.messages['test.groupLogs.detail.view']}
                </Balloon.Tooltip>

                <Balloon.Tooltip trigger={MoveTarget} triggerType="hover" align='lt'>
                    {this.props.intl.messages['test.groupLogs.detail.viewLog']}
                </Balloon.Tooltip>
            </div>
        );
    };


    onOk = () => {

    };

    onClose = () => {
        this.setState({
            showDetailLog: false
        })
    };

    renderOperationLog = () => {

    };

    panel = (type) => {
        if (type === 'REQUEST') {
            return 'primary';
        }
        if (type === 'WAIT') {
            return 'danger';
        }
    };

    renderHeader = (operationLog) => {
        let stage = this.props.intl.messages['test.exeLogs.stage.prepare'];
        let stageEnum = operationLog.stage;
        if (stageEnum === 'TEST') {
            stage = this.props.intl.messages['test.exeLogs.stage.test'];
        }
        if (stageEnum === 'DESTROY') {
            stage = this.props.intl.messages['test.exeLogs.stage.destroy'];
        }

        let begin = operationLog.begin.replace('T', ' ');
        let end = operationLog.end.replace('T', ' ');
        return stage + ' ' + operationLog.operationType + ' ' + begin + ' to ' + end;
    };

    onBack() {
        this.props.history.push('/test/groupLogs/' + this.state.groupId);
    }

    render() {
        return (
            <div style={styles.tableContainer}>
                <IceContainer>
                    <Button style={styles.button} onClick={this.onBack.bind(this)} >
                        <Icon type="arrow-left" size="xs" style={{ marginRight: '4px' }} />{this.props.intl.messages['test.groupLogs.detail.back']}
                    </Button>
                </IceContainer>

                <Table
                    dataSource={this.state.currentData}
                    // onSort={this.handleSort}
                    hasBorder={false}
                    className="custom-table"
                >
                    <Table.Column
                        width={150}
                        lock="left"
                        title={this.props.intl.messages['test.caseLists.table.type']}
                        dataIndex="caseType"
                        align="center"
                        cell={this.renderType}
                    />
                    <Table.Column
                        width={200}
                        // lock="left"
                        title={this.props.intl.messages['test.caseLists.table.name']}
                        dataIndex="caseName"
                        align="center"
                    />
                    <Table.Column width={200} title={this.props.intl.messages['test.exeLogs.table.begin']} dataIndex="begin"/>
                    <Table.Column width={200} title={this.props.intl.messages['test.exeLogs.table.end']} dataIndex="end"/>
                    <Table.Column
                        width={200}
                        title={this.props.intl.messages['test.exeLogs.table.testManager']}
                        dataIndex="createUserName"
                    />
                    <Table.Column
                        width={100}
                        title={this.props.intl.messages['test.exeLogs.table.status']}
                        dataIndex="success"
                        cell={this.renderState}
                    />
                    <Table.Column
                        width={200}
                        title={this.props.intl.messages['test.createGroup.table.operations']}
                        cell={this.renderOper}
                        lock="right"
                        align="center"
                    />
                </Table>

                <Dialog title={this.props.intl.messages['test.exeLogs.table.detail.title']}
                        visible={this.state.showDetailLog}
                        // isFullScreen
                        shouldUpdatePosition
                        style={{width: '1400px'}}
                        onOk={this.onClose}
                        onCancel={this.onClose}
                        onClose={this.onClose}
                        locale={{
                            'ok': 'Confirm',
                            'cancel': 'Cancel'
                        }}
                >

                    {this.state.detailLogData.map((operationLog, index) => {
                        return (
                            <IcePanel status={this.panel(operationLog.operationType)} style={{marginBottom: '10px'}} key={index}>
                                <IcePanel.Header>
                                    <Row>
                                        <Col span='23'>
                                            {this.renderHeader(operationLog)}
                                        </Col>
                                        <Col span='1'>
                                        </Col>
                                    </Row>
                                </IcePanel.Header>
                                <IcePanel.Body>
                                    <Row>
                                        <Col span="22" style={{whiteSpace: 'pre-line'}}>
                                            {operationLog.executeInfo}
                                        </Col>
                                        <Col span="2">

                                        </Col>
                                    </Row>
                                </IcePanel.Body>
                            </IcePanel>
                        );
                    })}

                </Dialog>
            </div>
        );
    }
}

const styles = {
    icon: {
        color: '#2c72ee',
        cursor: 'pointer',
    },
    tableContainer: {
        background: '#fff',
        paddingBottom: '10px',
    },
    pagination: {
        margin: '20px 0',
        textAlign: 'center',
    },
    editIcon: {
        color: '#999',
        cursor: 'pointer',
    },
    circle: {
        display: 'inline-block',
        background: '#28a745',
        width: '8px',
        height: '8px',
        borderRadius: '50px',
        marginRight: '4px',
    },
    stateText: {
        color: '#28a745',
    },
    circleFail: {
        display: 'inline-block',
        background: '#e72b00',
        width: '8px',
        height: '8px',
        borderRadius: '50px',
        marginRight: '4px',
    },
    stateTextFail: {
        color: '#e72b00',
    },
};

export default injectIntl(withRouter(DetailCaseLogTable));
