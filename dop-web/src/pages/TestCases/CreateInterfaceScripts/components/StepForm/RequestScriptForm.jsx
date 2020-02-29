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
    Grid, Icon,Balloon
} from '@icedesign/base';
import {TabPane} from "@icedesign/base/lib/tab";
import Tab from "@icedesign/base/lib/tab";
import RequestHeader from "./RequestHeader";
import RequestCheckPoint from "./RequestCheckPoint";
import {Option} from "@icedesign/base/lib/select";
import Select from "@icedesign/base/lib/select";
import ResultParam from "./ResultParam";
import RequestParam from "./RequestParam";
import {injectIntl} from "react-intl";

const { Row, Col } = Grid;
const demo = JSON.stringify({
    "a": "1",
    "b": "2"
});

class RequestScriptForm extends Component{

    constructor(props) {
        super(props);
        this.state = {
            value: this.props.currentScript
        };
    }

    validateFormAndPost = () => {
        let noError = true;
        this.refs.form.validateAll((errors, values) => {
            if (errors != null) {
                noError = false;
            }
        });
        if (noError) {
            this.doSubmit(this.state.value);
        }
        // this.props.cancel();
    };

    doSubmit = (content) => {
        // this.props.submitRequest(content);
    };

    addItem = () => {
        this.state.value.requestHeaders.push({
            name: '',
            value: ''
        });
        this.setState({ value: this.state.value });
    };

    removeItem = (index) => {
        this.state.value.requestHeaders.splice(index, 1);
        this.setState({
            value: this.state.value
        });
    };

    addCheckPoint = () => {
        this.state.value.requestCheckPoints.push({
            property: '',
            operation: '',
            value: ''
        });
        this.setState({ value: this.state.value });
    };

    removeCheckPoint = (index) => {
        this.state.value.requestCheckPoints.splice(index, 1);
        this.setState({
            value: this.state.value
        });
    };

    addResultParam = () => {
        this.state.value.resultParams.push({
            name: '',
            rawValue: '',
            paramType: 'STRING'
        });
        this.setState({ value: this.state.value });
    };

    removeResultParam = (index) => {
        this.state.value.resultParams.splice(index, 1);
        this.setState({
            value: this.state.value
        });
    };

    addRequestParam = () => {
        this.state.value.requestParams.push({
            name: '',
            value: '',
            paramClass: 'GET_PARAM'
        });
        this.setState({ value: this.state.value });
    };

    removeRequestParam = (index) => {
        this.state.value.requestParams.splice(index, 1);
        this.setState({
            value: this.state.value
        });
    };

    renderTab = (key) => {
        if (key === 'header') {
            return <RequestHeader
                requestHeaders={this.state.value.requestHeaders}
                addItem={this.addItem.bind(this)}
                removeItem={this.removeItem.bind(this)}/>;
        }

        if (key === 'param') {
            return <RequestParam
                requestParams={this.state.value.requestParams}
                addItem={this.addRequestParam.bind(this)}
                removeItem={this.removeRequestParam.bind(this)}/>;
        }

        if (key === 'body') {
            return <Row>
                <Col>
                    <FormBinder name="requestBody" >
                        <Input multiple style={{width: '100%'}}/>
                    </FormBinder>
                </Col>
            </Row>;
        }
        if (key === 'checkPoint') {
            return <RequestCheckPoint
                requestCheckPoints={this.state.value.requestCheckPoints}
                addItem={this.addCheckPoint.bind(this)}
                removeItem={this.removeCheckPoint.bind(this)}/>;
        }
        if (key === 'requestParam') {
            return <ResultParam
                resultParams={this.state.value.resultParams}
                addItem={this.addResultParam.bind(this)}
                removeItem={this.removeResultParam.bind(this)}
            />
        }
    };

    componentWillReceiveProps(nextProps, nextContext) {
        this.setState({
            value: nextProps.currentScript
        })
    }


    render() {
        const paramTab = (
            <div>
                {this.props.intl.messages['test.requestScript.resultParam.add']}&nbsp;
                <Balloon trigger={
                    <Icon type="help" size='xs'/>
                } triggerType="hover" align='r'>
                    {this.props.intl.messages['test.requestScript.resultParam.info1']}
                    <code>
                        {demo}
                    </code>
                    {this.props.intl.messages['test.requestScript.resultParam.info2']}
                </Balloon>
            </div>
        );


        const checkPointTab = (
            <div>
                {this.props.intl.messages['test.requestScript.checkPoint.add']}&nbsp;
                <Balloon trigger={
                    <Icon type="help" size='xs'/>
                } triggerType="hover" align='r'>
                    {this.props.intl.messages['test.requestScript.checkPoint.info1']}
                    <code>
                        {demo}
                    </code>
                    {this.props.intl.messages['test.requestScript.checkPoint.info2']}
                </Balloon>
            </div>
        );

        let header = this.props.intl.messages['test.requestScript.header'];
        let param = this.props.intl.messages['test.requestScript.param'];
        let body = this.props.intl.messages['test.requestScript.body'];

        const tabs = [
            { tab: header, key: "header", content: "这里是首页内容" },
            { tab: param, key: "param", content: "这里是首页内容" },
            { tab: body, key: "body", content: "这里是文档内容" },
            { tab: checkPointTab, key: "checkPoint", content: "这里是 API 内容" },
            { tab: paramTab, key: "requestParam", content: "这里是 API 内容" }
        ];

        return (
            <div>
                <FormBinderWrapper
                    value={this.state.value}
                    ref="request">
                    <Row>
                        <Col span="4">
                            <FormBinder name="httpMethod" >
                                <Select placeholder={this.props.intl.messages['test.requestScript.method.select']} style={{width: '100%'}}
                                >
                                    <Option value="GET">GET</Option>
                                    <Option value="POST">POST</Option>
                                    <Option value="PUT">PUT</Option>
                                    <Option value="DELETE">DELETE</Option>
                                </Select>
                            </FormBinder>
                        </Col>
                        <Col span="20">
                            <FormBinder name="rawUrl" >
                                <Input placeholder={this.props.intl.messages['test.requestScript.url.input']} style={{width: '100%'}}/>
                            </FormBinder>
                        </Col>
                    </Row>
                </FormBinderWrapper>

                <Tab onChange={this.handleChange}>
                    {tabs.map(item => (
                        <TabPane key={item.key} tab={item.tab} onClick={this.handleClick}>
                            <FormBinderWrapper
                                value={this.state.value}
                                ref="form"
                            >
                                {this.renderTab(item.key)}
                            </FormBinderWrapper>
                        </TabPane>
                    ))}
                </Tab>
            </div>
        );
    }



}

export default injectIntl(RequestScriptForm);
