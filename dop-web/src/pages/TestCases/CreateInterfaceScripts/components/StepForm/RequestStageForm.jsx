import React, { Component } from 'react';
import {
    FormBinderWrapper,
    FormBinder,
    FormError,
} from '@icedesign/form-binder';
import {
    Input,
    Button,
    Checkbox,
    Grid,
} from '@icedesign/base';
import {TabPane} from "@icedesign/base/lib/tab";
import Tab from "@icedesign/base/lib/tab";
import RequestHeader from "./RequestHeader";
import RequestCheckPoint from "./RequestCheckPoint";
import {Option} from "@icedesign/base/lib/select";
import Select from "@icedesign/base/lib/select";
import Operations from "./Operations";
import {injectIntl,FormattedMessage} from "react-intl";

const { Row, Col } = Grid;


const styles = {
    btns: {
        marginTop: '25px',
        marginBottom: '25px',
    },
};

class RequestStageForm extends Component{

    constructor(props) {
        super(props);
        this.state = {
            isSubmit: this.props.isSubmit,
            value: this.props.data,
            stage: this.props.stage,
            caseParams: this.props.caseParams
        };
    }

    addRequestScript = () => {
        let index = this.state.value.operations.length;
        this.state.value.operations.push({
            operationType: 'REQUEST',
            order: index
        });
        this.state.value.requestScripts.push({
            rawUrl: '',
            httpMethod: 'GET',
            requestHeaders: [],
            requestParams: [],
            requestBody: '',
            requestCheckPoints: [],
            retryTimes: '',
            retryInterval: '',
            resultParams: [],
            operationType: 'REQUEST',
            order: index
        });
        this.state.value.waitOperations.push({
            waitTime: 1000000,
            order: -1,
            operationType: 'WAIT'
        });
        this.setState({isSubmit: false, value: this.state.value});
    };

    addRequestScriptWithContent(script) {
        let index = this.state.value.operations.length;
        this.state.value.operations.push({
            operationType: 'REQUEST',
            order: index
        });
        this.state.value.requestScripts.push(script);
        script["order"] = index;
        this.state.value.waitOperations.push({
            waitTime: 1000000,
            order: -1,
            operationType: 'WAIT'
        });
        this.setState({isSubmit: false, value: this.state.value});
    };

    removeOperation = (index) => {
        this.state.value.operations.map((operation,i) => {
            if (i > index) {
                operation.order -= 1;
            }
        });
        this.state.value.requestScripts.map((script,i) => {
            if (i > index) {
                script.order -= 1;
            }
        });
        this.state.value.waitOperations.map((wait,i) => {
            if (i > index) {
                wait.order -= 1;
            }
        });
        this.state.value.operations.splice(index, 1);
        this.state.value.requestScripts.splice(index, 1);
        this.state.value.waitOperations.splice(index, 1);
        this.setState({
            value: this.state.value
        });
    };

    addWaitTime = () => {
        let index = this.state.value.operations.length;
        this.state.value.operations.push({
            operationType: 'WAIT',
            order: index
        });
        this.state.value.requestScripts.push({
            rawUrl: '',
            httpMethod: 'GET',
            requestHeaders: [],
            requestBody: '',
            requestCheckPoints: [],
            retryTimes: 2,
            retryInterval: 2000,
            resultParams: [],
            operationType: 'REQUEST',
            order: -1
        });
        this.state.value.waitOperations.push({
            waitTime: '',
            order: index,
            operationType: 'WAIT'
        });
        this.setState({isSubmit: false, value: this.state.value});
    };

    submit = () => {
        this.props.onSubmit(this.state.value);
    };

    lastStep = () => {
        // this.setState({
        //
        // })
        this.props.onLast(this.state.value);
    };

    renderLastStep = (stage) => {
        if (stage !== 'PREPARE') {
            return (
                <Button onClick={this.lastStep.bind(this)} type="primary">
                    {this.props.intl.messages['test.stageForm.last']}
                </Button>
            );
        }
    };

    renderNextStep = (stage) => {
        if (stage !== 'DESTROY') {
            return (
                <Button onClick={this.submit.bind(this)} type="primary" style={{marginLeft: '10px'}}>
                    {this.props.intl.messages['test.stageForm.next']}
                </Button>
            );
        }else {
            return (
                <Button onClick={this.submit.bind(this)} type="primary" style={{marginLeft: '10px'}}>
                    {this.props.intl.messages['test.stageForm.save']}
                </Button>
            );
        }
    };

    componentWillReceiveProps(nextProps, nextContext) {
        this.setState({
            isSubmit: nextProps.isSubmit,
            value: nextProps.data,
            stage: nextProps.stage,
            caseParams: nextProps.caseParams
        });
    }


    cancel = () => {
        this.setState({isSubmit: false, value: this.state.value});
    };

    render() {
        return (
            <div>
                <Operations
                    requestScripts={this.state.value.requestScripts}
                    waitOperations={this.state.value.waitOperations}
                    isSubmit={this.state.isSubmit}
                    operations={this.state.value.operations}
                    addRequestScript={this.addRequestScript.bind(this)}
                    addWaitTime={this.addWaitTime.bind(this)}
                    removeOperation={this.removeOperation.bind(this)}
                    cancel={this.cancel.bind(this)}
                    stage={this.props.stage}
                    addRequestScriptWithContent={this.addRequestScriptWithContent.bind(this)}
                    caseId={this.props.caseId}
                    addCaseParam={this.props.add_caseParam}
                    caseParams={this.state.caseParams}
                />

                <Row>
                    <Col offset={9} style={styles.btns}>
                        {this.renderLastStep(this.props.stage)}

                        {this.renderNextStep(this.props.stage)}
                    </Col>
                </Row>
            </div>

        );
    }
}

export default injectIntl(RequestStageForm);
