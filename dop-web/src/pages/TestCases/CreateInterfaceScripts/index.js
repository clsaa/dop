import React, { Component } from 'react';
import StepForm from './components/StepForm';
import {FormBinderWrapper} from "@icedesign/form-binder";
import CaseParams from "./components/CaseParam/CaseParams";
import IceContainer from "@icedesign/container";
import API from "../../API";
import Axios from "axios";
import {Feedback} from "@icedesign/base";
import {injectIntl} from "react-intl";

const Toast = Feedback.toast;

class CreateInterfaceScripts extends Component {
  constructor(props) {
    super(props);
    const case_Id = this.props.match.params.caseId;
    this.state = {
      caseId: case_Id,
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
      ],
      caseDto: {
        id: case_Id,
        caseParams: []
      }
    };
  }

  addItem = () => {
    this.state.caseDto.caseParams.push({
      ref: '',
      value: '',
      caseId: this.state.caseId
    });
    this.setState({ caseDto: this.state.caseDto });
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

  removeItem = (index) => {
    this.state.caseDto.caseParams.splice(index, 1);
    this.setState({
      caseDto: this.state.caseDto
    });
  };

  validateFields = () => {
    let noError = true;
    this.refs.form.validateAll((errors, values) => {
      if (errors != null) {
        noError = false;
      }
    });
    if (noError) {
      let stageUrl = API.test + '/interfaceCases/stages';
      let url = API.test + '/interfaceCases/caseParams';
      let _this = this;
      Axios.post(stageUrl, this.state.stages)
          .then(function (response) {
            Toast.success(_this.props.intl.messages['test.createInterface.saveScript.message']);
            let caseParams = _this.state.caseDto.caseParams;
            Axios.post(url, caseParams)
                .then(function (response) {
                  Toast.success(_this.props.intl.messages['test.createInterface.saveParam.message']);
                  _this.props.history.push('/testCases');
                }).catch(function (error) {
              Toast.error(error);
              _this.props.history.push('/testCases');
            });
          }).catch(function (error) {
        console.log(error);
        Toast.error(error);
      });
    }
  };
  render() {
    return (
        <div className="create-interface-scripts-page">
          <IceContainer>
            <FormBinderWrapper
                value={this.state.caseDto}
                ref="form"
            >
              <CaseParams caseParams={this.state.caseDto.caseParams}
                          addItem={this.addItem.bind(this)} removeItem={this.removeItem.bind(this)}
                          submit={this.validateFields.bind(this)} disableSave={true}
              />
            </FormBinderWrapper>
          </IceContainer>

          <StepForm caseId={this.state.caseId} stages={this.state.stages}
                    operation='INSERT' btnText={this.props.intl.messages['test.createInterface.btnText']}
                    saveParams={this.validateFields}
                    addParam={this.addItemWithContent.bind(this)}
                    caseParams={this.state.caseDto.caseParams}
          />
        </div>
    );
  }
}

export default injectIntl(CreateInterfaceScripts);
