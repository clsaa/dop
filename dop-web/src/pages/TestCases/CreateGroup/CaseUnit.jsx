import React, { Component } from 'react';
import {
    Input,
    Button,
    Grid, Icon, Select
} from '@icedesign/base';

import {  FormBinder, FormError } from '@icedesign/form-binder';
import API from "../../API";
import Axios from "axios";

const { Row, Col } = Grid;

const styles = {
    formItem: {
        marginBottom: '20px',
        display: 'flex',
        alignItems: 'center',
    },
    formItemLabel: {
        width: '70px',
        marginTop: '5px',
        marginBottom: '5px',
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

export default class CaseUnit extends Component{

    constructor(props) {
        super(props);
        this.state = {
            dataSource: [],
            appId: this.props.appId
        };
    }

    onSearch = value => {
        if (this.searchTimeout) {
            clearTimeout(this.searchTimeout);
        }
        let _this = this;
        let appId = this.state.appId;
        let api = API.test + "/simpleCases?appId=" + appId + "&key=" + encodeURI(value);
        this.searchTimeout = setTimeout(() => {
            Axios.get(api).then(function (response) {
                const dataSource = response.data.map(item => {
                    return {
                        label: item['searchInfo'],
                        value: _this.jsonStringForCase(item)
                    };
                });
                _this.setState({
                    dataSource
                });
            })
        }, 100);
    };

    componentWillReceiveProps(nextProps, nextContext) {
        let _this = this;
        let appId = nextProps.appId;
        // if (appId !== this.props.appId) {
        //     this.props.clearSelect();
        // }
        let api = API.test + "/simpleCases?appId=" + appId;
        Axios.get(api).then(function (response) {
            const dataSource = response.data.map(item => {
                return {
                    label: item['searchInfo'],
                    value: _this.jsonStringForCase(item)
                };
            });
            _this.setState({
                dataSource,appId
            });
        })
    }

    jsonStringForCase = (simpleCase) => {
        let standardUnit = {
            caseType: simpleCase['caseType'],
            caseId: simpleCase['id'],
            appId: simpleCase['applicationId'],
            caseName: simpleCase['caseName'],
        };
        return JSON.stringify(standardUnit);
    };

    renderSelectValue = (unitString) => {
        let unitJson = JSON.parse(unitString);
        let type = unitJson['caseType'];
        // let typeStr = type === 'MANUAL' ? '手工' : '接口';
        return type + '---【' + unitJson['caseId'] + '】' + '---' + unitJson['caseName'];
    };

    render() {
        return (
            <div>
                {this.props.caseUnits.map((caseUnit, index) => {
                    return (
                        <Row key={index} style={{marginBottom: '5px', marginTop: '5px',marginRight: '10px'}}>
                            <Col l='1'>
                                <span style={styles.formItemLabel}>Case {index+1}：</span>
                            </Col>
                            <Col l='8'>
                                <FormBinder required message="请选择一个测试用例" name={`caseUnits[${index}]`} >
                                    <Select
                                        showSearch
                                        dataSource={this.state.dataSource}
                                        onSearch={this.onSearch}
                                        filterLocal={false}
                                        className="temp"
                                        style={{width: '500px', marginRight: '10px'}}
                                        value={this.renderSelectValue(caseUnit)}
                                    />
                                </FormBinder>
                                <FormError name={`caseUnits[${index}]`} />
                            </Col>

                            <Col l='3'>
                                <Button type="secondary" onClick={this.props.removeItem.bind(this, index)}>
                                    <Icon type="ashbin" size='xxxl'/>
                                </Button>
                            </Col>
                        </Row>
                    );
                })}
                <div style={styles.buttons}>
                    <Button type="secondary" onClick={this.props.addItem}>
                        <Icon type="add" size='xxl'/>
                    </Button>
                </div>
            </div>
        );
    }
}
