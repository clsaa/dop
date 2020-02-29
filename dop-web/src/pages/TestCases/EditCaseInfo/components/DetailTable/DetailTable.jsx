import React, { Component } from 'react';
import IceContainer from '@icedesign/container/lib/index';
import {
  FormBinder as IceFormBinder,
  FormBinder,
  FormBinderWrapper,
  FormError as IceFormError,
  FormError
} from "@icedesign/form-binder";
import {Button, Feedback, Grid, Input, Icon} from "@icedesign/base";
import {injectIntl} from "react-intl";

const { Row, Col } = Grid;

const styles = {
  formItem: {
    marginBottom: '20px',
    display: 'flex',
    alignItems: 'center',
    color: '#999999'
  },
  formItemLabel: {
    width: '120px',
    mariginRight: '10px',
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
  },
  formCommonWidth: {
    width: "550px"
  },
  detailItem: {
    padding: '15px 0px',
    display: 'flex',
    borderTop: '1px solid #EEEFF3',
  },
  detailTitle: {
    marginRight: '30px',
    textAlign: 'right',
    width: '120px',
    color: '#999999',
  },
  detailBody: {
    flex: 1,
  },
  statusProcessing: {
    color: '#64D874',
  },
};

const Toast = Feedback.toast;

class DetailTable extends Component {
  static displayName = 'DetailTable';

  static propTypes = {};

  static defaultProps = {};

  constructor(props) {
    super(props);
    this.state = {
      caseDto: this.props.caseDto
    };
  }

  componentWillReceiveProps(nextProps, nextContext) {
    this.setState({
      caseDto: nextProps.caseDto
    })
  }

  render() {
    return (
      <div className="detail-table">

            <div style={styles.content}>

              <div style={styles.formItem}>
                <span style={styles.formItemLabel}>{this.props.intl.messages['test.createInterface.caseName']}</span>
                <FormBinder name="caseName" required message={this.props.intl.messages['test.createInterface.caseNameWarn']} >
                  <Input placeholder="case name" style={styles.formCommonWidth}/>
                </FormBinder>
                <FormError style={styles.formItemError} name="caseName" />
              </div>

              <div style={styles.formItem}>
                <span style={styles.formItemLabel}>{this.props.intl.messages['test.createInterface.appId']}</span>
                <FormBinder name="applicationId" required format="number" message={this.props.intl.messages['test.createInterface.appIdWarn']} >
                  <Input placeholder="application id" style={styles.formCommonWidth}/>
                </FormBinder>
                <FormError style={styles.formItemError} name="application id" />
              </div>

              <div style={styles.formItem}>
                <span style={styles.formItemLabel}>{this.props.intl.messages['test.createInterface.caseDesc']}</span>
                <FormBinder name="caseDesc" required  message={this.props.intl.messages['test.createInterface.caseDescWarn']} >
                  <Input multiple style={styles.formCommonWidth}/>
                </FormBinder>
                <FormError style={styles.formItemError} name="caseDesc" />
              </div>

              <div style={styles.formItem}>
                <span style={styles.formItemLabel}>{this.props.intl.messages['test.createInterface.preCondition']}</span>
                <FormBinder name="preCondition"  >
                  <Input multiple placeholder={this.props.intl.messages['test.createInterface.preCondition.place']} style={styles.formCommonWidth}/>
                </FormBinder>
                <FormError style={styles.formItemError} name="preCondition" />
              </div>

              <div style={styles.formItem}>
                <span style={styles.formItemLabel}>{this.props.intl.messages['test.createInterface.testSteps']}</span>
                <FormBinder name="steps" >
                  <Input multiple style={styles.formCommonWidth}/>
                </FormBinder>
                <FormError style={styles.formItemError} name="steps" />
              </div>

              <div style={styles.formItem}>
                <span style={styles.formItemLabel}>{this.props.intl.messages['test.createInterface.expectedResult']}</span>
                <FormBinder name="predicateResult" >
                  <Input multiple style={styles.formCommonWidth}/>
                </FormBinder>
                <FormError style={styles.formItemError} name="predicateResult" />
              </div>
            </div>
      </div>
    );
  }
}

export default injectIntl(DetailTable);
