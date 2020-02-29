import React, { Component } from 'react';
import {Table, Pagination, Icon, Input, Button} from '@icedesign/base';
import API from "../../../../API";
import Axios from "axios";
import Balloon from "@alifd/next/lib/balloon";
import Dialog from "@alifd/next/lib/dialog";
import {Col, Row} from "@alifd/next/lib/grid";
import IcePanel from '@icedesign/panel';
import {injectIntl} from "react-intl";

class Home extends Component {
  static displayName = 'Home';

  constructor(props) {
    super(props);
    this.state = {
      current: 1,
      currentData: [],
      total: 0,
      caseId: this.props.caseId,
      showDetailLog: false,
      detailLogData: [],
    };

    this.handlePagination = this.handlePagination.bind(this);
    this.refreshList(1);
  }

  refreshList(current) {
    if (!current) {
      current = 1;
    }
    let url = API.test + '/interfaceCases/logs/page/' + this.state.caseId;
    let _this = this;
    Axios.get(url, {
      params: {
        pageSize: 10,
        pageNo: current
      }
    }).then(function (response) {
      console.log(response);
      _this.setState({
        current: current,
        total: response.data.totalCount,
        currentData: response.data.pageList,
      });
    }).catch(function (error) {
      console.log(error);
    });
  }

  handlePagination = (current) => {
    this.refreshList(current);
  };

  handleSort = (dataIndex, order) => {
    const dataSource = this.state.currentData.sort((a, b) => {
      const result = a[dataIndex] - b[dataIndex];
      if (order === 'asc') {
        return result > 0 ? 1 : -1;
      }
      return result > 0 ? -1 : 1;
    });

    this.setState({
      dataSource,
    });
  };

  renderState = (value, index, record) => {
    if (value) {
      return (
          <div style={styles.state}>
            <span style={styles.circle} />
            <span style={styles.stateText}>{this.props.intl.messages['test.exeLogs.success']}</span>
          </div>
      );
    }else {
      return (
          <div style={styles.state}>
            <span style={styles.circleFail} />
            <span style={styles.stateTextFail}>{this.props.intl.messages['test.exeLogs.fail']}</span>
          </div>
      )
    }
  };

  renderTime = (value, index, record) => {
    return value.replace('T', ' ');
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
    return (
        <Balloon.Tooltip trigger={MoveTarget} triggerType="hover" align='l'>
          {this.props.intl.messages['test.exeLogs.detail']}
        </Balloon.Tooltip>
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
      return 'info';
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

  render() {
    return (
      <div style={styles.tableContainer}>
        <Table
          dataSource={this.state.currentData}
          onSort={this.handleSort}
          hasBorder={false}
          className="custom-table"
        >
          <Table.Column
            width={100}
            lock="left"
            title={this.props.intl.messages['test.exeLogs.table.id']}
            dataIndex="id"
            sortable
            align="center"
          />
          <Table.Column width={200} title={this.props.intl.messages['test.exeLogs.table.begin']} dataIndex="begin" cell={this.renderTime}/>
          <Table.Column width={200} title={this.props.intl.messages['test.exeLogs.table.end']} dataIndex="end" cell={this.renderTime}/>
          <Table.Column
            width={200}
            title={this.props.intl.messages['test.exeLogs.table.testManager']}
            dataIndex="createUserName"
          />
          <Table.Column
            width={200}
            title={this.props.intl.messages['test.exeLogs.table.status']}
            dataIndex="success"
            cell={this.renderState}
          />
          <Table.Column
            width={100}
            title={this.props.intl.messages['test.exeLogs.table.detail']}
            cell={this.renderOper}
            lock="right"
            align="center"
          />
        </Table>
        <Pagination
          style={styles.pagination}
          current={this.state.current}
          onChange={this.handlePagination}
          total={this.state.total}
        />

        <Dialog title={this.props.intl.messages['test.exeLogs.table.detail.table']}
                visible={this.state.showDetailLog}
                // isFullScreen
                shouldUpdatePosition
                style={{width: '1400px'}}
                onOk={this.onClose}
                onCancel={this.onClose}
                onClose={this.onClose}>

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

export default injectIntl(Home);

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
