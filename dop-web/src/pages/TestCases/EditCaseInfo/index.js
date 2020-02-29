import React, { Component } from 'react';
import DetailTable from './components/DetailTable';
import StepForm from "../CreateInterfaceScripts/components/StepForm/StepForm";
import API from "../../API";
import Axios from "axios";
import {Feedback} from "@icedesign/base";
import CaseParams from "../CreateInterfaceScripts/components/CaseParam/CaseParams";
import {FormBinderWrapper} from "@icedesign/form-binder";
import IceContainer from '@icedesign/container/lib/index';
import {injectIntl, FormattedMessage} from 'react-intl';

const Toast = Feedback.toast;

class EditCaseInfo extends Component {
  constructor(props) {
    super(props);
    const case_Id = this.props.match.params.caseId;
    this.state = {
      caseId: case_Id,
      caseDto: {
        caseParams: [],
        stages: [
          {
            caseId: case_Id,
            operations: [],
            stage: 'PREPARE',
            requestScripts: [],
            waitOperations: []
          },
          {
            caseId: case_Id,
            operations: [],
            stage: 'TEST',
            requestScripts: [],
            waitOperations: []
          },
          {
            caseId: case_Id,
            operations: [],
            stage: 'DESTROY',
            requestScripts: [],
            waitOperations: []
          }
        ]
      }
    };

    this.doGetCase(case_Id);
  }

  componentWillReceiveProps(nextProps, nextContext) {
    if (nextProps.match.params.caseId !== this.state.caseId) {
      let newCaseId = nextProps.match.params.caseId;
      this.doGetCase(newCaseId);
    }
  }

  doGetCase(caseID) {
    let url = API.test + '/interfaceCases/' + caseID;
    let _this = this;
    Axios.get(url).then(function (response) {
      let caseDto = response.data;
      if (caseDto) {
        let newStages = _this.doTrans(caseDto);
        _this.setState({
          stages: newStages,
          caseDto: caseDto
        });
      }else {
        Toast.error("No interface case info!");
      }
    }).catch(function (error) {
      Toast.error(error);
    });
  }

  doTrans (caseDto) {
    return caseDto.stages;
  };

  addItem = () => {
    this.state.caseDto.caseParams.push({
      ref: '',
      value: '',
      caseId: this.state.caseId
    });
    this.setState({ caseDto: this.state.caseDto });
  };

  removeItem = (index) => {
    this.state.caseDto.caseParams.splice(index, 1);
    this.setState({
      caseDto: this.state.caseDto
    });
  };

  validateFields = () => {
    let noError = true;
    this.refs.caseInfo.validateAll((errors, values) => {
      if (errors != null) {
        noError = false;
      }
    });
    this.refs.form.validateAll((errors, values) => {
      if (errors != null) {
        noError = false;
      }
    });
    let _this = this;
    if (noError) {
      let caseDto = this.state.caseDto;
      let url = API.test + '/interfaceCases';
      Axios.put(url, caseDto)
          .then(function (response) {
            Toast.success(_this.props.intl.messages['test.editCase.info.successInfo']);
            // _this.doGetCase(_this.state.caseId);
          }).catch(function (error) {
        console.log(error);
        Toast.error(error);
      });
    }
  };

  addItemWithContent (caseParam) {
    let data = this.state.caseDto.caseParams;
    let contains = null;
    data.forEach((param) => {
      if (param.ref === caseParam.ref) {
        contains = true;
      }
    });

    if (!contains) {
      this.state.caseDto.caseParams.push(caseParam);
      this.setState({caseDto: this.state.caseDto});
    }
  };

  render() {
    return (
        <div className="edit-case-info-page">
          <IceContainer title={this.props.intl.messages['test.editCase.info.title']}>
            <FormBinderWrapper
                value={this.state.caseDto}
                ref="caseInfo"
            >
              <DetailTable caseDto={this.state.caseDto}/>
            </FormBinderWrapper>

            <FormBinderWrapper
                value={this.state.caseDto}
                ref="form"
            >
              <CaseParams caseParams={this.state.caseDto.caseParams} caseDto={this.state.caseDto}
                          addItem={this.addItem.bind(this)} removeItem={this.removeItem.bind(this)}
                          submit={this.validateFields.bind(this)}
              />
            </FormBinderWrapper>
          </IceContainer>

          <StepForm caseId={this.state.caseId} stages={this.state.caseDto.stages}
                    operation='UPDATE' btnText={this.props.intl.messages['test.editCase.info.saveBtnText']}
                    addParam={this.addItemWithContent.bind(this)}
                    caseParams={this.state.caseDto.caseParams}
          />
        </div>
    );
  }
}
export default injectIntl(EditCaseInfo);
