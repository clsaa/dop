/* eslint-disable react/no-unused-state, no-plusplus */
import React, { Component } from 'react';
import {Table, Switch, Icon, Button, Grid, Pagination, Dialog, Select, Input, Feedback} from '@icedesign/base';
import IceContainer from '@icedesign/container';
import CreateManualCaseFrom from "../CreateTestCases/CreateManualCaseForm";
import API from "../../../API";
import Axios from "axios";
import {Link} from "react-router-dom";
import {FormBinder, FormBinderWrapper} from "@icedesign/form-binder";
import {withRouter} from "react-router-dom";
import Balloon from "@alifd/next/lib/balloon";
import {injectIntl, FormattedMessage} from 'react-intl';

const { Row, Col } = Grid;
const Toast = Feedback.toast;

class CustomTable extends Component {
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
      currentData: [{"id": "1"}],
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
    let url = API.test + '/interfaceCases/page';
    if (this.state.searchValue.type === 'manual') {
      url = API.test + '/manualCases/page';
    }
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
          this.props.history.push('/test/showExecuteLogs/' + record.id);
        }}
    />;

    let edit = <Icon
        type="edit"
        size="small"
        style={{...styles.icon, ...styles.editIcon}}
        onClick={() => {
          this.props.history.push('/test/editCases/' + record.id);
        }}
    />;
    return (
        <div style={styles.oper}>
          <Balloon.Tooltip trigger={edit} triggerType="hover" align='l'>
            <FormattedMessage
                id="test.caseLists.table.edit"
                defaultMessage="编辑测试用例"
            />
          </Balloon.Tooltip>
          <Balloon.Tooltip trigger={MoveTarget} triggerType="hover" align='r'>
            <FormattedMessage
                id="test.caseLists.table.logs"
                defaultMessage="查看执行日志"
            />
          </Balloon.Tooltip>
        </div>
    );
  };

  renderCaseType = () => {
    let type = this.state.searchValue.type;
    if (type === 'interface') {
      return  <FormattedMessage
          id='test.caseLists.table.type.interface'
          defaultMessage="接口测试"
      />;
    }else {
      return <FormattedMessage
          id='test.caseLists.table.type.manual'
          defaultMessage="手工测试"
      />;
    }
  };

  renderSwitch = (value,index,record) => {
    let caseId = record.id;
    return <Switch onChange={(checked) => {
      let type = this.state.searchValue.type;
      if (type === 'manual') {
        Toast.error(<FormattedMessage
            id='test.caseLists.table.message.manNotRun'
            defaultMessage="手工测试用例暂不支持执行！"
        />);
        return;
      }
      if (checked) {
        Toast.success(<FormattedMessage
            id='test.caseLists.table.message.inRun'
            defaultMessage="测试用例开始执行，执行结果请在用例执行历史记录中查看!"
        />);
        this.execute(caseId);
      }
    }
    }/>;
  };

  onOpen = () =>{
    this.setState({
      createManualDialogVisiable: true
    })
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

  execute = (id) => {
    // only interface script is executable
    let url = API.test + '/interfaceCases/execute/' + id;
    let _this = this;
    Axios.get(url).then(function (response) {
      console.log(response);
    }).catch(function (error) {
      console.log(error);
    });
  };

  render() {
    const {searchValue} = this.state.searchValue;

    return (
        <div>
          <IceContainer title={this.props.intl.messages["test.search.searchTitle"]}>
            <FormBinderWrapper value={this.state.searchValue} onChange={this.formChange}>
              <Row wrap>
                <Col xxs="24" l="8" style={styles.formCol}>
                  <span style={styles.label}>{this.props.intl.messages["test.search.caseOwner"]}</span>
                  <FormBinder name="owner">
                    <Select placeholder={this.props.intl.messages["test.search.pleaseSelect"]} style={{ width: '200px' }}>
                      <Select.Option value="mine">{this.props.intl.messages["test.search.caseOwner.mine"]}</Select.Option>
                      <Select.Option value="all">{this.props.intl.messages["test.search.caseOwner.all"]}</Select.Option>
                    </Select>
                  </FormBinder>
                </Col>

                <Col xxs="24" l="8" style={styles.formCol}>
                  <span style={styles.label}>{this.props.intl.messages["test.search.caseType"]}</span>
                  <FormBinder name="type">
                    <Select placeholder={this.props.intl.messages["test.search.pleaseSelect"]} style={{ width: '200px' }} defaultValue="interface" onClose={this.refreshList.bind(this, 1)}>
                      <Select.Option value="manual">{this.props.intl.messages["test.search.caseType.manual"]}</Select.Option>
                      <Select.Option value="interface">{this.props.intl.messages["test.search.caseType.interface"]}</Select.Option>
                    </Select>
                  </FormBinder>
                </Col>

                <Col xxs="24" l="8" style={styles.formCol}>
                  <span style={styles.label}>{this.props.intl.messages["test.search.testGroup"]}</span>
                  <FormBinder name="group">
                    <Select placeholder={this.props.intl.messages["test.search.pleaseSelect"]} style={{ width: '200px' }}>
                      <Select.Option value="success">{this.props.intl.messages["test.search.testGroup.demo1"]}</Select.Option>
                      <Select.Option value="fail">{this.props.intl.messages["test.search.testGroup.demo2"]}</Select.Option>
                      <Select.Option value="block">{this.props.intl.messages["test.search.testGroup.demo3"]}</Select.Option>
                      <Select.Option value="all">{this.props.intl.messages["test.search.testGroup.all"]}</Select.Option>
                    </Select>
                  </FormBinder>
                </Col>

                {/*<Col xxs="24" l="8" style={styles.formCol}>*/}
                {/*  <span style={styles.label}>执行结果:</span>*/}
                {/*  <FormBinder name="result">*/}
                {/*    <Select placeholder="请选择" style={{ width: '200px' }}>*/}
                {/*      <Select.Option value="success">成功</Select.Option>*/}
                {/*      <Select.Option value="fail">失败</Select.Option>*/}
                {/*      <Select.Option value="block">阻塞</Select.Option>*/}
                {/*      <Select.Option value="all">所有</Select.Option>*/}
                {/*    </Select>*/}
                {/*  </FormBinder>*/}
                {/*</Col>*/}

                <Col xxs="24" l="8" style={styles.formCol}>
                  <span style={styles.label}>{this.props.intl.messages["test.search.creator"]}</span>
                  <FormBinder name="cuser">
                    <Input />
                  </FormBinder>
                </Col>
              </Row>
            </FormBinderWrapper>
          </IceContainer>

          <IceContainer title={this.props.intl.messages['test.caseLists.title']}>
            <Row wrap style={styles.headRow}>
              <Col l="12">
                <Button style={styles.button} onClick={this.onOpen.bind(this)} >
                  <Icon type="add" size="xs" style={{ marginRight: '4px' }} />
                  {this.props.intl.messages["test.caseLists.add.manual"]}
                </Button>
                <Dialog
                    visible={this.state.createManualDialogVisiable}
                    onOk={this.onOk.bind(this)}
                    onCancel={this.onClose.bind(this)}
                    onClose={this.onClose.bind(this)}
                    title={this.props.intl.messages["test.caseLists.add.manual.dialog.title"]}
                    isFullScreen
                    locale={{
                      ok: 'Confirm',
                      cancel: 'Cancel'
                    }}
                >
                  <CreateManualCaseFrom
                      isSubmit={this.state.isSubmit}
                      close={this.onClose.bind(this)}
                      refresh={this.refreshList.bind(this)}
                  />
                </Dialog>

                <Button style={{ ...styles.button, marginLeft: 10}}>
                  <Link to="/test/createInterfaceCase">
                    <Icon type="add" size="xs" style={{ marginRight: '4px' }} />
                    {this.props.intl.messages["test.caseLists.add.interface"]}
                  </Link>
                </Button>
              </Col>
              <Col l="12" style={styles.center}>
                <Button type="normal" style={styles.button}>
                  {this.props.intl.messages["test.caseLists.delete"]}
                </Button>
              </Col>
            </Row>

            <Table
                dataSource={this.state.currentData}
                rowSelection={{ onChange: this.onChange }}
            >
              <Table.Column title={this.props.intl.messages["test.caseLists.table.caseId"]} dataIndex="id" width={100} />
              <Table.Column title={this.props.intl.messages["test.caseLists.table.name"]} dataIndex="caseName" width={100} />
              <Table.Column title={this.props.intl.messages["test.caseLists.table.type"]} cell={this.renderCaseType} width={100} />
              <Table.Column title={this.props.intl.messages["test.caseLists.table.status"]} dataIndex="status" width={100} />
              {/*<Table.Column title="执行结果" dataIndex="executeResult" width={100} />*/}
              <Table.Column title={this.props.intl.messages["test.caseLists.table.creator"]} dataIndex="createUserName" width={100} />
              <Table.Column title={this.props.intl.messages["test.caseLists.table.run"]} width={100} cell={this.renderSwitch} />
              <Table.Column title={this.props.intl.messages["test.caseLists.table.operations"]} width={100} cell={this.renderOper} />
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

export default injectIntl(withRouter(CustomTable));
