import React, { Component } from 'react';
import {
    Input,
    Button,
    Grid,
    Icon,
    Upload,
    Dialog,
    Table
} from '@icedesign/base';
import IcePanel from '@icedesign/panel';
import {Feedback} from "@icedesign/base";

import {  FormBinder, FormError } from '@icedesign/form-binder';
import RequestScriptForm from "./RequestScriptForm";
import WaitOperation from "./WaitOperation";
import API from "../../../../API";
import Axios from "axios";
import {injectIntl} from "react-intl";

const { Row, Col } = Grid;

const {toast} = Feedback;

const styles = {
    formItem: {
        marginBottom: '20px',
        display: 'flex',
        alignItems: 'center',
    },
    formItemLabel: {
        width: '70px',
        marginRight: '10px',
        display: 'inline-block',
        textAlign: 'right',
    },
    formItemError: {
        marginLeft: '10px',
    },
    preview: {
        border: '1px solid #eee',
        marginTop: 20,
        padding: 10
    }
};

class Operations extends Component{

    constructor(props) {
        super(props);
        this.state = {
            isSubmit: false,
            requestScripts: this.props.requestScripts,
            waitOperations: this.props.waitOperations,
            operations: this.props.operations,
            caseParams: [],
            visible: false,
            selectedItems: [],
            selectedRowKeys: [],
            dataSource: []
        };
    }

    componentWillReceiveProps(nextProps, nextContext) {
        this.setState({
            requestScripts: nextProps.requestScripts,
            waitOperations: nextProps.waitOperations,
            operations: nextProps.operations,
            caseParams: nextProps.caseParams
        })
    }

    beforeUpload(info) {
        console.log("beforeUpload callback : ", info);

    }

    onChange(info) {
        // console.log("onChane callback : ", info);
    }

    uploadSuccess(res, file) {
        // console.log("I am success!");
        // console.log(res);
        // console.log(file);
    };

    onError = (file) => {
        let data = file.response;
        if (data) {
            let case_id = this.props.caseId;
            let case_params = data.caseParams;
            if (case_params) {
                case_params.forEach((param) => {
                    param.caseId = case_id;
                    this.props.addCaseParam(param);
                });
            }

            let scripts = data.requestScripts;
            if (scripts) {
                let i = 0;
                scripts.forEach((script) => {
                   script.id = i++;
                });
                this.setState({
                    dataSource: scripts,
                    visible: true
                });
                // scripts.forEach((script) => {
                //     this.props.addRequestScriptWithContent(script);
                // })
            }
        }
    };


    onItemSelect = (select, records) => {
        this.setState({
            selectedRowKeys: select,
            selectedItems: records
        });
    };

    onDialogOk = () => {
        let selectedData = this.state.selectedItems;
        selectedData.forEach((data)=>{
            delete data['id'];
            this.props.addRequestScriptWithContent(data);
        });
        this.hideDialog();
    };

    showDialog = () => {
        this.setState({
            visible: true,
        });
    };

    hideDialog = () => {
        this.setState({
            selectedRowKeys: [],
            selectedItems: [],
            visible: false,
            dataSource: []
        });
    };

    render() {
        let importApi = API.test + '/interfaceCases/import';
        let autho = null;
        let x_login = null;
        if (window.sessionStorage.getItem('Authorization') && window.sessionStorage.getItem('x-login-token')) {
            autho = "Bearer " + window.sessionStorage.getItem('Authorization');
            x_login = window.sessionStorage.getItem('x-login-token');
        }else {
            toast.error(this.props.intl.messages['test.operations.loginFail']);
            return;
        }
        return (
            <div>
                <Dialog
                    className="choose-dialog"
                    style={{width: '700px'}}
                    autoFocus={false}
                    isFullScreen
                    title={this.props.intl.messages['test.operations.import.dialog.title']}
                    {...this.props}
                    onOk={this.onDialogOk}
                    onClose={this.hideDialog}
                    onCancel={this.hideDialog}
                    visible={this.state.visible}
                    locale={
                        {
                            ok: this.props.intl.messages['test.operations.import.dialog.ok'],
                            cancel: this.props.intl.messages['test.operations.import.dialog.cancel']
                        }
                    }
                >
                    <Table
                        dataSource={this.state.dataSource}
                        rowSelection={{
                            selectedRowKeys: this.state.selectedRowKeys,
                            onChange: this.onItemSelect,
                        }}
                        locale={
                            {
                                empty: this.props.intl.messages['test.operations.import.table.empty']
                            }
                        }
                    >
                        <Table.Column width={150} title={this.props.intl.messages['test.operations.import.dialog.col1']} dataIndex="httpMethod" />
                        <Table.Column title={this.props.intl.messages['test.operations.import.dialog.col2']} dataIndex="rawUrl" />
                    </Table>
                </Dialog>


                <Row>
                    <Col span="15"/>
                    <Col span='3'>
                        <div style={styles.buttons}>
                            <Button type="secondary" onClick={this.props.addRequestScript.bind(this)}>
                                {this.props.intl.messages['test.operations.addHttp']}
                            </Button>
                        </div>
                    </Col>
                    <Col span='3'>
                        <div style={styles.buttons}>
                            <Button type="secondary" onClick={this.props.addWaitTime.bind(this)}>
                                {this.props.intl.messages['test.operations.addWait']}
                            </Button>
                        </div>
                    </Col>
                    <Col span='3'>
                        <Upload
                            listType="text"
                            action={importApi}
                            accept=".json"
                            beforeUpload={this.beforeUpload}
                            onChange={this.onChange}
                            showUploadList={false}
                            onSuccess={this.uploadSuccess}
                            onError={this.onError}
                            headers={
                                {
                                    Authorization: autho,
                                    'x-login-token': x_login
                                }
                            }
                        >
                            <Button type="primary" style={{ margin: "0 0 10px" }}>
                                {this.props.intl.messages['test.operations.import']}
                            </Button>
                        </Upload>
                    </Col>
                </Row>
                <hr/>
                {this.state.operations.map((operation, index) => {
                    if (operation.operationType === 'REQUEST') {
                        return (
                            <div key={Math.random()}>
                                <IcePanel status='primary' style={{marginBottom: '10px'}}>
                                    <IcePanel.Header>
                                        <Row>
                                            <Col span='23'>
                                                {this.props.intl.messages['test.operations.http.title']}
                                            </Col>
                                            <Col span='1'>
                                                {/*<Button type="text" size='large' shape='ghost' onClick={this.props.removeOperation.bind(this,index)} style={{marginHeight: '20px'}}>*/}
                                                     <Icon type="error" onClick={this.props.removeOperation.bind(this,index)}/>
                                                {/*</Button>*/}
                                            </Col>
                                        </Row>
                                    </IcePanel.Header>
                                    <IcePanel.Body>
                                        <Row>
                                            <Col span="22">
                                                <RequestScriptForm currentScript={this.props.requestScripts[index]} isSubmit={this.state.isSubmit} cancel={this.props.cancel} order={index}/>
                                            </Col>
                                            <Col span="2">

                                            </Col>
                                        </Row>
                                    </IcePanel.Body>
                                </IcePanel>
                            </div>
                    );
                    }else if (operation.operationType === 'WAIT') {
                        return(
                            <div key={Math.random()}>
                                <IcePanel status='danger' style={{marginBottom: '10px'}}>
                                    <IcePanel.Header>
                                        <Row>
                                            <Col span='23'>
                                                {this.props.intl.messages['test.operations.wait.title']}
                                            </Col>
                                            <Col span='1'>
                                                {/*<Button size='large' onClick={this.props.removeOperation.bind(this,index)}>*/}
                                                    <Icon type="error" onClick={this.props.removeOperation.bind(this,index)}/>
                                                {/*</Button>*/}
                                            </Col>
                                        </Row>
                                    </IcePanel.Header>
                                    <IcePanel.Body>
                                        <Row>
                                            <Col span="22">
                                                <WaitOperation currentOperation={this.props.waitOperations[index]} isSubmit={this.state.isSubmit} cancel={this.props.cancel} order={index}/>
                                            </Col>
                                            <Col span="2">

                                            </Col>
                                        </Row>
                                    </IcePanel.Body>
                                </IcePanel>
                            </div>
                        );
                    }
                })}
            </div>
        );
    }
}

export default injectIntl(Operations);
