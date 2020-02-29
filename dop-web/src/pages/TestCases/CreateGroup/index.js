import {Feedback} from "@icedesign/base";
import React, { Component } from 'react';
import GroupInfo from "./GroupInfo";

const Toast = Feedback.toast;

export default class CreateGroup extends Component{

    constructor(props) {
        super(props);
        this.state = {
            groupDto: {
                appId: '',
                groupName: '',
                comment: '',
                executeWay: 'PARALLEL',
                caseUnits: []
            }
        }
    }

    render() {
        return (
            <div className="create-group-page">
                <GroupInfo groupDto={this.state.groupDto} operation='INSERT'/>
            </div>
        );
    }
}
