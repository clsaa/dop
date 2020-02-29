import React, {Component} from 'react';
import '../Styles.scss'
import {injectIntl} from "react-intl";


class Maven extends Component{
    render(){
        return (
            <div>
                <h3 className="chosen-task-detail-title">{this.props.intl.messages["pipeline.info.step.maven.title"]}</h3>
                <div
                    className="chosen-task-detail-body">
                    {this.props.intl.messages["pipeline.info.step.maven.tip"]+": "}<br/><br/>
                    {this.props.intl.messages["pipeline.info.step.maven.version"]}<br/>
                    {this.props.intl.messages["pipeline.info.step.maven.clean"]}<br/>
                </div>
            </div>
        )
    }
}
export default injectIntl(Maven)
