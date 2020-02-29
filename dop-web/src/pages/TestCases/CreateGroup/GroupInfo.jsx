import React, { Component } from 'react';
import IceContainer from '@icedesign/container';
import {
    Input,
    Button,
    Checkbox,
    Select,
    DatePicker,
    Switch,
    Radio,
    Grid, Feedback,
} from '@icedesign/base';
import Axios from "axios";
import {Link, withRouter} from "react-router-dom";
import {FormBinder, FormBinderWrapper, FormError} from '@icedesign/form-binder';
import API from "../../API";
import CaseUnit from "./CaseUnit";
import {injectIntl} from "react-intl";

const { Row, Col } = Grid;

// FormBinder 用于获取表单组件的数据，通过标准受控 API value 和 onChange 来双向操作数据
const CheckboxGroup = Checkbox.Group;
const RadioGroup = Radio.Group;
const { RangePicker } = DatePicker;

// Switch 组件的选中等 props 是 checked 不符合表单规范的 value 在此做转换
const SwitchForForm = (props) => {
    const checked = props.checked === undefined ? props.value : props.checked;

    return (
        <Switch
            {...props}
            checked={checked}
            onChange={(currentChecked) => {
                if (props.onChange) props.onChange(currentChecked);
            }}
        />
    );
};

const Toast = Feedback.toast;

class GroupInfo extends Component {
    static displayName = 'CreateGroup';

    static defaultProps = {};

    constructor(props) {
        super(props);
        this.state = {
            value: this.props.groupDto,
            operation: this.props.operation
        };
    }

    onFormChange = (value) => {
        this.setState({
            value
        });
    };

    clearSelect = () =>{
        let newValue = this.state.value;
        newValue['caseUnits'] = [];
        this.setState({
            value: newValue
        })
    };

    reset = () => {
        this.setState({
            value: {
                appId: '',
                groupName: '',
                comment: '',
                executeWay: 'PARALLEL',
                caseUnits: []
            },
        });
    };

    back = () => {
        this.props.history.push('/test/testGroups');
    };

    submit = () => {
        let _this = this;
        let noError = true;

        this.refs.form.validateAll((error, value) => {
            if (error != null) {
                noError = false;
            }
        });

        this.refs.ax.validateAll((error, value) => {
            if (error != null) {
                noError = false;
            }
        });

        if (noError) {
            let url = API.test + '/group';
            let caseUnits = this.state.value.caseUnits;
            let newUnits = [];
            let curAppId = this.state.value.appId;
            let hasError = false;
            caseUnits.forEach((unit) => {
                let newUnit = JSON.parse(unit);
                if (newUnit['appId'] && newUnit['appId'] != curAppId) {
                    hasError = true;
                }
                newUnits.push(newUnit);
            });
            if (hasError) {
                Toast.error("It seems you choose some invalid cases not belonging to the app of this case!" +
                    "Please reset and select test cases again!");
                return;
            }
            let param = this.state.value;
            param.caseUnits = newUnits;
            if (this.state.operation === 'UPDATE'){
                Axios.put(url, param)
                    .then(function (response) {
                        Toast.success(_this.props.intl.messages['test.newGroup.success.mes']);
                        _this.props.history.push('/test/testGroups');
                    }).catch(function (error) {
                    Toast.error(_this.props.intl.messages['test.newGroup.error.mes']);
                });
            } else if(this.state.operation === 'INSERT'){
                Axios.post(url, param)
                    .then(function (response) {
                        Toast.success(_this.props.intl.messages['test.newGroup.success.add.mes']);
                        _this.props.history.push('/test/testGroups');
                    }).catch(function (error) {
                    console.log(error);
                    Toast.error(_this.props.intl.messages['test.newGroup.error.add.mes']);
                });
            }
        }
    };

    addItem = () => {
        let newUnit = {
            "caseId": '',
            "caseType": 'MANUAL',
            "caseName": this.props.intl.messages['test.newGroup.default.name']
        };
        this.state.value.caseUnits.push(JSON.stringify(newUnit));
        this.setState({ value: this.state.value });
    };

    removeItem = (index) => {
        this.state.value.caseUnits.splice(index, 1);
        this.setState({
            value: this.state.value
        });
    };

    componentWillReceiveProps(nextProps, nextContext) {
        this.setState({
            value: nextProps.groupDto,
            operation: nextProps.operation
        })
    }

    render() {
        return (
            <div className="create-activity-form">
                <IceContainer title={this.props.intl.messages['test.newGroup.title']} style={styles.container}>
                    <FormBinderWrapper
                        value={this.state.value}
                        ref="form"
                        onChange={this.onFormChange}
                    >
                        <div>
                            <Row style={styles.formItem}>
                                <Col xxs="6" s="2" l="3" style={styles.formLabel}>
                                    {this.props.intl.messages['test.newGroup.name']}
                                </Col>

                                <Col s="12" l="7">
                                    <FormBinder
                                        name="groupName"
                                        required
                                        message={this.props.intl.messages['test.newGroup.nameWarn']}
                                    >
                                        <Input style={{ width: '100%' }} />
                                    </FormBinder>
                                    <FormError name="groupName" />
                                </Col>
                            </Row>

                            <Row style={styles.formItem}>
                                <Col xxs="6" s="2" l="3" style={styles.formLabel}>
                                    {this.props.intl.messages['test.createInterface.appId']}
                                </Col>

                                <Col s="12" l="7">
                                    <FormBinder
                                        name="appId"
                                        required
                                        message={this.props.intl.messages['test.createInterface.appIdWarn']}
                                    >
                                        <Input style={{ width: '100%' }} disabled={this.state.operation === 'UPDATE'}/>
                                    </FormBinder>
                                    <FormError name="appId" />
                                </Col>
                            </Row>

                            <Row>
                                <Col xxs="6" s="2" l="3" style={styles.formLabel}>
                                    {this.props.intl.messages['test.newGroup.desc']}
                                </Col>
                                <Col s="12" l="7">
                                    <FormBinder name="comment" required message={this.props.intl.messages['test.newGroup.descWarn']}>
                                        <Input multiple style={{ width: '100%' }} />
                                    </FormBinder>
                                    <FormError name="comment" />
                                </Col>
                            </Row>

                            <Row>
                                <Col xxs="6" s="2" l="3" style={styles.formLabel}>
                                    {this.props.intl.messages['test.newGroup.executeWay']}
                                </Col>
                                <Col s="12" l="7">
                                    <FormBinder name="executeWay">
                                        <Select placeholder="请选择" style={{ width: '200px' }}>
                                            <Select.Option value="PARALLEL">{this.props.intl.messages['test.createGroup.executeWay.parallel']}</Select.Option>
                                            <Select.Option value="SERIAL">{this.props.intl.messages['test.createGroup.executeWay.serial']}</Select.Option>
                                        </Select>
                                    </FormBinder>
                                </Col>
                            </Row>

                        </div>
                    </FormBinderWrapper>
                </IceContainer>

                <IceContainer title={this.props.intl.messages['test.newGroup.case.title']} style={styles.container}>
                    <FormBinderWrapper
                        value={this.state.value}
                        ref="ax"
                    >

                    <CaseUnit caseUnits={this.state.value.caseUnits} appId={this.state.value.appId} clearSelect={this.clearSelect}
                              addItem={this.addItem.bind(this)} removeItem={this.removeItem.bind(this)}/>

                    </FormBinderWrapper>

                    <Row style={styles.btns}>
                        <Col xxs="6" s="2" l="2" style={styles.formLabel}>
                            {' '}
                        </Col>
                        <Col s="12" l="10">
                            <Button style={{marginRight: '20px'}} onClick={this.back}>
                                {this.props.intl.messages['test.createInterface.cancel']}
                            </Button>

                            <Button onClick={this.submit} type='secondary'>
                                {this.state.operation === 'INSERT'? this.props.intl.messages['test.newGroup.case.create'] : this.props.intl.messages['test.newGroup.case.update']}
                            </Button>
                            <Button style={styles.resetBtn} onClick={this.reset}>
                                {this.props.intl.messages['test.createInterface.reset']}
                            </Button>
                        </Col>
                    </Row>
                </IceContainer>
            </div>
        );
    }
}

const styles = {
    container: {
        paddingBottom: 0,
    },
    formItem: {
        height: '28px',
        lineHeight: '28px',
        marginBottom: '25px',
    },
    formLabel: {
        textAlign: 'right',
    },
    btns: {
        margin: '25px 0',
    },
    resetBtn: {
        marginLeft: '20px',
    },
};

export default injectIntl(withRouter(GroupInfo));
