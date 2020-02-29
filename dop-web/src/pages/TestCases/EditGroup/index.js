import React, { Component } from 'react';
import API from "../../API";
import Axios from "axios";
import {Feedback} from "@icedesign/base";
import GroupInfo from "../CreateGroup/GroupInfo";


const Toast = Feedback.toast;

export default class EditGroup extends Component {
    constructor(props) {
        super(props);
        const group_id = this.props.match.params.groupId;
        this.state = {
            groupId: group_id,
            groupDto: {
                caseUnits: [],
            }
        };
        this.doGetCase(group_id);
    }

    componentWillReceiveProps(nextProps, nextContext) {
        if (nextProps.match.params.caseId !== this.state.caseId) {
            let newCaseId = nextProps.match.params.caseId;
            this.doGetCase(newCaseId);
        }
    }

    doGetCase(groupId) {
        let url = API.test + '/group/' + groupId;
        let _this = this;
        Axios.get(url).then(function (response) {
            let group_dto = response.data;
            if (group_dto) {
                let caseUnits = group_dto.caseUnits;
                let newUnits = [];
                caseUnits.forEach(caseUnit => {
                    newUnits.push(JSON.stringify(caseUnit));
                });
                group_dto.caseUnits = newUnits;
                _this.setState({
                    groupDto: group_dto
                });
            }else {
                Toast.error("No case group info!");
            }
        }).catch(function (error) {
            Toast.error(error);
        });
    }

    addItem = () => {
        let newUnit = {
            "caseId": '',
            "caseType": 'MANUAL',
            "caseName": ''
        };
        this.state.groupDto.caseUnits.push(JSON.stringify(newUnit));
        this.setState({
            groupDto: this.state.groupDto
        });
    };

    removeItem = (index) => {
        this.state.groupDto.caseUnits.splice(index, 1);
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
                    Toast.success("修改成功！");
                    _this.doGetCase(_this.state.caseId);
                }).catch(function (error) {
                console.log(error);
                Toast.error(error);
            });
        }
    };

    addItemWithContent (caseParam) {
        this.state.caseDto.caseParams.push(caseParam);
        this.setState({caseDto: this.state.caseDto});
    };

    render() {
        return (
            <div className="edit-group-info-page">
                <GroupInfo groupDto={this.state.groupDto} operation='UPDATE'/>
            </div>
        );
    }
}
