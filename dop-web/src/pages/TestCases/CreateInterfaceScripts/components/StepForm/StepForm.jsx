import React, { Component } from 'react';
import IceContainer from '@icedesign/container';
import {
  Grid,
  Step,
  Feedback, Icon, Button
} from '@icedesign/base';

import RequestStageForm from "./RequestStageForm";
import API from "../../../../API";
import Axios from "axios";
import {Link, withRouter} from "react-router-dom";
import {injectIntl, FormattedMessage} from 'react-intl';

const { Row, Col } = Grid;
const Toast = Feedback.toast;

class StepForm extends Component {
  static displayName = 'StepForm';

  static propTypes = {
  };

  static defaultProps = {
  };

  constructor(props) {
    super(props);
    const case_Id = this.props.caseId;
    this.state = {
      step: 0,
      caseId: case_Id,
      stages: this.props.stages,
      operation: this.props.operation,
      caseParams: this.props.caseParams
    };
  }

  componentWillReceiveProps(nextProps, nextContext) {
    this.setState({
      step: this.state.step,
      caseId: nextProps.caseId,
      stages: nextProps.stages,
      operation: nextProps.operation,
      caseParams: nextProps.caseParams
    });
  }

  nextStep = (stage) => {
    let newStep = this.state.step + 1;
    this.setState({
      step: newStep,
      stages: this.props.stages
    });
  };

  lastStep = (stage) => {
    let newStep = this.state.step - 1;
    this.setState({
      step: newStep,
      stages: this.props.stages
    });
  };

  postToServer = (stage) => {
    let _this = this;
    let stages = this.state.stages;
    if (this.state.operation === 'UPDATE') {
      let url = API.test + '/interfaceCases/' + this.state.caseId + '/stages';
      Axios.put(url, stages, {
        headers: {
          'Content-Type': 'application/json; charset=utf-8'
        }
      })
          .then(function (response) {
            Toast.success(_this.props.intl.messages['test.stepForm.saveTextSuccessfully']);
            _this.props.history.push('/testCases');
          }).catch(function (error) {
        console.log(error);
        Toast.error(error);
      });
    }else if (this.state.operation === 'INSERT') {
      this.props.saveParams();
    }
  };

  clickStep = (index) =>{
    this.setState({
      step: index
    })
  };

  addCaseParam(param){
    this.props.addParam(param);
  }

  renderStep = (step) => {
    if (step === 0) {
      return <RequestStageForm data={this.state.stages[step]} onSubmit={this.nextStep.bind(this)}
                               onLast={this.lastStep.bind(this)}
                               add_caseParam={this.addCaseParam.bind(this)}
                               caseParams={this.state.caseParams}
                               stage='PREPARE' caseId={this.state.caseId} isSubmit={false}/>;
    }

    if (step === 1) {
      return <RequestStageForm data={this.state.stages[step]} onSubmit={this.nextStep.bind(this)} onLast={this.lastStep.bind(this)}
                               add_caseParam={this.addCaseParam.bind(this)}
                               caseParams={this.state.caseParams}
                               stage='TEST' caseId={this.state.caseId} isSubmit={false}/>;
    }

    if (step === 2) {
      return <RequestStageForm data={this.state.stages[step]} onSubmit={this.postToServer.bind(this)}  onLast={this.lastStep.bind(this)}
                               add_caseParam={this.addCaseParam.bind(this)}
                               caseParams={this.state.caseParams}
                               stage='DESTROY' caseId={this.state.caseId} isSubmit={false}/>;
    }
  };

  render() {
    return (
      <div className="step-form">
        <IceContainer title={this.props.intl.messages['test.stepForm.title']}>
          <Row wrap>
            <Col xxs="24" s="5" l="5" style={styles.formLabel}>
              <Step
                current={this.state.step}
                direction="vertical"
                type="dot"
                animation={false}
                style={styles.step}
              >
                <Step.Item title={this.props.intl.messages['test.stepForm.step.prepare']} content="" onClick={this.clickStep.bind(this)}/>
                <Step.Item title={this.props.intl.messages['test.stepForm.step.test']} content="" onClick={this.clickStep.bind(this)}/>
                <Step.Item title={this.props.intl.messages['test.stepForm.step.destroy']} content="" onClick={this.clickStep.bind(this)}/>
              </Step>
            </Col>

            <Col xxs="24" s="18" l="18">
              {this.renderStep(this.state.step)}
            </Col>
          </Row>

          <Row>
            <Col>
              <Button type="secondary" onClick={this.postToServer}>
                <Icon type="success" size='xxl'/>
                {this.props.btnText}
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
  step: {
    marginBottom: '20px',
  },
  content: {
    height: '200px',
    justifyContent: 'center',
    alignItems: 'center',
    display: 'flex',
  },
  icon: {
    color: '#1DC11D',
    marginRight: '10px',
  },
};

export default injectIntl(withRouter(StepForm));
