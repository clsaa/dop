import React, {Component} from 'react';
import '../Styles.scss'
import {injectIntl} from "react-intl";


class Deployment extends Component{
    render(){
        return (
            <div>
                <h3 className="chosen-task-detail-title">{this.props.intl.messages["pipeline.info.step.deploy.title"]}</h3>
                <div
                    className="chosen-task-detail-body">
                    {this.props.intl.messages["pipeline.info.step.deploy.tip"]}
                </div>
            </div>
        )
    }
}
export default injectIntl(Deployment)
