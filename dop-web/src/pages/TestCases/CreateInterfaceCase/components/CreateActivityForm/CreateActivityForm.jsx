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
import API from "../../../../API";
import {FormBinder, FormBinderWrapper, FormError} from '@icedesign/form-binder';
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

class CreateActivityForm extends Component {
  static displayName = 'CreateActivityForm';

  static defaultProps = {};

  constructor(props) {
    super(props);
    this.state = {
      value: {
        caseName: '',
        caseDesc: '',
        preCondition: '',
        applicationId: '',
        steps: '',
        predicateResult: '',
        status: 'NEW'
      },
    };
  }

  onFormChange = (value) => {
    this.setState({
      value,
    });
  };

  reset = () => {
    this.setState({
      value: {
        caseName: '',
        caseDesc: '',
        preCondition: '',
        applicationId: '',
        steps: '',
        predicateResult: '',
        status: 'NEW'
      },
    });
  };

  back = () => {
    this.props.history.push('/testCases');
  };

  submit = () => {
    let _this = this;
    let noError = true;

    this.refs.form.validateAll((error, value) => {
      if (error != null) {
        noError = false;
      }
    });

    if (noError) {
      let url = API.test + '/interfaceCases';
      Axios.post(url, this.state.value)
          .then(function (response) {
            Toast.success(_this.props.intl.messages['test.createInterface.successInfo']);
            let route = '/test/createInterfaceScripts/' + response.data;
            _this.props.history.push(route);
          }).catch(function (error) {
        console.log(error);
        Toast.error(_this.props.intl.messages['test.createInterface.successInfo']);
      });
    }
  };

  render() {
    return (
      <div className="create-activity-form">
        <IceContainer title={this.props.intl.messages['test.createInterface.title']} style={styles.container}>
            <FormBinderWrapper
                value={this.state.value}
                ref="form"
            >
            <div>
              <Row style={styles.formItem}>
                <Col xxs="6" s="2" l="2" style={styles.formLabel}>
                  {this.props.intl.messages['test.createInterface.caseName']}
                </Col>

                <Col s="12" l="10">
                  <FormBinder
                    name="caseName"
                    required
                    message={this.props.intl.messages['test.createInterface.caseNameWarn']}
                  >
                    <Input style={{ width: '100%' }} />
                  </FormBinder>
                  <FormError name="caseName" />
                </Col>
              </Row>

              <Row style={styles.formItem}>
                <Col xxs="6" s="2" l="2" style={styles.formLabel}>
                  {this.props.intl.messages['test.createInterface.appId']}
                </Col>

                <Col s="12" l="10">
                  <FormBinder
                      name="applicationId"
                      required
                      message={this.props.intl.messages['test.createInterface.appIdWarn']}
                  >
                    <Input style={{ width: '100%' }} />
                  </FormBinder>
                  <FormError name="applicationId" />
                </Col>
              </Row>

              <Row>
                <Col xxs="6" s="2" l="2" style={styles.formLabel}>
                  {this.props.intl.messages['test.createInterface.caseDesc']}
                </Col>
                <Col s="12" l="10">
                  <FormBinder name="caseDesc" required message={this.props.intl.messages['test.createInterface.caseDescWarn']}>
                    <Input multiple style={{ width: '100%' }} />
                  </FormBinder>
                  <FormError name="caseDesc" />
                </Col>
              </Row>

              <Row>
                <Col xxs="6" s="2" l="2" style={styles.formLabel}>
                  {this.props.intl.messages['test.createInterface.preCondition']}
                </Col>
                <Col s="12" l="10">
                  <FormBinder name="preCondition">
                    <Input multiple style={{ width: '100%' }} placeholder={this.props.intl.messages['test.createInterface.preCondition.place']} />
                  </FormBinder>
                </Col>
              </Row>

              <Row>
                <Col xxs="6" s="2" l="2" style={styles.formLabel}>
                  {this.props.intl.messages['test.createInterface.testSteps']}
                </Col>
                <Col s="12" l="10">
                  <FormBinder name="steps">
                    <Input multiple style={{ width: '100%' }} />
                  </FormBinder>
                </Col>
              </Row>

              <Row>
                <Col xxs="6" s="2" l="2" style={styles.formLabel}>
                  {this.props.intl.messages['test.createInterface.expectedResult']}
                </Col>
                <Col s="12" l="10">
                  <FormBinder name="predicateResult">
                    <Input multiple style={{ width: '100%' }} />
                  </FormBinder>
                </Col>
              </Row>

              <Row style={styles.btns}>
                <Col xxs="6" s="2" l="2" style={styles.formLabel}>
                  {' '}
                </Col>
                <Col s="12" l="10">
                  <Button style={{marginRight: '20px'}} onClick={this.back}>
                    {this.props.intl.messages['test.createInterface.cancel']}
                  </Button>

                  <Button onClick={this.submit} type='secondary'>
                    {this.props.intl.messages['test.createInterface.yes']}
                    {/* <Link to='/test/createInterfaceScripts'>
                    </Link>*/}
                  </Button>
                  <Button style={styles.resetBtn} onClick={this.reset}>
                    {this.props.intl.messages['test.createInterface.reset']}
                  </Button>
                </Col>
              </Row>
            </div>
            </FormBinderWrapper>
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

export default injectIntl(withRouter(CreateActivityForm));
